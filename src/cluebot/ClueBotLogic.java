/*******************************************************************************
 * ClueBot
 *
 * Copyright ©2020 Andrew Levy, Austin Ingarra
 *******************************************************************************/

package cluebot;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Clue Bot Logic
 * @version Apr 9, 2020
 */
public class ClueBotLogic {
    private static ArrayList<String> suspects;
    private static ArrayList<String> weapons;
    private static ArrayList<String> rooms;

    public ClueBotLogic(String[] s, String[] w, String[] r){
        suspects = new ArrayList<>(Arrays.asList(s.clone()));
        weapons = new ArrayList<>(Arrays.asList(w.clone()));
        rooms = new ArrayList<>(Arrays.asList(r.clone()));
    }

    /**
     * Runs all logic checks to determine guilty cards
     * Prints all guilty cards
     * @param players players in game
     * @param hand user's hand
     */
    public static void calculate(OppPlayer[] players, ArrayList<Card> hand){
        possibleCheck(players);
        ArrayList<Card> confirmed = impossibleCheck(players, hand);
        ArrayList<Card> guilty = remainingCheck(players, hand);
        for(Card c: confirmed) {
            if (!guilty.contains(c)) {
                guilty.add(c);
            }
        }
        if(guilty.size() == 1 || guilty.size() == 2){
            System.out.println("GUILTY FOUND:");
        } else if(guilty.size() > 2){
            System.out.println("ACCUSE!:");
        }

        for(Card g : guilty){
            System.out.println(g.getName());
        }

    }

    /**
     * Runs a check on all of the opposing players impossible cards and determines if any card is on all of the lists
     * @param players List of all of the objects of opposing players
     * @param hand List of user's hand
     * @return A list of common cards between all possible lists (uncommon from player's hand)
     */
    public static ArrayList<Card> impossibleCheck(OppPlayer[] players, ArrayList<Card> hand){
        ArrayList<Card> commonCardsClone = players[0].getImpossibleCards();
        ArrayList<Card> commonCards = new ArrayList<>(commonCardsClone);
        for(Integer iter = 1; iter < players.length; iter++){
            ArrayList<Card> commonNextCardsClone = players[iter].getImpossibleCards();
            ArrayList<Card> commonNextCards = new ArrayList<>(commonNextCardsClone);
            commonCards.retainAll(commonNextCards);
        }
        commonCards.removeAll(hand);
        return commonCards;
    }

    /**
     * Runs a check on all of the opposing players possible cards and determines if any card has been revealed to be in their hand
     * @param players list of all players
     */
    public static void possibleCheck(OppPlayer[] players){
        for(Integer iter = 0; iter < players.length; iter++){
            for(Integer i = 0; i < players[iter].getPossibleCards().size(); i++)
                if(players[iter].getPossibleCards().get(i).size() == 1){
                    players[iter].setHand(players[iter].getPossibleCards().get(i).get(0));
                    for(Integer op = 0; op < players.length; op++){
                        if(op != iter){
                            players[op].setImpossible(players[iter].getPossibleCards().get(i).get(0));
                            System.out.println(players[op].getName() + " set " + players[iter].getPossibleCards().get(i).get(0).getName() + " impossible");
                        }
                    }
                }
        }
    }

    /**
     * Checks to see if there is only one card remaining in a card type
     * @param players list of all players in game
     * @param hand hand of user
     * @return list of all cards that are the last remaining of their kind
     */
    public static ArrayList<Card> remainingCheck(OppPlayer[] players, ArrayList<Card> hand){
        ArrayList<Card> allFound = new ArrayList<>();
        ArrayList<Card> guilty = new ArrayList<>();
        for(OppPlayer p : players){
            for(Card c : p.getHand()){
                allFound.add(c);
            }
        }
        for(Card h : hand){
            allFound.add(h);
        }
        for(Card c : allFound){
            if(c.getType() == "suspect"){
                suspects.remove(c.getName());
            } else if(c.getType() == "weapon"){
                weapons.remove(c.getName());
            } else {
                rooms.remove(c.getName());
            }
        }
        if(suspects.size() == 1){
            guilty.add(new Card(suspects.get(0), "suspect"));
        }
        if(weapons.size() == 1){
            guilty.add(new Card(weapons.get(0), "weapon"));
        }
        if(rooms.size() == 1){
            guilty.add(new Card(rooms.get(0), "room"));
        }
        return guilty;
    }
}
