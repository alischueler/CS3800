import java.util.ArrayList;
import java.util.List;

public class Node {
    int num;
    int[] to;
    int color;
    int count;

    public Node(int num, int size) {
        this.num = num;
        this.to = new int[size];
        this.count = 0;
    }

    public void addColor(int c){
        this.color = c;
    }

    public void addTo(Integer to1) {
        to[count] = (to1);
        count+=1;
    }

    public int[] getTo(){
        return to;
    }

    public int getColor() {
        return color;
    }
}
