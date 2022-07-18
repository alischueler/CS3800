import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class NFALang {

    public static void main(String args[]) {
        Scanner scan = new Scanner(System.in);
        String fileName = scan.nextLine();
        xml parser = new xml();
        List<String> states = xml.callParse(fileName, "states");
        List<String> transitions = xml.callParse(fileName, "transitions");
        Map<String, String> idName = parser.idName;
        NFA fig = new NFAMaker(states, transitions, idName);
        List<String> good = lang(fig);
        if (good.size()>0) {
            for (int i=0; i< good.size();i++) {
                System.out.println(good.get(i));
            }
        }
        else {
            System.out.println();
        }
    }

    public static List<String> lang(NFA fig) {
        NFA2 represent = new NFA2();
        List<String> alphabet = new ArrayList<String>(fig.getAlphabet());
        List<String> one = printAll(alphabet, 1);
        List<String> two = printAll(alphabet, 2);
        List<String> three = printAll(alphabet, 3);
        List<String> four = printAll(alphabet, 4);
        List<String> five = printAll(alphabet, 5);
        List<String> toPrint = new ArrayList<>();
        addList(fig, represent, one, toPrint);
        addList(fig, represent, two, toPrint);
        addList(fig, represent, three, toPrint);
        addList(fig, represent, four, toPrint);
        addList(fig, represent, five, toPrint);
        return toPrint;
    }

    public static void addList(NFA fig, NFA2 represent, List<String> los, List<String> toPrint) {
        for (String s: los) {
            if (represent.run(fig, s)){
               // System.out.println("good "+s);
                toPrint.add(s);
            }
        }
    }

    public static List<String> printAll(List<String> alphabet, int length) {
        List<String> toRet = new ArrayList<>();
        int n = alphabet.size();
        printAllHelp(alphabet, "", n, length, toRet);
        return toRet;
    }

    public static void printAllHelp(List<String> alphabet, String prefix, int n, int length, List<String> toRet) {
        if (length!=0) {
            for (int i = 0; i < n; i++) {
                if (!alphabet.get(i).equals("empty")) {
                    String newPrefix = prefix + alphabet.get(i);
                    printAllHelp(alphabet, newPrefix, n, length - 1, toRet);
                }
            }
        }
        else {
            toRet.add(prefix);
        }
    }

}

