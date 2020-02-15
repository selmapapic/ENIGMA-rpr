import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Callback;
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
import java.util.List;
import java.util.*;
import java.util.stream.Collectors;

import static javafx.scene.control.PopupControl.USE_COMPUTED_SIZE;

public class MainUserFormController {
    private User currentUser;
    public Label labelGreeting, labelGreeting1, labelInitial, labelInitial1;
    public MainUserFormController(User user) {
        currentUser = user;
    }
    public AnchorPane anchorMainView, anchorFilterView;
    public TableView<ScientificPaper> tableViewPapers, tableViewPapers1;
    public TableColumn<ScientificPaper, String> colTitle, colTitle1, colCategory, colCategory1;
    public TableColumn<ScientificPaper, String> colType, colType1;
    public TableColumn<ScientificPaper, LocalDate> colReleaseDate, colReleaseDate1;
    public TableColumn<ScientificPaper, Author> colAuthor, colAuthor1;
    public TableColumn<ScientificPaper, Integer> colId, colId1;
    public PapersDAO dao = PapersDAO.getInstance();
    public ObservableList<ScientificPaper> papers = FXCollections.observableArrayList();
    public ScientificPaper currentPaper, currentPaper1;
    public ChoiceBox<String> choiceType, choiceCategory;
    public ChoiceBox<Author> choiceAuthor;
    public DatePicker dpReleaseDate;
    public ObservableList<String> categoriesObservable = FXCollections.observableArrayList(dao.getAllCategories());
    public ObservableList<String> typesObservable = FXCollections.observableArrayList(dao.getAllTypes());
    public ObservableList<Author> authorObservable = FXCollections.observableArrayList(dao.getAllAuthors());
    private boolean isAnchorFilterOn = false;

    private void initializeTableView () {
        if(Locale.getDefault().getCountry().equals("BS")) {
            Author a = new Author("Svi", "autori");
            categoriesObservable.add(0, "Sve kategorije");
            typesObservable.add(0, "Svi tipovi");
            authorObservable.add(0, a);
        }
        else {
            Author a = new Author("All", "authors");
            categoriesObservable.add(0, "All categories");
            typesObservable.add(0, "All types");
            authorObservable.add(0, a);
        }

        choiceCategory.setItems(categoriesObservable);
        choiceType.setItems(typesObservable);
        choiceAuthor.setItems(authorObservable);

        colTitle.setCellValueFactory(new PropertyValueFactory<ScientificPaper, String>("title"));
        colAuthor.setCellValueFactory(new PropertyValueFactory<ScientificPaper, Author>("author"));
        colReleaseDate.setCellValueFactory(new PropertyValueFactory<ScientificPaper, LocalDate>("releaseDate"));
        colCategory.setCellValueFactory(new PropertyValueFactory<ScientificPaper, String>("category"));
        colId.setCellValueFactory(new PropertyValueFactory<ScientificPaper, Integer>("id"));
        colType.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getType().getName()));

        colTitle1.setCellValueFactory(new PropertyValueFactory<ScientificPaper, String>("title"));
        colAuthor1.setCellValueFactory(new PropertyValueFactory<ScientificPaper, Author>("author"));
        colReleaseDate1.setCellValueFactory(new PropertyValueFactory<ScientificPaper, LocalDate>("releaseDate"));
        colCategory1.setCellValueFactory(new PropertyValueFactory<ScientificPaper, String>("category"));
        colId1.setCellValueFactory(new PropertyValueFactory<ScientificPaper, Integer>("id"));
        colType1.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getType().getName()));

    }

    private void defaultChoiceValues () {   //postavljamo cb-ove na default vrijednosti
        if(Locale.getDefault().getCountry().equals("BS")) {
            Author a = new Author("Svi", "autori");
            choiceAuthor.getSelectionModel().select(a);
            choiceCategory.getSelectionModel().select("Sve kategorije");
            choiceType.getSelectionModel().select("Svi tipovi");
        }
        else {
            Author a = new Author("All", "authors");
            choiceAuthor.getSelectionModel().select(a);
            choiceCategory.getSelectionModel().select("All categories");
            choiceType.getSelectionModel().select("All types");
        }
    }

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

    private String readFile (File file) throws IOException {
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

    private String removeIllegalCharacters (String s) {
        s = s.replace('č','c');
        s = s.replace('ž', 'z');
        s = s.replace('ć', 'c');
        s = s.replace('š', 's');
        s = s.replace("đ", "dj");
        return s;
    }

    @FXML
    public void initialize () {
        anchorMainView.toFront();
        if(Locale.getDefault().getCountry().equals("BS")) {
            labelGreeting.setText("Zdravo, " + currentUser.getName());
            labelGreeting1.setText("Zdravo, " + currentUser.getName());
        }
        else {
            labelGreeting.setText("Welcome back, " + currentUser.getName());
            labelGreeting1.setText("Welcome back, " + currentUser.getName());
        }

        labelInitial.setText(String.valueOf(currentUser.getName().charAt(0)));
        labelInitial1.setText(String.valueOf(currentUser.getName().charAt(0)));

        papers.addAll(dao.getAllPapers());
        tableViewPapers.setItems(papers);
        tableViewPapers1.setItems(papers);

        defaultChoiceValues();
        initializeTableView();

        dpReleaseDate.setDayCellFactory(disableFutureDates());

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

    //za pdf copied from stack overflow
    public void viewAction () throws IOException {
        if(isAnchorFilterOn) {
            currentPaper = currentPaper1;
        }
        try {
            if (currentPaper == null) {
                throw new NoPaperSelectedException("Nothing selected");
            }
        } catch (NoPaperSelectedException e) {
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
            text = removeIllegalCharacters(text); //uklanjam karaktere koji se ne mogu prikazati u pdf
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

//            new Thread(() -> {
//                try {
//                    Thread.sleep(4000);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//                Platform.runLater(() -> {
//                    ResourceBundle bundle = ResourceBundle.getBundle("translation");
//                    Stage stageMainWin = new Stage();
//                    //MainUserFormController controller = new MainUserFormController(user);
//                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/mainUserForm.fxml"), bundle);
//                    //loader.setController(controller);
//                    Parent root = null;
//                    try {
//                        root = loader.load();
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                    stageMainWin.setResizable(true);
//                    stageMainWin.setScene(new Scene(root, USE_COMPUTED_SIZE, USE_COMPUTED_SIZE));
//                    stageMainWin.setResizable(false);
//                    stageMainWin.show();
//                });
//
//            }).start();


            openPdf("resources/pdfs/"  + currentPaper.getTitle() + ".pdf");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void editAccountAction () throws IOException {
        ResourceBundle bundle = ResourceBundle.getBundle("translation");
        Stage stageEditAccount = new Stage();
        EditAccountController controller = new EditAccountController(currentUser, labelGreeting);
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/editAccount.fxml"), bundle);
        loader.setController(controller);
        Parent root = loader.load();
        if (Locale.getDefault().getCountry().equals("BS")) stageEditAccount.setTitle("Uredi moj račun");
        else stageEditAccount.setTitle("Edit my account");
        stageEditAccount.setScene(new Scene(root, USE_COMPUTED_SIZE, USE_COMPUTED_SIZE));
        stageEditAccount.setResizable(false);
        stageEditAccount.show();
    }

    public void logOutAction () {
        Stage cureentStage = (Stage) tableViewPapers.getScene().getWindow();
        cureentStage.close();
    }

    public void aboutAction () {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("About ENIGMA");
        alert.setHeaderText(null);
        if(Locale.getDefault().getCountry().equals("BS")) {
            alert.setContentText("ENIGMA je softver koji se bavi uredjenjem naucnih radova. \n" +
                    "Omogucava vam da pristupite najnovijim naucnim radovima \n" +
                    "ENIGMA je kreirana od strane Selme Celosmanovic kao fakultetski projekat. \n" +
                    "Trenutna verzija: 1.0.1");
        }
        else {
            alert.setContentText("ENIGMA is a scientific paper management software. \n" +
                    "It allows you to access the newest scientific papers. \n" +
                    "ENIGMA is a university project made by Selma Celosmanovic. \n" +
                    "Current version: 1.0.1");
        }
        alert.showAndWait();
    }

    public void searchAction () {
        List<ScientificPaper> allPapers = dao.getAllPapers();
        String selectedCategory = choiceCategory.getSelectionModel().getSelectedItem();
        LocalDate selectedDate = dpReleaseDate.getValue();
        String selectedType = choiceType.getSelectionModel().getSelectedItem();
        Author selectedAuthor = choiceAuthor.getSelectionModel().getSelectedItem();

        Author defaultA = new Author("All", "authors");
        Author defaultB = new Author("Svi", "autori");
        if(selectedDate != null && selectedDate.compareTo(LocalDate.now()) <= 0) {
            allPapers = allPapers.stream().filter(paper -> paper.getReleaseDate().compareTo(selectedDate) >= 0).collect(Collectors.toList());
        }
        if(selectedCategory != null && !selectedCategory.equals("All categories") && !selectedCategory.equals("Sve kategorije")) {
            allPapers = allPapers.stream().filter(paper -> paper.getCategory().equals(selectedCategory)).collect(Collectors.toList());
        }
        if(selectedType != null && !selectedType.equals("All types") && !selectedType.equals("Svi tipovi")) {
            allPapers = allPapers.stream().filter(paper -> paper.getType().getName().equals(selectedType)).collect(Collectors.toList());
        }
        if(selectedAuthor != null && !selectedAuthor.equals(defaultA) && !selectedAuthor.equals(defaultB)) {
            allPapers = allPapers.stream().filter(paper -> paper.getAuthor().equals(selectedAuthor)).collect(Collectors.toList());
        }
        ObservableList<ScientificPaper> obsList = FXCollections.observableArrayList(allPapers);
        papers.removeAll(dao.getAllPapers());
        papers.addAll(obsList);
    }

    public void resetFiltersAction () {
        papers.removeAll(dao.getAllPapers());
        papers.addAll(dao.getAllPapers());
        defaultChoiceValues();
    }
}
