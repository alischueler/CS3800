import java.util.*;

public class alphabet {

    /**
     * A main method to output all 3 string variations of the given string through command line
     * @param args this is not referenced in the method
     */
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Set<Character> stringSet = new HashSet<>();
        List<Character> charL = new ArrayList<>();
        while (scanner.hasNext()) {
            String s = scanner.next();
            for (int i = 0; i < s.length(); i++) {
                stringSet.add(s.charAt(i));
            }
        }
        Iterator<Character> it = stringSet.iterator();
        while (it.hasNext()) {
            char c = it.next();
            charL.add(c);
        }
        double size = Math.pow(charL.size(), 3);
        int bs;
        if (charL.size() == 0) {
            bs = 0;
        }
        else {
            bs = (int)size / charL.size();
        }
        List<Character[]> toAdd = new ArrayList<>((int) size);
        for (int i = 0; i < size; i++) {
            toAdd.add(new Character[3]);
        }
        List<String> toPrint = new ArrayList<>((int) size);
        createThrees(charL, toPrint, toAdd, 0, (int) bs);
        for (String s : toPrint) {
            System.out.println(s);
        }
    }

    /**
     * A method to update a list of lists of characters of size 3 using the given list of strings, when all three
     * characters have been added, it updates the list of strings for printing
     * @param baseLine the characters available to be put into toAdd
     * @param toPrint the list that is updated when all character arrays have been filled
     * @param toAdd a list of character arrays that are updated index by index
     * @param currIndex the current index of the arrays iin toAdd the method will be adding to
     * @param bs the size of size / baseline used in the pattern of adding characters to the character arrays in toAdd
     */
    public static void createThrees(List<Character> baseLine, List<String> toPrint, List<Character[]> toAdd,
                                    int currIndex, int bs) {
        if (currIndex == 2) {
            int spot = 0;
            //outer loop for repeating sequence (size/baseline times)
            for (int j = 1; j <= baseLine.size(); j++) {
                //mid loop for getting the char
                for (int i = 0; i < baseLine.size(); i++) {
                    //inner loop for repeating the chars
                    for (int k = 1; k <= baseLine.size(); k++) {
                        toAdd.get(spot)[currIndex] = baseLine.get(i);
                        spot+=1;
                    }
                }
            }
            for (Character[] sArr : toAdd) {
                String check = convertToString(sArr);
                if (!toPrint.contains(check)) {
                    toPrint.add(check);
                }
            }

        }
        else if (currIndex == 1) {
            int spot = 0;
            //outside loop for repeating the sequence
            for (int j = 1; j <= bs; j++) {
                //inner loop for getting the chars
                for (int i = 0; i < baseLine.size(); i++) {
                    toAdd.get(spot)[currIndex] = baseLine.get(i);
                    spot+=1;
                }
            }
            currIndex+=1;
            createThrees(baseLine, toPrint, toAdd, currIndex, bs);
        }
        else {
            int spot = 0;
            //outer loop for going through each char
            for (int i = 0; i < baseLine.size(); i++) {
                //inner loop for repeating each char before moving onto next
                for (int j = 1; j <= bs; j++) {
                    toAdd.get(spot)[currIndex] = baseLine.get(i);
                    spot +=1;
                }
            }
            currIndex+=1;
            createThrees(baseLine, toPrint, toAdd, currIndex, bs);
        }
    }

    /**
     * A method that converts and return a given character array to a string
     * @param cArr the character array that will be converted to a string
     * @return the character array as a string
     */
    public static String convertToString(Character[] cArr) {
        StringBuilder sb = new StringBuilder(3);
        for (Character s : cArr) {
            sb.append(s);
        }
        return sb.toString();
    }
}
