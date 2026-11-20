package controller;

import dao.CamiaoDAO;
import dao.CarrinhaDAO;
import dao.CarregamentoDAO;
import dao.ClienteDAO;
import dao.ProprietarioDAO;
import dataStructure.ListaDuplamenteLigada;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Alert;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import model.Camiao;
import model.Carrinha;
import model.Carregamento;
import model.Cliente;
import model.Proprietario;
import model.Veiculo;

public class DashboardViewController {

    private static final Logger LOGGER = Logger.getLogger(DashboardViewController.class.getName());

    @FXML private Label lblVeiculos;
    @FXML private Label lblVeiculosRodape;
    @FXML private Label lblProprietarios;
    @FXML private Label lblProprietariosRodape;
    @FXML private Label lblClientes;
    @FXML private Label lblClientesRodape;
    @FXML private Label lblCarregamentos;
    @FXML private Label lblCarregamentosRodape;
    @FXML private Label lblCargaTransportada;
    @FXML private Label lblCargaRodape;
    @FXML private Label lblArrecadado;
    @FXML private Label lblArrecadadoRodape;

    @FXML private Label lblVeiculoMesMatricula;
    @FXML private Label lblVeiculoMesDetalhes;
    @FXML private Label lblVeiculoMesReceita;

    @FXML private BarChart<String, Number> graficoCarregamentos;
    @FXML private DatePicker dpCalendario;
    @FXML private Label lblDataSelecionada;
    @FXML private Label lblResumoData;

    private ListaDuplamenteLigada<Carregamento> todosCarregamentos;

    @FXML
    public void initialize() {
        carregarEstatisticas();
        carregarGrafico();
        configurarDatePicker();
    }

    private void configurarDatePicker() {
        if (dpCalendario != null) {
            dpCalendario.setValue(LocalDate.now());
            filtrarPorData(null);
        }
    }

    @FXML
    void filtrarPorData(ActionEvent event) {
        if (dpCalendario == null || lblDataSelecionada == null || lblResumoData == null) {
            return;
        }

        LocalDate data = dpCalendario.getValue();
        if (data == null) {
            lblDataSelecionada.setText("Data seleccionada: Nenhuma");
            lblResumoData.setText("Por favor, seleccione uma data válida.");
            return;
        }

        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        lblDataSelecionada.setText("Data: " + data.format(fmt));

        int qtdViagens = 0;
        double totalToneladas = 0.0;
        double totalValor = 0.0;

        if (todosCarregamentos != null) {
            for (Carregamento c : todosCarregamentos) {
                if (c != null && data.equals(c.getData())) {
                    qtdViagens++;
                    qtdViagensTotal(c);
                    totalToneladas += c.getQuantidade();
                    totalValor += c.getValorCobrado();
                }
            }
        }

        if (qtdViagens == 0) {
            lblResumoData.setText("Nenhum carregamento realizado nesta data.");
        } else {
            lblResumoData.setText(String.format("%d viagem(ns) · %.1f t transportadas · %.2f MZN",
                    qtdViagens, totalToneladas, totalValor));
        }
    }

    private void qtdViagensTotal(Carregamento c) {
        // Método auxiliar
    }

    private void carregarEstatisticas() {
        try {
            ListaDuplamenteLigada<Camiao> camioes = new CamiaoDAO().findAll();
            ListaDuplamenteLigada<Carrinha> carrinhas = new CarrinhaDAO().findAll();
            ListaDuplamenteLigada<Proprietario> props = new ProprietarioDAO().findAll();
            ListaDuplamenteLigada<Cliente> clientes = new ClienteDAO().findAll();
            todosCarregamentos = new CarregamentoDAO().findAll();

            int totalCamioes = camioes != null ? camioes.getTamanho() : 0;
            int totalCarrinhas = carrinhas != null ? carrinhas.getTamanho() : 0;
            int totalVeiculos = totalCamioes + totalCarrinhas;
            int totalProps = props != null ? props.getTamanho() : 0;
            int totalCli = clientes != null ? clientes.getTamanho() : 0;
            int totalCarreg = todosCarregamentos != null ? todosCarregamentos.getTamanho() : 0;

            if (lblVeiculos != null) {
                lblVeiculos.setText(String.valueOf(totalVeiculos));
            }
            if (lblVeiculosRodape != null) {
                lblVeiculosRodape.setText(totalCamioes + " camiões · " + totalCarrinhas + " carrinhas");
            }
            if (lblProprietarios != null) {
                lblProprietarios.setText(String.valueOf(totalProps));
            }
            if (lblClientes != null) {
                lblClientes.setText(String.valueOf(totalCli));
            }
            if (lblCarregamentos != null) {
                lblCarregamentos.setText(String.valueOf(totalCarreg));
            }

            double totalCarga = 0;
            double totalValor = 0;

            // Map para encontrar o veículo com maior facturação
            Map<String, Double> receitaPorVeiculo = new HashMap<>();
            Map<String, Veiculo> objVeiculoPorMatricula = new HashMap<>();

            if (todosCarregamentos != null) {
                for (Carregamento c : todosCarregamentos) {
                    if (c == null) continue;
                    totalCarga += c.getQuantidade();
                    totalValor += c.getValorCobrado();

                    if (c.getVeiculo() != null && c.getVeiculo().getMatricula() != null) {
                        String mat = c.getVeiculo().getMatricula();
                        receitaPorVeiculo.put(mat, receitaPorVeiculo.getOrDefault(mat, 0.0) + c.getValorCobrado());
                        objVeiculoPorMatricula.put(mat, c.getVeiculo());
                    }
                }
            }

            if (lblCargaTransportada != null) {
                lblCargaTransportada.setText(String.format("%.1f t", totalCarga));
            }
            if (lblArrecadado != null) {
                lblArrecadado.setText(String.format("%.2f MZN", totalValor));
            }

            // Veículo destaque (maior receita)
            String melhorMatricula = null;
            double maiorReceita = -1.0;
            for (Map.Entry<String, Double> entry : receitaPorVeiculo.entrySet()) {
                if (entry.getValue() > maiorReceita) {
                    maiorReceita = entry.getValue();
                    melhorMatricula = entry.getKey();
                }
            }

            if (lblVeiculoMesMatricula != null && lblVeiculoMesDetalhes != null && lblVeiculoMesReceita != null) {
                if (melhorMatricula != null) {
                    Veiculo v = objVeiculoPorMatricula.get(melhorMatricula);
                    lblVeiculoMesMatricula.setText(melhorMatricula);
                    String propNome = (v != null && v.getProprietario() != null) ? v.getProprietario().getNome() : "Sem proprietário";
                    String cap = (v != null) ? v.getCapacidadeCarga() + " t" : "";
                    lblVeiculoMesDetalhes.setText(cap + " · " + propNome);
                    lblVeiculoMesReceita.setText(String.format("Receita: %.2f MZN", maiorReceita));
                } else {
                    lblVeiculoMesMatricula.setText("—");
                    lblVeiculoMesDetalhes.setText("Sem viagens registadas");
                    lblVeiculoMesReceita.setText("0,00 MZN");
                }
            }

        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Erro ao carregar dados do dashboard", e);
            Alert alert = new Alert(Alert.AlertType.ERROR, "Erro ao carregar dados do dashboard: " + e.getMessage());
            alert.setHeaderText("Erro");
            alert.showAndWait();
        }
    }

    private void carregarGrafico() {
        if (graficoCarregamentos == null) return;
        graficoCarregamentos.getData().clear();

        XYChart.Series<String, Number> serieCamioes = new XYChart.Series<>();
        serieCamioes.setName("Camiões");

        XYChart.Series<String, Number> serieCarrinhas = new XYChart.Series<>();
        serieCarrinhas.setName("Carrinhas");

        String[] meses = {"Jan", "Fev", "Mar", "Abr", "Mai", "Jun", "Jul", "Ago", "Set", "Out", "Nov", "Dez"};
        int[] contagemCamioes = new int[12];
        int[] contagemCarrinhas = new int[12];

        if (todosCarregamentos != null) {
            for (Carregamento c : todosCarregamentos) {
                if (c != null && c.getData() != null) {
                    int mesIdx = c.getData().getMonthValue() - 1; // 0..11
                    if (mesIdx >= 0 && mesIdx < 12) {
                        if (c.getVeiculo() instanceof Camiao) {
                            contagemCamioes[mesIdx]++;
                        } else if (c.getVeiculo() instanceof Carrinha) {
                            contagemCarrinhas[mesIdx]++;
                        } else {
                            // Se o tipo base for genérico, contabiliza em camião
                            contagemCamioes[mesIdx]++;
                        }
                    }
                }
            }
        }

        // Adiciona apenas os meses do ano actual com valores reais (zero ou mais)
        int mesAtual = LocalDate.now().getMonthValue();
        for (int i = 0; i < mesAtual; i++) {
            serieCamioes.getData().add(new XYChart.Data<>(meses[i], contagemCamioes[i]));
            serieCarrinhas.getData().add(new XYChart.Data<>(meses[i], contagemCarrinhas[i]));
        }

        graficoCarregamentos.getData().addAll(serieCamioes, serieCarrinhas);
    }
}
