import java.time.LocalDate;

public class ScientificArticle extends ScientificPaper {
    
    public ScientificArticle(Author author, LocalDate releaseDate, String category, String title) {
        super(author, releaseDate, category, title);
    }

    public ScientificArticle() {
    }

    @Override
    public String toString() {
        return "ScientificArticle: " + super.toString();
    }
}
