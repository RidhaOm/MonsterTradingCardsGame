package model;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class Battle {
    User user1;
    User user2;

    //Constructor
    public Battle(User user1, User user2){
        this.user1=user1;
        this.user2=user2;
    }

    public Battle(){}

    public Card pureMonsterFight(Card card1, Card card2) {
        if( (card1.getCardType()=="Monster")&&(card2.getCardType()=="Monster") ){
            //Goblins vs Dragons ==> Dragons win
            if( (card1.getName()=="Goblin")&&(card2.getName()=="Dragon") ){
                System.out.println("Goblins are too afraid of Dragons to attack. => " + user2.getUsername() + " wins the round.");
                //user1.getDeck().remove(index1);
                return card2;
            }
            else if( (card2.getName()=="Goblin")&&(card1.getName()=="Dragon") ){
                System.out.println("Goblins are too afraid of Dragons to attack. => " + user1.getUsername() + " wins the round.");
                //user2.getDeck().remove(index2);
                return card1;
            }
            //Wizzards vs Orks => Wizzard wins
            else if( (card1.getName()=="Wizzard")&&(card2.getName()=="Ork") ){
                System.out.println("Wizzard can control Orks so they are not able to damage them. => " + user1.getUsername() + " wins the round.");
                //user2.getDeck().remove(index2);
                return card1;
            }
            else if( (card2.getName()=="Wizzard")&&(card1.getName()=="Ork") ){
                System.out.println("Goblins are too afraid of Dragons to attack. => " + user2.getUsername() + " wins the round.");
                //user1.getDeck().remove(index1);
                return card2;
            }
            // FireElves vs Dragons => FireElves win
            else if( (card1.getName()=="Elve")&&(card1.getElementType()=="Fire")&&(card2.getName()=="Dragon") ){
                System.out.println("The FireElves know Dragons since they were little and can evade their attacks. => " + user1.getUsername() + " wins the round.");
                //user2.getDeck().remove(index2);
                return card1;
            }
            else if( (card2.getName()=="Elve")&&(card2.getElementType()=="Fire")&&(card1.getName()=="Dragon") ){
                System.out.println("The FireElves know Dragons since they were little and can evade their attacks. => " + user2.getUsername() + " wins the round.");
                //user1.getDeck().remove(index1);
                return card2;
            }
            // Else => more damage wins:
            else if( card1.getDamage()>card2.getDamage() ){
                System.out.println(card1.getDamage() + " VS " + card2.getDamage() + " => " + user1.getUsername() + " wins the round.");
                //user2.getDeck().remove(index2);
                return card1;
            }
            else if( card2.getDamage()>card1.getDamage() ){
                System.out.println(card1.getDamage() + " VS " + card2.getDamage() + " => " + user2.getUsername() + " wins the round.");
                //user1.getDeck().remove(index1);
                return card2;
            }
            else {
                System.out.println(card2.getDamage() + " VS  " + card1.getDamage() + " => Draw.");
            }
        }
        return null;
    }

    public Card spellMixedFight(Card card1, Card card2){
        // Knights vs WaterSpells => WaterSpells win
        if( (card1.getName()=="Knight")&&(card2.getCardType()=="Spell")&&(card2.getElementType()=="Water") ){
            System.out.println("The armor of Knights is so heavy that WaterSpells make them drown them instantly. => " + user2.getUsername() + " wins the round.");
            //user1.getDeck().remove(index1);
            return card2;
        }
        else if( (card2.getName()=="Knight")&&(card1.getCardType()=="Spell")&&(card1.getElementType()=="Water") ){
            System.out.println("The armor of Knights is so heavy that WaterSpells make them drown them instantly. =>"  + user1.getUsername() + " wins the round.");
            //user2.getDeck().remove(index2);
            return card1;
        }
        // Krakens vs Spells => Krakens win
        else if( (card1.getName()=="Kraken")&&(card2.getCardType()=="Spell") ){
            System.out.println("The Kraken is immune against spells. => " + user1.getUsername() + " wins the round.");
            //user2.getDeck().remove(index2);
            return card1;
        }
        else if( (card2.getName()=="Kraken")&&(card1.getCardType()=="Spell") ){
            System.out.println("The Kraken is immune against spells. => " + user2.getUsername() + " wins the round.");
            //user1.getDeck().remove(index1);
            return card2;
        }
        else {
            double calculatedDamage1;
            double calculatedDamage2;
            //effective
            if ( (card1.getElementType() =="Water")&&(card2.getElementType() =="Fire") ||
                    (card1.getElementType() =="Fire")&&(card2.getElementType() =="Normal")||
                    (card1.getElementType() =="Normal")&&(card2.getElementType() =="Water") ){
                calculatedDamage1=card1.getDamage()*2;
                calculatedDamage2=card2.getDamage()/2;
            }
            //Not effective
            else if ( (card1.getElementType() =="Fire")&&(card2.getElementType() =="Water") ||
                    (card1.getElementType() =="Normal")&&(card2.getElementType() =="Fire")||
                    (card1.getElementType() =="Water")&&(card2.getElementType() =="Normal") ){
                calculatedDamage1=card1.getDamage()/2;
                calculatedDamage2=card2.getDamage()*2;
            }
            //No effect
            else {
                calculatedDamage1=card1.getDamage();
                calculatedDamage2=card2.getDamage();
            }

            if( calculatedDamage1>calculatedDamage2 ){
                System.out.println(card1.getDamage()+" VS "+card2.getDamage()+" -> "+calculatedDamage1 + " VS " + calculatedDamage2 + " => " + user1.getUsername() + " wins the round.");
                //user2.getDeck().remove(index2);
                return card1;
            }
            else if( calculatedDamage2>calculatedDamage1 ){
                System.out.println(card1.getDamage()+" VS "+card2.getDamage()+" -> "+calculatedDamage1 + " VS " + calculatedDamage2 + " => " + user2.getUsername() + " wins the round.");
                //user1.getDeck().remove(index1);
                return card2;
            }
            else {
                System.out.println(card1.getDamage()+" VS "+card2.getDamage()+" -> "+calculatedDamage1 + " VS " + calculatedDamage2 + " => Draw.");
            }
        }
        return null;
    }

    public void startBattle() {
        System.out.println("\nBattle Starts: \n");
        for (int i=1;i<=100;i++) {
            //choose a random card from the deck for user1:
            int indexCard1=(int)(Math.random()*(user1.getDeck().size()));//choose a number between 0 and 3
            //choose a random card from the deck for user2:
            int indexCard2=(int)(Math.random()*(user2.getDeck().size()));//choose a number between 0 and 3
            Card card1=user1.getDeck().get(indexCard1);
            Card card2=user2.getDeck().get(indexCard2);

            System.out.println("\nRound "+i+":\n");
            System.out.println("selected card for "+user1.getUsername()+": Type="+card1.getCardType()+" | Name="+card1.getName()+" | Element="+card1.getElementType()+" | Damage="+card1.getDamage());
            System.out.println("                                    vs");
            System.out.println("selected card for "+user2.getUsername()+": Type="+card2.getCardType()+" | Name="+card2.getName()+" | Element="+card2.getElementType()+" | Damage="+user2.getDeck().get(indexCard2).getDamage());
            Card winner;
            // Pure monster fight:
            if( (card1.getCardType()=="Monster")&&(user2.getDeck().get(indexCard2).getCardType()=="Monster") ){
                winner=pureMonsterFight(card1, card2);
            }
            // Spell and Mixed fight
            else{
                winner=spellMixedFight(card1, card2);
            }

            //Remove the card that lose
            if (winner==card1){
                user2.getDeck().remove(indexCard2);
            }
            else if (winner==card2){
                user1.getDeck().remove(indexCard1);
            }
            //End of the Battle
            if(user2.getDeck().size()==0) {
                System.out.println("\n==============================  " +user1.getUsername()+ " Wins  ==============================");
                break;
            }
            if(user1.getDeck().size()==0) {
                System.out.println("\n==============================  " +user2.getUsername()+ " Wins  ==============================");
                break;
            }

        }

    }

}
