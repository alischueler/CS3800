import java.util.*;

public class NFA2 {


    public static boolean run(NFA fig, String toRead) {
        String currChar = toRead.substring(0, 1);
        String toReadUpdate = toRead.substring(1);
        String startName = fig.getStart();
        LinkedHashSet<String> end = fig.getAccept();
        Map<String, String> idName = fig.getIdName();
        String start = getID(startName, idName);
        LinkedHashMap<String, LinkedHashMap<String, LinkedHashSet<String>>> delta = fig.getDelta();
        //System.out.println("START"+toRead+ " "+delta);
        boolean fin = runHelp(currChar, start, end, delta, toReadUpdate, idName);
        return fin;
    }

    private static boolean runHelp(String currChar, String currState, LinkedHashSet<String> accept, LinkedHashMap<String,
            LinkedHashMap<String, LinkedHashSet<String>>> delta, String toRead, Map<String, String> idName) {
        boolean toRet = false;
        //System.out.println("curr state " + currState + " curr char " + currChar+" toread "+toRead+" transitions "+delta.get(currState));
        LinkedHashMap<String, LinkedHashSet<String>> outer = delta.get(currState);
        if (currChar.length() > 0) {

            if (outer.containsKey("empty")) {
                //System.out.println("checking epsilon");
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

                LinkedHashSet<String> mtState = outer.get("empty");
                Iterator i = mtState.iterator();
                while (i.hasNext()) {
                    String ns = (String) i.next();
                    toRet = toRet || runHelp(newCurr, ns, accept, delta, toReadUpdate, idName);
                }
            }

            if (outer.containsKey(currChar)) {
                LinkedHashSet<String> newState = outer.get(currChar);
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
                Iterator i = newState.iterator();
                while (i.hasNext()) {
                    String ns = (String) i.next();
                    toRet = toRet || runHelp(newCurr, ns, accept, delta, toReadUpdate, idName);
                }
            }
        }
        else {
            //System.out.println("checking "+currState);
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


    public static boolean checkAccept(LinkedHashSet<String> accepts, String state, Map<String, String> idName) {
        String name = idName.get(state);
        return accepts.contains(name);
    }
}