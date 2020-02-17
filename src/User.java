public class User extends Person {
    private String name, surname, mail, degOfEducation;
    private int id;

    public User(int id, String name, String surname, String mail, String degOfEducation, String password) {
        super(password);
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.mail = mail;
        this.degOfEducation = degOfEducation;
    }

    public User() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getDegOfEducation() {
        return degOfEducation;
    }

    public void setDegOfEducation(String degOfEducation) {
        this.degOfEducation = degOfEducation;
    }

    @Override
    public String toString() {
        return name + " " + surname + ", " + mail + ", " + degOfEducation;
    }
}
