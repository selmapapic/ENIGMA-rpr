package sample;

import java.util.Date;

public abstract class ScientificPaper {
    private Author author;
    private Date releaseDate;
    private String category;
    private String title;

    public ScientificPaper(Author author, Date releaseDate, String category, String title) {
        this.author = author;
        this.releaseDate = releaseDate;
        this.category = category;
        this.title = title;
    }

    public Author getAuthor() {
        return author;
    }

    public void setAuthor(Author author) {
        this.author = author;
    }

    public Date getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(Date releaseDate) {
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
}
