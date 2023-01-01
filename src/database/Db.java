package database;

import model.Card;

import java.sql.*;
import java.util.Vector;

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
            String query = "insert into users (username, password, coins) values (?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, username);
            stmt.setString(2, password);
            stmt.setInt(3, 20);
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
            result="The new Card with the id: "+card.getId()+"  was created successfully.";
        } catch (Exception e) {
            e.printStackTrace();
            result=e.getMessage();
        }
        return result;
    }
    public String createPackage(Connection conn, Card[] cards) {
        String result;
        try {
            if(cards.length==5){
                String query = "insert into packages (card_1_id, card_2_id, card_3_id, card_4_id, card_5_id ) values (?, ?, ?, ?, ?)";
                PreparedStatement stmt = conn.prepareStatement(query);
                stmt.setString(1, cards[0].getId());
                stmt.setString(2, cards[1].getId());
                stmt.setString(3, cards[2].getId());
                stmt.setString(4, cards[3].getId());
                stmt.setString(5, cards[4].getId());
                stmt.executeUpdate();
                result="Package was successfully created";
            }
            else{
                result="ERROR: The package should have 5 Cards";
            }
        } catch (Exception e) {
            e.printStackTrace();
            result=e.getMessage();
        }
        return result;
    }
    public int checkCoins(Connection conn, String username){
        int coins=-1;
        try {
            //Check the coins:
            String query="SELECT coins FROM users WHERE username = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, username);
            ResultSet resultSet = stmt.executeQuery();
            if(resultSet.next()){
                coins = resultSet.getInt("coins");
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        return coins;
    }
    public boolean tableIsEmpty(Connection conn, String tableName){
        boolean tableIsEmpty=true;
        try {
            String sql = "SELECT EXISTS (SELECT 1 FROM "+tableName+")";
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                tableIsEmpty = !rs.getBoolean(1);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return tableIsEmpty;
    }
    public String acquirePackage(Connection conn, String username){
        String result;
        try {
            int coins=checkCoins(conn, username);
           if(tableIsEmpty(conn, "packages")){
                result="ERROR: There is no Packages in the database.";
            }
            else if(coins<5){
                result="ERROR: " + username + " only has " + coins + " coins left (the price of the package is 5 coins)";
            }
            else {
                String selectSQL = "SELECT card_1_id, card_2_id, card_3_id, card_4_id, card_5_id FROM packages LIMIT 1 OFFSET 0";
                PreparedStatement preparedStatement = conn.prepareStatement(selectSQL);
                ResultSet rs = preparedStatement.executeQuery();
                if (rs.next()) {
                    String card1Id = rs.getString("card_1_id");
                    String card2Id = rs.getString("card_2_id");
                    String card3Id = rs.getString("card_3_id");
                    String card4Id = rs.getString("card_4_id");
                    String card5Id = rs.getString("card_5_id");

                    String query = "UPDATE cards SET username = ? WHERE id = ? ";
                    PreparedStatement stmt = conn.prepareStatement(query);
                    stmt.setString(1, username);
                    stmt.setString(2, card1Id);
                    stmt.executeUpdate();
                    stmt.setString(2, card2Id);
                    stmt.executeUpdate();
                    stmt.setString(2, card3Id);
                    stmt.executeUpdate();
                    stmt.setString(2, card4Id);
                    stmt.executeUpdate();
                    stmt.setString(2, card5Id);
                    stmt.executeUpdate();
                }
                String deleteSql = "DELETE FROM packages WHERE id IN (SELECT id FROM packages LIMIT 1 OFFSET 0)";
                PreparedStatement pstmt = conn.prepareStatement(deleteSql);
                pstmt.executeUpdate();
                //-5 coins:
                String query = "UPDATE users SET coins = ? WHERE username = ? ";
                PreparedStatement stmt = conn.prepareStatement(query);
                stmt.setInt(1, checkCoins(conn, username)-5);
                stmt.setString(2, username);
                stmt.executeUpdate();
                result=username+" has successfully purchased the package for 5 coins";
            }

        } catch (Exception e) {
            e.printStackTrace();
            result=e.getMessage();
        }

        return result;
    }
    public Vector<Card> getStack(Connection conn, String username) {
        Vector<Card> stack = new Vector<Card>(5);
        try {
            String query = "SELECT * FROM cards WHERE username = ?";
            PreparedStatement statement = conn.prepareStatement(query);
            statement.setString(1, username);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                String id = resultSet.getString("id");
                String name = resultSet.getString("name");
                double damage = resultSet.getDouble("damage");
                String elementType = resultSet.getString("element_type");
                String cardType = resultSet.getString("card_type");
                Card card=new Card(id, cardType, elementType, name, damage);
                stack.add(card);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return stack;
    }
    public String showStack(Connection conn, String username) {
        String result="";
        Vector<Card> stack = getStack(conn, username);
        result=username+" has "+stack.size()+" cards:\n";
        int i=1;
        for (Card card : stack) {
            String cardInfos;
            if(card.getElementType().equals("Normal")){
                cardInfos="Card "+i+"=> Id: "+card.getId()+" | Name: "+card.getName()+" | Damage: "+card.getDamage()+"\n";
            }
            else{
                cardInfos="Card "+i+"=> Id: "+card.getId()+" | Name: "+card.getElementType()+card.getName()+" | Damage: "+card.getDamage()+"\n";
            }
            result+=cardInfos;
            i++;
        }
        return result;
    }
    public Card getCardById(Connection conn, String cardId) {
        try {
            String query = "SELECT * FROM cards WHERE id = ?";
            PreparedStatement statement = conn.prepareStatement(query);
            statement.setString(1, cardId);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                String id = resultSet.getString("id");
                String name = resultSet.getString("name");
                double damage = resultSet.getDouble("damage");
                String elementType = resultSet.getString("element_type");
                String cardType = resultSet.getString("card_type");
                Card card=new Card(id, cardType, elementType, name, damage);
                return card;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    public Vector<Card> getDeck(Connection conn, String username) {
        Vector<Card> deck = new Vector<Card>(4);
        try {
            String query = "SELECT card_1_id, card_2_id, card_3_id, card_4_id FROM decks WHERE username = ?";
            PreparedStatement statement = conn.prepareStatement(query);
            statement.setString(1, username);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                String card_1_id = resultSet.getString("card_1_id");
                String card_2_id = resultSet.getString("card_2_id");
                String card_3_id = resultSet.getString("card_3_id");
                String card_4_id = resultSet.getString("card_4_id");
                String[] cardIds={card_1_id, card_2_id, card_3_id, card_4_id};
                for(String cardid:cardIds){
                    Card card=getCardById(conn, cardid);
                    deck.add(card);
                }
            }
            System.out.println(deck.size());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return deck;
    }
    public String showDeck(Connection conn, String username) {
        String result="";
        Vector<Card> deck = getDeck(conn, username);
        if (deck.size()!=4){
            result= username+"'s deck has not yet been configured.\n";
        }
        else {
            result= username+"'s deck contains the following cards:\n";
            int i=1;
            for (Card card : deck) {
                String cardInfos;
                if(card.getElementType().equals("Normal")){
                    cardInfos="Card "+i+" => Id: "+card.getId()+" | Name: "+card.getName()+" | Damage: "+card.getDamage()+"\n";
                }
                else{
                    cardInfos="Card "+i+" => Id: "+card.getId()+" | Name: "+card.getElementType()+card.getName()+" | Damage: "+card.getDamage()+"\n";
                }
                result+=cardInfos;
                i++;
            }
        }
        return result;
    }
    public boolean checkDecksOwner(Connection conn, String username, Vector<String> cardsId){
        boolean result=true;
        try {
            for(String cardId:cardsId){
                String query = "SELECT username FROM cards WHERE id = ?";
                PreparedStatement statement = conn.prepareStatement(query);
                statement.setString(1, cardId);
                ResultSet resultSet = statement.executeQuery();
                if (resultSet.next()) {
                    if (!username.equals(resultSet.getString("username"))) {
                        // usernames are not equal
                        result=false;
                        break;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
    /*public String setDeck(Connection conn, String username, Vector<String> cardsId ){
        String result;
        try {
            if (cardsId.size()!=4){
                result= "ERROR: The Deck should contain exactly 4 Cards.\n";
            }
            else if (checkDecksOwner(conn, username, cardsId)==false) {
                result = "ERROR: Some cards in the deck do not belong to this user.";
            }
            else {
                String query = "INSERT INTO decks (username, card_1_id, card_2_id, card_3_id, card_4_id) values (?, ?, ?, ?, ?)";
                PreparedStatement stmt = conn.prepareStatement(query);
                stmt.setString(1, username);
                stmt.setString(2, cardsId.get(0));
                stmt.setString(3, cardsId.get(1));
                stmt.setString(4, cardsId.get(2));
                stmt.setString(5, cardsId.get(3));
                stmt.executeUpdate();

                result= username+"'s deck has been successfully configured.";
            }

        } catch (Exception e) {
            e.printStackTrace();
            result=e.getMessage();
        }

        return result;
    }*/
    public String setDeck(Connection conn, String username, Vector<String> cardsId ){
        String result="";
        try {
            if (cardsId.size()!=4){
                result= "ERROR: The Deck should contain exactly 4 Cards.\n";
            }
            else if (checkDecksOwner(conn, username, cardsId)==false) {
                result = "ERROR: Some cards in the deck do not belong to this user.";
            }
            else {
                //Check if a deck with username already exists:
                String selectQuery = "SELECT EXISTS (SELECT 1 FROM decks WHERE username=?)";
                PreparedStatement sstmt = conn.prepareStatement(selectQuery);
                sstmt.setString(1, username);
                ResultSet rs = sstmt.executeQuery();

                if (rs.next()) {
                    if (rs.getBoolean(1)) {
                        // deck with the given username exists, update the card ids:
                        String query = "UPDATE decks SET card_1_id=?, card_2_id=?, card_3_id=?, card_4_id=? WHERE username=?";
                        PreparedStatement stmt = conn.prepareStatement(query);
                        stmt.setString(1, cardsId.get(0));
                        stmt.setString(2, cardsId.get(1));
                        stmt.setString(3, cardsId.get(2));
                        stmt.setString(4, cardsId.get(3));
                        stmt.setString(5, username);
                        stmt.executeUpdate();
                    } else {
                        // deck with the given username does not exist, insert a new deck:
                        String query = "INSERT INTO decks (username, card_1_id, card_2_id, card_3_id, card_4_id) values (?, ?, ?, ?, ?)";
                        PreparedStatement stmt = conn.prepareStatement(query);
                        stmt.setString(1, username);
                        stmt.setString(2, cardsId.get(0));
                        stmt.setString(3, cardsId.get(1));
                        stmt.setString(4, cardsId.get(2));
                        stmt.setString(5, cardsId.get(3));
                        stmt.executeUpdate();
                    }
                    result= username+"'s deck has been successfully configured.";
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            result=e.getMessage();
        }

        return result;
    }
}
