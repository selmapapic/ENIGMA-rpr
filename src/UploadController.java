import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.util.Callback;
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
    public ObservableList<String> type = FXCollections.observableArrayList();
    public PapersDAO dao = PapersDAO.getInstance();
    public ScientificPaper forEdit;
    private String oldTitle;

    private Callback<DatePicker, DateCell> disableFutureDates() {
        return new Callback<>() {
            public DateCell call(final DatePicker datePicker) {
                return new DateCell() {
                    @Override
                    public void updateItem(LocalDate item, boolean empty) {
                        super.updateItem(item, empty);

                        // Disable all future date cells
                        if (item.isAfter(LocalDate.now())) {
                            this.setDisable(true);
                        }
                    }
                };
            }
        };
    }

    private String readFile (File file) {
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

    private void writeFile (File file) {
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

    private void validateField (TextField field) {
        field.getStyleClass().removeAll("invalidField");
        field.getStyleClass().add("validField");
    }

    private void invalidateField (TextField field) {
        field.getStyleClass().removeAll("validField");
        field.getStyleClass().add("invalidField");
    }

    public UploadController (ScientificPaper paper) {
        forEdit = paper;
        type.add("Bachelor's Thesis");
        type.add("Doctorate");
        type.add("Master's Thesis");
        type.add("Scientific Article");
        type.add("Seminary Paper");
        type.add("Other");
    }

    @FXML
    public void initialize() {
        fillPlacesForEdit();
        choiceType.setItems(type);
        choiceType.setValue("Other");
        dpDateOfIssue.setValue(LocalDate.now());
        dpDateOfIssue.setDayCellFactory(disableFutureDates());
    }

    public void cancelAction () {
        Stage stage = (Stage) fldTitle.getScene().getWindow();
        stage.close();
    }

    public void submitAction () throws IOException {
        if(fldTitle.getText().isEmpty() || fldAuthorName.getText().isEmpty() || fldAuthorSurname.getText().isEmpty()) {
            if (fldTitle.getText().isEmpty()) invalidateField(fldTitle);
            else validateField(fldTitle);

            if (fldAuthorName.getText().isEmpty()) invalidateField(fldAuthorName);
            else validateField(fldAuthorName);

            if (fldAuthorSurname.getText().isEmpty()) invalidateField(fldAuthorSurname);
            else validateField(fldAuthorSurname);
        }
        else {

            File file = new File("resources/files", fldTitle.getText() + ".txt"); //creating new file
            ScientificPaper paper = new ScientificPaper();

            if(choiceType.getSelectionModel().getSelectedItem() == null || choiceType.getSelectionModel().getSelectedItem().equals("Other")) { //default value for type
                paper.setType(PaperType.OTHER);
            }
            else if(choiceType.getSelectionModel().getSelectedItem().equals("Bachelor's Thesis")) {
                paper.setType(PaperType.BACHELORS_THESIS);
            }
            else if (choiceType.getSelectionModel().getSelectedItem().equals("Doctorate")) {
                paper.setType(PaperType.DOCTORATE);
            }
            else if(choiceType.getSelectionModel().getSelectedItem().equals("Master's Thesis")) {
                paper.setType(PaperType.MASTERS_THESIS);
            }
            else if(choiceType.getSelectionModel().getSelectedItem().equals("Scientific Article")) {
                paper.setType(PaperType.SCIENTIFIC_ARTICLE);
            }
            else if(choiceType.getSelectionModel().getSelectedItem().equals("Seminary Paper")) {
                paper.setType(PaperType.SEMINARY_PAPER);
            }

            if(dpDateOfIssue.getValue() == null) { //default value for date
                paper.setReleaseDate(LocalDate.now());
            }
            else {
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
                if(fldCategory.getText().isEmpty()) {
                    paper.setCategory("Unknown");
                }
                else {
                    paper.setCategory(fldCategory.getText());
                }
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
