package controller;

import javafx.event.ActionEvent;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.scene.control.cell.PropertyValueFactory;
import model.Proprietario;
import dao.ProprietarioDAO;
import dataStructure.ListaDuplamenteLigada;


public class ProprietarioViewController {

    private final ObservableList<Proprietario> dados = FXCollections.observableArrayList();
    private ListaDuplamenteLigada<Proprietario> listaProprietarios = new ProprietarioDAO().findAll();

    @FXML
    private Button btnActualizar;

    @FXML
    private Button btnGuardar;

    @FXML
    private Button btnLimpar;

    @FXML
    private TableView<Proprietario> tabela;
    @FXML
    private TableColumn<Proprietario, String> colContacto;
    @FXML
    private TableColumn<Proprietario, String> colDocumento;
    @FXML
    private TableColumn<Proprietario, String> colNome;
    @FXML
    private TableColumn<Proprietario, Integer> colNumVeiculos;

    @FXML
    private Label lblMensagem;
    @FXML
    private TextField txtContacto;
    @FXML
    private TextField txtDocumento;
    @FXML
    private TextField txtNome;

    private Integer numeroVeiculos = 2;

    @FXML
    public void initialize() {
        preencherTabela();
    }

    @FXML
    void actualizar(ActionEvent event) {
        Proprietario p = new Proprietario();
        p.setId(Integer.parseInt(btnActualizar.getAccessibleText()));
        p.setNome(txtNome.getText().trim());
        p.setContacto(txtContacto.getText().trim());
        p.setNumeroDocumentoIdentificacao(txtDocumento.getText().trim());

        new ProprietarioDAO().update(p);
        new Alert(Alert.AlertType.INFORMATION, "Proprietario Atualizado").show();
    }

    @FXML
    void guardar(ActionEvent event) {
        Proprietario p = new Proprietario();
        p.setNome(txtNome.getText().trim());
        p.setContacto(txtContacto.getText().trim());
        p.setNumeroDocumentoIdentificacao(txtDocumento.getText().trim());

        new ProprietarioDAO().save(p);
        new Alert(Alert.AlertType.INFORMATION, "Proprietario Salvo").show();
    }

    @FXML
    void limpar(ActionEvent event) {
        txtNome.setText("");
        txtContacto.setText("");
        txtDocumento.setText("");
    }
    
    @FXML
    void selecionarItem(MouseEvent event) {
        if (event.getClickCount() == 2) {
            Proprietario p = tabela.getFocusModel().getFocusedItem();
            txtNome.setText(p.getNome());
            txtContacto.setText(p.getContacto());
            txtDocumento.setText(p.getNumeroDocumentoIdentificacao());
            
            btnActualizar.setDisable(false);
            btnActualizar.setAccessibleText(String.valueOf(p.getId()));
        }
    }

    private void preencherTabela() {
        colNome.setCellValueFactory(new PropertyValueFactory<>("nome"));
        colContacto.setCellValueFactory(new PropertyValueFactory<>("contacto"));
        colDocumento.setCellValueFactory(new PropertyValueFactory<>("numeroDocumentoIdentificacao"));
        colNumVeiculos.setCellValueFactory(cellData -> new SimpleIntegerProperty(numeroVeiculos).asObject());

        tabela.setItems(dados);
        dados.clear();

        for (Proprietario p : listaProprietarios) {
            dados.add(p);
        }
    }

}
