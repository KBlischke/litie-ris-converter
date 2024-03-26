import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import modules.Parser;
import modules.Translator;

/**
 * JUnit tests for the {@link Translator} class.
 */
public class TestTranslator {

    /**
     * Tests the {@link Translator#getCategories()} method.
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
        Translator newEntry = new Translator(entry);
        assertArrayEquals(new String[]{"CN", "TY", "PY", "LA"}, newEntry.getCategories());
    }

    /**
     * Tests the {@link Translator#hasField()} method.
     *
     * @throws IllegalStateException if an illegal state is encountered
     */
    @Test
    void testHasField() throws IllegalStateException {
        String input = """
                000: 00001
                """;
        Parser entry = new Parser(input);
        Translator newEntry = new Translator(entry);
        assertEquals(true, newEntry.hasField("CN"));
        assertEquals(false, newEntry.hasField("IE"));
    }

    /**
     * Tests the {@link Translator#getField()} method.
     *
     * @throws IllegalStateException if an illegal state is encountered
     */
    @Test
    void testGetField() throws IllegalStateException {
        String input = """
                000: 00001
                """;
        Parser entry = new Parser(input);
        Translator newEntry = new Translator(entry);
        assertEquals("00001", newEntry.getField("CN")[0]);
        assertNotEquals("00002", newEntry.getField("CN")[0]);
        assertThrows(
            IndexOutOfBoundsException.class,
            () -> newEntry.getField("IE")
        );
    }

    /**
     * Tests the {@link Translator#getField()} method for the field {@code RT}.
     *
     * @throws IllegalStateException if an illegal state is encountered
     */
    @Test
    void testGetReferenceType() throws IllegalStateException {
        String input = """
                030: m
                """;
        Parser entry = new Parser(input);
        Translator newEntry = new Translator(entry);
        assertEquals("BOOK", newEntry.getField("TY")[0]);

        String input2 = """
                030: a
                """;
        Parser entry2 = new Parser(input2);
        Translator newEntry2 = new Translator(entry2);
        assertEquals("JOUR", newEntry2.getField("TY")[0]);

        String input3 = """
                030: a
                700: Musterquelle
                860: Musterimpressum
                """;
        Parser entry3 = new Parser(input3);
        Translator newEntry3 = new Translator(entry3);
        assertEquals("CHAP", newEntry3.getField("TY")[0]);
    }

    /**
     * Tests the {@link Translator#getField()} method for the fields {@code TI} and {@code T2}.
     *
     * @throws IllegalStateException if an illegal state is encountered
     */
    @Test
    void testGetTitle() throws IllegalStateException {
        String input1 = """
                100: Mustertitel : Musterzusatz
                """;
        Parser entry1 = new Parser(input1);
        Translator newEntry1 = new Translator(entry1);
        assertEquals("Mustertitel: Musterzusatz", newEntry1.getField("TI")[0]);

        String input2 = """
                030: ?
                100: Mustertitel : Musterzusatz
                """;
        Parser entry2 = new Parser(input2);
        Translator newEntry2 = new Translator(entry2);
        assertEquals("Mustertitel", newEntry2.getField("TI")[0]);
        assertEquals("Musterzusatz", newEntry2.getField("T2")[0]);
    }

    /**
     * Tests the {@link Translator#getField()} method for the fields
     * {@code T2}, {@code VL}, {@code IS} and {@code UR}.
     *
     * @throws IllegalStateException if an illegal state is encountered
     */
    @Test
    void testGetSource() throws IllegalStateException {
        String input1 = """
                700: Mustertitel. 1(1) no.1, [http://musterseite.de]
                """;
        Parser entry1 = new Parser(input1);
        Translator newEntry1 = new Translator(entry1);
        assertEquals("Mustertitel", newEntry1.getField("T2")[0]);
        assertEquals("1(1)", newEntry1.getField("VL")[0]);
        assertEquals("1", newEntry1.getField("IS")[0]);
        assertEquals("http://musterseite.de", newEntry1.getField("UR")[0]);

        String input2 = """
                700: Mustertitel
                """;
        Parser entry2 = new Parser(input2);
        Translator newEntry2 = new Translator(entry2);
        assertEquals("Mustertitel", newEntry2.getField("T2")[0]);

        String input3 = """
                700: Mustertitel. http://musterseite.de
                """;
        Parser entry3 = new Parser(input3);
        Translator newEntry3 = new Translator(entry3);
        assertEquals("Mustertitel", newEntry3.getField("T2")[0]);
        assertEquals("http://musterseite.de", newEntry3.getField("UR")[0]);

        String input4 = """
                700: Mustertitel. Vol.1(1) nos.1/2, [http://musterseite.de]
                """;
        Parser entry4 = new Parser(input4);
        Translator newEntry4 = new Translator(entry4);
        assertEquals("Mustertitel", newEntry4.getField("T2")[0]);
        assertEquals("1(1)", newEntry4.getField("VL")[0]);
        assertEquals("1/2", newEntry4.getField("IS")[0]);
        assertEquals("http://musterseite.de", newEntry4.getField("UR")[0]);

        String input5 = """
                700: Mustertitel. Nr.1 vom 01.01.2000
                """;
        Parser entry5 = new Parser(input5);
        Translator newEntry5 = new Translator(entry5);
        assertEquals("Mustertitel", newEntry5.getField("T2")[0]);
        assertEquals("1", newEntry5.getField("VL")[0]);
        assertEquals("01.01.2000", newEntry5.getField("DA")[0]);
    }

    /**
     * Tests the {@link Translator#getField()} method for the fields
     * {@code CY}, {@code PB} and {@code DA}.
     *
     * @throws IllegalStateException if an illegal state is encountered
     */
    @Test
    void testGetContact() throws IllegalStateException {
        String input1 = """
                860: Musterort : Musterverlag
                """;
        Parser entry1 = new Parser(input1);
        Translator newEntry1 = new Translator(entry1);
        assertEquals("Musterort", newEntry1.getField("CY")[0]);
        assertEquals("Musterverlag", newEntry1.getField("PB")[0]);

        String input2 = """
                860: 01.01.2000
                """;
        Parser entry2 = new Parser(input2);
        Translator newEntry2 = new Translator(entry2);
        assertEquals("01.01.2000", newEntry2.getField("DA")[0]);

        String input3 = """
                860: Musterort1;Musterort2 : Musterverlag
                """;
        Parser entry3 = new Parser(input3);
        Translator newEntry3 = new Translator(entry3);
        assertEquals("Musterort1", newEntry3.getField("CY")[0]);
        assertEquals("Musterort2", newEntry3.getField("CY")[1]);
        assertEquals("Musterverlag", newEntry3.getField("PB")[0]);
    }

    /**
     * Tests the {@link Translator#getField()} method for the fields {@code N1} and {@code UR}.
     *
     * @throws IllegalStateException if an illegal state is encountered
     */
    @Test
    void testGetFootnote() throws IllegalStateException {
        String input1 = """
                880: Musterfußnote
                """;
        Parser entry1 = new Parser(input1);
        Translator newEntry1 = new Translator(entry1);
        assertEquals("Fußnote: Musterfußnote", newEntry1.getField("N1")[0]);

        String input2 = """
                880: vgl. https://musterseite.de
                """;
        Parser entry2 = new Parser(input2);
        Translator newEntry2 = new Translator(entry2);
        assertEquals("https://musterseite.de", newEntry2.getField("UR")[0]);
    }

    /**
     * Tests the {@link Translator#getField()} method for the field {@code KW}.
     *
     * @throws IllegalStateException if an illegal state is encountered
     */
    @Test
    void testGetKeyword() throws IllegalStateException {
        String input = """
                510: Musterthema
                520: Musterfach
                530: Musterform
                540: Musterobjekt
                """;
        Parser entry = new Parser(input);
        Translator newEntry = new Translator(entry);
        assertEquals("Musterthema", newEntry.getField("KW")[0]);
        assertEquals("Musterfach", newEntry.getField("KW")[1]);
        assertEquals("Musterform", newEntry.getField("KW")[2]);
        assertEquals("Musterobjekt", newEntry.getField("KW")[3]);
    }

    /**
     * Tests the {@link Translator#getField()} method for the field {@code N1}.
     *
     * @throws IllegalStateException if an illegal state is encountered
     */
    @Test
    void testGetNote() throws IllegalStateException {
        String input = """
                875: Musterinhalt
                880: Musterfußnote
                """;
        Parser entry = new Parser(input);
        Translator newEntry = new Translator(entry);
        assertEquals("Inhalt: Musterinhalt", newEntry.getField("N1")[0]);
        assertEquals("Fußnote: Musterfußnote", newEntry.getField("N1")[1]);
    }
}
