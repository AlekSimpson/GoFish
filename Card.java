public class Card {
    private String suit;
    private String name;
    private int value;

    public Card(String suit, String name) {
        this.suit = suit;
        this.name = name;
        setValue();
    }

    public void setValue() {
        try {
            this.value = Integer.parseInt(this.name);
        }catch(Exception e) {
            this.value = 10;
            if (this.name.equals("A")) {
                this.value = 11;
            }
        }
    }

    public String getSuit() {
        return this.suit;
    }

    public String getName() {
        return this.name;
    }

    public int getValue() {
        return this.value;
    }
}
