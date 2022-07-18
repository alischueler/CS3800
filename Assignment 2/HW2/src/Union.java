import java.util.*;

public class Union {
    static DFA DFAone;
    static DFA DFAtwo;

    public static void main(String[] args) {
        xml parserOne = new xml();
        xml parserTwo = new xml();
        Scanner scan = new Scanner(System.in);
        String fileNames = scan.nextLine();
        String[] spl = fileNames.split(" ");
        List<String> statesOne = parserOne.callParse(spl[0], "states");
        List<String> transitionsOne = parserOne.callParse(spl[0], "transitions");
        DFAone = new DFAMaker(statesOne, transitionsOne,  parserOne.idName);

        List<String> statesTwo = parserTwo.callParse(spl[1], "states");
        List<String> transitionsTwo = parserTwo.callParse(spl[1], "transitions");
        DFAtwo = new DFAMaker(statesTwo, transitionsTwo, parserTwo.idName);

        List<List<String>> newStates = new ArrayList<>();
        List<String> newNames = combineNames(statesOne.get(0), statesTwo.get(0));
        List<String> newInitial = findNewInitial(statesOne.get(1), statesTwo.get(1));
        List<String> newFinal = findNewFinal(statesOne.get(0), statesOne.get(2), statesTwo.get(0), statesTwo.get(2));
        newStates.add(newNames);
        newStates.add(newInitial);
        newStates.add(newFinal);
        List<String> newTrans = combineTrans(newNames);
        DFA newDFA = new DFAMaker(newStates, newTrans, true);
        newDFA.setIDName(findMap(newNames));
        DFAXML dfaxml = new DFAXML();
        List<String> output = dfaxml.createXML(newDFA, true);
        for (String s:output) {
            System.out.println(s);
        }
    }

    private static Map<String, String> findMap( List<String> names) {
        Map<String, String> toRet = new HashMap<String, String>();

        for (int i = 0; i < names.size(); i++) {
            String[] spl = names.get(i).split(" ");
            String oneString = spl[0].substring(1);
            String startOneID = getID(oneString, DFAone.getIdName());
            String twoString = spl[1].substring(0, spl[1].length() - 1);
            String startTwoID = getID(twoString, DFAtwo.getIdName());
            toRet.put("("+startOneID+" "+startTwoID+")", names.get(i));
        }
        return toRet;

    }

    public static List<String> findNewInitial(String one, String two) {
        StringBuilder sb = new StringBuilder();
        sb.append("(");
        sb.append(one);
        sb.append(" ");
        sb.append(two);
        sb.append(")");
        List<String> sb2 = new ArrayList<>();
        sb2.add(sb.toString());
        return sb2;
    }

    public static List<String> findNewFinal(String oneAll, String one, String twoAll, String two) {
        HashSet<String> set = new HashSet<>();
        String[] onespl = oneAll.split(" ");
        String[] twospl = twoAll.split(" ");
        //all one final states
        for (int i = 0; i < twospl.length;i++) {
            set.add("(" + one + " " + twospl[i] + ")");
        }

        //all two final states
        for (int i = 0; i< onespl.length; i++) {
            set.add("("+onespl[i]+" "+two+")");
        }
        List<String> sb = new ArrayList<String>();
        for (String s : set) {
            sb.add(s);
        }
        return sb;
    }

    public static List<String> combineNames(String one, String two) {
        HashSet<String> set = new HashSet<>();
        String[] onespl = one.split(" ");
        String[] twospl = two.split(" ");
        for (int i =0; i<onespl.length;i++) {
            for (int j=0; j<twospl.length;j++) {
                StringBuilder toAdd = new StringBuilder();
                toAdd.append("(");
                toAdd.append(onespl[i]);
                toAdd.append(" ");
                toAdd.append(twospl[j]);
                toAdd.append(")");
                set.add(toAdd.toString());
            }
        }
        List<String> sb = new ArrayList<>();
        for (String s : set) {
            sb.add(s);
        }
        return sb;
    }

    //in order of start, end, symbol
    public static List<String> combineTrans(List<String> newStates) {
        List<String> toRet = new ArrayList<>();
        LinkedHashMap<String, LinkedHashMap<String, String>> one = DFAone.getDelta();
        LinkedHashMap<String, LinkedHashMap<String, String>> two = DFAtwo.getDelta();
        HashSet<String> alphabet = DFAone.getAlphabet();
        //length of a states x length of b states x size of alphabet
        //check where one goes to at x, check where two goes to at x, 1 2 will go there at x
        for (int i = 0; i < newStates.size(); i++) {
                String[] spl = newStates.get(i).split(" ");
                String oneString = spl[0].substring(1);
                String startOneID = getID(oneString, DFAone.getIdName());
                String twoString = spl[1].substring(0, spl[1].length()-1);
                String startTwoID = getID(twoString, DFAtwo.getIdName());
                for (String s : alphabet) {
                    StringBuilder toAdd = new StringBuilder();
                    String endOneID = one.get(startOneID).get(s);
                    String endTwoID = two.get(startTwoID).get(s);
                    toAdd.append("(");
                    toAdd.append(startOneID);
                    toAdd.append(" ");
                    toAdd.append(startTwoID);
                    toAdd.append(") (");
                    toAdd.append(endOneID);
                    toAdd.append(" ");
                    toAdd.append(endTwoID);
                    toAdd.append(") ");
                    toAdd.append(s);
                    toRet.add(toAdd.toString());
                }
            }
        return toRet;
    }

    public static String getID(String name, Map<String, String> idName) {
        String s ="";
        for (Map.Entry m : idName.entrySet()) {
            if (name.equals(m.getValue())) {
                s= (String)m.getKey();
            }
        }
        return s;
    }
}
