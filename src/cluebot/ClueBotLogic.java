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

    public ArrayList<Card> impossibleCheck(OppPlayer[] players, ArrayList<Card> hand){
        ArrayList<Card> commonCardClone = players[0].getImpossibleCards();
        ArrayList<Card> commonCards = new ArrayList<>(commonCardClone);
        for(Integer iter = 1; iter < players.length; iter++){
            commonCards.retainAll(players[iter].getImpossibleCards());
        }
        commonCards.removeAll(hand);
        System.out.println();
        System.out.println("FOUND CARDS:");
        commonCards.forEach(System.out::println);
        return commonCards;
    }
}
