import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

public class AllPapers {
    private ArrayList<ScientificPaper> papers = new ArrayList<>();
    private ArrayList<String> categories = new ArrayList<>();

    public AllPapers(ArrayList<ScientificPaper> papers, ArrayList<String> categories) {
        this.papers = papers;
        this.categories = categories;
    }

    public AllPapers() {
    }

    public ArrayList<ScientificPaper> getPapers() {
        return papers;
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
