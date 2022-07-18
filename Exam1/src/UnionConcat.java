import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class UnionConcat {

    public static void main(String[] args) {

        Scanner scan = new Scanner(System.in);
        String fileNames = scan.nextLine();
        String[] spl = fileNames.split(" ");


        //union nfa1 and nfa2
        NFAUnion union = new NFAUnion();
        NFA unionNFA = union.unionize(spl[0], spl[1]);
        //NFALang one = new NFALang();
        //System.out.println("UNION DELTA:"+unionNFA.getDelta());

        //concat nfa1 and nfa2
        NFAConct concat = new NFAConct();
        NFA concatNFA = concat.concat(spl[0], spl[1]);


        //nfalang up to length 5 for union
        NFALang unionLang = new NFALang();
        List<String> unionLangNFA = unionLang.lang(unionNFA);
        //System.out.println(unionLangNFA.size());
//        for (String s: unionLangNFA) {
//            System.out.println(s);
//        }

        //nfalang up to length 5 for concat
        NFALang concatLang = new NFALang();
        List<String> concatLangNFA = concatLang.lang(concatNFA);
//        System.out.println("here");
//        for (String s: concatLangNFA) {
//            System.out.println("concat");
//            System.out.println(s);
//        }

        //set difference between nfalang1 and nfalang 2
        Setdiff diff = new Setdiff();
        //System.out.println("THIS"+unionLangNFA.size());
        HashSet<String> diffL = diff.checkDiff(concatLangNFA, unionLangNFA);
        if (diffL.size()>0) {
            for (String s : diffL) {
                System.out.println(s);
            }
        }
    }
}
