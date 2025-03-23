package resources;

import org.junit.Test;
import static org.junit.Assert.*;

public class PlayersTest {
    Players types;

    @Test
    public void testValidType() {
        types = new Players("A", "B");

        assertTrue(types.validType("A"));
        assertTrue(types.validType("B"));

        assertFalse(types.validType("X"));
        assertFalse(types.validType(""));
        assertFalse(types.validType("na"));
        assertFalse(types.validType(null));
    }

    @Test
    public void testOpposites() {
        types = new Players("", "X");
        assertEquals("X", types.opposite(""));
        assertEquals("", types.opposite("X"));
    }

    @Test(expected = RuntimeException.class)
    public void testInvalidOpposite() {
        types = new Players("X", "Y");
        types.opposite("Z");
    }
}
