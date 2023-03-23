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
        //make sublists
        ArrayList<ArrayList<Card>> rows = new ArrayList<ArrayList<Card>>();

        int chunks = (int)Math.floor(this.hand.size() / 10);
        chunks = (chunks == 0) ? 1 : chunks; 
        int start = 0;
        int end = (this.hand.size() < 10) ? this.hand.size() : 10;
        for (int i = 0; i < chunks; i++) {
            ArrayList<Card> row = new ArrayList<Card>(this.hand.subList(start, end));
            rows.add(row);
            start = end;
            end+=10;
        }

        if (this.hand.size() > 10) {
            int amtRemaining = (this.hand.size() % 10);
            end-=10;
            start = (this.hand.size()-amtRemaining);
            end = (end+amtRemaining);
            ArrayList<Card> r = new ArrayList<Card>(this.hand.subList(start, end));
            rows.add(r);
        }

        for (ArrayList<Card> row : rows) {
            makeRow(row);
        }

        int size = (this.hand.size() >= 10) ? 10 : this.hand.size();
        int entireLength = (size * 7) + ((size + 1)*1);
        for (int i = 0; i < entireLength; i++) { System.out.print("-"); }
        System.out.println();
    }

    public void makeRow(ArrayList<Card> row) {
        /*
         * |K----|
         * |  *  |
         * |____K|
         */

        System.out.println(make(row, " |%s---|", " |%s----|"));
        System.out.println(makeMiddle(row));
        System.out.println(make(row, " |___%s|", " |____%s|"));
    }

    public String make(ArrayList<Card> cards, String formOne, String formTwo) {
        String total = "";
        for (Card card : cards) {
            String str = (card.getName().equals("10")) ? formOne : formTwo;
            total+= String.format(str, card.getName());
        }
        return total;
 
    }

    public String makeMiddle(ArrayList<Card> cards) {
        Map<String, String> suitsUnicode = Map.of("Spades", "\u2660", "Hearts", "\u2665", "Diamonds", "\u2666", "Clubs", "\u2663");
        String total = "";

        for (Card card : cards) {
            String str = String.format(" |  %s  |", suitsUnicode.get(card.getSuit()));
            total+=str;
        }

        return total;
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
