import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import modules.Unifier;

/**
 * JUnit test class for the {@link Unifier} class.
 */
public class TestUnifier {

    /**
     * Tests the {@link Unifier#unify()} method for general unification.
     */
    @Test
    void testUnifyAll() {
        // 
        String input1 = """
                 001 : abc 
                """;
        assertEquals("001: abc\n", Unifier.unify(input1));

        String input2 = """
                001:abc
                """;
        assertEquals("001: abc\n", Unifier.unify(input2));

        String input3 = """
                001:¬abc
                """;
        assertEquals("001: abc\n", Unifier.unify(input3));
    }

    /**
     * Tests the {@link Unifier#unify()} method for field <code>030</code>.
     */
    @Test
    void testUnify030() {
        String input1 = """
                030: m | a
                """;
        assertEquals("030: m|a\n", Unifier.unify(input1));

        String input2 = """
                030: M oder A
                """;
        assertEquals("030: m|a\n", Unifier.unify(input2));

        String input3 = """
                030: Monographie s. m
                """;
        assertEquals("030: m\n", Unifier.unify(input3));
    }

    /**
     * Tests the {@link Unifier#unify()} method for field {@code 100}.
     */
    @Test
    void testUnify100() {
        String input1 = """
                100: Mustertitel:Musterzusatz
                """;
        assertEquals("100: Mustertitel : Musterzusatz\n", Unifier.unify(input1));

        String input2 = """
                100: Mustertitel : Musterzusatz
                """;
        assertEquals("100: Mustertitel : Musterzusatz\n", Unifier.unify(input2));

        String input3 = """
                100: https://Musterseite.de
                """;
        assertEquals("100: https://Musterseite.de\n", Unifier.unify(input3));
    }

    /**
     * Tests the {@link Unifier#unify()} method for field {@code 21E}.
     */
    @Test
    void testUnify21E() {
        String input1 = """
                21E: Musterherausgeber ; Musterherausgeberin
                """;
        assertEquals("21E: Musterherausgeber;Musterherausgeberin\n", Unifier.unify(input1));

        String input2 = """
                21E: Musterherausgeber |Musterherausgeberin
                """;
        assertEquals("21E: Musterherausgeber;Musterherausgeberin\n", Unifier.unify(input2));
    }

    /**
     * Tests the {@link Unifier#unify()} method for field {@code 411}.
     */
    @Test
    void testUnify411() {
        String input1 = """
                411: Mustertitel; 1
                """;
        assertEquals("411: Mustertitel ; 1\n", Unifier.unify(input1));

        String input2 = """
                411: Mustertitel ; 1
                """;
        assertEquals("411: Mustertitel ; 1\n", Unifier.unify(input2));
    }

    /**
     * Tests the {@link Unifier#unify()} method for field {@code 700}.
     */
    @Test
    void testUnify700() {
        String input1 = """
                700: Musterquelle S.10-100
                """;
        assertEquals("700: Musterquelle\n810: S.10-100\n", Unifier.unify(input1));

        String input2 = """
                700: Musterquelle S.10
                """;
        assertEquals("700: Musterquelle\n810: S.10\n", Unifier.unify(input2));

        String input3 = """
                700: Musterquelle S. 10 - 100 Musterzusatz
                """;
        assertEquals("700: Musterquelle Musterzusatz\n810: S.10-100\n", Unifier.unify(input3));
    }

    /**
     * Tests the {@link Unifier#unify()} method for field {@code 200}.
     */
    @Test
    void testUnifyPipeDelimiter() {
        String input1 = """
                200: Mustermann, M.|Musterfrau, E.
                """;
        assertEquals("200: Mustermann, M.|Musterfrau, E.\n", Unifier.unify(input1));

        String input2 = """
                200: Mustermann, M. | Musterfrau, E.
                """;
        assertEquals("200: Mustermann, M.|Musterfrau, E.\n", Unifier.unify(input2));
    }
}
