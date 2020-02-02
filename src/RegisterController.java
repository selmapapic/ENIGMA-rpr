import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.util.ArrayList;

public class RegisterController {
    public TextField fldName, fldSurname, fldEmail;
    public PasswordField fldPass;
    public Button btnRegister, btnCancel;
    public ChoiceBox choiceEduDeg;
    public ArrayList<String> temporary;
    public ObservableList<String> academicDegree = FXCollections.observableArrayList();

    public RegisterController() {
        academicDegree.add("Srednja skola");
        academicDegree.add("Bachelor");
        academicDegree.add("Master");
        academicDegree.add("Doktorat");
    }

    @FXML
    public void initialize() {
        choiceEduDeg.setItems(academicDegree);

    }

    public void cancelAction () {
        Stage stage = (Stage) btnCancel.getScene().getWindow();
        stage.close();
    }

}
