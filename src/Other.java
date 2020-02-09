import java.time.LocalDate;

public class Other extends ScientificPaper {

    public Other(int id, Author author, LocalDate releaseDate, String category, String title) {
        super(id, author, releaseDate, category, title);
    }

    public Other() {
    }

    @Override
    public String toString() {
        return "Other: " + super.toString();
    }
}
