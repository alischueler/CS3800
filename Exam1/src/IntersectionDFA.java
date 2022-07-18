import java.util.*;

public class IntersectionDFA {
    static DFA DFAone;
    static DFA DFAtwo;

    public DFA intersect(DFA one, DFA two, boolean intersected) {
        DFAone = one;
        DFAtwo = two;
        String oneStates = convertToString(one.getStates());
        String twoStates = convertToString(two.getStates());
        String oneAccept = convertToString(one.getAccept());
        String twoAccept = convertToString(two.getAccept());
        String alpha = convertToString(one.getAlphabet());
        List<List<String>> newStates = new ArrayList<>();
        List<String> newNames;
        List<String> newInitial;
        List<String> newFinal;
        List<String> newTrans;
        if (intersected) {
            newNames = combineIntersectedNames(one.getStates(), two.getStates());
            newInitial = findNewInitialIntersection(one.getStart(), two.getStart());
            newFinal = findNewFinalIntersection(one.getAccept(), two.getAccept());
            newTrans = combineIntersectedTrans(newNames);
        }
        else {
            newNames = combineNames(oneStates, twoStates);
            newInitial = findNewInitial(one.getStart(), two.getStart());
            newFinal = findNewFinal(oneAccept, twoAccept);
            newTrans = combineTrans(newNames);
        }
        newTrans.add(alpha);
        newStates.add(newNames);
        newStates.add(newInitial);
        newStates.add(newFinal);
        DFA newDFA = new DFAMaker(newStates, newTrans, true);
        newDFA.setIDName(findMap(newNames));
        return newDFA;
    }

    private List<String> combineIntersectedTrans(List<String> newNames) {
        List<String> toRet = new ArrayList<>();
        LinkedHashMap<String, LinkedHashMap<String, String>> one = DFAone.getDelta();
        LinkedHashMap<String, LinkedHashMap<String, String>> two = DFAtwo.getDelta();
        HashSet<String> alphabet = DFAone.getAlphabet();
        //length of a states x length of b states x size of alphabet
        //check where one goes to at x, check where two goes to at x, 1 2 will go there at x
        for (int i = 0; i < newNames.size(); i++) {
            String[] spl = newNames.get(i).split(" ");
            String oneString = spl[0].substring(1)+" "+spl[1].substring(0, spl[1].length()-2);
            String startOneID = getID(oneString, DFAone.getIdName());
            String twoString = spl[2]+" "+spl[3].substring(0, spl[3].length()-3);
            String startTwoID = getID(twoString, DFAtwo.getIdName());
            for (String s : alphabet) {
                StringBuilder toAdd = new StringBuilder();
                String endOneID = one.get(startOneID).get(s);
                String endTwoID = two.get(startTwoID).get(s);
                toAdd.append("(");
                toAdd.append(startOneID+".1");
                toAdd.append(" ");
                toAdd.append(startTwoID+".2");
                toAdd.append(") ; (");
                toAdd.append(DFAone.getIdName().get(endOneID)+".1");
                toAdd.append(" ");
                toAdd.append(DFAtwo.getIdName().get(endTwoID)+".2");
                toAdd.append(") ");
                toAdd.append(s);
                toRet.add(toAdd.toString());
            }
        }
        return toRet;

    }

    private List<String> findNewFinalIntersection(HashSet<String> accept1, HashSet<String> accept2) {
        HashSet<String> set = new HashSet<>();
        for (String s1 : accept1) {
            for (String s2:accept2) {
                StringBuilder sb = new StringBuilder();
                sb.append("(").append(s1+".1").append(" ").append(s2+".2").append(")");
                set.add(sb.toString());
            }
        }
        List<String> sb = new ArrayList<>();
        for (String s : set) {
            sb.add(s);
        }
        return sb;
    }

    private List<String> findNewInitialIntersection(String start1, String start2) {
        String toAdd = "("+start1+".1"+" "+start2+".2"+")";
        List<String> arr = new ArrayList<>();
        arr.add(toAdd);
        return arr;
    }

    private List<String> combineIntersectedNames(HashSet<String> oneStates, HashSet<String> twoStates) {
        HashSet<String> set = new HashSet<>();
        for (String s1 : oneStates) {
            for (String s2:twoStates) {
                StringBuilder sb = new StringBuilder();
                sb.append("(").append(s1+".1").append(" ").append(s2+".2").append(")");
                set.add(sb.toString());
            }
        }
        List<String> sb = new ArrayList<>();
        for (String s : set) {
            sb.add(s);
        }
        return sb;

    }

    private static String convertToString(HashSet<String> convert) {
        StringBuilder sb = new StringBuilder();
        for (String s : convert) {
            if (sb.length()!=0) {
                sb.append(" ");
            }
            sb.append(s);
        }
        return sb.toString();
    }

    private static Map<String, String> findMap( List<String> names) {
        Map<String, String> toRet = new HashMap<String, String>();

        for (int i = 0; i < names.size(); i++) {
            toRet.put(names.get(i), names.get(i));
        }
        return toRet;

    }

    public static List<String> findNewInitial(String one, String two) {
        StringBuilder sb = new StringBuilder();
        sb.append("(");
        sb.append(one+".1");
        sb.append(" ");
        sb.append(two+".2");
        sb.append(")");
        List<String> sb2 = new ArrayList<>();
        sb2.add(sb.toString());
        return sb2;
    }

    public static List<String> findNewFinal( String one, String two) {
        HashSet<String> set = new HashSet<>();
        String[] onespl = one.split(" ");
        String[] twospl = two.split(" ");

        for (int i = 0; i< onespl.length; i++) {
            for (int j = 0; j< twospl.length; j++) {
                set.add("("+onespl[i]+".1"+ " "+twospl[j]+".2"+")");
            }
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
                toAdd.append(onespl[i]+".1");
                toAdd.append(" ");
                toAdd.append(twospl[j]+".2");
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
                String oneString = spl[0].substring(1, spl[0].length()-2);
                String startOneID = getID(oneString, DFAone.getIdName());
                String twoString = spl[1].substring(0, spl[1].length()-3);
                String startTwoID = getID(twoString, DFAtwo.getIdName());
                for (String s : alphabet) {
                    StringBuilder toAdd = new StringBuilder();
                    String endOneID = one.get(startOneID).get(s);
                    String endTwoID = two.get(startTwoID).get(s);
                    toAdd.append("(");
                    toAdd.append(oneString+".1");
                    toAdd.append(" ");
                    toAdd.append(twoString+".2");
                    toAdd.append(") ; (");
                    toAdd.append(DFAone.getIdName().get(endOneID)+".1");
                    toAdd.append(" ");
                    toAdd.append(DFAone.getIdName().get(endTwoID)+".2");
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
