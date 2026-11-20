package controller;

import dao.CarrinhaDAO;
import dao.CarregamentoDAO;
import dataStructure.ListaDuplamenteLigada;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
import model.Carrinha;
import model.Carregamento;

public class ListaCarrinhasViewController {

    private static final Logger LOGGER = Logger.getLogger(ListaCarrinhasViewController.class.getName());

    private final ListaDuplamenteLigada<Carrinha> dados = new ListaDuplamenteLigada<>();
    private final ObservableList<Carrinha> observavel = FXCollections.observableArrayList();
    private ListaDuplamenteLigada<Carrinha> carrinhas;
    private ListaDuplamenteLigada<Carregamento> todosCarregamentos;

    @FXML private TableColumn<Carrinha, Integer> colCapacidade;
    @FXML private TableColumn<Carrinha, String> colDocIdenticacao;
    @FXML private TableColumn<Carrinha, String> colMatricula;
    @FXML private TableColumn<Carrinha, Integer> colPassageiros;
    @FXML private TableColumn<Carrinha, String> colProprietario;
    @FXML private Label lblTotal;
    @FXML private TableView<Carrinha> tabela;

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
            carrinhas = new CarrinhaDAO().findAll();
            todosCarregamentos = new CarregamentoDAO().findAll();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Erro ao carregar carrinhas", e);
            Alert alert = new Alert(Alert.AlertType.ERROR, "Erro ao carregar carrinhas: " + e.getMessage());
            alert.setHeaderText("Erro");
            alert.showAndWait();
        }
    }

    private void configurarTabela() {
        colDocIdenticacao.setCellValueFactory(new PropertyValueFactory<>("documentoIdentifcacao"));
        colMatricula.setCellValueFactory(new PropertyValueFactory<>("matricula"));
        colPassageiros.setCellValueFactory(new PropertyValueFactory<>("passageiros"));
        colCapacidade.setCellValueFactory(new PropertyValueFactory<>("capacidadeCarga"));
        colProprietario.setCellValueFactory(c -> new SimpleStringProperty(
                c.getValue().getProprietario() != null ? c.getValue().getProprietario().getNome() : "Sem proprietário"));

        tabela.setItems(observavel);
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

        if (carrinhas != null) {
            for (Carrinha c : carrinhas) {
                if (c == null) continue;
                if (!termo.isEmpty()) {
                    boolean matchMat = c.getMatricula() != null && c.getMatricula().toLowerCase().contains(termo);
                    boolean matchDoc = c.getDocumentoIdentifcacao() != null && c.getDocumentoIdentifcacao().toLowerCase().contains(termo);
                    boolean matchCar = c.getTipoCarroceria() != null && c.getTipoCarroceria().toLowerCase().contains(termo);
                    boolean matchProp = c.getProprietario() != null && c.getProprietario().getNome() != null &&
                            c.getProprietario().getNome().toLowerCase().contains(termo);

                    if (!matchMat && !matchDoc && !matchCar && !matchProp) {
                        continue;
                    }
                }
                dados.add(c);
            }
        }

        if (lblTotal != null) {
            lblTotal.setText("Total de Carrinhas: " + dados.getTamanho());
        }
        observavel.setAll(dados);
    }

    @FXML
    void selecionarItem(MouseEvent event) {
        if (event.getClickCount() == 2) {
            verDetalhes(null);
        }
    }

    @FXML
    void verDetalhes(ActionEvent event) {
        Carrinha c = tabela.getSelectionModel().getSelectedItem();
        if (c == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING, "Seleccione uma carrinha na tabela para ver os detalhes.");
            alert.setHeaderText("Aviso");
            alert.showAndWait();
            return;
        }

        int viagens = 0;
        double facturado = 0.0;
        if (todosCarregamentos != null) {
            for (Carregamento car : todosCarregamentos) {
                if (car != null && car.getVeiculo() != null && c.getMatricula().equalsIgnoreCase(car.getVeiculo().getMatricula())) {
                    viagens++;
                    facturado += car.getValorCobrado();
                }
            }
        }

        String prop = c.getProprietario() != null ? c.getProprietario().getNome() : "Não atribuído";
        String contacto = c.getProprietario() != null ? c.getProprietario().getContacto() : "—";
        String docProp = c.getProprietario() != null ? c.getProprietario().getNumeroDocumentoIdentificacao() : "—";
        int passageiros = c.getPassageiros() != null ? c.getPassageiros() : 0;

        StringBuilder sb = new StringBuilder();
        sb.append("FICHA DA CARRINHA / MINIBUS\n\n");
        sb.append("• Matrícula: ").append(c.getMatricula()).append("\n");
        sb.append("• Nº de Chassi / Doc. ID: ").append(c.getDocumentoIdentifcacao()).append("\n");
        sb.append("• Lotação Máxima: ").append(passageiros).append(" passageiros\n");
        sb.append("• Capacidade Estimada de Carga: ").append(c.getCapacidadeCarga()).append(" toneladas\n");
        sb.append("• Tipo de Carroceria: ").append(c.getTipoCarroceria() != null ? c.getTipoCarroceria() : "Passageiros / Mista").append("\n\n");
        sb.append("PROPRIETÁRIO:\n");
        sb.append("• Nome: ").append(prop).append("\n");
        sb.append("• Contacto: ").append(contacto).append("\n");
        sb.append("• BI/NUIT: ").append(docProp).append("\n\n");
        sb.append("HISTÓRICO:\n");
        sb.append("• Total de Serviços Realizados: ").append(viagens).append("\n");
        sb.append("• Total Facturado: ").append(String.format("%.2f MZN", facturado)).append("\n");

        Alert alert = new Alert(Alert.AlertType.INFORMATION, sb.toString());
        alert.setHeaderText("Detalhes da Carrinha - TransCarga");
        alert.setTitle("TransCarga Moçambique");
        alert.showAndWait();
    }
}
