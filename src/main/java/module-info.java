module graphicInterface {
    requires javafx.controls;
    requires javafx.fxml;


    opens graphicInterface to javafx.fxml;
    exports graphicInterface;
    exports constant;
    exports enumeration;
    exports exception;
    exports gameObjects;
    exports graph;
}