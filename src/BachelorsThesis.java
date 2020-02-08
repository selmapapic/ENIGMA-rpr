import java.time.LocalDate;

public class BachelorsThesis extends ScientificPaper {

    public BachelorsThesis(Author author, LocalDate releaseDate, String category, String title) {
        super(author, releaseDate, category, title);
    }

    public BachelorsThesis() {
    }

    @Override
    public String toString() {
        return "BachelorsThesis: " + super.toString();
    }
}
