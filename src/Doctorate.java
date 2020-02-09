import java.time.LocalDate;

public class Doctorate extends ScientificPaper {

    public Doctorate(int id, Author author, LocalDate releaseDate, String category, String title) {
        super(id, author, releaseDate, category, title);
    }

    public Doctorate() {
    }

    @Override
    public String toString() {
        return "Doctorate: " + super.toString();
    }
}
