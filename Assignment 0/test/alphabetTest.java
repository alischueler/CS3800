import org.junit.Test;

import java.util.*;

import static org.junit.Assert.assertEquals;

public class alphabetTest {
    @Test
    public void testInput() {
        alphabet a = new alphabet();
        List<Character> s = new ArrayList<Character>(1);
        s.add(' ');
        int size = (int)Math.pow(s.size(), 3);
        List<String> to = new ArrayList<>(size);
        List<Character[]> to2 = new ArrayList<>(size);
        for (int i = 0; i < size; i++) {
            to2.add(new Character[3]);
        }
      //  a.createThrees(s, to, to2, 0, size, size / s.size());
        for (String q : to) {
           System.out.println(q);
        }
    }
}
