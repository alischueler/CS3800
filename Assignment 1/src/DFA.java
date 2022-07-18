import java.util.*;

/**
 * A class that initializes an instance of Fig 1.5 and has methods to extract its data
 */
public class DFA {
    static HashSet<String> states;
    static HashSet<String> alphabet;
    static HashMap<String, HashMap<String, String>> delta;
    static String start;
    static HashSet<String> accept;

    /**
     * A main method to use the keyword passed in the command line to outprint a piece of information about fig 1.5 DFA
     * @param args n/a information is read from command line, nothing is taken from args
     */
    public static void main(String[] args) {
        setUpInfo();
        List<String> toPrint = new ArrayList<>();
        Scanner scanner = new Scanner(System.in);
        String keyword = scanner.next();
        switch(keyword) {
            case "states":
                toPrint = convert(states);
                break;
            case "alpha":
                toPrint = convert(alphabet);
                break;
            case "transitions":
                toPrint = convertTransition(delta);
                break;
            case "start":
                toPrint.add(start);
                break;
            case "accepts":
                toPrint = convert(accept);
                break;
            default:
                break;
        }
        for (String s : toPrint) {
            System.out.println(s);
        }
    }

    /**
     * Initializes the fields of this DFA
     */
    private static void setUpInfo() {
        states = setUpStates();
        alphabet = setUpAlpha();
        delta = setUpDelta();
        start = setUpStart();
        accept = setUpAccept();
    }

    /**
     * Returns all the accept states in fig 1.5 DFA as a hashset of string
     * @return Hashset of String representing the set of Accept states in the DFA
     */
    private static HashSet<String> setUpAccept() {
        String[] sArr = states.toArray(new String[states.size()]);
        HashSet<String> ret = new HashSet<String>(1);
        ret.add(sArr[1]);
        return ret;
    }

    /**
     * Returns the start stater of fig 1.5 DFA as a String (its name)
     * @return String representing the start state of fig 1.5 DFA
     */
    private static String setUpStart() {
        String[] sArr = states.toArray(new String[states.size()]);
        return sArr[0];
    }

    /**
     * A method to fill in the transition hashmap of fig 1.5 DFA
     * @return Hashmap of String, Hashmap of String, String representing the transition function for each state, the
     * symbol and the next state
     */
    private static HashMap<String, HashMap<String, String>> setUpDelta() {
        String[] sArr = states.toArray(new String[states.size()]);
        String[] symArr = alphabet.toArray(new String[alphabet.size()]);
        HashMap<String, String> one = new HashMap<String, String>(1);
        HashMap<String, String> two = new HashMap<String, String>(1);
        HashMap<String, String> three = new HashMap<String, String>(1);
        one.put(symArr[0], sArr[0]);
        one.put(symArr[1], sArr[1]);
        two.put(symArr[0], sArr[2]);
        two.put(symArr[1], sArr[1]);
        three.put(symArr[0], sArr[1]);
        three.put(symArr[1], sArr[1]);
        HashMap<String, HashMap<String, String>> ret = new HashMap<String, HashMap<String, String>>();
        ret.put(sArr[0], one);
        ret.put(sArr[1], two);
        ret.put(sArr[2], three);
        return ret;
    }

    /**
     * A method to fill in info about the Alphabet of Fig 1.5 DFA
     * @return Hashset of String representing the alphabet of fig 1.5 DFA
     */
    private static HashSet<String> setUpAlpha() {
        String s1 = "0";
        String s2 = "1";
        HashSet<String> symbols = new HashSet<>(2);
        symbols.add(s1);
        symbols.add(s2);
        return symbols;
    }

    /**
     * A method to fill in info about the States of fig 1.5 DFA
     * @return Hashset of String representing all of the states of fig 1.5 DFA
     */
    private static HashSet<String> setUpStates() {
        String one = "q1";
        String two = "q2";
        String three = "q3";
        HashSet<String> toAdd = new HashSet<String>(3);
        toAdd.add(one);
        toAdd.add(two);
        toAdd.add(three);
        return toAdd;
    }

    /**
     * Converts the given delta to a list of string with the beginning state(outer key), symbol (inner key),
     * and end state separated by spaces
     * @param delta the transition map of fig 1.5 DFA
     * @return a list of string representing all possible movements within fig 1.5 DFA
     */
    private static List<String> convertTransition(HashMap<String, HashMap<String, String>> delta) {
        List<String> ret = new ArrayList<>();
        Iterator<Map.Entry<String, HashMap<String, String>>> it = delta.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<String,HashMap<String, String>> en = it.next();
            String start = en.getKey();
            HashMap<String, String> val = en.getValue();
            Iterator<Map.Entry<String, String>> it2 = val.entrySet().iterator();
            while(it2.hasNext()) {
                Map.Entry<String, String> inner = it2.next();
                String sym = inner.getKey();
                String end = inner.getValue();
                StringBuilder sb = new StringBuilder();
                sb.append(start);
                sb.append(" ");
                sb.append(sym);
                sb.append(" ");
                sb.append(end);
                ret.add(sb.toString());
            }
        }
        return ret;
    }

    /**
     * Converts the given hashset of string to a list of string
     * @param toConvert the hashset to convert to a list
     * @return a list of string converted from the given hashset
     */
    private static List<String> convert(HashSet<String> toConvert) {
        List<String> ret = new ArrayList<>();
        Iterator it = toConvert.iterator();
        while (it.hasNext()) {
            String s = it.next().toString();
            ret.add(s);
        }
        return ret;
    }
}
