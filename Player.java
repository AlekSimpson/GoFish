import java.util.ArrayList;
import java.util.Scanner;

public class Player {
    ArrayList<Card> hand;
    boolean isPlayersTurn;
    Scanner scnr;

    public Player(boolean isPlayersTurn, Scanner scnr) {
        hand = new ArrayList<Card>();
        this.isPlayersTurn = isPlayersTurn;
        this.scnr = scnr;
    }

    public void draw(Deck deck) {
        Card card = deck.draw();
        hand.add(card);
    }

    public ArrayList<Card> getHand() {
        return this.hand;
    }

    public void displayHand() {
        for (Card card : hand) {
            System.out.printf("|  %s of %s   ", card.getName(), card.getSuit());
        }
        System.out.println();
        System.out.println("-----------------------------");
    }

    public void addToHand(ArrayList<Card> wonCards) {
        for (Card card : wonCards) {
            this.hand.add(card);
        }
    }
}
