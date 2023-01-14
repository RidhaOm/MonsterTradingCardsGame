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

    public void initDeck(){
        for(int i=0; i<4;i++){
            String cardType;
            String elementType;
            String name="";
            int damage;
            //initialize damage:
            int min_damage=5;
            int max_damage=100;
            damage=(int)(Math.random()*(max_damage-min_damage+1)+min_damage);
            //initialize elementType:
            int min_elementType=1;
            int max_elementType=3;
            int num_elementType = (int)(Math.random()*(max_elementType-min_elementType+1)+min_elementType);
            if (num_elementType==1){
                elementType="Water";
            }
            else if(num_elementType==2){
                elementType="Fire";
            }
            else {
                elementType="Normal";
            }
            // initialize cardType:
            int min_cardType=1;
            int max_cardType=2;
            int num_cardType = (int)(Math.random()*(max_cardType-min_cardType+1)+min_cardType);
            if (num_cardType==1) {
                cardType="Monster";
                //initialize name:
                int min_name=1;
                int max_name=8;
                int name_num=(int)(Math.random()*(max_name-min_name+1)+min_name);
                switch (name_num) {
                    case 1:
                        name="Goblin";
                        break;
                    case 2:
                        name="Dragon";
                        break;
                    case 3:
                        name="Wizzard";
                        break;
                    case 4:
                        name="Ork";
                        break;
                    case 5:
                        name="Knight";
                        break;
                    case 6:
                        name="Kraken";
                        break;
                    case 7:
                        name="Elve";
                        break;
                    case 8:
                        name="Troll";
                        break;
                }
            }
            else {
                cardType="Spell";
                name="Spell";
            }

            //deck[i]=new Card(cardType, elementType, name, damage);
            //Card toAdd=new Card(cardType, elementType, name, damage);
            deck.add(new Card(cardType, elementType, name, damage));
        }
    }

    public void printDeck(){
        System.out.println("\nThis is the Deck of the user: "+getUsername()+": \n");
        for (int i=0; i<getDeck().size();i++){
            System.out.println("The card "+(i+1)+" is a "+getDeck().get(i).getCardType()+" '"+getDeck().get(i).getName()+"' with the element: "+getDeck().get(i).getElementType()+" and a damage="+getDeck().get(i).getDamage());
        }
    }

    public void printStack(){
        System.out.println("\nThis is the Stack of the user: "+getUsername()+": \n");
        for (int i=0; i<getStack().size();i++){
            System.out.println("The card "+(i+1)+" is a "+getStack().get(i).getCardType()+" '"+getStack().get(i).getName()+"' with the element: "+getStack().get(i).getElementType()+" and a damage="+getStack().get(i).getDamage());
        }
    }

    public void openPackage(){
        for(int i=0; i<5;i++){
            String cardType;
            String elementType;
            String name="";
            int damage;
            //initialize damage:
            int min_damage=5;
            int max_damage=100;
            damage=(int)(Math.random()*(max_damage-min_damage+1)+min_damage);
            //initialize elementType:
            int min_elementType=1;
            int max_elementType=3;
            int num_elementType = (int)(Math.random()*(max_elementType-min_elementType+1)+min_elementType);
            if (num_elementType==1){
                elementType="Water";
            }
            else if(num_elementType==2){
                elementType="Fire";
            }
            else {
                elementType="Normal";
            }
            // initialize cardType:
            int min_cardType=1;
            int max_cardType=2;
            int num_cardType = (int)(Math.random()*(max_cardType-min_cardType+1)+min_cardType);
            if (num_cardType==1) {
                cardType="Monster";
                //initialize name:
                int min_name=1;
                int max_name=8;
                int name_num=(int)(Math.random()*(max_name-min_name+1)+min_name);
                switch (name_num) {
                    case 1:
                        name="Goblin";
                        break;
                    case 2:
                        name="Dragon";
                        break;
                    case 3:
                        name="Wizzard";
                        break;
                    case 4:
                        name="Ork";
                        break;
                    case 5:
                        name="Knight";
                        break;
                    case 6:
                        name="Kraken";
                        break;
                    case 7:
                        name="Elve";
                        break;
                    case 8:
                        name="Troll";
                        break;
                }
            }
            else {
                cardType="Spell";
                name="Spell";
            }
            stack.add(new Card(cardType, elementType, name, damage));
            System.out.println("The card "+(i+1)+" is a "+cardType+" '"+name+"' with the element: "+elementType+" and a damage="+damage);

        }
    }

    public void chooseDeck(){
        var myScanner = new Scanner(System.in);
        int added[]={-1,-1,-1,-1};
        printStack();
        for(int i=0;i<4;i++) {
            while(true) {
                System.out.println("Choose a number of the card you want to add to the Deck");
                int card = myScanner.nextInt();
                boolean alreadyAdded = (card==added[0])||(card==added[1])||(card==added[2])||(card==added[3]);
                if ( (card>0) && (card < stack.size()) && !(alreadyAdded) ) {
                    deck.add(stack.get(card-1));
                    added[i]=card;
                    break;
                }
                else if (alreadyAdded) {
                    System.out.println("Card already added! Please choose another card");
                }
                else {
                    System.out.println("Error! Please choose a valid number of a card (Between 1 and "+stack.size()+")");
                }

            }

        }
    }
}
