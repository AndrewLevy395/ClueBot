/*******************************************************************************
 * ClueBot
 *
 * Andrew Levy, Austin Ingarra
 *******************************************************************************/

package cluebot;

import java.util.ArrayList;
import java.util.Arrays;
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
        prepareGame();
        System.out.println();
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
            System.out.println("What is the next event? (Type \"help\" for options)");
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
                case "remaining":
                    ClueBot.remain();
                    break;
                case "display":
                    ClueBot.display();
                    break;
                case "undo":
                    ClueBot.undo();
                    break;
                case "help":
                    System.out.println("Type one of these options:");
                    System.out.println("Reveal - an opposing player reveals a card to you after you make a suggestion");
                    System.out.println("Witness - witness an opposing player reveal a card to another opposing player after they make a suggestion");
                    System.out.println("Remaining - shows a list of the remaining cards with unknown alibi");
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
     * Prepares the list of cards as well as the initial cluebot logic
     */
    public static void prepareGame() {
        addToCardSet(suspects, "suspect");
        addToCardSet(weapons, "weapon");
        addToCardSet(rooms, "room");
        cluebotlogic = new ClueBotLogic(suspects, weapons, rooms);
    }

    /**
     * Method that runs when an opposing player reveals a card to the user
     * Asks for the card and the opposing player and updates the opposing players hand
     */
    public static void reveal() {
        Card revealCard = null;
        while(revealCard == null) {
            revealCard = retrieveCardFromUser("Which card was revealed to you?");
            if(revealCard == null){
                System.out.println("Invalid card name");
            }
        }

        OppPlayer revealPlayer = null;
        while(revealPlayer == null) {
            revealPlayer = retrieveOppPlayerFromUser("Which opposing player revealed that card to you?");
            if(revealPlayer == null){
                System.out.println("Invalid player name");
            } else {
                for(OppPlayer op : players){
                    if(op.getName() != revealPlayer.getName()){
                        op.setImpossible(revealCard);
                    }
                }
                revealPlayer.reveal(revealCard);
            }
        }
    }

    /**
     * Method that runs when the user witnesses an opposing player reveal to another opposing player
     * Asks for the three cards revealed as well as the player that did the revealing
     */
    public static void witness(){
        OppPlayer witnessPlayer = null;
        while(witnessPlayer == null) {
            witnessPlayer = retrieveOppPlayerFromUser("Which opposing player did you witness reveal a card to another player? (Who showed the card?)");
            if(witnessPlayer == null){
                System.out.println("Invalid player name");
            }
        }
        Integer witnessCount = 0;
        ArrayList<Card> witnessList = new ArrayList<>();
        Card witnessCard = null;
        String witnessType = null;
        while(witnessCount < 3) {
            switch(witnessCount){
                case 0:
                    witnessCard = retrieveCardFromUser("Which suspect was revealed?");
                    witnessType = "suspect";
                    break;
                case 1:
                    witnessCard = retrieveCardFromUser("Which weapon was revealed?");
                    witnessType = "weapon";
                    break;
                case 2:
                    witnessCard = retrieveCardFromUser("Which room was revealed?");
                    witnessType = "room";
                    break;
                default:
                    System.out.println("How did you even get to this error?");
            }
            if(witnessCard == null){
                System.out.println("Invalid card name of type " + witnessType);
            } else {
                if(witnessCard.getType() != witnessType){
                    witnessCard = null;
                } else {
                    Boolean possible = true;
                    for(Card c : hand){
                        if(witnessCard == c){
                            possible = false;
                        }
                    }
                    if(possible){
                        witnessList.add(witnessCard);
                    }
                    witnessCount++;
                }
            }
        }
        witnessPlayer.witness(witnessList);
    }

    /**
     * Method that runs when the user witnesses an opposing player pass on revealing to another opposing player
     * Asks for the three cards passed on and updates the list of impossible cards for that player
     */
    public static void pass(){
        OppPlayer passPlayer = null;
        while(passPlayer == null) {
            passPlayer = retrieveOppPlayerFromUser("Which opposing player passed?");
            if(passPlayer == null){
                System.out.println("Invalid player name");
            }
        }
        Integer passCount = 0;
        Card passCard = null;
        String passType = null;
        while(passCount < 3) {
            switch(passCount){
                case 0:
                    passCard = retrieveCardFromUser("Which suspect was passed on?");
                    passType = "suspect";
                    break;
                case 1:
                    passCard = retrieveCardFromUser("Which weapon was passed on?");
                    passType = "weapon";
                    break;
                case 2:
                    passCard = retrieveCardFromUser("Which room was passed on?");
                    passType = "room";
                    break;
                default:
                    System.out.println("How did you even get to this error?");
            }
            if(passCard == null){
                System.out.println("Invalid card name of type " + passType);
            } else {
                if(passCard.getType() != passType){
                    passCard = null;
                } else if(hand.contains(passCard)){
                    passCount++;
                } else {
                    passPlayer.pass(passCard);
                    passCount++;
                }
            }
        }
    }

    /**
     * Asks the player which opposing player to display and then displays that player's hand and list of impossible cards
     */
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

    /**
     * Prints all of the cards that are still possible to be guilty
     */
    public static void remain(){
        ArrayList<String> remainingS = new ArrayList<String>(Arrays.asList(suspects));
        ArrayList<String> remainingW = new ArrayList<String>(Arrays.asList(weapons));
        ArrayList<String> remainingR = new ArrayList<String>(Arrays.asList(rooms));
        for(OppPlayer p : players){
            for(Card c : p.getHand()){
                if(remainingS.contains(c.getName())){
                    remainingS.remove(c.getName());
                } else if(remainingR.contains(c.getName())){
                    remainingR.remove(c.getName());
                } else if(remainingW.contains(c.getName())){
                    remainingW.remove(c.getName());
                }
            }
        }
        for(Card h : hand){
            if(remainingS.contains(h.getName())){
                remainingS.remove(h.getName());
            } else if(remainingR.contains(h.getName())){
                remainingR.remove(h.getName());
            } else if(remainingW.contains(h.getName())){
                remainingW.remove(h.getName());
            }
        }
        System.out.println("All Remaining Suspects:");
        for(String s : remainingS){
            System.out.println(s);
        }
        System.out.println();
        System.out.println("All Remaining Weapons:");
        for(String w : remainingW){
            System.out.println(w);
        }
        System.out.println();
        System.out.println("All Remaining Rooms:");
        for(String r : remainingR){
            System.out.println(r);
        }
    }

    /**
     * TODO
     */
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

    /**
     * Retrieves card from user
     * @param message message that will be displayed to user about the card they're inputting
     */
    public static Card retrieveCardFromUser(String message){
        Card card = null;
        System.out.println(message);
        String retrieveCardString = scanner.nextLine().toLowerCase();
        for(Integer j = 0; j < allCards.size(); j++){
            if(allCards.get(j).getName().equals(retrieveCardString)){
                card = allCards.get(j);
            }
        }
        return card;
    }

    /**
     * Retrieves opposing player from user
     * @param message message that will be displayed to user about the opposing player they're inputting
     */
    public static OppPlayer retrieveOppPlayerFromUser(String message){
        OppPlayer player = null;
        System.out.println(message);
        String retrievePlayerString = scanner.nextLine().toLowerCase();
        for(Integer j = 0; j < players.length; j++){
            if(players[j].getName().equals(retrievePlayerString)){
                player = players[j];
            }
        }
        return player;
    }

    //
    // CLASSES FOR TESTING PURPOSES ONLY
    //

    /**
     * Sets the array of players
     * @param playerList array of players to be set
     */
    public static void setPlayers(OppPlayer[] playerList){
        players = playerList;
    }

    /**
     * Gets the array of players
     * @return array of players to be gotten
     */
    public static OppPlayer[] getPlayers(){
        return players;
    }
}
