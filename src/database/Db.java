package database;

import model.Card;

import java.sql.*;

public class Db {

    public Connection connectToDb(String dbname, String user, String pass){
        Connection conn=null;
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        try {
            conn= DriverManager.getConnection("jdbc:postgresql://localhost:5432/"+dbname, user, pass);
            if(conn!=null){
                System.out.println("Connection Established");
            }
            else{
                System.out.println("Connection failed");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return conn;
    }
    public String createUser(Connection conn, String username, String password){
        String result;
        try {
            String query = "insert into users (username, password) values (?, ?)";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, username);
            stmt.setString(2, password);
            stmt.executeUpdate();
            result="The new Username: "+username+" was created successfully.";
            System.out.println(result);
        } catch (Exception e) {
            e.printStackTrace();
            result=e.getMessage();
        }
        return result;
    }

    public String loginUser(Connection conn, String username, String password){
        String result;
        try {
            String query = "SELECT * FROM users WHERE username = ? AND password = ?";
            PreparedStatement statement = conn.prepareStatement(query);
            statement.setString(1, username);
            statement.setString(2, password);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                // Login credentials are correct
                result=username+" logged in successfully.";
            } else {
                // Login credentials are incorrect
                result="ERROR: Login credentials are incorrect.";
            }

            System.out.println(result);
        } catch (Exception e) {
            e.printStackTrace();
            result=e.getMessage();
        }
        return result;
    }

    public String createCard(Connection conn, Card card){
        String result;
        try {
            String query = "insert into cards (id, name, damage, element_type, card_type) values (?, ?, ?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, card.getId());
            stmt.setString(2, card.getName());
            stmt.setDouble(3, card.getDamage());
            stmt.setString(4, card.getElementType());
            stmt.setString(5, card.getCardType());

            stmt.executeUpdate();
            result="The new Car with the id: "+card.getId()+"  was created successfully.";
            System.out.println(result);
        } catch (Exception e) {
            e.printStackTrace();
            result=e.getMessage();
        }
        return result;
    }

    public void createPackage(Connection conn, String username, String password) {
        try {
            String query = "insert into packages (username, password) values (?, ?)";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, username);
            stmt.setString(2, password);
            stmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
