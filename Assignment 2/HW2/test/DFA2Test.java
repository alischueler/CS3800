import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class DFA2Test {

    @Test
    public void testGood() {
        DFA14 one = new DFA14();
        one.setUpInfo();
        DFA2 two = new DFA2();
        assertTrue(two.run(one, "000111"));
    }

    @Test
    public void testBad() {
        DFA14 one = new DFA14();
        one.setUpInfo();
        DFA2 two = new DFA2();
        assertTrue(!two.run(one, "10"));
    }

}