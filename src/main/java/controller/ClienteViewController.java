package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

public class ClienteViewController {

    @FXML
    private Button btnActualizar;

    @FXML
    private Button btnGuardar;

    @FXML
    private Button btnLimpar;

    @FXML
    private TableColumn<?, ?> colContacto;

    @FXML
    private TableColumn<?, ?> colLocalizacao;

    @FXML
    private TableColumn<?, ?> colNome;

    @FXML
    private Label lblMensagem;

    @FXML
    private TableView<?> tabela;

    @FXML
    private TextField txtContacto;

    @FXML
    private TextField txtLocalizacao;

    @FXML
    private TextField txtNome;

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
