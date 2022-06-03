package com.example.appgraph;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.shape.ArcType;
import javafx.stage.Stage;
import javafx.scene.canvas.*;
//
import javafx.fxml.FXML;
import javafx.scene.paint.Color;
import javafx.scene.Group;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class AppView extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(AppView.class.getResource("hello-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 750, 750);
        stage.setTitle("Graph");
        stage.setScene(scene);
        stage.setResizable(false);

        Controllers test = new Controllers();

        stage.show();


    }

    public static void main(String[] args) {
        launch();

    }
}
