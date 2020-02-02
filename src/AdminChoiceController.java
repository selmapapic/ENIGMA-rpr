import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;

import static javafx.scene.control.PopupControl.USE_COMPUTED_SIZE;

public class AdminChoiceController {
    public Button btnOverview, btnUpload;

    public void overviewAction () {

    }

    public void uploadAction () throws IOException {
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
