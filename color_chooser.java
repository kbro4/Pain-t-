package com.example.paint;

import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.ColorPicker;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class color_chooser extends help_bar {
    protected HBox box;
    public color_chooser() {
        // Inherits menu bar
        menubar = super.menubar;

        // Creates HBox
        box = new HBox(menubar);
        box.setPadding(new Insets(5, 5, 5, 5));

        //Creates color picker
        final ColorPicker colorPicker = new ColorPicker();
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

        // Adds color chooser
        box.getChildren().addAll(colorPicker, text);

    }

    public HBox get_HBox(){
        return this.box;
    }
}
