import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

import static javafx.scene.control.PopupControl.USE_COMPUTED_SIZE;

public class AdminController {
    public TextField fldPass;

    public void initialize () {
        fldPass.textProperty().addListener((observable, oldPass, newPass) -> {
            if(!newPass.isEmpty() && newPass.equals("a")) {
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

    public void okAction () throws IOException {
        if(!fldPass.getStyleClass().contains("invalidField") && !fldPass.getText().isEmpty()) {
            Stage stageChoice = new Stage();
            AdminChoiceController controller = new AdminChoiceController();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/adminChoice.fxml"));
            loader.setController(controller);
            Parent root = loader.load();
            stageChoice.setTitle("Choice");
            stageChoice.setResizable(true);
            stageChoice.setScene(new Scene(root, USE_COMPUTED_SIZE, USE_COMPUTED_SIZE));
            stageChoice.setResizable(false);
            stageChoice.show();

            Stage stage = (Stage) fldPass.getScene().getWindow();
            stage.close();
        }
    }
}
