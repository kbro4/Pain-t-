package com.example.paint;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
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

import java.util.Optional;
import java.util.Stack;
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
    private Boolean updated;
    @Override
    public void start(Stage stage) throws IOException {
        // Launches starting interface
        ImageView imgView = user_interface(stage);
        stage2 = stage;
        pane = new BorderPane();

        // Creates border pane, menu bar, help bar, and draw bar
        display_menu menu = new display_menu();
        help_bar help = new help_bar();
        ScrollPane scroll = new ScrollPane();
        scroll.setContent(menu.get_canvas());
        scroll.setPannable(true);
        draw pen1 = new draw(menu.get_canvas());

        // Puts everything together on the border pane
        pane.setTop(pen1.get_HBox());
        pane.setCenter(scroll);

        // Sets scene
        stage.setTitle("Paint");
        Scene scene = new Scene(pane, 595, 355, Color.WHITE);
        stage.setScene(scene);
        stage.show();

        // Sets default pen tool
        pane.setCenter(pen1.draw_tool(menu.get_canvas()));

        // Processes exit request
        stage.setOnCloseRequest(e -> {
            updated = pen1.check_updated();
            e.consume();
            aware_save(pen1);
        });

    }


    public void aware_save(draw pen1){
        // Checks to see if the canvas has been updated
        if (updated) {
            // Creates alert box
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Unsaved changes");
            alert.setContentText("Would you like to save your changes?");

            // Creates save, discard, cancel options
            ButtonType save_opt = new ButtonType("Save");
            ButtonType discard_opt = new ButtonType("Don't Save");
            ButtonType cancel_opt = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
            alert.getButtonTypes().setAll(save_opt, discard_opt, cancel_opt);

            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == save_opt) {
                // User chose save
                // Checks for save versus save as
                if (pen1.get_file() != null){
                    pen1.save(stage2, pen1.get_file(), pen1.get_imageview(), pen1.get_canvas());
                }
                else {
                    pen1.save_as(stage2, pen1.get_file(), pen1.get_imageview(), pen1.get_canvas());
                }
                stage2.close();

            } else if (result.get() == discard_opt) {
                // User chose don't save
                stage2.close();
            } else {
                // User chose cancel or exited the alert
                alert.close();
            }
        }
        else {
            stage2.close();
        }
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
