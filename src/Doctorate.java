import java.time.LocalDate;

public class Doctorate extends ScientificPaper {

    public Doctorate(Author author, LocalDate releaseDate, String category, String title) {
        super(author, releaseDate, category, title);
    }

    public Doctorate() {
    }
}
