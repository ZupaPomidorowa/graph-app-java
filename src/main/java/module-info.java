module com.example.appgraph {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.appgraph to javafx.fxml;
    exports com.example.appgraph;
}