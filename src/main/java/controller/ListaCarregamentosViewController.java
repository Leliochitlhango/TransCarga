package controller;

import dao.CarregamentoDAO;
import dao.ClienteDAO;
import dataStructure.ListaDuplamenteLigada;
import java.time.LocalDate;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import model.Carregamento;
import model.Cliente;

public class ListaCarregamentosViewController {

    private final ObservableList<Carregamento> dados = FXCollections.observableArrayList();
    private final ListaDuplamenteLigada<Carregamento> carregamentos = new CarregamentoDAO().findAll();

    @FXML
    private TableColumn<Carregamento, String> colCliente;

    @FXML
    private TableColumn<Carregamento, LocalDate> colData;

    @FXML
    private TableColumn<Carregamento, String> colDestino;

    @FXML
    private TableColumn<Carregamento, String> colOrigem;

    @FXML
    private TableColumn<Carregamento, String> colProprietario;

    @FXML
    private TableColumn<Carregamento, Double> colQuantidade;

    @FXML
    private TableColumn<Carregamento, Double> colValor;

    @FXML
    private TableColumn<Carregamento, String> colVeiculo;

    @FXML
    private Label lblNumServicos;

    @FXML
    private Label lblTotalGeral;

    @FXML
    private TableView<Carregamento> tabela;

    Double valorTotal=0.0;
    int servicos = 0;

    @FXML
    public void initialize() {
        preencherTabela();

        for (Carregamento c: carregamentos) {
            valorTotal += c.getValorCobrado();
        }
        
        lblTotalGeral.setText(String.valueOf("Total Arrecadado: "+valorTotal+"MT"));
        lblNumServicos.setText(String.valueOf("Servicos: "+carregamentos.getTamanho()));
    }

    private void preencherTabela() {
        colData.setCellValueFactory(new PropertyValueFactory<>("data"));
        colCliente.setCellValueFactory(new PropertyValueFactory<>("cliente"));
        colVeiculo.setCellValueFactory(new PropertyValueFactory<>("veiculo"));
        colProprietario.setCellValueFactory(new PropertyValueFactory<>("proprietario"));
        colQuantidade.setCellValueFactory(new PropertyValueFactory<>("quantidade"));
        colOrigem.setCellValueFactory(new PropertyValueFactory<>("localOrigem"));
        colDestino.setCellValueFactory(new PropertyValueFactory<>("locaDestino"));
        colValor.setCellValueFactory(new PropertyValueFactory<>("valorCobrado"));

        tabela.setItems(dados);
        dados.clear();

        for (Carregamento c : carregamentos) {
            dados.add(c);
        }
    }

}
