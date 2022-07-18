import java.util.*;

public class DFAMaker implements DFA {
    List<State> states;
    State start;
    List<State> accept;
    List<String> alphabet;
    List<DFATransition> delta;

    public DFAMaker(List<State> states, State start, List<State> accept, List<DFATransition> transitions, List<String> alphabet) {
        this.states = states;
        this.start = start;
        this.accept = accept;
        this.delta = transitions;
        this.alphabet = alphabet;
    }

    @Override
    public State getStart() {
        return start;
    }

    @Override
    public List<State> getAccept() {
        return accept;
    }

    public List<String> getAlphabet() {
        return alphabet;
    }

    public List<State> getStates() {
        return states;
    }

    @Override
    public List<DFATransition> getDelta() {
        return delta;
    }

}
