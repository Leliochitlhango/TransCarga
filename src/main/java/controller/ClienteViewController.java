package controller;

import dao.CarregamentoDAO;
import dao.ClienteDAO;
import dataStructure.ListaDuplamenteLigada;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import model.Carregamento;
import model.Cliente;
import util.Validador;

public class ClienteViewController {

    private static final Logger LOGGER = Logger.getLogger(ClienteViewController.class.getName());

    private final ListaDuplamenteLigada<Cliente> dados = new ListaDuplamenteLigada<>();
    private final ObservableList<Cliente> observavel = FXCollections.observableArrayList();
    private ListaDuplamenteLigada<Cliente> clientes;
    private ListaDuplamenteLigada<Carregamento> todosCarregamentos;

    @FXML private Button btnActualizar;
    @FXML private Button btnEliminar;
    @FXML private Button btnGuardar;
    @FXML private Button btnLimpar;

    @FXML private TableView<Cliente> tabela;
    @FXML private TableColumn<Cliente, String> colContacto;
    @FXML private TableColumn<Cliente, String> colLocalizacao;
    @FXML private TableColumn<Cliente, String> colNome;
    @FXML private Label lblTotalTabela;

    @FXML private Label lblMensagem;
    @FXML private TextField txtContacto;
    @FXML private TextField txtLocalizacao;
    @FXML private TextField txtNome;

    private int clienteIdEmEdicao = 0;

    @FXML
    public void initialize() {
        Validador.aplicarMascaraTelefone(txtContacto);
        configurarEventosValidacao();
        configurarTabela();
        carregarClientes();
        preencherTabela();
    }

    private void configurarTabela() {
        colNome.setCellValueFactory(new PropertyValueFactory<>("nome"));
        colContacto.setCellValueFactory(new PropertyValueFactory<>("contacto"));
        colLocalizacao.setCellValueFactory(new PropertyValueFactory<>("endereco"));
        tabela.setItems(observavel);
    }

    private void carregarClientes() {
        try {
            clientes = new ClienteDAO().findAll();
            todosCarregamentos = new CarregamentoDAO().findAll();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Erro ao carregar clientes", e);
            Alert alert = new Alert(Alert.AlertType.ERROR, "Erro ao carregar clientes: " + e.getMessage());
            alert.setHeaderText("Erro");
            alert.showAndWait();
        }
    }

    private void configurarEventosValidacao() {
        if (txtNome != null) {
            txtNome.textProperty().addListener((obs, oldVal, newVal) -> {
                txtNome.getStyleClass().remove("campo-erro");
                limparMensagemSeContem("nome");
            });
        }

        if (txtContacto != null) {
            txtContacto.textProperty().addListener((obs, oldVal, newVal) -> {
                verificarContactoEmTempoReal();
            });
        }

        if (txtLocalizacao != null) {
            txtLocalizacao.textProperty().addListener((obs, oldVal, newVal) -> {
                txtLocalizacao.getStyleClass().remove("campo-erro");
                limparMensagemSeContem("localização");
            });
        }
    }

    private void verificarContactoEmTempoReal() {
        if (txtContacto == null) return;
        String contacto = txtContacto.getText() != null ? txtContacto.getText().trim() : "";

        if (contacto.isEmpty()) {
            txtContacto.getStyleClass().remove("campo-erro");
            limparMensagemSeContem("contacto");
            return;
        }

        Cliente dup = encontrarClientePorContacto(contacto);
        if (dup != null && dup.getId() != clienteIdEmEdicao) {
            marcarErro(txtContacto, "Este contacto telefónico já pertence a outro cliente!");
            return;
        }

        String apenasDigitos = contacto.replaceAll("\\D", "");
        if (apenasDigitos.length() >= 9) {
            if (!Validador.isTelefoneValido(contacto)) {
                marcarErro(txtContacto, "Contacto inválido. Deve iniciar com 82 a 88 (formato: 8X XXX XXXX).");
                return;
            }
        }

        txtContacto.getStyleClass().remove("campo-erro");
        limparMensagemSeContem("contacto");
    }

    private Cliente encontrarClientePorContacto(String contacto) {
        if (contacto == null || clientes == null) return null;
        String limpo = contacto.replaceAll("\\D", "");

        for (Cliente c : clientes) {
            if (c != null && c.getContacto() != null) {
                String cLimpo = c.getContacto().replaceAll("\\D", "");
                if (!cLimpo.isEmpty() && cLimpo.equals(limpo)) {
                    return c;
                }
            }
        }
        return null;
    }

    private void limparMensagemSeContem(String termo) {
        if (lblMensagem != null && lblMensagem.getText().toLowerCase().contains(termo.toLowerCase())) {
            lblMensagem.setText("");
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
        } else if (!Validador.isTelefoneValido(contacto)) {
            marcarErro(txtContacto, "Contacto inválido. Deve começar com 82..88 e ter 9 dígitos (ex: 84 111 0001).");
            temErro = true;
        } else {
            Cliente dup = encontrarClientePorContacto(contacto);
            if (dup != null && dup.getId() != clienteIdEmEdicao) {
                marcarErro(txtContacto, "Este contacto telefónico já está registado!");
                temErro = true;
            }
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
        if (clienteIdEmEdicao == 0 && btnActualizar.getAccessibleText() != null) {
            clienteIdEmEdicao = Integer.parseInt(btnActualizar.getAccessibleText());
        }
        if (!validarCampos()) return;

        try {
            Cliente c = new Cliente();
            c.setId(clienteIdEmEdicao);
            c.setNome(txtNome.getText().trim());
            c.setContacto(Validador.formatarTelefone(txtContacto.getText().trim()));
            c.setEndereco(txtLocalizacao.getText().trim());

            new ClienteDAO().update(c);

            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Cliente actualizado com sucesso!");
            alert.setHeaderText("Sucesso");
            alert.showAndWait();

            carregarClientes();
            preencherTabela();
            limpar(null);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Erro ao actualizar cliente", e);
            Alert alert = new Alert(Alert.AlertType.ERROR, "Erro ao actualizar cliente: " + e.getMessage());
            alert.showAndWait();
        }
    }

    @FXML
    void guardar(ActionEvent event) {
        clienteIdEmEdicao = 0;
        if (!validarCampos()) return;

        try {
            Cliente c = new Cliente();
            c.setNome(txtNome.getText().trim());
            c.setContacto(Validador.formatarTelefone(txtContacto.getText().trim()));
            c.setEndereco(txtLocalizacao.getText().trim());

            new ClienteDAO().save(c);

            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Cliente registado com sucesso!");
            alert.setHeaderText("Sucesso");
            alert.showAndWait();

            carregarClientes();
            preencherTabela();
            limpar(null);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Erro ao guardar cliente", e);
            Alert alert = new Alert(Alert.AlertType.ERROR, "Erro ao guardar cliente: " + e.getMessage());
            alert.showAndWait();
        }
    }

    @FXML
    void eliminar(ActionEvent event) {
        if (clienteIdEmEdicao == 0) return;

        // Verificar se há carregamentos vinculados ao cliente
        if (todosCarregamentos != null) {
            for (Carregamento c : todosCarregamentos) {
                if (c != null && c.getCliente() != null && c.getCliente().getId() == clienteIdEmEdicao) {
                    Alert alertBloqueio = new Alert(Alert.AlertType.WARNING,
                            "Não é possível eliminar este cliente pois existem carregamentos/serviços registados para ele.");
                    alertBloqueio.setHeaderText("Operação Bloqueada");
                    alertBloqueio.showAndWait();
                    return;
                }
            }
        }

        Alert confirmacao = new Alert(Alert.AlertType.CONFIRMATION,
                "Tem certeza que deseja eliminar este cliente?", ButtonType.YES, ButtonType.NO);
        confirmacao.setHeaderText("Confirmação de Eliminação");
        Optional<ButtonType> resp = confirmacao.showAndWait();

        if (resp.isPresent() && resp.get() == ButtonType.YES) {
            try {
                Cliente c = new Cliente();
                c.setId(clienteIdEmEdicao);
                boolean removido = new ClienteDAO().remove(c);

                if (removido) {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION, "Cliente removido com sucesso!");
                    alert.setHeaderText("Sucesso");
                    alert.showAndWait();
                } else {
                    Alert alert = new Alert(Alert.AlertType.ERROR, "Cliente não encontrado na base de dados.");
                    alert.setHeaderText("Erro");
                    alert.showAndWait();
                }

                carregarClientes();
                preencherTabela();
                limpar(null);
            } catch (Exception e) {
                LOGGER.log(Level.SEVERE, "Erro ao eliminar cliente", e);
                Alert alert = new Alert(Alert.AlertType.ERROR, "Erro ao eliminar cliente: " + e.getMessage());
                alert.showAndWait();
            }
        }
    }

    @FXML
    void limpar(ActionEvent event) {
        clienteIdEmEdicao = 0;
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
        if (btnEliminar != null) {
            btnEliminar.setDisable(true);
        }
    }

    private void preencherTabela() {
        dados.clear();

        if (clientes != null) {
            for (Cliente c : clientes) {
                if (c != null) dados.add(c);
            }
            if (lblTotalTabela != null) {
                lblTotalTabela.setText("Total de clientes registados: " + clientes.getTamanho());
            }
        }
        observavel.setAll(dados);
    }

    @FXML
    void selecionarItem(MouseEvent event) {
        if (event.getClickCount() == 2) {
            Cliente c = tabela.getSelectionModel().getSelectedItem();
            if (c != null) {
                clienteIdEmEdicao = c.getId();
                txtNome.setText(c.getNome() != null ? c.getNome() : "");
                txtContacto.setText(c.getContacto() != null ? c.getContacto() : "");
                txtLocalizacao.setText(c.getEndereco() != null ? c.getEndereco() : "");

                btnActualizar.setAccessibleText(String.valueOf(c.getId()));
                btnActualizar.setDisable(false);
                if (btnEliminar != null) {
                    btnEliminar.setDisable(false);
                }

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
            eliminar(null);
        }
    }
}
