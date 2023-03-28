import java.util.ArrayList;
import java.util.Scanner;

public class Opponent extends Player {
    ArrayList<String> playerNeeds;
    ArrayList<String> books;
    String previouslyAskedFor;

    public Opponent(boolean isTurn, Scanner scnr) {
        super(isTurn, scnr);
        this.playerNeeds = new ArrayList<String>();
        this.books = new ArrayList<String>();
        this.previouslyAskedFor = "";
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
        presentDebugLog(); 
        // first check if opponent has anything the player needs
        String playerNeed = checkPlayerNeeded();
        // find which ranks opponent has the most 
        getBooks();
        // find highest priority
        String desired = "";
        int randInt = getRandomNumber(0, 100);
        if ((this.books.size() > 0) && (randInt >= 70)) {
            desired = this.books.get(0);
        }else if (this.hand.size() > 0) {
            desired = chooseRandomCard();
        }else {
            super.draw(game.getDeck(), true);
        }
        // checks if opponent can ask for card player previously asked for
        boolean askedPlayerNeed = false;
        for (Card card : this.hand) {
            if (card.getName().equals(playerNeed)) { 
                desired = playerNeed;
                askedPlayerNeed = true;
                System.out.println(askedPlayerNeed);
                break; 
            }
        }

        // checks if opponent is cheating
        boolean oppCheating = true;
        for (Card card : this.hand)  {
            if (card.getName().equals(desired)) {
                oppCheating = false;
                break;
            }
        }
        if (oppCheating || this.previouslyAskedFor == desired) {
            desired = chooseRandomCard();
        }else if (this.previouslyAskedFor == desired && this.hand.size() == 1) {
            desired = this.hand.get(0).getName();
        }

        while (true) {
            if (this.game.getPlayer().getHand().size() == 0) {
                this.game.getPlayer().draw(this.game.getDeck(), true);
            }
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
                if (desired == playerNeed) {
                    this.playerNeeds.remove(playerNeed);
                }
            }else if (answer.toUpperCase().equals("N")) {
                super.draw(game.getDeck(), false);
            }else {
                System.out.println("Your answer is not valid, try again");
                continue;
            }
            break;
        }
        this.previouslyAskedFor = desired;
        updateBookPosition();
    }

    public void updateBookPosition() {
        if (this.books.size() > 1) {
            String tmp = this.books.get(0);                
            this.books.remove(0);
            this.books.add(tmp);
        }
    }

    public String checkPlayerNeeded() {
        ArrayList<String> highPriority = new ArrayList<String>();
        if (this.playerNeeds.size() == 0) { return ""; }
        for (String needs: this.playerNeeds) {
            for (Card card : this.hand) {
                if (card.getName().equals(needs)) {
                    highPriority.add(needs);
                }
            }
        }
        if (highPriority.size() == 0) { return ""; }
        return highPriority.get(0);
    }

    // find which ranks opponent has the most 
    public void getBooks() {
        String[] ranks = {"2", "3", "4", "5", "6", "7", "8", "9", "10", "J", "Q", "K", "A"};
        ArrayList<String> currRanks = new ArrayList<String>();
        for (Card card : this.hand) { currRanks.add(card.getName()); }
        for (String rank : ranks) {
            int firstIdx = currRanks.indexOf(rank);
            int secondIdx = currRanks.lastIndexOf(rank);

            // if firstIdx and secondIdx don't match it means there is at least two of the same
            if (firstIdx != secondIdx) {
                // dont want to have the same rank twice in the books array
                if (!this.books.contains(rank)) {
                    this.books.add(rank);
                }
            }else {
                // previously had book was either stolen or lost from the hand somehow during the game
                // if that happens then the books array needs to update to reflect that
                if (this.books.contains(rank)) {
                    this.books.remove(rank);
                }
            }
        }
    }
    public void addToNeeds(String rank) {
        if (!this.playerNeeds.contains(rank)) {
            this.playerNeeds.add(rank);
        }
    }

    // picks a random card from the deck
    public String chooseRandomCard() {
        String desired = "";

        if (this.hand.size() == 1) {
            return this.hand.get(0).getName();
        }

        int max = 5;
        int i = 0;
        while (true) {
            int randomIdx = getRandomNumber(0, this.hand.size()-1);
            desired = this.hand.get(randomIdx).getName();
            if (!desired.equals(this.previouslyAskedFor) || this.hand.size() <= 3 || i == max) {
                break;
            }    
            i++;
        }
        return desired;
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

    public void presentDebugLog() {
        if (super.game.getDebug()) {
            System.out.println("========================");
            displayHand();
            System.out.print("playerNeeds: ");
            for (String str : this.playerNeeds) {
                System.out.printf("%s, ", str);
            }
            System.out.println();
            System.out.print("books: ");
            for (String book : this.books) {
                System.out.printf("%s, ", book);
            }
            System.out.println();
            System.out.println("========================");
        }
    }
}
