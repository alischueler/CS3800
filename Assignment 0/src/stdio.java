import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class stdio {

    /**
     * A main method to output, the given input (through the command line), three times on a new line
     * @param args this is not referenced in the method
     */
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        List<String> l = new ArrayList<>();
        while (scanner.hasNextLine()) {
            String toAdd = scanner.nextLine();
            l.add(toAdd);
        }
        scanner.close();
        if (l.size() > 0) {
            for (int i = 1; i <= 3; i++) {
                for (String s : l) {
                    System.out.println(s);
                }
            }
        }
        else {
            for (int i = 1; i<=3;i++ ) {
                System.out.print("\n");
            }
        }
    }
}
