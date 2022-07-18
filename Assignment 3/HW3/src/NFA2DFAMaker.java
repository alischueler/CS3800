import javax.lang.model.type.ArrayType;
import java.util.*;

public class NFA2DFAMaker {
    public NFA nfa;
    public LinkedHashSet<String> newStates;
    public LinkedHashSet<String> accept;
    public LinkedHashSet<String> alpha;
    public LinkedHashMap<String, LinkedHashMap<String, String>> newTrans;
    public String newStart;
    public LinkedHashMap<String, String> idName;

    public NFA2DFAMaker(NFA nfa) {
        this.nfa = nfa;
        createStates(nfa.getStates());
        createInitial(nfa.getStart());
        createFinal(nfa.getAccept(), newStates);
        createAlpha(nfa.getAlphabet());
        createidName(newStates);
        createTrans(nfa.getDelta(), newStates);
    }

    private void createidName(LinkedHashSet<String> newStates) {
        LinkedHashMap<String, String> toret = new LinkedHashMap<>();
        List<String> statesList = new ArrayList<>();
        statesList.addAll(newStates);
        for (String s : statesList) {
            toret.put(s, s);
        }
        idName = toret;
    }

    private void createAlpha(LinkedHashSet<String> alpha) {
        LinkedHashSet<String> alphaNew = new LinkedHashSet<>();
        for (String s : alpha) {
            if (!s.equals("empty")) {
                alphaNew.add(s);
            }
        }
        this.alpha = alphaNew;
    }

    private void createTrans(LinkedHashMap<String, LinkedHashMap<String, LinkedHashSet<String>>> delta,
                             LinkedHashSet<String> newStates) {
        LinkedHashMap<String, LinkedHashMap<String, String>> newDelta = new LinkedHashMap<>();
        List<String> NewalphaList = new ArrayList<>();
        NewalphaList.addAll(alpha);
        List<String> statesList = new ArrayList<>();
        statesList.addAll(newStates);
        LinkedHashMap<String, LinkedHashSet<String>> empties = findEmpties(delta);
        for (int i = 0; i < statesList.size(); i++) {
            String id = findID(statesList.get(i), idName);
            LinkedHashMap<String, String> toAdd = new LinkedHashMap<>();
            newDelta.put(id, toAdd);
        }

        for (int j = 0; j < NewalphaList.size(); j++) {
            for (String s : statesList) {
                if (s.equals("()")) {
                    LinkedHashMap<String, String> toAdd = newDelta.get(s);
                    toAdd.put(NewalphaList.get(j), s);
                    newDelta.replace(s, toAdd);

                } else {
                    String total = s.substring(s.indexOf("(") + 1, s.indexOf(")"));
                    String[] broken = total.split(" ");
                    StringBuilder sb = new StringBuilder();

                    for (int i = 0; i < broken.length; i++) {
                        String startState = broken[i];
                        String id = findID(startState, nfa.getIdName());
                        if (delta.get(id).containsKey(NewalphaList.get(j))) {
                            LinkedHashSet<String> points = delta.get(id).get(NewalphaList.get(j));
                            for (String end : points) {
                                String endName = nfa.getIdName().get(end);
                                if (empties.containsKey(end)) {
                                    sb.append(" ");
                                    LinkedHashSet<String> epsilonTransition = empties.get(end);
                                    for (String e : epsilonTransition) {
                                        sb.append(nfa.getIdName().get(e));
                                        sb.append(" ");
                                    }
                                }
                                sb.append(" ");
                                sb.append(endName);
                            }
                            String totalEnd = findEnd(sb.toString(), newStates);
                            LinkedHashMap<String, String> toAdd = newDelta.get(s);
                            toAdd.put(NewalphaList.get(j), totalEnd);
                            newDelta.replace(s, toAdd);
                        }
                        if (sb.toString().length()==0) {
                            LinkedHashMap<String, String> toAdd = newDelta.get(s);
                            toAdd.put(NewalphaList.get(j), "()");
                            newDelta.replace(s, toAdd);
                        }
                    }
                }
            }
        }
        newTrans = newDelta;
    }

    private String findEnd(String want, LinkedHashSet<String> newStates) {
        String[] wantSpl = want.split(" ");
        String toRet = "";
        for (String s : newStates) {
            String use = s;
            if (!s.equals("()")) {
                use = s.substring(s.indexOf("(") + 1, s.indexOf(")"));
            }
            String[] split = use.split(" ");
            boolean thisState = true;
            for (String sSplit : split) {
                if (!want.contains(sSplit)) {
                    thisState = false;
                    break;
                }
            }
            if (thisState) {
                boolean match = true;
                for (String w : wantSpl) {
                    if (!s.contains(w)) {
                        match = false;
                    }
                }
                if (match) {
                    return s;
                }
            }
        }
        return toRet;
    }

    private LinkedHashMap<String, LinkedHashSet<String>> findEmpties(
            LinkedHashMap<String, LinkedHashMap<String, LinkedHashSet<String>>> delta) {
        LinkedHashMap<String, LinkedHashSet<String>> toRet = new LinkedHashMap<>();
        for (Map.Entry<String, LinkedHashMap<String, LinkedHashSet<String>>> m : delta.entrySet()) {
            String start = m.getKey();
            LinkedHashMap<String, LinkedHashSet<String>> val1 = m.getValue();
            if (val1.containsKey("empty")) {
                LinkedHashSet<String> endStates = val1.get("empty");
                toRet.put(start, endStates);
            }
        }
        return toRet;
    }

    private void createFinal(LinkedHashSet<String> Oldaccept, LinkedHashSet<String> newStates) {
        LinkedHashSet<String> acceptList = new LinkedHashSet<>();
        for (String s : Oldaccept) {
            for (String s2 : newStates) {
                if (s2.contains(s)) {
                    acceptList.add(s2);
                }
            }
        }
        this.accept = acceptList;
    }

    private void createInitial(String start) {
        this.newStart = "("+start+")";
    }

    private void createStates(LinkedHashSet<String> states) {
        LinkedHashSet<String> statesList = new LinkedHashSet<>();
        for (String s : states) {
            statesList.add(s);
        }
        List<String> statesListList = new ArrayList<>();
        statesListList.addAll(statesList);
        String[] arr = new String[statesListList.size()];
        for (int i = 0; i < statesListList.size(); i++) {
            arr[i] = statesListList.get(i);
        }
        powerset p = new powerset();
        Set<Set<String>> toPrint = p.findPower(arr);
        List<String> toPrintString = p.changeString(toPrint);
        LinkedHashSet<String> po = new LinkedHashSet<>();
        for (String s : toPrintString) {
            StringBuilder sb = new StringBuilder();
            sb.append("(");
            sb.append(s);
            sb.append(")");
            po.add(sb.toString());
        }
        newStates = po;
    }


    public DFA makeDFA() {
        List<List<String>> stoRet = new ArrayList<>();
        List<String> statesList = new ArrayList<>();
        statesList.addAll(newStates);
        stoRet.add(statesList);
        List<String> startList = new ArrayList<>();
        startList.add(newStart);
        stoRet.add(startList);
        List<String> acceptList = new ArrayList<>();
        acceptList.addAll(accept);
        stoRet.add(acceptList);
        List<String> trans = transToString();
        DFA toRet = new DFAMaker(stoRet, trans, true);
        toRet.setIDName(idName);
        return toRet;
    }

    public static String findID(String name, Map<String, String> idName) {
        String s = "";
        for (Map.Entry m : idName.entrySet()) {
            if (name.equals(m.getValue())) {
                s = (String) m.getKey();
            }
        }
        return s;
    }


    public List<String> transToString() {
        List<String> toRet = new ArrayList<>();
        LinkedHashSet<String> symbols = new LinkedHashSet<>();
        Iterator<Map.Entry<String, LinkedHashMap<String, String>>> i = newTrans.entrySet().iterator();
        while (i.hasNext()) {
            Map.Entry<String, LinkedHashMap<String, String>> mapElement = i.next();
            String currState = mapElement.getKey();
            LinkedHashMap<String, String> inner = mapElement.getValue();
            Iterator<Map.Entry<String, String>> innerI = inner.entrySet().iterator();
            while (innerI.hasNext()) {
                Map.Entry<String, String> innerVals = innerI.next();
                String sym = innerVals.getKey();
                String toState = innerVals.getValue();
                StringBuilder indiv = new StringBuilder();
                indiv.append(currState);
                indiv.append(" ; ");
                indiv.append(toState);
                indiv.append(" ");
                indiv.append(sym);
                symbols.add(sym);
                toRet.add(indiv.toString());
            }
        }
        StringBuilder syms = new StringBuilder();
        for (String s : symbols) {
            syms.append(s);
            syms.append(" ");
        }
        toRet.add(syms.toString());
        return toRet;

    }
}
