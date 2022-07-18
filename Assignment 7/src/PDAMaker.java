import java.util.*;

public class PDAMaker implements PDA {
    List<State> allStates;
    State start;
    List<State> accept;
    List<PDATransition> transitions;
    List<String> inputalpha;
    List<String> stackalpha;

    public PDAMaker(List<State> states, State start, List<State> accept, List<PDATransition> transitions) {
        this.allStates = states;
        this.start = start;
        this.accept = accept;
        this.transitions = transitions;
        this.inputalpha = getInputAlpha(transitions);
        this.stackalpha = getStackAlpha(transitions);
    }

    public List<String> getInputAlpha(List<PDATransition> trans) {
        LinkedHashSet<String> toAdd = new LinkedHashSet<>();
        for (PDATransition t: trans) {
            if (!t.getRead().equals("empty")) {
                toAdd.add(t.getRead());
            }
        }
        List<String> toRet = new ArrayList<>(toAdd);
        return toRet;
    }

    public List<String> getStackAlpha(List<PDATransition> trans) {
        LinkedHashSet<String> toAdd = new LinkedHashSet<>();
        for (PDATransition t: trans) {
            if (!t.getPush().equals("empty")) {
                for (int i = 0; i < t.getPush().length(); i++) {
                    toAdd.add(Character.toString(t.getPush().charAt(i)));
                }
            }
            if (!t.getPop().equals("empty")) {
                for (int i = 0; i < t.getPop().length(); i++) {
                    toAdd.add(Character.toString(t.getPop().charAt(i)));
                }
            }
        }
        return new ArrayList<>(toAdd);
    }

    @Override
    public List<PDATransition> getTransitions() {
        return transitions;
    }

    @Override
    public List<State> getStates() {
        return allStates;
    }

    @Override
    public State getStart() {
        return start;
    }


    @Override
    public List<State> getAccept() {
        return accept;
    }

    @Override
    public boolean run(String str, int bound) {
        boolean toRet = false;
        Stack<String> stack = new Stack<>();
        if (bound>=0) {
           toRet = toRet || runHelp(start, transitions, str, stack, bound);
        }
        else {
            toRet = toRet || runHelp(start, transitions, str, stack, 2147483647);
        }
        return toRet;
    }

    public boolean runHelp(State curr, List<PDATransition> trans, String input, Stack<String> stack, int bound) {
        boolean toRet = false;
        if (bound >=0) {
            if (input.length() == 0) {
                if (accept.contains(curr)) {
                    toRet = true;
                }
            }
                Map<State, Map<Stack<String>, String>> toTry = new HashMap<>();
                for (PDATransition t : trans) {
                    if (t.getFrom().equals(curr.getId())) {
                        if (t.getRead().equals("empty")) {
                            if (t.getPop().equals("empty")) {
                                State state = findState(t.getTo());
                                Stack<String> stack2 = (Stack<String>) stack.clone();
                                stack2.push(t.getPush());
                                if (toTry.containsKey(state)) {
                                    Map<Stack<String>, String> add = toTry.get(state);
                                    add.put(stack2, input);
                                    toTry.replace(state, add);
                                } else {
                                    Map<Stack<String>, String> add = new HashMap<>();
                                    add.put(stack2, input);
                                    toTry.put(state, add);
                                }
                            }
                            else {
                                if (t.getPop().equals(stack.peek())) {
                                    Stack<String> stack2 = (Stack<String>) stack.clone();
                                    stack2.pop();
                                    if (!t.getPush().equals("empty")) {
                                        stack2.push(t.getPush());
                                    }
                                    State state = findState(t.getTo());
                                    if (toTry.containsKey(state)) {
                                        Map<Stack<String>, String> add = toTry.get(state);
                                        add.put(stack2, input);
                                        toTry.replace(state, add);
                                    } else {
                                        Map<Stack<String>, String> add = new HashMap<>();
                                        add.put(stack2, input);
                                        toTry.put(state, add);
                                    }
                                }
                            }
                        }
                        else if (input.length()>0){
                            if (t.getRead().equals(Character.toString(input.charAt(0)))) {
                                if (t.getPop().equals(stack.peek())) {
                                    State state = findState(t.getTo());
                                    Stack<String> stack2 = (Stack<String>) stack.clone();
                                    stack2.pop();
                                    if (!t.getPush().equals("empty")) {
                                        stack2.push(t.getPush());
                                    }
                                    String input2;
                                    if (input.length() > 1) {
                                        input2 = input.substring(1);
                                    } else {
                                        input2 = "";
                                    }
                                    if (toTry.containsKey(state)) {
                                        Map<Stack<String>, String> add = toTry.get(state);
                                        add.put(stack2, input2);
                                        toTry.replace(state, add);
                                    } else {
                                        Map<Stack<String>, String> add = new HashMap<>();
                                        add.put(stack2, input2);
                                        toTry.put(state, add);
                                    }
                                }
                            }
                        }
                    }
                }
                for (Map.Entry<State, Map<Stack<String>, String>> m : toTry.entrySet()) {
                    State nextcurr = m.getKey();
                    Map<Stack<String>, String> vals = m.getValue();
                    for (Map.Entry<Stack<String>, String> m2 : vals.entrySet()) {
                        Stack<String> nextStack = m2.getKey();
                        String input2 = m2.getValue();
                        toRet = toRet || runHelp(nextcurr, trans, input2, nextStack, bound -= 1);
                    }
                }

        }
        return toRet;
    }

    public State findState(String to) {
        State st = new State();
        for (State s : allStates) {
            if (s.getId().equals(to)) {
                st = s;
            }
        }
        return st;
    }

}
