import java.util.*;

public class CFGMaker implements CFG {
    List<Variable> variables;
    List<Terminal> terminals;
    LinkedHashMap<Variable, LinkedHashSet<String>> rules;
    Variable start;

    public CFGMaker(LinkedHashSet<Terminal> terminals, LinkedHashSet<Variable> variable, LinkedHashMap<Variable, LinkedHashSet<String>> rules, Variable start) {
        this.terminals = new ArrayList<>(terminals);
        this.variables = new ArrayList<>(variable);
        this.start = start;
        this.rules = rules;
    }

    public CFGMaker(List<Variable> variable, LinkedHashMap<Variable, LinkedHashSet<String>> rules) {
        this.variables = variable;
        this.start = variable.get(0);
        this.rules = rules;
        this.terminals = findTerminals(rules);
    }


    public List<Terminal> findTerminals(LinkedHashMap<Variable, LinkedHashSet<String>> rules) {
        LinkedHashSet<Terminal> ts = new LinkedHashSet<>();
        for (Map.Entry<Variable, LinkedHashSet<String>> r : rules.entrySet()) {
            LinkedHashSet<String> vals = r.getValue();
            for (String s : vals ) {
                for (int i = 0; i< s.length(); i++) {
                    String curr = Character.toString(s.charAt(i));
                    if (!checkPresent(variables, curr)) {
                        ts.add(new Terminal(curr));
                    }
                }
            }
        }
        return new ArrayList<>(ts);
    }


    public LinkedHashMap<Variable, LinkedHashSet<String>> getRules() {
        return rules;
    }

    @Override
    public List<Terminal> getTerminals() {
        return terminals;
    }

    @Override
    public List<Variable> getVariables() {
        return variables;
    }


    public boolean checkForRules(String s) {
        boolean toRet = false;
        for (Variable v : variables) {
            if (s.contains(v.toString())) {
                toRet = true;
            }
        }
        return toRet;
    }

    public static Variable findVariable(List<Variable> variables, String s) {
        Variable toRet = new Variable("");
        for (Variable v : variables) {
            if (v.toString().equals(s)) {
                toRet = v;
            }
        }
        return toRet;
    }

    public List<String> findPossibleString(List<String> productions, int size) {
        size -= 1;
        List<String> toRet = new ArrayList<>();
        if (size >= 1) {
            for (String s : productions) {
                List<String> notAll = findNotAll(s);
                toRet.addAll(notAll);
                List<String> one = findPossibleString(notAll, size);
                toRet.addAll(one);
            }
        } else {
            toRet.addAll(productions);
            toRet.removeIf(this::checkForRules);
        }
        return toRet;
    }

    public List<String> findNotAll(String s) {
        List<String> toRet = new ArrayList<>();
        StringBuilder var = new StringBuilder();
        for (int i = 0; i < s.length(); i++) {
            if (checkPresent(variables, Character.toString(s.charAt(i)))) {
                var.append((s.charAt(i)));
            }
        }
        List<String> possibleVars = permute(var.toString());
        for (String p : possibleVars) {
            int varcount = 0;
            List<String> next = new ArrayList<>();
            for (int i = 0; i < s.length(); i++) {
                if (checkPresent(variables, Character.toString(s.charAt(i)))) {
                    //upper case stays
                    if (Character.isUpperCase(p.charAt(varcount))) {
                        List<String> next2 = new ArrayList<>();
                        if (next.size() > 0) {
                            for (String str : next) {
                                next2.add(str + (s.charAt(i)));
                            }
                            next = next2;
                        } else {
                            next.add(Character.toString(s.charAt(i)));
                        }
                        varcount += 1;
                    }
                    //lower case means switch
                    else {
                        Variable v = findVariable(variables, Character.toString(s.charAt(i)));
                        LinkedHashSet<String> rule = rules.get(v);
                        List<String> next2 = new ArrayList<>();
                        if (next.size() > 0) {
                            for (String str : next) {
                                for (String r : rule) {
                                    next2.add(str + r);
                                }
                            }
                            next = next2;
                        } else {
                            next.addAll(rule);
                        }
                        varcount += 1;
                    }
                } else {
                    List<String> next2 = new ArrayList<>();
                    if (next.size() > 0) {
                        for (String str : next) {
                            next2.add(str + (s.charAt(i)));
                        }
                        next = next2;
                    } else {
                        next.add((Character.toString(s.charAt(i))));
                    }
                }
            }
            toRet.addAll(next);
        }
        return toRet;
    }

    static List<String> permute(String input) {
        List<String> combos = new ArrayList<>();
        int n = input.length();
        int max = 1 << n;
        input = input.toLowerCase();
        for (int i = 0; i < max; i++) {
            char[] combination = input.toCharArray();
            for (int j = 0; j < n; j++) {
                if (((i >> j) & 1) == 1)
                    combination[j] = (char) (combination[j] - 32);
            }
            String toAdd = makeCombo(combination);
            combos.add(toAdd);
        }
        return combos;
    }

    public static String makeCombo(char[] arr) {
        StringBuilder sb = new StringBuilder();
        for (char c : arr) {
            sb.append((c));
        }
        return sb.toString();
    }


    public static boolean checkPresent(List<Variable> variables, String s) {
        boolean toRet = false;
        for (Variable v : variables) {
            if (v.toString().equals(s)) {
                toRet = true;
            }
        }
        return toRet;
    }

    public CFG makeChomsky() {
        List<Variable> nullable = new ArrayList<>();
        LinkedHashMap<Variable, LinkedHashSet<String>> newRules = new LinkedHashMap<>(rules);
        List<Variable> newVariables = new ArrayList<>(variables);
        boolean added = true;
        while (added) {
            LinkedHashMap<Variable, LinkedHashSet<String>> addingRules = new LinkedHashMap<>();
            LinkedHashMap<Variable, LinkedHashSet<String>> removingRules = new LinkedHashMap<>();

         //   System.out.println(rules);
            //go through rules, if epsilon is rhs, add lhs to nullable
            for (Map.Entry<Variable, LinkedHashSet<String>> r : rules.entrySet()) {
           //     System.out.println(r);
                Variable lhs = r.getKey();
                LinkedHashSet<String> allRHS = r.getValue();
            //    System.out.println(allRHS);
                for (String s : allRHS) {
               //     System.out.println(s);
                    if (s.equals("")) {
                  //      System.out.println(allRHS);
                        nullable.add(lhs);
                  //      System.out.println(allRHS);
                        LinkedHashSet<String> existing = newRules.get(lhs);
                    //    System.out.println(allRHS+"here");
                        LinkedHashSet<String> newExisting = new LinkedHashSet<>(existing);
                        newExisting.remove(s);
                    //    System.out.println(allRHS);
                        newRules.replace(lhs, newExisting);
                    //    System.out.println(allRHS);
                        if (allRHS.size() == 1 && newRules.containsKey(lhs)) {
                            newVariables.remove(lhs);
                       //     System.out.println(allRHS);
                        }
                   //     System.out.println(allRHS);
                    }
                }
            }

            //go through copy of rules, for all rhs that contain a nullable, make a new rules
            for (Map.Entry<Variable, LinkedHashSet<String>> r : newRules.entrySet()) {
                Variable lhs = r.getKey();
                LinkedHashSet<String> allRHS = r.getValue();
                StringBuilder sb = new StringBuilder();
                for (String rhs : allRHS) {
                    LinkedHashSet<Variable> nullsSeen = new LinkedHashSet<>();
                    for(int i = 0; i< rhs.length(); i++) {
                        String curr = Character.toString(rhs.charAt(i));
                        if (!checkPresent(nullable, curr)) {
                            sb.append(curr);
                        }
                        else {
                            nullsSeen.add(findVariable(variables, curr));
                        }
                    }
                    if (!sb.toString().equals(rhs)) {
                        if (addingRules.containsKey(lhs)) {
                            LinkedHashSet<String> toAdd = addingRules.get(lhs);
                            toAdd.add(sb.toString());
                            addingRules.replace(lhs, toAdd);
                        } else {
                            LinkedHashSet<String> toAdd = new LinkedHashSet<>();
                            toAdd.add(sb.toString());
                            addingRules.put(lhs, toAdd);
                        }
                        if (nullsSeen.size()>0) {
                            if (removingRules.containsKey(lhs)) {
                                LinkedHashSet<String> toAdd = removingRules.get(lhs);
                                toAdd.add(rhs);
                                removingRules.replace(lhs, toAdd);
                            }
                            else {
                                LinkedHashSet<String> toAdd = new LinkedHashSet<>();
                                toAdd.add(rhs);
                                removingRules.put(lhs, toAdd);
                            }
                        }
                    }
                }
            }
            //update the new rules with rules to add and rules to remove
            for (Map.Entry<Variable, LinkedHashSet<String>> a : addingRules.entrySet()) {
                Variable v = a.getKey();
                LinkedHashSet<String> toAdd = addingRules.get(v);
                if (newRules.containsKey(v)) {
                    LinkedHashSet<String> existing = newRules.get(v);
                    existing.addAll(toAdd);
                    addingRules.replace(v, existing);
                } else {
                    LinkedHashSet<String> existing = new LinkedHashSet<>();
                    toAdd.addAll(toAdd);
                    addingRules.put(v, existing);
                }
            }
            for (Map.Entry<Variable, LinkedHashSet<String>> a : removingRules.entrySet()) {
                Variable v = a.getKey();
                LinkedHashSet<String> toRem = removingRules.get(v);
                if (newRules.containsKey(v)) {
                    LinkedHashSet<String> existing = newRules.get(v);
                    existing.removeAll(toRem);
                    addingRules.replace(v, existing);
                }
            }

            //add all epsilon transitions to a list of epsilons
            LinkedHashMap<Variable, String> epsilon = new LinkedHashMap<>();
            for (Map.Entry<Variable, LinkedHashSet<String>> r : newRules.entrySet()) {
                Variable v = r.getKey();
                LinkedHashSet<String> allRHS = r.getValue();
                for (String rhs : allRHS) {
                    if (rhs.equals("")) {
                        epsilon.put(v, rhs);
                    }
                }
            }

            //remove all of these epsilon rules from our rules
            for (Map.Entry<Variable, String> a : epsilon.entrySet()) {
                Variable v = a.getKey();
                String toRem = epsilon.get(v);
                if (newRules.containsKey(v)) {
                    LinkedHashSet<String> existing = newRules.get(v);
                    existing.remove(toRem);
                    addingRules.replace(v, existing);
                }
            }

            //remove all nullable epsilon transitions
            LinkedHashMap<Variable,LinkedHashSet<String>> finRules = new LinkedHashMap<>(newRules);
            for (Map.Entry<Variable, LinkedHashSet<String>> r : newRules.entrySet()) {
                Variable lhs = r.getKey();
                LinkedHashSet<String> allRHS = r.getValue();
                for (String rhs : allRHS) {
                    if (rhs.equals("")) {
                        LinkedHashSet<String> existing = finRules.get(lhs);
                        existing.remove(rhs);
                        finRules.replace(lhs, existing);
                    }
                }
            }

            //check to see if anything was added this round
            int newRulesSize = 0;
            int finRulesSize = 0;
            for (Map.Entry<Variable, LinkedHashSet<String>> r : newRules.entrySet()) {
                LinkedHashSet<String> vals = r.getValue();
                newRulesSize+= vals.size();
            }
            for (Map.Entry<Variable, LinkedHashSet<String>> f : finRules.entrySet()) {
                LinkedHashSet<String> vals = f.getValue();
               finRulesSize += vals.size();
            }
            if (newRulesSize == finRulesSize) {
                added = false;
            }
            newRules = new LinkedHashMap<>(finRules);
        }

        return new CFGMaker(newVariables, newRules);
    }
}
