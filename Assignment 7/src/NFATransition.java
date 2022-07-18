public class NFATransition {
    String from;
    String to;
    String read;

    NFATransition(String one, String two, String three) {
        this.from = one;
        this.to = two;
        this.read = three;
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
}
