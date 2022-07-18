import java.beans.JavaBean;
import java.util.*;

public class powerset {

    /**
     * A main method to output all sets (separated by spaces) within the powerset (on separate lines) of the given
     * string through the command line
     * @param args this is not referenced in the method
     */
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Set<String> stringSet = new HashSet<>();
        while (scanner.hasNext()) {
            String s = scanner.next();
            stringSet.add(s);
        }
        String[] stringList = new String[stringSet.size()];
        int count = 0;
        for (String s : stringSet) {
            stringList[count] = s;
            count +=1;
        }

//        Set<Set<String>> toPrint = findPower(stringList);
//        List<String> toPrintString = changeString(toPrint);
//        for (String s : toPrintString) {
//            System.out.println(s);
//        }
    }

//    public Set<Set<String>> recursivePowerSet(Set<String> set) {
//        if (set.isEmpty()) {
//            Set<Set<String>> ret = new HashSet<>();
//            ret.add(set);
//            return ret;
//        }
//
//        String element = set.iterator().next();
//        Set<String> subSetWithoutElement = getSubSetWithoutElement(set, element);
//        Set<Set<String>> powerSetSubSetWithoutElement = recursivePowerSet(subSetWithoutElement);
//        Set<Set<String>> powerSetSubSetWithElement = addElementToAll(powerSetSubSetWithoutElement, element);
//
//        Set<Set<String>> powerSet = new HashSet<>();
//        powerSet.addAll(powerSetSubSetWithoutElement);
//        powerSet.addAll(powerSetSubSetWithElement);
//        return powerSet;
//    }

    static List<List<String>> printPowerSet(String[] set, int set_size) {
        List<List<String>> toRet = new ArrayList<>();
        /*set_size of power set of a set
        with set_size n is (2**n -1)*/
        long pow_set_size = (long)Math.pow(2, set_size);
        int counter, j;

        /*Run from counter 000..0 to 111..1*/
        for(counter = 0; counter < pow_set_size; counter++) {
            List<String> toAdd = new ArrayList<>();
            for(j = 0; j < set_size; j++) {
                /* Check if jth bit in the
                counter is set If set then
                print jth element from set */
                if((counter & (1 << j)) > 0)
                    toAdd.add(set[j]);
            }
            toRet.add(toAdd);
        }
        System.out.println(toRet.size());
        System.out.println(2^set_size);
        return toRet;
    }


    /**
     * Uses the given array of strings to find and and return its powerset as a list of list of string
     * @param set the array of strings to be used to find its powerset
     * @return a set of set of string, the power set of the given array of strings
     */
    public static Set<Set<String>> findPower(String[] set) {
        if (set.length == 0) {
            Set<Set<String>> mt = new HashSet<>();
            mt.add(new HashSet<>());
            return mt;
        }
        String h = set[0];
        String[] newSet = new String[set.length - 1];
        for (int i = 1; i < set.length; i++) {
            newSet[i - 1] = set[i];
        }
        Set<Set<String>> toPrint = iterate(h, findPower(newSet));
        return toPrint;
    }

    /**
     * Uses a given string and set of set of string and recursive calls to add all sets to a set of set of strings
     * (powerset)
     * @param head a single element that will be combined with other variations of the set string
     * @param setString the set of set of string that will be added to a set
     * @return a power set as represented by a set of set of strings where the inner set is individual sets
     */
    public static Set<Set<String>> iterate(String head, Set<Set<String>> setString) {
        Set<Set<String>> toPrint = new HashSet<>();
        for (Set<String> curr : setString) {
            Set<String> out = new HashSet<>();
            out.add(head);
            out.addAll(curr);
            toPrint.add(out);
        }
        toPrint.addAll(setString);
        return toPrint;
    }

    /**
     * Converts the given powerset (set of set of strings) to a list of strings where the inner set is converted to a
     * string with space separators
     * @param given a powerset in the form of a set of sets
     * @return a list of string representing the powerset with each set as a single list
     */
    public static List<String> changeString(List<List<String>> given) {
        List<String> fstring = new ArrayList<>(given.size());
        for (List<String> inner : given) {
            StringBuilder sb = new StringBuilder();
            Iterator<String> it = inner.iterator();
            while (it.hasNext()) {
                sb.append(it.next());
                if (it.hasNext()) {
                    sb.append(" ");
                }
            }
            fstring.add(sb.toString());
        }
        return fstring;
        }

}
