import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.mindrot.jbcrypt.BCrypt;

import java.io.IOException;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.regex.Pattern;

import static javafx.scene.control.PopupControl.USE_COMPUTED_SIZE;

public class MainWinController {
    public Button btnSignIn;
    public TextField fldEmail;
    public PasswordField fldPass;
    public PapersUsersDAO dao = PapersUsersDAO.getInstance();
    public AnchorPane anchorSignUp, anchorSignIn;
    public Label labelWrongPass, labelWrongFormat, labelAcDegree, labelImpact;
    //register
    public TextField fldName, fldSurname, fldEmailSignUp;
    public PasswordField fldPassSignUp;
    public Button btnRegister;
    public ChoiceBox<String> choiceEduDeg;
    public ObservableList<String> academicDegree = FXCollections.observableArrayList();

    private boolean isTheStyleClassInvalid () {
        return fldName.getStyleClass().toString().contains("invalidField") || fldSurname.getStyleClass().toString().contains("invalidField") ||
                fldEmailSignUp.getStyleClass().toString().contains("invalidField") || fldPassSignUp.getStyleClass().toString().contains("invalidField");
    }

    private void invalidateField (TextField textField) {
        textField.getStyleClass().removeAll("validField");
        textField.getStyleClass().add("invalidField");
    }

    private void removeCssField (TextField textField) {
        textField.getStyleClass().removeAll("invalidField", "validField");
    }

    private String hashPassword(String plainTextPassword) {     //hesiranje passworda
        return BCrypt.hashpw(plainTextPassword, BCrypt.gensalt());
    }

    private boolean checkPass(String plainPassword, String hashedPassword) {    //provjera hesiranog i obicnog teksta
        return BCrypt.checkpw(plainPassword, hashedPassword);
    }

    private boolean isValidEmail (String email) {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\."+
                "[a-zA-Z0-9_+&*-]+)*@" +
                "(?:[a-zA-Z0-9-]+\\.)+[a-z" +
                "A-Z]{2,7}$";

        Pattern pat = Pattern.compile(emailRegex);
        if (email == null)
            return false;
        return pat.matcher(email).matches();
    }

    private void showAlertWrongInfo () {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        if(Locale.getDefault().getCountry().equals("BS")) {
            alert.setTitle("Greška");
            alert.setHeaderText("Pogrešni pristupni podaci!");
            alert.setContentText("Pokušajte ponovo!");
        }
        else {
            alert.setTitle("Error");
            alert.setHeaderText("Incorrect email and password!");
            alert.setContentText("Try again!");
        }
        alert.showAndWait();
    }

    public void defaultAcDegree() {
        academicDegree.clear();
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
        defaultAcDegree();
        labelWrongPass.setText("");
        labelWrongFormat.setText("");
        labelAcDegree.setText("");
        anchorSignUp.toBack();
        anchorSignIn.toFront();
        choiceEduDeg.setItems(academicDegree);
        if(Locale.getDefault().getCountry().equals("BS")) labelImpact.setText("kako biste vi sutra promijenili svijet.");
        else labelImpact.setText("so you can make an impact on tomrrow.");
        if(Locale.getDefault().getCountry().equals("BS")) choiceEduDeg.setValue("Stepen obrazovanja");
        else choiceEduDeg.setValue("Academic degree");
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
        labelWrongPass.setText("");
        removeCssField(fldEmail);
        removeCssField(fldPass);
    }

    public void openSignInAction () {
        anchorSignIn.toFront();
        fldName.setText("");        //pri switchanju, brise se stari tekst
        fldSurname.setText("");
        fldEmailSignUp.setText("");
        fldPassSignUp.setText("");
        labelAcDegree.setText("");
        labelWrongFormat.setText("");
        if(Locale.getDefault().getCountry().equals("BS")) choiceEduDeg.setValue("Stepen obrazovanja");
        else choiceEduDeg.setValue("Academic degree");
        removeCssField(fldName);       //brisu se obrubi
        removeCssField(fldSurname);
        removeCssField(fldEmailSignUp);
        removeCssField(fldPassSignUp);
    }

    public void registerAction () {
        if(fldName.getText().isEmpty()) invalidateField(fldName);
        else removeCssField(fldName);
        if(fldSurname.getText().isEmpty()) invalidateField(fldSurname);
        else removeCssField(fldSurname);
        if(fldEmailSignUp.getText().isEmpty() || (!fldEmailSignUp.getText().isEmpty() && !isValidEmail(fldEmailSignUp.getText()))) {
            invalidateField(fldEmailSignUp);
            if(!fldEmailSignUp.getText().isEmpty() && !isValidEmail(fldEmailSignUp.getText())) {
                if(Locale.getDefault().getCountry().equals("BS")) labelWrongFormat.setText("Pogrešan format email adrese");
                else labelWrongFormat.setText("Wrong email format");
            }
            else {
                labelWrongFormat.setText("");
            }
        }
        else {
            removeCssField(fldEmailSignUp);
            labelWrongFormat.setText("");
        }
        if(fldPassSignUp.getText().isEmpty()) invalidateField(fldPassSignUp);
        else removeCssField(fldPassSignUp);
        if(choiceEduDeg.getSelectionModel().getSelectedItem().equals("Stepen obrazovanja") || choiceEduDeg.getSelectionModel().getSelectedItem().equals("Academic degree")) {
            choiceEduDeg.getStyleClass().add("invalidField");
            if(Locale.getDefault().getCountry().equals("BS")) labelAcDegree.setText("Niste odabrali stepen obrazovanja");
            else labelAcDegree.setText("Academic degree not selected");
        }
        else {
            choiceEduDeg.getStyleClass().removeAll("invalidField");
            labelAcDegree.setText("");
        }
        //ukoliko su sva polja validna, tj nijedno polje nije nevalidno, dodaje se novi korisnik
        if(!isTheStyleClassInvalid() && !(choiceEduDeg.getSelectionModel().getSelectedItem().equals("Stepen obrazovanja") || choiceEduDeg.getSelectionModel().getSelectedItem().equals("Academic degree"))) {
            User user = new User(0, fldName.getText(), fldSurname.getText(), fldEmailSignUp.getText(), choiceEduDeg.getSelectionModel().getSelectedItem(), hashPassword(fldPassSignUp.getText()));
            dao.insertNewUser(user);

            openSignInAction();

        }
    }

    public void signInAction () throws IOException {
        if(fldEmail.getText().isEmpty() || fldPass.getText().isEmpty()) {
            if(fldEmail.getText().isEmpty()) invalidateField(fldEmail);
            else removeCssField(fldEmail);
            if(fldPass.getText().isEmpty()) invalidateField(fldPass);
            else removeCssField(fldPass);
        }
        else {
            removeCssField(fldPass); //uklanja se crveni obrub ukoliko nisu prazna polja
            removeCssField(fldEmail);

            User user = dao.getUser(fldEmail.getText());

            if(user == null) {
                showAlertWrongInfo();
            }
            else {
                boolean checkPassowrd = checkPass(fldPass.getText(), user.getPassword());       //provjera hesiranog i unesenog passworda
                if(!checkPassowrd) {
                    if(Locale.getDefault().getCountry().equals("BS")) labelWrongPass.setText("Pogrešna šifra");
                    else labelWrongPass.setText("Incorrect password");
                }
                else {
                    labelWrongPass.setText(""); //brise se labela za "wrong pass"

                    ResourceBundle bundle = ResourceBundle.getBundle("translation");
                    Stage stageMainWin = new Stage();
                    MainUserFormController controller = new MainUserFormController(user);
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/mainUserForm.fxml"), bundle);
                    loader.setController(controller);
                    Parent root = loader.load();
                    if (Locale.getDefault().getCountry().equals("BS")) stageMainWin.setTitle("Naučni radovi");
                    else stageMainWin.setTitle("Scientific papers");
                    stageMainWin.setScene(new Scene(root, USE_COMPUTED_SIZE, USE_COMPUTED_SIZE));
                    stageMainWin.setResizable(false);
                    stageMainWin.show();
                    fldPass.setText("");
                    fldEmail.setText("");
                }
            }
        }
    }

    public void bosanskiAction () {
        Locale.setDefault(new Locale("bs", "BS"));
        changeLanguage();
    }

    public void englishAction () {
        Locale.setDefault(new Locale("US", "US"));
        changeLanguage();
    }

    private void changeLanguage () {
        if(Locale.getDefault().getCountry().equals("BS")) labelImpact.setText("kako biste vi sutra promijenili svijet.");
        else labelImpact.setText("so you can make an impact on tomrrow.");
        Stage scene = (Stage) fldPass.getScene().getWindow();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/mainWin.fxml"), ResourceBundle.getBundle("translation"));
        loader.setController(this);
        try {
            scene.setScene(new Scene(loader.load()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
