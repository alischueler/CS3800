import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;

public interface CFG {

    public LinkedHashMap<Variable, LinkedHashSet<String>> getRules();

    public List<Terminal> getTerminals();

    public List<Variable> getVariables();

    CFG makeChomsky();
}
