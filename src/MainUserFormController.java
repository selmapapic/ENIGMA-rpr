import com.sun.jdi.ClassType;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

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
    public ScientificPaper currentPaper, currentPaper1;
    public ChoiceBox<String> choiceType, choiceCategory;
    public DatePicker dpReleaseDate;
    private ArrayList<String> categoriesList;
    public ObservableList<String> categoriesObservable = FXCollections.observableArrayList(dao.getAllCategories());
    private boolean isAnchorFilterOn = false;

    private void initializeTableView () {
        choiceCategory.setItems(categoriesObservable);

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
        dpReleaseDate.setValue(LocalDate.now());
        //todo postaviti da se disableuje
//        dpReleaseDate.setDayCellFactory(picker -> new DateCell() {
//            public void updateItem(LocalDate date) {
//                LocalDate today = LocalDate.now();
//                setDisable(date.compareTo(today) > 0 );
//            }
//        });;

        tableViewPapers.getSelectionModel().selectedItemProperty().addListener((obs, oldIme, newIme) -> { //listener for current paper
            if(tableViewPapers.getSelectionModel().getSelectedItem() == null) currentPaper = null;
            else {
                currentPaper = tableViewPapers.getSelectionModel().getSelectedItem();
            }
        });

        tableViewPapers1.getSelectionModel().selectedItemProperty().addListener((obs, oldIme, newIme) -> {
            if(tableViewPapers1.getSelectionModel().getSelectedItem() == null) currentPaper1 = null;
            else {
                currentPaper1 = tableViewPapers1.getSelectionModel().getSelectedItem();
            }
        });
    }

    public void filterAction () {
        anchorFilterView.toFront();
        isAnchorFilterOn = true;
    }

    public String readFile (File file) throws IOException {
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

    private void openPdf(String pdf) throws IOException {
        Desktop.getDesktop().open(new File(pdf));
    }

    //copied from stack overflow
    public void viewAction () throws IOException {
        if(isAnchorFilterOn) {
            currentPaper = currentPaper1;
        }
        if(currentPaper == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning");
            alert.setHeaderText("You haven't selected any of the papers from the list!");
            alert.setContentText("Please choose one and then click 'View'.");

            alert.showAndWait();
            return;
        }
        PDDocument doc = null;
        try
        {
            doc = new PDDocument();
            PDPage page = new PDPage();
            doc.addPage(page);
            PDPageContentStream contentStream = new PDPageContentStream(doc, page);

            PDFont pdfFont = PDType1Font.TIMES_ROMAN;
            float fontSize = 12;
            float leading = 1.5f * fontSize;

            PDRectangle mediabox = page.getMediaBox();
            float margin = 72;
            float width = mediabox.getWidth() - 2*margin;
            float startX = mediabox.getLowerLeftX() + margin;
            float startY = mediabox.getUpperRightY() - margin;

            File file = new File("resources/files", currentPaper.getTitle() + ".txt");
            String text = readFile(file);
            List<String> lines = new ArrayList<>();

            for (String t : text.split("\n"))
            {
                int lastSpace = -1;
                while (text.length() > 0)
                {
                    int spaceIndex = text.indexOf(' ', lastSpace + 1);
                    if (spaceIndex < 0)
                        spaceIndex = text.length();
                    String subString = text.substring(0, spaceIndex);
                    float size = fontSize * pdfFont.getStringWidth(subString) / 1000;
                    if (size > width)
                    {
                        if (lastSpace < 0)
                            lastSpace = spaceIndex;
                        subString = text.substring(0, lastSpace);
                        lines.add(subString);
                        text = text.substring(lastSpace).trim();
                        lastSpace = -1;
                    }
                    else if (spaceIndex == text.length())
                    {
                        lines.add(text);
                        text = "";
                    }
                    else
                    {
                        lastSpace = spaceIndex;
                    }
                }
            }

            contentStream.setFont(PDType1Font.TIMES_ROMAN, 16);
            contentStream.beginText();
            contentStream.newLineAtOffset(75, 750);
            contentStream.showText(currentPaper.getTitle());
            contentStream.endText();

            contentStream.beginText();
            contentStream.setFont(pdfFont, fontSize);
            contentStream.newLineAtOffset(startX, startY);
            for (String line : lines)
            {
                contentStream.showText(line);
                contentStream.newLineAtOffset(0, 2 + -leading);
            }
            contentStream.endText();
            contentStream.close();

            doc.save("resources/pdfs/" + currentPaper.getTitle() + ".pdf");
            doc.close();

            openPdf("resources/pdfs/"  + currentPaper.getTitle() + ".pdf");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void editAccountAction () {

    }

    public void logOutAction () {
        Stage cureentStage = (Stage) tableViewPapers.getScene().getWindow();
        cureentStage.close();
    }

    public void aboutAction () {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("About ENIGMA");
        alert.setHeaderText(null);
        alert.setContentText("ENIGMA is a scientific paper management software. \n" +
                "It allows you to access the newest scientific papers. \n" +
                "ENIGMA is a university project made by Selma Celosmanovic. \n" +
                "Current version: 1.0.0");

        alert.showAndWait();
    }

    public void searchAction () {
        List<ScientificPaper> allPapers = dao.getAllPapers();
        String selectedCategory = choiceCategory.getSelectionModel().getSelectedItem();
        LocalDate selectedDate = dpReleaseDate.getValue();
        if(selectedCategory != null) {
            allPapers = allPapers.stream().filter(paper -> paper.getCategory().equals(selectedCategory)).collect(Collectors.toList());
        }
        if(selectedDate != null && selectedDate.compareTo(LocalDate.now()) <= 0) {
            allPapers = allPapers.stream().filter(paper -> paper.getReleaseDate().compareTo(selectedDate) == 0).collect(Collectors.toList());
        }
        ObservableList<ScientificPaper> obsList = FXCollections.observableArrayList(allPapers);
        papers.removeAll(dao.getAllPapers());
        papers.addAll(obsList);
    }
}
