import javafx.scene.control.Button;
import javafx.stage.Stage;

public class LoginController {
    public Button btnCancel, btnLogin;

    public void cancelAction () {
        Stage stage = (Stage) btnCancel.getScene().getWindow();
        stage.close();
    }

    public void loginAction () {

    }

    public void registerAction () {

    }
}
