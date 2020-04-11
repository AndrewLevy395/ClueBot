/*******************************************************************************
 * ClueBot
 *
 * Copyright ©2020 Andrew Levy, Austin Ingarra
 *******************************************************************************/

package cluebot;

import java.util.ArrayList;

/**
 * Class for storing information on an opposing player
 * @version Apr 9, 2020
 */
public class OppPlayer {
    String name;
    ArrayList<Card> cards;
    ArrayList<Card> impossibleCards;
    ArrayList<ArrayList<Card>> possibleCards;

    /**
     *
     * Opposing Player Constructor
     * @param name The name of the player
     */
    public OppPlayer(String name) {
        this.name = name;
        this.cards = new ArrayList<>();
        this.impossibleCards = new ArrayList<>();
        this.possibleCards  = new ArrayList<>();
    }
}
