/*******************************************************************************
 * ClueBot
 *
 * Copyright ©2020 Andrew Levy, Austin Ingarra
 *******************************************************************************/

package cluebot;

import java.util.ArrayList;

/**
 * Clue Bot Logic
 * @version Apr 9, 2020
 */
public class ClueBotLogic {
    ArrayList<Card> allCards;

    public ClueBotLogic(ArrayList<Card> allCards){
        this.allCards = allCards;
    }

    public void calculate(OppPlayer[] players, ArrayList<Card> hand){
        this.impossibleCheck(players, hand);
    }

    /**
     * Runs a check on all of the opposing players impossible cards and determines if any card is on all of the lists
     * @param players List of all of the objects of opposing players
     * @param hand List of user's hand
     * @return A list of common cards between all possible lists (uncommon from player's hand)
     */
    public ArrayList<Card> impossibleCheck(OppPlayer[] players, ArrayList<Card> hand){
        ArrayList<Card> commonCardsClone = players[0].getImpossibleCards();
        ArrayList<Card> commonCards = new ArrayList<>(commonCardsClone);
        for(Integer iter = 1; iter < players.length; iter++){
            ArrayList<Card> commonNextCardsClone = players[iter].getImpossibleCards();
            ArrayList<Card> commonNextCards = new ArrayList<>(commonNextCardsClone);
            commonCards.retainAll(commonNextCards);
        }
        commonCards.removeAll(hand);
        System.out.println();
        System.out.println("FOUND CARDS:");
        commonCards.forEach(System.out::println);
        return commonCards;
    }
}
