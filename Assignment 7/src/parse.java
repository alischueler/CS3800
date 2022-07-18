import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;

public class parse {
    public static InputStreamReader in1;

    public static PDA parseArgs(String[] args) {
        LinkedHashSet<String> states = new LinkedHashSet<>();
        List<State> stateList;
        String start = "";
        State startState;
        LinkedHashSet<String> accept = new LinkedHashSet<>();
        List<State> acceptList;
        LinkedHashSet<String> transitions = new LinkedHashSet<>();
        List<PDATransition> transitionList;
        int i = 0;
        String section = "";
        while (i < args.length) {
            //all states
            if (args[i].equals("states")) {
                section = "states";
                i += 1;
            } else if (args[i].equals("start")) {
                section = "start";
                i += 1;
            } else if (args[i].equals("accept")) {
                section = "accept";
                i += 1;
            } else if (args[i].equals("transitions")) {
                section = "transitions";
                i += 1;
            }
            //variables
            else if (section.equals("states")) {
                states.add(args[i]+" "+args[i+1]);
                i += 2;
            }
            //terminals
            else if (section.equals("start")) {
                start = (args[i]+" "+args[i+1]);
                i += 2;
            }
            //accept states
            else if (section.equals("accept")) {
                accept.add(args[i]+" "+args[i+1]);
                i += 2;
            }
            //transition
            else if (section.equals("transitions")) {
                StringBuilder toAdd = new StringBuilder();
                for (int j = 0; j < 5; j++) {
                    if (args[i+j].equals("dollar")) {
                        toAdd.append("$");
                    }
                    else {
                        toAdd.append(args[i + j]);
                    }
                    if (j!=4) {
                        toAdd.append(" ");
                    }
                }
                transitions.add(toAdd.toString());
                i += 5;
            }
        }
        List<List<State>> allstate = convertState(states, start, accept);
        stateList = allstate.get(0);
        startState  = allstate.get(1).get(0);
        acceptList = allstate.get(2);
        transitionList = convertTransitions(transitions);
        PDAMaker fig = new PDAMaker(stateList, startState, acceptList, transitionList);
        return fig;
    }

    public static List<List<State>> convertState(LinkedHashSet<String> states, String start, LinkedHashSet<String> accept) {
        List<List<State>> allStates = new ArrayList<>();
        List<State> onlystates = new ArrayList<>();
        List<State> startState = new ArrayList<>();
        List<State> acceptState = new ArrayList<>();
        for (String s : states) {
            String[] spl = s.split(" ");
            State st = new State(spl[0], spl[1]);
            onlystates.add(st);
            if (start.equals(s)) {
                startState.add(st);
            }
            if (accept.contains(s)) {
                acceptState.add(st);
            }
        }
        allStates.add(onlystates);
        allStates.add(startState);
        allStates.add(acceptState);
        return allStates;
    }

    public static List<PDATransition> convertTransitions(LinkedHashSet<String> transitions) {
        List<PDATransition> transitionList = new ArrayList<>();
        for (String s : transitions) {
            String[] spl = s.split(" ");
            PDATransition t = new PDATransition(spl[0], spl[1], spl[2], spl[3], spl[4]);
            transitionList.add(t);
        }
        return transitionList;
    }


    public static NFA callParseNFA(String fileName) {
        createInputReader(fileName);
        List<String> states = parseStatesFile(in1);
        List<List<State>> statesList = convertState(states);
        createInputReader(fileName);
        List<String> transitions = parseTransitionsFile(in1);
        List<String> alpha = new ArrayList<>();
        List<NFATransition> transitionList = convertTransitions(transitions, alpha);
        return new NFAMaker(statesList.get(0), statesList.get(1).get(0), statesList.get(2), transitionList, alpha);
    }

    public static void createInputReader(String fileName) {
        InputStream file;
        try {
            file = new FileInputStream(fileName);
        }
        catch (IOException e) {
            throw new IllegalArgumentException("file not found");
        }
        in1 = new InputStreamReader(file);
    }

    public static List<List<State>> convertState(List<String> states) {
        List<List<State>> toRet = new ArrayList<>();
        String stateNames = states.get(0);
        String[] stateNamesArr = stateNames.split(" ");
        List<State> stateList = new ArrayList<>();
        String startNames = states.get(1);
        List<State> startList = new ArrayList<>();
        String acceptNames = states.get(2);
        List<State> acceptList = new ArrayList<>();
        for (int i = 0; i< stateNamesArr.length; i++) {
            State st = new State(stateNamesArr[i], stateNamesArr[i+1]);
            stateList.add(st);
            if (startNames.equals(stateNamesArr[i]+" "+stateNamesArr[i+1])) {
                startList.add(st);
            }
            if (acceptNames.contains(stateNamesArr[i]+" "+stateNamesArr[i+1])) {
                acceptList.add(st);
            }
            i+=1;
        }
        toRet.add(stateList);
        toRet.add(startList);
        toRet.add(acceptList);
        return toRet;
    }

    public static List<NFATransition> convertTransitions(List<String> transitions, List<String> alpha) {
        List<NFATransition> transitionList = new ArrayList<>();
        for (int i = 0; i <transitions.size();i++) {
            if (i== transitions.size()-1) {
                String[] alphaspl = transitions.get(i).split(" ");
                alpha.addAll(Arrays.asList(alphaspl));
            }
            else {
                String[] spl = transitions.get(i).split(" ");
                NFATransition t = new NFATransition(spl[0], spl[1], spl[2]);
                transitionList.add(t);
            }
        }
        return transitionList;
    }

    //order start, end, sym
    public static List<String> parseTransitionsFile(Readable file) {
        List<String> los = new ArrayList<>(3);
        StringBuilder allRelData = new StringBuilder();
        StringBuilder transition = new StringBuilder();
        LinkedHashSet<String> alpha = new LinkedHashSet<>();
        Scanner s = new Scanner(file);
        while (s.hasNextLine()) {
            //find all words between <state and </state>
            String word = s.nextLine();
            if (word.contains("<transition>") && (word.contains("</transition>"))) {
                allRelData.append(word);
                //do something
                findTransitionInfo(allRelData.toString().split(" "), transition, alpha);
                los.add(transition.toString());
                allRelData = new StringBuilder();
                transition = new StringBuilder();
            }
            else if (word.contains("<transition>") && !word.contains("<transition />")) {
                allRelData.append(word);
            }
            else if (word.contains("</transition>")) {
                allRelData.append(" ");
                allRelData.append(word);
                findTransitionInfo(allRelData.toString().split(" "), transition, alpha);
                los.add(transition.toString());
                allRelData = new StringBuilder();
                transition = new StringBuilder();
            }
            else if (allRelData.length()!=0) {
                allRelData.append(" ");
                allRelData.append(word);
            }
        }
        StringBuilder alphabet = new StringBuilder();
        for (String letter : alpha) {
            alphabet.append(letter);
            alphabet.append(" ");
        }
        los.add(alphabet.toString());
        return los;
    }


    /**
     * Uses the readable file to find all the names of accept states, names of states that are initial states, and
     * names of states that are end states and puts them all in a list
     * @param file the file to be read for information
     * @return a list of string with (1) names of states, (2) names of start states, (3) names of initial states
     */
    public static List<String> parseStatesFile(Readable file) {
        List<String> los = new ArrayList<>(3);
        StringBuilder allRelData = new StringBuilder();
        StringBuilder namesL = new StringBuilder();
        StringBuilder startL = new StringBuilder();
        StringBuilder endL = new StringBuilder();
        Scanner s = new Scanner(file);
        while (s.hasNextLine()) {
            //find all words between <state and </state>
            String word = s.nextLine();
            if (word.contains("<state") && (word.contains("</state>") || word.contains("/>"))) {
                allRelData.append(word);
                //do something
                findStatesInfo(allRelData.toString().split(" "), namesL, startL, endL);
                allRelData = new StringBuilder();
            }
            else if (word.contains("<state ") && !word.contains("<state />")) {
                allRelData.append(word);
            }
            else if (word.contains("</state>")) {
                allRelData.append(" ");
                allRelData.append(word);
                findStatesInfo(allRelData.toString().split(" "), namesL, startL, endL);
                allRelData = new StringBuilder();
            }
            else if (word.contains("/>") && (!word.contains("<final/>") && !word.contains("<initial/>"))) {
                allRelData.append(" ");
                allRelData.append(word);
                findStatesInfo(allRelData.toString().split(" "), namesL, startL, endL);
                allRelData = new StringBuilder();
            }
            else if (allRelData.length()!=0) {
                allRelData.append(" ");
                allRelData.append(word);
            }
        }
        String namesString = namesL.toString();
        String startString = startL.toString();
        String endString = endL.toString();
        los.add(namesString);
        los.add(startString);
        los.add(endString);
        return los;
    }

    public static void findTransitionInfo(String[] line, StringBuilder toAdd, LinkedHashSet<String> alpha) {
        String start = "";
        String end = "";
        String read = "";
        for (int i = 0; i<line.length; i++) {
            String curr = line[i];
            if (curr.contains("<from>")) {
                String[] l = curr.split("<from>");
                for (String s : l) {
                    if (s.contains("</from>")) {
                        String[] ids = s.split("</from>");
                        start = ids[0];
                    }
                }
            }
            if (curr.contains("<to")) {
                String[] l = curr.split("<to>");
                for (String s : l) {
                    if (s.contains("</to>")) {
                        String[] ids = s.split("</to>");
                        end = ids[0];
                    }
                }
            }
            if (curr.contains("<read>")) {
                String[] l = curr.split("<read>");
                for (String s : l) {
                    if (s.contains("</read>")) {
                        String[] ids = s.split("</read>");
                        read = ids[0];
                        alpha.add(read);
                    }
                }
            }
            else if (curr.contains("<read/>") && !curr.contains("<read>")) {
                read = "empty";
                alpha.add(read);
            }
        }
        toAdd.append(start);
        toAdd.append(" ");
        toAdd.append(end);
        toAdd.append(" ");
        toAdd.append(read);
    }

    /**
     * Uses the given array of strings to see if it contains a name, initial, or final and updates the names, start, or
     * end strinbuilders if the array contains the applicable words
     * @param words an array of strings from one line in the file, split on spaces
     * @param namesL the Stringbuilder containing the names of all states separated by commas
     * @param startL the Stringbuilder containing the names of all states that are initial states, separated by commas
     * @param endL the Stringbuilder containing the names of all states that are accept states, separated by commas
     */
    public static void findStatesInfo(String[] words, StringBuilder namesL, StringBuilder startL, StringBuilder endL) {
        String names = findPhrase(words, "name=");
        if (names.length()>=1) {
            if (namesL.length()!=0) {
                namesL.append(" ");
            }
            namesL.append(names);
        }
        String start = findPhrase(words, "<initial/>");
        if (start.length()>=1) {
            if (startL.length()!=0) {
                startL.append(" ");
            }
            startL.append(start);
        }
        String end = findPhrase(words, "<final/>");
        if (end.length()>=1) {
            if (endL.length()!=0) {
                endL.append(" ");
            }
            endL.append(end);
        }
    }

    /**
     * Determines if a string is in an array of string, representing a line in the file, and returns the name of that
     * state if it is, returns a blank string if not
     * @param line the whole line of code, or the whole chunk of code from < to > that spans across multiple lines
     * @param find the string to be found in the given line
     * @return a string, the name of the the state, that has the specific string associated with it
     */
    public static String findPhrase(String[] line, String find) {
        StringBuilder toRet = new StringBuilder();
        String found = "";
        for (int i = 0; i<line.length; i++) {
            String curr = line[i];
            if (curr.contains("name")) {
                String[] quotes = curr.split("\"");
                toRet.append(" ");
                toRet.append(quotes[1]);
            }
            if (curr.contains("id")) {
                String[] quotes = curr.split("\"");
                toRet.append(quotes[1]);
            }
            if (curr.contains(find)) {
                found = toRet.toString();
            }
        }
        return found;
    }

    public static PDA parseXMLPDA(String filename) {
        createInputReader(filename);
        List<String> states = parseStatesFile(in1);
        List<List<State>> statesList = convertState(states);
        createInputReader(filename);
        List<String> transitions = parseTransitionsFilePDA(in1);
        List<String> alpha = new ArrayList<>();
        List<PDATransition> transitionList = convertTransitionsPDA(transitions, alpha);
        return new PDAMaker(statesList.get(0), statesList.get(1).get(0), statesList.get(2), transitionList);
    }

    //order start, end, sym
    public static List<String> parseTransitionsFilePDA(Readable file) {
        List<String> los = new ArrayList<>(3);
        StringBuilder allRelData = new StringBuilder();
        StringBuilder transition = new StringBuilder();
        LinkedHashSet<String> alpha = new LinkedHashSet<>();
        Scanner s = new Scanner(file);
        while (s.hasNextLine()) {
            //find all words between <state and </state>
            String word = s.nextLine();
            if (word.contains("<transition>") && (word.contains("</transition>"))) {
                allRelData.append(word);
                //do something
                findTransitionInfoPDA(allRelData.toString().split(" "), transition);
                los.add(transition.toString());
                allRelData = new StringBuilder();
                transition = new StringBuilder();
            }
            else if (word.contains("<transition>") && !word.contains("<transition />")) {
                allRelData.append(word);
            }
            else if (word.contains("</transition>")) {
                allRelData.append(" ");
                allRelData.append(word);
                findTransitionInfoPDA(allRelData.toString().split(" "), transition);
                los.add(transition.toString());
                allRelData = new StringBuilder();
                transition = new StringBuilder();
            }
            else if (allRelData.length()!=0) {
                allRelData.append(" ");
                allRelData.append(word);
            }
        }
//        StringBuilder alphabet = new StringBuilder();
//        for (String letter : alpha) {
//            alphabet.append(letter);
//            alphabet.append(" ");
//        }
//        los.add(alphabet.toString());
       // System.out.println(los.toString());
        return los;
    }

    public static void findTransitionInfoPDA(String[] line, StringBuilder toAdd) {
        String start = "";
        String end = "";
        String read = "";
        String pop = "";
        String push = "";
        for (int i = 0; i<line.length; i++) {
            String curr = line[i];
            if (curr.contains("<from>")) {
                String[] l = curr.split("<from>");
                for (String s : l) {
                    if (s.contains("</from>")) {
                        String[] ids = s.split("</from>");
                        start = ids[0];
                    }
                }
            }
            if (curr.contains("<to")) {
                String[] l = curr.split("<to>");
                for (String s : l) {
                    if (s.contains("</to>")) {
                        String[] ids = s.split("</to>");
                        end = ids[0];
                    }
                }
            }
            if (curr.contains("<read>")) {
                String[] l = curr.split("<read>");
                for (String s : l) {
                    if (s.contains("</read>")) {
                        String[] ids = s.split("</read>");
                        read = ids[0];
                    }
                }
            }
            if (curr.contains("<pop>")) {
                String[] l = curr.split("<pop>");
                for (String s : l) {
                    if (s.contains("</pop>")) {
                        String[] ids = s.split("</pop>");
                        pop = ids[0];
                    }
                }
            }
            if (curr.contains("<push>")) {
                String[] l = curr.split("<push>");
                for (String s : l) {
                    if (s.contains("</push>")) {
                        String[] ids = s.split("</push>");
                        push = ids[0];
                    }
                }
            }
            if (curr.contains("<read/>") && !curr.contains("<read>")) {
                read = "empty";
            }
            if (curr.contains("<pop/>") && !curr.contains("<pop>")) {
                pop = "empty";
            }
            if (curr.contains("<push/>") && !curr.contains("<push>")) {
                push = "empty";
            }
        }
        toAdd.append(start);
        toAdd.append(" ");
        toAdd.append(end);
        toAdd.append(" ");
        toAdd.append(read);
        toAdd.append(" ");
        toAdd.append(pop);
        toAdd.append(" ");
        toAdd.append(push);
       // System.out.println(toAdd);
    }

    public static List<PDATransition> convertTransitionsPDA(List<String> transitions, List<String> alpha) {
        List<PDATransition> transitionList = new ArrayList<>();
        for (int i = 0; i <transitions.size();i++) {
                String[] spl = transitions.get(i).split(" ");
                PDATransition t = new PDATransition(spl[0], spl[1], spl[2], spl[3], spl[4]);
                transitionList.add(t);

        }
        return transitionList;
    }

    public static CFG parseXMLCFG(String filename) {
        createInputReader(filename);
        LinkedHashSet<Variable> variables = new LinkedHashSet<>();
        LinkedHashMap<Variable, LinkedHashSet<String>> rights = new LinkedHashMap<>();
        Variable start = new Variable("");
        int count = 0;
        Scanner s = new Scanner(in1);
        StringBuilder allData = new StringBuilder();
        while (s.hasNextLine()) {
            //find all words between <state and </state>
            String word = s.nextLine();
            //  System.out.println("new"+word);
            if (word.contains("<production") && (word.contains("</production>"))) {
                //      System.out.println("both");
                allData.append(word);
                if (count==0) {
                    start = getStart(allData, variables, rights);
                }
                //    System.out.println("data"+allData);
                else {
                    findRuleInfo(allData, variables, rights);
                }
                allData = new StringBuilder();
                count+=1;
            }
            else if (word.contains("<production")) {
                //     System.out.println("first");
                allData.append(word);
                //    System.out.println("data"+allData);
            }
            else if (word.contains("</production>")) {
                // System.out.println("last");
                allData.append(word);
                if (count==0) {
                    start = getStart(allData, variables, rights);
                }
                else {
                    // System.out.println("data"+allData);
                    findRuleInfo(allData, variables, rights);
                }
                allData = new StringBuilder();
                count+=1;
            }
            else if (allData.length()!=0) {
                allData.append(word);
            }
        }
        LinkedHashSet<Terminal> allterminals = findTerminals(variables, rights);
        CFG cfg = new CFGMaker(allterminals, variables, rights, start);
        return cfg;
    }

    public static Variable getStart(StringBuilder s, LinkedHashSet<Variable> variables, LinkedHashMap<Variable, LinkedHashSet<String>> rights) {
        String left = s.substring(s.indexOf("<left>")+6, s.indexOf("</left>"));
        String right;
        if (s.toString().contains("<right>") && s.toString().contains("</right>")) {
            //check if it contains upper case / a rule
            right  = s.substring(s.indexOf("<right>")+7, s.indexOf("</right>"));
        }
        else {
            right = "empty";
        }
        Variable v = new Variable(left);
        LinkedHashSet<String> toAdd = new LinkedHashSet<>();
        toAdd.add(right);
        rights.put(v, toAdd);
        variables.add(v);
        return v;
    }

    public static LinkedHashSet<Terminal> findTerminals(LinkedHashSet<Variable> variables, LinkedHashMap<Variable, LinkedHashSet<String>> rules) {
        LinkedHashSet<Terminal> lterms = new LinkedHashSet<>();
        for (LinkedHashSet<String> set:rules.values()) {
            for (String s : set) {
                for (int i =0; i<s.length();i++) {
                    if (checkVariables(variables, Character.toString(s.charAt(i))) && checkTerminal(lterms, Character.toString(s.charAt(i)))) {
                        lterms.add(new Terminal(Character.toString(s.charAt(i))));
                    }
                }
            }
        }
        return lterms;
    }

    public static boolean checkTerminal(LinkedHashSet<Terminal> terminals, String s) {
        boolean toRet = false;
        for (Terminal t : terminals) {
            if (t.toString().equals(s)) {
                toRet = true;
            }
        }
        return !toRet;
    }

    public static boolean checkVariables(LinkedHashSet<Variable> variables, String s) {
        boolean toRet = false;
        for (Variable v : variables) {
            if (v.toString().equals(s)) {
                toRet = true;
            }
        }
        return !toRet;
    }

    public static void findRuleInfo(StringBuilder line, LinkedHashSet<Variable> variables, LinkedHashMap<Variable, LinkedHashSet<String>> rights) {
        //isolate variable
        //System.out.println(line);
        String left = line.substring(line.indexOf("<left>")+6, line.indexOf("</left>"));
        String right;
        if (line.toString().contains("<right>") && line.toString().contains("</right>")) {
            //check if it contains upper case / a rule
            right  = line.substring(line.indexOf("<right>")+7, line.indexOf("</right>"));
        }
        else {
            right = "";
        }
        if (checkPresent(variables, left)) {
            Variable v = findVariable(variables, left);
            // System.out.println(rights+" "+v);
            LinkedHashSet<String> set = rights.get(v);
            // System.out.println(set);
            set.add(right);
            rights.replace(v, set);
        }
        else {
            Variable v = new Variable(left);
            variables.add(v);
            LinkedHashSet<String> set = new LinkedHashSet<>();
            set.add(right);
            rights.put(v, set);
        }
        // System.out.println(left);

    }

    public static Variable findVariable(LinkedHashSet<Variable> variables, String s) {
        Variable toRet = new Variable("");
        for (Variable v : variables) {
            if (v.toString().equals(s)) {
                //  System.out.println(v+" "+s);
                toRet = v;
            }
        }
        return toRet;
    }

    public static boolean checkPresent(LinkedHashSet<Variable> variables, String s) {
        boolean toRet = false;
        for (Variable v : variables) {
            if (v.toString().equals(s)) {
                toRet = true;
            }
        }
        return toRet;
    }

}