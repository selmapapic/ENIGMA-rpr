import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import org.mindrot.jbcrypt.BCrypt;

public class EditAccountController {
    public AnchorPane anchorCurrentInfo, anchorPassowrd, anchorEmail, anchorDeleteAcc;
    public Label labelName, labelSurname, labelEmail, labelAcDegree;
    public PasswordField fldOldPassword, fldNewPassword, fldConfirmPassword;
    public Label labelEmailChange;
    public TextField fldEmailChange;
    public PasswordField fldPassForDelete;
    public User currentUser;
    public UsersDAO dao = UsersDAO.getInstance();

    private String hashPassword(String plainTextPassword) {     //hesiranje passworda
        return BCrypt.hashpw(plainTextPassword, BCrypt.gensalt());
    }

    public EditAccountController (User current) {
        currentUser = current;
    }

    @FXML
    public void initialize () {
        anchorCurrentInfo.toFront();
        labelName.setText(currentUser.getName());
        labelSurname.setText(currentUser.getSurname());
        labelEmail.setText(currentUser.getMail());
        labelAcDegree.setText(currentUser.getDegOfEducation());
        fldOldPassword.textProperty().addListener((observable, oldPass, newPass) -> {
            if(!newPass.isEmpty() && newPass.equals(currentUser.getPassword())) {
                fldOldPassword.getStyleClass().removeAll("invalidField");
                fldOldPassword.getStyleClass().add("validField");
            }
            else {
                fldOldPassword.getStyleClass().removeAll("validField");
                fldOldPassword.getStyleClass().add("invalidField");
            }
        });
    }

    public void chgPasswordAction () {
        anchorPassowrd.toFront();

    }

    public void chgEmailAction () {
        anchorEmail.toFront();
    }

    public void confirmAEAction () {
        currentUser.setMail(fldEmailChange.getText());
        dao.editUser(currentUser);
    }

    public void deleteAccountAction () {
        anchorDeleteAcc.toFront();
    }

    public void btnDeleteAccountAction () {

    }
}
