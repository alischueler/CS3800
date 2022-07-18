import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class Matrix {

    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);
        String[] sarr = new String[3];
        sarr[0] = scan.next();
        sarr[1] = scan.next();
        sarr[2] = scan.next();
        scan.nextLine();
        int count = 0;
        int[][] x = new int[Integer.parseInt(sarr[0])][Integer.parseInt(sarr[1])];
        int[][] y = new int[Integer.parseInt(sarr[1])][Integer.parseInt(sarr[2])];
        int[][] z = new int[Integer.parseInt(sarr[0])][Integer.parseInt(sarr[2])];
        while (scan.hasNextLine()) {
            if (Integer.parseInt(sarr[0])>count){
                int[] toAdd = new int[Integer.parseInt(sarr[1])];
                for (int i = 0; i < Integer.parseInt(sarr[1]); i++) {
                    String curr = scan.next();
                    toAdd[i] = Integer.parseInt(curr);
                }
                x[count] = toAdd;
            }
            else if (Integer.parseInt(sarr[1])+Integer.parseInt(sarr[0])>count){
                int[] toAdd = new int[Integer.parseInt(sarr[2])];
                for (int i = 0; i < Integer.parseInt(sarr[2]); i++) {
                    String curr = scan.next();
                    toAdd[i] = Integer.parseInt(curr);
                }
                y[count-Integer.parseInt(sarr[0])] = toAdd;
            }
            else {
                int[] toAdd = new int[Integer.parseInt(sarr[2])];
                for (int i = 0; i < Integer.parseInt(sarr[2]); i++) {
                    String curr = scan.next();
                    toAdd[i] = Integer.parseInt(curr);
                }
                z[count-Integer.parseInt(sarr[1])-Integer.parseInt(sarr[0])] = toAdd;
            }
            count+=1;
            scan.nextLine();
        }
        int[][] xy = new int[x.length][y[0].length];
        for (int row = 0; row < xy.length; row++) {
            for (int col = 0; col < xy[row].length; col++) {
                xy[row][col] = multiplyMatricesCell(x, y, row, col);
            }
        }
        if (Arrays.deepEquals(xy, z)) {
            System.out.println(1);
        }
        else {
            System.out.println(0);
        }
    }

    static int multiplyMatricesCell(int[][] one, int[][] two, int row, int col) {
        int cell = 0;
        for (int i = 0; i < two.length; i++) {
            cell += one[row][i] * two[i][col];
        }
        return cell;
    }
}
