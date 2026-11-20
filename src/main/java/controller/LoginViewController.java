package controller;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class LoginViewController {

    private static final Logger LOGGER = Logger.getLogger(LoginViewController.class.getName());

    public static final String USUARIO_VALIDO = "admin";
    public static final String USUARIO_EMAIL_VALIDO = "admin@transcarga.co.mz";
    public static final String SENHA_VALIDA = "transcarga2026";

    // Variável estática para persistir estado do tema entre telas
    public static boolean modoEscuroAtivo = false;
    public static String ultimoUtilizador = "";

    @FXML private Button btnEntrar;
    @FXML private Button btnTemaLogin;
    @FXML private Label lblErro;
    @FXML private PasswordField txtSenha;
    @FXML private TextField txtUsuario;

    @FXML
    public void initialize() {
        if (lblErro != null) {
            lblErro.setText("");
        }

        if (txtUsuario != null) {
            if (ultimoUtilizador != null && !ultimoUtilizador.isEmpty()) {
                txtUsuario.setText(ultimoUtilizador);
            }
            txtUsuario.textProperty().addListener((obs, oldVal, newVal) -> {
                txtUsuario.getStyleClass().remove("campo-erro");
                if (lblErro != null) lblErro.setText("");
            });
            txtUsuario.setOnAction(e -> {
                if (txtSenha != null) txtSenha.requestFocus();
            });
        }

        if (txtSenha != null) {
            txtSenha.textProperty().addListener((obs, oldVal, newVal) -> {
                txtSenha.getStyleClass().remove("campo-erro");
                if (lblErro != null) lblErro.setText("");
            });
            txtSenha.setOnAction(this::entrar);
        }
    }

    @FXML
    void alternarTemaLogin(ActionEvent event) {
        Scene scene = btnTemaLogin.getScene();
        if (scene == null) return;

        modoEscuroAtivo = !modoEscuroAtivo;
        aplicarTema(scene, modoEscuroAtivo);

        if (btnTemaLogin != null) {
            btnTemaLogin.setText(modoEscuroAtivo ? "Modo Claro" : "Modo Escuro");
        }
    }

    public static void aplicarTema(Scene scene, boolean escuro) {
        if (scene == null) return;
        String caminhoTemaEscuro = LoginViewController.class.getResource("/css/tema-escuro.css").toExternalForm();

        if (escuro) {
            if (!scene.getStylesheets().contains(caminhoTemaEscuro)) {
                scene.getStylesheets().add(caminhoTemaEscuro);
            }
            if (scene.getRoot() != null && !scene.getRoot().getStyleClass().contains("modo-escuro")) {
                scene.getRoot().getStyleClass().add("modo-escuro");
            }
        } else {
            scene.getStylesheets().remove(caminhoTemaEscuro);
            if (scene.getRoot() != null) {
                scene.getRoot().getStyleClass().remove("modo-escuro");
            }
        }
    }

    @FXML
    void entrar(ActionEvent event) {
        String usuario = txtUsuario != null ? txtUsuario.getText().trim() : "";
        String senha = txtSenha != null ? txtSenha.getText() : "";

        boolean erro = false;

        if (usuario.isEmpty()) {
            if (txtUsuario != null && !txtUsuario.getStyleClass().contains("campo-erro")) {
                txtUsuario.getStyleClass().add("campo-erro");
            }
            if (lblErro != null) lblErro.setText("Por favor, introduza o utilizador.");
            erro = true;
        }

        if (senha.isEmpty()) {
            if (txtSenha != null && !txtSenha.getStyleClass().contains("campo-erro")) {
                txtSenha.getStyleClass().add("campo-erro");
            }
            if (lblErro != null && !erro) lblErro.setText("Por favor, introduza a palavra-passe.");
            erro = true;
        }

        if (erro) return;

        boolean usuarioCorreto = usuario.equalsIgnoreCase(USUARIO_VALIDO) 
                              || usuario.equalsIgnoreCase(USUARIO_EMAIL_VALIDO);
        boolean senhaCorreta = senha.equals(SENHA_VALIDA);

        if (!usuarioCorreto || !senhaCorreta) {
            if (txtUsuario != null && !txtUsuario.getStyleClass().contains("campo-erro")) {
                txtUsuario.getStyleClass().add("campo-erro");
            }
            if (txtSenha != null && !txtSenha.getStyleClass().contains("campo-erro")) {
                txtSenha.getStyleClass().add("campo-erro");
            }
            if (lblErro != null) {
                lblErro.setText("Credenciais inválidas. Verifique o utilizador e a palavra-passe.");
            }
            return;
        }

        ultimoUtilizador = usuario;

        // Credenciais corretas - Abrir a janela principal
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/view/MainView.fxml"));
            Stage stage = null;
            if (event.getSource() instanceof Node) {
                stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            } else if (btnEntrar != null && btnEntrar.getScene() != null) {
                stage = (Stage) btnEntrar.getScene().getWindow();
            }

            if (stage != null) {
                Scene novaCena = new Scene(root);
                if (modoEscuroAtivo) {
                    aplicarTema(novaCena, true);
                }
                stage.setScene(novaCena);
                stage.setTitle("TransCarga Moçambique");
                stage.setMinWidth(1100);
                stage.setMinHeight(680);
                var iconStream = getClass().getResourceAsStream("/Icones/ded851f6070fc383c2e19e79619e0004.jpg");
                if (iconStream != null && stage.getIcons().isEmpty()) {
                    stage.getIcons().add(new javafx.scene.image.Image(iconStream));
                }
                stage.centerOnScreen();
                stage.show();
            }
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Erro ao abrir a tela principal", e);
            Alert alert = new Alert(Alert.AlertType.ERROR, "Erro ao abrir a tela principal: " + e.getMessage());
            alert.showAndWait();
        }
    }
}
