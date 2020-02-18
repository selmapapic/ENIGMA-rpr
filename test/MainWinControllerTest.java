import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
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
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(ApplicationExtension.class)
public class MainWinControllerTest {
    @Start
    public void start(Stage stage) {
        File dbfile = new File("scientificPapers.db");
        dbfile.delete();

        MainWinController ctrl = new MainWinController();
        ResourceBundle bundle = ResourceBundle.getBundle("translation");
        Parent root = null;
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/mainWin.fxml"), bundle);
        loader.setController(ctrl);
        try {
            root = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        stage.setTitle("ENIGMA");
        stage.setScene(new Scene(root, USE_COMPUTED_SIZE, USE_COMPUTED_SIZE));
        stage.show();
        stage.setResizable(false);
        stage.toFront();
    }

    @Test
    void register(FxRobot robot) {
        ChoiceBox choiceEdu = robot.lookup("#choiceEduDeg").queryAs(ChoiceBox.class);
        assertEquals(5, choiceEdu.getItems().size());
    }

    @Test
    void checkMail (FxRobot robot) {
        robot.clickOn("#btnEnglish");
        robot.clickOn("#btnOpenRegister").clickOn("#fldEmailSignUp");
        robot.write("abc");
        robot.clickOn("#btnRegister");
        Label label = robot.lookup("#labelWrongFormat").queryAs(Label.class);
        assertEquals(label.getText(), "Wrong email format");

        robot.clickOn("#btnOpenRegister").clickOn("#fldEmailSignUp");
        robot.press(KeyCode.CONTROL).press(KeyCode.A).release(KeyCode.A).release(KeyCode.CONTROL);
        robot.write("abc@gmail.com");
        robot.clickOn("#btnRegister");
        assertEquals(label.getText(), "");
    }

    @Test
    void register2 (FxRobot robot) {
        robot.clickOn("#btnEnglish");
        robot.clickOn("#btnOpenRegister").clickOn("#fldName");
        robot.write("Neko");
        robot.clickOn("#fldSurname");
        robot.write("Nekic");
        robot.clickOn("#fldEmailSignUp").write("nekonekic@gmail.com");
        robot.clickOn("#fldPassSignUp").write("neko");

        robot.clickOn("#btnRegister");

        Label label = robot.lookup("#labelAcDegree").queryAs(Label.class);
        assertEquals(label.getText(), "Academic degree not selected");

        robot.clickOn("#choiceEduDeg").clickOn("High school");
        robot.clickOn("#btnRegister");

        PapersUsersDAO dao = PapersUsersDAO.getInstance();
        assertNotNull(dao.getUser("nekonekic@gmail.com"));
    }

    @Test
    public void logIn(FxRobot robot) {
        robot.clickOn("#btnEnglish");
        robot.clickOn("#btnOpenRegister").clickOn("#fldName");
        robot.write("Selma");
        robot.clickOn("#fldSurname");
        robot.write("Selmic");
        robot.clickOn("#fldEmailSignUp").write("selmaselmic@gmail.com");
        robot.clickOn("#choiceEduDeg").clickOn("High school");
        robot.clickOn("#fldPassSignUp").write("selma");
        robot.clickOn("#btnRegister");



        PapersUsersDAO dao = PapersUsersDAO.getInstance();
        assertNotNull(dao.getUser("selmaselmic@gmail.com"));

        robot.clickOn("#fldEmail").write("selmaselmic@gmail.com");
        robot.clickOn("#fldPass").write("selma");
        robot.clickOn("#btnSignIn");

        Label label = robot.lookup("#labelGreeting").queryAs(Label.class);
        assertEquals(label.getText(), "Welcome back, Selma");
    }

    @Test
    public void language (FxRobot robot) {
        robot.clickOn("#btnBosanski");
        Button btn = robot.lookup("#btnSignIn").queryAs(Button.class);
        assertEquals(btn.getText(), "Prijava");
    }
}
