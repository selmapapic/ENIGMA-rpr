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
    public UsersDAO dao = UsersDAO.getInstance();

    public RegisterController() {
        academicDegree.add("Srednja skola");
        academicDegree.add("Bachelor");
        academicDegree.add("Master");
        academicDegree.add("Doktorat");
    }

    private boolean isTheStyleClassInvalid (String s) {
        return s.contains("invalidField");
    }

    @FXML
    public void initialize() {
        choiceEduDeg.setItems(academicDegree);

    }

    public void cancelAction () {
        Stage stage = (Stage) btnCancel.getScene().getWindow();
        stage.close();
    }

    public void registerAction () {
        if(fldName.getText().isEmpty() || fldSurname.getText().isEmpty() || fldEmail.getText().isEmpty() || fldPass.getText().isEmpty() || choiceEduDeg.getSelectionModel().getSelectedItem() == null) {
            if(fldName.getText().isEmpty()) {
                fldName.getStyleClass().removeAll("validField");
                fldName.getStyleClass().add("invalidField");
            }
            else {
                fldName.getStyleClass().removeAll("invalidField");
                fldName.getStyleClass().add("validField");
            }

            if(fldSurname.getText().isEmpty()) {
                fldSurname.getStyleClass().removeAll("validField");
                fldSurname.getStyleClass().add("invalidField");
            }
            else {
                fldSurname.getStyleClass().removeAll("invalidField");
                fldSurname.getStyleClass().add("validField");
            }

            if(fldEmail.getText().isEmpty()) {
                fldEmail.getStyleClass().removeAll("validField");
                fldEmail.getStyleClass().add("invalidField");
            }
            else {
                fldEmail.getStyleClass().removeAll("invalidField");
                fldEmail.getStyleClass().add("validField");
            }

            if(fldPass.getText().isEmpty()) {
                fldPass.getStyleClass().removeAll("validField");
                fldPass.getStyleClass().add("invalidField");
            }
            else {
                fldPass.getStyleClass().removeAll("invalidField");
                fldPass.getStyleClass().add("validField");
            }
        }

        if(!isTheStyleClassInvalid(fldName.getStyleClass().toString()) && !isTheStyleClassInvalid(fldSurname.getStyleClass().toString())
            && !isTheStyleClassInvalid(fldEmail.getStyleClass().toString()) && !isTheStyleClassInvalid(fldPass.getStyleClass().toString()) && !(choiceEduDeg.getSelectionModel().getSelectedItem() == null)) {
            User user = new User(fldName.getText(), fldSurname.getText(), fldEmail.getText(), choiceEduDeg.getSelectionModel().getSelectedItem().toString(), fldPass.getText());
            dao.insertNewUser(user);

            Stage stage = (Stage) btnCancel.getScene().getWindow();
            stage.close();
        }

    }

}
