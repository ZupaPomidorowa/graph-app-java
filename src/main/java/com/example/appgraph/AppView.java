package com.example.appgraph;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;

/**
 * Class creating a window for the app.
 */
public class AppView extends Application {
    /**
     * Starts the app window.
     * @param stage app window
     * @throws IOException in case of an FXML loading error
     */
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(AppView.class.getResource("hello-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 750, 750);
        stage.setTitle("Graph");
        stage.setScene(scene);
        stage.setResizable(false);

        //Controllers test = new Controllers();

        stage.show();


    }

    /**
     * Launches the app
     * @param args command line arguments
     */
    public static void main(String[] args) {
        launch();

    }
}
