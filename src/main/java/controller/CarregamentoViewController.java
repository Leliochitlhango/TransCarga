package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class CarregamentoViewController {

    @FXML
    private Button btnLimpar;

    @FXML
    private Button btnRegistar;

    @FXML
    private ComboBox<?> cbCliente;

    @FXML
    private ComboBox<?> cbVeiculo;

    @FXML
    private DatePicker dpData;

    @FXML
    private Label lblCapacidade;

    @FXML
    private Label lblValidacao;

    @FXML
    private TextField txtDestino;

    @FXML
    private TextField txtOrigem;

    @FXML
    private TextField txtProprietario;

    @FXML
    private TextField txtQuantidade;

    @FXML
    private TextField txtValor;

    @FXML
    void limpar(ActionEvent event) {

    }

    @FXML
    void registar(ActionEvent event) {

    }

}
