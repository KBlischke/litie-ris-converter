package modules;

/**
 * The {@code InputHandler} class is responsible for processing input parameters.
 * It analyzes the input parameters and extracts source and destination information.
 */
public class InputHandler {
    /** The number of provided command-line arguments. */
    private int argSize;

    /** The path to the source file. */
    private String source;

    /** The path to the destination file. */
    private String dest;

    /**
     * Constructs a new instance of {@link InputHandler} and analyzes the provided arguments.
     *
     * @param args                      The input parameters to be analyzed.
     * @throws IllegalArgumentException If the provided arguments are invalid.
     */
    public InputHandler(String[] args) throws IllegalArgumentException {
        this.argSize = args.length;

        if (this.checkArgs(args)) {
            source = args[0];
            if (this.argSize == 2) {
                dest = args[1];
            }
            else {
                dest = new String(args[0]).replaceAll("\\.[dD][bB][mM]$", ".ris");
            }
        }
        else {
            throw new IllegalArgumentException();
        }
    }

    /**
     * Returns the number of input parameters.
     *
     * @return The number of input parameters.
     */
    public int getArgSize() {
        return this.argSize;
    }

    /**
     * Returns the path to the source file.
     *
     * @return The path to the source file.
     */
    public String getSource() {
        return this.source;
    }

    /**
     * Returns the path to the destination file.
     *
     * @return The path to the destination file.
     */
    public String getDest() {
        return this.dest;
    }

    /**
     * Checks the validity of the provided arguments.
     *
     * @param args The arguments to be checked.
     * @return     True if the arguments are valid, otherwise False.
     */
    private boolean checkArgs(String[] args) {
        boolean notEmpty = this.argSize > 0;
        boolean notOversized = this.argSize <= 2;
        boolean isDBM = args[0].toLowerCase().endsWith(".dbm");
        boolean isRIS = (
            this.argSize == 2 && !args[1].toLowerCase().endsWith(".ris")
        ) ? false : true;

        // Returning the validity of the arguments
        return notEmpty && notOversized && isDBM && isRIS;
    }
}
