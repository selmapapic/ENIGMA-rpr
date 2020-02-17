import org.junit.jupiter.api.Test;

import java.io.File;
import java.time.LocalDate;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class PapersUsersDAOTest {
    @Test
    public void addUser () {
        File dbfile = new File("scientificPapers.db");
        dbfile.delete();
        PapersUsersDAO.removeInstance();
        PapersUsersDAO dao = PapersUsersDAO.getInstance();

        dao.insertNewUser(new User(1, "Selma", "Celosmanovic", "scelosmano1@etf.unsa.ba", "Bachelor", "sel"));

        assertEquals(dao.getUser("scelosmano1@etf.unsa.ba").getSurname(), "Celosmanovic");
    }

    @Test
    public void noUser () {
        File dbfile = new File("scientificPapers.db");
        dbfile.delete();
        PapersUsersDAO.removeInstance();
        PapersUsersDAO dao = PapersUsersDAO.getInstance();

        dao.insertNewUser(new User(1, "Selma", "Celosmanovic", "scelosmano1@etf.unsa.ba", "Bachelor", "sel"));

        assertNull(dao.getUser("kdokic1@etf.unsa.ba"));
    }

    @Test
    public void editUser () {
        File dbfile = new File("scientificPapers.db");
        dbfile.delete();
        PapersUsersDAO.removeInstance();
        PapersUsersDAO dao = PapersUsersDAO.getInstance();

        dao.insertNewUser(new User(1, "Selma", "Celosmanovic", "scelosmano1@etf.unsa.ba", "Bachelor", "sel"));

        User user = dao.getUser("scelosmano1@etf.unsa.ba");
        user.setName("Lejla");

        dao.editUser(user);

        assertEquals(dao.getUser("scelosmano1@etf.unsa.ba").getName(), "Lejla");
    }

    @Test
    public void removeUser () {
        File dbfile = new File("scientificPapers.db");
        dbfile.delete();
        PapersUsersDAO.removeInstance();
        PapersUsersDAO dao = PapersUsersDAO.getInstance();
        User user = new User(1, "Selma", "Celosmanovic", "scelosmano1@etf.unsa.ba", "Bachelor", "sel");
        dao.insertNewUser(user);

        assertNotNull(dao.getUser("scelosmano1@etf.unsa.ba"));

        dao.deleteUser(user);

        assertNull(dao.getUser("scelosmano1@etf.unsa.ba"));
    }

    @Test
    public void addPaper () {
        File dbfile = new File("scientificPapers.db");
        dbfile.delete();
        PapersUsersDAO.removeInstance();
        PapersUsersDAO dao = PapersUsersDAO.getInstance();

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
        PapersUsersDAO.removeInstance();
        PapersUsersDAO dao = PapersUsersDAO.getInstance();

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
        PapersUsersDAO.removeInstance();
        PapersUsersDAO dao = PapersUsersDAO.getInstance();

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
        PapersUsersDAO.removeInstance();
        PapersUsersDAO dao = PapersUsersDAO.getInstance();

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
