package controller;

import dao.CamiaoDAO;
import dao.CarrinhaDAO;
import dao.CarregamentoDAO;
import dataStructure.ListaDuplamenteLigada;
import java.time.LocalDate;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import model.Camiao;
import model.Carrinha;
import model.Carregamento;
import util.ExportadorRelatorio;

public class TotalPorVeiculoViewController {

    private static final Logger LOGGER = Logger.getLogger(TotalPorVeiculoViewController.class.getName());

    private final ListaDuplamenteLigada<Carregamento> dadosTabela = new ListaDuplamenteLigada<>();
    private final ObservableList<Carregamento> observavel = FXCollections.observableArrayList();
    private ListaDuplamenteLigada<Camiao> camioes;
    private ListaDuplamenteLigada<Carrinha> carrinhas;
    private ListaDuplamenteLigada<Carregamento> todosCarregamentos;

    @FXML private Button btnExportar;
    @FXML private Button btnVerDetalhes;
    @FXML private ComboBox<String> cbVeiculo;
    @FXML private TextField txtPesquisa;

    @FXML private TableColumn<Carregamento, String> colCliente;
    @FXML private TableColumn<Carregamento, LocalDate> colData;
    @FXML private TableColumn<Carregamento, String> colDestino;
    @FXML private TableColumn<Carregamento, String> colOrigem;
    @FXML private TableColumn<Carregamento, Double> colQuantidade;
    @FXML private TableColumn<Carregamento, Double> colValor;
    @FXML private TableColumn<Carregamento, String> colVeiculo;

    @FXML private Label lblNumServicos;
    @FXML private Label lblTotal;
    @FXML private Label lblTotalCarga;
    @FXML private TableView<Carregamento> tabela;

    @FXML
    public void initialize() {
        carregarDados();
        configurarTabela();
        configurarCombo();
        configurarPesquisa();
        atualizarFiltros();
    }

    private void carregarDados() {
        try {
            camioes = new CamiaoDAO().findAll();
            carrinhas = new CarrinhaDAO().findAll();
            todosCarregamentos = new CarregamentoDAO().findAll();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Erro ao carregar totais por veículo", e);
            Alert alert = new Alert(Alert.AlertType.ERROR, "Erro ao carregar dados: " + e.getMessage());
            alert.setHeaderText("Erro");
            alert.showAndWait();
        }
    }

    private void configurarCombo() {
        if (cbVeiculo == null) return;
        cbVeiculo.getItems().clear();
        cbVeiculo.getItems().add("Todos os veículos");

        if (camioes != null) {
            for (Camiao c : camioes) {
                if (c != null && c.getMatricula() != null) {
                    cbVeiculo.getItems().add(c.getMatricula() + " (Camião)");
                }
            }
        }
        if (carrinhas != null) {
            for (Carrinha c : carrinhas) {
                if (c != null && c.getMatricula() != null) {
                    cbVeiculo.getItems().add(c.getMatricula() + " (Carrinha)");
                }
            }
        }
        cbVeiculo.setValue("Todos os veículos");
    }

    private void configurarTabela() {
        colData.setCellValueFactory(c -> new SimpleObjectProperty<>(c.getValue().getData()));
        colVeiculo.setCellValueFactory(c -> new SimpleStringProperty(
                c.getValue().getVeiculo() != null ? c.getValue().getVeiculo().getMatricula() : ""));
        colCliente.setCellValueFactory(c -> new SimpleStringProperty(
                c.getValue().getCliente() != null ? c.getValue().getCliente().getNome() : ""));
        colDestino.setCellValueFactory(new PropertyValueFactory<>("locaDestino"));
        colOrigem.setCellValueFactory(new PropertyValueFactory<>("localOrigem"));
        colQuantidade.setCellValueFactory(new PropertyValueFactory<>("quantidade"));
        colValor.setCellValueFactory(new PropertyValueFactory<>("valorCobrado"));

        tabela.setItems(observavel);
    }

    private void configurarPesquisa() {
        if (txtPesquisa != null) {
            txtPesquisa.textProperty().addListener((obs, oldVal, newVal) -> {
                atualizarFiltros();
            });
        }
    }

    @FXML
    void consultar(ActionEvent event) {
        atualizarFiltros();
    }

    private void atualizarFiltros() {
        dadosTabela.clear();

        String veiculoSelecionado = cbVeiculo != null ? cbVeiculo.getValue() : null;
        boolean filtrarVeiculo = veiculoSelecionado != null && !veiculoSelecionado.equals("Todos os veículos");
        String matriculaAlvo = "";
        if (filtrarVeiculo) {
            matriculaAlvo = veiculoSelecionado.split(" ")[0].trim();
        }

        String termo = txtPesquisa != null && txtPesquisa.getText() != null ?
                txtPesquisa.getText().trim().toLowerCase() : "";

        int qtdServicos = 0;
        double totalValor = 0.0;
        double totalVolume = 0.0;

        if (todosCarregamentos != null) {
            for (Carregamento c : todosCarregamentos) {
                if (c == null) continue;

                if (filtrarVeiculo) {
                    if (c.getVeiculo() == null || c.getVeiculo().getMatricula() == null) continue;
                    if (!c.getVeiculo().getMatricula().equalsIgnoreCase(matriculaAlvo)) continue;
                }

                if (!termo.isEmpty()) {
                    boolean matchMat = c.getVeiculo() != null && c.getVeiculo().getMatricula() != null &&
                            c.getVeiculo().getMatricula().toLowerCase().contains(termo);
                    boolean matchCli = c.getCliente() != null && c.getCliente().getNome() != null &&
                            c.getCliente().getNome().toLowerCase().contains(termo);
                    boolean matchOrigem = c.getLocalOrigem() != null && c.getLocalOrigem().toLowerCase().contains(termo);
                    boolean matchDestino = c.getLocaDestino() != null && c.getLocaDestino().toLowerCase().contains(termo);

                    if (!matchMat && !matchCli && !matchOrigem && !matchDestino) {
                        continue;
                    }
                }

                dadosTabela.add(c);
                qtdServicos++;
                totalValor += c.getValorCobrado();
                if (c.getQuantidade() != null) totalVolume += c.getQuantidade();
            }
        }

        if (lblNumServicos != null) lblNumServicos.setText(String.valueOf(qtdServicos));
        if (lblTotalCarga != null) lblTotalCarga.setText(String.format("%.1f", totalVolume));
        if (lblTotal != null) lblTotal.setText(String.format("%.2f MZN", totalValor));
        observavel.setAll(dadosTabela);
    }

    @FXML
    void exportar(ActionEvent event) {
        Stage stage = (Stage) tabela.getScene().getWindow();
        String filtroDesc = cbVeiculo != null ? cbVeiculo.getValue() : "Geral";
        ExportadorRelatorio.exportarCarregamentos(stage, dadosTabela, filtroDesc);
    }

    @FXML
    void selecionarLinha(MouseEvent event) {
        if (event.getClickCount() == 2) {
            verDetalhes(null);
        }
    }

    @FXML
    void verDetalhes(ActionEvent event) {
        Carregamento c = tabela.getSelectionModel().getSelectedItem();
        if (c == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING, "Seleccione uma linha na tabela para visualizar os detalhes.");
            alert.setHeaderText("Aviso");
            alert.showAndWait();
            return;
        }

        String veiculoMat = c.getVeiculo() != null ? c.getVeiculo().getMatricula() : "—";
        String proprietario = (c.getVeiculo() != null && c.getVeiculo().getProprietario() != null) ?
                c.getVeiculo().getProprietario().getNome() : "—";
        String cliente = c.getCliente() != null ? c.getCliente().getNome() : "—";
        String contactoCliente = c.getCliente() != null ? c.getCliente().getContacto() : "—";
        String enderecoCliente = c.getCliente() != null ? c.getCliente().getEndereco() : "—";

        StringBuilder sb = new StringBuilder();
        sb.append("DETALHES DO SERVIÇO / CARREGAMENTO\n\n");
        sb.append("• ID do Serviço: #").append(c.getId()).append("\n");
        sb.append("• Data de Execução: ").append(c.getData()).append("\n");
        sb.append("• Veículo: ").append(veiculoMat).append("\n");
        sb.append("• Proprietário do Veículo: ").append(proprietario).append("\n");
        sb.append("• Cliente: ").append(cliente).append("\n");
        sb.append("• Contacto do Cliente: ").append(contactoCliente).append("\n");
        sb.append("• Endereço do Cliente: ").append(enderecoCliente).append("\n");
        sb.append("• Rota: ").append(c.getLocalOrigem()).append(" ➔ ").append(c.getLocaDestino()).append("\n");
        sb.append("• Quantidade / Lotação: ").append(c.getQuantidade()).append("\n");
        sb.append("• Valor Cobrado: ").append(String.format("%.2f MZN", c.getValorCobrado())).append("\n");

        Alert alert = new Alert(Alert.AlertType.INFORMATION, sb.toString());
        alert.setHeaderText("Ficha Detalhada do Transporte");
        alert.setTitle("TransCarga Moçambique");
        alert.showAndWait();
    }
}
