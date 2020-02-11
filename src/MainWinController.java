import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Locale;
import java.util.ResourceBundle;

import static javafx.scene.control.PopupControl.USE_COMPUTED_SIZE;

public class MainWinController {
    public Button btnSignIn;
    public TextField fldEmail;
    public PasswordField fldPass;
    public UsersDAO dao = UsersDAO.getInstance();
    public AnchorPane anchorSignUp, anchorSignIn;
    //register
    public TextField fldName, fldSurname, fldEmailSignUp;
    public PasswordField fldPassSignUp;
    public Button btnRegister;
    public ChoiceBox<String> choiceEduDeg;
    public ObservableList<String> academicDegree = FXCollections.observableArrayList();

    private boolean isTheStyleClassInvalid (String s) {
        return s.contains("invalidField");
    }

    public MainWinController() {
        if(Locale.getDefault().getCountry().equals("BS")) {
            academicDegree.add("Stepen obrazovanja");
            academicDegree.add("Srednja skola");
            academicDegree.add("Bachelor");
            academicDegree.add("Master");
            academicDegree.add("Doktorat");
        }
        else {
            academicDegree.add("Academic degree");
            academicDegree.add("High school");
            academicDegree.add("Bachelor");
            academicDegree.add("Master");
            academicDegree.add("Doctorate");
        }
    }

    @FXML
    public void initialize () {
        anchorSignUp.toBack();
        anchorSignIn.toFront();
        choiceEduDeg.setItems(academicDegree);
        choiceEduDeg.getSelectionModel().select("0");
    }

    public void adminAction () throws IOException {
        ResourceBundle bundle = ResourceBundle.getBundle("translation");
        Stage stageAdmin = new Stage();
        AdminController controller = new AdminController();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/adminWin.fxml"), bundle);
        loader.setController(controller);
        Parent root = loader.load();
        stageAdmin.setTitle("Administrator");
        stageAdmin.setResizable(true);
        stageAdmin.setScene(new Scene(root, USE_COMPUTED_SIZE, USE_COMPUTED_SIZE));
        stageAdmin.setResizable(false);
        stageAdmin.show();
    }

    public void openRegisterAction () {
        anchorSignUp.toFront();
        fldEmail.setText("");
        fldPass.setText("");
        fldEmail.getStyleClass().removeAll("invalidField", "validField");
        fldPass.getStyleClass().removeAll("invalidField", "validField");
    }

    public void openSignInAction () {
        anchorSignIn.toFront();
        fldName.setText("");        //pri switchanju, brise se stari tekst
        fldSurname.setText("");
        fldEmailSignUp.setText("");
        fldPassSignUp.setText("");
        choiceEduDeg.getSelectionModel().clearSelection();
        fldName.getStyleClass().removeAll("invalidField", "validField");       //brisu se i obrubi
        fldSurname.getStyleClass().removeAll("invalidField", "validField");
        fldEmailSignUp.getStyleClass().removeAll("invalidField", "validField");
        fldPassSignUp.getStyleClass().removeAll("invalidField", "validField");
    }

    private void fieldsValidationCheck(TextField fldName, TextField fldSurname) {
        if(fldName.getText().isEmpty()) {
            fldName.getStyleClass().removeAll("validField");
            fldName.getStyleClass().add("invalidField");
        }
        else {
            fldName.getStyleClass().removeAll("invalidField");
        }

        if(fldSurname.getText().isEmpty()) {
            fldSurname.getStyleClass().removeAll("validField");
            fldSurname.getStyleClass().add("invalidField");
        }
        else {
            fldSurname.getStyleClass().removeAll("invalidField");
        }
    }

    public void registerAction () {
        if(fldName.getText().isEmpty() || fldSurname.getText().isEmpty() || fldEmail.getText().isEmpty() || fldPass.getText().isEmpty() || (choiceEduDeg.getSelectionModel().getSelectedItem().equals("Stepen obrazovanja") && choiceEduDeg.getSelectionModel().getSelectedItem().equals("Academic degree"))) {
            fieldsValidationCheck(fldName, fldSurname);

            fieldsValidationCheck(fldEmailSignUp, fldPassSignUp);
        }

        if(!isTheStyleClassInvalid(fldName.getStyleClass().toString()) && !isTheStyleClassInvalid(fldSurname.getStyleClass().toString()) //ukoliko su sva polja validna, tj nijedno polje nije nevalidno, dodaje se novi korisnik
                && !isTheStyleClassInvalid(fldEmailSignUp.getStyleClass().toString()) && !isTheStyleClassInvalid(fldPassSignUp.getStyleClass().toString()) && !(choiceEduDeg.getSelectionModel().getSelectedItem() == null)) {
            User user = new User(fldName.getText(), fldSurname.getText(), fldEmailSignUp.getText(), choiceEduDeg.getSelectionModel().getSelectedItem(), fldPassSignUp.getText());
            dao.insertNewUser(user);

            openSignInAction();
        }
    }

    public void signInAction () throws IOException {
        if(fldEmail.getText().isEmpty() || fldPass.getText().isEmpty()) {
            fieldsValidationCheck(fldEmail, fldPass);
        }
        else {
            fldPass.getStyleClass().removeAll("invalidField", "validField"); //uklanja se crveni obrub ukoliko nisu prazna polja
            fldEmail.getStyleClass().removeAll("invalidField", "validField");
            User user = dao.getUser(fldEmail.getText(), fldPass.getText());

            if(user == null) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                if(Locale.getDefault().getCountry().equals("BS")) {
                    alert.setTitle("Greška");
                    alert.setHeaderText("Pogrešan email i šifra!");
                    alert.setContentText("Pokušajte ponovo!");
                    alert.showAndWait();
                }
                else {
                    alert.setTitle("Error");
                    alert.setHeaderText("Incorrect email and password!");
                    alert.setContentText("Try again!");
                    alert.showAndWait();
                }
            }
            else {
                ResourceBundle bundle = ResourceBundle.getBundle("translation");
                Stage stageMainWin = new Stage();
                MainUserFormController controller = new MainUserFormController(user);
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/mainUserForm.fxml"), bundle);
                loader.setController(controller);
                Parent root = loader.load();
                if(Locale.getDefault().getCountry().equals("BS")) stageMainWin.setTitle("Naučni radovi");
                else stageMainWin.setTitle("Scientific papers");
                stageMainWin.setResizable(true);
                stageMainWin.setScene(new Scene(root, USE_COMPUTED_SIZE, USE_COMPUTED_SIZE));
                stageMainWin.setResizable(false);
                stageMainWin.show();
                fldPass.setText("");
                fldEmail.setText("");
            }
        }

    }
}
