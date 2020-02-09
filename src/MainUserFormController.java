import com.sun.jdi.ClassType;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;

import java.time.LocalDate;

public class MainUserFormController {
    private User currentUser;
    public Label labelName, labelName1, labelInitial, labelInitial1;
    public MainUserFormController(User user) {
        currentUser = user;
    }
    public AnchorPane anchorMainView, anchorFilterView;
    public TableView<ScientificPaper> tableViewPapers, tableViewPapers1;
    public TableColumn<ScientificPaper, String> colTitle, colTitle1, colCategory, colCategory1;
    public TableColumn<ScientificPaper, ClassType> colType, colType1;
    public TableColumn<ScientificPaper, LocalDate> colReleaseDate, colReleaseDate1;
    public TableColumn<ScientificPaper, Author> colAuthor, colAuthor1;
    public TableColumn<ScientificPaper, Integer> colId, colId1;
    public PapersDAO dao = PapersDAO.getInstance();
    public ObservableList<ScientificPaper> papers = FXCollections.observableArrayList();
    public ScientificPaper currentPaper;
    public ChoiceBox<String> choiceType, choiceCategory;
    public DatePicker dpReleaseDate;

    private void initializeTableView () {
        colTitle.setCellValueFactory(new PropertyValueFactory<ScientificPaper, String>("title"));
        colAuthor.setCellValueFactory(new PropertyValueFactory<ScientificPaper, Author>("author"));
        colReleaseDate.setCellValueFactory(new PropertyValueFactory<ScientificPaper, LocalDate>("releaseDate"));
        colCategory.setCellValueFactory(new PropertyValueFactory<ScientificPaper, String>("category"));
        colId.setCellValueFactory(new PropertyValueFactory<ScientificPaper, Integer>("id"));

        colTitle1.setCellValueFactory(new PropertyValueFactory<ScientificPaper, String>("title"));
        colAuthor1.setCellValueFactory(new PropertyValueFactory<ScientificPaper, Author>("author"));
        colReleaseDate1.setCellValueFactory(new PropertyValueFactory<ScientificPaper, LocalDate>("releaseDate"));
        colCategory1.setCellValueFactory(new PropertyValueFactory<ScientificPaper, String>("category"));
        colId1.setCellValueFactory(new PropertyValueFactory<ScientificPaper, Integer>("id"));
    }

    @FXML
    public void initialize () {
        //System.out.println(currentUser);
        anchorMainView.toFront();
        labelName.setText(currentUser.getName());
        labelName1.setText(currentUser.getName());
        labelInitial.setText(String.valueOf(currentUser.getName().charAt(0)));
        labelInitial1.setText(String.valueOf(currentUser.getName().charAt(0)));

        papers.addAll(dao.getAllPapers());
        tableViewPapers.setItems(papers);
        tableViewPapers1.setItems(papers);
        initializeTableView();
        dpReleaseDate
    }

    public void filterAction () {
        anchorFilterView.toFront();
    }

    public void viewAction () {

    }

    public void editAccountAction () {

    }

    public void logOutAction () {

    }
}
