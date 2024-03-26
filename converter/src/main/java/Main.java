import java.io.IOException;
import java.nio.file.InvalidPathException;

import modules.Assembler;
import modules.InputHandler;
import modules.Parser;
import modules.Reader;
import modules.Translator;
import modules.Unifier;
import modules.Validator;
import modules.Writer;

/**
 * The {@code Main} class serves as the entry point for the RIS data processing application.
 * It reads data from a specified source, processes it, and writes the result to a destination.
 */
public class Main {

    /**
     * The entry point of the application.
     *
     * @param args Command line arguments, including source and destination file paths.
     */
    public static void main(String[] args) {
        InputHandler input;
        Reader reader;
        Writer writer;

        try {
            input = new InputHandler(args);
        }
        catch (IllegalArgumentException e) {
            handleException(e);
            return;
        }

        try {
            reader = new Reader(input.getSource());
        }
        catch (InvalidPathException | IOException e) {
            handleException(e);
            return;
        }

        try {
            writer = new Writer(input.getDest());
        }
        catch (IOException e) {
            handleException(e);
            return;
        }

        processEntries(reader, writer);
    }

    /**
     * Handles exceptions by printing the stack trace.
     *
     * @param e The exception to handle.
     */
    private static void handleException(Exception e) {
        e.printStackTrace();
    }

    /**
     * Processes entries from the reader, unifies, translates, assembles,
     * and writes them using the writer, if they pass the validator.
     *
     * @param reader The {@link Reader} instance to read entries from.
     * @param writer The {@link Writer} instance to write processed entries to.
     */
    private static void processEntries(Reader reader, Writer writer) {
        while (reader.hasNext()) {
            String entry = reader.next();
            Validator validator = new Validator(entry);
            if (!validator.isValid()) {
                continue;
            }

            String unifiedEntry = Unifier.unify(entry);
            Parser parsedEntry = new Parser(unifiedEntry);
            Translator translatedEntry = new Translator(parsedEntry);
            String newEntry = Assembler.assemble(translatedEntry);

            try {
                writer.append(newEntry);
            } catch (IOException e) {
                handleException(e);
            }
        }
    }
}
