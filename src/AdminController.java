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
        System.out.println(fldPass.getStyleClass().toString());
        if(!fldPass.getStyleClass().contains("invalidField")) {
            Stage stageUpload = new Stage();
            UploadController controller = new UploadController();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/uploadWin.fxml"));
            loader.setController(controller);
            Parent root = loader.load();
            stageUpload.setTitle("Upload");
            stageUpload.setResizable(true);
            stageUpload.setScene(new Scene(root, USE_COMPUTED_SIZE, USE_COMPUTED_SIZE));
            //stageUpload.setResizable(false);
            stageUpload.show();
        }
    }
}
