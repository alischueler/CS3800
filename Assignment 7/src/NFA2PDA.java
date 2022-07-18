import java.util.List;
import java.util.Scanner;

public class NFA2PDA {

    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);
        String filename = scan.nextLine();
        parse parser = new parse();
        PDAXML xml = new PDAXML();
        NFA nfa = parser.callParseNFA(filename);
        PDA pda = nfa.toPDA();
        List<String> toPrint = xml.toxml(pda);
        for (String s: toPrint) {
            System.out.println(s);
        }
    }
}
