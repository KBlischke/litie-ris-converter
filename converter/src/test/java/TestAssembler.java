import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import modules.Assembler;
import modules.Parser;
import modules.Translator;

/**
 * JUnit test class for the {@link Assembler} class.
 */
public class TestAssembler {

    /**
     * Tests the {@link Assembler#assemble()} method.
     */
    @Test
    void testAssemble() {
        String input1 = """
                000: 00001
                030: m
                040: 2000
                050: d
                100: Mustertitel : Musterzusatz
                700: [http://musterseite.de]
                """;
        String result1 = """
                TY - BOOK
                CN - 00001
                PY - 2000
                LA - d
                TI - Mustertitel: Musterzusatz
                UR - http://musterseite.de
                ER -
                
                """;
        assertEquals(result1, Assembler.assemble(new Translator(new Parser(input1))));

        String input2 = """
                510: Musterthema
                520: Musterfach
                530: Musterform
                540: Musterobjekt
                """;
        String result2 = """
                TY - GEN
                KW - Musterthema
                KW - Musterfach
                KW - Musterform
                KW - Musterobjekt
                ER -
                
                """;
        assertEquals(result2, Assembler.assemble(new Translator(new Parser(input2))));

        String input3 = """
                000: 00001
                030: a
                100: Mustertitel : Musterzusatz
                700: Musterquelle
                """;
        String result3 = """
                TY - JOUR
                CN - 00001
                TI - Mustertitel: Musterzusatz
                T2 - Musterquelle
                ER -
                
                """;
        assertEquals(result3, Assembler.assemble(new Translator(new Parser(input3))));
    }
}
