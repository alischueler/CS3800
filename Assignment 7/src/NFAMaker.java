import java.util.*;

public class NFAMaker implements NFA {
    List<State> states;
    State start;
    List<State> accept;
    List<String> alphabet;
    List<NFATransition> delta;
    Map<String, String> idName;
    boolean epsilon;

    public NFAMaker(List<State> states, State start, List<State> accept, List<NFATransition> transitions, List<String> alpha){
        this.states = states;
        this.start = start;
        this.accept = accept;
        this.alphabet = alpha;
        this.delta = transitions;
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
    public List<NFATransition> getDelta() {
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

    @Override
    public PDA toPDA() {
        List<PDATransition> trans = new ArrayList<>();
        for (NFATransition t : getDelta()) {
            String from = t.getFrom();
            String to = t.getTo();
            String read = t.getRead();
            PDATransition newt = new PDATransition(from, to, read, "empty", "empty");
            trans.add(newt);
        }
        PDA pda = new PDAMaker(getStates(), getStart(), getAccept(), trans);
        return pda;
    }
}
