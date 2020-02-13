import javafx.scene.control.Alert;

import java.util.Locale;

public class NoPaperSelectedException extends Exception {
    public NoPaperSelectedException (String message) {
        super(message);
        Alert alert = new Alert(Alert.AlertType.WARNING);
        if(Locale.getDefault().getCountry().equals("BS")) {
            alert.setTitle("Upozorenje");
            alert.setHeaderText("Niste izabrali nijedan rad sa liste!");
            alert.setContentText("Molimo prvo izaberite rad, a potom kliknite 'Pogledaj'.");
        }
        else {
            alert.setTitle("Warning");
            alert.setHeaderText("You haven't selected any of the papers from the list!");
            alert.setContentText("Please choose one and then click 'View'.");
        }
        alert.showAndWait();
    }
}
