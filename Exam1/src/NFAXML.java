import java.util.*;

public class NFAXML {


    public static List<String> createXML(NFA fig, boolean id) {
        List<String> toRet = new ArrayList<>();
        int numStates = fig.getStates().size();
        //top of xml
        toRet.add("<automaton>");
        //states information
        toRet.add(" <!--The list of states.-->");
        LinkedHashSet<String> allStates2 = fig.getStates();
        List<String> allStates = new ArrayList<>();
        for (String s:allStates2) {
            allStates.add(s);
        }
        for (int i = 0; i < numStates; i++) {
            StringBuilder toAdd = new StringBuilder();
            String curr = allStates.get(i);
            toAdd.append("<state id=\"");
            if (id) {
                String currID = getID(curr, fig.getIdName());
                toAdd.append(currID);
            }
            else {
                toAdd.append(curr);
            }
            toAdd.append("\" name=\"");
            toAdd.append(curr);
            toAdd.append("\">");
            if (fig.getStart().equals(curr)) {
                toAdd.append("<initial/>");
            }
            if (fig.getAccept().contains(curr)) {
                toAdd.append("<final/>");
            }
            toAdd.append("</state>");
            toRet.add(toAdd.toString());
        }
        //transition information, use iterator
        toRet.add(" <!--The list of transitions.-->");
        LinkedHashMap<String, LinkedHashMap<String, LinkedHashSet<String>>> delta = fig.getDelta();
        Iterator<Map.Entry<String, LinkedHashMap<String, LinkedHashSet<String>>>> outer = delta.entrySet().iterator();
        while (outer.hasNext()) {
            Map.Entry<String, LinkedHashMap<String, LinkedHashSet<String>>> curr = outer.next();
            String start = curr.getKey();
            HashMap<String, LinkedHashSet<String>> val = curr.getValue();
            Iterator<Map.Entry<String, LinkedHashSet<String>>> inner = val.entrySet().iterator();
            while (inner.hasNext()) {
                Map.Entry<String, LinkedHashSet<String>> currInner = inner.next();
                String sym = currInner.getKey();
                LinkedHashSet<String> endVals = currInner.getValue();
                Iterator i = endVals.iterator();
                while (i.hasNext()) {
                    StringBuilder toAdd = new StringBuilder();
                    String end = (String)i.next();
                    toAdd.append("<transition><from>");
                    toAdd.append(start);
                    toAdd.append("</from><to>");
                    toAdd.append(end);
                    if (!sym.equals("empty")) {
                        toAdd.append("</to><read>");
                        toAdd.append(sym);
                        toAdd.append("</read></transition>");
                    }
                    else {
                        toAdd.append("</to><read>");
                        toAdd.append("");
                        toAdd.append("</read></transition>");
                    }
                    toRet.add(toAdd.toString());
                }
            }
        }
        //closing the automation
        toRet.add("</automaton>");
        return toRet;
    }

    public static String getID(String name, Map<String, String> idName) {
        String s ="";
        for (Map.Entry m : idName.entrySet()) {
            if (name.equals(m.getValue())) {
                s= (String)m.getKey();
            }
        }
        return s;
    }


}
