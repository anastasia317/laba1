module com.example.laba1 {
    requires javafx.controls;
    requires javafx.fxml;
    requires kotlin.stdlib;
    requires javafx.media;


    opens com.example.laba1 to javafx.fxml;
    exports com.example.laba1;
}