module dk.stonemountain.demo.demofx {
    requires javafx.controls;
    requires javafx.fxml;

    opens dk.stonemountain.demo.demofx to javafx.graphics;
}