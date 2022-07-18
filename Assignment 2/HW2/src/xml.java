import java.io.*;
import java.util.*;

public class xml {
    public static HashMap<String, String> idName = new HashMap<>();


    /**
     * A main method to output the names of states, names of start states, and names of initial states in the file
     * noted by the string passed in through the command line
     * @param args this is not referenced in the method
     */
    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);
        String fileName = scan.nextLine();
        List<String> toPrint = callParse(fileName, "states");
        for (String s : toPrint) {
            System.out.println(s);
        }

    }

    public static List<String> callParse(String fileName, String info) {
        InputStream file;
        List<String> toRet;
        try {
            file = new FileInputStream(fileName);
        }
        catch (IOException e) {
            throw new IllegalArgumentException("file not found");
        }
        InputStreamReader in = new InputStreamReader(file);
        switch(info) {
            case "states": toRet = parseStatesFile(in);
            break;
            case "transitions": toRet = parseTransitionsFile(in);
            break;
            default: toRet = new ArrayList<>();
                break;
        }
        return toRet;
    }

    //order start, end, sym
    public static List<String> parseTransitionsFile(Readable file) {
        List<String> los = new ArrayList<>(3);
        StringBuilder allRelData = new StringBuilder();
        StringBuilder transition = new StringBuilder();
        HashSet<String> alpha = new HashSet<>();
        Scanner s = new Scanner(file);
        while (s.hasNextLine()) {
            //find all words between <state and </state>
            String word = s.nextLine();
            if (word.contains("<transition") && word.contains("</transition>")) {
                allRelData.append(word);
                //do something
                findTransitionInfo(allRelData.toString().split(" "), transition, alpha);
                los.add(transition.toString());
                allRelData = new StringBuilder();
                transition = new StringBuilder();
            }
            else if (word.contains("<transition") && !word.contains("<transition />")) {
                allRelData.append(word);
            }
            else if (word.contains("</transition>")) {
                allRelData.append(" ");
                allRelData.append(word);
                findTransitionInfo(allRelData.toString().split(" "), transition, alpha);
                los.add(transition.toString());
                allRelData = new StringBuilder();
                transition = new StringBuilder();
            }
            else if (allRelData.length()!=0) {
                allRelData.append(" ");
                allRelData.append(word);
            }
        }
        StringBuilder alphabet = new StringBuilder();
        for (String letter : alpha) {
            alphabet.append(letter);
            alphabet.append(" ");
        }
        los.add(alphabet.toString());
        return los;
    }


    /**
     * Uses the readable file to find all the names of accept states, names of states that are initial states, and
     * names of states that are end states and puts them all in a list
     * @param file the file to be read for information
     * @return a list of string with (1) names of states, (2) names of start states, (3) names of initial states
     */
    public static List<String> parseStatesFile(Readable file) {
        List<String> los = new ArrayList<>(3);
        StringBuilder allRelData = new StringBuilder();
        StringBuilder namesL = new StringBuilder();
        StringBuilder startL = new StringBuilder();
        StringBuilder endL = new StringBuilder();
        Scanner s = new Scanner(file);
        while (s.hasNextLine()) {
            //find all words between <state and </state>
            String word = s.nextLine();
            if (word.contains("<state") && word.contains("</state>")) {
                allRelData.append(word);
                //do something
                findStatesInfo(allRelData.toString().split(" "), namesL, startL, endL);
                allRelData = new StringBuilder();
            }
            else if (word.contains("<state ") && !word.contains("<state />")) {
                allRelData.append(word);
            }
            else if (word.contains("</state>")) {
                allRelData.append(" ");
                allRelData.append(word);
                findStatesInfo(allRelData.toString().split(" "), namesL, startL, endL);
                allRelData = new StringBuilder();
            }
            else if (allRelData.length()!=0) {
                allRelData.append(" ");
                allRelData.append(word);
            }
        }
        String namesString = namesL.toString();
        String startString = startL.toString();
        String endString = endL.toString();
        los.add(namesString);
        los.add(startString);
        los.add(endString);
        return los;
    }

    public static void findTransitionInfo(String[] line, StringBuilder toAdd, HashSet<String> alpha) {
        String start = "";
        String end = "";
        String read = "";
        for (int i = 0; i<line.length; i++) {
            String curr = line[i];
            if (curr.contains("<from>")) {
                String[] l = curr.split("<from>");
                for (String s : l) {
                    if (s.contains("</from>")) {
                        String[] ids = s.split("</from>");
                        if (idName.containsKey(ids[0])) {
                            start = (ids[0]);
                        }
                    }
                }
            }
            if (curr.contains("<to")) {
                String[] l = curr.split("<to>");
                for (String s : l) {
                    if (s.contains("</to>")) {
                        String[] ids = s.split("</to>");
                        if (idName.containsKey(ids[0])) {
                            end = (ids[0]);
                        }
                    }
                }
            }
            if (curr.contains("<read>")) {
                String[] l = curr.split("<read>");
                for (String s : l) {
                    if (s.contains("</read>")) {
                        String[] ids = s.split("</read>");
                        read = ids[0];
                        alpha.add(read);
                    }
                }
            }
        }
        toAdd.append(start);
        toAdd.append(" ");
        toAdd.append(end);
        toAdd.append(" ");
        toAdd.append(read);
    }

    /**
     * Uses the given array of strings to see if it contains a name, initial, or final and updates the names, start, or
     * end strinbuilders if the array contains the applicable words
     * @param words an array of strings from one line in the file, split on spaces
     * @param namesL the Stringbuilder containing the names of all states separated by commas
     * @param startL the Stringbuilder containing the names of all states that are initial states, separated by commas
     * @param endL the Stringbuilder containing the names of all states that are accept states, separated by commas
     */
    public static void findStatesInfo(String[] words, StringBuilder namesL, StringBuilder startL, StringBuilder endL) {
        String names = findPhrase(words, "name=");
        if (names.length()>=1) {
            if (namesL.length()!=0) {
                namesL.append(" ");
            }
            namesL.append(names);
        }
        String start = findPhrase(words, "<initial/>");
        if (start.length()>=1) {
            if (startL.length()!=0) {
                startL.append(" ");
            }
            startL.append(start);
        }
        String end = findPhrase(words, "<final/>");
        if (end.length()>=1) {
            if (endL.length()!=0) {
                endL.append(" ");
            }
            endL.append(end);
        }
    }

    /**
     * Determines if a string is in an array of string, representing a line in the file, and returns the name of that
     * state if it is, returns a blank string if not
     * @param line the whole line of code, or the whole chunk of code from < to > that spans across multiple lines
     * @param find the string to be found in the given line
     * @return a string, the name of the the state, that has the specific string associated with it
     */
    public static String findPhrase(String[] line, String find) {
        String found = "";
        String name = "";
        String[] nameid = new String[2];
        for (int i = 0; i<line.length; i++) {
            String curr = line[i];
            if (curr.contains("name")) {
                String[] quotes = curr.split("\"");
                name = quotes[1];
                nameid[1]=quotes[1];
            }
            if (curr.contains("id")) {
                String[] quotes = curr.split("\"");
                nameid[0]=quotes[1];
            }
            if (curr.contains(find)) {
                found = name;
            }
        }
        addToMap(nameid);
        return found;
    }

    private static void addToMap(String[] toAdd) {
        if (!idName.containsKey(toAdd[0])) {
            idName.put(toAdd[0], toAdd[1]);
        }
    }
}
