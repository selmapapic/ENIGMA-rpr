import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.mindrot.jbcrypt.BCrypt;

import java.util.Locale;
import java.util.regex.Pattern;

public class EditAccountController {
    public AnchorPane anchorCurrentInfo, anchorPassword, anchorEmail, anchorDeleteAcc;
    public Label labelName, labelSurname, labelEmail, labelAcDegree;
    public PasswordField fldOldPassword, fldNewPassword, fldConfirmPassword;
    public Label labelEmailChange, labelWrongFormat;
    public TextField fldEmailChange;
    public PasswordField fldPassForDelete;
    public User currentUser;
    public PapersUsersDAO dao = PapersUsersDAO.getInstance();
    public Label closeOldForm, labelAreYouSure, labelAreYouSure2;

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

    public EditAccountController (User current, Label label) {
        currentUser = current;
        closeOldForm = label; //labela iz prethodne forme kako bi se mogla i ona zatvoriti ukoliko se obrise racun
    }



    @FXML
    public void initialize () {
        anchorCurrentInfo.toFront();
        labelName.setText(currentUser.getName());
        labelSurname.setText(currentUser.getSurname());
        labelEmail.setText(currentUser.getMail());
        labelAcDegree.setText(currentUser.getDegOfEducation());
        fldOldPassword.textProperty().addListener((observable, oldPass, newPass) -> {
            if(!newPass.isEmpty() && checkPass(newPass, currentUser.getPassword())) {
                fldOldPassword.getStyleClass().removeAll("invalidField");
                fldOldPassword.getStyleClass().add("validField");
            }
            else {
                fldOldPassword.getStyleClass().removeAll("validField");
                fldOldPassword.getStyleClass().add("invalidField");
            }
        });

        fldPassForDelete.textProperty().addListener((observable, oldPass, newPass) -> {
            if(!newPass.isEmpty() && checkPass(newPass, currentUser.getPassword())) {
                fldPassForDelete.getStyleClass().removeAll("invalidField");
                fldPassForDelete.getStyleClass().add("validField");
            }
            else {
                fldPassForDelete.getStyleClass().removeAll("validField");
                fldPassForDelete.getStyleClass().add("invalidField");
            }
        });

        fldNewPassword.textProperty().addListener((observable, oldPass, newPass) -> {
            if(!newPass.isEmpty()) {
                fldNewPassword.getStyleClass().removeAll("invalidField");
                fldNewPassword.getStyleClass().add("validField");
            }
            else {
                fldNewPassword.getStyleClass().removeAll("validField");
                fldNewPassword.getStyleClass().add("invalidField");
            }
        });

        fldConfirmPassword.textProperty().addListener((observable, oldPass, newPass) -> {
            if(!newPass.isEmpty() && newPass.equals(fldNewPassword.getText())) {
                fldConfirmPassword.getStyleClass().removeAll("invalidField");
                fldConfirmPassword.getStyleClass().add("validField");
            }
            else {
                fldConfirmPassword.getStyleClass().removeAll("validField");
                fldConfirmPassword.getStyleClass().add("invalidField");
            }
        });

        labelWrongFormat.setText("");
        if(Locale.getDefault().getCountry().equals("BS")) {
            labelAreYouSure.setText("Ukoliko odlucite obrisati svoj racun, nema povratka.");
            labelAreYouSure2.setText("Molimo budite sigurni da to zelite");
        }
        else {
            labelAreYouSure.setText("Once you delete your account, there is no going back.");
            labelAreYouSure2.setText("Please be certain");
        }
    }

    public void chgPasswordAction () {
        anchorPassword.toFront();

    }

    public void confirmAPAction () {
        if(!fldOldPassword.getStyleClass().contains("invalidField") && !fldNewPassword.getStyleClass().contains("invalidField") && !fldConfirmPassword.getStyleClass().contains("invalidField")) {
            fldNewPassword.getText();
            currentUser.setPassword(hashPassword(fldNewPassword.getText()));
            dao.editUser(currentUser);
            anchorCurrentInfo.toFront();
        }
    }

    public void chgEmailAction () {
        labelWrongFormat.setText("");
        labelEmailChange.setText(currentUser.getMail());
        fldEmailChange.setText("");
        anchorEmail.toFront();
    }

    public void confirmAEAction () {
        if(fldEmailChange.getText().isEmpty() || !isValidEmail(fldEmailChange.getText())) {
            fldEmailChange.getStyleClass().add("invalidField");
            if(!isValidEmail(fldEmailChange.getText())) {
                if(Locale.getDefault().getCountry().equals("BS")) labelWrongFormat.setText("Pogrešan format email adrese");
                else labelWrongFormat.setText("Wrong email format");
            }
            return;
        }
        labelWrongFormat.setText("");
        currentUser.setMail(fldEmailChange.getText());
        dao.editUser(currentUser);
        anchorCurrentInfo.toFront();
        labelEmail.setText(currentUser.getMail()); //refresha se email
    }

    public void deleteAccountAction () {
        anchorDeleteAcc.toFront();
    }

    public void deleteAccountBtnAction () {
        if(fldPassForDelete.getStyleClass().contains("invalidField")) return;
        dao.deleteUser(currentUser);
        exitAction();
        Stage curentStage = (Stage) closeOldForm.getScene().getWindow();
        curentStage.close();

    }

    public void exitAction () {
        Stage curentStage = (Stage) anchorDeleteAcc.getScene().getWindow();
        curentStage.close();
    }
}
