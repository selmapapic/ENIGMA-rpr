import java.time.LocalDate;

public class BachelorsThesis extends ScientificPaper {

    public BachelorsThesis(int id, Author author, LocalDate releaseDate, String category, String title) {
        super(id, author, releaseDate, category, title);
    }

    public BachelorsThesis() {
    }

    @Override
    public String toString() {
        return "Bachelor's Thesis: " + super.toString();
    }
}
