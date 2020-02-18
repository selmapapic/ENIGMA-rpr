import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.api.FxRobot;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;

import java.io.File;
import java.io.IOException;
import java.util.ResourceBundle;

import static javafx.scene.layout.Region.USE_COMPUTED_SIZE;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(ApplicationExtension.class)
public class AdminControllerTest {
    @Start
    public void start (Stage stage) {
        File dbfile = new File("scientificPapers.db");
        dbfile.delete();

        AdminController ctrl = new AdminController();
        ResourceBundle bundle = ResourceBundle.getBundle("translation");
        Parent root = null;
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/adminWin.fxml"), bundle);
        loader.setController(ctrl);
        try {
            root = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        stage.setTitle("Administrator");
        stage.setScene(new Scene(root, USE_COMPUTED_SIZE, USE_COMPUTED_SIZE));
        stage.show();
        stage.setResizable(false);
        stage.toFront();
    }

    @Test
    public void password(FxRobot robot) {
        robot.clickOn("#fldPass").write("123");
        robot.clickOn("#btnOk");
        PasswordField pass = robot.lookup("#fldPass").queryAs(PasswordField.class);

        assertTrue(pass.getStyleClass().toString().contains("invalidField"));

    }

    @Test
    public void password2(FxRobot robot) {
        robot.clickOn("#fldPass").write("administrator");
        PasswordField pass = robot.lookup("#fldPass").queryAs(PasswordField.class);

        assertFalse(pass.getStyleClass().toString().contains("invalidField"));
    }

    @Test
    public void deletePaper(FxRobot robot) {
        robot.clickOn("#fldPass").write("administrator");
        PasswordField pass = robot.lookup("#fldPass").queryAs(PasswordField.class);

        assertFalse(pass.getStyleClass().toString().contains("invalidField"));

        robot.clickOn("#btnOk");
        robot.clickOn("#btnOverview");

        TableView tableView = robot.lookup("#tableViewPapers").queryAs(TableView.class);
        int initial = tableView.getItems().size();
        tableView.getSelectionModel().select(2);
        robot.clickOn("#btnDelete");

        robot.lookup(".dialog-pane").tryQuery().isPresent();
        DialogPane dialogPane = robot.lookup(".dialog-pane").queryAs(DialogPane.class);

        Button ok = (Button) dialogPane.lookupButton(ButtonType.OK);
        robot.clickOn(ok);

        assertEquals(tableView.getItems().size() + 1, initial);

    }
}
