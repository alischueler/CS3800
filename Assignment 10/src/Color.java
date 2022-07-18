import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Color {

    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);
        int verticies = Integer.parseInt(scan.next());
        int edges = Integer.parseInt(scan.next());
        scan.nextLine();
        int count = 1;
        List<Node> colors = new ArrayList<>(verticies);
        for (int i = 1; i <= verticies; i++) {
            colors.add(new Node(i, edges));
        }
        while (count <= edges) {
            String one = scan.next();
            int oneint = Integer.parseInt(one);
            String two = scan.next();
            int twoint = Integer.parseInt(two);
            Node n1 = colors.get(oneint - 1);
            n1.addTo(twoint);
            Node n2 = colors.get(twoint - 1);
            n2.addTo(oneint);
            count += 1;
        }
        for (int i = 1; i <= verticies; i++) {
            Node curr = colors.get(i - 1);
            curr.addColor(Integer.parseInt(scan.next()));
        }
        boolean one = false;
        for (Node n : colors) {
            int[] to = n.getTo();
            for (int i : to) {
                if ((n.getColor())==(i)) {
                    one = true;
                    System.out.println(0);
                    break;
                }
            }
        }
        if (!one) {
            System.out.println(1);
        }

    }
}
