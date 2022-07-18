public class PDATransition {
    String from;
    String to;
    String read;
    String push;
    String pop;

    public PDATransition(String one, String two, String three, String four, String five) {
        this.from = one;
        this.to = two;
        this.read = three;
        this.pop = four;
        this.push = five;
    }

    public String getFrom() {
        return from;
    }

    public String getTo() {
        return to;
    }

    public String getRead() {
        return read;
    }

    public String getPush() {
        return push;
    }

    public String getPop() {
        return pop;
    }

    @Override
    public String toString() {
        return "from " + from + " to " + to + " read " + read + " pop " + pop + " push " + push;
    }
}
