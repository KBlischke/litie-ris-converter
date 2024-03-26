import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import modules.InputHandler;

public class TestInputHandler {
    @Test
    void testGetArgSize() throws IllegalArgumentException {
        String[] testArgs1 = {"test.dbm"};
        InputHandler testInput1 = new InputHandler(testArgs1);
        assertEquals(1, testInput1.getArgSize());

        String[] testArgs2 = {"test.dbm", "test.ris"};
        InputHandler testInput2 = new InputHandler(testArgs2);
        assertEquals(2, testInput2.getArgSize());
    }

    @Test
    void testGetSource() throws IllegalArgumentException {
        String[] testArgs1 = {"test.dbm"};
        InputHandler testInput1 = new InputHandler(testArgs1);
        assertEquals("test.dbm", testInput1.getSource());
        assertNotEquals(".dbm", testInput1.getSource());

        String[] testArgs2 = {"directory/test.dbm"};
        InputHandler testInput2 = new InputHandler(testArgs2);
        assertEquals("directory/test.dbm", testInput2.getSource());
        assertNotEquals("test.dbm", testInput2.getSource());

        String[] testArgs3 = {"TEST.DBM"};
        InputHandler testInput3 = new InputHandler(testArgs3);
        assertEquals("TEST.DBM", testInput3.getSource());

        String[] testArgs4 = {"test.dbm", "test.ris"};
        InputHandler testInput4 = new InputHandler(testArgs4);
        assertEquals("test.dbm", testInput4.getSource());

        String[] testArgs5 = {"test.ris"};
        assertThrows(IllegalArgumentException.class, () -> {new InputHandler(testArgs5);});

        String[] testArgs6 = {"test"};
        assertThrows(IllegalArgumentException.class, () -> {new InputHandler(testArgs6);});
    }

    @Test
    void testGetDest() throws IllegalArgumentException {
        String[] testArgs1 = {"test.dbm"};
        InputHandler testInput1 = new InputHandler(testArgs1);
        assertEquals("test.ris", testInput1.getDest());
        assertNotEquals(".ris", testInput1.getDest());

        String[] testArgs2 = {"directory/test.dbm"};
        InputHandler testInput2 = new InputHandler(testArgs2);
        assertEquals("directory/test.ris", testInput2.getDest());
        assertNotEquals("test.ris", testInput2.getDest());

        String[] testArgs3 = {"TEST.DBM"};
        InputHandler testInput3 = new InputHandler(testArgs3);
        assertEquals("TEST.ris", testInput3.getDest());

        String[] testArgs4 = {"test.dbm", "test.ris"};
        InputHandler testInput4 = new InputHandler(testArgs4);
        assertEquals("test.ris", testInput4.getDest());

        String[] testArgs5 = {"TEST.DBM", "TEST.RIS"};
        InputHandler testInput5 = new InputHandler(testArgs5);
        assertEquals("TEST.RIS", testInput5.getDest());

        String[] testArgs6 = {"directory/test.dbm", "directory/test.ris"};
        InputHandler testInput6 = new InputHandler(testArgs6);
        assertEquals("directory/test.ris", testInput6.getDest());
        assertNotEquals("test.ris", testInput6.getDest());

        String[] testArgs7 = {"test.ris", "test.dbm"};
        assertThrows(IllegalArgumentException.class, () -> {new InputHandler(testArgs7);});

        String[] testArgs8 = {"test", "test"};
        assertThrows(IllegalArgumentException.class, () -> {new InputHandler(testArgs8);});
    }
}
