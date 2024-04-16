package modules;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Paths;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * The {@code Reader} class is responsible for reading and processing content
 * from a DBM file in Allegro format.
 * It implements the Iterator interface to iterate through sections delimited by specific markers.
 */
public class Reader implements Iterator<String> {

    /** The path of the file. */
    private String path;

    /** The content of the file as a list of strings. */
    private List<String> file;

    /** The total number of lines in the file. */
    private int lines;

    /** The current position while iterating through the file. */
    private int position;

    /**
     * Constructs a {@link Reader} object with the specified file path.
     *
     * @param input                 The path of the file to be read.
     * @throws InvalidPathException If the provided file path is invalid.
     * @throws IOException          If an I/O error occurs while reading the file.
     */
    public Reader(String input) throws InvalidPathException, IOException {
        this.path = input;
        this.file = Files.readAllLines(Paths.get(input), Charset.forName("ISO-8859-1"));
        this.lines = this.file.size();
        this.position = 0;
    }

    /**
     * Gets the path of the file being read.
     *
     * @return The file path.
     */
    public String getPath() {
        return this.path;
    }

    /**
     * Gets the total number of lines in the file.
     *
     * @return The number of lines in the file.
     */
    public int getSize() {
        return this.lines;
    }

    /**
     * Gets the current position while iterating through the file.
     *
     * @return The current position.
     */
    public int getPosition() {
        return this.position;
    }

    /**
     * Sets the current position to the specified index.
     *
     * @param index                      The index to set the position to.
     * @throws IndexOutOfBoundsException If the index is out of bounds.
     */
    public void setPosition(int index) throws IndexOutOfBoundsException {
        if (index > this.lines - 1 || index < 0) {
            throw new IndexOutOfBoundsException();
        }
        this.position = index;
    }

    /**
     * Finds the index of the next section start marked by {@code 000:}.
     *
     * @return The index of the next section start or {@code -1} if not found.
     */
    private int getNextStart() {
        for (int i = this.position; i < this.lines; ++i) {
            if (this.file.get(i).strip().startsWith("000:")) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Finds the index of the next section end marked by {@code &&&}.
     *
     * @return The index of the next section end or {@code -1} if not found.
     */
    private int getNextEnd() {
        for (int i = this.position + 1; i < this.lines; ++i) {
            if (this.file.get(i).strip().equals("&&&")) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Checks if there is a next section in the file.
     *
     * @return {@code true} if there is a next section, {@code false} otherwise.
     */
    @Override
    public boolean hasNext() {
        int nextStart = this.getNextStart();
        int nextEnd = this.getNextEnd();

        return nextStart >= 0 && nextEnd >= 0 && nextStart < nextEnd;
    }

    /**
     * Retrieves the next section from the file.
     *
     * @return                        The content of the next section.
     * @throws NoSuchElementException If there is no next section.
     */
    @Override
    public String next() throws NoSuchElementException {
        if (!this.hasNext()) {
            throw new NoSuchElementException();
        } else {
            int nextStart = this.getNextStart();
            int nextEnd = this.getNextEnd();
            StringBuilder entry = new StringBuilder();

            for (int i = nextStart; i <= nextEnd; ++i) {
                entry.append(String.format("%s\n", this.file.get(i).strip()));
                this.position = i;
            }
            return entry.toString();
        }
    }
}
