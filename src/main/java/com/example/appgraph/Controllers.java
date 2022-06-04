package com.example.appgraph;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.stage.FileChooser;

import javafx.scene.paint.Color;

import javafx.scene.control.*;
import javafx.stage.Stage;


import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import static java.lang.Integer.parseInt;

public class Controllers implements Initializable {

    private static final double BLUE_HUE = Color.DARKBLUE.getHue();

    private static final double RED_HUE = Color.RED.getHue();
    GeneratedGraph gg;
    FileChooser fileChooser = new FileChooser();
    private GraphicsContext gc;

    @FXML
    void importFile(ActionEvent event) {
        File file = fileChooser.showOpenDialog(new Stage());
        //graph = new GraphReader(file.getAbsolutePath()).readGraph(); //czytanie grafu z pliku
        //graph.printGraph(graph);   wypisanie grafu test
    }

    //@FXML
    //private Canvas cw1 = new Canvas(610,610);

    @FXML
    private ScrollPane scrollPane = new ScrollPane();
    @FXML
    private TextField min;

    @FXML
    private TextField max;

    @FXML
    private TextField rowsField;

    @FXML
    private TextField columnsField;

   @FXML
    void generate(ActionEvent event) throws IOException {
       int rows = parseInt(rowsField.getText());
       int columns = parseInt(columnsField.getText());
       int w1 = parseInt(min.getText());
       int w2 = parseInt(max.getText());

       gg = new Graph(rows, columns, w1, w2);
       gg.generateGraph();

       /*tests*/
       System.out.println(rows);
       System.out.println(columns);
       System.out.println(w1);
       System.out.println(w2);

       gg.printGraph();

       System.out.println("Start draw Graph");
       drawGraph();
       System.out.println("End draw Graph");


  }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        fileChooser.setInitialDirectory(new File("D:\\"));
    }

    @FXML
    void connectivity (ActionEvent event) {
       //co tutaj zrobic ?? zaznaczanie zmienic na przycisk ???
    }


    public void drawGraph () {
        //Group root = new Group();

        Canvas cw1 = new Canvas(gg.getRows() * 30, gg.getColumns() * 30);

        //scrollPane = new ScrollPane(cw1);
        scrollPane.setContent(cw1);
        gc = cw1.getGraphicsContext2D();

        gc.setFill(Color.BLACK);
        gc.fillRect(0,0, cw1.getWidth(),cw1.getHeight());

        double width = cw1.getWidth();
        double height = cw1.getHeight();
        double right = width/(gg.getColumns()); //-1
        double down = height/(gg.getRows());  //-1

        System.out.println(gg.getRows());
        System.out.println(gg.getColumns());
        System.out.println(right);
        System.out.println(down);

        //double r = 0.8*Math.sqrt(width*height/2*Math.PI* gg.getGraphSize());
        //double r = 20;
        double r = height/(2*gg.getRows()-1);

        System.out.println("r: " + r);

        for (int i = 0; i <gg.getRows() ; i++) {
            for (int j = 0; j < gg.getColumns() - 1; j++) {
                gc.setFill(getWeightColour(gg.getVertex(i*gg.getColumns()+j).getWeight(Vertex.RIGHT)));
                gc.fillRect(0.5 * r + right * j, 0.5 * r + down * i, right, r * 0.1);
            }
        }

        for (int j = 0; j<gg.getColumns(); j++) {
            for (int i = 0; i < gg.getRows() - 1; i++) {
                gc.setFill(getWeightColour(gg.getVertex(i*gg.getColumns()+j).getWeight(Vertex.LOWER)));
                gc.fillRect(0.45 * r + right * j, 0.5 * r + down * i, r * 0.1, down);
            }
        }

        gc.setFill(Color.WHITE);
        for(int i =0; i<gg.getRows()+1; i++){
            for (int j = 0; j<gg.getColumns()+1; j++) {
                gc.fillOval(right*j, down*i, r, r);
            }
        }







        /*
        for (int i = 0; i<gg.getColumns()-1; i++) {
            gc.fillRect(0.5*r, 0.5*r+down*i, down, r*0.1);
        }

         */




        /*
        for(int i = 0; i < gg.getRows(); i++) {
            for (int j = 0; j < gg.getColumns()-1; j++) {

                gc.fillRect(right*j*r*0.5, down*i*r*0.5, right, r*0.25);
            }
        }

         */

        //root.getChildren().add(cw1);

    }

    public void clearCanva () {
       //funkcja czyszczaca kanwe
    }

    // funkcja zaznaczajaca wierzcholki

    public Color getWeightColour(double weight) {
       double hue = (RED_HUE - BLUE_HUE) * (weight - gg.getWeightLower()) / (gg.getWeightUpper() - gg.getWeightLower()) + BLUE_HUE;
       return Color.hsb(hue, 1.0, 1.0);
    }

}

