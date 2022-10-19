package main;

import data.User;
import data.Card;

public class Main {

    public static void main(String[] args) {
        User user1 = new User("user1");
        User user2 = new User("user2");
        // Initialize and print the deck of each user:
        user1.initDeck();
        user2.initDeck();
        System.out.println("This is the Deck of the user: "+user1.getUsername()+": ");
        for (int i=0; i<4;i++){
            System.out.println("The card "+(i+1)+" is a "+user1.getDeck()[i].getCardType()+" '"+user1.getDeck()[i].getName()+"' with the element: "+user1.getDeck()[i].getElementType()+" and a damage="+user1.getDeck()[i].getDamage());
        }
        System.out.println("This is the Deck of the user: "+user2.getUsername()+": ");
        for (int i=0; i<4;i++){
            System.out.println("The card "+(i+1)+" is a "+user2.getDeck()[i].getCardType()+" '"+user2.getDeck()[i].getName()+"' with the element: "+user2.getDeck()[i].getElementType()+" and a damage="+user2.getDeck()[i].getDamage());
        }
        //Start the Battle:
        System.out.println("\nBattle Start: \n");
        for (int i=1;;i++) {
            //choose a random card from the deck for user1:
            int card1=(int)(Math.random()*(3-0+1)+0);//choose a number between 0 and 3
            //choose a random card from the deck for user2:
            int card2=(int)(Math.random()*(3-0+1)+0);//choose a number between 0 and 3
            System.out.println("Round "+i+":\n");
            System.out.println("selected card for "+user1.getUsername()+": Type="+user1.getDeck()[card1].getCardType()+" | Name="+user1.getDeck()[card1].getName()+" | Element="+user1.getDeck()[card1].getElementType()+" | Damage="+user1.getDeck()[card1].getDamage());
            System.out.println("                                    vs");
            System.out.println("selected card for "+user2.getUsername()+": Type="+user2.getDeck()[card2].getCardType()+" | Name="+user2.getDeck()[card2].getName()+" | Element="+user2.getDeck()[card2].getElementType()+" | Damage="+user2.getDeck()[card2].getDamage());
            // Pure monster fight:
            if( (user1.getDeck()[card1].getCardType()=="Monster")&&(user2.getDeck()[card2].getCardType()=="Monster") ){
                //Goblins vs Dragons ==> Dragons win
                if( (user1.getDeck()[card1].getName()=="Goblin")&&(user2.getDeck()[card2].getName()=="Dragon") ){
                    System.out.println("Goblins are too afraid of Dragons to attack. => " + user2.getUsername() + " wins the round.");
                }
                else if( (user2.getDeck()[card2].getName()=="Goblin")&&(user1.getDeck()[card1].getName()=="Dragon") ){
                    System.out.println("Goblins are too afraid of Dragons to attack. => " + user1.getUsername() + " wins the round.");
                }
                //Wizzards vs Orks => Wizzard win
                else if( (user1.getDeck()[card1].getName()=="Wizzard")&&(user2.getDeck()[card2].getName()=="Ork") ){
                    System.out.println("Wizzard can control Orks so they are not able to damage them. => " + user1.getUsername() + " wins the round.");
                }
                else if( (user2.getDeck()[card2].getName()=="Wizzard")&&(user1.getDeck()[card1].getName()=="Ork") ){
                    System.out.println("Goblins are too afraid of Dragons to attack. => " + user2.getUsername() + " wins the round.");
                }
                // FireElves vs Dragons => FireElves win
                else if( (user1.getDeck()[card1].getName()=="Elve")&&(user1.getDeck()[card1].getElementType()=="Fire")&&(user2.getDeck()[card2].getName()=="Dragon") ){
                    System.out.println("The FireElves know Dragons since they were little and can evade their attacks. => " + user1.getUsername() + " wins the round.");
                }
                else if( (user2.getDeck()[card2].getName()=="Elve")&&(user2.getDeck()[card2].getElementType()=="Fire")&&(user1.getDeck()[card1].getName()=="Dragon") ){
                    System.out.println("The FireElves know Dragons since they were little and can evade their attacks. => " + user2.getUsername() + " wins the round.");
                }
                // Else => more damage wins:
                else if( user1.getDeck()[card1].getDamage()>user2.getDeck()[card2].getDamage() ){
                    System.out.println(user1.getDeck()[card1].getDamage() + " > " + user2.getDeck()[card2].getDamage() + ". => " + user1.getUsername() + " wins the round.");
                }
                else if( user2.getDeck()[card2].getDamage()>user1.getDeck()[card1].getDamage() ){
                    System.out.println(user2.getDeck()[card2].getDamage() + " > " + user1.getDeck()[card1].getDamage() + ". => " + user2.getUsername() + " wins the round.");
                }
                else {
                    System.out.println(user2.getDeck()[card2].getDamage() + " = " + user1.getDeck()[card1].getDamage() + ". => Draw.");
                }
            }
            // Spell and Mixed fight
            else{
                // Knights vs WaterSpells => WaterSpells win
                if( (user1.getDeck()[card1].getName()=="Knight")&&(user2.getDeck()[card2].getCardType()=="Spell")&&(user2.getDeck()[card2].getElementType()=="Water") ){
                    System.out.println("The armor of Knights is so heavy that WaterSpells make them drown them instantly. => " + user2.getUsername() + " wins the round.");
                }
                else if( (user2.getDeck()[card2].getName()=="Knight")&&(user1.getDeck()[card1].getCardType()=="Spell")&&(user1.getDeck()[card1].getElementType()=="Water") ){
                    System.out.println("The armor of Knights is so heavy that WaterSpells make them drown them instantly. =>"  + user1.getUsername() + " wins the round.");
                }
                // Krakens vs Spells => Krakens win
                else if( (user1.getDeck()[card1].getName()=="Kraken")&&(user2.getDeck()[card2].getCardType()=="Spell") ){
                    System.out.println("The Kraken is immune against spells. => " + user1.getUsername() + " wins the round.");
                }
                else if( (user2.getDeck()[card2].getName()=="Kraken")&&(user1.getDeck()[card1].getCardType()=="Spell") ){
                    System.out.println("The Kraken is immune against spells. => " + user2.getUsername() + " wins the round.");
                }
                else {
                    int calculatedDamage1;
                    int calculatedDamage2;
                    //effective
                    if ( (user1.getDeck()[card1].getElementType() =="Water")&&(user2.getDeck()[card2].getElementType() =="Fire") ||
                            (user1.getDeck()[card1].getElementType() =="Fire")&&(user2.getDeck()[card2].getElementType() =="Normal")||
                            (user1.getDeck()[card1].getElementType() =="Normal")&&(user2.getDeck()[card2].getElementType() =="Water") ){
                        calculatedDamage1=user1.getDeck()[card1].getDamage()*2;
                        calculatedDamage2=user2.getDeck()[card2].getDamage()/2;
                    }
                    //Not effective
                    else if ( (user1.getDeck()[card1].getElementType() =="Fire")&&(user2.getDeck()[card2].getElementType() =="Water") ||
                            (user1.getDeck()[card1].getElementType() =="Normal")&&(user2.getDeck()[card2].getElementType() =="Fire")||
                            (user1.getDeck()[card1].getElementType() =="Water")&&(user2.getDeck()[card2].getElementType() =="Normal") ){
                        calculatedDamage1=user1.getDeck()[card1].getDamage()/2;
                        calculatedDamage2=user2.getDeck()[card2].getDamage()*2;
                    }
                    //No effect
                    else {
                        calculatedDamage1=user1.getDeck()[card1].getDamage();
                        calculatedDamage2=user2.getDeck()[card2].getDamage();
                    }

                    if( calculatedDamage1>calculatedDamage2 ){
                        System.out.println(user1.getDeck()[card1].getDamage()+" VS "+user2.getDeck()[card2].getDamage()+" -> "+calculatedDamage1 + " VS " + calculatedDamage2 + " => " + user1.getUsername() + " wins the round.");
                    }
                    else if( calculatedDamage2>calculatedDamage1 ){
                        System.out.println(user1.getDeck()[card1].getDamage()+" VS "+user2.getDeck()[card2].getDamage()+" -> "+calculatedDamage1 + " VS " + calculatedDamage2 + " => " + user2.getUsername() + " wins the round.");
                    }
                    else {
                        System.out.println(user1.getDeck()[card1].getDamage()+" VS "+user2.getDeck()[card2].getDamage()+" -> "+calculatedDamage1 + " VS " + calculatedDamage2 + " => " + user2.getUsername() + " Draw.");
                    }
                }

            }

            if(i>0)
                break;
        }
    }


}