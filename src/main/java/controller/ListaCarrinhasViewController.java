package controller;

import dao.CamiaoDAO;
import dao.CarrinhaDAO;
import dataStructure.ListaDuplamenteLigada;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import model.Carrinha;

public class ListaCarrinhasViewController {

    private final ObservableList<Carrinha> dados = FXCollections.observableArrayList();
    private final ListaDuplamenteLigada<Carrinha> carrinhas = new CarrinhaDAO().findAll();
    
    @FXML
    private TableColumn<Carrinha, Integer> colCapacidade;

    @FXML
    private TableColumn<Carrinha, String> colDocIdenticacao;

    @FXML
    private TableColumn<Carrinha, String> colMatricula;

    @FXML
    private TableColumn<Carrinha, Integer> colPassageiros;

    @FXML
    private TableColumn<Carrinha, String> colProprietario;

    @FXML
    private Label lblTotal;

    @FXML 
    public void initialize() {
        preencherTabela();
        lblTotal.setText(String.valueOf("Total de Camiões: "+carrinhas.getTamanho()));
    }
    
    
    @FXML
    private TableView<Carrinha> tabela;
    
    private void preencherTabela() {
        colDocIdenticacao.setCellValueFactory(new PropertyValueFactory<>("documentoIdentifcacao"));
        colMatricula.setCellValueFactory(new PropertyValueFactory<>("matricula"));
        colPassageiros.setCellValueFactory(new PropertyValueFactory<>("passageiros"));
        colCapacidade.setCellValueFactory(new PropertyValueFactory<>("capacidadeCarga"));
        colProprietario.setCellValueFactory(new PropertyValueFactory<>("proprietario"));

        tabela.setItems(dados);
        dados.clear();
        
        for (Carrinha c : carrinhas) {
            dados.add(c);
        }
    }

}
