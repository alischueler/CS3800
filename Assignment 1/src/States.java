import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

public class States {
    public HashSet<State> allStates;

    public States(HashSet<State> allStates) {
        this.allStates = allStates;
    }

    public List<String> convert() {
        List<String> ret = new ArrayList<>();
        Iterator it = allStates.iterator();
        while (it.hasNext()) {
            String s = it.next().toString();
            ret.add(s);
        }
        return ret;
    }
}
