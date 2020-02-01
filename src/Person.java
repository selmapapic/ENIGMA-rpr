public abstract class Person {
    private String password; //password je jedini zajednicki atribut administratora i korisnika

    public Person(String password) {
        this.password = password;
    }

    public Person() {

    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
