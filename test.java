import com.example.paint.Main;
import com.example.paint.display_menu;
import com.example.paint.draw;
import com.example.paint.help_bar;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;

public class test {

    @Test
    public void test_set_pane(){
        BorderPane test_pane = new BorderPane();
        Main.set_pane(test_pane);
        Assertions.assertEquals(test_pane, Main.get_pane());
    }

    @Test
    public void test_user_interface(){
        ImageView test_imageview = new ImageView();
        test_imageview.setFitWidth(20);
        test_imageview.setFitHeight(20);

        ImageView method_interface = Main.user_interface(Main.get_stage());
        Assertions.assertEquals(test_imageview.getFitWidth(), method_interface.getFitWidth());
    }

    @Test
    public void test_set_stage(){
        Boolean test_updated = false;
        Main instance = new Main();
        instance.set_updated(false);
        Assertions.assertEquals(test_updated, instance.get_updated());
    }
}
