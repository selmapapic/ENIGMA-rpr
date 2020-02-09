import java.time.LocalDate;

public class SeminaryPaper extends ScientificPaper {

    public SeminaryPaper(int id, Author author, LocalDate releaseDate, String category, String title) {
        super(id, author, releaseDate, category, title);
    }

    public SeminaryPaper() {
    }

    @Override
    public String toString() {
        return "Seminary paper: " + super.toString();
    }
}
