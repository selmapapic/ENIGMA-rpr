import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

import static javafx.scene.control.PopupControl.USE_COMPUTED_SIZE;

public class LoginController {
    public Button btnCancel, btnLogin;
    public TextField fldEmail;
    public PasswordField fldPass;
    public UsersDAO dao = UsersDAO.getInstance();

    public void cancelAction () {
        Stage stage = (Stage) btnCancel.getScene().getWindow();
        stage.close();
    }

    public void loginAction () throws IOException {
        if(fldEmail.getText().isEmpty() || fldPass.getText().isEmpty()) {
            if (fldEmail.getText().isEmpty()) {
                fldEmail.getStyleClass().removeAll("validField");
                fldEmail.getStyleClass().add("invalidField");
            } else {
                fldEmail.getStyleClass().removeAll("invalidField");
                fldEmail.getStyleClass().add("validField");
            }

            if (fldPass.getText().isEmpty()) {
                fldPass.getStyleClass().removeAll("validField");
                fldPass.getStyleClass().add("invalidField");
            } else {
                fldPass.getStyleClass().removeAll("invalidField");
                fldPass.getStyleClass().add("validField");
            }
        }
        else {
            User user = dao.getUser(fldEmail.getText(), fldPass.getText());
            if(user == null) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Incorrect email and password.");
                alert.setContentText("Try again!");
                alert.showAndWait();
            }
            else {
                Stage stageMainWin = new Stage();
                UserPapersOverviewController controller = new UserPapersOverviewController(user);
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/mainUserForm.fxml"));
                loader.setController(controller);
                Parent root = loader.load();
                stageMainWin.setTitle("Scientific papers");
                stageMainWin.setResizable(true);
                stageMainWin.setScene(new Scene(root, USE_COMPUTED_SIZE, USE_COMPUTED_SIZE));
                stageMainWin.setResizable(false);
                stageMainWin.show();
            }
        }

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
