import java.util.*;

public class NFAMaker implements NFA {
    List<State> states;
    State start;
    List<State> accept;
    List<String> alphabet;
    List<DFATransition> delta;
    Map<String, String> idName;

    public NFAMaker(List<State> states, State start, List<State> accept, List<DFATransition> transitions, List<String> alphabet) {
        this.states = states;
        this.start = start;
        this.accept = accept;
        this.delta = transitions;
        this.alphabet = alphabet;
    }


    @Override
    public List<State> getStates() {
        return states;
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
    public List<String> getAlphabet() {
        return alphabet;
    }

    @Override
    public List<DFATransition> getDelta() {
        return delta;
    }

    @Override
    public Map<String, String> getIdName() {
        return idName;
    }

    @Override
    public void setIDName(Map<String, String> idName) {
        this.idName = idName;
    }
}
