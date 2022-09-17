package com.example.paint;

import javafx.geometry.Orientation;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.ScrollBar;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class Scrolls {

    ScrollBar scroll;
    ScrollBar scroll2;
    public Scrolls(Stage primaryStage){
        // Creates vertical scroll bar
        scroll = new ScrollBar();
        scroll.setMin(0);
        scroll.setMax(100);
        scroll.setValue(0);
        scroll.setOrientation(Orientation.HORIZONTAL);
        scroll.setUnitIncrement(10);
        scroll.setBlockIncrement(10);

        // Creates horizontal scroll bar
        scroll2 = new ScrollBar();
        scroll2.setMin(0);
        scroll2.setMax(100);
        scroll2.setValue(0);
        scroll2.setOrientation(Orientation.VERTICAL);
        scroll2.setUnitIncrement(10);
        scroll2.setBlockIncrement(10);

        StackPane root = new StackPane();
        root.getChildren().add(scroll);

        StackPane root2 = new StackPane();
        root2.getChildren().add(scroll2);
    }

    public ScrollBar get_horz_scroll(){
        return scroll;
    }

    public ScrollBar get_vert_scroll(){
        return scroll2;
    }


}
