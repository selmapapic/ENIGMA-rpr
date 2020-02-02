import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;

public class UploadController {
    public TextField fldTitle, fldAuthorName, fldAuthorSurname, fldCategory;
    public ChoiceBox<ScientificPaper> choiceType;
    public DatePicker dpDateOfIssue;
    public TextArea areaText;
    private AllPapers papers;

    public UploadController(AllPapers papers) {
        this.papers = papers;
    }

    public void zapisiDatoteku (File fajl) {
        if(fajl == null) return;
        try {
            FileWriter reader = new FileWriter(fajl);
            String s = fldTitle.getText() + "\n" + areaText.getText();
            reader.write(s);
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void cancelAction () {
        Stage stage = (Stage) fldTitle.getScene().getWindow();
        stage.close();
    }

    public void submitAction () {
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
            String title = fldTitle.getText();
            title.replaceAll("\\s+", "");
            File file = new File(title + ".txt");
            ScientificPaper paper;

            if(choiceType.getSelectionModel().getSelectedItem() == null) {
                paper = new Other();
                if(dpDateOfIssue.getValue() == null) {
                    paper.setReleaseDate(LocalDate.now());
                }
                else {
                    paper.setReleaseDate(dpDateOfIssue.getValue());
                }
                Author author = new Author(fldAuthorName.getText(), fldAuthorSurname.getText());
                paper.setAuthor(author);
                paper.setCategory(fldCategory.getText());
                papers.addPaper(paper);
            }
            else if(choiceType.getSelectionModel().getSelectedItem().equals("Bachelors Thesis")) {
                paper = new BachelorsThesis();
            }

        }
    }
}
