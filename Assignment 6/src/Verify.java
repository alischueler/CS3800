import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;

public class Verify {
    public static InputStreamReader in;

    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);
        List<String> derivs = new ArrayList<>();
        while (scan.hasNextLine()) {
            derivs.add(scan.nextLine());
        }
        createInputReader("types.jff");
        parse parser = new parse();
        CFG cfg = parser.parseXML(in);
        boolean accept = cfg.checkDerivs(derivs);
        if (accept) {
            System.out.println("accept");
        }
        else {
            System.out.println("reject");
        }
    }

    public static void createInputReader(String fileName) {
        InputStream file;
        try {
            file = new FileInputStream(fileName);
        }
        catch (IOException e) {
            throw new IllegalArgumentException("file not found");
        }
        in = new InputStreamReader(file);
    }
}
