import java.time.LocalDate;

public class ScientificArticle extends ScientificPaper {
    
    public ScientificArticle(int id, Author author, LocalDate releaseDate, String category, String title) {
        super(id, author, releaseDate, category, title);
    }

    public ScientificArticle() {
    }

    @Override
    public String toString() {
        return "Scientific article: " + super.toString();
    }
}
