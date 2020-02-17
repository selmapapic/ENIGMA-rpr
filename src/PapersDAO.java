import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class PapersDAO {
    private static PapersDAO instance;
    private Connection conn; //uzimanje konekcije iz UsersDAO

    private PreparedStatement papersQuery, addPaperQuery, getPaperId, removePaperQuery, getAllCategories, getAllTypes, getAllAuthors;

    public static PapersDAO getInstance() {
        if(instance == null) instance = new PapersDAO();
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

    public PapersDAO() {
        try {
            conn = DriverManager.getConnection("jdbc:sqlite:scientificPapers.db");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            papersQuery = conn.prepareStatement("SELECT * from scientific_paper");
        } catch (SQLException e) {
            regenerateBase();
            try {
                papersQuery = conn.prepareStatement("SELECT * from scientific_paper");
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }

        try {
            addPaperQuery = conn.prepareStatement("INSERT INTO scientific_paper VALUES (?,?,?,?,?,?,?)");
            getPaperId = conn.prepareStatement("SELECT MAX (id)+1 FROM scientific_paper");
            removePaperQuery = conn.prepareStatement("DELETE FROM scientific_paper WHERE id=?");
            getAllCategories = conn.prepareStatement("SELECT DISTINCT category FROM scientific_paper");
            getAllTypes = conn.prepareStatement("SELECT DISTINCT type FROM scientific_paper");
            getAllAuthors = conn.prepareStatement("SELECT DISTINCT author_name, author_surname FROM scientific_paper");
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
            return getStringsFromResultSet(resultSet);//.stream().distinct().collect(Collectors.toList()); //removing duplicates
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<String> getAllTypes () {
        try {
            ResultSet resultSet = getAllTypes.executeQuery();
            return getStringsFromResultSet(resultSet);//.stream().distinct().collect(Collectors.toList());
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
        return authors;//.stream().distinct().collect(Collectors.toList());
    }
}
