import java.util.*;

public interface NFA {
    List<State> getStates();
    State getStart();
    List<State> getAccept();
    List<String> getAlphabet();
    List<NFATransition> getDelta();
    Map<String, String> getIdName();
    void setIDName(Map<String, String> idName);
    PDA toPDA();
}
