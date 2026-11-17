package controller;

import dao.CamiaoDAO;
import dao.CarrinhaDAO;
import dao.CarregamentoDAO;
import dao.ProprietarioDAO;
import dataStructure.ListaDuplamenteLigada;
import java.time.LocalDate;
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
import javafx.scene.control.cell.PropertyValueFactory;
import model.Camiao;
import model.Carrinha;
import model.Carregamento;
import model.Proprietario;

public class TransportesPorProprietarioController {

    private final ObservableList<Carregamento> dadosTabela = FXCollections.observableArrayList();
    private ListaDuplamenteLigada<Proprietario> proprietarios;
    private ListaDuplamenteLigada<Carregamento> todosCarregamentos;
    private ListaDuplamenteLigada<Camiao> camioes;
    private ListaDuplamenteLigada<Carrinha> carrinhas;

    @FXML private ComboBox<String> cbProprietario;
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
    }

    private void carregarDados() {
        try {
            proprietarios = new ProprietarioDAO().findAll();
            todosCarregamentos = new CarregamentoDAO().findAll();
            camioes = new CamiaoDAO().findAll();
            carrinhas = new CarrinhaDAO().findAll();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void configurarTabela() {
        colData.setCellValueFactory(c -> new SimpleObjectProperty<>(c.getValue().getData()));
        colCliente.setCellValueFactory(c -> new SimpleStringProperty(
                c.getValue().getCliente() != null ? c.getValue().getCliente().getNome() : ""));
        colVeiculo.setCellValueFactory(c -> new SimpleStringProperty(
                c.getValue().getVeiculo() != null ? c.getValue().getVeiculo().getMatricula() : ""));
        colDestino.setCellValueFactory(new PropertyValueFactory<>("locaDestino"));
        colOrigem.setCellValueFactory(new PropertyValueFactory<>("localOrigem"));
        colQuantidade.setCellValueFactory(new PropertyValueFactory<>("quantidade"));
        colValor.setCellValueFactory(new PropertyValueFactory<>("valorCobrado"));

        tabela.setItems(dadosTabela);
    }

    private void configurarComboProprietarios() {
        if (cbProprietario == null) return;
        cbProprietario.getItems().clear();

        if (proprietarios != null) {
            for (Proprietario p : proprietarios) {
                if (p != null && p.getNome() != null) {
                    cbProprietario.getItems().add(p.getNome());
                }
            }
        }
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

    @FXML
    void proprietarioSelecionado(ActionEvent event) {
        String propNome = cbProprietario.getValue();
        if (propNome == null || propNome.trim().isEmpty()) {
            return;
        }

        dadosTabela.clear();
        int qtdServicos = 0;
        double subtotal = 0.0;

        if (todosCarregamentos != null) {
            for (Carregamento c : todosCarregamentos) {
                if (c != null && c.getVeiculo() != null && c.getVeiculo().getProprietario() != null) {
                    if (propNome.equals(c.getVeiculo().getProprietario().getNome())) {
                        dadosTabela.add(c);
                        qtdServicos++;
                        subtotal += c.getValorCobrado();
                    }
                }
            }
        }

        if (lblNumServicos != null) {
            lblNumServicos.setText(qtdServicos + " serviço(s)");
        }
        if (lblTotal != null) {
            lblTotal.setText(String.format("Subtotal: %.2f MZN", subtotal));
        }
        if (lblMensagem != null) {
            lblMensagem.setText(qtdServicos == 0 ? "Nenhum transporte encontrado para este proprietário." : "");
        }
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
                if (c != null && c.getVeiculo() != null && matricula.equals(c.getVeiculo().getMatricula())) {
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

    @FXML
    void consultar(ActionEvent event) {
        proprietarioSelecionado(event);
    }
}
