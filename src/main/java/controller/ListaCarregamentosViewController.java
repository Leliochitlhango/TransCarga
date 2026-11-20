package controller;

import dao.CamiaoDAO;
import dao.CarrinhaDAO;
import dao.CarregamentoDAO;
import dao.ProprietarioDAO;
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
import model.Proprietario;
import util.ExportadorRelatorio;

public class ListaCarregamentosViewController {

    private static final Logger LOGGER = Logger.getLogger(ListaCarregamentosViewController.class.getName());

    private final ListaDuplamenteLigada<Carregamento> dados = new ListaDuplamenteLigada<>();
    private final ObservableList<Carregamento> observavel = FXCollections.observableArrayList();
    private ListaDuplamenteLigada<Carregamento> carregamentos;
    private ListaDuplamenteLigada<Proprietario> proprietarios;
    private ListaDuplamenteLigada<Camiao> camioes;
    private ListaDuplamenteLigada<Carrinha> carrinhas;

    @FXML private Button btnExportar;
    @FXML private Button btnVerDetalhes;
    @FXML private ComboBox<String> cbProprietario;
    @FXML private ComboBox<String> cbVeiculo;
    @FXML private TextField txtPesquisa;

    @FXML private TableColumn<Carregamento, String> colCliente;
    @FXML private TableColumn<Carregamento, LocalDate> colData;
    @FXML private TableColumn<Carregamento, String> colDestino;
    @FXML private TableColumn<Carregamento, String> colOrigem;
    @FXML private TableColumn<Carregamento, String> colProprietario;
    @FXML private TableColumn<Carregamento, Double> colQuantidade;
    @FXML private TableColumn<Carregamento, Double> colValor;
    @FXML private TableColumn<Carregamento, String> colVeiculo;

    @FXML private Label lblNumServicos;
    @FXML private Label lblTotalGeral;

    @FXML private TableView<Carregamento> tabela;

    @FXML
    public void initialize() {
        carregarDados();
        configurarTabela();
        configurarCombos();
        configurarPesquisa();
        filtrar();
    }

    private void carregarDados() {
        try {
            carregamentos = new CarregamentoDAO().findAll();
            proprietarios = new ProprietarioDAO().findAll();
            camioes = new CamiaoDAO().findAll();
            carrinhas = new CarrinhaDAO().findAll();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Erro ao carregar histórico de carregamentos", e);
            Alert alert = new Alert(Alert.AlertType.ERROR, "Erro ao carregar carregamentos: " + e.getMessage());
            alert.setHeaderText("Erro");
            alert.showAndWait();
        }
    }

    private void configurarCombos() {
        if (cbProprietario != null) {
            cbProprietario.getItems().clear();
            cbProprietario.getItems().add("Todos");
            if (proprietarios != null) {
                for (Proprietario p : proprietarios) {
                    if (p != null && p.getNome() != null) cbProprietario.getItems().add(p.getNome());
                }
            }
            cbProprietario.setValue("Todos");
        }

        if (cbVeiculo != null) {
            cbVeiculo.getItems().clear();
            cbVeiculo.getItems().add("Todos");
            if (camioes != null) {
                for (Camiao c : camioes) {
                    if (c != null && c.getMatricula() != null) cbVeiculo.getItems().add(c.getMatricula());
                }
            }
            if (carrinhas != null) {
                for (Carrinha c : carrinhas) {
                    if (c != null && c.getMatricula() != null) cbVeiculo.getItems().add(c.getMatricula());
                }
            }
            cbVeiculo.setValue("Todos");
        }
    }

    private void configurarPesquisa() {
        if (txtPesquisa != null) {
            txtPesquisa.textProperty().addListener((obs, oldVal, newVal) -> {
                filtrar();
            });
        }
    }

    private void configurarTabela() {
        colData.setCellValueFactory(c -> new SimpleObjectProperty<>(c.getValue().getData()));
        colCliente.setCellValueFactory(c -> new SimpleStringProperty(
                c.getValue().getCliente() != null ? c.getValue().getCliente().getNome() : ""));
        colVeiculo.setCellValueFactory(c -> new SimpleStringProperty(
                c.getValue().getVeiculo() != null ? c.getValue().getVeiculo().getMatricula() : ""));
        colProprietario.setCellValueFactory(c -> new SimpleStringProperty(
                (c.getValue().getVeiculo() != null && c.getValue().getVeiculo().getProprietario() != null)
                        ? c.getValue().getVeiculo().getProprietario().getNome() : ""));
        colQuantidade.setCellValueFactory(new PropertyValueFactory<>("quantidade"));
        colOrigem.setCellValueFactory(new PropertyValueFactory<>("localOrigem"));
        colDestino.setCellValueFactory(new PropertyValueFactory<>("locaDestino"));
        colValor.setCellValueFactory(new PropertyValueFactory<>("valorCobrado"));

        tabela.setItems(observavel);
    }

    @FXML
    void filtrar() {
        dados.clear();
        String prop = cbProprietario != null ? cbProprietario.getValue() : "Todos";
        String veic = cbVeiculo != null ? cbVeiculo.getValue() : "Todos";
        String termo = txtPesquisa != null && txtPesquisa.getText() != null ? 
                txtPesquisa.getText().trim().toLowerCase() : "";

        double valorTotal = 0.0;
        int totalServicos = 0;

        if (carregamentos != null) {
            for (Carregamento c : carregamentos) {
                if (c == null) continue;

                if (prop != null && !prop.equals("Todos")) {
                    if (c.getVeiculo() == null || c.getVeiculo().getProprietario() == null) continue;
                    if (!prop.equalsIgnoreCase(c.getVeiculo().getProprietario().getNome())) continue;
                }

                if (veic != null && !veic.equals("Todos")) {
                    if (c.getVeiculo() == null || c.getVeiculo().getMatricula() == null) continue;
                    if (!veic.equalsIgnoreCase(c.getVeiculo().getMatricula())) continue;
                }

                if (!termo.isEmpty()) {
                    boolean mMat = c.getVeiculo() != null && c.getVeiculo().getMatricula() != null &&
                            c.getVeiculo().getMatricula().toLowerCase().contains(termo);
                    boolean mCli = c.getCliente() != null && c.getCliente().getNome() != null &&
                            c.getCliente().getNome().toLowerCase().contains(termo);
                    boolean mOrig = c.getLocalOrigem() != null && c.getLocalOrigem().toLowerCase().contains(termo);
                    boolean mDest = c.getLocaDestino() != null && c.getLocaDestino().toLowerCase().contains(termo);

                    if (!mMat && !mCli && !mOrig && !mDest) continue;
                }

                dados.add(c);
                totalServicos++;
                valorTotal += c.getValorCobrado();
            }
        }

        if (lblTotalGeral != null) {
            lblTotalGeral.setText(String.format("Total: %.2f MZN", valorTotal));
        }
        if (lblNumServicos != null) {
            lblNumServicos.setText("Serviços: " + totalServicos);
        }
        observavel.setAll(dados);
    }

    @FXML
    void exportar(ActionEvent event) {
        Stage stage = (Stage) tabela.getScene().getWindow();
        ExportadorRelatorio.exportarCarregamentos(stage, dados, "Histórico Geral de Carregamentos");
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

        StringBuilder sb = new StringBuilder();
        sb.append("FICHA DO CARREGAMENTO / SERVIÇO\n\n");
        sb.append("• ID do Serviço: #").append(c.getId()).append("\n");
        sb.append("• Data: ").append(c.getData()).append("\n");
        sb.append("• Veículo: ").append(veiculoMat).append("\n");
        sb.append("• Proprietário: ").append(proprietario).append("\n");
        sb.append("• Cliente: ").append(cliente).append("\n");
        sb.append("• Origem: ").append(c.getLocalOrigem()).append("\n");
        sb.append("• Destino: ").append(c.getLocaDestino()).append("\n");
        sb.append("• Quantidade / Passageiros: ").append(c.getQuantidade()).append("\n");
        sb.append("• Valor Cobrado: ").append(String.format("%.2f MZN", c.getValorCobrado())).append("\n");

        Alert alert = new Alert(Alert.AlertType.INFORMATION, sb.toString());
        alert.setHeaderText("Detalhes do Serviço");
        alert.setTitle("TransCarga Moçambique");
        alert.showAndWait();
    }
}
