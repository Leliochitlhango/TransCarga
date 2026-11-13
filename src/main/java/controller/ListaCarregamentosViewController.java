package controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

public class ListaCarregamentosViewController {

    @FXML
    private TableColumn<?, ?> colCliente;

    @FXML
    private TableColumn<?, ?> colData;

    @FXML
    private TableColumn<?, ?> colDestino;

    @FXML
    private TableColumn<?, ?> colOrigem;

    @FXML
    private TableColumn<?, ?> colProprietario;

    @FXML
    private TableColumn<?, ?> colQuantidade;

    @FXML
    private TableColumn<?, ?> colValor;

    @FXML
    private TableColumn<?, ?> colVeiculo;

    @FXML
    private Label lblNumServicos;

    @FXML
    private Label lblTotalGeral;

    @FXML
    private TableView<?> tabela;

}
