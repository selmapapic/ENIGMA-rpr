import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.davidmoten.text.utils.WordWrap;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;

public class UploadController {
    public TextField fldTitle, fldAuthorName, fldAuthorSurname, fldCategory;
    public ChoiceBox<String> choiceType;
    public DatePicker dpDateOfIssue;
    public TextArea areaText;
    private AllPapers papers = AllPapers.getInstance();
    public ObservableList<String> type = FXCollections.observableArrayList();
    public PapersDAO dao = PapersDAO.getInstance();

    public UploadController () {
        type.add("Bachelors Thesis");
        type.add("Doctorate");
        type.add("Masters Thesis");
        type.add("Scientific Article");
        type.add("Seminary Paper");
        type.add("Other");
    }

    public void initialize() {
        choiceType.setItems(type);
        choiceType.setValue("Other");
        dpDateOfIssue.setValue(LocalDate.now());
    }

    public void writeFile (File fajl) {
        if(fajl == null) return;
        try {
            FileWriter reader = new FileWriter(fajl);
            String s = fldTitle.getText() + "\n" + areaText.getText();
            String wrapped = WordWrap.from(s).maxWidth(160).insertHyphens(true).wrap();
            reader.write(wrapped);
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void cancelAction () {
        Stage stage = (Stage) fldTitle.getScene().getWindow();
        stage.close();
    }

    public void submitAction () throws IOException {
        if(fldTitle.getText().isEmpty() || fldAuthorName.getText().isEmpty() || fldAuthorSurname.getText().isEmpty()) {
            if (fldTitle.getText().isEmpty()) {
                fldTitle.getStyleClass().removeAll("validField");
                fldTitle.getStyleClass().add("invalidField");
            } else {
                fldTitle.getStyleClass().removeAll("invalidField");
                fldTitle.getStyleClass().add("validField");
            }

            if (fldAuthorName.getText().isEmpty()) {
                fldAuthorName.getStyleClass().removeAll("validField");
                fldAuthorName.getStyleClass().add("invalidField");
            } else {
                fldAuthorName.getStyleClass().removeAll("invalidField");
                fldAuthorName.getStyleClass().add("validField");
            }

            if (fldAuthorSurname.getText().isEmpty()) {
                fldAuthorSurname.getStyleClass().removeAll("validField");
                fldAuthorSurname.getStyleClass().add("invalidField");
            } else {
                fldAuthorSurname.getStyleClass().removeAll("invalidField");
                fldAuthorSurname.getStyleClass().add("validField");
            }
        }
        else {

            //todo dodati da pozelene mjesta
            String title = fldTitle.getText();
            title.replace(" ", "");
            File file = new File("resources/files", title + ".txt");
            ScientificPaper paper = null;

            if(choiceType.getSelectionModel().getSelectedItem() == null || choiceType.getSelectionModel().getSelectedItem().equals("Other")) {
                paper = new Other();
            }
            else if(choiceType.getSelectionModel().getSelectedItem().equals("Bachelors Thesis")) {
                paper = new BachelorsThesis();
            }
            else if (choiceType.getSelectionModel().getSelectedItem().equals("Doctorate")) {
                paper = new Doctorate();
            }
            else if(choiceType.getSelectionModel().getSelectedItem().equals("Masters Thesis")) {
                paper = new MastersThesis();
            }
            else if(choiceType.getSelectionModel().getSelectedItem().equals("Scientific Article")) {
                paper = new ScientificArticle();
            }
            else if(choiceType.getSelectionModel().getSelectedItem().equals("Seminary Paper")) {
                paper = new SeminaryPaper();
            }

            if(dpDateOfIssue.getValue() == null) {
                assert paper != null;
                paper.setReleaseDate(LocalDate.now());
            }
            else {
                assert paper != null;
                System.out.println(dpDateOfIssue.getValue());
                System.out.println(paper);
                paper.setReleaseDate(dpDateOfIssue.getValue());
            }
            paper.setTitle(fldTitle.getText());
            Author author = new Author(fldAuthorName.getText(), fldAuthorSurname.getText());
            paper.setAuthor(author);
            paper.setCategory(fldCategory.getText());
            file.createNewFile();
            writeFile(file);
            //papers.addPaper(paper);
            dao.addPaper(paper);
            Stage stage = (Stage) fldTitle.getScene().getWindow();
            stage.close();
        }
    }
}
