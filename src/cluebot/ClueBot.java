/*******************************************************************************
 * ClueBot
 *
 * Copyright ©2020 Andrew Levy, Austin Ingarra
 *******************************************************************************/

package cluebot;

import java.util.Scanner;

/**
 * Runs clue bot
 * @version Apr 9, 2020
 */
public class ClueBot
{
    static Integer numPlayers;
    static OppPlayer[] players;
    static Boolean gameOver;

    public static void main(String[] args) {
        System.out.println("Hello and Welcome to ClueBot!");
        System.out.println("How many players? (including yourself)");
        Scanner scanner = new Scanner(System.in);
        numPlayers = Integer.parseInt(scanner.nextLine());
        players = new OppPlayer[numPlayers];
        for(Integer i = 0; i < numPlayers - 1; i++) {
            System.out.println("Opposing Player " + (i + 1) + " name?");
            OppPlayer player = new OppPlayer(scanner.nextLine());
            players[i] = player;
        }
        gameOver = false;
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
                case "help":
                    System.out.println("Type one of three options:");
                    System.out.println("Reveal - an opposing player reveals a card to you after you make a suggestion");
                    System.out.println("Witness - witness an opposing player reveal a card to another opposing player after they make a suggestion");
                    System.out.println("Pass - an opposing player has no cards and must pass during a suggestion");
                    break;
                default:
                    System.out.println("Invalid clue");
            }
        }
        scanner.close();
    }

    public static void reveal(){
        System.out.println("REVEAL");
    }

    public static void witness(){
        System.out.println("WITNESS");
    }

    public static void pass(){
        System.out.println("PASS");
    }
}
