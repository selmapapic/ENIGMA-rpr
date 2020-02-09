import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.davidmoten.text.utils.WordWrap;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Scanner;

public class UploadController {
    public TextField fldTitle, fldAuthorName, fldAuthorSurname, fldCategory;
    public ChoiceBox<String> choiceType;
    public DatePicker dpDateOfIssue;
    public TextArea areaText;
    //private AllPapers papers = AllPapers.getInstance();
    public ObservableList<String> type = FXCollections.observableArrayList();
    public PapersDAO dao = PapersDAO.getInstance();
    public ScientificPaper forEdit = null;
    private String oldTitle;

    public UploadController (ScientificPaper paper) {
        forEdit = paper;
        type.add("Bachelors Thesis");
        type.add("Doctorate");
        type.add("Masters Thesis");
        type.add("Scientific Article");
        type.add("Seminary Paper");
        type.add("Other");
    }

    public void initialize() {
        fillPlacesForEdit();
        if(forEdit != null) choiceType.setDisable(true); //onemogucavanje promjene tipa todo
        choiceType.setItems(type);
        choiceType.setValue("Other");
        dpDateOfIssue.setValue(LocalDate.now());
    }

    public String readFile (File file) {
        String paper = "";
        if(file == null) return "";
        try {
            Scanner scanner = new Scanner(file);
            scanner.nextLine();
            while (scanner.hasNextLine()) {
                paper += scanner.nextLine();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return paper;
    }

    public void writeFile (File file) {
        if(file == null) return;
        try {
            FileWriter writer = new FileWriter(file);
            String s = fldTitle.getText() + "\n" + areaText.getText();
            String wrapped = WordWrap.from(s).maxWidth(160).insertHyphens(true).wrap();
            writer.write(wrapped);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void cancelAction () {
        Stage stage = (Stage) fldTitle.getScene().getWindow();
        stage.close();
    }

    private void fillPlacesForEdit () {
        if(forEdit != null) {
            oldTitle = forEdit.getTitle();
            fldTitle.setText(forEdit.getTitle());
            fldAuthorName.setText(forEdit.getAuthor().getName());
            fldAuthorSurname.setText(forEdit.getAuthor().getSurname());
            fldCategory.setText(forEdit.getCategory());
            choiceType.getSelectionModel().select(forEdit.getClass().getName());
            dpDateOfIssue.setValue(forEdit.getReleaseDate());
            File file = new File("resources/files", forEdit.getTitle() + ".txt");
            areaText.setText(readFile(file));
        }
    }

    public void submitAction () throws IOException {

        //initializeForEdit();

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

            File file = new File("resources/files", fldTitle.getText() + ".txt"); //creating new file
            ScientificPaper paper = null;

            if(choiceType.getSelectionModel().getSelectedItem() == null || choiceType.getSelectionModel().getSelectedItem().equals("Other")) { //default value for type
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

            if(dpDateOfIssue.getValue() == null) { //default value for date
                assert paper != null;
                paper.setReleaseDate(LocalDate.now());
            }
            else {
                assert paper != null;
                paper.setReleaseDate(dpDateOfIssue.getValue());
            }
            if (forEdit != null) {
                forEdit.setTitle(fldTitle.getText());
                Author author = new Author(fldAuthorName.getText(), fldAuthorSurname.getText());
                forEdit.setAuthor(author);
                forEdit.setReleaseDate(dpDateOfIssue.getValue());
                forEdit.setCategory(fldCategory.getText());
                File file1 = new File("resources/files", oldTitle + ".txt"); //delete old file
                file1.delete();
                File file2 = new File("resources/files", fldTitle.getText() + ".txt");  //create new edited file
                writeFile(file2);
            }
            else {
                paper.setTitle(fldTitle.getText());
                Author author = new Author(fldAuthorName.getText(), fldAuthorSurname.getText());
                paper.setAuthor(author);
                paper.setCategory(fldCategory.getText());
                file.createNewFile();
                writeFile(file);
                dao.addPaper(paper);
            }
            Stage stage = (Stage) fldTitle.getScene().getWindow();
            stage.close();
        }
    }

    public ScientificPaper getForEdit() {
        return forEdit;
    }
}
