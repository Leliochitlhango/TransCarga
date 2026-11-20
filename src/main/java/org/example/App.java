package org.example;

import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class App extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/view/LoginView.fxml"));
        stage.setScene(new Scene(root));
        stage.setTitle("TransCarga Moçambique - Iniciar Sessão");
        stage.setMinWidth(1100);
        stage.setMinHeight(680);
        var iconStream = getClass().getResourceAsStream("/Icones/ded851f6070fc383c2e19e79619e0004.jpg");
        if (iconStream != null) {
            stage.getIcons().add(new Image(iconStream));
        }
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
