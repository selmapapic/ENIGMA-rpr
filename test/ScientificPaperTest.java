import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

public class ScientificPaperTest {
    @Test
    public void constructor () {
        Author a = new Author("Neko", "Nekic");
        ScientificPaper paper = new ScientificPaper();
        assertNull(paper.getAuthor());

        paper.setAuthor(a);
        assertNotNull(paper.getAuthor());

        ScientificPaper paper1 = new ScientificPaper(new Author("Bla", "Blic"), LocalDate.now(), "Nesto", "Naslov", 1, PaperType.DOCTORATE);
        assertEquals(paper1.getAuthor().getName(), "Bla");
    }

    @Test
    public void getterAndSetter () {
        ScientificPaper paper1 = new ScientificPaper(new Author("Bla", "Blic"), LocalDate.now(), "Nesto", "Naslov", 1, PaperType.DOCTORATE);

        assertEquals(paper1.getReleaseDate(), LocalDate.now());
        assertEquals(paper1.getCategory(), "Nesto");

        paper1.setCategory("Nesto drugo");
        assertEquals(paper1.getCategory(), "Nesto drugo");

        paper1.setReleaseDate(LocalDate.of(2020, 3, 17));
        assertEquals(paper1.getReleaseDate(), LocalDate.of(2020,3,17));

        paper1.setType(PaperType.OTHER);
        assertEquals(paper1.getType().getName(), "Other");

        paper1.setTitle("Title");
        assertEquals(paper1.getTitle(), "Title");

        paper1.setId(5);
        assertEquals(5, paper1.getId());
    }

    @Test
    public void toStringTest () {
        ScientificPaper paper1 = new ScientificPaper(new Author("Bla", "Blic"), LocalDate.now(), "Nesto", "Naslov", 1, PaperType.DOCTORATE);
        assertEquals(paper1.toString(), "Doctorate: 1, Naslov, Bla Blic, 2020-02-17, Nesto");
    }
}
