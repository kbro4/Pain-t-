package com.example.paint;

import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.SnapshotParameters;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.robot.Robot;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Pair;

import javax.imageio.ImageIO;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.util.Stack;
import java.awt.*;
import java.io.File;
import java.util.Optional;
import java.util.ArrayList;

import static java.awt.Color.WHITE;
import static java.awt.SystemColor.menu;

public class draw extends help_bar {
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
    private int polygon_sides;
    private ArrayList<Double> xPoints;
    private ArrayList<Double> yPoints;
    private String in_use;
    private Stack<Image> undo;
    private Stack<Image> redo;
    private boolean opened_image;
    private Image bottom_im;
    private int bottom_im_added;
    private boolean doneyet;
    double x1;
    double x2;
    double y1;
    double y2;
    private double[] last_cords;
    private int match_count;
    private Boolean want_rotate;

    public draw(Canvas canvas) {
        menubar = super.menubar;
        updated = false;
        saved = true;
        bottom_im_added = 0;
        last_cords = new double[4];
        want_rotate = false;
        final GraphicsContext graphicsContext = canvas.getGraphicsContext2D();

        // Puts initial canvas at bottom layer of stack
        undo = new Stack<>();
        redo = new Stack<>();
        undo.push(draw_image(canvas));

        // Draws initial canvas
        initDraw(graphicsContext, 0, 0);

        // Creates clear canvas button
        final Button resetButton = new Button("Clear Canvas");
        resetButton.setOnAction(actionEvent -> {
            clear_canvas(canvas);
        });
        resetButton.setTranslateX(5);

        //Creates color picker
        colorPicker = new ColorPicker();
        colorPicker.setValue(Color.WHITE);

        final Text text = new Text();
        text.setFont(Font.font("Verdana", 20));
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
        resize.setOnAction(o -> {
            resize_canvas(canvas);
        });
        resize.setTranslateX(5);

        // Creates shapes toolbar
        ToolBar toolbar = new ToolBar();
        toolbar.setTranslateX(5);

        // Creates free draw button
        final ToggleButton freeB = new ToggleButton();
        freeB.setTooltip(new Tooltip("Draw a free line of selected width"));
        File file = new File("C:\\Users\\kjbro\\Downloads\\free_icon.png");
        Image freeImage = new Image(file.toURI().toString());
        ImageView freeIcon = new ImageView(freeImage);
        freeIcon.setFitWidth(20);
        freeIcon.setFitHeight(20);
        freeB.setGraphic(freeIcon);
        freeB.setOnAction(z -> {
            z.consume();
            selected.setSelected(false);
            freeB.setSelected(true);
            selected = freeB;
            in_use = "freeB";
            draw_tool(canvas);
        });

        // Creates line button
        final ToggleButton lineB = new ToggleButton();
        lineB.setTooltip(new Tooltip("Draw a fixed line of selected width"));
        File file1 = new File("C:\\Users\\kjbro\\Downloads\\line_icon.png");
        Image lineImage = new Image(file1.toURI().toString());
        ImageView lineIcon = new ImageView(lineImage);
        lineIcon.setFitWidth(20);
        lineIcon.setFitHeight(20);
        lineB.setGraphic(lineIcon);
        lineB.setOnAction(a -> {
            a.consume();
            selected.setSelected(false);
            lineB.setSelected(true);
            selected = lineB;
            in_use = "lineB";
            draw_line(canvas);
        });

        // Creates square button
        final ToggleButton squareB = new ToggleButton();
        squareB.setTooltip(new Tooltip("Square"));
        File file2 = new File("C:\\Users\\kjbro\\Downloads\\square_icon.png");
        Image squareImage = new Image(file2.toURI().toString());
        ImageView squareIcon = new ImageView(squareImage);
        squareIcon.setFitWidth(20);
        squareIcon.setFitHeight(20);
        squareB.setGraphic(squareIcon);
        squareB.setOnAction(b -> {
            b.consume();
            selected.setSelected(false);
            squareB.setSelected(true);
            selected = squareB;
            in_use = "squareB";
            draw_square(canvas);
        });

        // Creates rectangle button
        final ToggleButton rectangleB = new ToggleButton();
        rectangleB.setTooltip(new Tooltip("Rectangle"));
        File file3 = new File("C:\\Users\\kjbro\\Downloads\\rectangle_icon.jpg");
        Image rectangleImage = new Image(file3.toURI().toString());
        ImageView rectangleIcon = new ImageView(rectangleImage);
        rectangleIcon.setFitWidth(20);
        rectangleIcon.setFitHeight(20);
        rectangleB.setGraphic(rectangleIcon);
        rectangleB.setOnAction(c -> {
            c.consume();
            selected.setSelected(false);
            rectangleB.setSelected(true);
            selected = rectangleB;
            in_use = "rectangleB";
            draw_rectangle(canvas);
        });

        // Creates circle button
        final ToggleButton circleB = new ToggleButton();
        circleB.setTooltip(new Tooltip("Circle"));
        File file4 = new File("C:\\Users\\kjbro\\Downloads\\circle_icon.png");
        Image circleImage = new Image(file4.toURI().toString());
        ImageView circleIcon = new ImageView(circleImage);
        circleIcon.setFitWidth(20);
        circleIcon.setFitHeight(20);
        circleB.setGraphic(circleIcon);
        circleB.setOnAction(d -> {
            d.consume();
            selected.setSelected(false);
            circleB.setSelected(true);
            selected = circleB;
            in_use = "circleB";
            draw_circle(canvas);
        });

        // Creates ellipse button
        final ToggleButton ellipseB = new ToggleButton();
        ellipseB.setTooltip(new Tooltip("Ellipse"));
        File file5 = new File("C:\\Users\\kjbro\\Downloads\\ellipse_icon.png");
        Image ellipseImage = new Image(file5.toURI().toString());
        ImageView ellipseIcon = new ImageView(ellipseImage);
        ellipseIcon.setFitWidth(20);
        ellipseIcon.setFitHeight(20);
        ellipseB.setGraphic(ellipseIcon);
        ellipseB.setOnAction(e -> {
            e.consume();
            selected.setSelected(false);
            ellipseB.setSelected(true);
            selected = ellipseB;
            in_use = "ellipseB";
            draw_ellipse(canvas);
        });

        // Creates dashed line button
        final ToggleButton dashedB = new ToggleButton();
        dashedB.setTooltip(new Tooltip("Dashed Line"));
        File file6 = new File("C:\\Users\\kjbro\\Downloads\\dashed_icon.png");
        Image dashedImage = new Image(file6.toURI().toString());
        ImageView dashedIcon = new ImageView(dashedImage);
        dashedIcon.setFitWidth(20);
        dashedIcon.setFitHeight(20);
        dashedB.setGraphic(dashedIcon);
        dashedB.setOnAction(f -> {
            f.consume();
            selected.setSelected(false);
            dashedB.setSelected(true);
            selected = dashedB;
            in_use = "dashedB";
            draw_dashed_line(canvas);
        });

        // Color grabber button
        final ToggleButton colorB = new ToggleButton();
        colorB.setTooltip(new Tooltip("Click on a point in the canvas to get the color"));
        File file7 = new File("C:\\Users\\kjbro\\Downloads\\color_icon.png");
        Image colorImage = new Image(file7.toURI().toString());
        ImageView colorIcon = new ImageView(colorImage);
        colorIcon.setFitWidth(20);
        colorIcon.setFitHeight(20);
        colorB.setGraphic(colorIcon);
        colorB.setOnAction(g -> {
            g.consume();
            selected.setSelected(false);
            colorB.setSelected(true);
            selected = colorB;
            in_use = "colorB";
            color_grabber(canvas, text);
        });

        // Rounded rectangle button
        final ToggleButton roundedB = new ToggleButton();
        roundedB.setTooltip(new Tooltip("Rounded Rectangle"));
        File file8 = new File("C:\\Users\\kjbro\\Downloads\\rounded_icon.png");
        Image roundedImage = new Image(file8.toURI().toString());
        ImageView roundedIcon = new ImageView(roundedImage);
        roundedIcon.setFitWidth(20);
        roundedIcon.setFitHeight(20);
        roundedB.setGraphic(roundedIcon);
        roundedB.setOnAction(h -> {
            h.consume();
            selected.setSelected(false);
            roundedB.setSelected(true);
            selected = roundedB;
            in_use = "roundedB";
            draw_rounded_rect(canvas);
        });

        // Eraser button
        final ToggleButton eraserB = new ToggleButton();
        eraserB.setTooltip(new Tooltip("Eraser"));
        File file9 = new File("C:\\Users\\kjbro\\Downloads\\eraser_icon.png");
        Image eraserImage = new Image(file9.toURI().toString());
        ImageView eraserIcon = new ImageView(eraserImage);
        eraserIcon.setFitWidth(20);
        eraserIcon.setFitHeight(20);
        eraserB.setGraphic(eraserIcon);
        eraserB.setOnAction(i -> {
            i.consume();
            selected.setSelected(false);
            eraserB.setSelected(true);
            selected = eraserB;
            in_use = "eraserB";
            eraser(canvas);
        });

        final ToggleButton polygonB = new ToggleButton();
        polygonB.setTooltip(new Tooltip("Draws a polygon with desired number of sides"));
        File file10 = new File("C:\\Users\\kjbro\\Downloads\\polygon_icon.jpg");
        Image polygonImage = new Image(file10.toURI().toString());
        ImageView polygonIcon = new ImageView(polygonImage);
        polygonIcon.setFitWidth(20);
        polygonIcon.setFitHeight(20);
        polygonB.setGraphic(polygonIcon);
        polygonB.setOnAction(j -> {
            j.consume();
            selected.setSelected(false);
            polygonB.setSelected(true);
            selected = polygonB;
            in_use = "polygonB";
            polygon_sides_prompt(canvas);
        });

        final Button undoB = new Button("Undo");
        undoB.setOnAction(k -> {
            undo();
        });
        undoB.setTranslateX(5);

        final Button redoB = new Button("Redo");
        redoB.setOnAction(l -> {
            redo();
        });
        redoB.setTranslateX(5);

        final ToggleButton copyB = new ToggleButton("Copy part");
        copyB.setTooltip(new Tooltip("Copies and pastes a part of an image or drawing"));
        copyB.setOnAction(m -> {
            m.consume();
            selected.setSelected(false);
            copyB.setSelected(true);
            selected = copyB;
            in_use = "copyB";
            image_copy(undo.peek());
        });

        final Button rotecanvB = new Button("Rotate Canvas");
        rotecanvB.setTooltip(new Tooltip("Rotates an image/canvas with alterations"));
        rotecanvB.setOnAction(o -> {
            o.consume();
            rotate_canvas(canvas);
        });
        rotecanvB.setTranslateX(10);

        final Button mirror_image = new Button("Mirror Image");
        mirror_image.setTooltip(new Tooltip("Flips the image horizontally and vertically"));
        mirror_image.setOnAction(p -> {
            p.consume();
            mirror_image();
        });
        mirror_image.setTranslateX(10);

        final Button rote_part_canvasB = new Button("Rotate Part of Canvas");
        rote_part_canvasB.setTooltip(new Tooltip("Rotates a part of the canvas"));
        rote_part_canvasB.setOnAction(q -> {
            q.consume();
            rotate_part_of_canvas();
        });
        rote_part_canvasB.setTranslateX(10);

        // Adds options to toolbar
        toolbar.getItems().addAll(freeB, lineB, squareB, rectangleB, roundedB, circleB, ellipseB, dashedB, eraserB, colorB, polygonB, copyB);

        // Set buttons to unselected
        freeB.setSelected(true);
        selected = freeB;
        in_use = "freeB";

        // Creates menubar box
        button_box = new HBox(menubar);
        button_box.getChildren().addAll(resetButton, undoB, redoB, colorPicker, sizeChooser, resize, toolbar, rotecanvB, rote_part_canvasB, mirror_image);
    }

    public void mirror_image() {
        // Mirrors image
        GraphicsContext gc = get_canvas().getGraphicsContext2D();
        // Mirrors the image
        gc.drawImage(draw_image(get_canvas()), 0.0, 0.0, get_canvas().getWidth(), get_canvas().getHeight(), get_canvas().getWidth(), get_canvas().getHeight(), -get_canvas().getWidth(), -get_canvas().getHeight());
        // Sets new mirrored image as default
        set_image(draw_image(get_canvas()));
    }


    public void rotate_part_of_canvas() {
        want_rotate = true;
        image_copy(draw_image(get_canvas()));
        want_rotate = false;
    }

    public void rotate_canvas(Canvas canvas) {
        // Rotates canvas
        get_canvas().setRotate(get_canvas().getRotate() + 90);
        // Sets canvas
        root = new StackPane();
        scroll = new ScrollPane();
        set_canvas(get_canvas());
        scroll.setContent(get_canvas());
        root.getChildren().add(scroll);
        Main.get_pane().setCenter(root);
    }

    public void set_line_width(int new_width) {
        linewidth = new_width;
    }

    public int get_line_width() {
        return linewidth;
    }

    public void image_copy(Image image){
        Canvas canvas = get_canvas();
        // Gets area user wants to copy
        doneyet = false;
        if (doneyet == false){
            // Gets current canvas, images, scrolls
            root = new StackPane();
            ScrollPane scrolls = new ScrollPane();
            canvas = get_canvas();
            scrolls.setContent(canvas);
            root.getChildren().add(scrolls);
            Main.get_pane().setCenter(root);

            canvas.addEventHandler(MouseEvent.MOUSE_PRESSED, (MouseEvent event) -> {
                // Gets selected starting points
                x1 = event.getX();
                y1 = event.getY();
            });
            canvas.addEventHandler(MouseEvent.MOUSE_DRAGGED, (MouseEvent event) -> {
            });
            canvas.addEventHandler(MouseEvent.MOUSE_RELEASED, (MouseEvent event) -> {
                // Gets selected ending points
                x2 = event.getX();
                y2 = event.getY();
                doneyet = true;
                select_rect(image);
            });
        }
    }

    public void select_rect(Image image1){
        // Creates an imageview of the selected area
        ImageView imageview1 = new ImageView();
        imageview1.setImage(image1);
        imageview1.setPreserveRatio(true);
        imageview1.setSmooth(true);

        Rectangle2D rectangle = new Rectangle2D(x1, y1, x2 - x1, y2 - y1);
        imageview1.setViewport(rectangle);
        if (want_rotate){
            imageview1.setRotate(imageview1.getRotate() + 90);
        }
        paste_rect(imageview1, rectangle);
    }

    public void paste_rect(ImageView imageview1, Rectangle2D rectangle) {
        // Converts imageview to image
        Image image = draw_image(imageview1);
        ScrollPane scrolls = new ScrollPane();
        Canvas canvas = get_canvas();
        doneyet = false;
        if (get_in_use() == "copyB") {
            canvas.setOnMousePressed((MouseEvent event) -> {
                System.out.println("Still rolling");
                double x = event.getX();
                double y = event.getY();
                draw_image(image, canvas, x, y, rectangle);
                scrolls.setContent(get_canvas());
                root.getChildren().addAll(scrolls);
                Main.get_pane().setCenter(root);
                doneyet = true;
            });
        }
    }

    public void set_bottom_layer(){
        // Sets image at bottom of undo stack
        bottom_im = get_image();
        undo.push(bottom_im);
        bottom_im_added = bottom_im_added + 1;
    }

    public Image draw_image(Canvas canvas){
        WritableImage image = new WritableImage((int) canvas.getWidth(), (int) canvas.getHeight());
        canvas.snapshot(null, image);
        return image;
    }

    public Image draw_image(ImageView imageview1){
        // Draws image out of imageview
        SnapshotParameters snap = new SnapshotParameters();
        WritableImage image = imageview1.snapshot(snap, null);
        return image;
    }

    public void draw_image(Image im, Canvas canvas, double x, double y, Rectangle2D rectangle){
        // Draws copied image
        GraphicsContext graphicsContext = canvas.getGraphicsContext2D();
        graphicsContext.drawImage(im, x, y, rectangle.getWidth(), rectangle.getHeight());
        set_canvas(canvas);
    }

    public void draw_image(Image last_im){
        // Redraws last image
        GraphicsContext graphicsContext = get_canvas().getGraphicsContext2D();
        double x = get_canvas().getWidth();
        double y = get_canvas().getHeight();
        undo_clear_canvas(get_canvas());
        setCanvas(get_canvas(), get_image());
        graphicsContext.drawImage(last_im, 0, 0, x, y);
    }

    public void undo(){
        // Removes last image
        Image last_im = undo.pop();
        // Checks to see if image just removed was the bottom layer
        if (!undo.isEmpty()){
            // Puts old image in redo
            redo.push(last_im);
            // Gets back last image
            draw_image(undo.peek());
        }
        else {
            draw_image(last_im);
            undo.push(last_im);
        }
    }

    public void redo(){
        // Makes sure something is in redo
        if (!redo.isEmpty()){
            Image last_im = redo.pop();
            undo.push(last_im);
            draw_image(last_im);
        }
    }

    public String get_in_use(){
        return in_use;
    }

    public void master_handle(Canvas canvas, String tool, Text text){
        GraphicsContext graphicsContext = canvas.getGraphicsContext2D();
        double[] xPoints = new double[polygon_sides];
        double[] yPoints = new double[polygon_sides];

        // Master handler for all draw/shapes tools
        canvas.addEventHandler(MouseEvent.MOUSE_PRESSED, (MouseEvent event) ->{
            switch (tool){
                case ("Pen"):
                    if (get_in_use() == "freeB"){
                        graphicsContext.setLineWidth(linewidth);
                        graphicsContext.beginPath();
                        graphicsContext.moveTo(event.getX(), event.getY());
                        graphicsContext.stroke();
                    }
                case ("Line"):
                    if (get_in_use() == "lineB"){
                        graphicsContext.setLineWidth(linewidth);
                        initialTouch = new Pair<>(event.getX(), event.getY());
                    }
                case ("Square"):
                    if (get_in_use() == "squareB"){
                        graphicsContext.setLineWidth(linewidth);
                        initialTouch = new Pair<>(event.getX(), event.getY());
                    }
                case ("Rectangle"):
                    if (get_in_use() == "rectangleB"){
                        graphicsContext.setLineWidth(linewidth);
                        initialTouch = new Pair<>(event.getX(), event.getY());
                    }
                case ("Circle"):
                    if (get_in_use() == "circleB"){
                        graphicsContext.setLineWidth(linewidth);
                        initialTouch = new Pair<>(event.getX(), event.getY());
                    }
                case ("Ellipse"):
                    if (get_in_use() == "ellipseB"){
                        graphicsContext.setLineWidth(linewidth);
                        initialTouch = new Pair<>(event.getX(), event.getY());
                    }
                case ("Rounded Rectangle"):
                    if (get_in_use() == "roundedB"){
                        graphicsContext.setLineWidth(linewidth);
                        initialTouch = new Pair<>(event.getX(), event.getY());
                    }
                case ("Dashed Line"):
                    if (get_in_use() == "dashedB"){
                        graphicsContext.setLineWidth(linewidth);
                        initialTouch = new Pair<>(event.getX(), event.getY());
                    }
                case ("Eraser"):
                    if (get_in_use() == "eraserB"){
                        graphicsContext.setFill(Color.WHITE);
                        graphicsContext.setLineWidth(linewidth);
                        graphicsContext.beginPath();
                        graphicsContext.moveTo(event.getX(), event.getY());
                        graphicsContext.setStroke(Color.WHITE);
                        graphicsContext.stroke();
                        graphicsContext.fill();
                    }
                case ("Color Grabber"):
                    if (get_in_use() == "colorB"){
                        int xValue = MouseInfo.getPointerInfo().getLocation().x;
                        int yValue = MouseInfo.getPointerInfo().getLocation().y;
                        Robot robot = new Robot();
                        Color color = robot.getPixelColor(xValue, yValue);
                        colorPicker.setValue(color);
                        text.setFill(colorPicker.getValue());
                    }
                case ("Polygon"):
                    if (get_in_use() == "polygonB"){
                        initialTouch = new Pair<>(event.getX(), event.getY());
                    }
            }
        });

        canvas.addEventHandler(MouseEvent.MOUSE_DRAGGED, (MouseEvent event) ->{
            switch (tool) {
                case ("Pen"):
                    if (get_in_use() == "freeB") {
                        graphicsContext.setLineDashes(0);
                        graphicsContext.setLineWidth(linewidth);
                        graphicsContext.lineTo(event.getX(), event.getY());
                        graphicsContext.stroke();
                    }
                case ("Line"):
                case ("Square"):
                case ("Rectangle"):
                case ("Circle"):
                case ("Ellipse"):
                case ("Rounded Rectangle"):
                case ("Dashed Line"):
                case ("Eraser"):
                    if (get_in_use() == "eraserB") {
                        graphicsContext.setFill(Color.WHITE);
                        graphicsContext.setLineDashes(0);
                        graphicsContext.setLineWidth(linewidth);
                        graphicsContext.lineTo(event.getX(), event.getY());
                        graphicsContext.setStroke(Color.WHITE);
                        graphicsContext.stroke();
                    }
                case ("Color Grabber"):
                case ("Polygon"):
            }
        });
        canvas.addEventHandler(MouseEvent.MOUSE_RELEASED, (MouseEvent event) ->{
            switch (tool){
                case ("Pen"):
                    if (get_in_use() == "freeB"){
                        // Checks for loaded image
                        if (bottom_im_added == 0){
                            set_bottom_layer();
                        }
                        updated = true;
                        saved = false;
                        undo.push(draw_image(canvas));
                        redo.clear();
                    }
                case ("Line"):
                    if (get_in_use() == "lineB"){
                        graphicsContext.setLineDashes(0);
                        graphicsContext.strokeLine(initialTouch.getKey(), initialTouch.getValue(), event.getX(), event.getY());
                        updated = true;
                        saved = false;
                        if (!check_last_cords(initialTouch.getKey(), initialTouch.getValue(), event.getX(), event.getY())){
                            undo.push(draw_image(canvas));
                            last_cords[0] = initialTouch.getKey();
                            last_cords[1] = initialTouch.getValue();
                            last_cords[2] = event.getX();
                            last_cords[3] = event.getY();
                            redo.clear();
                        }
                    }
                case ("Square"):
                    if (get_in_use() == "squareB"){
                        graphicsContext.setLineDashes(0);
                        graphicsContext.strokeRect(initialTouch.getKey(), initialTouch.getValue(), event.getX() - initialTouch.getKey(), event.getX() - initialTouch.getKey());
                        updated = true;
                        saved = false;
                        if (!check_last_cords(initialTouch.getKey(), initialTouch.getValue(), event.getX() - initialTouch.getKey(), event.getX() - initialTouch.getKey())){
                            undo.push(draw_image(canvas));
                            last_cords[0] = initialTouch.getKey();
                            last_cords[1] = initialTouch.getValue();
                            last_cords[2] = event.getX() - initialTouch.getKey();
                            last_cords[3] = event.getX() - initialTouch.getKey();
                            redo.clear();
                        }
                    }
                case ("Rectangle"):
                    if (get_in_use() == "rectangleB"){
                        graphicsContext.setLineDashes(0);
                        graphicsContext.strokeRect(initialTouch.getKey(), initialTouch.getValue(), event.getX() - initialTouch.getKey(), event.getY() - initialTouch.getValue());
                        updated = true;
                        saved = false;
                        if (!check_last_cords(initialTouch.getKey(), initialTouch.getValue(), event.getX() - initialTouch.getKey(), event.getY() - initialTouch.getValue())){
                            undo.push(draw_image(canvas));
                            last_cords[0] = initialTouch.getKey();
                            last_cords[1] = initialTouch.getValue();
                            last_cords[2] = event.getX() - initialTouch.getKey();
                            last_cords[3] = event.getY() - initialTouch.getValue();
                            redo.clear();
                        }
                    }
                case ("Circle"):
                    if (get_in_use() == "circleB"){
                        graphicsContext.setLineDashes(0);
                        graphicsContext.strokeOval(initialTouch.getKey(), initialTouch.getValue(), event.getX() - initialTouch.getKey(), event.getX() - initialTouch.getKey());
                        updated = true;
                        saved = false;
                        if (!check_last_cords(initialTouch.getKey(), initialTouch.getValue(), event.getX() - initialTouch.getKey(), event.getX() - initialTouch.getKey())){
                            undo.push(draw_image(canvas));
                            last_cords[0] = initialTouch.getKey();
                            last_cords[1] = initialTouch.getValue();
                            last_cords[2] = event.getX() - initialTouch.getKey();
                            last_cords[3] = event.getX() - initialTouch.getKey();
                            redo.clear();
                        }
                    }
                case ("Ellipse"):
                    if (get_in_use() == "ellipseB"){
                        graphicsContext.setLineDashes(0);
                        graphicsContext.strokeOval(initialTouch.getKey(), initialTouch.getValue(), event.getX() - initialTouch.getKey(), event.getY() - initialTouch.getValue());
                        updated = true;
                        saved = false;
                        if (!check_last_cords(initialTouch.getKey(), initialTouch.getValue(), event.getX() - initialTouch.getKey(), event.getY() - initialTouch.getValue())){
                            undo.push(draw_image(canvas));
                            last_cords[0] = initialTouch.getKey();
                            last_cords[1] = initialTouch.getValue();
                            last_cords[2] = event.getX() - initialTouch.getKey();
                            last_cords[3] = event.getY() - initialTouch.getValue();
                            redo.clear();
                        }
                    }
                case ("Rounded Rectangle"):
                    if (get_in_use() == "roundedB"){
                        graphicsContext.setLineDashes(0);
                        graphicsContext.strokeRoundRect(initialTouch.getKey(), initialTouch.getValue(), event.getX() - initialTouch.getKey(), event.getY() - initialTouch.getValue(), 20, 20);
                        updated = true;
                        saved = false;
                        if (!check_last_cords(initialTouch.getKey(), initialTouch.getValue(), event.getX() - initialTouch.getKey(), event.getY() - initialTouch.getValue())){
                            undo.push(draw_image(canvas));
                            last_cords[0] = initialTouch.getKey();
                            last_cords[1] = initialTouch.getValue();
                            last_cords[2] = event.getX() - initialTouch.getKey();
                            last_cords[3] = event.getY() - initialTouch.getValue();
                            redo.clear();
                        }
                    }
                case ("Dashed Line"):
                    if (get_in_use() == "dashedB"){
                        graphicsContext.setLineDashes(25);
                        graphicsContext.strokeLine(initialTouch.getKey(), initialTouch.getValue(), event.getX(), event.getY());
                        updated = true;
                        saved = false;
                        if (!check_last_cords(initialTouch.getKey(), initialTouch.getValue(), event.getX(), event.getY())){
                            undo.push(draw_image(canvas));
                            last_cords[0] = initialTouch.getKey();
                            last_cords[1] = initialTouch.getValue();
                            last_cords[2] = event.getX();
                            last_cords[3] = event.getY();
                            redo.clear();
                        }
                    }
                case ("Eraser"):
                    if (get_in_use() == "eraserB"){
                        graphicsContext.setFill(Color.WHITE);
                        graphicsContext.setLineWidth(linewidth);
                        graphicsContext.beginPath();
                        graphicsContext.moveTo(event.getX(), event.getY());
                        graphicsContext.setStroke(Color.WHITE);
                        graphicsContext.stroke();
                        graphicsContext.fill();
                    }
                case ("Color Grabber"):
                    if (get_in_use() == "colorB"){
                        int xValue = MouseInfo.getPointerInfo().getLocation().x;
                        int yValue = MouseInfo.getPointerInfo().getLocation().y;
                        Robot robot = new Robot();
                        Color color = robot.getPixelColor(xValue, yValue);
                        colorPicker.setValue(color);
                        text.setFill(colorPicker.getValue());
                    }
                case ("Polygon"):
                    if (get_in_use() == "polygonB"){
                        double width = event.getX() - initialTouch.getKey();
                        for (int i = 0; i < xPoints.length; i++){
                            //xPoints[i] = calc_vertices();
                        }
                    }
            }
        });
    }

    public Boolean check_last_cords(double x1, double y1, double x2, double y2){
        match_count = 0;
        if (x1 == last_cords[0]){
            match_count = match_count + 1;
        }
        if (y1 == last_cords[1]){
            match_count = match_count + 1;
        }
        if (x2 == last_cords[2]){
            match_count = match_count + 1;
        }
        if (y2 == last_cords[3]){
            match_count = match_count + 1;
        }

        if (match_count == 4){
            return true;
        }
        else {
            return false;
        }
    }
    /*

    public double calc_vertices(){
        int increments = 12 / polygon_sides;
        double point1;
        return point1;
    }

     */

    public void polygon(Canvas canvas){
        root = new StackPane();
        canvas = get_canvas();
        ScrollPane scrolls = new ScrollPane();
        final GraphicsContext graphicsContext = canvas.getGraphicsContext2D();

        Text text = new Text("Ignore");
        master_handle(canvas, "Polygon", text);

        scrolls.setContent(canvas);
        root.getChildren().add(scrolls);
        Main.get_pane().setCenter(root);

    }

    public void polygon_sides_prompt(Canvas canvas){
        // Creates prompt window
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));

        // Add prompts for number of sides
        Label xvar= new Label("Set how many sides you want:");
        grid.add(xvar, 0, 1);
        TextField userentry1= new TextField();
        grid.add(userentry1, 1, 1);

        // Adds enter button
        Button btn = new Button("Enter");
        HBox hbBtn = new HBox(10);
        hbBtn.setAlignment(Pos.BOTTOM_CENTER);
        hbBtn.getChildren().add(btn);
        grid.add(hbBtn, 1, 4);

        btn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                // Converts to int
                String temp = userentry1.getText();
                polygon_sides = Integer.parseInt(temp);
                polygon(canvas);
            }
        });

        // Sets grid on screne
        BorderPane root = new BorderPane();
        root.setCenter(grid);
        Stage pop_stage = new Stage();
        pop_stage.setTitle("Polygon request");
        Scene pop_scene = new Scene(root, 400, 400);
        pop_stage.setScene(pop_scene);
        pop_stage.show();

    }

    public void eraser(Canvas canvas){
        // Eraser function
        root = new StackPane();
        canvas = get_canvas();
        ScrollPane scrolls = new ScrollPane();
        final GraphicsContext graphicsContext = canvas.getGraphicsContext2D();

        Text text = new Text("Ignore");

        master_handle(canvas, "Eraser", text);

        scrolls.setContent(canvas);
        root.getChildren().add(scrolls);
        Main.get_pane().setCenter(root);
    }

    public void undo_clear_canvas(Canvas canvas){
        final GraphicsContext graphicsContext = canvas.getGraphicsContext2D();
        graphicsContext.setFill(Color.WHITE);
        graphicsContext.clearRect(0, 0, graphicsContext.getCanvas().getWidth(), graphicsContext.getCanvas().getHeight());
        graphicsContext.strokeRect(0, 0, graphicsContext.getCanvas().getWidth(), graphicsContext.getCanvas().getHeight());
        graphicsContext.fillRect(0, 0, graphicsContext.getCanvas().getWidth(), graphicsContext.getCanvas().getHeight());
    }

    public void clear_canvas(Canvas canvas){
        // Creates alert box
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Clear canvas");
        alert.setContentText("Are you sure you want to clear the canvas?");

        // Creates save, discard, cancel options
        ButtonType yes_opt = new ButtonType("Yes");
        ButtonType cancel_opt = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
        alert.getButtonTypes().setAll(yes_opt, cancel_opt);

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == yes_opt) {
            // Clears canvas
            canvas = get_canvas();
            final GraphicsContext graphicsContext = canvas.getGraphicsContext2D();
            graphicsContext.setFill(Color.WHITE);
            graphicsContext.clearRect(0, 0, graphicsContext.getCanvas().getWidth(), graphicsContext.getCanvas().getHeight());
            graphicsContext.strokeRect(0, 0, graphicsContext.getCanvas().getWidth(), graphicsContext.getCanvas().getHeight());
            graphicsContext.fillRect(0, 0, graphicsContext.getCanvas().getWidth(), graphicsContext.getCanvas().getHeight());

        } else {
            // User chose cancel or exited the alert
            alert.close();
        }

    }

    public void draw_rounded_rect(Canvas canvas){
        // Rounded rectangle function
        root = new StackPane();
        canvas = get_canvas();
        ScrollPane scrolls = new ScrollPane();
        final GraphicsContext graphicsContext = canvas.getGraphicsContext2D();

        Text text = new Text("Ignore");

        master_handle(canvas, "Rounded Rectangle", text);

        scrolls.setContent(canvas);
        root.getChildren().add(scrolls);

        Main.get_pane().setCenter(root);
    }

    public void color_grabber(Canvas canvas, Text text){
        // Color grabber
        root = new StackPane();
        canvas = get_canvas();
        ScrollPane scrolls = new ScrollPane();

        master_handle(canvas, "Color Grabber", text);

        scrolls.setContent(canvas);
        Main.get_pane().setCenter(scrolls);

    }

    public void draw_ellipse(Canvas canvas){
        // Ellipse function
        root = new StackPane();
        canvas = get_canvas();
        scroll = get_scrolls();
        ScrollPane scrolls = new ScrollPane();

        final GraphicsContext graphicsContext = canvas.getGraphicsContext2D();

        Text text = new Text("Ignore");

        master_handle(canvas, "Ellipse", text);

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

        Text text = new Text("Ignore");

        master_handle(canvas, "Circle", text);

        scrolls.setContent(canvas);
        root.getChildren().add(scrolls);

        Main.get_pane().setCenter(root);
    }

    public void draw_square(Canvas canvas){
        System.out.println(get_canvas());
        // Square function
        root = new StackPane();
        canvas = get_canvas();
        ScrollPane scrolls = new ScrollPane();
        final GraphicsContext graphicsContext = canvas.getGraphicsContext2D();

        Text text = new Text("Ignore");

        master_handle(canvas, "Square", text);

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

        Text text = new Text("Ignore");

        master_handle(canvas, "Rectangle", text);

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

        Text text = new Text("Ignore");

        master_handle(canvas, "Dashed Line", text);

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

        Text text = new Text("Ignore");

        master_handle(canvas, "Line", text);

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

        Text text = new Text("Ignore");

        master_handle(canvas, "Pen", text);

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
        // Initial canvas space
        double canvasWidth = gc.getCanvas().getWidth();
        double canvasHeight = gc.getCanvas().getHeight();

        gc.fill();
        gc.strokeRect(x, // x of the upper left corner
                y, // y of the upper left corner
                canvasWidth, // width of the rectangle
                canvasHeight); // height of the rectangle
    }
}
