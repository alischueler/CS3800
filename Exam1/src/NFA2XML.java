import java.util.*;

public class NFA2XML {
    static NFA nfa1;

    public static void main(String[] args) {
        List<String> states = new ArrayList<>();
        StringBuilder stateSB = new StringBuilder();
        StringBuilder startSB = new StringBuilder();
        StringBuilder acceptSB = new StringBuilder();
        List<String> transitions = new ArrayList<>();
        HashSet<String> alphabet = new HashSet<>();
        int i = 0;
        String section = "";
        while (i < args.length) {
            //all states
            if (args[i].equals("states")) {
                section = "states";
                i += 1;
            }
            else if (args[i].equals("start")) {
                section = "start";
                i += 1;
            }
            else if (args[i].equals("accept")) {
                section = "accept";
                i += 1;
            }
            else if (args[i].equals("delta")) {
                section = "transition";
                i+=1;
            }

            else if (section.equals("states")) {
                if (stateSB.length() > 0) {
                    stateSB.append(" ");
                }
                stateSB.append(args[i]);
                i += 1;
            }
            //start states
            else if (section.equals("start")) {
                if (startSB.length() > 0) {
                    startSB.append(" ");
                }
                startSB.append(args[i]);
                i += 1;
            }
            //accept states
            else if (section.equals("accept")) {
                if (acceptSB.length() > 0) {
                    acceptSB.append(" ");
                }
                acceptSB.append(args[i]);
                i += 1;
            }
            //transition
            else if (section.equals("transition")) {
                StringBuilder toAdd = new StringBuilder();
                for (int j = 0; j < 3; j++) {
                    toAdd.append(args[i + j]);
                    toAdd.append(" ");
                    if (j == 1 & !alphabet.contains(args[i + j])) {
                        alphabet.add(args[i + j]);
                    }
                }
                transitions.add(toAdd.toString());
                i += 3;
            }
        }
        StringBuilder alpha = new StringBuilder();
        for (String s : alphabet) {
            alpha.append(s);
            alpha.append(" ");
        }
        states.add(stateSB.toString());
        states.add(startSB.toString());
        states.add(acceptSB.toString());
        transitions.add(alpha.toString());
        Map<String, String> map = findMap(stateSB.toString());
        NFA fig = new NFAMaker(states, transitions, map);
        NFAXML nfaxml = new NFAXML();
        List<String> output = nfaxml.createXML(fig, false);
        for (String s : output) {
            System.out.println(s);
        }
    }

    private static Map<String, String> findMap(String names) {
        String[] spl = names.split(" ");
        Map<String, String> toRet = new HashMap<String, String>();

        for (int i = 0; i < spl.length; i++) {
            toRet.put(spl[i], spl[i]);
        }
        return toRet;
    }
}
