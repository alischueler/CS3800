import java.util.*;

public class DFA2 {


    public static boolean run(DFA fig, String toRead) {
        String currChar;
        String toReadUpdate;
        if (toRead.length()>0) {
            currChar = toRead.substring(0, 1);
            toReadUpdate = toRead.substring(1);
        }
        else {
            currChar = toRead;
            toReadUpdate = "";
        }
        String startName = fig.getStart();
        HashSet<String> end = fig.getAccept();
        Map<String, String> idName = fig.getIdName();
        String start = getID(startName, idName);
        LinkedHashMap<String, LinkedHashMap<String, String>> delta = fig.getDelta();
        boolean fin = runHelp(currChar, start, end, delta, toReadUpdate, idName);
        return fin;
    }

    private static boolean runHelp(String currChar, String currState, HashSet<String> accept, HashMap<String,
            LinkedHashMap<String, String>> delta, String toRead, Map<String, String> idName) {
        //System.out.println("currChar "+currChar+ " curr state "+currState+ " to read "+toRead);
        boolean toRet = false;
        LinkedHashMap<String, String> outer = delta.get(currState);
        if (currChar.length() > 0) {
            if (outer.containsKey(currChar)) {
                String newState = outer.get(currChar);
                String newCurr;
                String toReadUpdate;
                if (toRead.length() > 1) {
                    newCurr = toRead.substring(0, 1);
                    toReadUpdate = toRead.substring(1);
                }
                else if (toRead.length() ==1) {
                    newCurr = toRead;
                    toReadUpdate = "";
                }
                else {
                    newCurr = "";
                    toReadUpdate = "";
                }
                toRet = toRet || runHelp(newCurr, newState, accept, delta, toReadUpdate, idName);
            }
//            else {
//                toRet = toRet || false;
//            }
        }
        else {
            toRet = toRet || (checkAccept(accept, currState, idName));
        }
        return toRet;
    }


    public static String getID(String state, Map<String, String> idName) {
        String s = "";
        for (Map.Entry m : idName.entrySet()) {
            if (state.equals(m.getValue())) {
                s = (String) m.getKey();
            }
        }
        return s;
    }


    public static boolean checkAccept(HashSet<String> accepts, String state, Map<String, String> idName) {
        String name = idName.get(state);
        return accepts.contains(name);
    }
}