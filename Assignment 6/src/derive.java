import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class derive {
    public static InputStreamReader in;

    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);
        List<String> derivs = new ArrayList<>();
        while (scan.hasNextLine()) {
            derivs.add(scan.nextLine());
        }
        createInputReader(derivs.get(0));
        parse parser = new parse();
        CFG cfg = parser.parseXML(in);
        List<String> toPrint = cfg.findPath(derivs.get(1), derivs.get(2));
        for (String s : toPrint) {
            System.out.println(s);
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
