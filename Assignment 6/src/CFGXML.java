import java.util.*;

public class CFGXML {


    public List<String> toxml(CFG fig) {
        List<String> toRet = new ArrayList<>();
        LinkedHashMap<Variable, LinkedHashSet<String>> rules = fig.getRules();
        int numStates = rules.size();
        //top of xml
        toRet.add("<structure>");
        //states information
        toRet.add("<type>grammar</type>");
        for(Map.Entry<Variable, LinkedHashSet<String>> r: rules.entrySet()) {
            Variable v = r.getKey();
            LinkedHashSet<String> prods = r.getValue();
            for (String p : prods) {
                StringBuilder sb = new StringBuilder();
                sb.append("<production><left>");
                sb.append(v);
                sb.append("</left>");
                if (p.equals("empty")) {
                    sb.append("<right/>");
                }
                else {
                    sb.append("<right>");
                    sb.append(p);
                    sb.append("</right>");
                }
                sb.append("</production>");
                toRet.add(sb.toString());
            }
        }
        toRet.add("</structure>");
        return toRet;
    }
}
