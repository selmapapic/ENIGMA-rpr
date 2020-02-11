import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.Locale;
import java.util.ResourceBundle;

import static javafx.scene.control.PopupControl.USE_COMPUTED_SIZE;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Locale.setDefault(new Locale("BS", "BS"));
        ResourceBundle bundle = ResourceBundle.getBundle("translation");
        MainWinController ctrl = new MainWinController();

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/mainWin.fxml"), bundle);
        loader.setController(ctrl);
        Parent root = loader.load();
        primaryStage.setTitle("ENIGMA");
        primaryStage.setScene(new Scene(root, USE_COMPUTED_SIZE, USE_COMPUTED_SIZE));
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
