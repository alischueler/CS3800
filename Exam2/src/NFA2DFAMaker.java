import javax.lang.model.type.ArrayType;
import java.util.*;

public class NFA2DFAMaker {
    public NFA nfa;
    public List<State> newStates;
    public List<State> accept;
    public List<String> alpha;
    public List<DFATransition> newTrans;
    public State newStart;

    public NFA2DFAMaker(NFA nfa) {
        this.nfa = nfa;
        createStates(nfa.getStates());
        createInitial(nfa.getStart());
        createFinal(nfa.getAccept(), newStates);
        createAlpha(nfa.getAlphabet());
        createTrans(nfa.getDelta(), newStates);
    }

    private void createAlpha(List<String> alpha) {
        List<String> alphaNew = new ArrayList<>();
        for (String s : alpha) {
            if (!s.equals("empty")) {
                alphaNew.add(s);
            }
        }
        this.alpha = alphaNew;
    }

    private void createTrans(List<DFATransition> delta, List<State> newStates) {
        LinkedHashSet<DFATransition> newDelta = new LinkedHashSet<>();
        List<String> NewalphaList = new ArrayList<>(alpha);
        List<State> statesList = new ArrayList<>(newStates);
        List<DFATransition> empties = findEmpties(delta);

        for (int j = 0; j < NewalphaList.size(); j++) {
            for (State s : statesList) {
//                System.out.println ("curr letter "+ NewalphaList.get(j));
//                System.out.println ("curr state "+s);
                //the fail state goes back to itself on all letters of the alphabet
                if (s.getId().equals("()")) {
                    DFATransition toAdd = new DFATransition(s.getId(), s.getId(), NewalphaList.get(j));
                    newDelta.add(toAdd);
                }
                //otherwise, find where all of the parts of the state go to, and make it end here
                else {
                    String total = s.getId().substring(s.getId().indexOf("(") + 1, s.getId().indexOf(")"));
                    String[] broken = total.split(" ");
                    StringBuilder sb = new StringBuilder();
                    for (int i = 0; i < broken.length; i++) {
                        String startState = broken[i];
                        //does this state have transitions on the curr letter?
                        if (hasTrans(NewalphaList.get(j), startState, delta)) {
                            //if it does, find all places that it ends
                            List<String> ends = findEnds(NewalphaList.get(j), startState, delta);
                            for (String e : ends) {
                                sb.append(e);
                                sb.append(" ");
                                //if any of these ends are in the list of states with epsilon transitions,
                                if (contains(empties, e)) {
                                    List<String> check = new ArrayList<>();
                                    List<String> epsilonTransition = findEnds("empty", startState, empties);
                                    for (String ep : epsilonTransition) {
                                        sb.append(ep);
                                        sb.append(" ");
                                    }
                                }
                            }
                        }
                        //if it does not have transitions on the current letters
                        else {
                            //see if it has epsilon transitions
                            if (contains(empties, startState)) {
                                //if it does, go to next state and see where it goes on curr letter
                                List<String> check = new ArrayList<>();
                                List<String> epsilonTransition = findEnds("empty", startState, empties);
                                for (String ep : epsilonTransition) {
                                    sb.append(ep);
                                    sb.append(" ");
                                }
                            }
                            //if it does not do nothing
                        }
                    }
                    if (sb.toString().length() == 0) {
                        DFATransition newTrans = new DFATransition(s.getId(), "()", NewalphaList.get(j));
                        newDelta.add(newTrans);
                    } else {
                        String totalEnd = findEnd(sb.toString(), newStates);
                        DFATransition newtrans = new DFATransition(s.getId(), totalEnd, NewalphaList.get(j));
                        newDelta.add(newtrans);
                    }
                }
            }
        }
        List<DFATransition> toRet = new ArrayList<>();
        toRet.addAll(newDelta);
        newTrans = toRet;
    }

    public boolean hasTrans(String read, String start, List<DFATransition> trans) {
        boolean toRet = false;
        for (DFATransition t : trans) {
            if (t.getFrom().equals(start) && t.getRead().equals(read)) {
                toRet = true;
            }
        }
        return toRet;
    }

    public boolean contains(List<DFATransition> states, String curr) {
        boolean toRet = false;
        for (DFATransition s : states) {
            if (s.getFrom().equals(curr)) {
                toRet = true;
            }
        }
        return toRet;
    }

    public List<String> findEnds(String read, String start, List<DFATransition> trans) {
        List<String> toRet = new ArrayList<>();
        for (DFATransition t : trans) {
            if (t.getFrom().equals(start) && t.getRead().equals(read)) {
                toRet.add(t.getTo());
            }
        }
        return toRet;
    }

    private String findEnd(String want, List<State> newStates) {
        String[] wantSpl = want.split(" ");
        String toRet = "";
        for (State s : newStates) {
            String use = s.getId();
            if (!s.equals("()")) {
                use = s.getId().substring(s.getId().indexOf("(") + 1, s.getId().indexOf(")"));
            }
            String[] split = use.split(" ");
            boolean thisState = true;
            for (String sSplit : split) {
                if (!want.contains(sSplit)) {
                    thisState = false;
                    break;
                }
            }
            if (thisState) {
                boolean match = true;
                for (String w : wantSpl) {
                    if (!s.getId().contains(w)) {
                        match = false;
                    }
                }
                if (match) {
                    return s.getId();
                }
            }
        }
        return toRet;
    }

    private List<DFATransition> findEmpties(List<DFATransition> delta) {
        List<DFATransition> toRet = new ArrayList<>();
        for (DFATransition t : delta) {
            String read = t.getRead();
            if (read.equals("empty")) {
                toRet.add(new DFATransition(t));
            }
        }
        return toRet;
    }

    private void createFinal(List<State> Oldaccept, List<State> newStates) {
        LinkedHashSet<State> acceptList = new LinkedHashSet<>();
        for (State s : Oldaccept) {
            for (State s2 : newStates) {
                if (!s2.getId().equals("()")) {
                    String use = s2.getId().substring(s2.getId().indexOf("(") + 1, s2.getId().indexOf(")"));
                    String[] broken = use.split(" ");
                    for (int i = 0; i < broken.length; i++) {
                        if (s.getId().equals(broken[i])) {
                            acceptList.add(s2);
                        }
                    }
                }
            }
        }
        List<State> nAccept = new ArrayList<>();
        nAccept.addAll(acceptList);
        this.accept = nAccept;
    }

    private void createInitial(State start) {
        State newStart = new State("(" + start.getId() + ")", "(" + start.getId() + ")");
        this.newStart = newStart;
    }

    private void createStates(List<State> states) {
        String[] arr = new String[states.size()];
        for (int i = 0; i < states.size(); i++) {
            arr[i] = states.get(i).getId();
        }
        powerset p = new powerset();
        List<List<String>> toPrint = p.printPowerSet(arr, arr.length);
        List<String> toPrintString = p.changeString(toPrint);
        LinkedHashSet<String> po = new LinkedHashSet<>();
        for (String s : toPrintString) {
            StringBuilder sb = new StringBuilder();
            sb.append("(");
            sb.append(s);
            sb.append(")");
            po.add(sb.toString());
        }
        List<State> toRet = new ArrayList<>();
        for (String s : po) {
            toRet.add(new State(s, s));
        }
        newStates = toRet;
    }


    public DFA makeDFA() {
        DFA toRet = new DFAMaker(newStates, newStart, accept, newTrans, alpha);
        return toRet;
    }
}
