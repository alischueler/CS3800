import java.util.*;

public class NFAUnion {


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

        NFA newNFA = union(NFAone, NFAtwo);
        NFAXML NFAXML = new NFAXML();
        List<String> output = NFAXML.createXML(newNFA, true);
        for (String s : output) {
            System.out.println(s);
        }
    }

    public static NFA union(NFA NFAone, NFA NFAtwo) {

        List<String> newStates = new ArrayList<>();
        Map<String, String> oldtoNewOne = new LinkedHashMap<>();
        Map<String, String> oldtoNewTwo = new LinkedHashMap<>();

        String statesOne = convert(NFAone.getStates());
        String statesTwo = convert(NFAtwo.getStates());
        String acceptOne = convert(NFAone.getAccept());
        String acceptTwo = convert(NFAtwo.getAccept());

        String newNames = combineNames(statesOne, statesTwo, oldtoNewOne, oldtoNewTwo);
        String newInitial = findNewInitial();
        String newFinal = findNewFinal(acceptOne, acceptTwo);
        newStates.add(newNames);
        newStates.add(newInitial);
        newStates.add(newFinal);
        Map<String, String> newMap = findMap(newNames);
        List<String> newTrans = combineTrans(newNames, NFAone.getStart(), NFAtwo.getStart(), oldtoNewOne, oldtoNewTwo,
                NFAone, NFAtwo);
        NFA newNFA = new NFAMaker(newStates, newTrans, newMap);
        return newNFA;
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

    private static Map<String, String> findMap(String names) {
        Map<String, String> toRet = new HashMap<String, String>();
        String[] namesArr = names.split(" ");
        for (int i = 0; i < namesArr.length; i++) {
            toRet.put(namesArr[i], namesArr[i]);
        }
        return toRet;

    }

    public static String findNewInitial() {
        String sb = "0.0";
        ;
        return sb;
    }

    public static String findNewFinal(String oneAll, String twoAll) {
        LinkedHashSet<String> set = new LinkedHashSet<>();
        String[] onespl = oneAll.split(" ");
        String[] twospl = twoAll.split(" ");
        //all one final states
        for (int i = 0; i < onespl.length; i++) {
            set.add(onespl[i] + ".1");
        }

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

    public static String combineNames(String one, String two, Map<String, String> oldtoNewOne, Map<String, String> oldtoNewTwo) {
        LinkedHashSet<String> set = new LinkedHashSet<>();
        String[] onespl = one.split(" ");
        String[] twospl = two.split(" ");
        set.add("0.0");
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

    //in order of start, end, symbol
    public static List<String> combineTrans(String newStates, String oldOneStart, String oldTwoStart,
                                            Map<String, String> oldtoNewOne, Map<String, String> oldtoNeewTwo,
                                            NFA NFAone, NFA NFAtwo) {

        List<String> toRet = new ArrayList<>();
        LinkedHashSet<String> symbols = new LinkedHashSet<>();

        String[] newArr = newStates.split(" ");

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
            String ending = curr.substring(newArr[i].length()-2);
           // System.out.println("curr " + curr + " og " + og);
            if (ending.equals(".1")) {
                if (og.equals(oldOneStart)) {
                    LinkedHashMap<String, LinkedHashSet<String>> innerStart = update.get("0.0");
                    if (innerStart.containsKey("empty")) {
                        LinkedHashSet<String> existing = innerStart.get("empty");
                        existing.add(curr);
                        innerStart.replace("empty", existing);
                    } else {
                        LinkedHashSet<String> notExisting = new LinkedHashSet<>();
                        notExisting.add(curr);
                        innerStart.put("empty", notExisting);
                    }
                    update.replace("0.0", innerStart);

                }
                if (one.containsKey(og)) {
                    //iterate through hashmaps and add them appropriately
                   // System.out.println("ONE" + one);
                    String id = getID(og, oneIdName);
                    //System.out.println("id " + id);
                    LinkedHashMap<String, LinkedHashSet<String>> toReplace = one.get(id);
                    updateInfo(toReplace, oneIdName, true);
                    updateInfo(toReplace, oldtoNewOne, false);
                    update.replace(curr, toReplace);
                } else {
                    //do nothing
                }
            } else if (ending.equals(".2")) {
                if (og.equals(oldTwoStart)) {
                    LinkedHashMap<String, LinkedHashSet<String>> innerStart = update.get("0.0");
                    if (innerStart.containsKey("empty")) {
                        LinkedHashSet<String> existing = innerStart.get("empty");
                        existing.add(curr);
                        innerStart.replace("empty", existing);
                    } else {
                        LinkedHashSet<String> notExisting = new LinkedHashSet<>();
                        notExisting.add(curr);
                        innerStart.replace("empty", notExisting);
                    }
                    update.replace("0.0", innerStart);
                }
                if (two.containsKey(og)) {
                    //iterate through hashmaps and add them appropriately
                    LinkedHashMap<String, LinkedHashSet<String>> toReplace = two.get(getID(og, twoIdName));
                    updateInfo(toReplace, twoIdName, true);
                    updateInfo(toReplace, oldtoNeewTwo, false);
                    update.replace(curr, toReplace);
                }
                else {
                    //do nothing
                }
            }
            //System.out.println(update);
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
                for (String s : toStates) {
                    StringBuilder indiv = new StringBuilder();
                    indiv.append(currState);
                    indiv.append(" ");
                    indiv.append(s);
                    indiv.append(" ");
                    indiv.append(sym);
                    symbols.add(sym);
                    toRet.add(indiv.toString());
                }
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
        } else {
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
            //System.out.println(m.getValue() + " vs " + name);
            if (name.equals(m.getValue())) {
                s = (String) m.getKey();
            }
        }
        //System.out.println(s);
        return s;
    }
}
