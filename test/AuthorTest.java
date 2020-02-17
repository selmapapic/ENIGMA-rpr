import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AuthorTest {
    @Test
    public void constructor () {
        Person p = new Author("Selma", "Celosmanovic");
        assertEquals(p.getClass(), Author.class);
    }

    @Test
    public void getterAndSetter () {
        Author a = new Author("Selma", "Celosmanovic");
        assertEquals(a.getName(), "Selma");
        assertEquals(a.getSurname(), "Celosmanovic");
        a.setName("Neko");
        a.setSurname("Nekic");
        assertEquals("Neko Nekic", a.toString());
    }
}
