package controller;

import dao.CamiaoDAO;
import dao.CarrinhaDAO;
import dao.ProprietarioDAO;
import dataStructure.ListaDuplamenteLigada;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import model.Camiao;
import model.Carrinha;
import model.Proprietario;
import util.Validador;

public class ProprietarioViewController {

    private static final Logger LOGGER = Logger.getLogger(ProprietarioViewController.class.getName());

    private final ListaDuplamenteLigada<Proprietario> dados = new ListaDuplamenteLigada<>();
    private final ObservableList<Proprietario> observavel = FXCollections.observableArrayList();
    private ListaDuplamenteLigada<Proprietario> listaProprietarios;
    private ListaDuplamenteLigada<Camiao> listaCamioes;
    private ListaDuplamenteLigada<Carrinha> listaCarrinhas;

    @FXML private Button btnActualizar;
    @FXML private Button btnEliminar;
    @FXML private Button btnGuardar;
    @FXML private Button btnLimpar;

    @FXML private TableView<Proprietario> tabela;
    @FXML private TableColumn<Proprietario, String> colContacto;
    @FXML private TableColumn<Proprietario, String> colDocumento;
    @FXML private TableColumn<Proprietario, String> colNome;
    @FXML private TableColumn<Proprietario, Integer> colNumVeiculos;
    @FXML private Label lblTotalTabela;

    @FXML private Label lblMensagem;
    @FXML private Label lblDocumento;
    @FXML private RadioButton rbBI;
    @FXML private RadioButton rbNUIT;
    @FXML private ToggleGroup tgTipoDocumento;

    @FXML private TextField txtContacto;
    @FXML private TextField txtDocumento;
    @FXML private TextField txtNome;

    private int proprietarioIdEmEdicao = 0;

    @FXML
    public void initialize() {
        Validador.aplicarMascaraTelefone(txtContacto);
        configurarEventosValidacao();
        configurarTabela();
        carregarDados();
        preencherTabela();
    }

    private void configurarTabela() {
        colNome.setCellValueFactory(new PropertyValueFactory<>("nome"));
        colContacto.setCellValueFactory(new PropertyValueFactory<>("contacto"));
        colDocumento.setCellValueFactory(new PropertyValueFactory<>("numeroDocumentoIdentificacao"));
        colNumVeiculos.setCellValueFactory(cellData -> 
            new SimpleIntegerProperty(contarVeiculosDoProprietario(cellData.getValue())).asObject()
        );
        tabela.setItems(observavel);
    }

    private void carregarDados() {
        try {
            listaProprietarios = new ProprietarioDAO().findAll();
            listaCamioes = new CamiaoDAO().findAll();
            listaCarrinhas = new CarrinhaDAO().findAll();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Erro ao carregar proprietários", e);
            Alert alert = new Alert(Alert.AlertType.ERROR, "Erro ao carregar dados dos proprietários: " + e.getMessage());
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

        // Restrição e formatação em tempo real no campo de documento
        if (txtDocumento != null) {
            txtDocumento.textProperty().addListener((obs, oldVal, newVal) -> {
                if (newVal == null) return;
                
                boolean isNuit = rbNUIT != null && rbNUIT.isSelected();
                if (isNuit) {
                    // NUIT: Apenas 9 dígitos numéricos
                    String digitos = newVal.replaceAll("\\D", "");
                    if (digitos.length() > 9) {
                        digitos = digitos.substring(0, 9);
                    }
                    if (!newVal.equals(digitos)) {
                        txtDocumento.setText(digitos);
                        txtDocumento.positionCaret(digitos.length());
                        return;
                    }
                } else {
                    // BI: 12-13 dígitos + 1 letra maiúscula
                    String upper = newVal.toUpperCase();
                    StringBuilder sb = new StringBuilder();
                    for (int i = 0; i < upper.length(); i++) {
                        char ch = upper.charAt(i);
                        int len = sb.length();
                        if (len < 12) {
                            if (Character.isDigit(ch)) sb.append(ch);
                        } else if (len == 12) {
                            if (Character.isDigit(ch) || (ch >= 'A' && ch <= 'Z')) sb.append(ch);
                        } else if (len == 13) {
                            char c13 = sb.charAt(12);
                            if (Character.isDigit(c13) && ch >= 'A' && ch <= 'Z') {
                                sb.append(ch);
                            }
                        }
                    }
                    String filtrado = sb.toString();
                    if (!newVal.equals(filtrado)) {
                        txtDocumento.setText(filtrado);
                        txtDocumento.positionCaret(filtrado.length());
                        return;
                    }
                }
                
                verificarDocumentoEmTempoReal();
            });
        }
    }

    @FXML
    void mudarTipoDocumento(ActionEvent event) {
        if (txtDocumento == null) return;
        txtDocumento.setText("");
        txtDocumento.getStyleClass().remove("campo-erro");
        limparMensagemSeContem("documento");

        if (rbNUIT != null && rbNUIT.isSelected()) {
            if (lblDocumento != null) lblDocumento.setText("Número de NUIT (9 dígitos)");
            txtDocumento.setPromptText("Ex.: 123456789");
        } else {
            if (lblDocumento != null) lblDocumento.setText("Número de BI (12-13 dígitos + letra)");
            txtDocumento.setPromptText("Ex.: 110100234567B");
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

        Proprietario dup = encontrarProprietarioPorContacto(contacto);
        if (dup != null && dup.getId() != proprietarioIdEmEdicao) {
            marcarErro(txtContacto, "Este contacto telefónico já pertence a outro proprietário!");
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

    private void verificarDocumentoEmTempoReal() {
        if (txtDocumento == null) return;
        String doc = txtDocumento.getText() != null ? txtDocumento.getText().trim().toUpperCase() : "";

        if (doc.isEmpty()) {
            txtDocumento.getStyleClass().remove("campo-erro");
            limparMensagemSeContem("documento");
            return;
        }

        Proprietario dup = encontrarProprietarioPorDocumento(doc);
        if (dup != null && dup.getId() != proprietarioIdEmEdicao) {
            marcarErro(txtDocumento, "Este documento já está registado para outro proprietário!");
            return;
        }

        boolean isNuit = rbNUIT != null && rbNUIT.isSelected();
        if (isNuit) {
            if (doc.length() == 9 && !Validador.isNUITValido(doc)) {
                marcarErro(txtDocumento, "NUIT inválido. Deve conter exactamente 9 dígitos.");
                return;
            }
        } else {
            if (doc.length() >= 13 && !Validador.isBIValido(doc)) {
                marcarErro(txtDocumento, "BI inválido. Deve conter 12 ou 13 dígitos seguidos por 1 letra maiúscula.");
                return;
            }
        }

        txtDocumento.getStyleClass().remove("campo-erro");
        limparMensagemSeContem("documento");
    }

    private Proprietario encontrarProprietarioPorContacto(String contacto) {
        if (contacto == null || listaProprietarios == null) return null;
        String limpo = contacto.replaceAll("\\D", "");

        for (Proprietario p : listaProprietarios) {
            if (p != null && p.getContacto() != null) {
                String cLimpo = p.getContacto().replaceAll("\\D", "");
                if (!cLimpo.isEmpty() && cLimpo.equals(limpo)) {
                    return p;
                }
            }
        }
        return null;
    }

    private Proprietario encontrarProprietarioPorDocumento(String doc) {
        if (doc == null || listaProprietarios == null) return null;
        String docLimpo = doc.replaceAll("\\s", "").toUpperCase();

        for (Proprietario p : listaProprietarios) {
            if (p != null && p.getNumeroDocumentoIdentificacao() != null) {
                String pDocLimpo = p.getNumeroDocumentoIdentificacao().replaceAll("\\s", "").toUpperCase();
                if (!pDocLimpo.isEmpty() && pDocLimpo.equals(docLimpo)) {
                    return p;
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
        String documento = txtDocumento.getText() != null ? txtDocumento.getText().trim().toUpperCase() : "";

        if (nome.isEmpty()) {
            marcarErro(txtNome, "Por favor, introduza o nome completo.");
            temErro = true;
        }

        if (contacto.isEmpty()) {
            marcarErro(txtContacto, "Por favor, introduza o contacto telefónico.");
            temErro = true;
        } else if (!Validador.isTelefoneValido(contacto)) {
            marcarErro(txtContacto, "Contacto inválido. Deve começar com 82..88 e ter 9 dígitos (ex: 84 123 4567).");
            temErro = true;
        } else {
            Proprietario dup = encontrarProprietarioPorContacto(contacto);
            if (dup != null && dup.getId() != proprietarioIdEmEdicao) {
                marcarErro(txtContacto, "Este contacto telefónico já está registado!");
                temErro = true;
            }
        }

        boolean isNuit = rbNUIT != null && rbNUIT.isSelected();
        if (documento.isEmpty()) {
            marcarErro(txtDocumento, isNuit ? "Por favor, introduza o NUIT (9 dígitos)." : "Por favor, introduza o BI (12-13 dígitos + letra).");
            temErro = true;
        } else if (isNuit) {
            if (!Validador.isNUITValido(documento)) {
                marcarErro(txtDocumento, "NUIT inválido! Deve conter exactamente 9 dígitos numéricos.");
                temErro = true;
            } else {
                Proprietario dupDoc = encontrarProprietarioPorDocumento(documento);
                if (dupDoc != null && dupDoc.getId() != proprietarioIdEmEdicao) {
                    marcarErro(txtDocumento, "Este NUIT já pertence a outro proprietário!");
                    temErro = true;
                }
            }
        } else {
            if (!Validador.isBIValido(documento)) {
                marcarErro(txtDocumento, "BI inválido! Deve conter 12 ou 13 dígitos seguidos por 1 letra maiúscula (ex: 110100234567B).");
                temErro = true;
            } else {
                Proprietario dupDoc = encontrarProprietarioPorDocumento(documento);
                if (dupDoc != null && dupDoc.getId() != proprietarioIdEmEdicao) {
                    marcarErro(txtDocumento, "Este BI já pertence a outro proprietário!");
                    temErro = true;
                }
            }
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
        if (proprietarioIdEmEdicao == 0 && btnActualizar.getAccessibleText() != null) {
            proprietarioIdEmEdicao = Integer.parseInt(btnActualizar.getAccessibleText());
        }
        if (!validarCampos()) return;

        try {
            Proprietario p = new Proprietario();
            p.setId(proprietarioIdEmEdicao);
            p.setNome(txtNome.getText().trim());
            p.setContacto(Validador.formatarTelefone(txtContacto.getText().trim()));
            p.setNumeroDocumentoIdentificacao(txtDocumento.getText().trim().toUpperCase());

            new ProprietarioDAO().update(p);

            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Proprietário actualizado com sucesso!");
            alert.setHeaderText("Sucesso");
            alert.showAndWait();

            carregarDados();
            preencherTabela();
            limpar(null);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Erro ao actualizar proprietário", e);
            Alert alert = new Alert(Alert.AlertType.ERROR, "Erro ao actualizar proprietário: " + e.getMessage());
            alert.showAndWait();
        }
    }

    @FXML
    void guardar(ActionEvent event) {
        proprietarioIdEmEdicao = 0;
        if (!validarCampos()) return;

        try {
            Proprietario p = new Proprietario();
            p.setNome(txtNome.getText().trim());
            p.setContacto(Validador.formatarTelefone(txtContacto.getText().trim()));
            p.setNumeroDocumentoIdentificacao(txtDocumento.getText().trim().toUpperCase());

            new ProprietarioDAO().save(p);

            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Proprietário registado com sucesso!");
            alert.setHeaderText("Sucesso");
            alert.showAndWait();

            carregarDados();
            preencherTabela();
            limpar(null);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Erro ao guardar proprietário", e);
            Alert alert = new Alert(Alert.AlertType.ERROR, "Erro ao guardar proprietário: " + e.getMessage());
            alert.showAndWait();
        }
    }

    @FXML
    void eliminar(ActionEvent event) {
        if (proprietarioIdEmEdicao == 0) return;

        Proprietario pParaEliminar = null;
        if (listaProprietarios != null) {
            for (Proprietario p : listaProprietarios) {
                if (p != null && p.getId() == proprietarioIdEmEdicao) {
                    pParaEliminar = p;
                    break;
                }
            }
        }

        // Validação de integridade referencial: não permitir apagar proprietário com veículos associados
        if (pParaEliminar != null && contarVeiculosDoProprietario(pParaEliminar) > 0) {
            Alert alertBloqueio = new Alert(Alert.AlertType.WARNING,
                    "Não é possível eliminar este proprietário porque existem veículos associados a ele no sistema.\n" +
                    "Por favor, reatribua ou remova primeiro os veículos correspondentes.");
            alertBloqueio.setHeaderText("Operação Bloqueada");
            alertBloqueio.showAndWait();
            return;
        }

        Alert confirmacao = new Alert(Alert.AlertType.CONFIRMATION,
                "Tem certeza que deseja eliminar este proprietário?", ButtonType.YES, ButtonType.NO);
        confirmacao.setHeaderText("Confirmação de Eliminação");
        Optional<ButtonType> resp = confirmacao.showAndWait();

        if (resp.isPresent() && resp.get() == ButtonType.YES) {
            try {
                Proprietario p = new Proprietario();
                p.setId(proprietarioIdEmEdicao);
                boolean removido = new ProprietarioDAO().remove(p);

                if (removido) {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION, "Proprietário eliminado com sucesso!");
                    alert.setHeaderText("Sucesso");
                    alert.showAndWait();
                } else {
                    Alert alert = new Alert(Alert.AlertType.ERROR, "Não foi possível encontrar o proprietário na base de dados.");
                    alert.setHeaderText("Erro");
                    alert.showAndWait();
                }

                carregarDados();
                preencherTabela();
                limpar(null);
            } catch (Exception e) {
                LOGGER.log(Level.SEVERE, "Erro ao eliminar proprietário", e);
                Alert alert = new Alert(Alert.AlertType.ERROR, "Erro ao eliminar proprietário: " + e.getMessage());
                alert.showAndWait();
            }
        }
    }

    @FXML
    void limpar(ActionEvent event) {
        proprietarioIdEmEdicao = 0;
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
        if (btnEliminar != null) {
            btnEliminar.setDisable(true);
        }
    }

    @FXML
    void selecionarItem(MouseEvent event) {
        if (event.getClickCount() == 2) {
            Proprietario p = tabela.getSelectionModel().getSelectedItem();
            if (p != null) {
                proprietarioIdEmEdicao = p.getId();
                txtNome.setText(p.getNome() != null ? p.getNome() : "");
                txtContacto.setText(p.getContacto() != null ? p.getContacto() : "");
                
                String doc = p.getNumeroDocumentoIdentificacao() != null ? p.getNumeroDocumentoIdentificacao() : "";
                txtDocumento.setText(doc);

                // Detecta se é NUIT ou BI para ajustar o RadioButton
                if (Validador.isNUITValido(doc)) {
                    if (rbNUIT != null) rbNUIT.setSelected(true);
                    if (lblDocumento != null) lblDocumento.setText("Número de NUIT (9 dígitos)");
                } else {
                    if (rbBI != null) rbBI.setSelected(true);
                    if (lblDocumento != null) lblDocumento.setText("Número de BI (12-13 dígitos + letra)");
                }

                btnActualizar.setDisable(false);
                btnActualizar.setAccessibleText(String.valueOf(p.getId()));
                if (btnEliminar != null) {
                    btnEliminar.setDisable(false);
                }

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
        dados.clear();

        if (listaProprietarios != null) {
            for (Proprietario p : listaProprietarios) {
                if (p != null) dados.add(p);
            }
            if (lblTotalTabela != null) {
                lblTotalTabela.setText("Total de proprietários registados: " + listaProprietarios.getTamanho());
            }
        }
        observavel.setAll(dados);
    }
}
