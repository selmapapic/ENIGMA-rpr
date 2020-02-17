import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AdministratorTest {
    @Test
    public void constructorAndInheritance () {
        Person p = new Administrator("admin");
        assertEquals(p.getClass(), Administrator.class);
    }

    @Test
    public void getterAndSetter () {
        Person p = new Administrator("admin");
        assertEquals(p.getPassword(), "admin");
        p.setPassword("admin123");
        assertEquals(p.getPassword(), "admin123");
    }
}
