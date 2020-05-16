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
        ArrayList<Card> hand;
        hand = makeHandCards();
        assertEquals("hall", hand.get(0).getName());
        assertEquals("study", hand.get(1).getName());
        assertEquals("green", hand.get(2).getName());
        assertEquals("rope", hand.get(3).getName());
    }

    @Test
    public void testReveal() {
        Card knife = new Card("knife", "weapon");
        assertFalse(ClueBot.getPlayers()[0].handContains(knife));
        ClueBot.getPlayers()[0].reveal(knife);
        assertTrue(ClueBot.getPlayers()[0].handContains(knife));
    }

    @Test
    public void testPass() {
        Card knife = new Card("knife", "weapon");
        assertFalse(ClueBot.getPlayers()[0].impossibleContains(knife));
        ClueBot.getPlayers()[0].pass(knife);
        assertTrue(ClueBot.getPlayers()[0].impossibleContains(knife));
    }

    @Test
    public void testLogicImpossible() {
        ArrayList<Card> hand;
        hand = makeHandCards();
        Card knife = new Card("knife", "weapon");
        ClueBot.getPlayers()[0].setImpossible(knife);
        ClueBot.getPlayers()[1].setImpossible(knife);
        ClueBot.getPlayers()[2].setImpossible(knife);
        ArrayList<Card> imCard = new ArrayList<>(ClueBotLogic.impossibleCheck(ClueBot.getPlayers(), hand));
        assertEquals("knife", imCard.get(0).getName());
        assertNotEquals("mustard", imCard.get(0).getName());
    }

    @Test
    public void testLogicPossible() {
        ArrayList<Card> possible = new ArrayList<>();
        Card mustard = new Card("mustard", "suspect");
        Card knife = new Card("knife", "weapon");
        Card ballroom = new Card("ballroom", "room");
        possible.add(mustard);
        possible.add(knife);
        possible.add(ballroom);
        ClueBot.getPlayers()[0].setImpossible(knife);
        ClueBot.getPlayers()[0].setImpossible(ballroom);
        ClueBot.getPlayers()[0].witness(possible);
        ClueBotLogic.possibleCheck(ClueBot.getPlayers());
        assertEquals(1, ClueBot.getPlayers()[0].getPossibleCards().get(0).size());
        assertEquals("mustard", ClueBot.getPlayers()[0].getPossibleCards().get(0).get(0).getName());
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
