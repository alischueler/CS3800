
import java.util.*;

/**
 * A class that initializes an instance of Fig 1.5 and has methods to extract its data
 */
public class DFA2 {

    /**
     * A main method to use the keyword passed in the command line to outprint a piece of information about fig 1.5 DFA
     *
     * @param args n/a information is read from command line, nothing is taken from args
     */
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String toRead = "";
        try {
            toRead = scanner.next();
            mainHelp(toRead, args);
        }
        catch (NoSuchElementException e){
            System.out.println("reject");
        }
    }

    public static void mainHelp(String toRead, String[] args) {
        DFA fig;
        //redundant but will make easy to add more options later
        switch(args[0]){
            case "fig1.4": fig = new DFA14();
                if (run(fig, toRead)) {
                    System.out.println("accept");
                } else {
                    System.out.println("reject");
                }
                break;
            default: fig = new DFA14();
        }
    }

    public static boolean run(DFA fig, String toRead) {
        String currChar = toRead.substring(0, 1);
        String toReadUpdate = toRead.substring(1);
        String startName = fig.getStart();
        HashSet<String> end = fig.getAccept();
        Map<String, String> idName = fig.getIdName();
        String start = getID(startName, idName);
        LinkedHashMap<String, LinkedHashMap<String, String>> delta = fig.getDelta();
        return runHelp(currChar, start, end, delta, toReadUpdate, idName);
    }

    private static boolean runHelp(String currChar, String currState, HashSet<String> accept, LinkedHashMap<String,
            LinkedHashMap<String, String>> delta, String toRead, Map<String, String> idName) {
        LinkedHashMap<String, String> outer = delta.get(currState);
        String newState = outer.get(currChar);
        if (toRead.length()>0) {
            String newCurr = toRead.substring(0, 1);
            String toReadUpdate = toRead.substring(1);
            return runHelp(newCurr, newState, accept, delta, toReadUpdate, idName);
        }
        else {
            if (checkAccept(accept, newState, idName)) {
                return true;
            }
            else {
                return false;
            }
        }
    }

    public static String getID(String state, Map<String, String> idName) {
        String s ="";
        for (Map.Entry m : idName.entrySet()) {
            if (state.equals(m.getValue())) {
                s= (String)m.getKey();
            }
        }
        return s;
    }


    public static boolean checkAccept(HashSet<String> accepts, String state, Map<String, String> idName) {
        String name = idName.get(state);
        return accepts.contains(name);
    }
}