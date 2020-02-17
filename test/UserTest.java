import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class UserTest {
    @Test
    public void constructor () {
        Person user = new User();
        assertEquals(user.getClass(), User.class);
    }

    @Test
    public void getterAndSetter () {
        User user = new User(1, "Selma", "Celosmanovic", "scelosmano1@etf.unsa.ba", "Bachelor", "selma");
        assertEquals(user.getId(),1);
        assertEquals(user.getName(),"Selma");
        user.setName("Lejla");
        assertEquals(user.getName(), "Lejla");

        assertEquals(user.getSurname(), "Celosmanovic");
        user.setSurname("Djokic");
        assertEquals(user.getSurname(), "Djokic");

        user.setId(7);
        assertEquals(user.getId(), 7);

        assertEquals(user.getMail(), "scelosmano1@etf.unsa.ba");
        assertEquals(user.getDegOfEducation(), "Bachelor");

        user.setDegOfEducation("Master");
        assertEquals(user.getDegOfEducation(), "Master");

        user.setMail("blabla@gmail.com");
        assertEquals(user.getMail(), "blabla@gmail.com");
    }

    @Test
    public void toStringTest() {
        User user = new User(1, "Selma", "Celosmanovic", "scelosmano1@etf.unsa.ba", "Bachelor", "selma");
        String s = "Selma Celosmanovic, scelosmano1@etf.unsa.ba, Bachelor";
        assertEquals(user.toString(), s);
    }
}
