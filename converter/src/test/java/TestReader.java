import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.NoSuchElementException;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import modules.Reader;

/**
 * JUnit test class for the {@link Reader} class.
 */
public class TestReader {
    /**
     * The path to the test file used in the tests.
     */
    private static final String TEST_FILE_PATH = "src/test/resources/test.dbm";

    /**
     * The content of the test file used in the tests.
     */
    private static final String TEST_FILE_CONTENT = """
            000:00001
            030:a
            040:2000
            050:d
            100:Musterartikel : Musterzusatz
            200:Mustermann, M.
            540:Musterobjekt
            550:Musterstadt|Musterland|D
            700:Musterzeitschrift. 1(2000) H.1, S.1-10
            &&&
            
            000:00002
            030:m
            040:2000
            050:d
            100:Mustermonographie : Musterzusatz
            200:Musterfrau, E.
            540:Musterobjekt
            550:Musterstadt|Musterland|D
            &&&
            """;

    /**
     * Writes the test file before each test to maintain a clean state.
     *
     * @throws IOException If an I/O error occurs while deleting the file.
     */
    @BeforeEach
    void buildUp() throws IOException {
        Files.writeString(Paths.get(TEST_FILE_PATH), TEST_FILE_CONTENT, StandardCharsets.UTF_8);
    }

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
     * Tests the {@link Reader#getPath()} method.
     *
     * @throws InvalidPathException If the provided file path is invalid.
     * @throws IOException          If an I/O error occurs while reading the file.
     */
    @Test
    void testGetPath() throws InvalidPathException, IOException {
        Reader testReader = new Reader("src/test/resources/test.dbm");
        assertEquals("src/test/resources/test.dbm", testReader.getPath());
        assertNotEquals("test.dbm", testReader.getPath());
    }

    /**
     * Tests the {@link Reader#getSize()} method.
     *
     * @throws InvalidPathException If the provided file path is invalid.
     * @throws IOException          If an I/O error occurs while reading the file.
     */
    @Test
    void testGetSize() throws InvalidPathException, IOException {
        Reader testReader = new Reader("src/test/resources/test.dbm");
        assertEquals(20, testReader.getSize());
        assertNotEquals(10, testReader.getSize());
    }

    /**
     * Tests the {@link Reader#getPosition()} and {@link Reader#setPosition(int)} methods.
     *
     * @throws InvalidPathException If the provided file path is invalid.
     * @throws IOException          If an I/O error occurs while reading the file.
     */
    @Test
    void testPosition() throws InvalidPathException, IOException {
        Reader testReader = new Reader("src/test/resources/test.dbm");

        assertEquals(0, testReader.getPosition());
        assertNotEquals(10, testReader.getPosition());

        testReader.setPosition(10);
        assertEquals(10, testReader.getPosition());
        assertNotEquals(0, testReader.getPosition());

        assertThrows(IndexOutOfBoundsException.class, () -> {
            testReader.setPosition(-1);
        });

        assertThrows(IndexOutOfBoundsException.class, () -> {
            testReader.setPosition(1000000000);
        });
    }

    /**
     * Tests the {@link Reader#next()} method.
     *
     * @throws InvalidPathException If the provided file path is invalid.
     * @throws IOException          If an I/O error occurs while reading the file.
     */
    @Test
    void testNext() throws InvalidPathException, IOException {
        Reader testReader = new Reader("src/test/resources/test.dbm");

        String firstEntry = """
                000:00001
                030:a
                040:2000
                050:d
                100:Musterartikel : Musterzusatz
                200:Mustermann, M.
                540:Musterobjekt
                550:Musterstadt|Musterland|D
                700:Musterzeitschrift. 1(2000) H.1, S.1-10
                &&&
                """;
        assertEquals(firstEntry, testReader.next());

        String secondEntry = """
                000:00002
                030:m
                040:2000
                050:d
                100:Mustermonographie : Musterzusatz
                200:Musterfrau, E.
                540:Musterobjekt
                550:Musterstadt|Musterland|D
                &&&
                """;
        assertEquals(secondEntry, testReader.next());

        assertThrows(NoSuchElementException.class, () -> {
            testReader.next();
        });
    }
}
