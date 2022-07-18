import java.util.List;

public interface PDA {
    List<PDATransition> getTransitions();

    List<State> getStates();

    State getStart();

    List<State> getAccept();

    boolean run(String str, int bound);
}
