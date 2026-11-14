package controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import model.Camiao;
import dataStructure.ListaDuplamenteLigada;
import dao.CamiaoDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.cell.PropertyValueFactory;


public class ListaCamioesViewController {

    private final ObservableList<Camiao> dados = FXCollections.observableArrayList();
    private final ListaDuplamenteLigada<Camiao> camioes = new CamiaoDAO().findAll();
    
    
    @FXML
    private TableColumn<Camiao, Integer> colCapacidade;

    @FXML
    private TableColumn<Camiao, String> colDocIdenticacao;

    @FXML
    private TableColumn<Camiao, String> colMatricula;

    @FXML
    private TableColumn<Camiao, String> colMercadoria;

    @FXML
    private TableColumn<Camiao, String> colProprietario;

    @FXML
    private Label lblTotal;

    @FXML
    private TableView<Camiao> tabela;
    
    
    @FXML 
    public void initialize() {
        preencherTabela();
        lblTotal.setText(String.valueOf("Total de Camiões: "+camioes.getTamanho()));
    }
    
    private void preencherTabela() {
        colDocIdenticacao.setCellValueFactory(new PropertyValueFactory<>("documentoIdentifcacao"));
        colMatricula.setCellValueFactory(new PropertyValueFactory<>("matricula"));
        colMercadoria.setCellValueFactory(new PropertyValueFactory<>("tipoMercadoria"));
        colCapacidade.setCellValueFactory(new PropertyValueFactory<>("capacidadeCarga"));
        colProprietario.setCellValueFactory(new PropertyValueFactory<>("proprietario"));

        tabela.setItems(dados);
        dados.clear();
        
        for (Camiao c : camioes) {
            dados.add(c);
        }
    }

}
