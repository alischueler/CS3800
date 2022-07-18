import java.util.List;
import java.util.Scanner;

public class CFG2PDA {
    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);
        parse parser = new parse();
        PDAXML xml = new PDAXML();
        String filename = scan.nextLine();
        CFG cfg = parser.parseXMLCFG(filename);
        PDA pda = cfg.toPDA();
        List<String> toPrint = xml.toxml(pda);
        for (String s: toPrint) {
            System.out.println(s);
        }
    }
}
