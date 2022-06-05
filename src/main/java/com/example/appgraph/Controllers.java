package com.example.appgraph;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.stage.FileChooser;
import javafx.scene.paint.Color;
import javafx.scene.control.*;
import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import static java.lang.Double.parseDouble;
import static java.lang.Integer.parseInt;
import static javafx.scene.paint.Color.*;

public class Controllers implements Initializable {

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
    private TextField segmentsField;

    @FXML
    private Button saveFile;

    @FXML
    private Button checkConnectivity;

    @FXML
    private Label connectivityInfo;

    @FXML
    private Label weightLowerLabel;

    @FXML
    private Label weightUpperLabel;

    GeneratedGraph gg;
    ReadGraph rg;
    FileChooser fileChooser = new FileChooser();

    Canvas canvas;
    GraphicsContext gc;

    ArrayList<NodeXY> listXY =new ArrayList<>();


    private static final double BLUE = Color.DARKBLUE.getHue();
    private static final double RED = Color.RED.getHue();

    private static final int POINT_SIZE = 20;
    private static final int EDGE_LENGTH = 20;
    private static final int EDGE_THICKNESS = 5;
    private static final int PADDING = 20;

    @FXML
    void generate() {
        try {
            int rows = parseInt(rowsField.getText());
            int columns = parseInt(columnsField.getText());
            double w1 = parseDouble(min.getText());
            double w2 = parseDouble(max.getText());
            int segments = parseInt(segmentsField.getText());

            if (rows * columns > 10000 || rows <= 0 || columns <= 0 ||
                    w1 >= w2 || w1 <= 0.0 || w2 >= 100.0 ||
                    segments <= 0 || segments > 10) {
                throw new IllegalArgumentException();
            }

            gg = new Graph(rows, columns, w1, w2);
            gg.generateGraph();

            //test - printing rows, columns, w1, w2
            System.out.println("Rows " + rows);
            System.out.println("Columns " + columns);
            System.out.println("w1 " + w1);
            System.out.println("w2 " + w2);

            gg.printGraph();

            updateWeightLabels((Graph) gg);

            //splitting graph
            if (segments > 1) {
                gg.splitGraph();
            }

            drawGraph((Graph) gg);
            saveFile.setDisable(false);
            checkConnectivity.setDisable(false);
            connectivityInfo.setText("");
        } catch (NumberFormatException e) {
            System.err.println("Incorrect argument format." + e);
            Alert popUp = new Alert(Alert.AlertType.ERROR);
            popUp.setTitle("Error");
            popUp.setHeaderText("Incorrect argument format");
            popUp.setContentText("Please provide arguments in the correct format.");
            popUp.showAndWait();
        } catch (IllegalArgumentException e) {
            System.err.println("Arguments out of range." + e);
            Alert popUp = new Alert(Alert.AlertType.ERROR);
            popUp.setTitle("Error");
            popUp.setHeaderText("Arguments out of range");
            popUp.setContentText("Please provide arguments only in the allowed range.");
            popUp.showAndWait();
        }
    }

    @FXML
    void importFile() {
        saveFile.setDisable(true);
        File selectedFile = fileChooser.showOpenDialog(null);

        System.out.println(selectedFile.getAbsolutePath());

        gg = null;
        rg = new Graph();

        try {
            FileReader reader = new FileReader(selectedFile.getAbsolutePath());
            rg.readGraph(reader);
            //test - printing imported graph
            rg.printGraph();
            updateWeightLabels((Graph) rg);
            drawGraph((Graph) rg);
            checkConnectivity.setDisable(false);
            connectivityInfo.setText("");
        } catch (IOException e) {
            rg = null;
            System.err.println("Incorrect file format." + e);
            Alert popUp = new Alert(Alert.AlertType.ERROR);
            popUp.setTitle("Error");
            popUp.setHeaderText("Incorrect file format");
            popUp.setContentText("Please provide an input file formatted correctly.");
            popUp.showAndWait();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        fileChooser.setInitialDirectory(new File("C:\\"));
        saveFile.setDisable(true);
        checkConnectivity.setDisable(true);
    }
    @FXML
    void checkConnectivity() {
        // NOT THE BEST SOLUTION - TO BE CHANGED!
        if (gg != null) {
            Connectivity bfs = new Connectivity((Graph) gg);
            if (bfs.isConnected((Graph) gg)) {
                connectivityInfo.setText("The graph is connected.");
                System.out.println("The graph is connected.");
            } else {
                connectivityInfo.setText("The graph is disconnected.");
                System.out.println("The graph is disconnected.");
            }
        } else {
            Connectivity bfs = new Connectivity((Graph) rg);
            if (bfs.isConnected((Graph) rg)) {
                connectivityInfo.setText("The graph is connected.");
                System.out.println("The graph is connected.");
            } else {
                connectivityInfo.setText("The graph is disconnected.");
                System.out.println("The graph is disconnected.");
            }
        }
    }
    @FXML
    public void saveGraph() throws FileNotFoundException, UnsupportedEncodingException {
        File selectedFile = fileChooser.showSaveDialog(null);

        //test - print path to file
        System.out.println(selectedFile.getAbsolutePath());

        String value = selectedFile.getAbsolutePath();
        PrintWriter writer = new PrintWriter(value, "UTF-8");
            gg.writeGraph(writer);
            //test - print graph wrote to the file
            gg.printGraph();
    }

    public void drawGraph(Graph g) {
        canvas = new Canvas(g.getColumns() * (POINT_SIZE + EDGE_LENGTH) + PADDING, g.getRows() * (POINT_SIZE + EDGE_LENGTH) + PADDING);
        scrollPane.setContent(canvas);
        //GraphicsContext gc = canvas.getGraphicsContext2D();
        gc = canvas.getGraphicsContext2D();
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());

        int nodeNumber = 0;

        //draw nodes
        gc.setFill(Color.BLACK);
        for (int i = 0; i < g.getRows(); i++) {
            for (int j = 0; j < g.getColumns(); j++) {
                gc.fillOval((j * (POINT_SIZE + EDGE_LENGTH) + PADDING),
                        (i * (POINT_SIZE + EDGE_LENGTH) + PADDING),
                        POINT_SIZE,
                        POINT_SIZE);
                NodeXY nodexy = new NodeXY(nodeNumber, (j * (POINT_SIZE + EDGE_LENGTH) + PADDING), (i * (POINT_SIZE + EDGE_LENGTH) + PADDING), i, j);
                listXY.add(nodexy);
                nodeNumber++;
            }
        }

        //draw edges horizontal
        for (int i = 0; i < g.getRows(); i++) {
            for (int j = 0; j < (g.getColumns() - 1); j++) {
                int vertexIndex = i * g.getColumns() + j;
                if (g.getVertex(vertexIndex).hasNeighbour(Vertex.RIGHT)) {
                    gc.setFill(getWeightColour(g, g.getVertex(vertexIndex).getWeight(Vertex.RIGHT)));
                    gc.fillRect(((j + 1) * POINT_SIZE + j * EDGE_LENGTH + PADDING),
                            ((2 * i + 1) * 0.5 * (POINT_SIZE - EDGE_THICKNESS) + i * (EDGE_LENGTH + EDGE_THICKNESS) + PADDING),
                            EDGE_LENGTH,
                            EDGE_THICKNESS);
                }
            }
        }

        //draw edges vertical
        for (int i = 0; i < (g.getRows() - 1); i++) {
            for (int j = 0; j < g.getColumns(); j++) {
                int vertexIndex = i * g.getColumns() + j;
                if (g.getVertex(vertexIndex).hasNeighbour(Vertex.LOWER)) {
                    gc.setFill(getWeightColour(g, g.getVertex(vertexIndex).getWeight(Vertex.LOWER)));
                    gc.fillRect(((2 * j + 1) * 0.5 * (POINT_SIZE - EDGE_THICKNESS) + j * (EDGE_LENGTH + EDGE_THICKNESS) + PADDING),
                            ((i + 1) * POINT_SIZE + i * EDGE_LENGTH + PADDING),
                            EDGE_THICKNESS,
                            EDGE_LENGTH);
                }
            }
        }

        // enable vertex selection for path display
        canvas.setOnMouseClicked(event -> {
            // TO BE CHANGED!
            double mousex = event.getX();
            double mousey = event.getY();

            if (clickedNodes.size() == 0) {
                for (NodeXY xy : listXY) {
                    if (mousex > xy.x && mousex < xy.x + POINT_SIZE && mousey > xy.y && mousey < xy.y + POINT_SIZE) {
                        gc.setFill(FUCHSIA);
                        gc.fillOval(xy.x, xy.y, POINT_SIZE, POINT_SIZE);
                        clickedNodes.add(xy);
                        break;
                    }
                }
            } else if (clickedNodes.size() == 1) {
                for (NodeXY xy : listXY) {
                    if (mousex > xy.x && mousex < xy.x + POINT_SIZE && mousey > xy.y && mousey < xy.y + POINT_SIZE) {
                        if(clickedNodes.contains(xy)) {
                            gc.setFill(BLACK);
                            gc.fillOval(xy.x, xy.y, POINT_SIZE, POINT_SIZE);
                            //clickedNodes.remove(xy);
                            clickedNodes.clear();
                            break;
                        } else {
                            gc.setFill(FUCHSIA);
                            gc.fillOval(xy.x, xy.y, POINT_SIZE, POINT_SIZE);
                            clickedNodes.add(xy);
                            break;
                        }
                    }
                }
            } else if (clickedNodes.size() == 2) {
                for (NodeXY xy : listXY) {
                    if (mousex > xy.x && mousex < xy.x + POINT_SIZE && mousey > xy.y && mousey < xy.y + POINT_SIZE) {
                        if (clickedNodes.contains(xy)) {
                            gc.setFill(BLACK);
                            gc.fillOval(xy.x, xy.y, POINT_SIZE, POINT_SIZE);
                            clickedNodes.remove(xy);
                            NodeXY aaa = clickedNodes.get(0);
                            gc.fillOval(aaa.x, aaa.y, POINT_SIZE, POINT_SIZE);
                            clickedNodes.clear();
                            break;
                            //return;
                        } else {
                            gc.setFill(BLACK);
                            gc.fillOval(clickedNodes.get(0).x, clickedNodes.get(0).y, POINT_SIZE, POINT_SIZE);
                            gc.fillOval(clickedNodes.get(1).x, clickedNodes.get(1).y, POINT_SIZE, POINT_SIZE);

                            clickedNodes.clear();
                            clickedNodes.add(xy);
                            gc.setFill(FUCHSIA);
                            gc.fillOval(xy.x, xy.y, POINT_SIZE, POINT_SIZE);

                            break;
                        }

                    }

                }

            }
        });
    }

    public Color getWeightColour(Graph g, double weight) {
        double hue = (BLUE - RED) * (weight - g.getWeightLower()) / (g.getWeightUpper() - g.getWeightLower()) + RED;
        return Color.hsb(hue, 1.0, 1.0);
    }

    public void updateWeightLabels(Graph g) {
        weightLowerLabel.setText(String.valueOf(g.getWeightLower()));
        weightUpperLabel.setText(String.valueOf(g.getWeightUpper()));
    }



    ArrayList<NodeXY> clickedNodes = new ArrayList<>(2); // do zmiany
    /*public void mouseListener(MouseEvent event) {
        double mousex = event.getX();
        double mousey = event.getY();

        if (clickedNodes.size() == 0) {
            for (NodeXY xy : listXY) {
                if (mousex > xy.x && mousex < xy.x + POINT_SIZE && mousey > xy.y && mousey < xy.y + POINT_SIZE) {
                    gc.setFill(FUCHSIA);
                    gc.fillOval(xy.x, xy.y, POINT_SIZE, POINT_SIZE);
                    clickedNodes.add(xy);
                    break;
                }
            }
        } else if (clickedNodes.size() == 1) {
            for (NodeXY xy : listXY) {
                if (mousex > xy.x && mousex < xy.x + POINT_SIZE && mousey > xy.y && mousey < xy.y + POINT_SIZE) {
                    if (clickedNodes.contains(xy)) {
                        gc.setFill(BLACK);
                        gc.fillOval(xy.x, xy.y, POINT_SIZE, POINT_SIZE);
                        //clickedNodes.remove(xy);
                        clickedNodes.clear();
                        break;
                    } else {
                        gc.setFill(FUCHSIA);
                        gc.fillOval(xy.x, xy.y, POINT_SIZE, POINT_SIZE);
                        clickedNodes.add(xy);
                        break;
                    }
                }
            }
        } else if (clickedNodes.size() == 2) {
            for (NodeXY xy : listXY) {
                if (mousex > xy.x && mousex < xy.x + POINT_SIZE && mousey > xy.y && mousey < xy.y + POINT_SIZE) {
                    if (clickedNodes.contains(xy)) {
                        gc.setFill(BLACK);
                        gc.fillOval(xy.x, xy.y, POINT_SIZE, POINT_SIZE);
                        clickedNodes.remove(xy);
                        NodeXY aaa = clickedNodes.get(0);
                        gc.fillOval(aaa.x, aaa.y, POINT_SIZE, POINT_SIZE);
                        clickedNodes.clear();
                        break;
                        //return;
                    } else {
                        gc.setFill(BLACK);
                        gc.fillOval(clickedNodes.get(0).x, clickedNodes.get(0).y, POINT_SIZE, POINT_SIZE);
                        gc.fillOval(clickedNodes.get(1).x, clickedNodes.get(1).y, POINT_SIZE, POINT_SIZE);

                        clickedNodes.clear();
                        clickedNodes.add(xy);
                        gc.setFill(FUCHSIA);
                        gc.fillOval(xy.x, xy.y, POINT_SIZE, POINT_SIZE);

                        break;
                    }

                }

            }

        }


    }*/







}

