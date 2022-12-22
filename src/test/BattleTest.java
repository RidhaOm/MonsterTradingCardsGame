package test;

import data.Battle;
import data.Card;
import data.User;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BattleTest {
    Battle battle =new Battle(new User("user1"), new User("user2"));

    @Test
    void dragonsShouldBeatGoblins() {
        var card1=new Card("Monster", "Fire", "Dragon", 1);
        var card2=new Card("Monster", "Water", "Goblin", 99);
        assertEquals(card1, battle.pureMonsterFight(card1,card2));
    }

    @Test
    void wizzardsShouldBeatOrks() {
        var card1=new Card("Monster", "Fire", "Wizzard", 1);
        var card2=new Card("Monster", "Water", "Ork", 99);
        assertEquals(card1, battle.pureMonsterFight(card1,card2));
    }

    @Test
    void fireElvesShouldBeatDragons() {
        var card1=new Card("Monster", "Fire", "Elve", 1);
        var card2=new Card("Monster", "Water", "Dragon", 99);
        assertEquals(card1, battle.pureMonsterFight(card1,card2));
    }

    @Test
    void theMonsterWhoHasMoreDamageShouldWin() {
        var card1=new Card("Monster", "Fire", "Ork", 98);
        var card2=new Card("Monster", "Water", "Dragon", 99);
        assertEquals(card2, battle.pureMonsterFight(card1,card2));

        card1=new Card("Monster", "Water", "Knight", 49);
        card2=new Card("Monster", "Fire", "Wizzard", 50);
        assertEquals(card2, battle.pureMonsterFight(card1,card2));

        card1=new Card("Monster", "Normal", "Kraken", 3);
        card2=new Card("Monster", "Fire", "Goblin", 2);
        assertEquals(card1, battle.pureMonsterFight(card1,card2));

        card1=new Card("Monster", "Normal", "Elve", 20);
        card2=new Card("Monster", "Water", "Elve", 30);
        assertEquals(card2, battle.pureMonsterFight(card1,card2));
    }

    @Test
    void monstersWithTheSameDamageDraw() {
        var card1=new Card("Monster", "Fire", "Kraken", 50);
        var card2=new Card("Monster", "Water", "Ork", 50);
        assertEquals(null, battle.pureMonsterFight(card1,card2));

        card1=new Card("Monster", "Normal", "Knight", 30);
        card2=new Card("Monster", "Water", "Elve", 30);
        assertEquals(null, battle.pureMonsterFight(card1,card2));
    }

    @Test
    void waterSpellsShouldBeatKnights() {
        var card1=new Card("Spell", "Water", "Spell", 1);
        var card2=new Card("Monster", "Normal", "Knight", 99);
        assertEquals(card1, battle.spellMixedFight(card1,card2));
    }

    @Test
    void krakensShouldBeatSpells() {
        var card1=new Card("Spell", "Fire", "Spell", 99);
        var card2=new Card("Monster", "Fire", "Kraken", 10);
        assertEquals(card2, battle.spellMixedFight(card1,card2));
    }

    @Test
    void theCardWhoHasMoreCalculatedDamageShouldWin() {
        var card1=new Card("Spell", "Fire", "Spell", 60);
        var card2=new Card("Monster", "Water", "Dragon", 10);
        assertEquals(card1, battle.spellMixedFight(card1,card2));

        card1=new Card("Spell", "Fire", "Spell", 50);
        card2=new Card("Spell", "Water", "Spell", 15);
        assertEquals(card2, battle.spellMixedFight(card1,card2));

        card1=new Card("Monster", "Fire", "Troll", 60);
        card2=new Card("Spell", "Normal", "Spell", 20);
        assertEquals(card1, battle.spellMixedFight(card1,card2));

        card1=new Card("Spell", "Normal", "Spell", 60);
        card2=new Card("Monster", "Fire", "Ork", 20);
        assertEquals(card2, battle.spellMixedFight(card1,card2));

        card1=new Card("Spell", "Normal", "Spell", 40);
        card2=new Card("Spell", "Water", "Spell", 100);
        assertEquals(card1, battle.spellMixedFight(card1,card2));

        card1=new Card("Spell", "Normal", "Spell", 20);
        card2=new Card("Monster", "Water", "Kraken", 100);
        assertEquals(card2, battle.spellMixedFight(card1,card2));
    }

    @Test
    void cardsWithTheSameCalculatedDamageDraw() {
        var card1=new Card("Spell", "Fire", "Spell", 65);
        var card2=new Card("Monster", "Fire", "Knight", 65);
        assertEquals(null, battle.spellMixedFight(card1,card2));

        card1=new Card("Spell", "Normal", "Spell", 20);
        card2=new Card("Spell", "Water", "Spell", 80);
        assertEquals(null, battle.spellMixedFight(card1,card2));

        card1=new Card("Monster", "Fire", "Elve", 15);
        card2=new Card("Spell", "Normal", "Spell", 60);
        assertEquals(null, battle.spellMixedFight(card1,card2));
    }
}