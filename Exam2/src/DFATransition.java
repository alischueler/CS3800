public class DFATransition {
    String from;
    String to;
    String read;

    public DFATransition(String from, String to, String read) {
        this.from = from;
        this.to = to;
        this.read = read;
    }

    public DFATransition(DFATransition t) {
        this.from = t.getFrom();
        this.to = t.getTo();
        this.read = t.getRead();
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
