import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;

public interface CFG {

    public LinkedHashMap<Variable, LinkedHashSet<String>> getRules();

    public LinkedHashSet<String> findString(int size);

    boolean checkDerivs(List<String> derivations);

    List<String> findPath(String start, String end);

    PDA toPDA();
}
