package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

import model.Veiculo;
import model.Camiao;
import model.Carrinha;
import dao.CamiaoDAO;
import dao.CarrinhaDAO;
import dao.ProprietarioDAO;

import dataStructure.ListaDuplamenteLigada;
import javafx.scene.control.Alert;
import model.Proprietario;

public class VeiculoViewController {
    
    ListaDuplamenteLigada<Camiao> camioes = new CamiaoDAO().findAll();
    ListaDuplamenteLigada<Carrinha> carrinhas = new CarrinhaDAO().findAll();
    ListaDuplamenteLigada<Proprietario> proprietarios = new ProprietarioDAO().findAll();

    @FXML
    private Button btnActualizar;

    @FXML
    private Button btnGuardar;

    @FXML
    private Button btnLimpar;

    @FXML
    private ComboBox<String> cbProprietario;

    @FXML
    private ComboBox<String> cbTipo;

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

    @FXML private Label lblMarca;
    
    @FXML private Label lblModelo;
    
    @FXML private Label lblMensagem;

    @FXML private TableView<?> tabela;

    @FXML private TextField txtCapacidade;

    @FXML private TextField txtMarca;

    @FXML private TextField txtMatricula;

    @FXML private TextField txtModelo;

    @FXML
    public void initialize() {
        cbTipo.getItems().add("CAMIAO");
        cbTipo.getItems().add("CARRINHA");
        
        for (Proprietario p: proprietarios) {
            cbProprietario.getItems().add(p.getNome());
        }
        
    }
    
    
    @FXML
    void actualizar(ActionEvent event) {

    }

    @FXML
    void guardar(ActionEvent event) {
        String tipo  = cbTipo.getValue();
        
        if (tipo.equals("CAMIAO")) {
            Camiao c = new Camiao();
            c.setMatricula(txtMatricula.getText().trim());
            c.setDocumentoIdentifcacao(txtMarca.getText().trim());
            for (Proprietario p: proprietarios) {
                if (cbProprietario.getValue().equals(p.getNome())) {
                    c.setProprietario(p);
                    break;
                }
            }
            c.setCapacidadeCarga(Integer.parseInt(txtCapacidade.getText().trim()));
            c.setTipoMercadoria(txtModelo.getText());
            c.setNumeroEixos("4");
            
            new CamiaoDAO().save(c);
            
            new Alert(Alert.AlertType.INFORMATION, "Camiao Registado").show();
            
        } else if (tipo.equals("CARRINHA")) {
            Carrinha c = new Carrinha();
            c.setMatricula(txtMatricula.getText().trim());
            c.setDocumentoIdentifcacao(txtMarca.getText().trim());
            for (Proprietario p: proprietarios) {
                if (cbProprietario.getValue().equals(p.getNome())) {
                    c.setProprietario(p);
                    break;
                }
            }
            c.setCapacidadeCarga(Integer.parseInt(txtCapacidade.getText().trim()));
            c.setPassageiros(Integer.valueOf(txtModelo.getText()));
            c.setTipoCarroceria("Fechada");
            
            new CarrinhaDAO().save(c);
            
            new Alert(Alert.AlertType.INFORMATION, "Carrinha Registada").show();
        } else {
            new Alert(Alert.AlertType.WARNING, "Tipo de Veiculo invalido").show();
        }
         
        
    }

    @FXML
    void limpar(ActionEvent event) {

    }
    
    @FXML
    void mudarTipoVeiculo(ActionEvent event) {
        if (cbTipo.getValue().equals("CAMIAO")) {
            lblMarca.setText("Doc. ID");
            txtMarca.setPromptText("Ex: DI-0023");
            lblModelo.setText("Mercadoria");
            txtModelo.setPromptText("Ex: Areia Grossa");
        }
        
        if (cbTipo.getValue().equals("CARRINHA")) {
            lblMarca.setText("Doc. ID");
            txtMarca.setPromptText("Ex: DI-0023");
            lblModelo.setText("Passageiros");
            txtModelo.setPromptText("Ex: 15");
        }
    }

}
