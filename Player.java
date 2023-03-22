import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Player {
    ArrayList<Card> hand;
    int points;
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
        int startIdx = 0;
        int amtDisplayed = (this.hand.size() <= 10) ? this.hand.size() : 10;
        System.out.println(amtDisplayed);
        while (true) {
            for (int i = startIdx; i < amtDisplayed; i++) {
                String str = (this.hand.get(i).getName().equals("10")) ? " |%s---|" : " |%s----|";
                System.out.printf(str, this.hand.get(i).getName());
            }
            System.out.print("GETTING HERE");
            System.out.println();
            for (int i = startIdx; i < amtDisplayed; i++) {
                System.out.printf(" |  %s  |", suitsUnicode.get(this.hand.get(i).getSuit()));
            }
            System.out.println();
            for (int i = startIdx; i < amtDisplayed; i++) {
                String str = (this.hand.get(i).getName().equals("10")) ? " |___%s|" : " |____%s|";
                System.out.printf(str, this.hand.get(i).getName());
            }

            System.out.println();

            if (this.hand.size() - amtDisplayed <= 0) {
                break;
            }
            amtDisplayed+=10;
            startIdx+=10;
        }

        // Draw bottom seperator
        int length = (hand.size() * 7) + ((hand.size() + 1)*1);
        for (int i = 0; i < length; i++) {
            System.out.print("-");
        }
        
        System.out.println();
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
                System.out.println("[POINT EARNED]");
                this.points++;

                for (Card card : collected) {
                    this.hand.remove(card);
                }
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

    public int getPoints() {
        return this.points;
    }
}
