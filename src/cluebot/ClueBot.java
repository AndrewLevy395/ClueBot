/*******************************************************************************
 * ClueBot
 *
 * Copyright ©2020 Andrew Levy, Austin Ingarra
 *******************************************************************************/

package cluebot;

import java.util.ArrayList;
import java.util.Scanner;

/**
 * Runs clue bot
 * @version Apr 9, 2020
 */
public class ClueBot
{
    private static OppPlayer[] players; //array of all opposing players
    private static String[] suspects = new String[] {"plum", "white", "scarlet", "green", "mustard", "peacock"};
    private static String[] weapons = new String[] {"knife", "rope", "revolver", "candlestick", "lead pipe", "wrench"};
    private static String[] rooms = new String[] {"hall", "ballroom", "conservatory", "billiard room", "study", "kitchen", "library", "lounge", "dining room"};
    private static ArrayList<Card> allCards = new ArrayList<>(); //list of all cards
    private static ArrayList<Card> hand = new ArrayList<>(); //list of cards in player's personal hand
    private static ClueBotLogic cluebotlogic = new ClueBotLogic();
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        ClueBot.addToCardSet(suspects, "suspects");
        ClueBot.addToCardSet(weapons, "weapons");
        ClueBot.addToCardSet(rooms, "rooms");
        System.out.println("Hello and Welcome to ClueBot!");
        System.out.println("How many players? (including yourself)");
        Integer numPlayers = Integer.parseInt(scanner.nextLine());
        players = new OppPlayer[numPlayers - 1];
        for(Integer i = 0; i < numPlayers - 1; i++) {
            System.out.println("Opposing Player " + (i + 1) + " name?");
            OppPlayer player = new OppPlayer(scanner.nextLine().toLowerCase());
            players[i] = player;
        }

        Boolean gameOver = false;
        while(gameOver == false) {
            System.out.println("What is the next clue? (Type \"help\" for options)");
            String clue = scanner.nextLine();
            switch(clue.toLowerCase()){
                case "reveal":
                    ClueBot.reveal();
                    break;
                case "witness":
                    ClueBot.witness();
                    break;
                case "pass":
                    ClueBot.pass();
                    break;
                case "remove":
                    ClueBot.remove();
                    break;
                case "help":
                    System.out.println("Type one of three options:");
                    System.out.println("Reveal - an opposing player reveals a card to you after you make a suggestion");
                    System.out.println("Witness - witness an opposing player reveal a card to another opposing player after they make a suggestion");
                    System.out.println("Remove - remove a prior clue");
                    System.out.println("Pass - an opposing player has no cards and must pass during a suggestion");
                    break;
                default:
                    System.out.println("Invalid clue");
            }
        }
        scanner.close();
    }

    /**
     * Method that runs when an opposing player reveals a card to the user
     * Asks for the card and the opposing player and updates the opposing players hand
     */
    public static void reveal() {
        Card revealCard = null;

        Boolean valid = false;
        while(valid == false) {
            System.out.println("Which card was revealed to you?");
            String revealCardString = scanner.nextLine().toLowerCase();
            for(Integer j = 0; j < allCards.size(); j++){
                if(allCards.get(j).getName().equals(revealCardString)){
                    revealCard = allCards.get(j);
                    valid = true;
                }
            }
            if(revealCard == null){
                System.out.println("Invalid card name");
            }
        }

        valid = false;
        while(valid == false) {
            System.out.println("Which opposing player revealed that card to you?");
            String revealPlayerString = scanner.nextLine().toLowerCase();
            for(Integer j = 0; j < players.length; j++){
                if(players[j].getName().equals(revealPlayerString)){
                    valid = true;
                    players[j].reveal(revealCard);
                }
            }
            if(valid == false){
                System.out.println("Invalid player name");
            }
        }
    }

    public static void witness(){
        System.out.println("WITNESS");
    }

    public static void pass(){
        System.out.println("PASS");
    }

    public static void remove(){
        System.out.println("REMOVE");
    }

    /**
     * Adds all of the cards of one card type to the list of total cards
     * @param cards String array of all cards of one type
     * @param type The tpye of cards
     */
    public static void addToCardSet(String[] cards, String type){
        for(Integer k = 0; k < cards.length; k++){
            allCards.add(new Card(cards[k], type));
        }
    }
}
