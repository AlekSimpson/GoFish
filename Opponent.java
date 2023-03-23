import java.util.ArrayList;
import java.util.Scanner;

public class Opponent extends Player {
    ArrayList<String> playerNeeds;

    public Opponent(boolean isTurn, Scanner scnr) {
        super(isTurn, scnr);
        this.playerNeeds = new ArrayList<String>();
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
        // first check if opponent has anything the player needs
        String highPriority = checkPlayerNeeded();
        // find which ranks opponent has the most 
        ArrayList<String> multiples = findMostCommonRanks();
        // find highest priority
        String highestPriority = "";
        if (multiples.size() == 0) {
            highestPriority = findHighestPriorityAsk(highPriority, multiples);
        }else {
            highestPriority = this.hand.get(0).getName();
        }

        String desired = highestPriority;
        System.out.printf("HIGHEST PRIORITY IS: %s\n", highestPriority);
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

    public String findHighestPriorityAsk(String string, ArrayList<String> multiples) {
        for (String str : multiples) {
            if (string.equals(str)) {
                return string;
            }
        }
        return multiples.get(0);
    }

    public String checkPlayerNeeded() {
        ArrayList<String> highPriority = new ArrayList<String>();
        for (String needs: this.playerNeeds) {
            for (Card card : this.hand) {
                if (card.getName().equals(needs)) {
                    highPriority.add(needs);
                }
            }
        }
        return highPriority.get(0);
   }

   public void addToNeeds(String rank) {
       this.playerNeeds.add(rank);
   }

    // find which ranks opponent has the most 
    public ArrayList<String> findMostCommonRanks() {
        ArrayList<Card> temp = super.hand;
        ArrayList<String> multiples = new ArrayList<String>(); 
        for (Card card : temp) {
            for (Card c : temp) {
                if (card.getName().equals(c.getName()) && !multiples.contains(card.getName())) {
                    multiples.add(card.getName());
                }
            }
        }
        return multiples;
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
