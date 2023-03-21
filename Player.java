import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class Player {
    ArrayList<Card> hand;
    ArrayList<ArrayList<Card>> sets;
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
        Map<String, String> suitsUnicode = new HashMap<String,String>();
        suitsUnicode.put("Spades", "\u2660");
        suitsUnicode.put("Hearts", "\u2665");
        suitsUnicode.put("Diamonds", "\u2666");
        suitsUnicode.put("Clubs", "\u2663");
        /*
         * |K----|
         * |  *  |
         * |____K|
         */
        for (Card card : hand) {
            System.out.printf("|%s----|\n", card.getName());
            System.out.printf("|  %s  |\n", suitsUnicode.get(card.getSuit()));
            System.out.printf("|____%s|\n", card.getName());
            // System.out.printf("|  %s of %s   ", card.getName(), card.getSuit());
        }
        System.out.println();
        System.out.println("-----------------------------");
    }

    public void addToHand(ArrayList<Card> wonCards) {
        for (Card card : wonCards) {
            this.hand.add(card);
        }
    }

    // checks if player has collected all four of a certain rank
    public void checkForFullSet() {
        String[] ranks = {"2", "3", "4", "5", "6", "7", "8", "9", "10", "J", "Q", "K", "A"};
        Map<String, ArrayList<Card>> collectedRanks = new HashMap<>();    
        for (String rank : ranks) {
            collectedRanks.put(rank, new ArrayList<Card>());
        }

        for (Card card : this.hand) {
            ArrayList<Card> rankCollection = collectedRanks.get(card.getName());
            rankCollection.add(card);
        }

        for (String rank : ranks) {
            ArrayList<Card> collected = collectedRanks.get(rank);

            if (collected.size() == 4) {
                this.sets.add(collected);
            }

            for (Card card : collected) {
                this.hand.remove(card);
            }
        }
   }

    public void moveCards(ArrayList<Card> iterList, ArrayList<Card> editList, String searchedFor) {
        ArrayList<Card> removed = new ArrayList<Card>();
        for (Card c : iterList) {
            if (c.getName().equals(searchedFor)) {
                editList.add(c);
                removed.add(c);
            }
        }
        for (Card r : removed) {
            iterList.remove(r);
        }
    }
}
