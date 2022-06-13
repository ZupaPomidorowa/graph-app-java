package com.example.appgraph;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Point2D;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.stage.FileChooser;
import javafx.scene.paint.Color;
import javafx.scene.control.*;
import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
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
     * Map of vertex coordinates on the canvas.
     */
    Map<Integer, Point2D> vertexCoordinates = new HashMap<>();

    /**
     * Index of a vertex from which the path begins.
     */
    int startVertex;

    /**
     * Index of a vertex to which the path leads.
     */
    int finishVertex;

    /**
     * List of clicked nodes.
     */
    ArrayList<Point2D> clickedNodes = new ArrayList<>(2); // to be changed?

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

            System.out.println("Rows = " + rows);
            System.out.println("Columns = " + columns);
            System.out.println("w1 = " + w1);
            System.out.println("w2 = " + w2);

            gg.printGraph();

            updateWeightLabels((Graph) gg);

            // splitting the graph
            for(int i = 1; i < segments; i++) {
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
        Graph checked;
        if(gg != null) {
            checked = (Graph)gg;
        } else {
            checked = (Graph)rg;
        }

        Connectivity bfs = new Connectivity(checked);
        if (bfs.isConnected(checked)) {
            connectivityLabel.setText("The graph is connected.");
            System.out.println("The graph is connected.");
        } else {
            connectivityLabel.setText("The graph is disconnected.");
            System.out.println("The graph is disconnected.");
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

        System.out.println(selectedFile.getAbsolutePath());

        String value = selectedFile.getAbsolutePath();
        PrintWriter writer = new PrintWriter(value, "UTF-8");
            gg.writeGraph(writer);
            gg.printGraph();
    }

    /**
     * Draws the graph, highlights a clicked vertex.
     * @param g generated graph or graph read from a file
     */
    public void drawGraph(Graph g) {
        // clear canvas
        canvas = new Canvas(g.getColumns() * (POINT_SIZE + EDGE_LENGTH) + PADDING,
                g.getRows() * (POINT_SIZE + EDGE_LENGTH) + PADDING);
        scrollPane.setContent(canvas);
        gc = canvas.getGraphicsContext2D();
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());

        // draw nodes
        gc.setFill(Color.BLACK);
        for (int i = 0; i < g.getRows(); i++) {
            for (int j = 0; j < g.getColumns(); j++) {
                int vertexIndex = i * g.getColumns() + j;
                gc.fillOval((j * (POINT_SIZE + EDGE_LENGTH) + PADDING),
                        (i * (POINT_SIZE + EDGE_LENGTH) + PADDING),
                        POINT_SIZE,
                        POINT_SIZE);
                vertexCoordinates.put(vertexIndex,
                        new Point2D((j * (POINT_SIZE + EDGE_LENGTH) + (POINT_SIZE / 2) + PADDING),
                                (i * (POINT_SIZE + EDGE_LENGTH) + (POINT_SIZE / 2) + PADDING)));
            }
        }

        // draw horizontal edges
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

        // draw vertical edges
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

        // enable vertex selection for shortest path finding
        canvas.setOnMouseClicked(event -> {
            double mouseX = event.getX();
            double mouseY = event.getY();
            Point2D mousePoint = new Point2D(mouseX, mouseY);

            if (clickedNodes.size() == 0) {
                // 1st click
                for (int i = 0; i < g.getGraphSize(); i++) {
                    Point2D vertexCenterPoint = vertexCoordinates.get(i);
                    startVertex = i;
                    if (pointDistance(mousePoint, vertexCenterPoint) <= (POINT_SIZE / 2)) {
                        gc.setFill(FUCHSIA);
                        gc.fillOval((vertexCenterPoint.getX() - (POINT_SIZE / 2)),
                                (vertexCenterPoint.getY() - (POINT_SIZE / 2)),
                                POINT_SIZE,
                                POINT_SIZE);
                        clickedNodes.add(vertexCenterPoint);
                        break;
                    }
                }
            } else if (clickedNodes.size() == 1) {
                // 2nd click
                for (int i = 0; i < g.getGraphSize(); i++) {
                    Point2D vertexCenterPoint = vertexCoordinates.get(i);
                    finishVertex = i;
                    if (pointDistance(mousePoint, vertexCenterPoint) <= (POINT_SIZE / 2)) {
                        if(clickedNodes.contains(vertexCenterPoint)) {
                            // the same vertex clicked twice
                            gc.setFill(BLACK);
                            gc.fillOval((vertexCenterPoint.getX() - (POINT_SIZE / 2)),
                                    (vertexCenterPoint.getY() - (POINT_SIZE / 2)),
                                    POINT_SIZE,
                                    POINT_SIZE);
                            clickedNodes.clear();
                            drawGraph(g);
                            break;
                        } else {
                            // 2nd vertex different from the 1st one
                            gc.setFill(FUCHSIA);
                            gc.fillOval((vertexCenterPoint.getX() - (POINT_SIZE / 2)),
                                    (vertexCenterPoint.getY() - (POINT_SIZE / 2)),
                                    POINT_SIZE,
                                    POINT_SIZE);
                            clickedNodes.add(vertexCenterPoint);
                            drawPath(g);
                            break;
                        }
                    }
                }
            } else if (clickedNodes.size() == 2) {
                // 3rd click
                for (int i = 0; i < g.getGraphSize(); i++) {
                    Point2D vertexCenterPoint = vertexCoordinates.get(i);
                    startVertex = i;
                    if (pointDistance(mousePoint, vertexCenterPoint) <= (POINT_SIZE / 2)) {
                        if (clickedNodes.contains(vertexCenterPoint)) {
                            // the same vertex clicked twice
                            gc.setFill(BLACK);
                            gc.fillOval((vertexCenterPoint.getX() - (POINT_SIZE / 2)),
                                    (vertexCenterPoint.getY() - (POINT_SIZE / 2)),
                                    POINT_SIZE,
                                    POINT_SIZE);
                            clickedNodes.remove(vertexCenterPoint);
                            Point2D previouslyClicked = clickedNodes.get(0);
                            gc.fillOval((previouslyClicked.getX() - (POINT_SIZE / 2)),
                                    (previouslyClicked.getY() - (POINT_SIZE / 2)),
                                    POINT_SIZE,
                                    POINT_SIZE);
                            clickedNodes.clear();
                            drawGraph(g);
                            break;
                        } else {
                            // 3rd vertex different from the 1st and 2nd one
                            gc.setFill(BLACK);
                            gc.fillOval((clickedNodes.get(0).getX() - (POINT_SIZE / 2)),
                                    (clickedNodes.get(0).getY() - (POINT_SIZE / 2)),
                                    POINT_SIZE,
                                    POINT_SIZE);
                            gc.fillOval((clickedNodes.get(1).getX() - (POINT_SIZE / 2)),
                                    (clickedNodes.get(1).getY() - (POINT_SIZE / 2)),
                                    POINT_SIZE,
                                    POINT_SIZE);
                            clickedNodes.clear();
                            drawGraph(g);

                            clickedNodes.add(vertexCenterPoint);
                            gc.setFill(FUCHSIA);
                            gc.fillOval((vertexCenterPoint.getX() - (POINT_SIZE / 2)),
                                    (vertexCenterPoint.getY() - (POINT_SIZE / 2)),
                                    POINT_SIZE,
                                    POINT_SIZE);
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
    private Color getWeightColour(Graph g, double weight) {
        double hue = (BLUE - RED) * (weight - g.getWeightLower()) / (g.getWeightUpper() - g.getWeightLower()) + RED;
        return Color.hsb(hue, 1.0, 1.0);
    }

    /**
     * Calculates the Euclidean distance between 2 points.
     * @param p1 first point
     * @param p2 second point
     * @return distance between the 2 points
     */
    private double pointDistance(Point2D p1, Point2D p2) {
        double x1 = p1.getX();
        double y1 = p1.getY();
        double x2 = p2.getX();
        double y2 = p2.getY();

        return Math.sqrt((x2 - x1) * (x2 - x1) + (y2 - y1) * (y2 - y1));
    }

    /**
     * Draws the shortest path between 2 vertices on the canvas.
     * @param g graph containing the 2 points
     */
    private void drawPath(Graph g) {
        Path dijkstry = new Path(g);
        ArrayList<Integer> path = dijkstry.findPath(g, startVertex, finishVertex);
        if (path != null) {
            System.out.print("Path between " + startVertex + " and " + finishVertex + ": ");
            for (int i = (path.size() - 1); i > 0; i--) {
                System.out.print(path.get(i) + "-");
            }
            System.out.println(path.get(0) + ".");
        } else {
            System.out.println("Path between " + startVertex + " and " + finishVertex + " doesn't exist.");
            Alert popUp = new Alert(Alert.AlertType.INFORMATION);
            popUp.setTitle("Path finder");
            popUp.setHeaderText("Path doesn't exist");
            popUp.setContentText("The path between vertices " + startVertex + " and " + finishVertex + " doesn't exist.");
            popUp.showAndWait();
            return;
        }

        gc.setStroke(FUCHSIA);
        gc.setLineWidth(EDGE_THICKNESS);
        for(int i = 0; i < (path.size() - 1); i++) {
            double startX = vertexCoordinates.get(path.get(i)).getX();
            double startY = vertexCoordinates.get(path.get(i)).getY();
            double finishX = vertexCoordinates.get(path.get(i + 1)).getX();
            double finishY = vertexCoordinates.get(path.get(i + 1)).getY();
            gc.strokeLine(startX, startY, finishX, finishY);
        }
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

