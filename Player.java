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

    public void draw(Deck deck, boolean verbose) {
        Card card = deck.draw();
        if (verbose)
        System.out.printf("[DREW %s of %s]\n", card.getName(), card.getSuit());
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

        int rows = (int) Math.ceil(this.hand.size()/10.0);
        int start = 0;
        int end = (this.hand.size() > 10) ? 10 : this.hand.size();

        for (int i = 1; i <= rows; i++) {
            for (int j = start; j < end; j++) {
                Card card = this.hand.get(j);
                String str = (card.getName().equals("10")) ? " |%s---|" : " |%s----|";
                System.out.printf(str, card.getName());
            }
            System.out.println();
            for (int j = start; j < end; j++) {
                Card card = this.hand.get(j);
                System.out.printf(" |  %s  |", suitsUnicode.get(card.getSuit()));
            }
            System.out.println();
            for (int j = start; j < end; j++) {
                Card card = this.hand.get(j);
                String str = (card.getName().equals("10")) ? " |___%s|" : " |____%s|";
                System.out.printf(str, card.getName());
            }
            System.out.println();
            //----
            start = end;
            end+=10;
        }

        // Draw bottom seperator
        int len = (hand.size() * 7) + ((hand.size() + 1)*1)+1;
        int length = (hand.size() <= 10) ? len : 81; 
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
                System.out.printf("[POINT EARNED - FOUND 4: %s]\n", collected.get(0).getName());
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
