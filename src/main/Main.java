package main;

import data.User;
import data.Card;

public class Main {

    public static void main(String[] args) {
        User user1 = new User("ridha");
        System.out.println("the user " + user1.getUsername() + " has " + user1.getCoins() + " Coins\n");
    }


}