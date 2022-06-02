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

public class HelloApplication extends Application {
    @FXML public Canvas cw1;
    //
    //@FXML public GraphicsContext gc;

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));
        //
        Scene scene1 = new Scene(fxmlLoader.load(), 600, 400);

        stage.setTitle("Graph");
        // GraphicsContext gc = cw1.getGraphicsContext2D();

        // create a canvas

        int g_row=3;
        int g_col=4;
        int size=50;
        int c_row=g_row*size + 2 *size;
        int c_col=g_col*size + 2 *size;

        Canvas canvas = new Canvas(c_row, c_col);

        // graphics context
        GraphicsContext graphics_context = canvas.getGraphicsContext2D();
        drawGraph(graphics_context,g_row,g_col);
// set fill for rectangle
/*         graphics_context.setFill(Color.RED);
        graphics_context.fillRect(20, 20, 70, 70);

        // set fill for oval
        graphics_context.setFill(Color.BLUE);
        graphics_context.fillOval(30, 30, 70, 70);
// create a Group

 */
        Group group = new Group(canvas);

        // create a scene
        Scene scene = new Scene(group, 200, 200);

        stage.setScene(scene);
        /*
        stage.setScene(scene1); */
        stage.show();

    }


    private void drawGraph (GraphicsContext gc, int row, int columns) {
        gc.setFill(Color.GREEN);
        gc.setStroke(Color.BLUE);
        gc.setLineWidth(5);
    //    gc.strokeLine(40, 10, 10, 40);
        for (int j=0; j<columns; j++)
            for (int i=0; i<row; i++)
             { gc.fillOval(10 + i*50, 60 +j*50, 30, 30);
        }
        //gc.strokeOval(60, 60, 30, 30);
    /*    gc.fillRoundRect(110, 60, 30, 30, 10, 10);
        gc.strokeRoundRect(160, 60, 30, 30, 10, 10);
        gc.fillArc(10, 110, 30, 30, 45, 240, ArcType.OPEN);
        gc.fillArc(60, 110, 30, 30, 45, 240, ArcType.CHORD);
        gc.fillArc(110, 110, 30, 30, 45, 240, ArcType.ROUND);
        gc.strokeArc(10, 160, 30, 30, 45, 240, ArcType.OPEN);
        gc.strokeArc(60, 160, 30, 30, 45, 240, ArcType.CHORD);
        gc.strokeArc(110, 160, 30, 30, 45, 240, ArcType.ROUND);
        gc.fillPolygon(new double[]{10, 40, 10, 40},
                new double[]{210, 210, 240, 240}, 4);
        gc.strokePolygon(new double[]{60, 90, 60, 90},
                new double[]{210, 210, 240, 240}, 4);
        gc.strokePolyline(new double[]{110, 140, 110, 140},
                new double[]{210, 210, 240, 240}, 4);*/
    }

    public static void main(String[] args) {
        launch();
    }
}