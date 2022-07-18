import java.util.*;

public class PDAXML {


    public List<String> toxml(PDA fig) {
        List<String> toRet = new ArrayList<>();
        List<PDATransition> transitions = fig.getTransitions();
        State start = fig.getStart();
        List<State> accept = fig.getAccept();
        List<State> states = fig.getStates();
        //top of xml
        toRet.add("<structure>");
        //states information
        toRet.add("<type>pda</type>");
        toRet.add("<automaton>");
        for (State s : states) {
            StringBuilder sb = new StringBuilder();
            String id = s.getId();
            String name = s.getName();
            sb.append("<state id=\""+id+"\" name=\""+name+"\">");
            if (start.equals(s)) {
                sb.append("<initial/>");
            }
            if (accept.contains(s)) {
                sb.append("<final/>");
            }
            sb.append("</state>");
            toRet.add(sb.toString());
        }
        for(PDATransition t: transitions) {
            String from = t.getFrom();
            String to = t.getTo();
            String read = t.getRead();
            String push = t.getPush();
            String pop = t.getPop();
            StringBuilder sb = new StringBuilder();
            sb.append("<transition><from>");
            sb.append(from);
            sb.append("</from><to>");
            sb.append(to);
            sb.append("</to>");
            if (read.equals("empty")) {
                sb.append("<read/>");
            }
            else {
                sb.append("<read>");
                sb.append(read);
                sb.append("</read>");
            }
            if (pop.equals("empty")) {
                sb.append("<pop/>");
            }
            else {
                sb.append("<pop>");
                sb.append(pop);
                sb.append("</pop>");
            }
            if (push.equals("empty")) {
                sb.append("<push/>");
            }
            else {
                sb.append("<push>");
                sb.append(push);
                sb.append("</push>");
            }
            sb.append("</transition>");
            toRet.add(sb.toString());
        }
        toRet.add("</automaton>");
        toRet.add("</structure>");
        return toRet;
    }
}
