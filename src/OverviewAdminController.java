import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import net.sf.jasperreports.engine.JRException;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Locale;
import java.util.Optional;
import java.util.ResourceBundle;

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

//    private void showAlert () {
//        Alert alert = new Alert(Alert.AlertType.WARNING);
//        if(Locale.getDefault().getCountry().equals("BS")) {
//            alert.setTitle("Upozorenje");
//            alert.setHeaderText("Niste izabrali nijedan rad sa liste.");
//            alert.setContentText("Molimo izaberite rad pa pokušajte ponovo!");
//        }
//        else {
//            alert.setTitle("Warning");
//            alert.setHeaderText("You haven't selected any of the papers from the list!");
//            alert.setContentText("Please choose one and then try again!");
//        }
//        alert.showAndWait();
//    }

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
        if(currentPaper == null) try {
            throw new NoPaperSelectedException("Nothing selected");
        } catch (NoPaperSelectedException e) {
            return;
        }
        else {
            ResourceBundle bundle = ResourceBundle.getBundle("translation");
            Stage stageUpload = new Stage();
            UploadController controller = new UploadController(currentPaper);
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/uploadWin.fxml"), bundle);
            loader.setController(controller);
            Parent root = loader.load();
            stageUpload.setTitle("Upload");
            stageUpload.setResizable(true);
            stageUpload.setScene(new Scene(root, USE_COMPUTED_SIZE, USE_COMPUTED_SIZE));
            stageUpload.setOnHiding(windowEvent -> {
                if (controller.getForEdit() == null) {
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
    }

    public void deleteAction () throws NoPaperSelectedException {
        if(currentPaper == null) throw new NoPaperSelectedException("Nothing selected");
        else {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            if(Locale.getDefault().getCountry().equals("BS")) {
                alert.setTitle("Brisanje");
                alert.setHeaderText("Odabrali ste brisanje rada '" + currentPaper.getTitle() + "'.");
                alert.setContentText("Želite li nastaviti?");
            }
            else {
                alert.setTitle("Deletion");
                alert.setHeaderText("You chose to delete '" + currentPaper.getTitle() + "'.");
                alert.setContentText("Do you wish to proceed? ");
            }
            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.OK){
                File file = new File("resources/files", currentPaper.getTitle() + ".txt");
                file.delete();
                dao.removePaper(currentPaper);
                papers.remove(currentPaper);
            } else {
                return;
            }

        }
    }

    public void printAction () throws JRException {
        new PrintReport().showReport(dao.getConnection());
    }

    public void exitAction () {
        Stage curentStage = (Stage) tableViewPapers.getScene().getWindow();
        curentStage.close();
    }
}
