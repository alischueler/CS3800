import java.util.*;

public class DFAMaker implements DFA {
    HashSet<String> states;
    String start;
    HashSet<String> accept;
    HashSet<String> alphabet;
    LinkedHashMap<String, LinkedHashMap<String, String>> delta;
    Boolean union;
    Map<String, String> idName;

    public DFAMaker(List<String> states, List<String> transitions) {
        union = false;
        setUpStates(states.get(0));
        start = states.get(1);
        setUpAccept(states.get(2));
        setUpTransitions(transitions);
    }

    public DFAMaker(List<String> states, List<String> transitions, Map<String, String> idName) {
        setUpStates(states.get(0));
        start = states.get(1);
        setUpAccept(states.get(2));
        setUpTransitions(transitions);
        this.idName = idName;
    }

    public DFAMaker(List<List<String>> states, List<String> transitions, boolean union) {
        setUpStates(states.get(0));
        start = states.get(1).get(0);
        setUpAccept(states.get(2));
        this.union = union;
        if (union) {
            setUpUnionTransitions(transitions);
        } else {
            setUpTransitions(transitions);
        }
    }

    public void setUpStates(String los) {
        states = new LinkedHashSet<>(los.length());
        String[] spl = los.split(" ");
        for (String s : spl) {
            states.add(s);
        }
    }

    public void setUpStates(List<String> los) {
        states = new LinkedHashSet<>(los.size());
        for (String s : los) {
            states.add(s);
        }
    }

    public void setUpAccept(String los) {
        accept = new HashSet<>(los.length());
        String[] spl = los.split(" ");
        for (String s : spl) {
            accept.add(s);
        }
    }

    public void setUpAccept(List<String> los) {
        accept = new HashSet<>(los.size());
        for (String s : los) {
            accept.add(s);
        }
    }

    //in order of start, sym, end
    public void setUpTransitions(List<String> los) {
        delta = new LinkedHashMap<String, LinkedHashMap<String, String>>();
        alphabet = new HashSet<>();
        for (int i = 0; i < los.size(); i++) {
            String[] broken = los.get(i).split(" ");
            if (i == los.size() - 1) {
                for (String s : broken) {
                    alphabet.add(s);
                }
            } else {
                if (delta.size() > 0) {
                    if (delta.containsKey(broken[0])) {
                        LinkedHashMap<String, String> inner = delta.get(broken[0]);
                        inner.put(broken[2], broken[1]);
                        delta.replace(broken[0], inner);
                    } else {
                        LinkedHashMap<String, String> inner = new LinkedHashMap<>();
                        inner.put(broken[2], broken[1]);
                        delta.put(broken[0], inner);
                    }
                }
                else {
                    LinkedHashMap<String, String> inner = new LinkedHashMap<>();
                    inner.put(broken[2], broken[1]);
                    delta.put(broken[0], inner);
                }
            }
        }
    }

    //in order of start, sym, end
    public void setUpUnionTransitions(List<String> los) {
        delta = new LinkedHashMap<String, LinkedHashMap<String, String>>();
        alphabet = new HashSet<>();
        for (int i = 0; i < los.size(); i++) {
            if (i == (los.size() - 1)) {
                String[] alp = los.get(i).split(" ");
                for (String s : alp) {
                    alphabet.add(s);
                }
            } else {
                String[] broke = los.get(i).split(" ; ");
                String start = broke[0];
                String end;
                String sym;
                sym = broke[1].substring(broke[1].length()-1);
                if (broke[1].contains("()")) {
                    end = "()";
                } else {
                    end = broke[1].substring(0, broke[1].length()-2);
                }

                if (delta.containsKey(start)) {
                    LinkedHashMap<String, String> inner = delta.get(start);
                    inner.put(sym, end);
                    delta.replace(start, inner);
                } else {
                    LinkedHashMap<String, String> inner = new LinkedHashMap<>();
                    inner.put(sym, end);
                    delta.put(start, inner);
                }
            }
        }
    }

    @Override
    public String getStart() {
        return start;
    }

    @Override
    public HashSet<String> getAccept() {
        return accept;
    }

    public HashSet<String> getAlphabet() {
        return alphabet;
    }

    public HashSet<String> getStates() {
        return states;
    }

    @Override
    public LinkedHashMap<String, LinkedHashMap<String, String>> getDelta() {
        return delta;
    }

    @Override
    public Map<String, String> getIdName() {
        return idName;
    }

    @Override
    public void setIDName(Map<String, String> idName) {
        this.idName = idName;
    }
}
