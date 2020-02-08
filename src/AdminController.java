import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;

public class AdminController {
    public TextField fldPass;
    public AnchorPane anchorChoice, anchorAdminPass;

    @FXML
    public void initialize () {
        anchorAdminPass.toFront();
        fldPass.textProperty().addListener((observable, oldPass, newPass) -> {
            System.out.println(newPass);
            if(!newPass.isEmpty() && newPass.equals("a")) {
                System.out.println("usao u if");
                fldPass.getStyleClass().removeAll("myborderedregion");
                fldPass.getStyleClass().removeAll("invalidField");
                fldPass.getStyleClass().add("validField");
            }
            else {
                fldPass.getStyleClass().add("myborderedregion");
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
            anchorChoice.toFront();
        }
    }

    public void uploadAction () {

    }

    public void overviewAction () {

    }
}
