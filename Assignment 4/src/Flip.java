import java.util.*;

public class Flip {
    static DFA DFAone;

    public static void main(String[] args) {
        xml parserOne = new xml();
        Scanner scan = new Scanner(System.in);
        String fileNames = scan.nextLine();
        String[] spl = fileNames.split(" ");
        List<String> statesOne = parserOne.callParse(spl[0], "states");
        List<String> transitionsOne = parserOne.callParse(spl[0], "transitions");
//        for (String s : transitionsOne) {
//            System.out.println(s);
//        }
        Map<String, String> idNameOne = parserOne.idName;
        DFAone = new DFAMaker(statesOne, transitionsOne, idNameOne);
        NFA nfa = flip(DFAone);
        NFAXML xml = new NFAXML();
        List<String> toPrint = xml.createXML(nfa, true);
        for (String s : toPrint) {
            System.out.println(s);
        }
    }

    public static NFA flip(DFA dfa) {
        List<String> newStates = findNewStates(dfa.getStates());
        Map<String, String> idName = findIdName(newStates.get(0));
        List<String> newTrans = findNewTrans(DFAone.getDelta(), newStates);
        NFA nfa = new NFAMaker(newStates, newTrans, idName);
        return nfa;
    }

    public static List<String> findNewTrans(LinkedHashMap<String, LinkedHashMap<String, String>> delta, List<String> newStates) {
        List<String> toRet = new ArrayList<>();
        String[] states = newStates.get(0).split(" ");
        String newAccept = newStates.get(2);
        String newStart = newStates.get(1);
        String oldStart = DFAone.getStart();
        HashSet<String> alphabet = DFAone.getAlphabet();
        HashSet<String> oldAccept = DFAone.getAccept();
       // System.out.println(delta);

        for (String s : states) {
            if (s.equals(newStart)) {
                for (String a : oldAccept) {
                    StringBuilder sb = new StringBuilder();
                    sb.append(s).append(" ").append(a).append(" empty");
                    toRet.add(sb.toString());
                    //System.out.println(sb);
                }
            }
            if (oldStart.equals(s)) {
                StringBuilder sb = new StringBuilder();
                sb.append(s).append(" ").append(newAccept).append(" ").append("empty");
                toRet.add(sb.toString());
                //System.out.println(sb);
            }
            if (!s.equals(newStart) && !newAccept.contains(s)) {
                String from = s;
                String fromid = getName(from, DFAone.getIdName());
                LinkedHashMap<String, String> inner = delta.get(fromid);
                for(String a : alphabet) {
                    StringBuilder sb = new StringBuilder();
                    String symbol = a;
                    String toId = inner.get(symbol);
                    String to = DFAone.getIdName().get(toId);
                    sb.append(to).append(" ").append(from).append(" ").append(symbol);
                    toRet.add(sb.toString());
                    //System.out.println(sb);
                }
            }
        }
        StringBuilder sb = new StringBuilder();
        for (String s : alphabet) {
            if (sb.length()>0) {
                sb.append(" ");
            }
            sb.append(s);
        }
       // System.out.println(sb);
        sb.append(" ").append("empty");
        toRet.add(sb.toString());
        return toRet;
    }

    public static String getName(String name, Map<String, String> idName) {
        String toRet = "";
        for (Map.Entry<String, String> m: idName.entrySet()) {
            if (m.getValue().equals(name)) {
                toRet = m.getKey();
            }
        }
        return toRet;
    }

    public static Map<String, String> findIdName(String states) {
        Map<String, String> toRet = new LinkedHashMap<>();
        String[] statesarr = states.split(" ");
        for (String s : statesarr) {
            toRet.put(s, s);
        }
        return toRet;
    }

    public static List<String> findNewStates(HashSet<String> allStates) {
        List<String> toRet = new ArrayList<>();
        List<String> los = new ArrayList<>();
        los.addAll(allStates);
        checkExist("0.0", 0, los);
        String start = getLastEle(los);
        checkExist("100", 1, los);
        String end = getLastEle(los);

        StringBuilder sb = new StringBuilder();
        for(String s: los) {
            if (sb.length()>0) {
                sb.append(" ");
            }
            sb.append(s);
        }
        toRet.add(sb.toString());
        toRet.add(start);
        toRet.add(end);
        return toRet;
    }

    public static String getLastEle(List<String> los) {
        String[] elements = new String[los.size()];
        // Convert LinkedHashSet to Array
        elements = los.toArray(elements);
        // Get the last element with the help of the index.
        String lastEle = elements[elements.length - 1];
        return lastEle;
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
}
