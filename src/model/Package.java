package model;

public class Package {
    int id;
    Card[] cards = new Card[5];

    public Package(int id, Card[] cards){
        this.id=id;
        this.cards=cards;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Card[] getCards() {
        return cards;
    }

    public void setCards(Card[] cards) {
        this.cards = cards;
    }
}
