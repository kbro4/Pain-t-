package com.example.paint;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.SnapshotParameters;
import javafx.scene.canvas.Canvas;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.HBox;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;

import static com.example.paint.Main.get_stage;

public class timer extends display_menu {
    Timer timer;
    private Label timerlabel;
    private Integer time_seconds;
    private Timeline timeline;

    public timer(){
        super();
        timer = new Timer();
        time_seconds = 10;
        String timertext = time_seconds.toString();
        //KeyValue time = new KeyValue(time_seconds.intValue(), 0);

        Button autosave = new Button(timertext);
        Button hide_box = new Button("Hide/Show timer");

        HBox timerbox = new HBox();
        timerbox.getChildren().addAll(autosave, hide_box);
        Main.get_pane().setBottom(timerbox);

        hide_box.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                if (autosave.isVisible()){
                    autosave.setVisible(false);
                }
                else {
                    autosave.setVisible(true);
                }
            }
        });

        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                autosave(Main.get_stage(), get_file(), get_imageview(), get_canvas());
            }
        };
        timer.schedule(task,10000, 10000);

    }

    public Timer get_timer(){
        return this.timer;
    }

    public void autosave(Stage stage, File file, ImageView imgView, Canvas canvas) {
        SnapshotParameters snap = new SnapshotParameters();
        //System.out.println(super.get_file());
        file = super.get_file();
        //System.out.println(file);
        if (file != null){
            Image im_to_save = draw_image(imgView);
            File backup = new File(get_file() + ".bak");
            try {
                // Saves image
                WritableImage image = canvas.snapshot(snap, null);
                RenderedImage renderedImage = SwingFXUtils.fromFXImage(im_to_save, null);
                ImageIO.write(renderedImage, "png", backup);
                System.out.println("It worked");
            }
            catch (IOException exception){
                Logger.getLogger(Main.class.getName()).log(Level.SEVERE,null, exception);
            }
        }
    }

    public Image draw_image(ImageView imageview1){
        // Draws image out of imageview
        SnapshotParameters snap = new SnapshotParameters();
        WritableImage image = imageview1.snapshot(snap, null);
        return image;
    }

}
