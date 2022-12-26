package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

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
        Statement statement;
        String result;
        try {
            String query = "insert into users (username, password) values ('"+username+"', '"+password+"');";
            statement=conn.createStatement();
            statement.executeUpdate(query);
            result="The new Username: "+username+" was successfully created.";
            System.out.println(result);
        } catch (Exception e) {
            e.printStackTrace();
            result=e.toString();
        }
        return result;
    }
}
