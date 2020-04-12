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
    private String name;
    private ArrayList<Card> hand; //cards in the players hand
    private ArrayList<Card> impossibleCards; //cards that are impossible for the player to have in their hand
    private ArrayList<ArrayList<Card>> possibleCards; //cards in sets of three that the player show during a suggestion

    /**
     * Opposing Player Constructor
     * @param name The name of the player
     */
    public OppPlayer(String name) {
        this.name = name;
        this.hand = new ArrayList<>();
        this.impossibleCards = new ArrayList<>();
        this.possibleCards  = new ArrayList<>();
    }

    /**
     * Return the name of the player
     * @return Name of the player as a string
     */
    public String getName() {
        return this.name;
    }

    public void reveal(Card card){
        System.out.println("REVEAL LOGIC");
        System.out.println(this.getName());
        System.out.println(card.getName());
    }
}
