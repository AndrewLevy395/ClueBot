/*******************************************************************************
 * ClueBot
 *
 * Andrew Levy, Austin Ingarra
 *******************************************************************************/

package cluebot;

/**
 * Blueprint for a card within the game
 * @version Apr 9, 2020
 */
public class Card {
    private String name;
    private String type;

    /**
     * Create a new card
     * @param name Name of the card
     * @param type Type of card
     */
    public Card(String name, String type) {
        this.name = name;
        this.type = type;
    }

    /**
     * Get the name of the card
     * @return String of the name of the card
     */
    public String getName() {
        return this.name;
    }

    /**
     * Get the type of the card
     * @return String of the type of the card
     */
    public String getType() {
        return this.type;
    }
}
