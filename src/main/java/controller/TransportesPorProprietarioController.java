package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

public class TransportesPorProprietarioController {

    @FXML
    private Button btnConsultar;

    @FXML
    private ComboBox<?> cbProprietario;

    @FXML
    private TableColumn<?, ?> colCliente;

    @FXML
    private TableColumn<?, ?> colData;

    @FXML
    private TableColumn<?, ?> colDestino;

    @FXML
    private TableColumn<?, ?> colOrigem;

    @FXML
    private TableColumn<?, ?> colQuantidade;

    @FXML
    private TableColumn<?, ?> colValor;

    @FXML
    private TableColumn<?, ?> colVeiculo;

    @FXML
    private Label lblMensagem;

    @FXML
    private Label lblNumServicos;

    @FXML
    private Label lblTotal;

    @FXML
    private TableView<?> tabela;

    @FXML
    void consultar(ActionEvent event) {

    }

}
