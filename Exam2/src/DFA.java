import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public interface DFA {
    List<State> getStates();
    State getStart();
    List<State> getAccept();
    List<String> getAlphabet();
    List<DFATransition> getDelta();
}
