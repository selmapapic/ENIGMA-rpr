import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Scanner;

public class PapersDAO {
    private static PapersDAO instance;
    private Connection conn;

    private PreparedStatement papersQuery, addPaperQuery, getPaperId;

    public static PapersDAO getInstance() {
        if(instance == null) instance = new PapersDAO();
        return instance;
    }

    private PapersDAO() {
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
        } catch (SQLException e) {
            e.printStackTrace();
        }
        //insertUserQuery = conn.prepareStatement("INSERT INTO user VALUES (?,?,?,?,?,?)");
        //certainUserQuery = conn.prepareStatement("SELECT * FROM user WHERE email=? AND password=?");
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

    public void addPaper (ScientificPaper paper) {
        try {
            ResultSet rs = getPaperId.executeQuery();
            int id = 1;
            if(rs.next()) {
                id = rs.getInt(1);
            }
            if(id == 0) id = 1;
            addPaperQuery.setInt(1, id);
            addPaperQuery.setString(2, paper.getTitle());
            addPaperQuery.setString(3, paper.getAuthor().getName());
            addPaperQuery.setString(4, paper.getAuthor().getSurname());
            addPaperQuery.setString(5, paper.getReleaseDate().toString());
            addPaperQuery.setString(6, paper.getCategory());

            if(paper instanceof Other) {
                addPaperQuery.setString(7, "Other");
            }
            else if (paper instanceof BachelorsThesis) {
                addPaperQuery.setString(7, "BachelorsThesis");
            }
            else if (paper instanceof Doctorate) {
                addPaperQuery.setString(7, "Doctorate");
            }
            else if (paper instanceof MastersThesis) {
                addPaperQuery.setString(7, "MastersThesis");
            }
            else if (paper instanceof ScientificArticle) {
                addPaperQuery.setString(7, "ScientificArticle");
            }
            else if (paper instanceof SeminaryPaper) {
                addPaperQuery.setString(7, "SeminaryPaper");
            }
            addPaperQuery.executeUpdate();
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
        ScientificPaper paper;
        switch (resultSet.getString(7)) {
            case "Other":
                paper = new Other();
                break;
            case "BachelorsThesis":
                paper = new BachelorsThesis();
                break;
            case "Doctorate":
                paper = new Doctorate();
                break;
            case "MastersThesis":
                paper = new MastersThesis();
                break;
            case "ScientificArticle":
                paper = new ScientificArticle();
                break;
            case "SeminaryPaper":
                paper = new SeminaryPaper();
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + resultSet.getString(7));
        }

        paper.setTitle(resultSet.getString(2));
        Author author = new Author(resultSet.getString(3), resultSet.getString(4));
        paper.setAuthor(author);
        paper.setReleaseDate(LocalDate.parse(resultSet.getString(5)));
        paper.setCategory(resultSet.getString(6));
        return paper;
    }
}
