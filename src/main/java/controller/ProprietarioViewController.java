package controller;

import dao.CamiaoDAO;
import dao.CarrinhaDAO;
import dao.ProprietarioDAO;
import dataStructure.ListaDuplamenteLigada;
import javafx.beans.property.SimpleIntegerProperty;
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
import javafx.scene.input.MouseEvent;
import model.Camiao;
import model.Carrinha;
import model.Proprietario;

public class ProprietarioViewController {

    private final ObservableList<Proprietario> dados = FXCollections.observableArrayList();
    private ListaDuplamenteLigada<Proprietario> listaProprietarios;
    private ListaDuplamenteLigada<Camiao> listaCamioes;
    private ListaDuplamenteLigada<Carrinha> listaCarrinhas;

    @FXML private Button btnActualizar;
    @FXML private Button btnGuardar;
    @FXML private Button btnLimpar;

    @FXML private TableView<Proprietario> tabela;
    @FXML private TableColumn<Proprietario, String> colContacto;
    @FXML private TableColumn<Proprietario, String> colDocumento;
    @FXML private TableColumn<Proprietario, String> colNome;
    @FXML private TableColumn<Proprietario, Integer> colNumVeiculos;

    @FXML private Label lblMensagem;
    @FXML private TextField txtContacto;
    @FXML private TextField txtDocumento;
    @FXML private TextField txtNome;

    @FXML
    public void initialize() {
        configurarEventosValidacao();
        carregarDados();
        preencherTabela();
    }

    private void carregarDados() {
        try {
            listaProprietarios = new ProprietarioDAO().findAll();
            listaCamioes = new CamiaoDAO().findAll();
            listaCarrinhas = new CarrinhaDAO().findAll();
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

        if (txtDocumento != null) {
            txtDocumento.textProperty().addListener((obs, oldVal, newVal) -> {
                txtDocumento.getStyleClass().remove("campo-erro");
                if (lblMensagem != null && lblMensagem.getText().contains("documento")) {
                    lblMensagem.setText("");
                }
            });
        }
    }

    private boolean validarCampos() {
        boolean temErro = false;

        String nome = txtNome.getText() != null ? txtNome.getText().trim() : "";
        String contacto = txtContacto.getText() != null ? txtContacto.getText().trim() : "";
        String documento = txtDocumento.getText() != null ? txtDocumento.getText().trim() : "";

        if (nome.isEmpty()) {
            marcarErro(txtNome, "Por favor, introduza o nome completo.");
            temErro = true;
        }

        if (contacto.isEmpty()) {
            marcarErro(txtContacto, "Por favor, introduza o contacto telefónico.");
            temErro = true;
        }

        if (documento.isEmpty()) {
            marcarErro(txtDocumento, "Por favor, introduza o documento de identificação (BI/NUIT).");
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
            Proprietario p = new Proprietario();
            if (btnActualizar.getAccessibleText() != null) {
                p.setId(Integer.parseInt(btnActualizar.getAccessibleText()));
            }
            p.setNome(txtNome.getText().trim());
            p.setContacto(txtContacto.getText().trim());
            p.setNumeroDocumentoIdentificacao(txtDocumento.getText().trim());

            new ProprietarioDAO().update(p);

            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Proprietário actualizado com sucesso!");
            alert.setHeaderText("Sucesso");
            alert.showAndWait();

            carregarDados();
            preencherTabela();
            limpar(null);
        } catch (Exception e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR, "Erro ao actualizar proprietário: " + e.getMessage());
            alert.showAndWait();
        }
    }

    @FXML
    void guardar(ActionEvent event) {
        if (!validarCampos()) return;

        try {
            Proprietario p = new Proprietario();
            p.setNome(txtNome.getText().trim());
            p.setContacto(txtContacto.getText().trim());
            p.setNumeroDocumentoIdentificacao(txtDocumento.getText().trim());

            new ProprietarioDAO().save(p);

            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Proprietário registado com sucesso!");
            alert.setHeaderText("Sucesso");
            alert.showAndWait();

            carregarDados();
            preencherTabela();
            limpar(null);
        } catch (Exception e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR, "Erro ao guardar proprietário: " + e.getMessage());
            alert.showAndWait();
        }
    }

    @FXML
    void limpar(ActionEvent event) {
        txtNome.setText("");
        txtContacto.setText("");
        txtDocumento.setText("");

        txtNome.getStyleClass().remove("campo-erro");
        txtContacto.getStyleClass().remove("campo-erro");
        txtDocumento.getStyleClass().remove("campo-erro");

        if (lblMensagem != null) {
            lblMensagem.setText("");
        }
        if (btnActualizar != null) {
            btnActualizar.setDisable(true);
            btnActualizar.setAccessibleText(null);
        }
    }

    @FXML
    void selecionarItem(MouseEvent event) {
        if (event.getClickCount() == 2) {
            Proprietario p = tabela.getFocusModel().getFocusedItem();
            if (p != null) {
                txtNome.setText(p.getNome() != null ? p.getNome() : "");
                txtContacto.setText(p.getContacto() != null ? p.getContacto() : "");
                txtDocumento.setText(p.getNumeroDocumentoIdentificacao() != null ? p.getNumeroDocumentoIdentificacao() : "");

                btnActualizar.setDisable(false);
                btnActualizar.setAccessibleText(String.valueOf(p.getId()));

                txtNome.getStyleClass().remove("campo-erro");
                txtContacto.getStyleClass().remove("campo-erro");
                txtDocumento.getStyleClass().remove("campo-erro");
                if (lblMensagem != null) lblMensagem.setText("");
            }
        }
    }

    private int contarVeiculosDoProprietario(Proprietario p) {
        if (p == null) return 0;
        int count = 0;
        if (listaCamioes != null) {
            for (Camiao c : listaCamioes) {
                if (c != null && c.getProprietario() != null && c.getProprietario().getId() == p.getId()) {
                    count++;
                }
            }
        }
        if (listaCarrinhas != null) {
            for (Carrinha c : listaCarrinhas) {
                if (c != null && c.getProprietario() != null && c.getProprietario().getId() == p.getId()) {
                    count++;
                }
            }
        }
        return count;
    }

    private void preencherTabela() {
        colNome.setCellValueFactory(new PropertyValueFactory<>("nome"));
        colContacto.setCellValueFactory(new PropertyValueFactory<>("contacto"));
        colDocumento.setCellValueFactory(new PropertyValueFactory<>("numeroDocumentoIdentificacao"));
        colNumVeiculos.setCellValueFactory(cellData -> 
            new SimpleIntegerProperty(contarVeiculosDoProprietario(cellData.getValue())).asObject()
        );

        tabela.setItems(dados);
        dados.clear();

        if (listaProprietarios != null) {
            for (Proprietario p : listaProprietarios) {
                if (p != null) dados.add(p);
            }
        }
    }
}
