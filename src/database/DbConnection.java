package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DbConnection {

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
    public void insertTest(Connection conn, String username, String password) {
        Statement statement;
        try {
            String query = "insert into usertest (username, password) values ('"+username+"', '"+password+"');";
            statement=conn.createStatement();
            statement.executeUpdate(query);
            System.out.println("Inserted");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
