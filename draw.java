package com.example.paint;

import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.DragEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.robot.Robot;
import javafx.scene.shape.Ellipse;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Pair;

import java.awt.*;
import java.io.File;

import static java.awt.SystemColor.menu;

public class draw extends help_bar{
    private HBox button_box;
    private Button pen_button;
    StackPane root;
    private int linewidth;
    private Stage stage2;
    private Canvas layer = new Canvas();
    private Pair<Double, Double> initialTouch;
    private ColorPicker colorPicker;
    private boolean updated;

    private ToggleButton selected;

    private Ellipse ellipse;

    public draw(Canvas canvas){
        menubar = super.menubar;
        updated = false;
        saved = true;
        final GraphicsContext graphicsContext = canvas.getGraphicsContext2D();

        /*
        // Creates reset button
        final Button resetButton = new Button("Reset");
        resetButton.setOnAction(actionEvent -> {
            graphicsContext.clearRect(1, 1, graphicsContext.getCanvas().getWidth() - 2,
                    graphicsContext.getCanvas().getHeight() - 2);
        });
        resetButton.setTranslateX(10);

         */

        //Creates color picker
        colorPicker = new ColorPicker();
        colorPicker.setValue(Color.WHITE);

        final Text text = new Text();
        text.setFont(Font.font ("Verdana", 20));
        text.setFill(colorPicker.getValue());

        // To change colors
        colorPicker.setOnAction(new EventHandler() {
            public void handle(Event t) {
                text.setFill(colorPicker.getValue());
            }
        });
        colorPicker.setTranslateX(5);

        ChoiceBox<String> sizeChooser = new ChoiceBox<>(FXCollections.observableArrayList("1", "2", "3", "4", "5"));
        // Select the first option by default
        sizeChooser.getSelectionModel().selectFirst();

        // Creates width modifier
        sizeChooser.getSelectionModel().selectedIndexProperty().addListener((ChangeListener) (ov, old, newval) -> {
            Number idx = (Number) newval;

            switch (idx.intValue()) {
                case 0:
                    graphicsContext.setLineWidth(1);
                    linewidth = 1;
                    break;
                case 1:
                    graphicsContext.setLineWidth(2);
                    linewidth = 2;
                    break;
                case 2:
                    graphicsContext.setLineWidth(3);
                    linewidth = 3;
                    break;
                case 3:
                    graphicsContext.setLineWidth(4);
                    linewidth = 4;
                    break;
                case 4:
                    graphicsContext.setLineWidth(5);
                    linewidth = 5;
                    break;
                default:
                    graphicsContext.setLineWidth(1);
                    linewidth = 1;
                    break;
            }
        });
        sizeChooser.setTranslateX(5);

        final Button resize = new Button("Resize");
        resize.setOnAction(o->{
            resize_canvas(canvas);
        });
        resize.setTranslateX(5);

        // Creates shapes toolbar
        ToolBar toolbar = new ToolBar();
        toolbar.setTranslateX(5);

        // Creates free draw button
        final ToggleButton freeB = new ToggleButton();
        File file = new File("C:\\Users\\kjbro\\Downloads\\free_icon.png");
        Image freeImage = new Image(file.toURI().toString());
        ImageView freeIcon = new ImageView(freeImage);
        freeIcon.setFitWidth(20);
        freeIcon.setFitHeight(20);
        freeB.setGraphic(freeIcon);
        freeB.setOnAction(z->{
            selected.setSelected(false);
            freeB.setSelected(true);
            selected = freeB;
            draw_tool(canvas);
        });

        // Creates line button
        final ToggleButton lineB = new ToggleButton();
        File file1 = new File("C:\\Users\\kjbro\\Downloads\\line_icon.png");
        Image lineImage = new Image(file1.toURI().toString());
        ImageView lineIcon = new ImageView(lineImage);
        lineIcon.setFitWidth(20);
        lineIcon.setFitHeight(20);
        lineB.setGraphic(lineIcon);
        lineB.setOnAction(a->{
            selected.setSelected(false);
            lineB.setSelected(true);
            selected = lineB;
            draw_line(canvas);
        });

        // Creates square button
        final ToggleButton squareB = new ToggleButton();
        File file2 = new File("C:\\Users\\kjbro\\Downloads\\square_icon.png");
        Image squareImage = new Image(file2.toURI().toString());
        ImageView squareIcon = new ImageView(squareImage);
        squareIcon.setFitWidth(20);
        squareIcon.setFitHeight(20);
        squareB.setGraphic(squareIcon);
        squareB.setOnAction(b->{
            selected.setSelected(false);
            squareB.setSelected(true);
            selected = squareB;
            draw_square(canvas);
        });

        // Creates rectangle button
        final ToggleButton rectangleB = new ToggleButton();
        File file3 = new File("C:\\Users\\kjbro\\Downloads\\rectangle_icon.jpg");
        Image rectangleImage = new Image(file3.toURI().toString());
        ImageView rectangleIcon = new ImageView(rectangleImage);
        rectangleIcon.setFitWidth(20);
        rectangleIcon.setFitHeight(20);
        rectangleB.setGraphic(rectangleIcon);
        rectangleB.setOnAction(c->{
            selected.setSelected(false);
            rectangleB.setSelected(true);
            selected = rectangleB;
            draw_rectangle(canvas);
        });

        // Creates circle button
        final ToggleButton circleB = new ToggleButton();
        File file4 = new File("C:\\Users\\kjbro\\Downloads\\circle_icon.png");
        Image circleImage = new Image(file4.toURI().toString());
        ImageView circleIcon = new ImageView(circleImage);
        circleIcon.setFitWidth(20);
        circleIcon.setFitHeight(20);
        circleB.setGraphic(circleIcon);
        circleB.setOnAction(d->{
            selected.setSelected(false);
            circleB.setSelected(true);
            selected = circleB;
            draw_circle(canvas);
        });

        // Creates ellipse button
        final ToggleButton ellipseB = new ToggleButton();
        File file5 = new File("C:\\Users\\kjbro\\Downloads\\ellipse_icon.png");
        Image ellipseImage = new Image(file5.toURI().toString());
        ImageView ellipseIcon = new ImageView(ellipseImage);
        ellipseIcon.setFitWidth(20);
        ellipseIcon.setFitHeight(20);
        ellipseB.setGraphic(ellipseIcon);
        ellipseB.setOnAction(e->{
            selected.setSelected(false);
            ellipseB.setSelected(true);
            selected = ellipseB;
            draw_ellipse(canvas);
        });

        // Creates dashed line button
        final ToggleButton dashedB = new ToggleButton();
        File file6 = new File("C:\\Users\\kjbro\\Downloads\\dashed_icon.png");
        Image dashedImage = new Image(file6.toURI().toString());
        ImageView dashedIcon = new ImageView(dashedImage);
        dashedIcon.setFitWidth(20);
        dashedIcon.setFitHeight(20);
        dashedB.setGraphic(dashedIcon);
        dashedB.setOnAction(f->{
            selected.setSelected(false);
            dashedB.setSelected(true);
            selected = dashedB;
            draw_dashed_line(canvas);
        });

        final ToggleButton colorB = new ToggleButton();
        File file7 = new File("C:\\Users\\kjbro\\Downloads\\color_icon.png");
        Image colorImage = new Image(file7.toURI().toString());
        ImageView colorIcon = new ImageView(colorImage);
        colorIcon.setFitWidth(20);
        colorIcon.setFitHeight(20);
        colorB.setGraphic(colorIcon);
        colorB.setOnAction(g->{
            selected.setSelected(false);
            colorB.setSelected(true);
            selected = colorB;
            color_grabber(canvas, text);
        });

        // Adds options to toolbar
        toolbar.getItems().addAll(freeB, lineB, squareB, rectangleB, circleB, ellipseB, dashedB, colorB);

        // Set buttons to unselected
        freeB.setSelected(true);
        lineB.setSelected(false);
        squareB.setSelected(false);
        rectangleB.setSelected(false);
        circleB.setSelected(false);
        ellipseB.setSelected(false);
        dashedB.setSelected(false);
        colorB.setSelected(false);

        selected = freeB;

        // Creates menubar box
        button_box = new HBox(menubar);
        button_box.getChildren().addAll(colorPicker, sizeChooser, resize, toolbar);
    }

    public void color_grabber(Canvas canvas, Text text){
        // Color grabber
        root = new StackPane();
        canvas = get_canvas();
        ScrollPane scrolls = new ScrollPane();

        canvas.addEventHandler(MouseEvent.MOUSE_PRESSED, (MouseEvent event) -> {
            int xValue = MouseInfo.getPointerInfo().getLocation().x;
            int yValue = MouseInfo.getPointerInfo().getLocation().y;
            Robot robot = new Robot();
            Color color = robot.getPixelColor(xValue, yValue);
            colorPicker.setValue(color);
            text.setFill(colorPicker.getValue());
        });

        canvas.addEventHandler(MouseEvent.MOUSE_DRAGGED, (MouseEvent event) -> {
        });

        canvas.addEventHandler(MouseEvent.MOUSE_RELEASED, (MouseEvent event) -> {
        });

        scrolls.setContent(canvas);
        Main.get_pane().setCenter(scrolls);

    }

    public void draw_ellipse(Canvas canvas){
        // Ellipse function
        root = new StackPane();
        canvas = get_canvas();
        scroll = get_scrolls();
        ScrollPane scrolls = new ScrollPane();

        Canvas top_canvas = new Canvas(canvas.getHeight(), canvas.getWidth());
        final GraphicsContext graphicsContext = canvas.getGraphicsContext2D();

        canvas.addEventHandler(MouseEvent.MOUSE_PRESSED, (MouseEvent event) -> {
            graphicsContext.setLineDashes(0);
            graphicsContext.setLineWidth(linewidth);
            ellipse = new Ellipse();
            ellipse.setCenterX(event.getX());
            ellipse.setCenterY(event.getY());
            ellipse.setStroke(Color.BLACK);
            root.getChildren().add(ellipse);
        });

        canvas.addEventHandler(MouseEvent.MOUSE_DRAGGED, (MouseEvent event) -> {
            ellipse.setRadiusX(event.getX() - ellipse.getCenterX());
            ellipse.setRadiusY(event.getY() - ellipse.getCenterY());
        });

        canvas.addEventHandler(MouseEvent.MOUSE_RELEASED, (MouseEvent event) -> {
            updated = true;
            saved = false;
        });

        scrolls.setContent(canvas);
        root.getChildren().add(scrolls);
        Main.get_pane().setCenter(root);
    }

    public void draw_circle(Canvas canvas){
        // Circle function
        root = new StackPane();
        canvas = get_canvas();
        ScrollPane scrolls = new ScrollPane();
        scroll = get_scrolls();
        final GraphicsContext graphicsContext = canvas.getGraphicsContext2D();

        canvas.addEventHandler(MouseEvent.MOUSE_PRESSED, (MouseEvent event) -> {
            graphicsContext.setLineWidth(linewidth);
            initialTouch = new Pair<>(event.getX(), event.getY());
        });

        canvas.addEventHandler(MouseEvent.MOUSE_DRAGGED, (MouseEvent event) -> {
        });

        canvas.addEventHandler(MouseEvent.MOUSE_RELEASED, (MouseEvent event) -> {
            graphicsContext.setLineDashes(0);
            graphicsContext.strokeOval(initialTouch.getKey(), initialTouch.getValue(), event.getX() - initialTouch.getKey(), event.getX() - initialTouch.getKey());
            updated = true;
            saved = false;
        });

        scrolls.setContent(canvas);
        root.getChildren().add(scrolls);

        Main.get_pane().setCenter(root);
    }

    public void draw_square(Canvas canvas){
        // Square function
        root = new StackPane();
        canvas = get_canvas();
        ScrollPane scrolls = new ScrollPane();
        final GraphicsContext graphicsContext = canvas.getGraphicsContext2D();

        canvas.addEventHandler(MouseEvent.MOUSE_PRESSED, (MouseEvent event) -> {
            graphicsContext.setLineWidth(linewidth);
            initialTouch = new Pair<>(event.getX(), event.getY());
        });

        canvas.addEventHandler(MouseEvent.MOUSE_DRAGGED, (MouseEvent event) -> {
        });

        canvas.addEventHandler(MouseEvent.MOUSE_RELEASED, (MouseEvent event) -> {
            graphicsContext.setLineDashes(0);
            graphicsContext.strokeRect(initialTouch.getKey(), initialTouch.getValue(), event.getX() - initialTouch.getKey(), event.getX() - initialTouch.getKey());
            updated = true;
            saved = false;
        });

        scrolls.setContent(canvas);
        root.getChildren().add(scrolls);
        Main.get_pane().setCenter(root);
    }

    public void draw_rectangle(Canvas canvas){
        // Rectangle function
        root = new StackPane();
        canvas = get_canvas();
        ScrollPane scrolls = new ScrollPane();
        final GraphicsContext graphicsContext = canvas.getGraphicsContext2D();

        canvas.addEventHandler(MouseEvent.MOUSE_PRESSED, (MouseEvent event) -> {
            graphicsContext.setLineWidth(linewidth);
            initialTouch = new Pair<>(event.getX(), event.getY());
        });

        canvas.addEventHandler(MouseEvent.MOUSE_DRAGGED, (MouseEvent event) -> {
        });

        canvas.addEventHandler(MouseEvent.MOUSE_RELEASED, (MouseEvent event) -> {
            graphicsContext.setLineDashes(0);
            graphicsContext.strokeRect(initialTouch.getKey(), initialTouch.getValue(), event.getX() - initialTouch.getKey(), event.getY() - initialTouch.getValue());
            updated = true;
            saved = false;
        });

        scrolls.setContent(canvas);
        root.getChildren().add(scrolls);

        Main.get_pane().setCenter(root);
    }

    public void draw_dashed_line(Canvas canvas){
        // Dashed line function
        root = new StackPane();
        canvas = get_canvas();
        ScrollPane scrolls = new ScrollPane();
        final GraphicsContext graphicsContext = canvas.getGraphicsContext2D();

        canvas.addEventHandler(MouseEvent.MOUSE_PRESSED, (MouseEvent event) -> {
            graphicsContext.setLineWidth(linewidth);
            initialTouch = new Pair<>(event.getX(), event.getY());
        });

        canvas.addEventHandler(MouseEvent.MOUSE_DRAGGED, (MouseEvent event) -> {
        });

        canvas.addEventHandler(MouseEvent.MOUSE_RELEASED, (MouseEvent event) -> {
            graphicsContext.setLineDashes(25);
            graphicsContext.strokeLine(initialTouch.getKey(), initialTouch.getValue(), event.getX(), event.getY());
            updated = true;
            saved = false;
        });

        scrolls.setContent(canvas);
        root.getChildren().add(scrolls);

        Main.get_pane().setCenter(root);
    }

    public void draw_line(Canvas canvas){
        // Line function
        root = new StackPane();
        canvas = get_canvas();
        ScrollPane scrolls = new ScrollPane();
        final GraphicsContext graphicsContext = canvas.getGraphicsContext2D();

        canvas.addEventHandler(MouseEvent.MOUSE_PRESSED, (MouseEvent event) -> {
            graphicsContext.setLineWidth(linewidth);
            initialTouch = new Pair<>(event.getX(), event.getY());
        });

        canvas.addEventHandler(MouseEvent.MOUSE_DRAGGED, (MouseEvent event) -> {
        });

        canvas.addEventHandler(MouseEvent.MOUSE_RELEASED, (MouseEvent event) -> {
            graphicsContext.setLineDashes(0);
            graphicsContext.strokeLine(initialTouch.getKey(), initialTouch.getValue(), event.getX(), event.getY());
            updated = true;
            saved = false;
        });

        scrolls.setContent(canvas);
        root.getChildren().add(scrolls);
        Main.get_pane().setCenter(root);

    }

    public void resize_canvas(Canvas canvas){
        // Creates prompt window
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));

        // Add prompts for new width
        Label xvar= new Label("Set new width:");
        grid.add(xvar, 0, 1);
        TextField userentry1= new TextField();
        grid.add(userentry1, 1, 1);

        // Adds prompt for new height
        Label yvar = new Label("Set new height:");
        grid.add(yvar, 0, 2);
        TextField userentry2= new TextField();
        grid.add(userentry2, 1, 2);

        // Adds apply button
        Button btn = new Button("Apply");
        HBox hbBtn = new HBox(10);
        hbBtn.setAlignment(Pos.BOTTOM_CENTER);
        hbBtn.getChildren().add(btn);
        grid.add(hbBtn, 1, 4);

        btn.setOnAction(new EventHandler<ActionEvent>() {
                            @Override
                            public void handle(ActionEvent e) {
                                // Changes width
                                String vars1 = userentry1.getText();
                                double new_width = Double.parseDouble(vars1);
                                canvas.setWidth(new_width);
                                alter_canvas_width(new_width);

                                // Changes height
                                String vars2 = userentry2.getText();
                                double new_height = Double.parseDouble(vars2);
                                canvas.setHeight(new_height);
                                alter_canvas_height(new_height);

                                Main.get_pane().setCenter(draw_tool(canvas));

                            }
                        });

        BorderPane root = new BorderPane();
        root.setCenter(grid);
        Stage pop_stage = new Stage();
        pop_stage.setTitle("Resize");
        Scene pop_scene = new Scene(root, 400, 400);
        pop_stage.setScene(pop_scene);
        pop_stage.show();
    }

    public Boolean check_updated(){
        // Checks to see if the program's updates have been saved
        if (check_saved()){
            updated = false;
        }
        else {
            updated = true;
        }

        // Checks to see if the program has been updated
        if (updated){
            return true;
        }
        else {
            return false;
        }
    }

    public Boolean check_saved(){
        return saved;
    }

    public Node draw_tool(Canvas canvas) {
        // Draw function
        root = new StackPane();
        canvas = get_canvas();
        ScrollPane scrolls = new ScrollPane();
        final GraphicsContext graphicsContext = canvas.getGraphicsContext2D();

        canvas.addEventHandler(MouseEvent.MOUSE_PRESSED, (MouseEvent event)  -> {
            graphicsContext.setLineWidth(linewidth);
            graphicsContext.beginPath();
            graphicsContext.moveTo(event.getX(), event.getY());
            graphicsContext.stroke();
        });

        canvas.addEventHandler(MouseEvent.MOUSE_DRAGGED, (MouseEvent event) -> {
            graphicsContext.setLineDashes(0);
            graphicsContext.setLineWidth(linewidth);
            graphicsContext.lineTo(event.getX(), event.getY());
            graphicsContext.stroke();
        });

        canvas.addEventHandler(MouseEvent.MOUSE_RELEASED, (MouseEvent event) -> {
            updated = true;
            saved = false;
        });

        initDraw(graphicsContext, canvas.getLayoutX(), canvas.getLayoutY());

        scrolls.setContent(canvas);
        root.getChildren().add(scrolls);
        Main.get_pane().setCenter(root);

        return root;

    }

    public Node get_draw(){
        return this.root;
    }

    public HBox get_HBox(){
        return this.button_box;
    }

    private void initDraw(GraphicsContext gc, double x, double y) {
        // Fills in line
        double canvasWidth = gc.getCanvas().getWidth();
        double canvasHeight = gc.getCanvas().getHeight();

        gc.fill();
        gc.strokeRect(x, // x of the upper left corner
                y, // y of the upper left corner
                canvasWidth, // width of the rectangle
                canvasHeight); // height of the rectangle
    }
}
