package controller;

import dao.CamiaoDAO;
import dao.CarregamentoDAO;
import dataStructure.ListaDuplamenteLigada;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import model.Camiao;
import model.Carregamento;

public class ListaCamioesViewController {

    private final ListaDuplamenteLigada<Camiao> dados = new ListaDuplamenteLigada<>();
    private ListaDuplamenteLigada<Camiao> camioes;
    private ListaDuplamenteLigada<Carregamento> todosCarregamentos;

    @FXML private TableColumn<Camiao, Integer> colCapacidade;
    @FXML private TableColumn<Camiao, String> colDocIdenticacao;
    @FXML private TableColumn<Camiao, String> colMatricula;
    @FXML private TableColumn<Camiao, String> colMercadoria;
    @FXML private TableColumn<Camiao, String> colProprietario;
    @FXML private Label lblTotal;
    @FXML private TableView<Camiao> tabela;

    @FXML private TextField txtPesquisa;
    @FXML private Button btnVerDetalhes;

    @FXML 
    public void initialize() {
        carregarDados();
        configurarTabela();
        configurarPesquisa();
        atualizarFiltros();
    }

    private void carregarDados() {
        try {
            camioes = new CamiaoDAO().findAll();
            todosCarregamentos = new CarregamentoDAO().findAll();
            // Ordenação crescente por capacidade de carga diretamente na Lista Duplamente Ligada
            if (camioes != null) {
                camioes.ordenar((c1, c2) -> Integer.compare(c1.getCapacidadeCarga(), c2.getCapacidadeCarga()));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void configurarTabela() {
        colDocIdenticacao.setCellValueFactory(new PropertyValueFactory<>("documentoIdentifcacao"));
        colMatricula.setCellValueFactory(new PropertyValueFactory<>("matricula"));
        colMercadoria.setCellValueFactory(new PropertyValueFactory<>("tipoMercadoria"));
        colCapacidade.setCellValueFactory(new PropertyValueFactory<>("capacidadeCarga"));
        colProprietario.setCellValueFactory(c -> new SimpleStringProperty(
                c.getValue().getProprietario() != null ? c.getValue().getProprietario().getNome() : "Sem proprietário"));

        tabela.setItems(dados.asObservableList());
    }

    private void configurarPesquisa() {
        if (txtPesquisa != null) {
            txtPesquisa.textProperty().addListener((obs, oldVal, newVal) -> {
                atualizarFiltros();
            });
        }
    }

    private void atualizarFiltros() {
        dados.clear();
        String termo = txtPesquisa != null && txtPesquisa.getText() != null ?
                txtPesquisa.getText().trim().toLowerCase() : "";

        if (camioes != null) {
            for (Camiao c : camioes) {
                if (c == null) continue;
                if (!termo.isEmpty()) {
                    boolean matchMat = c.getMatricula() != null && c.getMatricula().toLowerCase().contains(termo);
                    boolean matchDoc = c.getDocumentoIdentifcacao() != null && c.getDocumentoIdentifcacao().toLowerCase().contains(termo);
                    boolean matchMerc = c.getTipoMercadoria() != null && c.getTipoMercadoria().toLowerCase().contains(termo);
                    boolean matchProp = c.getProprietario() != null && c.getProprietario().getNome() != null &&
                            c.getProprietario().getNome().toLowerCase().contains(termo);

                    if (!matchMat && !matchDoc && !matchMerc && !matchProp) {
                        continue;
                    }
                }
                dados.add(c);
            }
        }

        if (lblTotal != null) {
            lblTotal.setText("Total de Camiões: " + dados.getTamanho());
        }
    }

    @FXML
    void selecionarItem(MouseEvent event) {
        if (event.getClickCount() == 2) {
            verDetalhes(null);
        }
    }

    @FXML
    void verDetalhes(ActionEvent event) {
        Camiao c = tabela.getSelectionModel().getSelectedItem();
        if (c == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING, "Seleccione um camião na tabela para ver os detalhes.");
            alert.setHeaderText("Aviso");
            alert.showAndWait();
            return;
        }

        int viagens = 0;
        double faturado = 0.0;
        if (todosCarregamentos != null) {
            for (Carregamento car : todosCarregamentos) {
                if (car != null && car.getVeiculo() != null && c.getMatricula().equalsIgnoreCase(car.getVeiculo().getMatricula())) {
                    viagens++;
                    faturado += car.getValorCobrado();
                }
            }
        }

        String prop = c.getProprietario() != null ? c.getProprietario().getNome() : "Não atribuído";
        String contacto = c.getProprietario() != null ? c.getProprietario().getContacto() : "—";
        String docProp = c.getProprietario() != null ? c.getProprietario().getNumeroDocumentoIdentificacao() : "—";

        StringBuilder sb = new StringBuilder();
        sb.append("🚚 FICHA DO CAMIÃO DE CARGA\n\n");
        sb.append("• Matrícula: ").append(c.getMatricula()).append("\n");
        sb.append("• Nº de Chassi / Doc. ID: ").append(c.getDocumentoIdentifcacao()).append("\n");
        sb.append("• Tipo de Mercadoria: ").append(c.getTipoMercadoria() != null ? c.getTipoMercadoria() : "Carga Geral").append("\n");
        sb.append("• Capacidade Máxima: ").append(c.getCapacidadeCarga()).append(" toneladas\n");
        sb.append("• Número de Eixos: ").append(c.getNumeroEixos() != null ? c.getNumeroEixos() : "Padrão").append("\n\n");
        sb.append("PROPRIETÁRIO:\n");
        sb.append("• Nome: ").append(prop).append("\n");
        sb.append("• Contacto: ").append(contacto).append("\n");
        sb.append("• BI/NUIT: ").append(docProp).append("\n\n");
        sb.append("HISTÓRICO:\n");
        sb.append("• Total de Serviços Realizados: ").append(viagens).append("\n");
        sb.append("• Total Faturado: ").append(String.format("%.2f MZN", faturado)).append("\n");

        Alert alert = new Alert(Alert.AlertType.INFORMATION, sb.toString());
        alert.setHeaderText("Detalhes do Camião - TransCarga");
        alert.setTitle("TransCarga Moçambique");
        alert.showAndWait();
    }
}
