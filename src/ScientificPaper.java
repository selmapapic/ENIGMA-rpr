import java.time.LocalDate;

public abstract class ScientificPaper {
    private Author author;
    private LocalDate releaseDate;
    private String category;
    private String title;

    public ScientificPaper(Author author, LocalDate releaseDate, String category, String title) {
        this.author = author;
        this.releaseDate = releaseDate;
        this.category = category;
        this.title = title;
    }

    public ScientificPaper() {
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

    @Override
    public String toString() {
        return title + ", " + author.getName() + " " + author.getSurname() + ", " + releaseDate + ", " + category;
    }
}
