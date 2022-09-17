package com.example.paint;

import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import static java.awt.SystemColor.menu;

public class draw extends color_chooser{
    private HBox button_box;
    private Button pen_button;

    public draw(Stage primaryStage, Canvas canvas) {
        // menubar = super.menubar;
        StackPane root = new StackPane();
        final GraphicsContext graphicsContext = canvas.getGraphicsContext2D();

        /*
        // Creates reset button
        final Button resetButton = new Button("Reset");
        resetButton.setOnAction(actionEvent -> {
            graphicsContext.clearRect(1, 1, graphicsContext.getCanvas().getWidth() - 2,
                    graphicsContext.getCanvas().getHeight() - 2);
        });
        resetButton.setTranslateX(10);

        // Set up the pen color chooser
        ChoiceBox<String> colorChooser = new ChoiceBox<>(
                FXCollections.observableArrayList("Black", "Blue", "Red", "Green", "Brown", "Orange"));
        // Select the first option by default
        colorChooser.getSelectionModel().selectFirst();

        colorChooser.getSelectionModel().selectedIndexProperty().addListener((ChangeListener) (ov, old, newval) -> {
            Number idx = (Number) newval;
            Color newColor;
            switch (idx.intValue()) {
                case 0:
                    newColor = Color.BLACK;
                    break;
                case 1:
                    newColor = Color.BLUE;
                    break;
                case 2:
                    newColor = Color.RED;
                    break;
                case 3:
                    newColor = Color.GREEN;
                    break;
                case 4:
                    newColor = Color.BROWN;
                    break;
                case 5:
                    newColor = Color.ORANGE;
                    break;
                default:
                    newColor = Color.BLACK;
                    break;
            }
            graphicsContext.setStroke(newColor);

        });
        colorChooser.setTranslateX(5);

        ChoiceBox<String> sizeChooser = new ChoiceBox<>(FXCollections.observableArrayList("1", "2", "3", "4", "5"));
        // Select the first option by default
        sizeChooser.getSelectionModel().selectFirst();

        sizeChooser.getSelectionModel().selectedIndexProperty().addListener((ChangeListener) (ov, old, newval) -> {
            Number idx = (Number) newval;

            switch (idx.intValue()) {
                case 0:
                    graphicsContext.setLineWidth(1);
                    break;
                case 1:
                    graphicsContext.setLineWidth(2);
                    break;
                case 2:
                    graphicsContext.setLineWidth(3);
                    break;
                case 3:
                    graphicsContext.setLineWidth(4);
                    break;
                case 4:
                    graphicsContext.setLineWidth(5);
                    break;
                default:
                    graphicsContext.setLineWidth(1);
                    break;
            }
        });
        sizeChooser.setTranslateX(5);

         */

        canvas.addEventHandler(MouseEvent.MOUSE_PRESSED, (MouseEvent event) -> {
            graphicsContext.beginPath();
            graphicsContext.moveTo(event.getX(), event.getY());
            graphicsContext.stroke();
        });

        canvas.addEventHandler(MouseEvent.MOUSE_DRAGGED, (MouseEvent event) -> {
            graphicsContext.lineTo(event.getX(), event.getY());
            graphicsContext.stroke();
        });

        canvas.addEventHandler(MouseEvent.MOUSE_RELEASED, (MouseEvent event) -> {
        });

        initDraw(graphicsContext, canvas.getLayoutX(), canvas.getLayoutY());

        //BorderPane container = Main.get_pane();
        Main.get_pane().setCenter(canvas);

        //container.setCenter(canvas);

        root.getChildren().add(canvas);

        Scene scene = new Scene(root, 400, 400);
        primaryStage.setTitle("java2s.com");
        primaryStage.setScene(scene);
        primaryStage.show();

    }

    public void button_box(Canvas canvas){
        menubar = super.menubar;
        final GraphicsContext graphicsContext = canvas.getGraphicsContext2D();
        // Creates reset button
        final Button resetButton = new Button("Reset");
        resetButton.setOnAction(actionEvent -> {
            graphicsContext.clearRect(1, 1, graphicsContext.getCanvas().getWidth() - 2,
                    graphicsContext.getCanvas().getHeight() - 2);
        });
        resetButton.setTranslateX(10);

        // Set up the pen color chooser
        ChoiceBox<String> colorChooser = new ChoiceBox<>(
                FXCollections.observableArrayList("Black", "Blue", "Red", "Green", "Brown", "Orange"));
        // Select the first option by default
        colorChooser.getSelectionModel().selectFirst();

        colorChooser.getSelectionModel().selectedIndexProperty().addListener((ChangeListener) (ov, old, newval) -> {
            Number idx = (Number) newval;
            Color newColor;
            switch (idx.intValue()) {
                case 0:
                    newColor = Color.BLACK;
                    break;
                case 1:
                    newColor = Color.BLUE;
                    break;
                case 2:
                    newColor = Color.RED;
                    break;
                case 3:
                    newColor = Color.GREEN;
                    break;
                case 4:
                    newColor = Color.BROWN;
                    break;
                case 5:
                    newColor = Color.ORANGE;
                    break;
                default:
                    newColor = Color.BLACK;
                    break;
            }
            graphicsContext.setStroke(newColor);

        });
        colorChooser.setTranslateX(5);

        ChoiceBox<String> sizeChooser = new ChoiceBox<>(FXCollections.observableArrayList("1", "2", "3", "4", "5"));
        // Select the first option by default
        sizeChooser.getSelectionModel().selectFirst();

        sizeChooser.getSelectionModel().selectedIndexProperty().addListener((ChangeListener) (ov, old, newval) -> {
            Number idx = (Number) newval;

            switch (idx.intValue()) {
                case 0:
                    graphicsContext.setLineWidth(1);
                    break;
                case 1:
                    graphicsContext.setLineWidth(2);
                    break;
                case 2:
                    graphicsContext.setLineWidth(3);
                    break;
                case 3:
                    graphicsContext.setLineWidth(4);
                    break;
                case 4:
                    graphicsContext.setLineWidth(5);
                    break;
                default:
                    graphicsContext.setLineWidth(1);
                    break;
            }
        });
        sizeChooser.setTranslateX(5);

        button_box = new HBox(menubar);
        button_box.getChildren().addAll(colorChooser, sizeChooser, resetButton);
    }

    public HBox get_HBox(){
        return this.button_box;
    }

    private void initDraw(GraphicsContext gc, double x, double y) {
        double canvasWidth = gc.getCanvas().getWidth();
        double canvasHeight = gc.getCanvas().getHeight();

        gc.fill();
        gc.strokeRect(x, // x of the upper left corner
                y, // y of the upper left corner
                canvasWidth, // width of the rectangle
                canvasHeight); // height of the rectangle

    }
}
