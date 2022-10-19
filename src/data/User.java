package data;

public class User {

    String username;
    String password;
    int coins=20;
    Card[] stack;
    Card[] deck= new Card[4];

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

    public Card[] getDeck() {
        return deck;
    }

    public void setDeck(Card[] deck) {
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
                int max_name=7;
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
                }
            }
            else {
                cardType="Spell";
                name="Spell";
            }

            deck[i]=new Card(cardType, elementType, name, damage);
            //System.out.println("The card "+(i+1)+" is a "+cardType+" '"+name+"' with the element: "+elementType+"("+num_elementType +") and a damage="+damage);
        }
    }

}
