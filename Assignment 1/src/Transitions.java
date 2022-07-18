import java.util.*;

public class Transitions {
    HashMap<State, HashMap<Symbol, State>> transitions;

    public Transitions(HashMap<State, HashMap<Symbol, State>> transitions) {
        this.transitions = transitions;
    }

    public List<String> convert() {
        List<String> ret = new ArrayList<>();
        Iterator<Map.Entry<State, HashMap<Symbol, State>>> it = transitions.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<State,HashMap<Symbol, State>> en = it.next();
            String start = en.getKey().toString();
            HashMap<Symbol, State> val = en.getValue();
            Iterator<Map.Entry<Symbol, State>> it2 = val.entrySet().iterator();
            while(it2.hasNext()) {
                Map.Entry<Symbol, State> inner = it2.next();
                String sym = inner.getKey().toString();
                String end = inner.getValue().toString();
                StringBuilder sb = new StringBuilder();
                sb.append(start);
                sb.append(" ");
                sb.append(sym);
                sb.append(end);
                ret.add(sb.toString());
            }
        }
        return ret;
    }
}
