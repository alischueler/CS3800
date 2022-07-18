import java.util.*;

public class NFAMaker implements NFA {
    LinkedHashSet<String> states;
    String start;
    LinkedHashSet<String> accept;
    LinkedHashSet<String> alphabet;
    LinkedHashMap<String, LinkedHashMap<String, LinkedHashSet<String>>> delta;
    Map<String, String> idName;
    boolean epsilon;

    public NFAMaker(List<String> states, List<String> transitions) {
        setUpStates(states.get(0));
        start = states.get(1);
        setUpAccept(states.get(2));
        setUpTransitions(transitions);
    }

    public NFAMaker(List<String> states, List<String> transitions, Map<String, String> idName) {
        this.idName = idName;
        setUpStates(states.get(0));
        start = states.get(1);
        setUpAccept(states.get(2));
        setUpTransitions(transitions);
    }

    public void setUpStates(String los) {
        states = new LinkedHashSet<>(los.length());
        String[] spl = los.split(" ");
        for (int i =0; i<spl.length;i++) {
            checkExist(spl[i], i, states);
            updateMap(spl[i], states);
        }
    }

    public void updateMap(String og, HashSet<String> find) {

        String[] elements = new String[find.size()];
        // Convert LinkedHashSet to Array
        elements = find.toArray(elements);
        // Get the last element with the help of the index.
        String lastEle = elements[elements.length - 1];
        for (Map.Entry m : idName.entrySet()) {
            if (m.getValue().equals(og)) {
                idName.replace((String)m.getKey(), lastEle);
            }
        }
    }


    public void setUpAccept(String los) {
        accept = new LinkedHashSet<>(los.length());
        String[] spl = los.split(" ");
        for (String s : spl) {
            accept.add(s);
        }
    }

    public void checkExist(String s, int i, LinkedHashSet<String> states) {
        if (states.contains(s)) {
            StringBuilder sb = new StringBuilder();
            sb.append(s);
            sb.append(i);
            checkExist(sb.toString(), i, states);
        }
        else {
            states.add(s);
        }
    }

    //in order of start, sym, end
    public void setUpTransitions(List<String> los) {
        delta = new LinkedHashMap<String, LinkedHashMap<String, LinkedHashSet<String>>>();
        alphabet = new LinkedHashSet<>();
        for (int i = 0; i < los.size(); i++) {
            String[] broken = los.get(i).split(" ");
            if (i == los.size() - 1) {
                for (String s : broken) {
                    if (s.equals("empty")) {
                        epsilon = true;
                    }
                    else {
                        epsilon = false;
                    }
                    alphabet.add(s);
                }
            } else {
                if (delta.size() > 0) {
                    if (delta.containsKey(broken[0])) {
                        LinkedHashMap<String, LinkedHashSet<String>> inner = delta.get(broken[0]);
                        LinkedHashSet<String> toAdd;
                        if (inner.containsKey(broken[2])) {
                            toAdd = inner.get(broken[2]);
                            toAdd.add(broken[1]);
                            inner.replace(broken[2],toAdd);
                        }
                        else {
                            toAdd = new LinkedHashSet<>();
                            toAdd.add(broken[1]);
                            inner.put(broken[2],toAdd);
                        }
                        delta.replace(broken[0], inner);
                    }
                    else {
                        LinkedHashMap<String, LinkedHashSet<String>> inner = new LinkedHashMap<>();
                        LinkedHashSet<String> toAdd = new LinkedHashSet<>();
                        toAdd.add(broken[1]);
                        inner.put(broken[2], toAdd);
                        delta.put(broken[0], inner);
                    }
                }
                else {
                    LinkedHashMap<String, LinkedHashSet<String>> inner = new LinkedHashMap<>();
                    LinkedHashSet<String> toAdd = new LinkedHashSet<>();
                    toAdd.add(broken[1]);
                    inner.put(broken[2], toAdd);
                    delta.put(broken[0], inner);
                }
            }
        }
    }


    @Override
    public LinkedHashSet<String> getStates() {
        return states;
    }

    @Override
    public String getStart() {
        return start;
    }

    @Override
    public LinkedHashSet<String> getAccept() {
        return accept;
    }

    @Override
    public LinkedHashSet<String> getAlphabet() {
        return alphabet;
    }

    @Override
    public LinkedHashMap<String, LinkedHashMap<String, LinkedHashSet<String>>> getDelta() {
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

    @Override
    public LinkedHashMap<String, LinkedHashMap<String, LinkedHashSet<String>>> updateSymbols(LinkedHashMap<String, String> oldToNew) {
        LinkedHashMap<String, LinkedHashMap<String, LinkedHashSet<String>>> newDelta = new LinkedHashMap<>();
        for (Map.Entry<String, LinkedHashMap<String, LinkedHashSet<String>>> m : delta.entrySet()) {
            String start = m.getKey();
            LinkedHashMap<String, LinkedHashSet<String>> inner = m.getValue();
            LinkedHashMap<String, LinkedHashSet<String>> newInner = new LinkedHashMap<>();
            for (Map.Entry<String, LinkedHashSet<String>> inner2 : inner.entrySet()) {
                String sym = inner2.getKey();
                String newSym = oldToNew.get(sym);
                LinkedHashSet<String> end = inner2.getValue();
                newInner.put(newSym, end);
            }
            newDelta.put(start, newInner);
        }
        return newDelta;
    }
}
