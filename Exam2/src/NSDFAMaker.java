import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;

public class NSDFAMaker implements NSDFA{
    List<State> states;
    List<State> startStates;
    List<State> acceptStates;
    List<DFATransition> transitions;
    List<String> alphabet;

    public NSDFAMaker(List<State> states, List<State> start, List<State> accept, List<DFATransition> transitions, List<String> alphabet) {
     this.states = states;
     this.startStates = start;
     this.acceptStates = accept;
     this.transitions = transitions;
     this.alphabet = alphabet;
    }

    public NFA makeNFA() {
        State newStart = new State("NEWSTART", "NEWSTART");
        List<State> newStates = new ArrayList<>(states);
        newStates.add(newStart);
        List<State> newaccept = new ArrayList<>(acceptStates);
        for (State s : startStates) {
            if (acceptStates.contains(s)) {
                if (!newaccept.contains(newStart)) {
                    newaccept.add(newStart);
                }
            }
        }
        List<DFATransition> newtransitions = new ArrayList<>(transitions);
        LinkedHashSet<DFATransition> addEpsilon = new LinkedHashSet<>();
        for (State s : startStates) {
            DFATransition toAdd = new DFATransition(newStart.getId(), s.getId(), "empty");
            addEpsilon.add(toAdd);
        }
        newtransitions.addAll(addEpsilon);
        return new NFAMaker(newStates, newStart, newaccept, newtransitions, alphabet);
    }
}
