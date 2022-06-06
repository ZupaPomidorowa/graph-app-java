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

/**
 * Class controlling the GUI.
 */
public class Controllers implements Initializable {
    /**
     * ScrollPane in which the canvas with the graph is located.
     */
    @FXML
    private ScrollPane scrollPane = new ScrollPane();
    /**
     * Text field for the lower end of the weight range.
     */
    @FXML
    private TextField weightLowerField;
    /**
     * Text field for the upper end of the weight range.
     */
    @FXML
    private TextField weightUpperField;
    /**
     * Text field for the number of rows.
     */
    @FXML
    private TextField rowsField;
    /**
     * Text field for the number of columns.
     */
    @FXML
    private TextField columnsField;
    /**
     * Text field for the number of segments.
     */
    @FXML
    private TextField segmentsField;
    /**
     * Button for saving the graph file.
     */
    @FXML
    private Button saveFile;
    /**
     * Button for checking the connectivity of a graph.
     */
    @FXML
    private Button checkConnectivity;
    /**
     * Label describing if graph is connected or disconnected.
     */
    @FXML
    private Label connectivityLabel;
    /**
     * Label for the lower end of the weight range.
     */
    @FXML
    private Label weightLowerLabel;
    /**
     * Label for the upper end of the weight range.
     */
    @FXML
    private Label weightUpperLabel;
    /**
     * Graph generated at runtime.
     */
    GeneratedGraph gg;
    /**
     * Graph read from a file.
     */
    ReadGraph rg;
    /**
     * File choosing window.
     */
    FileChooser fileChooser = new FileChooser();
    /**
     * Canvas in the ScrollPane on which the graph is drawn.
     */
    Canvas canvas;
    /**
     * GraphicContext for drawing on the canvas.
     */
    GraphicsContext gc;
    /**
     * List of vertex coordinates on the canvas.
     */
    ArrayList<NodeXY> listXY = new ArrayList<>();
    /**
     * List of clicked nodes.
     */
    ArrayList<NodeXY> clickedNodes = new ArrayList<>(2); // do zmiany
    /**
     * Dark blue hue (lower end of the weight range).
     */
    private static final double BLUE = Color.DARKBLUE.getHue();
    /**
     * Red hue (upper end of the weight range).
     */
    private static final double RED = Color.RED.getHue();
    /**
     * Vertex graphical representation diameter.
     */
    private static final int POINT_SIZE = 20;
    /**
     * Edge graphical representation length.
     */
    private static final int EDGE_LENGTH = 20;
    /**
     * Edge graphical representation width.
     */
    private static final int EDGE_THICKNESS = 5;
    /**
     * Margin on the canvas.
     */
    private static final int PADDING = 20;

    /**
     * Generates a graph from user-provided parameters.
     */
    @FXML
    void generate() {
        try {
            int rows = parseInt(rowsField.getText());
            int columns = parseInt(columnsField.getText());
            double w1 = parseDouble(weightLowerField.getText());
            double w2 = parseDouble(weightUpperField.getText());
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
            connectivityLabel.setText("");
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

    /**
     * Reads a graph from a user-imported file.
     */
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
            connectivityLabel.setText("");
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

    /**
     * Checks if the graph is connected.
     */
    @FXML
    void checkConnectivity() {
        // NOT THE BEST SOLUTION - TO BE CHANGED!
        if (gg != null) {
            Connectivity bfs = new Connectivity((Graph) gg);
            if (bfs.isConnected((Graph) gg)) {
                connectivityLabel.setText("The graph is connected.");
                System.out.println("The graph is connected.");
            } else {
                connectivityLabel.setText("The graph is disconnected.");
                System.out.println("The graph is disconnected.");
            }
        } else {
            Connectivity bfs = new Connectivity((Graph) rg);
            if (bfs.isConnected((Graph) rg)) {
                connectivityLabel.setText("The graph is connected.");
                System.out.println("The graph is connected.");
            } else {
                connectivityLabel.setText("The graph is disconnected.");
                System.out.println("The graph is disconnected.");
            }
        }
    }

    /**
     * Saves the generated graph into a file.
     * @throws FileNotFoundException in case of the file being unable to be found
     * @throws UnsupportedEncodingException in case of an unsupported encoding
     */
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

    /**
     * Draws the graph, highlights a clicked vertex.
     * @param g generated graph or graph read from a file
     */
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
            double mouseX = event.getX();
            double mouseY = event.getY();

            if (clickedNodes.size() == 0) {
                for (NodeXY xy : listXY) {
                    if (mouseX > xy.x && mouseX < xy.x + POINT_SIZE && mouseY > xy.y && mouseY < xy.y + POINT_SIZE) {
                        gc.setFill(FUCHSIA);
                        gc.fillOval(xy.x, xy.y, POINT_SIZE, POINT_SIZE);
                        clickedNodes.add(xy);
                        break;
                    }
                }
            } else if (clickedNodes.size() == 1) {
                for (NodeXY xy : listXY) {
                    if (mouseX > xy.x && mouseX < xy.x + POINT_SIZE && mouseY > xy.y && mouseY < xy.y + POINT_SIZE) {
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
                    if (mouseX > xy.x && mouseX < xy.x + POINT_SIZE && mouseY > xy.y && mouseY < xy.y + POINT_SIZE) {
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

    /**
     * Gets edge colour depending on the weight.
     * @param g graph
     * @param weight weight value
     * @return colour value
     */
    public Color getWeightColour(Graph g, double weight) {
        double hue = (BLUE - RED) * (weight - g.getWeightLower()) / (g.getWeightUpper() - g.getWeightLower()) + RED;
        return Color.hsb(hue, 1.0, 1.0);
    }

    /**
     * Updates graph weight range labels (under the colour scale).
     * @param g graph
     */
    public void updateWeightLabels(Graph g) {
        weightLowerLabel.setText(String.valueOf(g.getWeightLower()));
        weightUpperLabel.setText(String.valueOf(g.getWeightUpper()));
    }
}

