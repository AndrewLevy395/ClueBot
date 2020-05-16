package test;

import org.junit.jupiter.api.*;

import static org.junit.Assert.*;

import cluebot.*;

import java.util.ArrayList;

public class ClueTest {

    @BeforeEach
    public void init() {
        ClueBot.prepareGame();
        OppPlayer[] playerList = new OppPlayer[]{new OppPlayer("Tom"), new OppPlayer("Devin"), new OppPlayer("Chris")};
        ClueBot.setPlayers(playerList);
    }

    @Test
    public void testPlayers() {
        OppPlayer[] players = ClueBot.getPlayers();
        assertEquals("Tom", players[0].getName());
        assertEquals("Devin", players[1].getName());
        assertEquals("Chris", players[2].getName());
    }

    @Test
    public void testAddCardToHand() {
        ArrayList<Card> hand = new ArrayList<>();
        hand = makeHandCards();
        assertEquals("hall", hand.get(0).getName());
        assertEquals("study", hand.get(1).getName());
        assertEquals("green", hand.get(2).getName());
        assertEquals("rope", hand.get(3).getName());
    }

    @Test
    public void testReveal() {
        ArrayList<Card> hand = new ArrayList<>();
        hand = makeHandCards();
        Card knife = new Card("knife", "weapon");
        assertFalse(ClueBot.getPlayers()[0].handContains(knife));
        ClueBot.getPlayers()[0].reveal(knife);
        assertTrue(ClueBot.getPlayers()[0].handContains(knife));
    }

    @Test
    public void testWitness() {
        ArrayList<Card> hand = new ArrayList<>();
        hand = makeHandCards();
        Card knife = new Card("knife", "weapon");
        assertFalse(ClueBot.getPlayers()[0].impossibleContains(knife));
        ClueBot.getPlayers()[0].witness(knife);
        assertTrue(ClueBot.getPlayers()[0].impossibleContains(knife));
    }

    public ArrayList<Card> makeHandCards(){
        ArrayList<Card> hand = new ArrayList<>();
        Card hall = new Card("hall", "room");
        hand.add(hall);
        Card study = new Card("study", "room");
        hand.add(study);
        Card green = new Card("green", "suspect");
        hand.add(green);
        Card rope = new Card("rope", "weapon");
        hand.add(rope);
        return hand;
    }
}
