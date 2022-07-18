public class Symbol {
    String name;

    public Symbol(String s) {
        if (s.length()>1) {
            throw new IllegalArgumentException("must be length 1");
        }
        else {
            this.name = s;
        }
    }

    @Override
    public String toString() {
        return name;
    }
}
