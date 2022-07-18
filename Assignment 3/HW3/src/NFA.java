import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;

public interface NFA {
    LinkedHashSet<String> getStates();
    String getStart();
    LinkedHashSet<String> getAccept();
    LinkedHashSet<String> getAlphabet();
    LinkedHashMap<String, LinkedHashMap<String, LinkedHashSet<String>>> getDelta();
    Map<String, String> getIdName();
    void setIDName(Map<String, String> idName);
}
