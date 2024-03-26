package modules;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * {@code Validator} class to validate a given entry.
 */
public class Validator {
    private final String entry;
    private static final Pattern FIELD_030_PATTERN = Pattern.compile(
        "^\\s*030\\s*:(.+)$", Pattern.MULTILINE
    );
    private static final Pattern REFERENCE_PATTERN = Pattern.compile("(?:siehe|s\\.)");

    /**
     * Constructor for the {@link Validator} class.
     *
     * @param entry The entry to be validated.
     */
    public Validator(String entry) {
        this.entry = entry;
    }

    /**
     * Checks if the entry is valid.
     *
     * @return {@code true} if the entry has field {@code 030} and does not have a reference,
     * {@code false} otherwise.
     */
    public boolean isValid() {
        return hasField030() && !hasReference();
    }

    /**
     * Checks if the entry has field {@code 030}.
     *
     * @return {@code true} if the entry has field {@code 030}, {@code false} otherwise.
     */
    private boolean hasField030() {
        return FIELD_030_PATTERN.matcher(entry).find();
    }

    /**
     * Checks if the entry has a reference.
     *
     * @return {@code true} if the entry has a reference, {@code false} otherwise.
     */
    private boolean hasReference() {
        Matcher match = FIELD_030_PATTERN.matcher(entry);
        if (match.find()) {
            return REFERENCE_PATTERN.matcher(match.group(1)).find();
        }
        return false;
    }
}
