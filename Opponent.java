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

    public void playTurn(Player player, Deck deck) {
        ArrayList<Card> temp = super.hand;
        ArrayList<String> multiples = new ArrayList<String>(); 

        // find which ranks opponent has the most 
        for (Card card : temp) {
            for (Card c : temp) {
                if (card.getName().equals(c.getName())) {
                    if (!multiples.contains(card.getName())){
                        multiples.add(card.getName());
                    }
                }
            }
        }

        String desired = multiples.get(0);
        while (true) {
            System.out.printf("Do you have any %s's? (y/n)\n", desired);
            String answer = super.scnr.nextLine();

            // this checks if the player is lying
            boolean whichCase = answer.toUpperCase().equals("N");
            boolean pl_lying = playerIsLying(answer, player, whichCase);

            if (pl_lying) {
                System.out.println("It is against the games rules to lie. Try again.");
                continue;
            }

            if (answer.toUpperCase().equals("Y")) {
                // search for desired card and move it to opponents hand
                moveCards(player.getHand(), super.hand, desired);
                break;
            }else if (answer.toUpperCase().equals("N")) {
                super.draw(deck);
                System.out.println("darn!");
                break;
            }else {
                System.out.println("Your answer is not valid, try again");
            }
        }
    }

    //NOTE: THIS FUNCTION IS BUGGED
    public boolean playerIsLying(String card, Player pl, boolean checkNoCase) {
        for (Card c : pl.getHand()) {
            if (checkNoCase) {
                if (card.equals(c)) {
                    return true;
                }
            }else {
                if (card.equals(c)) {
                    return false;
                }
            }
       }
        return false;
    }
}
