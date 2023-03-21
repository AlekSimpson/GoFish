import java.util.ArrayList;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scnr = new Scanner(System.in);
        gameloop(scnr);
        scnr.close();
    }

    public static void gameloop(Scanner scnr) {
        clearScreen();
        while (true)  {
            System.out.println("--------------------------------------");
            System.out.println("Would you like to play a game or exit?");
            System.out.println("press p to play");
            System.out.println("press q to quit");
            String answer = scnr.nextLine();

            clearScreen();

            if (answer.toUpperCase().equals("Q")) {
                break;
            }else if  (answer.toUpperCase().equals("P")) {
                startGame(scnr);
            }else {
                System.out.println("That is not a valid option. Please try again.");
            }
        }
    }

    public static void startGame(Scanner scnr) {
        // init objects
        Deck deck = new Deck();
        Player player = new Player(true, scnr);
        Opponent opponent = new Opponent(false, scnr);
        // deal players in
        for (int i = 1; i <= 14; i++) {
            if (i % 2 == 0) {
                player.draw(deck);
            }else {
                opponent.draw(deck);
            }
        }

        String rank = "ERROR";
        String whosTurn = "Your";
        while (checkGameState(deck)) {
            System.out.printf("--- %s turn ---\n", whosTurn);
            if (player.isPlayersTurn) {
                player.displayHand();
                System.out.println("Do you have any...? (type desired rank)");
                rank = scnr.nextLine();
                ArrayList<Card> cardsWon = opponent.checkHandFor(rank);

                if (cardsWon.size() == 0) {
                    player.draw(deck);
                }else {
                    player.addToHand(cardsWon);
                }

                player.isPlayersTurn = false;
                opponent.isPlayersTurn = true;
                whosTurn = "Opponent";
            }else if (opponent.isPlayersTurn) {
                opponent.playTurn(player, deck);

                player.isPlayersTurn = true;
                opponent.isPlayersTurn = false;
                whosTurn = "Your";
            }
        }
    }

    // checks to see if the game should end yet
    public static boolean checkGameState(Deck deck) {
        if (deck.getStackCount() == 0) {
            return false;
        }
        return true;
    }

    public static void clearScreen() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }
}
