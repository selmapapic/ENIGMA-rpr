import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        MainWinController ctrl = new MainWinController();

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/mainWin.fxml"));
        loader.setController(ctrl);
        Parent root = loader.load();
        primaryStage.setTitle("ENIGMA");
        primaryStage.setScene(new Scene(root, 690, 463));
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
