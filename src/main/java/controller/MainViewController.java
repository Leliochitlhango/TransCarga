package controller;

import java.io.IOException;
import java.net.URL;
import java.util.List;
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
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class MainViewController {

    private static final Logger LOGGER = Logger.getLogger(MainViewController.class.getName());

    // Pasta onde estão os .fxml (dentro de src/main/resources)
    private static final String PASTA_VIEWS = "/view/";

    @FXML
    private StackPane areaConteudo;

    @FXML
    private Button btnDashboard;
    @FXML
    private Button btnProprietarios;
    @FXML
    private Button btnVeiculos;
    @FXML
    private Button btnClientes;
    @FXML
    private Button btnNovoCarregamento;
    @FXML
    private Button btnHistoricoCarregamentos;
    @FXML
    private Button btnConsultas;
    @FXML
    private Button btnCamioes;
    @FXML
    private Button btnCarrinhas;
    @FXML
    private Button btnTotalVeiculo;

    @FXML
    private Button btnSair;
    @FXML
    private Button btnTema;

    private List<Button> botoesMenu;

    @FXML
    public void initialize() {
        botoesMenu = List.of(
            btnDashboard,
            btnProprietarios,
            btnVeiculos,
            btnClientes,
            btnNovoCarregamento,
            btnHistoricoCarregamentos,
            btnConsultas,
            btnCamioes,
            btnCarrinhas,
            btnTotalVeiculo
        );
        mostrarDashboard();
        atualizarTextoBotaoTema();
    }

    @FXML
    void alternarModoEscuro(ActionEvent event) {
        Scene scene = btnTema != null && btnTema.getScene() != null ? btnTema.getScene() : areaConteudo.getScene();
        if (scene == null) return;
        LoginViewController.modoEscuroAtivo = !LoginViewController.modoEscuroAtivo;
        LoginViewController.aplicarTema(scene, LoginViewController.modoEscuroAtivo);
        atualizarTextoBotaoTema();
    }

    private void atualizarTextoBotaoTema() {
        if (btnTema != null) {
            btnTema.setText(LoginViewController.modoEscuroAtivo ? "Modo Claro" : "Modo Escuro");
        }
    }

    @FXML
    void mostrarDashboard() {
        abrirTela("DashboardView.fxml", btnDashboard);
    }

    @FXML
    void mostrarProprietarios() {
        abrirTela("ProprietarioView.fxml", btnProprietarios);
    }

    @FXML
    void mostrarVeiculos() {
        abrirTela("VeiculoView.fxml", btnVeiculos);
    }

    @FXML
    void mostrarClientes() {
        abrirTela("ClienteView.fxml", btnClientes);
    }

    @FXML
    void mostrarNovoCarregamento() {
        abrirTela("CarregamentoView.fxml", btnNovoCarregamento);
    }

    @FXML
    void mostrarHistoricoCarregamentos() {
        abrirTela("ListaCarregamentosView.fxml", btnHistoricoCarregamentos);
    }

    @FXML
    void mostrarConsultas() {
        abrirTela("ConsultasView.fxml", btnConsultas);
    }

    @FXML
    void mostrarCamioes() {
        abrirTela("ListaCamioesView.fxml", btnCamioes);
    }

    @FXML
    void mostrarCarrinhas() {
        abrirTela("ListaCarrinhasView.fxml", btnCarrinhas);
    }

    @FXML
    void mostrarTotalVeiculo() {
        abrirTela("TotalPorVeiculoView.fxml", btnTotalVeiculo);
    }

    @FXML
    void sair(ActionEvent event) {
        try {
            URL url = getClass().getResource(PASTA_VIEWS + "LoginView.fxml");
            if (url == null) return;
            Parent root = FXMLLoader.load(url);
            Scene scene = areaConteudo != null ? areaConteudo.getScene() : null;
            if (scene != null) {
                scene.setRoot(root);
                Stage stage = (Stage) scene.getWindow();
                if (stage != null) {
                    stage.setTitle("TransCarga Moçambique - Iniciar Sessão");
                }
            }
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Erro ao retornar ao ecrã de início de sessão", e);
        }
    }

    // ---------- Lógica comum ----------
    private void abrirTela(String ficheiroFxml, Button botaoActivo) {
        try {
            URL url = getClass().getResource(PASTA_VIEWS + ficheiroFxml);
            if (url == null) {
                throw new IOException("Ficheiro não encontrado: " + PASTA_VIEWS + ficheiroFxml);
            }
            Node tela = FXMLLoader.load(url);
            areaConteudo.getChildren().setAll(tela);
            marcarBotaoActivo(botaoActivo);
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Erro ao abrir tela: " + ficheiroFxml, e);
            Alert alerta = new Alert(Alert.AlertType.ERROR,
                    "Não foi possível abrir a tela " + ficheiroFxml + ":\n" + e.getMessage());
            alerta.setHeaderText("Erro ao carregar tela");
            alerta.showAndWait();
        }
    }

    private void marcarBotaoActivo(Button activo) {
        if (botoesMenu == null) return;
        for (Button b : botoesMenu) {
            if (b != null) {
                b.getStyleClass().remove("item-menu-ativo");
            }
        }
        if (activo != null && !activo.getStyleClass().contains("item-menu-ativo")) {
            activo.getStyleClass().add("item-menu-ativo");
        }
    }
}
