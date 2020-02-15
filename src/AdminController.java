import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Locale;
import java.util.ResourceBundle;

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
        ResourceBundle bundle = ResourceBundle.getBundle("translation");
        Stage stageUpload = new Stage();
        UploadController controller = new UploadController(null);
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/uploadWin.fxml"), bundle);
        loader.setController(controller);
        Parent root = loader.load();
        if(Locale.getDefault().getCountry().equals("BS")) stageUpload.setTitle("Učitaj");
        else stageUpload.setTitle("Upload");
        stageUpload.setResizable(true);
        stageUpload.setScene(new Scene(root, 850, 600));
        stageUpload.show();
    }

    public void overviewAction () throws IOException {
        ResourceBundle bundle = ResourceBundle.getBundle("translation");
        Stage stageOverview= new Stage();
        OverviewAdminController controller = new OverviewAdminController();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/overviewPapersAdmin.fxml"), bundle);
        loader.setController(controller);
        Parent root = loader.load();
        if (Locale.getDefault().getCountry().equals("BS")) stageOverview.setTitle("Pregled");
        else stageOverview.setTitle("Overview");
        stageOverview.setResizable(true);
        stageOverview.setScene(new Scene(root, USE_COMPUTED_SIZE, USE_COMPUTED_SIZE));
        stageOverview.setResizable(false);
        stageOverview.show();
    }
}
