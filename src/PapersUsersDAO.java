import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class PapersUsersDAO {
    private static PapersUsersDAO instance;
    private static Connection conn;

    private PreparedStatement usersQuery, insertUserQuery, getUserId, certainUserQuery, editUserQuery, deleteUserQuery,
            papersQuery, addPaperQuery, getPaperId, removePaperQuery, getAllCategories, getAllTypes, getAllAuthors/*, deleteAllUsersQuery*/;

    public static PapersUsersDAO getInstance() {
        if(instance == null) instance = new PapersUsersDAO();
        return instance;
    }

    public static void removeInstance () {
        if(instance != null) {
            try {
                instance.conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        instance = null;
    }

    private PapersUsersDAO() {
        try {
            conn = DriverManager.getConnection("jdbc:sqlite:scientificPapers.db");
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try {
            usersQuery = conn.prepareStatement("SELECT * from user");
        } catch (SQLException e) {
            regenerateBase();
            try {
                usersQuery = conn.prepareStatement("SELECT * from user");
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }

        try {
            //users
            insertUserQuery = conn.prepareStatement("INSERT INTO user VALUES (?,?,?,?,?,?)");
            getUserId = conn.prepareStatement("SELECT MAX (id)+1 FROM user");
            certainUserQuery = conn.prepareStatement("SELECT * FROM user WHERE email=? /*AND password=?*/");
            editUserQuery = conn.prepareStatement("UPDATE user SET name=?, surname=?, email=?, password=? WHERE id=?");
            deleteUserQuery = conn.prepareStatement("DELETE FROM user WHERE id=?");
            //papers
            addPaperQuery = conn.prepareStatement("INSERT INTO scientific_paper VALUES (?,?,?,?,?,?,?)");
            getPaperId = conn.prepareStatement("SELECT MAX (id)+1 FROM scientific_paper");
            removePaperQuery = conn.prepareStatement("DELETE FROM scientific_paper WHERE id=?");
            getAllCategories = conn.prepareStatement("SELECT DISTINCT category FROM scientific_paper");
            getAllTypes = conn.prepareStatement("SELECT DISTINCT type FROM scientific_paper");
            getAllAuthors = conn.prepareStatement("SELECT DISTINCT author_name, author_surname FROM scientific_paper");
            papersQuery = conn.prepareStatement("SELECT * FROM scientific_paper");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void close () {
        try {
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void regenerateBase() {
        Scanner in = null;
        try {
            in = new Scanner(new FileInputStream("scientificPapers.db.sql"));
            String sqlQuery = "";
            while(in.hasNext()) {
                sqlQuery += in.nextLine();
                if(sqlQuery.length() > 1 && sqlQuery.charAt(sqlQuery.length() - 1) == ';') {
                    try {
                        Statement stmt = conn.createStatement();
                        stmt.execute(sqlQuery);
                        sqlQuery = "";
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            }
            in.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
    //UsersDAO
    public void insertNewUser (User user) {
        try {
            ResultSet rs = getUserId.executeQuery();
            int id = 1;
            if(rs.next()) {
                id = rs.getInt(1);
            }
            if(id == 0) id = 1;
            insertUserQuery.setInt(1, id);
            insertUserQuery.setString(2, user.getName());
            insertUserQuery.setString(3, user.getSurname());
            insertUserQuery.setString(4, user.getMail());
            insertUserQuery.setString(5, user.getAcademicDegree());
            insertUserQuery.setString(6, user.getPassword());
            insertUserQuery.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public User getUser (String email) {
        User user = null;
        try {
            certainUserQuery.setString(1, email);
            //certainUserQuery.setString(2, password);
            ResultSet rs = certainUserQuery.executeQuery();

            if(rs.next()) {
                user = new User();
                user.setId(rs.getInt(1));
                user.setName(rs.getString(2));
                user.setSurname(rs.getString(3));
                user.setMail(rs.getString(4));
                user.setAcademicDegree(rs.getString(5));
                user.setPassword(rs.getString(6));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return user;
    }

    public static Connection getConn() {
        return conn;
    }

    public void editUser (User user) {
        try {
            editUserQuery.setString(1, user.getName());
            editUserQuery.setString(2, user.getSurname());
            editUserQuery.setString(3, user.getMail());
            editUserQuery.setString(4, user.getPassword());
            editUserQuery.setInt(5, user.getId());
            editUserQuery.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteUser (User user) {
        try {
            deleteUserQuery.setInt(1, user.getId());
            deleteUserQuery.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //PapersDAO
    private void addPaperHelp(ScientificPaper paper) throws SQLException {
        addPaperQuery.setString(2, paper.getTitle());
        addPaperQuery.setString(3, paper.getAuthor().getName());
        addPaperQuery.setString(4, paper.getAuthor().getSurname());
        addPaperQuery.setString(5, paper.getReleaseDate().toString());
        addPaperQuery.setString(6, paper.getCategory());
        addPaperQuery.setString(7, paper.getType().getName());
        addPaperQuery.executeUpdate();
    }

    public void addPaper (ScientificPaper paper) {
        try {
            ResultSet rs = getPaperId.executeQuery();
            int id = 1;
            if(rs.next()) {
                id = rs.getInt(1);
            }
            if(id == 0) id = 1;
            addPaperQuery.setInt(1, id);
            addPaperHelp(paper);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<ScientificPaper> getAllPapers () {
        ArrayList<ScientificPaper> papers = new ArrayList<>();
        try {
            ResultSet resultSet = papersQuery.executeQuery();
            while (resultSet.next()) {
                ScientificPaper paper = getPaperFromResultSet(resultSet);
                papers.add(paper);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return papers;
    }

    private ScientificPaper getPaperFromResultSet (ResultSet resultSet) throws SQLException {
        ScientificPaper paper = new ScientificPaper();
        switch (resultSet.getString(7)) {
            case "Other":
                paper.setType(PaperType.OTHER);
                break;
            case "Bachelor's Thesis":
                paper.setType(PaperType.BACHELORS_THESIS);
                break;
            case "Doctorate":
                paper.setType(PaperType.DOCTORATE);
                break;
            case "Master's Thesis":
                paper.setType(PaperType.MASTERS_THESIS);
                break;
            case "Scientific Article":
                paper.setType(PaperType.SCIENTIFIC_ARTICLE);
                break;
            case "Seminary Paper":
                paper.setType(PaperType.SEMINARY_PAPER);
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + resultSet.getString(7));
        }
        paper.setId(resultSet.getInt(1));
        paper.setTitle(resultSet.getString(2));
        Author author = new Author(resultSet.getString(3), resultSet.getString(4));
        paper.setAuthor(author);
        paper.setReleaseDate(LocalDate.parse(resultSet.getString(5)));
        paper.setCategory(resultSet.getString(6));
        return paper;
    }

    public void removePaper (ScientificPaper paper) {
        try {
            removePaperQuery.setInt(1, paper.getId());
            removePaperQuery.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public Connection getConnection () {
        return conn;
    }

    public void editPaper (ScientificPaper paper) {
        try {
            removePaperQuery.setInt(1, paper.getId());
            removePaperQuery.executeUpdate();
            addPaperQuery.setInt(1, paper.getId());
            addPaperHelp(paper);
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public List<String> getAllCategories () {
        try {
            ResultSet resultSet = getAllCategories.executeQuery();
            return getStringsFromResultSet(resultSet);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<String> getAllTypes () {
        try {
            ResultSet resultSet = getAllTypes.executeQuery();
            return getStringsFromResultSet(resultSet);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public ArrayList<String> getStringsFromResultSet (ResultSet resultSet) throws SQLException {
        ArrayList<String> strings = new ArrayList<>();
        while (resultSet.next()) {
            strings.add(resultSet.getString(1));
        }
        return strings;
    }

    public List<Author> getAllAuthors () {
        ArrayList<Author> authors = new ArrayList<>();
        try {
            ResultSet resultSet = getAllAuthors.executeQuery();
            while (resultSet.next()) {
                Author a = new Author(resultSet.getString(1), resultSet.getString(2));
                authors.add(a);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return authors;
    }
}
