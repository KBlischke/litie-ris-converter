import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import modules.Parser;

/**
 * Tests for the {@link Parser} class.
 */
public class TestParser {

    /**
     * Tests the {@link Parser#getCategories()} method.
     */
    @Test
    void testGetCategories() {
        String input = """
                000: 0001
                030: m
                040: 2000
                050: d
                """;
        Parser entry = new Parser(input);
        assertArrayEquals(new String[]{"000", "030", "040", "050"}, entry.getCategories());
    }

    /**
     * Tests the {@link Parser#hasField()} method.
     */
    @Test
    void testHasField() throws IllegalStateException {
        String input = """
                000: 00001
                """;
        Parser entry = new Parser(input);
        assertEquals(true, entry.hasField("000"));
        assertEquals(false, entry.hasField("001"));
    }

    /**
     * Tests the {@link Parser#getField()} method.
     */
    @Test
    void testGetField() throws IllegalStateException {
        String input = """
                000: 00001
                """;
        Parser entry = new Parser(input);
        assertEquals("00001", entry.getField("000")[0]);
        assertNotEquals("00002", entry.getField("000")[0]);
        assertThrows(
            IndexOutOfBoundsException.class,
            () -> entry.getField("001")
        );
    }

    /**
     * Tests the {@link Parser#parseField()} method.
     */
    @Test
    void testParseField() throws IllegalStateException {
        String input1 = """
                030: m
                """;
        Parser entry1 = new Parser(input1);
        assertEquals("m", entry1.getField("030")[0]);

        String input2 = """
                030: m|a
                """;
        Parser entry2 = new Parser(input2);
        assertEquals("m", entry2.getField("030")[0]);
        assertEquals("a", entry2.getField("030")[1]);

        String input3 = """
                030: m | a | x
                """;
        Parser entry3 = new Parser(input3);
        assertEquals("m", entry3.getField("030")[0]);
        assertEquals("a", entry3.getField("030")[1]);
        assertEquals("x", entry3.getField("030")[2]);
    }

    /**
     * Tests the {@link Parser#parseField()} method for the field {@code 100}.
     */
    @Test
    void testParseField100() throws IllegalStateException {
        String input1 = """
                100: Mustertitel
                """;
        Parser entry1 = new Parser(input1);
        assertEquals("Mustertitel", entry1.getField("100")[0]);

        String input2 = """
                100: Mustertitel : Musterzusatz
                """;
        Parser entry2 = new Parser(input2);
        assertEquals("Mustertitel", entry2.getField("100")[0]);
        assertEquals("Musterzusatz", entry2.getField("100")[1]);

        String input3 = """
                100: https://Musterseite.de
                """;
        Parser entry3 = new Parser(input3);
        assertEquals("https://Musterseite.de", entry3.getField("100")[0]);
    }

    /**
     * Tests the {@link Parser#parseField700()} method.
     */
    @Test
    void testParseField700() throws IllegalStateException {
        String input1 = """
                700: Mustertitel: Musterzusatz. 1(1) no.1,
                """;
        Parser entry1 = new Parser(input1);
        assertEquals("Mustertitel: Musterzusatz", entry1.getField("700")[0]);
        assertEquals("1(1)", entry1.getField("700")[1]);
        assertEquals("no.1", entry1.getField("700")[2]);

        String input2 = """
                700: Mustertitel: Musterzusatz. 2020 [http://musterseite.de]
                """;
        Parser entry2 = new Parser(input2);
        assertEquals("Mustertitel: Musterzusatz", entry2.getField("700")[0]);
        assertEquals("2020", entry2.getField("700")[1]);
        assertEquals("http://musterseite.de", entry2.getField("700")[2]);

        String input3 = """
                700: Mustertitel: Musterzusatz
                """;
        Parser entry3 = new Parser(input3);
        assertEquals("Mustertitel: Musterzusatz", entry3.getField("700")[0]);

        String input4 = """
                700: Mustertitel. Nr.257 vom 01.01.2000
                """;
        Parser entry4 = new Parser(input4);
        assertEquals("Mustertitel", entry4.getField("700")[0]);
        assertEquals("257", entry4.getField("700")[1]);
        assertEquals("01.01.2000", entry4.getField("700")[2]);
    }

    /**
     * Tests the {@link Parser#parseField860()} method.
     */
    @Test
    void testParseField860() throws IllegalStateException {
        String input1 = """
                860: Musterstadt : Musterverlag
                """;
        Parser entry1 = new Parser(input1);
        assertEquals("Musterstadt", entry1.getField("860")[0]);
        assertEquals("Musterverlag", entry1.getField("860")[1]);

        String input2 = """
                860: Musterstadt : , : Musterverlag
                """;
        Parser entry2 = new Parser(input2);
        assertEquals("Musterstadt", entry2.getField("860")[0]);
        assertEquals("Musterverlag", entry2.getField("860")[1]);

        String input3 = """
                860: Musterstadt1 ; Musterstadt2 : Musterverlag : Musterabteilung
                """;
        Parser entry3 = new Parser(input3);
        assertEquals("Musterstadt1;Musterstadt2", entry3.getField("860")[0]);
        assertEquals("Musterverlag : Musterabteilung", entry3.getField("860")[1]);

        String input4 = """
                860: Musterstadt
                """;
        Parser entry4 = new Parser(input4);
        assertEquals("Musterstadt", entry4.getField("860")[0]);
    }
}
