import java.util.*;

public class NFAConct {
    static NFA NFAone;
    static NFA NFAtwo;

    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);
        String fileNames = scan.nextLine();
        String[] spl = fileNames.split(" ");

        NFA newNFA = concat(spl[0], spl[1]);
        NFAXML NFAXML = new NFAXML();
        List<String> output = NFAXML.createXML(newNFA, true);
        for (String s : output) {
            System.out.println(s);
        }
    }

    public static NFA concat(String first, String second) {

        xml parserOne = new xml();
        List<String> statesOne = parserOne.callParse(first, "states");
        List<String> transitionsOne = parserOne.callParse(first, "transitions");
        Map<String, String> idNameOne = parserOne.idName;
        NFAone = new NFAMaker(statesOne, transitionsOne, idNameOne);

        xml parserTwo = new xml();
        List<String> statesTwo = parserTwo.callParse(second, "states");
        List<String> transitionsTwo = parserTwo.callParse(second, "transitions");
        Map<String, String> idNameTwo = parserTwo.idName;
        NFAtwo = new NFAMaker(statesTwo, transitionsTwo, idNameTwo);

        List<String> newStates = new ArrayList<>();
        Map<String, String> oldtoNewOne = new LinkedHashMap<>();
        Map<String, String> oldtoNewTwo = new LinkedHashMap<>();
        String newNames = combineNames(statesOne.get(0), statesTwo.get(0), oldtoNewOne, oldtoNewTwo);
        String newInitial = findNewInitial(statesOne.get(1));
        String newFinal = findNewFinal(statesTwo.get(2));
        newStates.add(newNames);
        newStates.add(newInitial);
        newStates.add(newFinal);
        Map<String, String> newMap = findMap(newNames);
        List<String> newTrans = combineTrans(newNames, statesOne.get(1), statesTwo.get(1), oldtoNewOne, oldtoNewTwo);
        NFA newNFA = new NFAMaker(newStates, newTrans, newMap);
        return newNFA;
    }

    private static List<String> combineTrans(String newNames, String s, String s1, Map<String, String> oldtoNewOne,
                                      Map<String, String> oldtoNewTwo) {
        //keep all of the one, all of the two
        //attach final of one to initial of two with epsilon transitions
        List<String> toRet = new ArrayList<>();
        LinkedHashSet<String> symbols = new LinkedHashSet<>();

        String[] newArr = newNames.split(" ");

        LinkedHashMap<String, LinkedHashMap<String, LinkedHashSet<String>>> update = new LinkedHashMap<>();
        for (int i = 0; i < newArr.length; i++) {
            update.put(newArr[i], new LinkedHashMap<String, LinkedHashSet<String>>());
        }

        LinkedHashMap<String, LinkedHashMap<String, LinkedHashSet<String>>> one = NFAone.getDelta();
        Map<String, String> oneIdName = NFAone.getIdName();
        LinkedHashMap<String, LinkedHashMap<String, LinkedHashSet<String>>> two = NFAtwo.getDelta();
        Map<String, String> twoIdName = NFAtwo.getIdName();

        for (int i = 0; i < newArr.length; i++) {
            //current state
            String curr = newArr[i];
            String og = curr.substring(0, newArr[i].length() - 2);
            if (curr.contains(".1")) {

                LinkedHashMap<String, LinkedHashSet<String>> toUse = one.get(getID(og, oneIdName));
                //id to name
                updateInfo(toUse, oneIdName, true);
                //old name to new name
                updateInfo(toUse, oldtoNewOne, false);

                //if this is a final state of 1 -> make epsilon trans to initial state in twp
                if (NFAone.getAccept().contains(curr.substring(0, curr.length()-2))) {
                    //add epsilon trans
                    String startTwo = NFAtwo.getStart();
                    if (toUse.containsKey("empty")) {
                        LinkedHashSet<String> empties = toUse.get("empty");
                        String newName = oldtoNewTwo.get(startTwo);
                        empties.add(newName);
                        toUse.replace("empty", empties);
                    }
                    else {
                        LinkedHashSet<String> empties = new LinkedHashSet<String>();
                        String newName = oldtoNewTwo.get(startTwo);
                        empties.add(newName);
                        toUse.put("empty", empties);
                    }
                }
                update.replace(curr, toUse);
            }
            else {
                //do not need to edit second NFA
                LinkedHashMap<String, LinkedHashSet<String>> toUse = two.get(getID(og, twoIdName));
                updateInfo(toUse, twoIdName, true);
                updateInfo(toUse, oldtoNewTwo, false);
                update.replace(curr, toUse);
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
