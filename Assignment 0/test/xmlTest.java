import org.junit.Test;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import static org.junit.Assert.assertEquals;

public class xmlTest {

    @Test
    public void name() {
        xml x = new xml();
        String[] s = new String[3];
        s[0] = "<state";
        s[1] = "id=\"0\"";
        s[2] = "name=\"q1\"><initial/></state>";
        assertEquals("", x.findPhrase(s, "name="));
    }

    @Test
    public void initial() {
        xml x = new xml();
        String[] s = new String[3];
        s[0] = "<state";
        s[1] = "id=\"0\"";
        s[2] = "name=\"q1\"><initial/></state>";
        assertEquals("", x.findPhrase(s, "initial/"));
    }
    @Test
    public void nofinal() {
        xml x = new xml();
        String[] s = new String[3];
        s[0] = "<state";
        s[1] = "id=\"0\"";
        s[2] = "name=\"q1\"><initial/></state>";
        assertEquals("", x.findPhrase(s, "final/"));
    }

    @Test
    public void nointial() {
        xml x = new xml();
        String[] s = new String[3];
        s[0] = "<state";
        s[1] = "id=\"1\"";
        s[2] = "name=\"q3\"></state>\n";
        assertEquals("", x.findPhrase(s, "initial/"));
    }

    @Test
    public void fin() {
        xml x = new xml();
        String[] s = new String[3];
        s[0] = "<state";
        s[1] = "id=\"1\"";
        s[2] = "name=\"q2\"><final/></state>";
        assertEquals("", x.findPhrase(s, "final/"));
    }

    @Test
    public void fin2() {
        xml x = new xml();
        InputStream i;
        try {
           i = new FileInputStream("fig1.4.jff");
        }
        catch (IOException e) {
            throw new IllegalArgumentException("file not found");
        }
        assertEquals("", x.parseFile(new InputStreamReader(i)));
    }
}
