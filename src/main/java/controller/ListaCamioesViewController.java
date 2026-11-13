package controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

public class ListaCamioesViewController {

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
    private Label lblTotal;

    @FXML
    private TableView<?> tabela;

}
