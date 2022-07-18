import org.junit.Test;

import java.util.ArrayList;
import java.util.Set;

import static org.junit.Assert.assertEquals;

public class powersetTest {
//    @Test
//    public void testInput() {
//        powerset p = new powerset();
//        String[] s = new String[1];
//        s[0] = "";
//        System.out.println("start");
//        for (String s2 : p.changeString(p.findPower(s))) {
//            System.out.println(s2);
//        }
//        System.out.println("end");
//        //assertEquals("", p.changeString(p.findPower(s)));
//    }

    @Test
    public void testInput1() {
        powerset p = new powerset();
        String[] s = new String[11];
        s[0] = "0";
        s[1] = "1";
        s[2] = "2";
        s[3] = "3";
        s[4] = "4";
        s[5] = "5";
        s[6] = "6";
        s[7] = "7";
        s[8] = "8";
        s[9] = "9";
        s[10] = "START";
        System.out.println("start");
        p.printPowerSet(s, 11);
//        for (Set s2 : (p.printPowerSet(s, 11))) {
//            System.out.println(s2);
//        }
//        System.out.println("end");
        assertEquals("", (p.changeString(p.printPowerSet(s, 11))));
    }

    @Test
    public void testInput2() {
        powerset p = new powerset();
        String[] s = new String[3];
        s[0] = "a";
        s[1] = "b";
        s[2] = "c";
        assertEquals("", (p.findPower(s)));
    }

}
