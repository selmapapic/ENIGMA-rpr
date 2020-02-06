import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;

import static javafx.scene.control.PopupControl.USE_COMPUTED_SIZE;

public class MainWinController {
    public Button btnAdmin, btnSignIn, btnRegister, btnSignIn1, btnRegister1;
    public TextField fldEmail;
    public PasswordField fldPass;
    public UsersDAO dao = UsersDAO.getInstance();
    public AnchorPane anchorSignUp, anchorSignIn;

    public void initialize () {
        anchorSignUp.toBack();
        anchorSignIn.toFront();
    }

    public void adminAction () throws IOException {
        Stage stageAdmin = new Stage();
        AdminController controller = new AdminController();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/adminWin.fxml"));
        loader.setController(controller);
        Parent root = loader.load();
        stageAdmin.setTitle("Administrator");
        stageAdmin.setResizable(true);
        stageAdmin.setScene(new Scene(root, USE_COMPUTED_SIZE, USE_COMPUTED_SIZE));
        stageAdmin.setResizable(false);
        stageAdmin.show();
    }

    public void openRegisterAction () {
        anchorSignUp.toFront();
    }

    public void openSignInAction () {
        anchorSignIn.toFront();
    }

    public void registerAction () throws IOException {
//        Stage stageUser = new Stage();
//        RegisterController controller = new RegisterController();
//        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/userRegister.fxml"));
//        loader.setController(controller);
//        Parent root = loader.load();
//        stageUser.setTitle("Login page");
//        stageUser.setResizable(true);
//        stageUser.setScene(new Scene(root, USE_COMPUTED_SIZE, USE_COMPUTED_SIZE));
//        stageUser.setResizable(false);
//        stageUser.show();
    }

    public void signInAction () throws IOException {
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
}
