import java.util.*;

public class parse {

    public static CFG parseArgs(String[] args) {
        List<String> variables = new ArrayList<>();
        List<String> terminals = new ArrayList<>();
        List<String> rules = new ArrayList<>();
        String start = "";
        int i = 0;
        String section = "";
        while (i < args.length) {
            //all states
            if (args[i].equals("variables")) {
                section = "variables";
                i += 1;
            } else if (args[i].equals("terminals")) {
                section = "terminals";
                i += 1;
            } else if (args[i].equals("rules")) {
                section = "rules";
                i += 1;
            } else if (args[i].equals("start")) {
                section = "start";
                i += 1;
            }
            //variables
            else if (section.equals("variables")) {
                variables.add(args[i]);
                i += 1;
            }
            //terminals
            else if (section.equals("terminals")) {
                terminals.add(args[i]);
                i += 1;
            }
            //accept states
            else if (section.equals("start")) {
                start = args[i];
                i += 1;
            }
            //transition
            else if (section.equals("rules")) {
                StringBuilder toAdd = new StringBuilder();
                for (int j = 0; j < 2; j++) {
                    if (j==1) {
                        toAdd.append(" ");
                    }
                    toAdd.append(args[i + j]);
                }
              //  System.out.println(toAdd);
                rules.add(toAdd.toString());
                i += 2;
            }
        }
        LinkedHashSet<Variable> variables2 = convertVariable(variables);
        LinkedHashSet<Terminal> terminals2 = convertTerminal(terminals);
        LinkedHashMap<Variable, LinkedHashSet<String>> rules2 = convertRules(rules, variables2, terminals2);
        CFGMaker fig = new CFGMaker(terminals2, variables2, rules2, findVariable(variables2, start));
        return fig;
    }

    public static LinkedHashSet<Variable> convertVariable(List<String> variables) {
        LinkedHashSet<Variable> toRet = new LinkedHashSet<>();
        for (String s : variables) {
            if (checkVariables(toRet, s)) {
                toRet.add(new Variable(s));
            }
        }
        return toRet;
    }

    public static LinkedHashSet<Terminal> convertTerminal(List<String> terminals) {
        LinkedHashSet<Terminal> toRet = new LinkedHashSet<>();
        for (String s : terminals) {
            if (checkTerminal(toRet, s)) {
                toRet.add(new Terminal(s));
            }
        }
        return toRet;
    }

    public static LinkedHashMap<Variable, LinkedHashSet<String>> convertRules(List<String> rules,
                                                                              LinkedHashSet<Variable> variables,
                                                                              LinkedHashSet<Terminal> terminals) {
        LinkedHashMap<Variable, LinkedHashSet<String>> toRet = new LinkedHashMap<>();
        for (int i = 0; i<rules.size();i++) {
            String[] spl = rules.get(i).split(" ");
            Variable v = findVariable(variables, spl[0]);
            if (toRet.containsKey(v)) {
                LinkedHashSet<String> toAdd = toRet.get(v);
                toAdd.add(spl[1]);
                toRet.replace(v, toAdd);
            }
            else {
                LinkedHashSet<String> toAdd = new LinkedHashSet<>();
                toAdd.add(spl[1]);
                toRet.put(v, toAdd);
            }
        }
        return toRet;
    }

    public static CFG parseXML(Readable file) {
        LinkedHashSet<Variable> variables = new LinkedHashSet<>();
        LinkedHashMap<Variable, LinkedHashSet<String>> rights = new LinkedHashMap<>();
        Variable start = new Variable("");
        int count = 0;
        Scanner s = new Scanner(file);
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
        //List<Rule> rules = updateAllRule(allterminals, variables, rights);
        //System.out.println(allterminals);
       // System.out.println(variables);
       // System.out.println(rights);
       // System.out.println(start);
       // System.out.println(rules);
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