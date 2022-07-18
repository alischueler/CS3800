import java.util.List;
import java.util.Scanner;

public class NSDFA2DFA {

    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);
        parse parser = new parse();
        String filename = scan.nextLine();
        DFA2XML xml = new DFA2XML();
        NSDFA nsdfa = parser.parseXMLNSDFA(filename);
        NFA nfa = nsdfa.makeNFA();
        NFA2DFAMaker combine = new NFA2DFAMaker(nfa);
        DFA dfa = combine.makeDFA();
        List<String> toPrint = xml.createXML(dfa);
        for (String s: toPrint) {
            System.out.println(s);
        }
    }
}
