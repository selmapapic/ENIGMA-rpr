import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

public class AllPapers {
    private PapersDAO dao = PapersDAO.getInstance();
    private ArrayList<ScientificPaper> papers = dao.getAllPapers();
    private ArrayList<String> categories = new ArrayList<>();

    private ObservableList<ScientificPaper> papersObservable = FXCollections.observableArrayList(papers);
    private SimpleObjectProperty<ScientificPaper> currentPaper = new SimpleObjectProperty<>();

//    public AllPapers(ArrayList<ScientificPaper> papers, ArrayList<String> categories) {
//        this.papers = papers;
//        this.categories = categories;
//    }

    public AllPapers() {
    }


    public ObservableList<ScientificPaper> getPapersObservable() {
        return papersObservable;
    }

    public void setPapersObservable(ObservableList<ScientificPaper> papersObservable) {
        this.papersObservable = papersObservable;
    }

    public ScientificPaper getCurrentPaper() {
        return currentPaper.get();
    }

    public SimpleObjectProperty<ScientificPaper> currentPaperProperty() {
        return currentPaper;
    }

    public void setCurrentPaper(ScientificPaper currentPaper) {
        this.currentPaper.set(currentPaper);
    }

//    public static AllPapers getInstance() {
//        if(instance == null) instance = new AllPapers();
//        return instance;
//    }

    public ArrayList<ScientificPaper> getPapers() {
        return dao.getAllPapers();
    }

    public void setPapers(ArrayList<ScientificPaper> papers) {
        this.papers = papers;
    }

    public void addPaper (ScientificPaper paper) {
        papers.add(paper);
    }

    public void removePaper (ScientificPaper paper) {
        String title = paper.getTitle();
        title.replaceAll("\\s+","");
        try {
            Files.deleteIfExists(Paths.get(title + ".txt"));
        } catch (IOException e) {
            System.out.println("Deletion failed.");
        }
    }

    public ArrayList<String> getCategories() {
        return categories;
    }

    public void setCategories(ArrayList<String> categories) {
        this.categories = categories;
    }
}
