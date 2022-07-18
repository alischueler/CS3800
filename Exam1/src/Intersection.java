import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class Intersection {
    static DFA dfa1;
    static DFA dfa2;
    static DFA dfa3;
    static DFA dfa4;

    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);
        String input;
        if (scan.hasNext()) {
            input = scan.nextLine();
        }
        else {
            input = "";
        }

        //reas four jflap files and set as dfas
        xml parser1 = new xml();
        List<String> statesOne = parser1.callParse(args[0], "states");
        List<String> transitionsOne = parser1.callParse(args[0], "transitions");
        Map<String, String> idNameOne = parser1.idName;
        dfa1 = new DFAMaker(statesOne, transitionsOne, idNameOne);

        xml parser2 = new xml();
        List<String> statesTwo = parser2.callParse(args[1], "states");
        List<String> transitionsTwo = parser2.callParse(args[1], "transitions");
        Map<String, String> idNameTwo = parser2.idName;
        dfa2 = new DFAMaker(statesTwo, transitionsTwo, idNameTwo);

        xml parser3 = new xml();
        List<String> statesThree = parser3.callParse(args[2], "states");
        List<String> transitionsThree = parser3.callParse(args[2], "transitions");
        Map<String, String> idNameThree = parser3.idName;
        dfa3 = new DFAMaker(statesThree, transitionsThree, idNameThree);

        xml parser4 = new xml();
        List<String> statesFour = parser4.callParse(args[3], "states");
        List<String> transitionsFour = parser4.callParse(args[3], "transitions");
        Map<String, String> idNameFour = parser4.idName;
        dfa4 = new DFAMaker(statesFour, transitionsFour, idNameFour);

        //intersect 1 and 2
        IntersectionDFA first = new IntersectionDFA();
        DFA OneTwo = first.intersect(dfa1, dfa2, false);

        //intersect 3 and 4
        IntersectionDFA second = new IntersectionDFA();
        DFA ThreeFour = second.intersect(dfa3, dfa4, false);

        //intersect 1.2 and 3.4
        IntersectionDFA fin = new IntersectionDFA();
        DFA all = fin.intersect(OneTwo, ThreeFour, true);


        DFA2 dfa2 = new DFA2();
        if (dfa2.run(all, input)) {
            System.out.println("valid");
        }
        else {
            System.out.println("invalid");
        }
    }
}
