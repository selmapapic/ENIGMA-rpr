import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import net.sf.jasperreports.engine.JRException;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;

import static javafx.scene.control.PopupControl.USE_COMPUTED_SIZE;

public class OverviewAdminController {
    public Button btnEdit, btnPrint, btnDelete, btnExit;
    public TableView<ScientificPaper> tableViewPapers;
    public TableColumn<ScientificPaper, String> colTitle, colCategory;
    public TableColumn<ScientificPaper, String> colType;
    public TableColumn<ScientificPaper, LocalDate> colReleaseDate;
    public TableColumn<ScientificPaper, Author> colAuthor;
    public TableColumn<ScientificPaper, Integer> colId;
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
        colType.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getType().getName()));
        tableViewPapers.getSelectionModel().selectedItemProperty().addListener((obs, oldIme, newIme) -> {
            if(tableViewPapers.getSelectionModel().getSelectedItem() == null) currentPaper = null;
            else {
                currentPaper = tableViewPapers.getSelectionModel().getSelectedItem();
            }
        });
    }

    public void editAction () throws IOException {
        Stage stageUpload = new Stage();
        //System.out.println(currentPaper);
        UploadController controller = new UploadController(currentPaper);
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/uploadWin.fxml"));
        loader.setController(controller);
        Parent root = loader.load();
        stageUpload.setTitle("Upload");
        stageUpload.setResizable(true);
        stageUpload.setScene(new Scene(root, USE_COMPUTED_SIZE, USE_COMPUTED_SIZE));
        stageUpload.setOnHiding(windowEvent -> {
            if(controller.getForEdit() == null) {
                //System.out.println("nista");
            } else {
                papers.removeAll(dao.getAllPapers());
                dao.editPaper(controller.getForEdit());
                tableViewPapers.refresh();
                papers.addAll(dao.getAllPapers());
            }
        });
        stageUpload.show();
    }

    public void deleteAction () {
        if(currentPaper != null) {
            File file = new File("resources/files", currentPaper.getTitle() + ".txt");
            file.delete();
            dao.removePaper(currentPaper);
            papers.remove(currentPaper);
        }
    }

    public void printAction () throws JRException {
        new PrintReport().showReport(dao.getConnection());
    }

    public void exitAction () {
        Stage cureentStage = (Stage) tableViewPapers.getScene().getWindow();
        cureentStage.close();
    }
}
