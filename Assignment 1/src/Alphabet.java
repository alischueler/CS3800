import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

public class Alphabet {
    HashSet<Symbol> allStrings;

    public Alphabet(HashSet<Symbol> allStrings) {
        this.allStrings = allStrings;
    }

    public List<String> convert() {
        List<String> ret = new ArrayList<>();
        Iterator it = allStrings.iterator();
        while (it.hasNext()) {
            String s = it.next().toString();
            ret.add(s);
        }
        return ret;
    }

}
