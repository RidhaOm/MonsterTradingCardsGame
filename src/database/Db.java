package database;

import model.Battle;
import model.Card;
import model.ScoreboardEntry;
import model.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

public class Db {

    public Connection connectToDb(String dbname, String user, String pass) {
        Connection conn = null;
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        try {
            conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/" + dbname, user, pass);
            /*if(conn!=null){
                System.out.println("Connection Established");
            }
            else{
                System.out.println("Connection failed");
            }*/
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return conn;
    }

    public String createUser(Connection conn, String username, String password) {
        String result;
        try {
            //Table users:
            String query = "insert into users (username, password, coins) values (?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, username);
            stmt.setString(2, password);
            stmt.setInt(3, 20);
            stmt.executeUpdate();
            //Table stats:
            String statsQuery = "INSERT INTO stats (username, games_played, wins, ELO_value) values (?, ?, ?, ?)";
            stmt = conn.prepareStatement(statsQuery);
            stmt.setString(1, username);
            stmt.setInt(2, 0);
            stmt.setInt(3, 0);
            stmt.setInt(4, 100);
            stmt.executeUpdate();
            //Table scoreboard
            String scoreQuery = "INSERT INTO scoreboard (username, ELO_value) values (?, ?)";
            stmt = conn.prepareStatement(scoreQuery);
            stmt.setString(1, username);
            stmt.setInt(2, 100);
            stmt.executeUpdate();
            result = "The new User \"" + username + "\" was created successfully.";
        } catch (Exception e) {
            e.printStackTrace();
            result = e.getMessage();
        }
        return result;
    }

    public String loginUser(Connection conn, String username, String password) {
        String result;
        try {
            String query = "SELECT * FROM users WHERE username = ? AND password = ?";
            PreparedStatement statement = conn.prepareStatement(query);
            statement.setString(1, username);
            statement.setString(2, password);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                // Login credentials are correct
                result = username + " logged in successfully.";
            } else {
                // Login credentials are incorrect
                result = "ERROR: Login credentials are incorrect.";
            }
        } catch (Exception e) {
            //e.printStackTrace();
            result = e.getMessage();
        }
        return result;
    }

    public String createCard(Connection conn, Card card) {
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
            result = "The new Card with the id: " + card.getId() + "  was created successfully.";
        } catch (Exception e) {
            //e.printStackTrace();
            result = e.getMessage();
        }
        return result;
    }

    public String createPackage(Connection conn, Card[] cards) {
        String result;
        try {
            if (cards.length == 5) {
                String query = "insert into packages (card_1_id, card_2_id, card_3_id, card_4_id, card_5_id ) values (?, ?, ?, ?, ?)";
                PreparedStatement stmt = conn.prepareStatement(query);
                stmt.setString(1, cards[0].getId());
                stmt.setString(2, cards[1].getId());
                stmt.setString(3, cards[2].getId());
                stmt.setString(4, cards[3].getId());
                stmt.setString(5, cards[4].getId());
                stmt.executeUpdate();
                result = "Package was successfully created";
            } else {
                result = "ERROR: The package should have 5 Cards";
            }
        } catch (Exception e) {
            //e.printStackTrace();
            result = e.getMessage();
        }
        return result;
    }

    public int checkCoins(Connection conn, String username) {
        int coins = -1;
        try {
            //Check the coins:
            String query = "SELECT coins FROM users WHERE username = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, username);
            ResultSet resultSet = stmt.executeQuery();
            if (resultSet.next()) {
                coins = resultSet.getInt("coins");
            }
        } catch (Exception e) {
            //e.printStackTrace();
        }

        return coins;
    }

    public boolean tableIsEmpty(Connection conn, String tableName) {
        boolean tableIsEmpty = true;
        try {
            String sql = "SELECT EXISTS (SELECT 1 FROM " + tableName + ")";
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                tableIsEmpty = !rs.getBoolean(1);
            }
        } catch (Exception e) {
            //e.printStackTrace();
        }
        return tableIsEmpty;
    }

    public String acquirePackage(Connection conn, String username) {
        String result;
        try {
            int coins = checkCoins(conn, username);
            if (tableIsEmpty(conn, "packages")) {
                result = "ERROR: There is no Packages in the database.";
            } else if (coins < 5) {
                result = "ERROR: " + username + " only has " + coins + " coins left (the price of the package is 5 coins)";
            } else {
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
                stmt.setInt(1, checkCoins(conn, username) - 5);
                stmt.setString(2, username);
                stmt.executeUpdate();
                result = username + " has successfully purchased the package for 5 coins";
            }

        } catch (Exception e) {
            //e.printStackTrace();
            result = e.getMessage();
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
                Card card = new Card(id, cardType, elementType, name, damage);
                stack.add(card);
            }
        } catch (Exception e) {
            //e.printStackTrace();
        }
        return stack;
    }

    public String showStack(Connection conn, String username) {
        String result = "";
        Vector<Card> stack = getStack(conn, username);
        result = username + " has " + stack.size() + " cards:\n";
        int i = 1;
        for (Card card : stack) {
            String cardInfos;
            if (card.getElementType().equals("Normal")) {
                cardInfos = "Card " + i + "=> Id: " + card.getId() + " | Name: " + card.getName() + " | Damage: " + card.getDamage() + "\n";
            } else {
                cardInfos = "Card " + i + "=> Id: " + card.getId() + " | Name: " + card.getElementType() + card.getName() + " | Damage: " + card.getDamage() + "\n";
            }
            result += cardInfos;
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
                Card card = new Card(id, cardType, elementType, name, damage);
                return card;
            }
        } catch (Exception e) {
            //e.printStackTrace();
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
                String[] cardIds = {card_1_id, card_2_id, card_3_id, card_4_id};
                for (String cardid : cardIds) {
                    Card card = getCardById(conn, cardid);
                    deck.add(card);
                }
            }
        } catch (Exception e) {
            //e.printStackTrace();
        }
        return deck;
    }

    public String showDeck(Connection conn, String username) {
        String result = "";
        Vector<Card> deck = getDeck(conn, username);
        if (deck.size() != 4) {
            result = username + "'s deck has not yet been configured.\n";
        } else {
            result = username + "'s deck contains the following cards:\n";
            int i = 1;
            for (Card card : deck) {
                String cardInfos;
                if (card.getElementType().equals("Normal")) {
                    cardInfos = "Card " + i + " => Id: " + card.getId() + " | Name: " + card.getName() + " | Damage: " + card.getDamage() + "\n";
                } else {
                    cardInfos = "Card " + i + " => Id: " + card.getId() + " | Name: " + card.getElementType() + card.getName() + " | Damage: " + card.getDamage() + "\n";
                }
                result += cardInfos;
                i++;
            }
        }
        return result;
    }

    public boolean checkDecksOwner(Connection conn, String username, Vector<String> cardsId) {
        boolean result = true;
        try {
            for (String cardId : cardsId) {
                String query = "SELECT username FROM cards WHERE id = ?";
                PreparedStatement statement = conn.prepareStatement(query);
                statement.setString(1, cardId);
                ResultSet resultSet = statement.executeQuery();
                if (resultSet.next()) {
                    if (!username.equals(resultSet.getString("username"))) {
                        // usernames are not equal
                        result = false;
                        break;
                    }
                }
            }
        } catch (Exception e) {
            ////e.printStackTrace();
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
    public String setDeck(Connection conn, String username, Vector<String> cardsId) {
        String result = "";
        try {
            if (cardsId.size() != 4) {
                result = "ERROR: The Deck should contain exactly 4 Cards.\n";
            } else if (checkDecksOwner(conn, username, cardsId) == false) {
                result = "ERROR: Some cards in the deck do not belong to this user.";
            } else {
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
                    result = username + "'s deck has been successfully configured.";
                }
            }

        } catch (Exception e) {
            //e.printStackTrace();
            result = e.getMessage();
        }

        return result;
    }

    public String showUserData(Connection conn, String username) {
        String result = "";
        try {
            String query = "SELECT name, bio, image FROM users WHERE username = ?";
            PreparedStatement statement = conn.prepareStatement(query);
            statement.setString(1, username);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                String name = resultSet.getString("name");
                String bio = resultSet.getString("bio");
                String image = resultSet.getString("image");
                result = "Name: " + name + " | bio: " + bio + " | image: " + image;
            }
        } catch (Exception e) {
            //e.printStackTrace();
            result = e.getMessage();
        }
        return result;
    }

    public String editUserData(Connection conn, String username, String name, String bio, String image) {
        String result = "";
        try {
            String query = "UPDATE users SET name=?, bio=?, image=? WHERE username=?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, name);
            stmt.setString(2, bio);
            stmt.setString(3, image);
            stmt.setString(4, username);
            stmt.executeUpdate();
            result = username + "'s data has been successfully edited.";

        } catch (Exception e) {
            //e.printStackTrace();
            result = e.getMessage();
        }

        return result;
    }

    public void addToQueue(Connection conn, String username) {
        try {
            // Create the prepared statement and set the username as a parameter
            PreparedStatement stmt = conn.prepareStatement("INSERT INTO queue (username) VALUES (?)");
            stmt.setString(1, username);

            // Execute the statement
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteFromQueue(Connection conn, String username) {
        try {
            // Create the prepared statement and set the username as a parameter
            PreparedStatement stmt = conn.prepareStatement("DELETE FROM queue WHERE username = ?");
            stmt.setString(1, username);

            // Execute the statement
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public String getNextInQueue(Connection conn, String username) {
        try {
            String query = "SELECT * FROM queue WHERE username = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, username);
            ResultSet resultSet = stmt.executeQuery();

            if (resultSet.next()) {
                // The user is in the queue, check if there is another user in the queue
                query = "SELECT * FROM queue WHERE username != ?";
                stmt = conn.prepareStatement(query);
                stmt.setString(1, username);
                resultSet = stmt.executeQuery();

                if (resultSet.next()) {
                    // There is another user in the queue, return their username
                    return resultSet.getString("username");
                } else {
                    // There are no other users in the queue, return null
                    return null;
                }
            } else {
                // The user is not in the queue, return null
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public String getPendingOpponentUsername(Connection conn, String username) {
        String query = "SELECT * FROM battles WHERE user1 = ? OR user2 = ? AND completed = false";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, username);
            stmt.setString(2, username);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                // The user is in a pending battle, return the opponent's username
                String opponentUsername;
                if (rs.getString("user1").equals(username)) {
                    opponentUsername = rs.getString("user2");
                } else {
                    opponentUsername = rs.getString("user1");
                }
                return opponentUsername;
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    public boolean hasPendingBattle(Connection conn, String username) {
        String query = "SELECT * FROM battles WHERE user1 = ? OR user2 = ? AND completed = false";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, username);
            stmt.setString(2, username);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                // The user is in a pending battle
                return true;
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return false;
    }

    public String getStats(Connection conn, String username) {
        String result="";
        try {
            //Check the coins:
            String query = "SELECT games_played, wins, ELO_value FROM stats WHERE username = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, username);
            ResultSet resultSet = stmt.executeQuery();
            if (resultSet.next()) {
                int games_played = resultSet.getInt("games_played");
                int wins = resultSet.getInt("wins");
                int ELO_value = resultSet.getInt("ELO_value");
                result="The stats of " + username + ": \n"
                        + "Games played: " + games_played + "\n"
                        + "wins: " + wins + "\n"
                        + "ELO Value:  " + ELO_value + "\n";
            }
        } catch (Exception e) {
            result=e.getMessage();
        }

        return result;
    }

    public void updateStats(Connection conn, String username, String opponentUsername, String winner) {
        try {
            if (winner.equals("Draw")) {
                // Table stats
                String statsQuery = "UPDATE stats SET games_played = games_played + 1 WHERE username = ? ";
                PreparedStatement stmt = conn.prepareStatement(statsQuery);
                stmt.setString(1, username);
                stmt.executeUpdate();
                stmt.setString(1, opponentUsername);
                stmt.executeUpdate();
            } else {
                String loser;
                if (winner.equals(username)) {
                    loser = opponentUsername;
                } else {
                    loser = username;
                }
                // Table stats
                String winnerStatsQuery = "UPDATE stats SET games_played = games_played + 1, wins = wins + 1, ELO_value = ELO_value + 3 WHERE username = ? ";
                PreparedStatement stmt = conn.prepareStatement(winnerStatsQuery);
                stmt.setString(1, winner);
                stmt.executeUpdate();

                String loserStatsQuery = "UPDATE stats SET games_played = games_played + 1, ELO_value = ELO_value - 5 WHERE username = ? ";
                stmt = conn.prepareStatement(loserStatsQuery);
                stmt.setString(1, loser);
                stmt.executeUpdate();
                //Table scoreboard
                String winnerScoreQuery = "UPDATE scoreboard SET ELO_value = ELO_value + 3 WHERE username = ? ";
                stmt = conn.prepareStatement(winnerScoreQuery);
                stmt.setString(1, winner);
                stmt.executeUpdate();

                String loserScoreQuery = "UPDATE scoreboard SET ELO_value = ELO_value - 5 WHERE username = ? ";
                stmt = conn.prepareStatement(loserScoreQuery);
                stmt.setString(1, loser);
                stmt.executeUpdate();
            }

        } catch (Exception e) {
        }
    }

    public List<ScoreboardEntry> getScoreboard(Connection conn) {
        List<ScoreboardEntry> scoreboard = new ArrayList<>();
        String query = "SELECT username, ELO_value FROM scoreboard ORDER BY ELO_value DESC";
        try (Statement stmt = conn.createStatement()) {
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                String username = rs.getString("username");
                int ELO_value = rs.getInt("ELO_value");
                scoreboard.add(new ScoreboardEntry(username, ELO_value));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return scoreboard;
    }

    public String startBattle(Connection conn, String username, String opponentUsername) {
        String result = "Battle between " + username + " and " + opponentUsername + "\n";
        try {
            // Insert a new row in the battles table for this battle
            String query = "insert into battles (user1, user2, completed) values (?, ?, ?) returning id";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, username);
            stmt.setString(2, opponentUsername);
            stmt.setBoolean(3, false);
            ResultSet rs = stmt.executeQuery();
            int battleId = -1;
            if (rs.next()) {
                battleId = rs.getInt("id");
            }

            User user = new User(username);
            user.setDeck(getDeck(conn, username));
            User opponentUser = new User(opponentUsername);
            opponentUser.setDeck(getDeck(conn, opponentUsername));
            Battle battle = new Battle(user, opponentUser);
            //End Battle:
            User winner = battle.startBattle();
            if (winner == null) {
                result += "Draw\n";
            } else {
                result += winner.getUsername() + " Wins\n";
            }

            // Update the row in the battles table to mark the battle as completed and set the winner
            query = "update battles set completed = ?, winner = ? where id = ?";
            stmt = conn.prepareStatement(query);
            stmt.setBoolean(1, true);
            if (winner != null) {
                stmt.setString(2, winner.getUsername());
            } else {
                stmt.setString(2, null);
            }
            stmt.setInt(3, battleId);
            stmt.executeUpdate();
            //update the stats in the tables stats and scoreboard:
            if (winner==null){
                updateStats(conn, username, opponentUsername, "Draw");
            }
            else {
                updateStats(conn, username, opponentUsername, winner.getUsername());
            }
            result=battle.getBattleLog();
        } catch (Exception e) {
            //e.printStackTrace();
            result += e.getMessage();
        }

        return result;
    }


}
