package data;

public class Card {
    String cardType;
    String elementType;
    String name;
    int damage;

    //constructor:
    public Card(String cardType, String elementType, String name, int damage ){
        this.cardType=cardType;
        this.elementType=elementType;
        this.name=name;
        this.damage=damage;
    }

    public String getCardType() {
        return cardType;
    }

    public void setCardType(String cardType) {
        this.cardType = cardType;
    }

    public String getElementType() {
        return elementType;
    }

    public void setElementType(String elementType) {
        this.elementType = elementType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getDamage() {
        return damage;
    }

    public void setDamage(int damage) {
        this.damage = damage;
    }
}
