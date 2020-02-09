import javafx.scene.control.Label;

public class UserPapersOverviewController {
    private User currentUser;
    public Label ime;
    public UserPapersOverviewController(User user) {
        currentUser = user;
    }

    public void initialize () {
        //ime.setText(currentUser.getName());
    }
}
