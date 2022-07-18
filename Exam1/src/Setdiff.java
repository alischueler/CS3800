import java.util.*;

public class Setdiff {

    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);
        List<String> set1 = new ArrayList<>();
        List<String> set2 = Arrays.asList("bat", "horse", "turtle", "fish", "squirrel", "bird");
        while (scan.hasNext()) {
            set1.add(scan.next());
        }
        LinkedHashSet<String> output = checkDiff(set1, set2);
        StringBuilder sb = new StringBuilder();
        for (String s : output) {
            if (sb.length() !=0) {
                sb.append(" ");
            }
            sb.append(s);
        }
        if (sb.length()>0) {
            System.out.println(sb);
        }
        else {
            System.out.println();
        }
    }

    public static LinkedHashSet<String> checkDiff(List<String> set1, List<String> set2) {
        LinkedHashSet<String> set = new LinkedHashSet<>();
        for (String s1 : set1) {
            if (!set2.contains(s1)) {
                set.add(s1);
            }
        }
        return set;
    }
}
