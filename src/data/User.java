package data;

public class User {

    String username;
    String password;
    int coins=20;
    Card[] stack;

    //Constructor:
    public User(String new_username) {
        this.username=new_username;
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

    public Card[] getStack() {
        return stack;
    }

    public void setStack(Card[] stack) {
        this.stack = stack;
    }
}
