import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;

import static javafx.scene.control.PopupControl.USE_COMPUTED_SIZE;

public class LoginController {
    public Button btnCancel, btnLogin;

    public void cancelAction () {
        Stage stage = (Stage) btnCancel.getScene().getWindow();
        stage.close();
    }

    public void loginAction () {

    }

    public void registerAction () throws IOException {
        Stage stageRegister = new Stage();
        RegisterController controller = new RegisterController();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/userRegister.fxml"));
        loader.setController(controller);
        Parent root = loader.load();
        stageRegister.setTitle("Register page");
        stageRegister.setResizable(true);
        stageRegister.setScene(new Scene(root, USE_COMPUTED_SIZE, USE_COMPUTED_SIZE));
        stageRegister.setResizable(false);
        stageRegister.show();
    }
}
