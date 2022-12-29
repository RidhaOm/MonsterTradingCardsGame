package model;

public class Card {
    String id;
    String cardType;
    String elementType;
    String name;
    double damage;

    //constructor:
    public Card(String cardType, String elementType, String name, double damage ){
        this.cardType=cardType;
        this.elementType=elementType;
        this.name=name;
        this.damage=damage;
    }

    public Card(String id, String cardType, String elementType, String name, double damage ){
        this.id=id;
        this.cardType=cardType;
        this.elementType=elementType;
        this.name=name;
        this.damage=damage;
    }
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public double getDamage() {
        return damage;
    }

    public void setDamage(double damage) {
        this.damage = damage;
    }
}
