package controller;

import dao.CamiaoDAO;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import model.Carregamento;
import dao.CarregamentoDAO;
import dao.CarrinhaDAO;
import dao.ClienteDAO;
import dataStructure.ListaDuplamenteLigada;
import java.time.LocalDate;
import javafx.scene.control.Alert;
import model.Camiao;
import model.Carrinha;
import model.Cliente;

public class CarregamentoViewController {

    private final ListaDuplamenteLigada<Cliente> clientes = new ClienteDAO().findAll();
    private final ListaDuplamenteLigada<Camiao> camioes = new CamiaoDAO().findAll();
    private final ListaDuplamenteLigada<Carrinha> carrinhas = new CarrinhaDAO().findAll();

    @FXML
    private Button btnLimpar;

    @FXML
    private Button btnRegistar;

    @FXML
    private ComboBox<String> cbCliente;

    @FXML
    private ComboBox<String> cbVeiculo;

    @FXML
    private DatePicker dpData;

    @FXML
    private TextField txtTipoVeiculo;
    
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
    public void initialize() {
        for (Cliente c : clientes) {
            cbCliente.getItems().add(c.getNome());
        }

        for (Camiao c : camioes) {
            cbVeiculo.getItems().add(c.getMatricula());
        }

        for (Carrinha c : carrinhas) {
            cbVeiculo.getItems().add(c.getMatricula());
        }
    }

    @FXML
    void limpar(ActionEvent event) {

    }

    @FXML
    void registar(ActionEvent event) {
        int capacity = Integer.parseInt(lblCapacidade.getText().trim());
        int quantidade = Integer.parseInt(txtQuantidade.getText());

        if (capacity < quantidade) {
            new Alert(Alert.AlertType.WARNING, "A quantidade maior que capacidade do veiculo").show();
            return;
        }

        Carregamento novoCarregamento = new Carregamento();

        novoCarregamento.setLocalOrigem(txtOrigem.getText().trim());
        novoCarregamento.setLocaDestino(txtDestino.getText().trim());
        novoCarregamento.setValorCobrado(Double.parseDouble(txtValor.getText()));
        novoCarregamento.setData(dpData.getValue());
        novoCarregamento.setQuantidade(Double.valueOf(txtQuantidade.getText()));

        for (Cliente cli : clientes) {
            if (cli.getNome().equals(cbCliente.getValue())) {
                novoCarregamento.setCliente(cli);
                break;
            }
        }

        for (Camiao c : camioes) {
            if (cbVeiculo.getValue().equals(c.getMatricula())) {
                novoCarregamento.setVeiculo(c);
                break;
            }
        }
        for (Carrinha c : carrinhas) {
            if (cbVeiculo.getValue().equals(c.getMatricula())) {
                novoCarregamento.setVeiculo(c);
                break;
            }
        }
        
        new CarregamentoDAO().save(novoCarregamento);
        
        
        
        new Alert(Alert.AlertType.INFORMATION, "Carregamento Salvo").show();
        
    }

    @FXML
    void veiculoSelecionado(ActionEvent event) {
        for (Camiao c : camioes) {
            if (cbVeiculo.getValue().equals(c.getMatricula())) {
                lblCapacidade.setText(String.valueOf(c.getCapacidadeCarga()));
                txtProprietario.setText(c.getProprietario().getNome());
                txtProprietario.setEditable(false);
                txtTipoVeiculo.setText("CAMIÃO");
                txtTipoVeiculo.setEditable(false);
                break;
            }
        }
        for (Carrinha c : carrinhas) {
            if (cbVeiculo.getValue().equals(c.getMatricula())) {
                lblCapacidade.setText(String.valueOf(c.getCapacidadeCarga()));
                txtProprietario.setText(c.getProprietario().getNome());
                txtProprietario.setEditable(false);
                txtTipoVeiculo.setText("CARRINHA");
                txtTipoVeiculo.setEditable(false);
                break;
            }
        }
    }

}
