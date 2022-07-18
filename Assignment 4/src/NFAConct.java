import java.util.*;

public class NFAConct {
    public static void main(String[] args) {
        xml parserOne = new xml();
        Scanner scan = new Scanner(System.in);
        String fileNames = scan.nextLine();
        String[] spl = fileNames.split(" ");
        List<String> statesOne = parserOne.callParse(spl[0], "states");
        List<String> transitionsOne = parserOne.callParse(spl[0], "transitions");
        Map<String, String> idNameOne = parserOne.idName;
        NFA NFAone = new NFAMaker(statesOne, transitionsOne, idNameOne);

        xml parserTwo = new xml();
        List<String> statesTwo = parserTwo.callParse(spl[1], "states");
        List<String> transitionsTwo = parserTwo.callParse(spl[1], "transitions");
        Map<String, String> idNameTwo = parserTwo.idName;
        NFA NFAtwo = new NFAMaker(statesTwo, transitionsTwo, idNameTwo);

        NFA newNFA = concat(NFAone, NFAtwo);

        NFAXML NFAXML = new NFAXML();
        List<String> output = NFAXML.createXML(newNFA, true);
        for (String s : output) {
            System.out.println(s);
        }
    }

    public static NFA concat(NFA NFAone, NFA NFAtwo) {
        List<String> newStates = new ArrayList<>();
        Map<String, String> oldtoNewOne = new LinkedHashMap<>();
        Map<String, String> oldtoNewTwo = new LinkedHashMap<>();
        String statesOne = convert(NFAone.getStates());
        String statesTwo = convert(NFAtwo.getStates());
        String statesTwoFinal = convert(NFAtwo.getAccept());

        String newNames = combineNames(statesOne, statesTwo, oldtoNewOne, oldtoNewTwo);
        String newInitial = findNewInitial(NFAone.getStart());
        String newFinal = findNewFinal(statesTwoFinal);
        newStates.add(newNames);
        newStates.add(newInitial);
        newStates.add(newFinal);
        Map<String, String> newMap = findMap(newNames);
        List<String> newTrans = combineTrans(newNames, oldtoNewOne, oldtoNewTwo, NFAone, NFAtwo);
        NFA newNFA = new NFAMaker(newStates, newTrans, newMap);
        return newNFA;
    }

    public static String convert(HashSet<String> set) {
        StringBuilder sb = new StringBuilder();
        for (String s : set) {
            if (sb.length()>0) {
                sb.append(" ");
            }
            sb.append(s);
        }
        return sb.toString();
    }

    private static List<String> combineTrans(String newNames, Map<String, String> oldtoNewOne,
                                      Map<String, String> oldtoNewTwo, NFA NFAone, NFA NFAtwo) {
        //keep all of the one, all of the two
        //attach final of one to initial of two with epsilon transitions
        List<String> toRet = new ArrayList<>();
        LinkedHashSet<String> symbols = new LinkedHashSet<>();

        String[] newArr = newNames.split(" ");

        LinkedHashMap<String, LinkedHashMap<String, LinkedHashSet<String>>> one = NFAone.getDelta();
        Map<String, String> oneIdName = NFAone.getIdName();
        LinkedHashMap<String, LinkedHashMap<String, LinkedHashSet<String>>> two = NFAtwo.getDelta();
        Map<String, String> twoIdName = NFAtwo.getIdName();

        for (int i = 0; i < newArr.length; i++) {
            //current state
            String curr = newArr[i];
            String og = curr.substring(0, newArr[i].length() - 2);
            String ending = curr.substring(newArr[i].length()-2);
            if (ending.equals(".1")) {
                String id = getID(og, oneIdName);
                String old = getID(curr, oldtoNewOne);
                if (one.containsKey(id)) {
                    if (NFAone.getAccept().contains(old)) {
                        String startTwo = NFAtwo.getStart();
                        String newName = oldtoNewTwo.get(startTwo);
                        StringBuilder sb = new StringBuilder();
                        sb.append(curr).append(" ").append(newName).append(" empty");
                        toRet.add(sb.toString());
                    }
                    LinkedHashMap<String, LinkedHashSet<String>> toUse = one.get(id);
                    for (Map.Entry<String, LinkedHashSet<String>> m : toUse.entrySet()) {
                        String symbol = m.getKey();
                        LinkedHashSet<String> ends = m.getValue();
                        for (String end : ends) {
                            StringBuilder sb = new StringBuilder();
                            String endName = oneIdName.get(end);
                            String name = oldtoNewOne.get(endName);
                            sb.append(curr).append(" ").append(name).append(" ").append(symbol);
                            toRet.add(sb.toString());
                        }
                    }
                }
                else {
                    if (NFAone.getAccept().contains(old)) {
                        String startTwo = NFAtwo.getStart();
                        String newName = oldtoNewTwo.get(startTwo);
                        StringBuilder sb = new StringBuilder();
                        sb.append(curr).append(" ").append(newName).append(" empty");
                        toRet.add(sb.toString());
                    }
                }


//                    //id to name
//                    updateInfo(toUse, oneIdName, true);
//                    //old name to new name
//                    updateInfo(toUse, oldtoNewOne, false);
//
//                    //if this is a final state of 1 -> make epsilon trans to initial state in twp
//                    if (NFAone.getAccept().contains(curr.substring(0, curr.length() - 2))) {
//                        //add epsilon trans
//                        String startTwo = NFAtwo.getStart();
//                        if (toUse.containsKey("empty")) {
//                            LinkedHashSet<String> empties = toUse.get("empty");
//                            String newName = oldtoNewTwo.get(startTwo);
//                            empties.add(newName);
//                            toUse.replace("empty", empties);
//                        } else {
//                            LinkedHashSet<String> empties = new LinkedHashSet<String>();
//                            String newName = oldtoNewTwo.get(startTwo);
//                            empties.add(newName);
//                            toUse.put("empty", empties);
//                        }
//                    }
//                    update.put(curr, toUse);
            }
            else {
                String id = getID(og, twoIdName);
                if (two.containsKey(id)) {
                    LinkedHashMap<String, LinkedHashSet<String>> toUse = two.get(id);
                    for (Map.Entry<String, LinkedHashSet<String>> m: toUse.entrySet()) {
                        String symbol = m.getKey();
                        LinkedHashSet<String> ends = m.getValue();
                        for (String end: ends) {
                            StringBuilder sb = new StringBuilder();
                            String endName = twoIdName.get(end);
                            String name = oldtoNewTwo.get(endName);
                            sb.append(curr).append(" ").append(name).append(" ").append(symbol);
                            toRet.add(sb.toString());
                        }
                    }

//                    //do not need to edit second NFA
//                    String id = getID(og, twoIdName);
//                    System.out.println(id);
//                    LinkedHashMap<String, LinkedHashSet<String>> toUse = two.get(id);
//                    System.out.println(toUse);
//                    updateInfo(toUse, twoIdName, true);
//                    System.out.println(toUse);
//                    updateInfo(toUse, oldtoNewTwo, false);
//                    System.out.println(toUse);
//                    update.put(curr, toUse);
//                    System.out.println(update);
                }
            }
        }
//        Iterator<Map.Entry<String, LinkedHashMap<String, LinkedHashSet<String>>>> i = update.entrySet().iterator();
//        while (i.hasNext()) {
//            Map.Entry<String, LinkedHashMap<String, LinkedHashSet<String>>> mapElement = i.next();
//            String currState = mapElement.getKey();
//            LinkedHashMap<String, LinkedHashSet<String>> inner = mapElement.getValue();
//            Iterator<Map.Entry<String, LinkedHashSet<String>>> innerI = inner.entrySet().iterator();
//            while (innerI.hasNext()) {
//                Map.Entry<String, LinkedHashSet<String>> innerVals = innerI.next();
//                String sym = innerVals.getKey();
//                LinkedHashSet<String> toStates = innerVals.getValue();
//                for (String state : toStates) {
//                    StringBuilder indiv = new StringBuilder();
//                    indiv.append(currState);
//                    indiv.append(" ");
//                    indiv.append(state);
//                    indiv.append(" ");
//                    indiv.append(sym);
//                    symbols.add(sym);
//                    toRet.add(indiv.toString());
//                }
//            }
//        }
        StringBuilder syms = new StringBuilder();
        for (String sym : symbols) {
            syms.append(sym);
            syms.append(" ");
        }
        toRet.add(syms.toString());
        return toRet;
    }

    public static String combineNames(String one, String two, Map<String, String> oldtoNewOne, Map<String, String> oldtoNewTwo) {
        LinkedHashSet<String> set = new LinkedHashSet<>();
        String[] onespl = one.split(" ");
        String[] twospl = two.split(" ");
        //all one final states
        for (int i = 0; i < onespl.length; i++) {
            oldtoNewOne.put(onespl[i], onespl[i] + ".1");
            set.add(onespl[i] + ".1");
        }

        //all two final states
        for (int i = 0; i < twospl.length; i++) {
            oldtoNewTwo.put(twospl[i], twospl[i] + ".2");
            set.add(twospl[i] + ".2");
        }
        StringBuilder sb = new StringBuilder();
        for (String s : set) {
            sb.append(s + " ");
        }
        return sb.toString();
    }

    public static String findNewInitial(String nfaone) {
        return nfaone+".1";
    }

    public static String findNewFinal(String twofinal) {
        LinkedHashSet<String> set = new LinkedHashSet<>();
        String[] twospl = twofinal.split(" ");
        //all two final states
        for (int i = 0; i < twospl.length; i++) {
            set.add(twospl[i] + ".2");
        }
        StringBuilder sb = new StringBuilder();
        for (String s : set) {
            sb.append(s + " ");
        }
        return sb.toString();
    }

    private static Map<String, String> findMap(String names) {
        Map<String, String> toRet = new HashMap<String, String>();
        String[] namesArr = names.split(" ");
        for (int i = 0; i < namesArr.length; i++) {
            toRet.put(namesArr[i], namesArr[i]);
        }
        return toRet;

    }

    public static void updateInfo(HashMap<String, LinkedHashSet<String>> toUpdate, Map<String, String> idName, boolean idToName) {
        if (idToName) {
            for (Map.Entry<String, LinkedHashSet<String>> m : toUpdate.entrySet()) {
                LinkedHashSet<String> replace = new LinkedHashSet<>();
                LinkedHashSet<String> toStates = m.getValue();
                for (String s : toStates) {
                    String news = idName.get(s);
                    replace.add(news);
                }
                toUpdate.replace(m.getKey(), replace);
            }
        }
        else {
            for (Map.Entry<String, LinkedHashSet<String>> m : toUpdate.entrySet()) {
                LinkedHashSet<String> replace = new LinkedHashSet<>();
                LinkedHashSet<String> toStates = m.getValue();
                for (String s : toStates) {
                    String news = idName.get(s);
                    replace.add(news);
                }
                toUpdate.replace(m.getKey(), replace);
            }
        }
    }

    public static String getID(String name, Map<String, String> idName) {
        String s = "";
        for (Map.Entry m : idName.entrySet()) {
            if (name.equals(m.getValue())) {
                s = (String) m.getKey();
            }
        }
        return s;
    }

}
