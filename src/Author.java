public class Author extends Person {
    private String name, surname;

    public Author(String password, String name, String surname) {
        super(password);
        this.name = name;
        this.surname = surname;
    }

    public Author(String name, String surname) {
        super();
        this.name = name;
        this.surname = surname;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    @Override
    public String toString() {
        return name + " " + surname;
    }
}
