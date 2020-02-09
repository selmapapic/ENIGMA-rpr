import com.sun.jdi.ClassType;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.time.LocalDate;

public class OverviewAdminController {
    public Button btnEdit, btnPrint, btnDelete, btnExit;
    public TableView<ScientificPaper> tableViewPapers;
    public TableColumn<ScientificPaper, String> colTitle, colCategory;
    public TableColumn<ScientificPaper, ClassType> colType;
    public TableColumn<ScientificPaper, LocalDate> colReleaseDate;
    public TableColumn<ScientificPaper, Author> colAuthor;
    public TableColumn<ScientificPaper, Integer> colId;
    //public AllPapers papers = new AllPapers();
    public PapersDAO dao = PapersDAO.getInstance();
    public ObservableList<ScientificPaper> papers = FXCollections.observableArrayList();
    public ScientificPaper currentPaper;

    @FXML
    public void initialize () {
        papers.addAll(dao.getAllPapers());
        tableViewPapers.setItems(papers);

        colTitle.setCellValueFactory(new PropertyValueFactory<ScientificPaper, String>("title"));
        colAuthor.setCellValueFactory(new PropertyValueFactory<ScientificPaper, Author>("author"));
        colReleaseDate.setCellValueFactory(new PropertyValueFactory<ScientificPaper, LocalDate>("releaseDate"));
        colCategory.setCellValueFactory(new PropertyValueFactory<ScientificPaper, String>("category"));
        colId.setCellValueFactory(new PropertyValueFactory<ScientificPaper, Integer>("id"));
        //todo
        //colType.setCellValueFactory(new PropertyValueFactory<>(getClass().toString()));
        //colType.setCellValueFactory(papers.getClass().toString());
        tableViewPapers.getSelectionModel().selectedItemProperty().addListener((obs, oldIme, newIme) -> {
            if(tableViewPapers.getSelectionModel().getSelectedItem() == null) currentPaper = null;
            else currentPaper = tableViewPapers.getSelectionModel().getSelectedItem();
            System.out.println(currentPaper);
        });
    }

    public void editAction () {

    }

    public void deleteAction () {
        if(currentPaper != null) {
            dao.removePaper(currentPaper);
            papers.remove(currentPaper);
        }
    }

    public void printAction () {

    }

    public void exitAction () {

    }
}
