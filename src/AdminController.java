import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;

import static javafx.scene.control.PopupControl.USE_COMPUTED_SIZE;

public class AdminController {
    public TextField fldPass;
    public AnchorPane anchorChoice, anchorAdminPass;

    @FXML
    public void initialize () {
        anchorAdminPass.toFront();
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
            anchorChoice.toFront();
        }
    }

    public void uploadAction () throws IOException {
        Stage stageUpload = new Stage();
        UploadController controller = new UploadController(null);
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/uploadWin.fxml"));
        loader.setController(controller);
        Parent root = loader.load();
        stageUpload.setTitle("Upload");
        stageUpload.setResizable(true);
        stageUpload.setScene(new Scene(root, USE_COMPUTED_SIZE, USE_COMPUTED_SIZE));
        //stageUpload.setResizable(false);
        stageUpload.show();
    }

    public void overviewAction () throws IOException {
        Stage stageUpload = new Stage();
        OverviewAdminController controller = new OverviewAdminController();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/overviewPapersAdmin.fxml"));
        loader.setController(controller);
        Parent root = loader.load();
        stageUpload.setTitle("Overview");
        stageUpload.setResizable(true);
        stageUpload.setScene(new Scene(root, USE_COMPUTED_SIZE, USE_COMPUTED_SIZE));
        stageUpload.setResizable(false);
        stageUpload.show();
    }
}
