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

import java.util.regex.*;


import java.io.*;
import java.net.URL;
import java.util.ResourceBundle;

import static java.lang.Integer.parseInt;

public class Controllers implements Initializable {
    GeneratedGraph gg;
    ReadGraph rg;
    FileChooser fileChooser = new FileChooser();
    private GraphicsContext gc;

    @FXML
    private Canvas cw1 = new Canvas(610,610);

    @FXML
    private TextField min;

    @FXML
    private TextField max;

    @FXML
    private TextField rowsField;

    @FXML
    private TextField columnsField;

    @FXML
    private CheckBox checkConnect;

    @FXML
    private Label myLabel;

    @FXML
    void generate(ActionEvent event) throws IOException {
        int rows = parseInt(rowsField.getText());
        int columns = parseInt(columnsField.getText());
        int w1 = parseInt(min.getText());
        int w2 = parseInt(max.getText());

        gg = new Graph(rows, columns, w1, w2);
        gg.generateGraph();

        //test - printing rows, columns, w1, w2
        System.out.println("Rows " + rows);
        System.out.println("Columns " + columns);
        System.out.println("w1 " + w1);
        System.out.println("w2 " + w2);

        gg.printGraph();

        drawGraph();
    }

    @FXML
    void importFile(ActionEvent event) {
        File selectedFile = fileChooser.showOpenDialog(null);

        System.out.println(selectedFile.getAbsolutePath());

        rg = new Graph();

        try {
            FileReader reader = new FileReader(selectedFile.getAbsolutePath());
            rg.readGraph(reader);
        } catch (IOException e) {
            System.out.println("Incorrect file format." + e);
        }

        //test - printing imported graph
        rg.printGraph();

        drawGraph2();

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        fileChooser.setInitialDirectory(new File("C:\\"));
    }


    @FXML
    void change (ActionEvent event) {    //check box connectivity
        if (checkConnect.isSelected()) {
            Connectivity bfs = new Connectivity((Graph) gg);
            if(bfs.isConnected((Graph) gg)) {
                myLabel.setText("The graph is connected.");
                System.out.println("The graph is connected.");
            } else {
                myLabel.setText("The graph is disconnected.");
                System.out.println("The graph is disconnected.");
            }

        }
        else {
            myLabel.setText("");
        }

    }


    public void drawGraph () {
        gc = cw1.getGraphicsContext2D();
        gc.setFill(Color.valueOf("blue"));
        gc.clearRect(0,0, cw1.getWidth(),cw1.getHeight());

        double width = cw1.getWidth();
        double height = cw1.getHeight();
        double right = width/(gg.getColumns());  //intervals horizontal
        double down = height/(gg.getRows());     //intervals vertical
        double r = height/(2*gg.getRows()-1);    // r = 2*radius

        //test - rows, columns, intervals horizontal (right), intervals vertical (down), r
        System.out.println("Rows " + gg.getRows());
        System.out.println("Columns " + gg.getColumns());
        System.out.println("Intervals horizontal (right) " + right);
        System.out.println("Intervals vertical (down) " + down);
        System.out.println("r " + r);

        //draw nodes
        for(int i =0; i<gg.getRows()+1; i++){
            for (int j = 0; j<gg.getColumns()+1; j++) {
                gc.fillOval(right*j, down*i, r, r);
            }
        }

        //draw edges horizontal
        for (int i = 0; i <gg.getRows() ; i++) {
            for (int j = 0; j < gg.getColumns() - 1; j++) {
                gc.fillRect(0.5 * r + right * j, 0.5 * r + down * i, right, r * 0.1);
            }
        }

        //draw edges vertical
        for (int j = 0; j<gg.getColumns(); j++) {
            for (int i = 0; i < gg.getRows() - 1; i++) {
                gc.fillRect(0.45 * r + right * j, 0.5 * r + down * i, r * 0.1, down);
            }
        }

    }

    void drawGraph2() {
        gc = cw1.getGraphicsContext2D();
        gc.setFill(Color.valueOf("blue"));
        gc.clearRect(0,0, cw1.getWidth(),cw1.getHeight());

        double width = cw1.getWidth();
        double height = cw1.getHeight();
        double right = width/(rg.getColumns()); //intervals horizontal
        double down = height/(rg.getRows());    //intervals vertical
        double r = height/(2*rg.getRows()-1);   //r = 2*radius

        //test - rows, columns, intervals horizontal (right), intervals vertical (down), r
        System.out.println("Rows " + rg.getRows());
        System.out.println("Columns " + rg.getColumns());
        System.out.println("Intervals horizontal (right) " + right);
        System.out.println("Intervals vertical (down) " + down);
        System.out.println("r " + r);

        //draw nodes
        for(int i =0; i<rg.getRows()+1; i++) {
            for (int j = 0; j < rg.getColumns() + 1; j++) {
                gc.fillOval(right * j, down * i, r, r);
            }
        }

        //draw edges horizontal
        for (int i = 0; i <rg.getRows() ; i++) {
            for (int j = 0; j < rg.getColumns() - 1; j++) {
                gc.fillRect(0.5 * r + right * j, 0.5 * r + down * i, right, r * 0.1);
            }
        }

        //draw edges vertical
        for (int j = 0; j<rg.getColumns(); j++) {
            for (int i = 0; i < rg.getRows() - 1; i++) {
                gc.fillRect(0.45 * r + right * j, 0.5 * r + down * i, r * 0.1, down);
            }
        }

    }


    @FXML
    public void saveGraph (ActionEvent event) throws FileNotFoundException, UnsupportedEncodingException {
        File selectedFile = fileChooser.showSaveDialog(null);

        //test - print path to file
        System.out.println(selectedFile.getAbsolutePath());

        String value = selectedFile.getAbsolutePath();
        PrintWriter writer = new PrintWriter(value, "UTF-8");
        gg.writeGraph(writer);

        //test - print graph wrote to the file
        gg.printGraph();


    }


}

