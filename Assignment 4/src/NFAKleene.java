import java.util.*;

public class NFAKleene {

    public static void main(String[] args) {
        xml parserOne = new xml();
        Scanner scan = new Scanner(System.in);
        String fileNames = scan.nextLine();
        String[] spl = fileNames.split(" ");
        List<String> statesOne = parserOne.callParse(spl[0], "states");
        List<String> transitionsOne = parserOne.callParse(spl[0], "transitions");
        Map<String, String> idNameOne = parserOne.idName;
        NFA nfa1 = new NFAMaker(statesOne, transitionsOne, idNameOne);

        NFA nfa2 = kleene(nfa1);
        NFAXML NFAXML = new NFAXML();
        List<String> output = NFAXML.createXML(nfa2, true);
        for (String s : output) {
            System.out.println(s);
        }
    }

    public static NFA kleene(NFA nfa1) {
        String statesOne = convert(nfa1.getStates());
        String statesAccept = convert(nfa1.getAccept());

        List<String> allStates = new ArrayList<>();
        String newStates = fixStates(statesOne, nfa1.getIdName());
        Map<String, String> newidName = fixIdName(newStates);
        allStates.add(newStates);
        String newStart = newStates.split(" ")[newStates.split(" ").length - 1];
        allStates.add(newStart);
        String newAccept = findAccept(statesAccept, newStates.split(" ")[newStates.split(" ").length - 1]);
        allStates.add(newAccept);
        List<String> newTrans = fixTrans(newStates, newStart, nfa1);

        NFA nfa2 = new NFAMaker(allStates, newTrans, newidName);
        return nfa2;
    }

    public static String convert(HashSet<String> set) {
        StringBuilder sb = new StringBuilder();
        for (String s : set) {
            if (sb.length() > 0) {
                sb.append(" ");
            }
            sb.append(s);
        }
        return sb.toString();
    }


    private static String findAccept(String s, String s1) {
        return s + " " + s1;
    }

    private static Map<String, String> fixIdName(String states) {
        Map<String, String> newidName = new LinkedHashMap<>();
        for (String s : states.split(" ")) {
            newidName.put(s, s);
        }
        //newidName.put("0.0", "0.0");
        return newidName;
    }

    public static String fixStates(String states, Map<String, String> newidName) {
        LinkedHashSet<String> set = new LinkedHashSet<>();

        List<String> test = new ArrayList<>();
        String[] spl = states.split(" ");
        for (int i = 0; i < spl.length; i++) {
            test.add(spl[i]);
        }
        checkExist("0.0", 0, test);

        set.addAll(test);

        StringBuilder sb = new StringBuilder();
        for (String s : set) {
            sb.append(s).append(" ");
        }
        return sb.toString();
    }

    public static void updateMap(String og, List<String> find, Map<String, String> newidName) {

        String[] elements = new String[find.size()];
        // Convert LinkedHashSet to Array
        elements = find.toArray(elements);
        // Get the last element with the help of the index.
        String lastEle = elements[elements.length - 1];
        newidName.put(lastEle, lastEle);

    }

    public static void checkExist(String s, int i, List<String> states) {
        if (states.contains(s)) {
            StringBuilder sb = new StringBuilder();
            sb.append(s);
            sb.append(i);
            checkExist(sb.toString(), i, states);
        } else {
            states.add(s);
        }
    }

    public static List<String> fixTrans(String newStates, String newStart, NFA nfa1) {

        //add all old transitions in
        //if a final state, add empty transition to old initial state
        //add empty transition from new initial to old initial
        List<String> toRet = new ArrayList<>();
        LinkedHashSet<String> symbols = new LinkedHashSet<>();

        String[] newArr = newStates.split(" ");

        LinkedHashMap<String, LinkedHashMap<String, LinkedHashSet<String>>> update = transfer(nfa1);
        Map<String, String> oldIdName = nfa1.getIdName();
        // System.out.println(update);
        // System.out.println(nfa1.getAccept());
        for (int i = 0; i < newArr.length; i++) {
            String curr = newArr[i];
            if (nfa1.getAccept().contains(curr)) {
                if (update.containsKey(curr)) {
                    LinkedHashMap<String, LinkedHashSet<String>> currPoints = update.get(curr);
                    if (currPoints.containsKey("empty")) {
                        LinkedHashSet<String> ends = currPoints.get("empty");
                        ends.add(nfa1.getStart());
                        currPoints.replace("empty", ends);
                    } else {
                        LinkedHashSet<String> newHS = new LinkedHashSet<>();
                        newHS.add(nfa1.getStart());
                        currPoints.put("empty", newHS);
                    }
                    update.replace(curr, currPoints);
                }
                else {
                    LinkedHashMap<String, LinkedHashSet<String>> currPoints = new LinkedHashMap<>();
                    LinkedHashSet<String> toAdd = new LinkedHashSet<>();
                    toAdd.add(nfa1.getStart());
                    currPoints.put("empty", toAdd);
                    update.put(curr, currPoints);
                }
            } else if (curr.equals(newStart)) {
                LinkedHashSet<String> toAdd = new LinkedHashSet<>();
                toAdd.add(nfa1.getStart());
                LinkedHashMap<String, LinkedHashSet<String>> inner = new LinkedHashMap<>();
                inner.put("empty", toAdd);
                update.put(curr, inner);
            }
        }
        Iterator<Map.Entry<String, LinkedHashMap<String, LinkedHashSet<String>>>> i = update.entrySet().iterator();
        while (i.hasNext()) {
            Map.Entry<String, LinkedHashMap<String, LinkedHashSet<String>>> mapElement = i.next();
            String currState = mapElement.getKey();
            LinkedHashMap<String, LinkedHashSet<String>> inner = mapElement.getValue();
            Iterator<Map.Entry<String, LinkedHashSet<String>>> innerI = inner.entrySet().iterator();
            while (innerI.hasNext()) {
                Map.Entry<String, LinkedHashSet<String>> innerVals = innerI.next();
                String sym = innerVals.getKey();
                LinkedHashSet<String> toStates = innerVals.getValue();
                for (String state : toStates) {
                    StringBuilder indiv = new StringBuilder();
                    indiv.append(currState);
                    indiv.append(" ");
                    indiv.append(state);
                    indiv.append(" ");
                    indiv.append(sym);
                    symbols.add(sym);
                    toRet.add(indiv.toString());
                }
            }
        }
        StringBuilder syms = new StringBuilder();
        for (String sym : symbols) {
            syms.append(sym);
            syms.append(" ");
        }
        toRet.add(syms.toString());
        return toRet;
    }

    public static LinkedHashMap<String, LinkedHashMap<String, LinkedHashSet<String>>> transfer(NFA nfa1) {
        LinkedHashMap<String, LinkedHashMap<String, LinkedHashSet<String>>> old = nfa1.getDelta();
        LinkedHashMap<String, LinkedHashMap<String, LinkedHashSet<String>>> newDelta =
                new LinkedHashMap<String, LinkedHashMap<String, LinkedHashSet<String>>>();
        for (Map.Entry<String, LinkedHashMap<String, LinkedHashSet<String>>> m : old.entrySet()) {
            String start = m.getKey();
            String startName = nfa1.getIdName().get(start);
            LinkedHashMap<String, LinkedHashSet<String>> inner = m.getValue();
            LinkedHashMap<String, LinkedHashSet<String>> newinner = new LinkedHashMap<String, LinkedHashSet<String>>();
            for (Map.Entry<String, LinkedHashSet<String>> m2 : inner.entrySet()) {
                String sym = m2.getKey();
                LinkedHashSet<String> hs = m2.getValue();
                LinkedHashSet<String> newhs = new LinkedHashSet<String>();
                for (String s : hs) {
                    newhs.add(nfa1.getIdName().get(s));
                }
                newinner.put(sym, newhs);
            }
            newDelta.put(startName, newinner);
        }
        return newDelta;
    }

}
