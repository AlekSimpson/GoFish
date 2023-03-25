import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class Opponent extends Player {
    ArrayList<String> playerNeeds;
    private int age;

    public Opponent(boolean isTurn, Scanner scnr) {
        super(isTurn, scnr);
        this.playerNeeds = new ArrayList<String>();
        this.age = 0;
    }

    public ArrayList<Card> checkHandFor(String rank) {
        ArrayList<Card> matches = new ArrayList<Card>();
        //search for cards and find matches
        moveCards(super.hand, matches, rank.toUpperCase());

        if (matches.size() == 0) {
            System.out.println("Go fish!");
        }else {
            System.out.printf("I have %d %s's\n", matches.size(), rank);
        }
        return matches;
    }  

    public void playTurn(Game game) {
        System.out.println("======================");
        //displayHand();
        for (String str : this.playerNeeds) {
            System.out.printf("%s, ", str);
        }
        System.out.println();
        System.out.println("======================");
        String desired = whatToAskFor();
        while (true) {
            System.out.printf("Do you have any %s's? (y/n)\n", desired);
            String answer = super.scnr.nextLine();

            // this checks if the player is lying
            boolean whichCase = answer.toUpperCase().equals("N");
            boolean pl_lying = playerIsLying(desired, game.getPlayer(), whichCase);

            if (pl_lying) {
                System.out.println("It is against the games rules to lie. Try again.");
                continue;
            }

            if (answer.toUpperCase().equals("Y")) {
                // search for desired card and move it to opponents hand
                moveCards(game.getPlayer().getHand(), super.hand, desired);
                break;
            }else if (answer.toUpperCase().equals("N")) {
                super.draw(game.getDeck(), false);
                System.out.println("darn!");
                break;
            }else {
                System.out.println("Your answer is not valid, try again");
            }
        }
    }

    public String whatToAskFor() {
        // checks for books
        ArrayList<String> books = getBooks();
        // System.out.println(books.get(0));
        Random rnd = new Random();
        int index = rnd.nextInt(0, this.hand.size()-1);
        // System.out.printf("index: %d\n", index);
        String retVal = this.hand.get(index).getName();
        // else ask for book card
        if (books.size() > 0) {
            retVal = books.get(0);
        }
        // check what player needs
        if (this.playerNeeds.size() > 0) {
            String pNeeds = this.playerNeeds.get(0);
            for (Card card : super.hand) {
                if (card.getName().equals(pNeeds)) {
                    retVal = pNeeds;
                    break;
                }
            }
        }
        // remove last element from playerNeeds if age == 3
        if (this.age == 3) {
            if (this.playerNeeds.size() > 0) {
                this.playerNeeds.remove(this.playerNeeds.size()-1);
            }
            this.age = 0;
        }else {
            this.age++;
        }
        return retVal;
    }

    // find which ranks opponent has the most 
    public ArrayList<String> getBooks() {
        String[] ranks = {"2", "3", "4", "5", "6", "7", "8", "9", "10", "J", "Q", "K", "A"};
        ArrayList<String> currRanks = new ArrayList<String>();
        ArrayList<String> books = new ArrayList<String>(); 
        for (Card card : this.hand) { currRanks.add(card.getName()); }

        for (String rank : ranks) {
            if (currRanks.indexOf(rank) != currRanks.lastIndexOf(rank)) {
                books.add(rank);
            } 
        }

        return books;
    }

    public void addToNeeds(String rank) {
       this.playerNeeds.add(rank);
    }

    // checks if player is not lying about what cards they have in their hand
    public boolean playerIsLying(String desired, Player pl, boolean playerSaidNo) {
        // ask for card x -> pl says no  -> we expect no card x's in pl.hand
        // ask for card x -> pl says yes -> we expect pl.hand to have at least one card x
        boolean retVal = false;

        if (playerSaidNo) {
            for (Card c : pl.getHand()) {
                if (c.getName().equals(desired)) {
                    // player's hand does have desired card, but player answered no when asked if they had it
                    retVal = true;
                }
            }
        }else { // player said yes
            boolean doesHave = false;
            for (Card c : pl.getHand()) {
                if (c.getName().equals(desired)) {
                    doesHave = true;
                }
            }
            retVal = !doesHave;
        }

        return retVal;
    }
}
