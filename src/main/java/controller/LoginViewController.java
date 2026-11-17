package controller;

import java.io.IOException;
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

    // Credenciais do utilizador único do sistema
    public static final String USUARIO_VALIDO = "admin";
    public static final String USUARIO_EMAIL_VALIDO = "admin@transcarga.co.mz";
    public static final String SENHA_VALIDA = "transcarga2026";

    @FXML
    private Button btnEntrar;

    @FXML
    private Label lblErro;

    @FXML
    private PasswordField txtSenha;

    @FXML
    private TextField txtUsuario;

    @FXML
    public void initialize() {
        if (lblErro != null) {
            lblErro.setText("");
        }

        // Eventos para remover estilo de erro ao digitar
        if (txtUsuario != null) {
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

        if (erro) {
            return;
        }

        // Validação das credenciais do utilizador único
        boolean usuarioCorreto = usuario.equalsIgnoreCase(USUARIO_VALIDO) 
                              || usuario.equalsIgnoreCase(USUARIO_EMAIL_VALIDO)
                              || usuario.equalsIgnoreCase("lelio");
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
                stage.setScene(new Scene(root));
                stage.setTitle("TransCarga Moçambique - Gestão de Frotas");
                stage.centerOnScreen();
                stage.show();
            }
        } catch (IOException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR, "Erro ao abrir a tela principal: " + e.getMessage());
            alert.showAndWait();
        }
    }
}
