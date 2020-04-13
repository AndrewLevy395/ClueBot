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
    private static ClueBotLogic cluebotlogic;
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        ClueBot.addToCardSet(suspects, "suspects");
        ClueBot.addToCardSet(weapons, "weapons");
        ClueBot.addToCardSet(rooms, "rooms");
        cluebotlogic = new ClueBotLogic(allCards);
        System.out.println("Hello and Welcome to ClueBot!");
        System.out.println("How many players? (including yourself)");
        Integer numPlayers = Integer.parseInt(scanner.nextLine());
        players = new OppPlayer[numPlayers - 1];
        for(Integer i = 0; i < numPlayers - 1; i++) {
            System.out.println("Opposing Player " + (i + 1) + " name?");
            OppPlayer player = new OppPlayer(scanner.nextLine().toLowerCase());
            players[i] = player;
        }

        Boolean completeHand = false;
        System.out.println();
        System.out.println("Which cards are in your hand?");
        while(!completeHand){
            System.out.println("Type the name of a card in your hand. Use the last name only for character cards (Type \"done\" when your hand is complete)");
            String addCard = (scanner.nextLine().toLowerCase());
            if (addCard.equals("done")) {
                completeHand = true;
            } else {
                ClueBot.addToHand(addCard);
            }
        }

        Boolean gameOver = false;
        while(!gameOver) {
            System.out.println();
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
                case "display":
                    ClueBot.display();
                    break;
                case "undo":
                    ClueBot.undo();
                    break;
                case "help":
                    System.out.println("Type one of three options:");
                    System.out.println("Reveal - an opposing player reveals a card to you after you make a suggestion");
                    System.out.println("Witness - witness an opposing player reveal a card to another opposing player after they make a suggestion");
                    System.out.println("Display - display the known hand of an opposing player as well as a list of cards that are impossible for them to have");
                    System.out.println("Undo - remove the prior clue");
                    System.out.println("Pass - an opposing player has no cards and must pass during a suggestion");
                    break;
                default:
                    System.out.println("Invalid clue");
            }
            cluebotlogic.calculate(players, hand);
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
        while(!valid) {
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
        while(!valid) {
            System.out.println("Which opposing player revealed that card to you?");
            String revealPlayerString = scanner.nextLine().toLowerCase();
            for(Integer j = 0; j < players.length; j++){
                if(players[j].getName().equals(revealPlayerString)){
                    valid = true;
                    players[j].reveal(revealCard);
                }
            }
            if(!valid){
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

    public static void display(){
        Boolean valid = false;
        while(!valid){
            System.out.println();
            System.out.println("Which opposing player would you like to display?");
            String displayPlayerString = scanner.nextLine().toLowerCase();
            for(Integer j = 0; j < players.length; j++){
                if(players[j].getName().equals(displayPlayerString)){
                    valid = true;
                    players[j].display();
                }
            }
            if(!valid){
                System.out.println("Invalid Player");
            }
        }
    }

    public static void undo(){
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

    /**
     * Adds a given card to the users hand
     * @param cardName Name of the card that the user is adding to their hand
     */
    public static void addToHand(String cardName){
        Boolean valid = false;
        for(Integer i = 0; i < allCards.size(); i++){
            if(cardName.equals(allCards.get(i).getName())){
                hand.add(allCards.get(i));
                for(Integer j = 0; j < players.length; j++){
                    players[j].setImpossible(allCards.get(i));
                }
                valid = true;
            }
        }
        System.out.println();
        if(!valid){
            System.out.println("Invalid Card");
            System.out.println();
        }
        System.out.println("Current Hand:");
        for(Integer iter = 0; iter < hand.size(); iter++){
            System.out.println(hand.get(iter).getName());
        }
        System.out.println();
    }
}
