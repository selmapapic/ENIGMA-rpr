import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class AdminController {
    public TextField fldPass;

    public void initialize () {
        fldPass.textProperty().addListener((observable, oldPass, newPass) -> {
            if(!newPass.isEmpty() && newPass.equals("administrator")) {
                fldPass.getStyleClass().removeAll("invalidField");
                fldPass.getStyleClass().add("validField");
            }
            else {
                fldPass.getStyleClass().removeAll("validField");
                fldPass.getStyleClass().add("invalidField");
            }
        });
    }

    public void cancelAction () {
        Stage stage = (Stage) fldPass.getScene().getWindow();
        stage.close();
    }

    public void okAction () {

    }
}
