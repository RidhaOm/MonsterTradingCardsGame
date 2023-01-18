package model;
import database.Db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.*;

public class User {

    String username;
    String password;
    int coins=20;
    Vector<Card> stack = new Vector<Card>(5);
    //Card[] deck= new Card[4];
    Vector<Card> deck = new Vector<Card>(4);

    //Constructor:
    public User(String new_username) {
        this.username=new_username;
    }

    Connection connectToDb() {
        Db db = new Db();
        Connection conn = db.connectToDb("postgres", "postgres", "");
        return conn;
    }

    //Getter and setter:
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getCoins() {
        return coins;
    }

    public void setCoins(int coins) {
        this.coins = coins;
    }

    public Vector<Card> getStack() {
        return stack;
    }

    public void setStack(Vector<Card> stack) {
        this.stack = stack;
    }

    public Vector<Card> getDeck() {
        return deck;
    }

    public void setDeck(Vector<Card> deck) {
        this.deck = deck;
    }
}
