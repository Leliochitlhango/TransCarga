package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.scene.control.DialogPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.control.cell.PropertyValueFactory;

import model.Cliente;
import dao.ClienteDAO;
import dataStructure.ListaDuplamenteLigada;
import javafx.scene.input.KeyEvent;

public class ClienteViewController {
    
    private final ObservableList<Cliente> dados = FXCollections.observableArrayList();
    private final ListaDuplamenteLigada<Cliente> clientes = new ClienteDAO().findAll();
    

    @FXML
    private Button btnActualizar;

    @FXML
    private Button btnGuardar;

    @FXML
    private Button btnLimpar;

    @FXML
    private TableView<Cliente> tabela;
    
    @FXML
    private TableColumn<Cliente, String> colContacto;

    @FXML
    private TableColumn<Cliente, String> colLocalizacao;

    @FXML
    private TableColumn<Cliente, String> colNome;

    @FXML
    private Label lblMensagem;

    @FXML
    private TextField txtContacto;

    @FXML
    private TextField txtLocalizacao;

    @FXML
    private TextField txtNome;

    @FXML
    public void initialize() {
        preencherTabela();
    }
    
    
    @FXML
    void actualizar(ActionEvent event) {
        Cliente c = new Cliente();
        c.setId(Integer.parseInt(btnActualizar.getAccessibleText()));
        c.setNome(txtNome.getText().trim());
        c.setContacto(txtContacto.getText().trim());
        c.setEndereco(txtLocalizacao.getText().trim());
        
        new ClienteDAO().update(c);
        
        new Alert(Alert.AlertType.INFORMATION, "Cliente Atualizado").show();
    }

    @FXML
    void guardar(ActionEvent event) {
        Cliente c = new Cliente();
        c.setNome(txtNome.getText().trim());
        c.setContacto(txtContacto.getText().trim());
        c.setEndereco(txtLocalizacao.getText().trim());
        
        new ClienteDAO().save(c);
        
        new Alert(Alert.AlertType.INFORMATION, "Cliente Cadastrado").show();
    }

    @FXML
    void limpar(ActionEvent event) {
        txtNome.setText("");
        txtContacto.setText("");
        txtLocalizacao.setText("");
    }
    
    private void preencherTabela() {
        colNome.setCellValueFactory(new PropertyValueFactory<>("nome"));
        colContacto.setCellValueFactory(new PropertyValueFactory<>("contacto"));
        colLocalizacao.setCellValueFactory(new PropertyValueFactory<>("endereco"));

        tabela.setItems(dados);
        dados.clear();
        
        for (Cliente c : clientes) {
            dados.add(c);
        }
    }

    @FXML
    void selecionarItem(MouseEvent event) {
        if (event.getClickCount() == 2) {
            Cliente c = tabela.getFocusModel().getFocusedItem();
            txtNome.setText(c.getNome());
            txtContacto.setText(c.getContacto());
            txtLocalizacao.setText(c.getEndereco());
            
            btnActualizar.setAccessibleText(String.valueOf(c.getId()));
            btnActualizar.setDisable(false);
        }
    }    
    
    @FXML
    void deletarItem(KeyEvent event) {
        if (event.getCode() == event.getCode().DELETE) {
            
            Cliente c = tabela.getFocusModel().getFocusedItem();
            
            new ClienteDAO().remove(c);
            
            new Alert(Alert.AlertType.INFORMATION, "Cliente Removido").show();
        }    
    }
    
}