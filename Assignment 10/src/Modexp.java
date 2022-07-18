import java.util.Scanner;

public class Modexp {

    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);
        String one = scan.next();
        String two = scan.next();
        String three = scan.next();
        String four = scan.next();
        long a = Integer.parseInt(one);
        long b = Integer.parseInt(two);
        long c = Integer.parseInt(three);
        long p = Integer.parseInt(four);
        long compare = exp(a, b, p);
        if (compare == c) {
            System.out.println(1);
        } else {
            System.out.println(0);
        }
    }

    public static long exp(long a, long b, long p) {
        a %= p;
        long toRet = 1;
        while (b >= 1) {
            if ((b % 2) == 1) {
                toRet = (toRet * a) % p;
            }
            a = (a * a) % p;
            b /= 2;
        }
        return toRet;
    }
}
