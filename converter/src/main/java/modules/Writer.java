package modules;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

/**
 * The {@code Writer} class provides methods for creating and writing to a file.
 */
public class Writer {
    private Path path;

    /**
     * Constructs a new {@link Writer} object with the specified file path.
     * If the file already exists, it is cleared.
     *
     * @param filePath     The path to the file.
     * @throws IOException If an I/O error occurs while creating or accessing the file.
     */
    public Writer(String filePath) throws IOException {
        this.path = Paths.get(filePath);

        if (Files.exists(path)) {
            Files.write(path, "".getBytes(StandardCharsets.UTF_8));
        } else {
            Files.createFile(path);
        }
    }

    /**
     * Appends the specified entry to the file.
     *
     * @param entry        The content to append to the file.
     * @throws IOException If an I/O error occurs while writing to the file.
     */
    public void append(String entry) throws IOException {
        Files.write(path, entry.getBytes(StandardCharsets.UTF_8), StandardOpenOption.APPEND);
    }
}
