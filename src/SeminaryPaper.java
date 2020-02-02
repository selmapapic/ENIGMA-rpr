import java.time.LocalDate;

public class SeminaryPaper extends ScientificPaper {

    public SeminaryPaper(Author author, LocalDate releaseDate, String category, String title) {
        super(author, releaseDate, category, title);
    }

    public SeminaryPaper() {
    }
}
