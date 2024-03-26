import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import modules.Validator;

/**
 * Tests for the {@link Validator} class.
 */
public class TestValidator {

    /**
     * Tests the {@link Validator#isValid()} method.
     */
    @Test
    void testIsValid() {
        String input1 = """
                000:00001
                030:m
                040:2000
                """;
        Validator validator1 = new Validator(input1);
        assertTrue(validator1.isValid());

        String input2 = """
                000:00001
                040:2000
                """;
        Validator validator2 = new Validator(input2);
        assertFalse(validator2.isValid());

        String input3 = """
                000:00001
                030:Monographie s. m
                """;
        Validator validator3 = new Validator(input3);
        assertFalse(validator3.isValid());

        String input4 = """
                000:00001
                030:Monographie siehe m
                """;
        Validator validator4 = new Validator(input4);
        assertFalse(validator4.isValid());
    }
}
