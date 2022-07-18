import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class NFA2DFA {

    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);
        String fileName = scan.nextLine();
        xml parser = new xml();
        List<String> states = xml.callParse(fileName, "states");
        List<String> transitions = xml.callParse(fileName, "transitions");
        Map<String, String> idName = parser.idName;
        NFA nfa = new NFAMaker(states, transitions, idName);
        NFA2DFAMaker combine = new NFA2DFAMaker(nfa);
        DFA dfa = combine.makeDFA();
        DFA2XML DFA2XML = new DFA2XML();
        List<String> output = DFA2XML.createXML(dfa, true);
        for (String s : output) {
            System.out.println(s);
        }
    }
}
