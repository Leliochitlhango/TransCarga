package controller;

import dao.CarregamentoDAO;
import dataStructure.ListaDuplamenteLigada;
import java.time.LocalDate;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import model.Carregamento;

public class ListaCarregamentosViewController {

    private final ObservableList<Carregamento> dados = FXCollections.observableArrayList();
    private ListaDuplamenteLigada<Carregamento> carregamentos;

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
        preencherTabela();
    }

    private void carregarDados() {
        try {
            carregamentos = new CarregamentoDAO().findAll();
        } catch (Exception e) {
            e.printStackTrace();
        }

        double valorTotal = 0.0;
        int totalServicos = 0;

        if (carregamentos != null) {
            totalServicos = carregamentos.getTamanho();
            for (Carregamento c : carregamentos) {
                if (c != null) valorTotal += c.getValorCobrado();
            }
        }

        if (lblTotalGeral != null) {
            lblTotalGeral.setText(String.format("Total Arrecadado: %.2f MZN", valorTotal));
        }
        if (lblNumServicos != null) {
            lblNumServicos.setText("Serviços: " + totalServicos);
        }
    }

    private void preencherTabela() {
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

        tabela.setItems(dados);
        dados.clear();

        if (carregamentos != null) {
            for (Carregamento c : carregamentos) {
                if (c != null) dados.add(c);
            }
        }
    }
}
