import java.util.List;

public class dyck {

    public static void main(String[] args) {
        parse parser = new parse();
        CFGXML xml = new CFGXML();
        CFG cfg = parser.parseArgs(args);
        List<String> toPrint = xml.toxml(cfg);
        for (String s: toPrint) {
            System.out.println(s);
        }

    }
}
