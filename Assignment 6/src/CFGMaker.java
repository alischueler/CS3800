import java.util.*;

public class CFGMaker implements CFG {
    List<Variable> variables;
    List<Terminal> terminals;
    LinkedHashMap<Variable, LinkedHashSet<String>> rules;
    Variable start;

    public CFGMaker(LinkedHashSet<Terminal> terminals, LinkedHashSet<Variable> variable, LinkedHashMap<Variable, LinkedHashSet<String>> rules, Variable start) {
        List<Terminal> terminals1 = new ArrayList<>();
        terminals1.addAll(terminals);
        this.terminals = terminals1;
        List<Variable> variables1 = new ArrayList<>();
        variables1.addAll(variable);
        this.variables = variables1;
        this.start = start;
        this.rules = rules;
    }

    public CFGMaker() {
    }

    public LinkedHashMap<Variable, LinkedHashSet<String>> getRules() {
        return rules;
    }

    @Override
    public LinkedHashSet<String> findString(int size) {
        LinkedHashSet<String> startProds = rules.get(start);
        List<String> prods = new ArrayList<>();
        prods.addAll(startProds);
        List<String> toRet = findPossibleString(prods, size);
        toRet.removeIf(this::checkForRules);
        return new LinkedHashSet<>(toRet);
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
                //  System.out.println(v+" "+s);
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

    @Override
    public boolean checkDerivs(List<String> derivations) {
        boolean toRet = false;
        if (checkLast(derivations)) {
            return false;
        } else {
            if (derivations.size() >= 1) {
                String curr = (derivations.get(0));
                derivations.remove(derivations.get(0));
                toRet = continueCheck(start.var, curr, derivations);
            }
        }
        return toRet;
    }

    public boolean continueCheck(String comparison, String currDeriv, List<String> nextDerivs) {
        boolean toRet = false;
        if (!comparison.equals(currDeriv)) {
            return false;
        } else {
            if (nextDerivs.size() >= 1) {
                String next = nextDerivs.get(0);
                List<String> notAll = findNotAll(currDeriv);
                List<String> possiblenextcomps = new ArrayList<>(notAll);
                if (compare(next, possiblenextcomps) >= 0) {
                    String conf = possiblenextcomps.get(compare(next, possiblenextcomps));
                    nextDerivs.remove(nextDerivs.get(0));
                    toRet = toRet || continueCheck(conf, next, nextDerivs);
                } else {
                    toRet = false;
                }
            } else {
                toRet = true;
            }
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

    public int compare(String next, List<String> possibleNext) {
        int toRet = -1;
        for (int i = 0; i < possibleNext.size(); i++) {
            if (possibleNext.get(i).equals(next)) {
                toRet = i;
            }
        }
        return toRet;
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

    public boolean checkLast(List<String> derivations) {
        boolean toRet = false;
        for (Map.Entry<Variable, LinkedHashSet<String>> r : rules.entrySet()) {
            if (derivations.get(derivations.size() - 1).contains(r.getKey().toString())) {
                toRet = true;
            }
        }
        return toRet;
    }

    public List<String> findPath(String start, String end) {
        int count = 0;
        boolean keep = true;
        List<String> order = new ArrayList<>();
        Map<Integer, List<String>> track = new LinkedHashMap<>();
        while (keep) {
            List<String> prods = findNotAll(start);
            List<String> children = findPossibleStrings(prods, count);
            track.put(count, children);
            if (children.contains(end)) {
                order.add(end);
                keep = false;
            }
            else {
                count+=1;
            }
        }
        for (int i = count-1; i>0; i--) {
           // System.out.println(i);
            List<String> minOne = track.get(i);
           // System.out.println(minOne);
            for (String s : minOne) {
                List<String> child = findNotAll(s);
                if (child.contains(end)) {
                    order.add(s);
                    end = s;
                    break;
                }
            }
        }
        order.add(start);
        Collections.reverse(order);
        return order;
    }

    public List<String> findPossibleStrings(List<String> productions, int size) {
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
        }
        return toRet;
    }

}
