import java.time.LocalDate;
import java.util.Objects;

public class ScientificPaper {
    private Author author;
    private LocalDate releaseDate;
    private String category;
    private String title;
    private int id;
    private PaperType type;

    public ScientificPaper(Author author, LocalDate releaseDate, String category, String title, int id, PaperType type) {
        this.author = author;
        this.releaseDate = releaseDate;
        this.category = category;
        this.title = title;
        this.id = id;
        this.type = type;
    }

    public ScientificPaper() { }

    public PaperType getType() {
        return type;
    }

    public void setType(PaperType type) {
        this.type = type;
    }

    public Author getAuthor() {
        return author;
    }

    public void setAuthor(Author author) {
        this.author = author;
    }

    public LocalDate getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(LocalDate releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return type.getName() + ": " + id + ", " + title + ", " + author.getName() + " " + author.getSurname() + ", " + releaseDate + ", " + category;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ScientificPaper paper = (ScientificPaper) o;
        return id == paper.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
