import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class xml {

    /**
     * A main method to output the names of states, names of start states, and names of initial states in the file
     * noted by the string passed in through the command line
     * @param args this is not referenced in the method
     */
    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);
        String fileName = scan.nextLine();
        InputStream file;
        try {
            file = new FileInputStream(fileName);
        }
        catch (IOException e) {
            throw new IllegalArgumentException("file not found");
        }
        List<String> toPrint = parseFile(new InputStreamReader(file));
        for (String s : toPrint) {
            System.out.println(s);
        }

    }

    /**
     * Uses the readable file to find all the names of accept states, names of states that are initial states, and
     * names of states that are end states and puts them all in a list
     * @param file the file to be read for information
     * @return a list of string with (1) names of states, (2) names of start states, (3) names of initial states
     */
    public static List<String> parseFile(Readable file) {
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
                findInfo(allRelData.toString().split(" "), namesL, startL, endL);
                allRelData = new StringBuilder();
            }
            else if (word.contains("<state ") && !word.contains("<state />")) {
                allRelData.append(word);
            }
            else if (word.contains("</state>")) {
                allRelData.append(" ");
                allRelData.append(word);
                findInfo(allRelData.toString().split(" "), namesL, startL, endL);
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

    /**
     * Uses the given array of strings to see if it contains a name, initial, or final and updates the names, start, or
     * end strinbuilders if the array contains the applicable words
     * @param words an array of strings from one line in the file, split on spaces
     * @param namesL the Stringbuilder containing the names of all states separated by commas
     * @param startL the Stringbuilder containing the names of all states that are initial states, separated by commas
     * @param endL the Stringbuilder containing the names of all states that are accept states, separated by commas
     */
    public static void findInfo(String[] words, StringBuilder namesL, StringBuilder startL, StringBuilder endL) {
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
        for (int i = 0; i<line.length; i++) {
            String curr = line[i];
            if (curr.contains("name")) {
                String[] quotes = curr.split("\"");
                name = quotes[1];
            }
            if (curr.contains(find)) {
                found = name;
            }
        }
        return found;
    }
}
