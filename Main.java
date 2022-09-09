package com.example.paint;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;

import java.util.logging.Level;
import java.util.logging.Logger;
import java.awt.image.RenderedImage;
import javafx.embed.swing.SwingFXUtils;

import java.io.*;
import java.io.IOException;
import java.util.List;
import java.util.Scanner;

import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import javax.imageio.ImageIO;

public class Main extends Application {

    private static BorderPane pane;
    private static Stage stage2;
    @Override
    public void start(Stage stage) throws IOException {
        // Launches starting interface
        ImageView imgView = user_interface(stage);
        stage2 = stage;
        pane = new BorderPane();

        // Creates border pane and menu bar
        display_menu menu = new display_menu();
        pane.setTop(menu.get_vbox());
        pane.setCenter(imgView);
        stage.setTitle("Paint");
        Scene scene = new Scene(pane, 595, 355, Color.BEIGE);
        stage.setScene(scene);
        stage.show();

    }

    public static BorderPane get_pane(){
        return pane;
    }

    public static Stage get_stage(){
        return stage2;
    }

    public static void main(String[] args) {
        launch();
    }

    public static ImageView user_interface(Stage stage){
        // Opening user interface
        ImageView imgView = new ImageView();
        imgView.setFitWidth(20);
        imgView.setFitHeight(20);

        return imgView;
    }

}
