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

    /**
     * Get players hand
     * @return hand
     */
    public ArrayList<Card> getHand(){
        return hand;
    }

    /**
     * Add a card to an opposing player's hand
     * @param handCard The card being added
     */
    public void setHand(Card handCard) {
        if(!hand.contains(handCard)){
            this.hand.add(handCard);
        }
    }

    /**
     * Get the list of impossible cards
     * @return The list of impossible cards
     */
    public ArrayList<Card> getImpossibleCards() {
        return this.impossibleCards;
    }

    /**
     * Get the list of possible cards
     * @return The list of possible cards
     */
    public ArrayList<ArrayList<Card>> getPossibleCards() {
        return this.possibleCards;
    }

    /**
     * Add a card to the list of impossible cards for an opposing player
     */
    public void setImpossible(Card impossibleCard) {
        if(!impossibleCards.contains(impossibleCard)){
            this.impossibleCards.add(impossibleCard);
            for(ArrayList<Card> a : possibleCards){
                for(Card c : a){
                    if(c == impossibleCard){
                        a.remove(c);
                    }
                }
            }
        }
    }

    /**
     * Display an opposing player's known hand and the list of cards that the user knows they do not have
     */
    public void display() {
        System.out.println(this.getName() + "'s hand:");
        for(Card C : hand){
            System.out.println(C.getName());
        }
        System.out.println();
        System.out.println("Cards that are impossible for " + this.getName() + " to have:");
        for(Card C : impossibleCards){
            System.out.println(C.getName());
        }
        System.out.println();
        System.out.println("Cards that are possible for " + this.getName() + " to have:");
        for(ArrayList<Card> A : possibleCards){
            for(Card C : A){
                System.out.print(C.getName() + " | ");
            }
            System.out.println();
        }
        System.out.println();
    }

    /**
     * Takes the revealed card and adds it to the players hand
     * Removes card from the players possible list of cards
     * @param card Card that was revealed to the user
     */
    public void reveal(Card card){
        setHand(card);
        Card removeCard = null;
        for(ArrayList<Card> suggestion : possibleCards){
            for(Card suggestedCard : suggestion){
                if(suggestedCard.getName().equals(card.getName())){
                    removeCard = suggestedCard;
                }
            }
            if(removeCard != null){
                suggestion.remove(removeCard);
            }
        }
    }

    /**
     * Adds array list of cards to the players possible list of cards
     * @param cards List of three suggested cards , one of which was revealed by the player
     */
    public void witness(ArrayList<Card> cards){
        ArrayList<Card> possible = new ArrayList<>();
        for(Card c : cards){
            if(!impossibleCards.contains(c)){
                possible.add(c);
            }
        }
        possibleCards.add(possible);
    }

    /**
     * Takes the passed card and sets it to be impossible for this user to have
     * Removes card from the players possible list of cards
     * @param card Card that was passed by the player
     */
    public void pass(Card card){
        setImpossible(card);
    }

    //
    // FOR TESTING PURPOSES
    //

    /**
     * Check to see if opposing player's hand contains a certain card
     * @param handCard The card being checked
     * @return true if hand contains card
     */
    public boolean handContains(Card handCard) {
        for(Card c : hand){
            if(c.getName() == handCard.getName()){
                return true;
            }
        }
        return false;
    }

    /**
     * Check to see if opposing player's hand contains a certain card
     * @param handCard The card being checked
     * @return true if hand contains card
     */
    public boolean impossibleContains(Card handCard) {
        for(Card c : impossibleCards){
            if(c.getName() == handCard.getName()){
                return true;
            }
        }
        return false;
    }
}
