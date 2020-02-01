import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;

import static javafx.scene.control.PopupControl.USE_COMPUTED_SIZE;

public class MainWinController {
    public Button btnAdmin, btnUser;

    public void adminAction () throws IOException {
        Stage stageAdmin = new Stage();
        AdminController controller = new AdminController();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/adminWin.fxml"));
        loader.setController(controller);
        Parent root = loader.load();
        stageAdmin.setTitle("Pretraga slike");
        stageAdmin.setResizable(true);
        stageAdmin.setScene(new Scene(root, USE_COMPUTED_SIZE, USE_COMPUTED_SIZE));
        stageAdmin.setResizable(false);
        stageAdmin.show();
    }

    public void userAction () {

    }

}
