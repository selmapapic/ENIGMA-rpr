import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;

public class OverviewAdminController {
    public Button btnEdit, btnPrint, btnDelete, btnExit;
    public ListView<ScientificPaper> listViewPapers;
    public AllPapers papers = new AllPapers();
    //public PapersDAO dao = PapersDAO.getInstance();

    @FXML
    public void initialize () {
        listViewPapers.setItems(papers.getPapersObservable());
    }

    public void editAction () {

    }

    public void deleteAction () {

    }

    public void printAction () {

    }

    public void exitAction () {

    }
}
