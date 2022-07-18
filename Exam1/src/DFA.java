import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;

public interface DFA {
    HashSet<String> getStates();
    String getStart();
    HashSet<String> getAccept();
    HashSet<String> getAlphabet();
    LinkedHashMap<String, LinkedHashMap<String, String>> getDelta();
    Map<String, String> getIdName();
    void setIDName(Map<String, String> idName);
}
