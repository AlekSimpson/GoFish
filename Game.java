public class Game {
    private Player player;
    private Opponent opponent;
    private Deck deck;
   
    public Game(Player pl, Opponent opp, Deck deck) {
        this.player = pl;
        this.opponent = opp;
        this.deck = deck;
    }

    public Player getPlayer() {
        return this.player;
    }

    public Opponent getOpponent() {
        return this.opponent;
    }

    public Deck getDeck() {
        return this.deck;
    }

    public void clearScreen() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }
}
