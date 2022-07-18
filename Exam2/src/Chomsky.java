import java.util.List;
import java.util.Scanner;

public class Chomsky {
    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);
        parse parser = new parse();
        CFGXML xml = new CFGXML();
        String filename = scan.nextLine();
        CFG cfg = parser.parseXMLCFG(filename);
        CFG cfg2 = cfg.makeChomsky();
        List<String> toPrint = xml.toxml(cfg2);
        for (String s: toPrint) {
            System.out.println(s);
        }
    }
}
