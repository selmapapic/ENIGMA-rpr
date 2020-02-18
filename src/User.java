public class User extends Person {
    private String name, surname, mail, academicDegree;
    private int id;

    public User(int id, String name, String surname, String mail, String academicDegree, String password) {
        super(password);
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.mail = mail;
        this.academicDegree = academicDegree;
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

    public String getAcademicDegree() {
        return academicDegree;
    }

    public void setAcademicDegree(String academicDegree) {
        this.academicDegree = academicDegree;
    }

    @Override
    public String toString() {
        return name + " " + surname + ", " + mail + ", " + academicDegree;
    }
}
