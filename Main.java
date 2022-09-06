package com.example.paint;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Main extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("hello-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 320, 240);
/*
        //Creates image object
        InputStream stream = new FileInputStream("\"C:\\Users\\kjbro\\Downloads\\elephant.jpg\"");

        //Creates image view
        ImageView imageView = new ImageView();

        //Sets the image view parameters
        imageView.setX(10);
        imageView.setY(10);
        imageView.setFitWidth(575);
        imageView.setPreserveRatio(true);

        //Sets the Scene object
        Group root = new Group(imageView);
        Scene scene2 = new Scene(root, 595, 370);
        stage.setTitle("Displaying Image");
        stage.setScene(scene2);
        stage.show();
*/


        stage.setTitle("Welcome to Paint");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}