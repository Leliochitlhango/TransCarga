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
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import model.Camiao;
import model.Carrinha;
import model.Carregamento;
import model.Proprietario;

public class TransportesPorProprietarioController {

    private static final Logger LOGGER = Logger.getLogger(TransportesPorProprietarioController.class.getName());

    private final ListaDuplamenteLigada<Carregamento> dadosTabela = new ListaDuplamenteLigada<>();
    private final ObservableList<Carregamento> observavel = FXCollections.observableArrayList();
    private ListaDuplamenteLigada<Proprietario> proprietarios;
    private ListaDuplamenteLigada<Carregamento> todosCarregamentos;
    private ListaDuplamenteLigada<Camiao> camioes;
    private ListaDuplamenteLigada<Carrinha> carrinhas;

    @FXML private ComboBox<String> cbProprietario;
    @FXML private TextField txtPesquisa;

    @FXML private TableColumn<Carregamento, String> colCliente;
    @FXML private TableColumn<Carregamento, LocalDate> colData;
    @FXML private TableColumn<Carregamento, String> colDestino;
    @FXML private TableColumn<Carregamento, String> colOrigem;
    @FXML private TableColumn<Carregamento, Double> colQuantidade;
    @FXML private TableColumn<Carregamento, Double> colValor;
    @FXML private TableColumn<Carregamento, String> colVeiculo;

    @FXML private Label lblMensagem;
    @FXML private Label lblNumServicos;
    @FXML private Label lblTotal;
    @FXML private TableView<Carregamento> tabela;

    // Lado Direito: Consulta por Veículo
    @FXML private ComboBox<String> cbVeiculoConsulta;
    @FXML private Label lblTotalVeiculoValor;
    @FXML private Label lblTotalVeiculoServicos;
    @FXML private BarChart<String, Number> graficoVeiculo;

    @FXML
    public void initialize() {
        carregarDados();
        configurarTabela();
        configurarComboProprietarios();
        configurarComboVeiculos();
        configurarFiltroPesquisa();
        filtrarTabela();
    }

    private void carregarDados() {
        try {
            proprietarios = new ProprietarioDAO().findAll();
            todosCarregamentos = new CarregamentoDAO().findAll();
            camioes = new CamiaoDAO().findAll();
            carrinhas = new CarrinhaDAO().findAll();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Erro ao carregar transportes por proprietário", e);
            javafx.scene.control.Alert alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.ERROR, "Erro ao carregar dados: " + e.getMessage());
            alert.setHeaderText("Erro");
            alert.showAndWait();
        }
    }

    private void configurarTabela() {
        colData.setCellValueFactory(c -> new SimpleObjectProperty<>(c.getValue().getData()));
        colCliente.setCellValueFactory(c -> new SimpleStringProperty(
                c.getValue().getCliente() != null ? c.getValue().getCliente().getNome() : "Não informado"));
        colVeiculo.setCellValueFactory(c -> new SimpleStringProperty(
                c.getValue().getVeiculo() != null ? c.getValue().getVeiculo().getMatricula() : "Não informado"));
        colDestino.setCellValueFactory(new PropertyValueFactory<>("locaDestino"));
        colOrigem.setCellValueFactory(new PropertyValueFactory<>("localOrigem"));
        colQuantidade.setCellValueFactory(new PropertyValueFactory<>("quantidade"));
        colValor.setCellValueFactory(new PropertyValueFactory<>("valorCobrado"));

        tabela.setItems(observavel);
    }

    private void configurarComboProprietarios() {
        if (cbProprietario == null) return;
        cbProprietario.getItems().clear();
        cbProprietario.getItems().add("Todos os proprietários");

        if (proprietarios != null) {
            for (Proprietario p : proprietarios) {
                if (p != null && p.getNome() != null) {
                    cbProprietario.getItems().add(p.getNome());
                }
            }
        }
        cbProprietario.setValue("Todos os proprietários");
    }

    private void configurarComboVeiculos() {
        if (cbVeiculoConsulta == null) return;
        cbVeiculoConsulta.getItems().clear();

        if (camioes != null) {
            for (Camiao c : camioes) {
                if (c != null && c.getMatricula() != null) {
                    cbVeiculoConsulta.getItems().add(c.getMatricula());
                }
            }
        }
        if (carrinhas != null) {
            for (Carrinha c : carrinhas) {
                if (c != null && c.getMatricula() != null) {
                    cbVeiculoConsulta.getItems().add(c.getMatricula());
                }
            }
        }
    }

    private void configurarFiltroPesquisa() {
        if (txtPesquisa != null) {
            txtPesquisa.textProperty().addListener((obs, oldVal, newVal) -> {
                filtrarTabela();
            });
        }
    }

    @FXML
    void proprietarioSelecionado(ActionEvent event) {
        filtrarTabela();
    }

    private void filtrarTabela() {
        dadosTabela.clear();

        String propSelecionado = cbProprietario != null ? cbProprietario.getValue() : null;
        boolean filtrarPorProp = propSelecionado != null && !propSelecionado.equals("Todos os proprietários");

        String termo = txtPesquisa != null && txtPesquisa.getText() != null ? 
                txtPesquisa.getText().trim().toLowerCase() : "";

        int qtdServicos = 0;
        double subtotal = 0.0;

        if (todosCarregamentos != null) {
            for (Carregamento c : todosCarregamentos) {
                if (c == null) continue;

                // Checar filtro de proprietário
                if (filtrarPorProp) {
                    if (c.getVeiculo() == null || c.getVeiculo().getProprietario() == null) {
                        continue;
                    }
                    if (!propSelecionado.equalsIgnoreCase(c.getVeiculo().getProprietario().getNome())) {
                        continue;
                    }
                }

                // Checar filtro de texto em todos os campos
                if (!termo.isEmpty()) {
                    boolean matchMatricula = c.getVeiculo() != null && c.getVeiculo().getMatricula() != null && 
                            c.getVeiculo().getMatricula().toLowerCase().contains(termo);
                    boolean matchCliente = c.getCliente() != null && c.getCliente().getNome() != null && 
                            c.getCliente().getNome().toLowerCase().contains(termo);
                    boolean matchOrigem = c.getLocalOrigem() != null && c.getLocalOrigem().toLowerCase().contains(termo);
                    boolean matchDestino = c.getLocaDestino() != null && c.getLocaDestino().toLowerCase().contains(termo);
                    boolean matchProp = c.getVeiculo() != null && c.getVeiculo().getProprietario() != null && 
                            c.getVeiculo().getProprietario().getNome() != null && 
                            c.getVeiculo().getProprietario().getNome().toLowerCase().contains(termo);
                    boolean matchData = c.getData() != null && c.getData().toString().contains(termo);

                    if (!matchMatricula && !matchCliente && !matchOrigem && !matchDestino && !matchProp && !matchData) {
                        continue;
                    }
                }

                dadosTabela.add(c);
                qtdServicos++;
                subtotal += c.getValorCobrado();
            }
        }

        if (lblNumServicos != null) {
            lblNumServicos.setText(qtdServicos + " serviço(s)");
        }
        if (lblTotal != null) {
            lblTotal.setText(String.format("Subtotal: %.2f MZN", subtotal));
        }
        if (lblMensagem != null) {
            lblMensagem.setText(qtdServicos == 0 ? "Nenhum resultado encontrado para os filtros actuais." : "");
        }
        observavel.setAll(dadosTabela);
    }

    @FXML
    void veiculoSelecionado(ActionEvent event) {
        String matricula = cbVeiculoConsulta.getValue();
        if (matricula == null || matricula.trim().isEmpty()) {
            return;
        }

        int qtdServicos = 0;
        double totalValor = 0.0;
        int[] viagensPorMes = new int[12];
        String[] meses = {"Jan", "Fev", "Mar", "Abr", "Mai", "Jun", "Jul", "Ago", "Set", "Out", "Nov", "Dez"};

        if (todosCarregamentos != null) {
            for (Carregamento c : todosCarregamentos) {
                if (c != null && c.getVeiculo() != null && matricula.equalsIgnoreCase(c.getVeiculo().getMatricula())) {
                    qtdServicos++;
                    totalValor += c.getValorCobrado();
                    if (c.getData() != null) {
                        int m = c.getData().getMonthValue() - 1;
                        if (m >= 0 && m < 12) viagensPorMes[m]++;
                    }
                }
            }
        }

        if (lblTotalVeiculoValor != null) {
            lblTotalVeiculoValor.setText(String.format("%.2f MZN", totalValor));
        }
        if (lblTotalVeiculoServicos != null) {
            lblTotalVeiculoServicos.setText(qtdServicos + " serviço(s) realizado(s)");
        }

        if (graficoVeiculo != null) {
            graficoVeiculo.getData().clear();
            XYChart.Series<String, Number> serie = new XYChart.Series<>();
            serie.setName(matricula);
            int mesAtual = LocalDate.now().getMonthValue();
            for (int i = 0; i < mesAtual; i++) {
                serie.getData().add(new XYChart.Data<>(meses[i], viagensPorMes[i]));
            }
            graficoVeiculo.getData().add(serie);
        }
    }
}
