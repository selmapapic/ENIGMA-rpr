import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.sql.*;
import java.util.Scanner;

public class UsersDAO {
    private static UsersDAO instance;
    private static Connection conn;

    private PreparedStatement usersQuery, insertUserQuery, getUserId, certainUserQuery, editUserQuery, deleteUserQuery;

    public static UsersDAO getInstance() {
        if(instance == null) instance = new UsersDAO();
        return instance;
    }

    private UsersDAO() {
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
            insertUserQuery = conn.prepareStatement("INSERT INTO user VALUES (?,?,?,?,?,?)");
            getUserId = conn.prepareStatement("SELECT MAX (id)+1 FROM user");
            certainUserQuery = conn.prepareStatement("SELECT * FROM user WHERE email=? /*AND password=?*/");
            editUserQuery = conn.prepareStatement("UPDATE user SET name=?, surname=?, email=?, password=? WHERE id=?");
            deleteUserQuery = conn.prepareStatement("DELETE FROM user WHERE id=?");
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
            insertUserQuery.setString(5, user.getDegOfEducation());
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
                user.setDegOfEducation(rs.getString(5));
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
}
