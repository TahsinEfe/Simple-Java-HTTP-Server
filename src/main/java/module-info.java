module com.serversocket.serversocket {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.bootstrapfx.core;
    requires com.google.gson;

    opens com.serversocket.serversocket to javafx.fxml;
    exports com.serversocket.serversocket;
}