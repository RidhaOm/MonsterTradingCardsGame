package test;

import model.Battle;
import model.Card;
import model.User;
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
    void monstersWithTheSameDamageDifferentElementDraw() {
        var card1=new Card("Monster", "Fire", "Kraken", 50);
        var card2=new Card("Monster", "Water", "Ork", 50);
        assertEquals(null, battle.pureMonsterFight(card1,card2));

        card1=new Card("Monster", "Normal", "Knight", 30);
        card2=new Card("Monster", "Water", "Elve", 30);
        assertEquals(null, battle.pureMonsterFight(card1,card2));

    }

    @Test
    void monstersWithTheSameDamageSameElementDraw() {
        var card1=new Card("Monster", "Fire", "Kraken", 50);
        var card2=new Card("Monster", "Fire", "Knight", 50);
        assertEquals(null, battle.pureMonsterFight(card1,card2));

        card1=new Card("Monster", "Normal", "Dragon", 30);
        card2=new Card("Monster", "Normal", "Elve", 30);
        assertEquals(null, battle.pureMonsterFight(card1,card2));

        card1=new Card("Monster", "Water", "Ork", 30);
        card2=new Card("Monster", "Water", "Ork", 30);
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
    void spellFightTheCardWhoHasMoreCalculatedDamageShouldWin() {

        var card1=new Card("Spell", "Fire", "Spell", 50);
        var card2=new Card("Spell", "Water", "Spell", 15);
        assertEquals(card2, battle.spellMixedFight(card1,card2));

        card1=new Card("Spell", "Normal", "Spell", 40);
        card2=new Card("Spell", "Water", "Spell", 100);
        assertEquals(card1, battle.spellMixedFight(card1,card2));
    }

    @Test
    void mixedFightTheCardWhoHasMoreCalculatedDamageShouldWin() {
        var card1=new Card("Spell", "Fire", "Spell", 60);
        var card2=new Card("Monster", "Water", "Dragon", 10);
        assertEquals(card1, battle.spellMixedFight(card1,card2));

        card1=new Card("Monster", "Fire", "Troll", 60);
        card2=new Card("Spell", "Normal", "Spell", 20);
        assertEquals(card1, battle.spellMixedFight(card1,card2));

        card1=new Card("Spell", "Normal", "Spell", 60);
        card2=new Card("Monster", "Fire", "Ork", 20);
        assertEquals(card2, battle.spellMixedFight(card1,card2));

        card1=new Card("Spell", "Normal", "Spell", 20);
        card2=new Card("Monster", "Water", "Kraken", 100);
        assertEquals(card2, battle.spellMixedFight(card1,card2));
    }

    @Test
    void cardsWithTheSameDamageAndCalculatedDamageDraw() {
        var card1=new Card("Spell", "Fire", "Spell", 65);
        var card2=new Card("Monster", "Fire", "Knight", 65);
        assertEquals(null, battle.spellMixedFight(card1,card2));
    }
    @Test
    void cardsWithDifferentDamageTheSameCalculatedDamageDraw() {
        var card1=new Card("Spell", "Fire", "Spell", 65);
        var card2=new Card("Monster", "Fire", "Knight", 65);
        assertEquals(null, battle.spellMixedFight(card1,card2));

        card1=new Card("Spell", "Normal", "Spell", 21);
        card2=new Card("Spell", "Water", "Spell", 80);
        assertEquals(null, battle.spellMixedFight(card1,card2));

        card1=new Card("Monster", "Fire", "Elve", 16);
        card2=new Card("Spell", "Normal", "Spell", 60);
        assertEquals(null, battle.spellMixedFight(card1,card2));
    }

    @Test
    void damageAmplificationInMonsterFight() {
        var card1=new Card("Monster", "Fire", "Kraken", 50);
        var card2=new Card("Monster", "Water", "Ork", 25);
        assertEquals(card1, battle.pureMonsterFight(card1,card2));

        card1=new Card("Monster", "Fire", "Kraken", 20);
        card2=new Card("Monster", "Water", "Dragon", 70);
        assertEquals(card2, battle.spellMixedFight(card1,card2));

        card1=new Card("Monster", "Normal", "Elve", 15);
        card2=new Card("Monster", "Water", "Ork", 60);
        assertEquals(card2, battle.spellMixedFight(card1,card2));
    }

    @Test
    void damageAmplificationInSpellFight() {
        var card1=new Card("Spell", "Fire", "Spell", 50);
        var card2=new Card("Spell", "Fire", "Spell", 25);
        assertEquals(card1, battle.spellMixedFight(card1,card2));

        card1=new Card("Spell", "Normal", "Spell", 20);
        card2=new Card("Spell", "Water", "Spell", 70);
        assertEquals(card1, battle.spellMixedFight(card1,card2));

        card1=new Card("Monster", "Fire", "Spell", 15);
        card2=new Card("Spell", "Normal", "Spell", 60);
        assertEquals(card2, battle.spellMixedFight(card1,card2));
    }

    @Test
    void damageAmplificationInMixedFight() {
        var card1=new Card("Spell", "Fire", "Spell", 50);
        var card2=new Card("Monster", "Fire", "Knight", 25);
        assertEquals(card1, battle.spellMixedFight(card1,card2));

        card1=new Card("Spell", "Normal", "Spell", 20);
        card2=new Card("Spell", "Water", "Spell", 70);
        assertEquals(card1, battle.spellMixedFight(card1,card2));

        card1=new Card("Monster", "Fire", "Elve", 15);
        card2=new Card("Spell", "Normal", "Spell", 60);
        assertEquals(card2, battle.spellMixedFight(card1,card2));
    }

    @Test
    void damageAmplificationActiveShouldAdd4Damage() {
        var card1=new Card("Spell", "Fire", "Spell", 50);
        var card2=new Card("Monster", "Fire", "Knight", 25);
        battle.spellMixedFight(card1,card2);
        assertEquals(54, card1.getDamage());

        card1=new Card("Spell", "Normal", "Spell", 20);
        card2=new Card("Spell", "Water", "Spell", 70);
        battle.spellMixedFight(card1,card2);
        assertEquals(74, card2.getDamage());

        card1=new Card("Monster", "Fire", "Elve", 15);
        card2=new Card("Spell", "Normal", "Spell", 60);
        battle.spellMixedFight(card1,card2);
        assertEquals(64, card2.getDamage());
    }

    @Test
    void damageAmplificationNotActiveShouldNotAdd4Damage() {
        var card1=new Card("Spell", "Fire", "Spell", 49);
        var card2=new Card("Monster", "Fire", "Knight", 25);
        battle.spellMixedFight(card1,card2);
        assertEquals(49, card1.getDamage());

        card1=new Card("Spell", "Normal", "Spell", 20);
        card2=new Card("Spell", "Water", "Spell", 30);
        battle.spellMixedFight(card1,card2);
        assertEquals(30, card2.getDamage());

        card1=new Card("Monster", "Fire", "Elve", 15);
        card2=new Card("Spell", "Normal", "Spell", 16);
        battle.spellMixedFight(card1,card2);
        assertEquals(16, card2.getDamage());
    }


}