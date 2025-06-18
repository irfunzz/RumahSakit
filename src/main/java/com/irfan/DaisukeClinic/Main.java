package com.irfan.DaisukeClinic;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import com.irfan.DaisukeClinic.controller.MainController;
import java.io.IOException;

public class Main extends Application {

    private MainController mainController;

    @Override
    public void start(Stage primaryStage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/irfan/DaisukeClinic/MainView.fxml"));
        Parent root = fxmlLoader.load();

        mainController = fxmlLoader.getController();

        primaryStage.setTitle("Daisuke Clinic Management System");
        primaryStage.setScene(new Scene(root, 1000, 700));
        primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("/assets/icon.png")));
        primaryStage.show();


        primaryStage.setOnCloseRequest(event -> {
            System.out.println("Aplikasi ditutup. Menyimpan data...");
            if (mainController != null) {
                mainController.saveAllManagerDataOnExit();
            }
            System.out.println("Data berhasil disimpan. Sampai jumpa!");
        });
    }

    public static void main(String[] args) {
        launch(args);
    }
}