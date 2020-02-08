import java.time.LocalDate;

public class Other extends ScientificPaper {

    public Other(Author author, LocalDate releaseDate, String category, String title) {
        super(author, releaseDate, category, title);
    }

    public Other() {
    }

    @Override
    public String toString() {
        return "Other: " + super.toString();
    }
}
