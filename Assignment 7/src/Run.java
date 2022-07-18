import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Run {

    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);
        List<String> ar = new ArrayList<>();
        while(scan.hasNext()) {
            ar.add(scan.next());
        }
        parse parser = new parse();
        String filename = ar.get(0);
        String str = "";
        if (ar.size()>1) {
            str = ar.get(1);
        }
        int bound = -1;
        if (ar.size()>2) {
            bound = Integer.parseInt(ar.get(2));
        }
        PDA pda = parser.parseXMLPDA(filename);
        boolean accept = pda.run(str, bound);
        if (accept) {
            System.out.println("accept");
        }
        else {
            System.out.println("reject");
        }
    }
}
