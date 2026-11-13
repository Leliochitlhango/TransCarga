package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

public class VeiculoViewController {

    @FXML
    private Button btnActualizar;

    @FXML
    private Button btnGuardar;

    @FXML
    private Button btnLimpar;

    @FXML
    private ComboBox<?> cbProprietario;

    @FXML
    private ComboBox<?> cbTipo;

    @FXML
    private TableColumn<?, ?> colCapacidade;

    @FXML
    private TableColumn<?, ?> colMarca;

    @FXML
    private TableColumn<?, ?> colMatricula;

    @FXML
    private TableColumn<?, ?> colModelo;

    @FXML
    private TableColumn<?, ?> colProprietario;

    @FXML
    private TableColumn<?, ?> colTipo;

    @FXML
    private Label lblMensagem;

    @FXML
    private TableView<?> tabela;

    @FXML
    private TextField txtCapacidade;

    @FXML
    private TextField txtMarca;

    @FXML
    private TextField txtMatricula;

    @FXML
    private TextField txtModelo;

    @FXML
    void actualizar(ActionEvent event) {

    }

    @FXML
    void guardar(ActionEvent event) {

    }

    @FXML
    void limpar(ActionEvent event) {

    }

}
