package com.example.paint;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
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

import java.io.*;
import java.io.IOException;
import java.util.List;
import java.util.Scanner;

import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Main extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        ImageView imgView = user_interface(stage);
        menu_bar(stage, imgView);

    }

    public static void main(String[] args) {
        launch();
    }

    public static ImageView user_interface(Stage stage){
        //User interface
        ImageView imgView = new ImageView();
        imgView.setFitWidth(20);
        imgView.setFitHeight(20);

        return imgView;
    }

    public static void menu_bar(Stage stage, ImageView imgView) {
        // Creates File menu
        Menu file = new Menu("File");
        MenuItem op_item = new MenuItem("Open", imgView);
        MenuItem s_item = new MenuItem("Save", imgView);
        MenuItem sa_item = new MenuItem("Save as", imgView);
        file.getItems().addAll(op_item, s_item, sa_item);
        File file2 = null;

        op_item.setOnAction(
                new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(final ActionEvent e) {
                        open_image(stage);
                    }
                });
        s_item.setOnAction(
                new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(final ActionEvent e) {
                        save(stage, file2, imgView);
                    }
                });

        sa_item.setOnAction(
                new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(final ActionEvent e) {
                        save_as(stage);
                    }
                });


        //Creates a menu bar and adds menu to it.
        MenuBar menuBar = new MenuBar(file);
        Group root = new Group(menuBar);
        Scene scene = new Scene(root, 595, 355, Color.BEIGE);
        stage.setTitle("Paint");
        stage.setScene(scene);
        stage.show();
    }

    public static void display_menu(Stage stage, ImageView imgView){
        // Creates File menu
        Menu file = new Menu("File");
        MenuItem op_item = new MenuItem("Open", imgView);
        MenuItem s_item = new MenuItem("Save", imgView);
        MenuItem sa_item = new MenuItem("Save as", imgView);
        file.getItems().addAll(op_item, s_item, sa_item);
        File file2 = null;

        op_item.setOnAction(
                new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(final ActionEvent e) {
                        open_image(stage);
                    }
                });
        s_item.setOnAction(
                new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(final ActionEvent e) {
                        save(stage, file2, imgView);
                    }
                });

        sa_item.setOnAction(
                new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(final ActionEvent e) {
                        save_as(stage);
                    }
                });


        //Creates a menu bar and adds menu to it.
        MenuBar menuBar = new MenuBar(file);
        Group root = new Group(menuBar);
        stage.setTitle("Paint");
        Scene scene = new Scene(root, 595, 355, Color.BEIGE);
        //stage.setScene(scene);
        stage.show();
    }
    public static void open_image(Stage stage){
        // Creates file chooser
        stage.setTitle("Open");
        final FileChooser fileChooser = new FileChooser();
        final Button openButton = new Button("Open a Picture...");

        // Accesses file chooser
        openButton.setOnAction(
                new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(final ActionEvent e) {
                        File file = fileChooser.showOpenDialog(stage);
                        if (file != null) {
                            try {
                                openFile(file, stage);
                            } catch (FileNotFoundException ex) {
                                throw new RuntimeException(ex);
                            }
                        }
                    }
                });

        // Creates open picture prompt
        final GridPane inputGridPane = new GridPane();
        GridPane.setConstraints(openButton, 0, 0);
        inputGridPane.setHgap(6);
        inputGridPane.setVgap(6);
        inputGridPane.getChildren().addAll(openButton);
        final Pane rootGroup = new VBox(12);
        rootGroup.getChildren().addAll(inputGridPane);
        rootGroup.setPadding(new Insets(12, 12, 12, 12));

        stage.setScene(new Scene(rootGroup));
        stage.show();

    }

    private static void openFile(File file, Stage stage) throws FileNotFoundException {
        //Creates image object
        InputStream stream = new FileInputStream(file);
        Image image = new Image(stream);

        //Creates image view
        ImageView imageView = new ImageView();

        //Sets image to the image view
        imageView.setImage(image);

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

        display_menu(stage, imageView);
    }

    public static void save(Stage stage, File file, ImageView imgView){
        WritableImage image = new WritableImage((int)imgView.getFitWidth(), (int)imgView.getFitHeight());
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save");
        fileChooser.getExtensionFilters().addAll(new ExtensionFilter("All Files", "*.*"));

        //Open directory from existing directory
        System.out.println(file.getPath());

        if(file.getPath() != null){
            File existDirectory = file.getParentFile();
            fileChooser.setInitialDirectory(existDirectory);
        }
        else {
            save_as(stage);
        }
    }

    public static void save_as(Stage stage){
        //Creates a File chooser
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save as");
        fileChooser.getExtensionFilters().addAll(new ExtensionFilter("All Files", "*.*"));

        // Opens a dialog box
        fileChooser.showSaveDialog(stage);
    }

}
