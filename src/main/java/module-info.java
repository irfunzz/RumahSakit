module com.example.rumahsakit2 {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;

    opens com.irfan.DaisukeClinic to javafx.fxml;
    exports com.irfan.DaisukeClinic;
    exports com.irfan.DaisukeClinic.controller;
    exports com.irfan.DaisukeClinic.model.core;
    opens com.irfan.DaisukeClinic.controller to javafx.fxml;
}