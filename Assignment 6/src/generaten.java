import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;

public class generaten {
    public static InputStreamReader in;

    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);
        String fileName = scan.next();
        String size = scan.next();
        int count = Integer.parseInt(size);
        createInputReader(fileName);
        parse parser = new parse();
        CFG fig = parser.parseXML(in);
        LinkedHashSet<String> toAdd = fig.findString(count);
        for (String s : toAdd) {
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
