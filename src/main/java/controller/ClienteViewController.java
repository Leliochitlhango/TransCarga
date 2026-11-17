package controller;

import dao.ClienteDAO;
import dataStructure.ListaDuplamenteLigada;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import model.Cliente;

public class ClienteViewController {

    private final ObservableList<Cliente> dados = FXCollections.observableArrayList();
    private ListaDuplamenteLigada<Cliente> clientes;

    @FXML private Button btnActualizar;
    @FXML private Button btnGuardar;
    @FXML private Button btnLimpar;

    @FXML private TableView<Cliente> tabela;
    @FXML private TableColumn<Cliente, String> colContacto;
    @FXML private TableColumn<Cliente, String> colLocalizacao;
    @FXML private TableColumn<Cliente, String> colNome;

    @FXML private Label lblMensagem;
    @FXML private TextField txtContacto;
    @FXML private TextField txtLocalizacao;
    @FXML private TextField txtNome;

    @FXML
    public void initialize() {
        configurarEventosValidacao();
        carregarClientes();
        preencherTabela();
    }

    private void carregarClientes() {
        try {
            clientes = new ClienteDAO().findAll();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void configurarEventosValidacao() {
        if (txtNome != null) {
            txtNome.textProperty().addListener((obs, oldVal, newVal) -> {
                txtNome.getStyleClass().remove("campo-erro");
                if (lblMensagem != null && lblMensagem.getText().contains("nome")) {
                    lblMensagem.setText("");
                }
            });
        }

        if (txtContacto != null) {
            txtContacto.textProperty().addListener((obs, oldVal, newVal) -> {
                txtContacto.getStyleClass().remove("campo-erro");
                if (lblMensagem != null && lblMensagem.getText().contains("contacto")) {
                    lblMensagem.setText("");
                }
            });
        }

        if (txtLocalizacao != null) {
            txtLocalizacao.textProperty().addListener((obs, oldVal, newVal) -> {
                txtLocalizacao.getStyleClass().remove("campo-erro");
                if (lblMensagem != null && lblMensagem.getText().contains("localização")) {
                    lblMensagem.setText("");
                }
            });
        }
    }

    private boolean validarCampos() {
        boolean temErro = false;

        String nome = txtNome.getText() != null ? txtNome.getText().trim() : "";
        String contacto = txtContacto.getText() != null ? txtContacto.getText().trim() : "";
        String localizacao = txtLocalizacao.getText() != null ? txtLocalizacao.getText().trim() : "";

        if (nome.isEmpty()) {
            marcarErro(txtNome, "Por favor, introduza o nome do cliente.");
            temErro = true;
        }

        if (contacto.isEmpty()) {
            marcarErro(txtContacto, "Por favor, introduza o contacto telefónico.");
            temErro = true;
        }

        if (localizacao.isEmpty()) {
            marcarErro(txtLocalizacao, "Por favor, introduza a localização/endereço.");
            temErro = true;
        }

        return !temErro;
    }

    private void marcarErro(TextField campo, String mensagem) {
        if (campo != null && !campo.getStyleClass().contains("campo-erro")) {
            campo.getStyleClass().add("campo-erro");
        }
        if (lblMensagem != null) {
            lblMensagem.setText(mensagem);
        }
    }

    @FXML
    void actualizar(ActionEvent event) {
        if (!validarCampos()) return;

        try {
            Cliente c = new Cliente();
            if (btnActualizar.getAccessibleText() != null) {
                c.setId(Integer.parseInt(btnActualizar.getAccessibleText()));
            }
            c.setNome(txtNome.getText().trim());
            c.setContacto(txtContacto.getText().trim());
            c.setEndereco(txtLocalizacao.getText().trim());

            new ClienteDAO().update(c);

            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Cliente actualizado com sucesso!");
            alert.setHeaderText("Sucesso");
            alert.showAndWait();

            carregarClientes();
            preencherTabela();
            limpar(null);
        } catch (Exception e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR, "Erro ao actualizar cliente: " + e.getMessage());
            alert.showAndWait();
        }
    }

    @FXML
    void guardar(ActionEvent event) {
        if (!validarCampos()) return;

        try {
            Cliente c = new Cliente();
            c.setNome(txtNome.getText().trim());
            c.setContacto(txtContacto.getText().trim());
            c.setEndereco(txtLocalizacao.getText().trim());

            new ClienteDAO().save(c);

            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Cliente cadastrado com sucesso!");
            alert.setHeaderText("Sucesso");
            alert.showAndWait();

            carregarClientes();
            preencherTabela();
            limpar(null);
        } catch (Exception e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR, "Erro ao guardar cliente: " + e.getMessage());
            alert.showAndWait();
        }
    }

    @FXML
    void limpar(ActionEvent event) {
        txtNome.setText("");
        txtContacto.setText("");
        txtLocalizacao.setText("");

        txtNome.getStyleClass().remove("campo-erro");
        txtContacto.getStyleClass().remove("campo-erro");
        txtLocalizacao.getStyleClass().remove("campo-erro");

        if (lblMensagem != null) {
            lblMensagem.setText("");
        }
        if (btnActualizar != null) {
            btnActualizar.setDisable(true);
            btnActualizar.setAccessibleText(null);
        }
    }

    private void preencherTabela() {
        colNome.setCellValueFactory(new PropertyValueFactory<>("nome"));
        colContacto.setCellValueFactory(new PropertyValueFactory<>("contacto"));
        colLocalizacao.setCellValueFactory(new PropertyValueFactory<>("endereco"));

        tabela.setItems(dados);
        dados.clear();

        if (clientes != null) {
            for (Cliente c : clientes) {
                if (c != null) dados.add(c);
            }
        }
    }

    @FXML
    void selecionarItem(MouseEvent event) {
        if (event.getClickCount() == 2) {
            Cliente c = tabela.getFocusModel().getFocusedItem();
            if (c != null) {
                txtNome.setText(c.getNome() != null ? c.getNome() : "");
                txtContacto.setText(c.getContacto() != null ? c.getContacto() : "");
                txtLocalizacao.setText(c.getEndereco() != null ? c.getEndereco() : "");

                btnActualizar.setAccessibleText(String.valueOf(c.getId()));
                btnActualizar.setDisable(false);

                txtNome.getStyleClass().remove("campo-erro");
                txtContacto.getStyleClass().remove("campo-erro");
                txtLocalizacao.getStyleClass().remove("campo-erro");
                if (lblMensagem != null) lblMensagem.setText("");
            }
        }
    }

    @FXML
    void deletarItem(KeyEvent event) {
        if (event.getCode() == event.getCode().DELETE) {
            Cliente c = tabela.getFocusModel().getFocusedItem();
            if (c != null) {
                try {
                    new ClienteDAO().remove(c);
                    Alert alert = new Alert(Alert.AlertType.INFORMATION, "Cliente removido com sucesso!");
                    alert.setHeaderText("Sucesso");
                    alert.showAndWait();
                    carregarClientes();
                    preencherTabela();
                    limpar(null);
                } catch (Exception e) {
                    e.printStackTrace();
                    Alert alert = new Alert(Alert.AlertType.ERROR, "Erro ao remover cliente: " + e.getMessage());
                    alert.showAndWait();
                }
            }
        }
    }
}
