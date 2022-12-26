package main;

import Server.Server;
import Server.HttpServer;

import java.io.IOException;

public class Main {


    public static void main(String[] args) throws IOException {
        Server server = new Server(10001);
        //HttpServer server1 = new HttpServer();
        System.out.println("Server started...");
        server.start();
    }


}



/*
//DbConnection db = new DbConnection();
        //Connection conn=db.connectToDb("postgres", "postgres", "root");
        //db.insertTest(conn, "myUser", "myPass");


        User user1 = new User("user1");
        User user2 = new User("user2");

        // Initialize and print the deck of each user:
        user1.initDeck();
        user2.initDeck();
        user1.printDeck();
        user2.printDeck();

        //Start the Battle:
        Battle battle= new Battle(user1, user2);
        battle.startBattle();
*/


        /*
        user1.openPackage();
        user1.openPackage();
        user1.chooseDeck();
        user1.printStack();
        user1.printDeck();
         */


