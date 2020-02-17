import org.junit.jupiter.api.Test;

import java.io.File;
import java.time.LocalDate;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PapersDAOTest {
    @Test
    public void addPaper () {
        File dbfile = new File("scientificPapers.db");
        dbfile.delete();
        PapersDAO.removeInstance();
        PapersDAO dao = PapersDAO.getInstance();

        Author a = new Author("Selma", "Celosmanovic");
        ScientificPaper paper = new ScientificPaper(a, LocalDate.now(), "Architecture", "Arhitektura bla bla", 9, PaperType.DOCTORATE);

        dao.addPaper(paper);

        ArrayList<ScientificPaper> papers = dao.getAllPapers();
        assertEquals(papers.size(), 9);
        assertEquals(papers.get(8).getCategory(), "Architecture");
    }

    @Test
    public void removePaper () {
        File dbfile = new File("scientificPapers.db");
        dbfile.delete();
        PapersDAO.removeInstance();
        PapersDAO dao = PapersDAO.getInstance();

        Author a = new Author("Selma", "Celosmanovic");
        ScientificPaper paper = new ScientificPaper(a, LocalDate.now(), "Architecture", "Arhitektura bla bla", 9, PaperType.DOCTORATE);

        dao.addPaper(paper);

        ArrayList<ScientificPaper> papers = dao.getAllPapers();
        assertEquals(papers.size(), 9);

        dao.removePaper(paper);
        papers = dao.getAllPapers();
        assertEquals(papers.size(), 8);

        dao.removePaper(papers.get(1));
        papers = dao.getAllPapers();
        assertEquals(papers.get(1).getTitle(), "Fibonacci");
    }

    @Test
    public void editPaper() {
        File dbfile = new File("scientificPapers.db");
        dbfile.delete();
        PapersDAO.removeInstance();
        PapersDAO dao = PapersDAO.getInstance();

        ArrayList<ScientificPaper> papers = dao.getAllPapers();
        ScientificPaper paper = papers.get(4);
        paper.setTitle("Programming in C");
        paper.setCategory("Programiranje");
        dao.editPaper(paper);

        papers = dao.getAllPapers();

        assertEquals(papers.get(4).getTitle(), "Programming in C");
        assertEquals(papers.get(4).getCategory(), "Programiranje");
    }

    @Test
    public void mix () {
        File dbfile = new File("scientificPapers.db");
        dbfile.delete();
        PapersDAO.removeInstance();
        PapersDAO dao = PapersDAO.getInstance();

        Author a = new Author("Selma", "Celosmanovic");
        ScientificPaper paper = new ScientificPaper(a, LocalDate.now(), "Architecture", "Arhitektura bla bla", 9, PaperType.MASTERS_THESIS);

        dao.addPaper(paper);

        ArrayList<ScientificPaper> papers = dao.getAllPapers();
        assertEquals(papers.size(), 9);

        dao.removePaper(papers.get(0));
        dao.addPaper(new ScientificPaper(new Author("Kanita", "Djokic"), LocalDate.now(), "Cooking", "Nesto nesto", 1, PaperType.BACHELORS_THESIS));

        assertEquals(dao.getAllPapers().get(8).getAuthor().getName(), "Kanita");
        assertEquals(dao.getAllPapers().get(8).getId(), 10);
    }
}
