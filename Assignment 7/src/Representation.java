import java.util.List;

public class Representation {

    public static void main(String[] args) {
        parse parser = new parse();
        PDAXML xml = new PDAXML();
        PDA pda = parser.parseArgs(args);
        List<String> toPrint = xml.toxml(pda);
        for (String s: toPrint) {
            System.out.println(s);
        }
    }
}
