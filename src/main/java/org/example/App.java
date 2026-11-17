package org.example;

import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class App extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/view/LoginView.fxml"));
        stage.setScene(new Scene(root));
        stage.setTitle("TransCarga Moçambique - Iniciar Sessão");
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
