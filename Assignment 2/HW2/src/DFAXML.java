import java.util.*;

public class DFAXML {


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
        DFA fig = new DFAMaker(states, transitions);
        List<String> output = createXML(fig, false);
        for (String s : output) {
            System.out.println(s);
        }
    }

    public static List<String> createXML(DFA fig, boolean id) {
        List<String> toRet = new ArrayList<>();
        int numStates = fig.getStates().size();
        //top of xml
        toRet.add("<automaton>");
        //states information
        toRet.add(" <!--The list of states.-->");
        HashSet<String> allStates2 = fig.getStates();
        List<String> allStates = new ArrayList<>();
        for (String s:allStates2) {
            allStates.add(s);
        }
        for (int i = 0; i < numStates; i++) {
            StringBuilder toAdd = new StringBuilder();
            String curr = allStates.get(i);
            toAdd.append("<state id=\"");
            if (id) {
                String currID = getID(curr, fig.getIdName());
                toAdd.append(currID);
            }
            else {
                toAdd.append(curr);
            }
            toAdd.append("\" name=\"");
            toAdd.append(curr);
            toAdd.append("\">");
            if (fig.getStart().equals(curr)) {
                toAdd.append("<initial/>");
            }
            if (fig.getAccept().contains(curr)) {
                toAdd.append("<final/>");
            }
            toAdd.append("</state>");
            toRet.add(toAdd.toString());
        }
        //transition information, use iterator
        toRet.add(" <!--The list of transitions.-->");
        LinkedHashMap<String, LinkedHashMap<String, String>> delta = fig.getDelta();
        Iterator<Map.Entry<String, LinkedHashMap<String, String>>> outer = delta.entrySet().iterator();
        while (outer.hasNext()) {
            Map.Entry<String, LinkedHashMap<String, String>> curr = outer.next();
            String start = curr.getKey();
            HashMap<String, String> val = curr.getValue();
            Iterator<Map.Entry<String, String>> inner = val.entrySet().iterator();
            while (inner.hasNext()) {
                StringBuilder toAdd = new StringBuilder();
                Map.Entry<String, String> currInner = inner.next();
                String sym = currInner.getKey();
                String end = currInner.getValue();
                toAdd.append("<transition><from>");
                toAdd.append(start);
                toAdd.append("</from><to>");
                toAdd.append(end);
                toAdd.append("</to><read>");
                toAdd.append(sym);
                toAdd.append("</read></transition>");
                toRet.add(toAdd.toString());
            }
        }
        //clossing the automation
        toRet.add("</automaton>");
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
