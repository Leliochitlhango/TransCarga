package controller;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;

public class MainViewController {

    // Pasta onde estão os .fxml (dentro de src/main/resources)
    private static final String PASTA_VIEWS = "/view/";

    @FXML
    private StackPane areaConteudo;
    @FXML
    private Button btnCamioes;
    @FXML
    private Button btnCarregamentos;
    @FXML
    private Button btnCarrinhas;
    @FXML
    private Button btnClientes;
    @FXML
    private Button btnNovoCarregamento;
    @FXML
    private Button btnPorProprietario;
    @FXML
    private Button btnProprietarios;
    @FXML
    private Button btnSair;
    @FXML
    private Button btnTotalVeiculo;
    @FXML
    private Button btnVeiculos;

    //private List<Button> botoesMenu;

    @FXML
    public void initialize(URL location, java.util.ResourceBundle resources) {
//        botoesMenu = List.of(
//                btnProprietarios, btnVeiculos, btnClientes,
//                btnCamioes, btnCarrinhas,
//                btnNovoCarregamento, btnCarregamentos,
//                btnPorProprietario, btnTotalVeiculo
//        );


// Tela inicial
        mostrarProprietarios();
    }

    @FXML
    void mostrarCamioes() {
        abrirTela("ListaCamioesView.fxml", btnCamioes);
    }

    @FXML
    void mostrarCarregamentos() {
        abrirTela("ListaCarregamentosView.fxml", btnCarregamentos);
    }

    @FXML
    void mostrarCarrinhas() {
        abrirTela("ListaCarrinhasView.fxml", btnCarrinhas);
    }

    @FXML
    void mostrarClientes() {
        abrirTela("ClienteView.fxml", btnCamioes);
    }

    @FXML
    void mostrarNovoCarregamento() {
        abrirTela("CarregamentoView.fxml", btnNovoCarregamento);
    }

    @FXML
    void mostrarPorProprietario() {
        abrirTela("TransportesPorProprietarioView.fxml", btnPorProprietario);
    }

    @FXML
    void mostrarProprietarios() {
        abrirTela("ProprietarioView.fxml", btnProprietarios);
    }

    @FXML
    void mostrarTotalVeiculo() {
        abrirTela("TotalPorVeiculoView.fxml", btnTotalVeiculo);
    }

    @FXML
    void mostrarVeiculos() {
        abrirTela("VeiculoView.fxml", btnVeiculos);
    }

    @FXML
    void sair(ActionEvent event) {
        Platform.exit();
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
            //marcarBotaoActivo(botaoActivo);
        } catch (IOException e) {
            e.printStackTrace();
            Alert alerta = new Alert(Alert.AlertType.ERROR,
                    "Não foi possível abrir a tela " + ficheiroFxml + ":\n" + e.getMessage());
            alerta.setHeaderText("Erro ao carregar tela");
            alerta.showAndWait();
        }
    }

//    private void marcarBotaoActivo(Button activo) {
//        for (Button b : botoesMenu) {
//            b.getStyleClass().remove("activo");
//        }
//        activo.getStyleClass().add("activo");
//    }
}
