import java.time.LocalDate;

public class MastersThesis extends ScientificPaper {

    public MastersThesis(int id, Author author, LocalDate releaseDate, String category, String title) {
        super(id, author, releaseDate, category, title);
    }

    public MastersThesis() {
    }

    @Override
    public String toString() {
        return "Master's Thesis: " + super.toString();
    }
}
