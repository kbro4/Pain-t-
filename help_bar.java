package com.example.paint;

import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.Button;
import javafx.scene.control.MenuBar;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import javax.imageio.ImageIO;
import java.awt.image.RenderedImage;
import java.io.*;
import java.util.logging.Level;
import java.util.logging.Logger;

import static java.awt.SystemColor.text;


public class help_bar extends display_menu{
    private Menu file;
    private MenuItem help_item;
    private MenuItem about_item;
    private VBox vbox;
    private static ImageView imageview;
    private File file2;

    public help_bar() {
        // Inherits menubar
        menubar = super.menubar;

        // Creates File bar
        file = new Menu("Help");
        help_item = new MenuItem("Help");
        about_item = new MenuItem("About");
        file.getItems().addAll(help_item, about_item);
        menubar.getMenus().add(file);
        vbox = new VBox(menubar);
        imageview = new ImageView();

        //File bar options
        // Opens help
        help_item.setOnAction(o -> {
            open_help();

        });
        // Opens about
        about_item.setOnAction(p -> {
            open_about();
        });
    }

    public VBox get_vbox(){
        return this.vbox;
    }

    public static void open_help(){
        // Help doesn't have to do anything yet
    }

    public static void open_about(){
        Stage primaryStage = new Stage();

        // Creates a Text object
        Text text = new Text();

        // Sets font
        text.setFont(Font.font("arial", FontWeight.NORMAL, FontPosture.REGULAR, 20));

        // Sets the position
        text.setX(10);
        text.setY(100);

        // Text
        text.setText("Paint 1.2.1 was created by Kurt Brown for CS-250." + System.lineSeparator() + "May God and Prof. Rosasco have mercy.");

        // Creates a Group object
        Group root = new Group(text);

        // Creates a scene object
        Scene scene = new Scene(root, 600, 300);

        // Sets title to the Stage
        primaryStage.setTitle("About");

        // Adds scene to the stage
        primaryStage.setScene(scene);

        // Displays the contents of the stage
        primaryStage.show();
    }

}



