import java.util.ArrayList;
import java.util.Random;
import java.util.Stack;

public class Deck {
    private Stack<Card> stack; 
    private ArrayList<Card> cards;

    public Deck() {
        cards = new ArrayList<Card>();
        stack = new Stack<Card>();
        initCards();
        shuffle();
        shuffle();
        shuffle();
        for (Card c : cards) {
            this.stack.add(c);
        }
    }

    private void initCards() {
        String[] suits = {"Diamonds", "Hearts", "Spades", "Clubs"};
        String[] faces = {"2", "3", "4", "5", "6", "7", "8", "9", "10", "J", "Q", "K", "A"};
        for (String s : suits) {
            for (String face : faces) {
                Card card = new Card(s, face);
                this.cards.add(card);
            }
        }
    }

    public void shuffle() {
        Random rand = new Random();

        for (int i = this.cards.size()-1; i > 0; i--){
            int randIndex = rand.nextInt(i);

            Card temp = this.cards.get(i);
            this.cards.set(i, this.cards.get(randIndex));
            this.cards.set(randIndex, temp);
        } 

        //for (Card c : cards) {
         //   this.stack.add(c);
        //}
    }

    public Card draw() {
        return this.stack.pop();
    }

    public int getStackCount() {
        return this.stack.size();
    }
}
