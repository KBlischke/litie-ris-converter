import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import modules.Writer;

/**
 * Tests for the {@link Writer} class.
 */
public class TestWriter {

    /**
     * The path to the test file used in the tests.
     */
    private static final String TEST_FILE_PATH = "src/test/resources/test.ris";

    /**
     * Deletes the test file after each test to maintain a clean state.
     *
     * @throws IOException If an I/O error occurs while deleting the file.
     */
    @AfterEach
    void tearDown() throws IOException {
        Files.deleteIfExists(Path.of(TEST_FILE_PATH));
    }

    /**
     * Tests the {@link Writer#append(String)} method by appending content to the file
     * and verifying if the actual content matches the expected content.
     *
     * @throws IOException If an I/O error occurs during the test.
     */
    @Test
    void testAppend() throws IOException {
        Writer writer = new Writer(TEST_FILE_PATH);
        String contentToAppend = "Mustereintrag";

        writer.append(contentToAppend);

        String actualContent = Files.readString(Path.of(TEST_FILE_PATH));
        assertEquals(contentToAppend, actualContent);
    }

    /**
     * Tests the {@link Writer#append(String)} method by appending content multiple times to the file
     * and verifying if the actual content matches the expected content.
     *
     * @throws IOException If an I/O error occurs during the test.
     */
    @Test
    void testAppendMultipleTimes() throws IOException {
        Writer writer = new Writer(TEST_FILE_PATH);
        String contentToAppend1 = "Mustereinleitung";
        String contentToAppend2 = "Musterschluss";
        String expectedContent = contentToAppend1 + contentToAppend2;

        writer.append(contentToAppend1);
        writer.append(contentToAppend2);

        String actualContent = Files.readString(Path.of(TEST_FILE_PATH));
        assertEquals(expectedContent, actualContent);
    }

    /**
     * Tests the {@link Writer#append(String)} method by appending an empty string to the file
     * and verifying if the actual content is an empty string.
     *
     * @throws IOException If an I/O error occurs during the test.
     */
    @Test
    void testAppendEmptyString() throws IOException {
        Writer writer = new Writer(TEST_FILE_PATH);

        writer.append("");

        String actualContent = Files.readString(Path.of(TEST_FILE_PATH));
        assertEquals("", actualContent);
    }
}
