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
        boolean debug = false;
        // init objects
        Deck deck = new Deck();
        Player player = new Player(true, scnr);
        Opponent opponent = new Opponent(false, scnr);
        Game game = new Game(player, opponent, deck, debug);
        player.setGame(game);
        opponent.setGame(game);
        // deal players in 
        dealPlayersIn(player, opponent, deck);

        String rank = "ERROR";
        String whosTurn = "Your";
        while (checkGameState(deck)) {
            printTopBar(game, whosTurn);

            if (player.isPlayersTurn) {
               player.displayHand();
               rank = getPlayerRequestedRank(scnr, player);
               // collecting info for AI player to base decisions off of
               opponent.addToNeeds(rank.toUpperCase());
               ArrayList<Card> cardsWon = opponent.checkHandFor(rank);

                if (cardsWon.size() == 0) {
                    player.draw(deck, true);
                }else {
                    player.addToHand(cardsWon);
                }

                // checks if player has any full books
                player.checkForFullSet();

                player.isPlayersTurn = false;
                opponent.isPlayersTurn = true;
                whosTurn = "Opponent";
            }else if (opponent.isPlayersTurn) {
                opponent.playTurn(game);
                // checks if it has any full books
                opponent.checkForFullSet();

                player.isPlayersTurn = true;
                opponent.isPlayersTurn = false;
                whosTurn = "Your";
                clearScreen();
            }
        }

    }

    public void displayGameResults(Game game) {
        boolean playerWon = game.getPlayer().getPoints() > game.getOpponent().getPoints();
        if (playerWon) {
            System.out.println("You won!");
        }else {
            System.out.println("You lost! Better luck next time.");
        }
    }

    public static String getPlayerRequestedRank(Scanner scnr, Player player) {
        String rank = "";
        while (true) {
            System.out.println("Do you have any...? (type desired rank)");
            rank = scnr.nextLine();
            if (!isValidRankToAskFor(player, rank)) {
                System.out.println("You cannot ask for a rank that you do not have in your hand.\nTry again.");
            }else {
                break;
            }
        }
        return rank;
    }

    public static boolean isValidRankToAskFor(Player player, String rank) {
        for (Card card : player.getHand()) {
            if (card.getName().equals(rank.toUpperCase())) {
                return true;
            }
        }
        return false;
    } 

    // checks to see if the game should end yet
    public static boolean checkGameState(Deck deck) {
        if (deck.getStackCount() == 0) {
            return false;
        }
        return true;
    }

    // clears the program output, similar to "clear" in unix based OS's
    public static void clearScreen() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    // deals players in 
    public static void dealPlayersIn(Player player, Opponent opponent, Deck deck) {
        int dealAmt = 7;
        for (int i = 1; i <= dealAmt*2; i++) {
            if (i % 2 == 0) {
                player.draw(deck, false);
            }else {
                opponent.draw(deck, false);
            }
        }
    }

    public static void printTopBar(Game game, String whosTurn) {
        if (game.debug) {
            System.out.println("[DEBUG INFO]");
            System.out.printf("Player hand count: %d\n", game.getPlayer().getHand().size());
            System.out.printf("Opponent hand count: %d\n", game.getOpponent().getHand().size());
            System.out.printf("Amount left in deck: %d", game.getDeck().getStackCount());
        }
        int size = (game.getPlayer().getHand().size() >= 10) ? 10 : game.getPlayer().getHand().size();
        int entireLength = (size * 7) + ((size + 1)*1);
        // int textLength = (player.isPlayersTurn) ? 11 : 15;
        int textLength = (game.getPlayer().isPlayersTurn) ? 29 : 37;

        int length = (entireLength - textLength)/2;
        length++;
        for (int i = 0; i < length; i++) { System.out.print("-"); }
        int points = (whosTurn.equals("Your")) ? game.getPlayer().getPoints() : game.getOpponent().getPoints();
        System.out.printf(" %s turn | %s points: %d | %d left in deck", whosTurn, whosTurn, points, game.getDeck().getDeckSize());
        for (int i = 0; i < length; i++) { System.out.print("-"); }
        System.out.println();
    }
}
