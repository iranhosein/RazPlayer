module ir.razplayer {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;
    requires javafx.media;

    opens ir.razplayer to javafx.fxml;
    exports ir.razplayer;
    exports ir.razplayer.view;
    opens ir.razplayer.view to javafx.fxml;
}