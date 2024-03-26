package modules;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * The {@code Unifier} class provides methods for unifying and formatting DBM entries.
 */
public class Unifier {

    /**
     * Unifies and formats the given DBM entry.
     *
     * @param entry The DBM entry to be unified.
     * @return      The unified and formatted DBM entry.
     */
    public static String unify(String entry) {
        String[] lines = entry.split("\n");
        StringBuilder unifiedEntry = new StringBuilder();

        for (String line : lines) {
            if (Pattern.compile("^\\s*\\w{3}\\s*:\\s*.+$").matcher(line).find()) {
                line = unifyAll(line);
                String field = getField(line);
                line = unifyField(line, field);
                unifiedEntry.append(line).append("\n");
            }
        }

        return unifiedEntry.toString();
    }

    /**
     * Unifies a line by trimming leading and trailing spaces, replacing colon spacing
     * and removing not-signs.
     *
     * @param line The input line to be unified.
     * @return     The unified line.
     */
    private static String unifyAll(String line) {
        line = line.trim();
        line = line.replaceFirst("\\s*:\\s*", ": ");
        line = line.replaceAll("\u00AD", "");
        line = line.replaceAll("¬", "");
        return line;
    }

    /**
     * Unifies a line based on its field name.
     *
     * @param line  The input line to be unified.
     * @param field The field to determine the unification method.
     * @return      The unified line.
     */
    private static String unifyField(String line, String field) {
        switch (field) {
            case "030":
                return unify030(line);
            case "100":
                return unify100(line);
            case "200", "270", "510", "520", "530", "540", "590":
                return unifyPipeDelimiter(line);
            case "21E":
                return unify21E(line);
            case "411":
                return unify411(line);
            case "700":
                return unify700(line);
            default:
                return line;
        }
    }

    /**
     * Unifies a line with field {@code 030} by replacing {@code oder} and
     * {@code und} with {@code |} and removing spaces around pipes,
     * converting to lowercase, and replacing content
     * with the last word.
     *
     * @param line The input line to be unified.
     * @return     The unified line.
     */
    private static String unify030(String line) {
        line = replaceSpecialCase(line);
        line = line.replaceAll("\\s*\\|\\s*", "|");
        line = line.toLowerCase();
        String suffix = line.split(" ")[line.split(" ").length - 1];
        return replaceContent(line, suffix);
    }

    /**
     * Unifies a line with field {@code 100} by replacing
     * colons with and without spaces around them by {@code :}.
     *
     * @param line The input line to be unified.
     * @return     The unified line.
     */
    private static String unify100(String line) {
        String content = getContent(line);
        if (Pattern.compile("https?:").matcher(line).find()) {
            content = content.replaceAll("\\s*:\\s*", ":");
        }
        else {
            content = content.replaceAll("\\s*:\\s*", " : ");
        }
        return replaceContent(line, content);
    }

    /**
     * Unifies a line with field {@code 21E} by replacing
     * semicolons and pipes with and without spaces around them by {@code ;}.
     *
     * @param line The input line to be unified.
     * @return     The unified line.
     */
    private static String unify21E(String line) {
        line = line.replaceAll("\\s*[;|]\\s*", ";");
        return line;
    }

    /**
     * Unifies a line with field {@code 411} by replacing
     * semicolons with and without spaces around them by {@code ;}.
     *
     * @param line The input line to be unified.
     * @return     The unified line.
     */
    private static String unify411(String line) {
        line = line.replaceAll("\\s*;\\s*", " ; ");
        return line;
    }

    /**
     * Unifies a line with field {@code 700} by extracting
     * pages information and adding it to field {@code 810}.
     *
     * @param line The input line to be unified.
     * @return     The unified line.
     */
    private static String unify700(String line) {
        String pages = extractPagerange(line);
        if (!pages.isEmpty()) {
            line = line.replace(pages, " ").trim();
            pages = pages.replace(" ", "");
            line = line.concat("\n810: ").concat(pages);
        }
        return line;
    }

    /**
     * Unifies a line with pipe delimiters by removing spaces around pipes.
     *
     * @param line The input line to be unified.
     * @return     The unified line.
     */
    private static String unifyPipeDelimiter(String line) {
        return line.replaceAll("\\s*\\|\\s*", "|");
    }

    /**
     * Retrieves the field name from a line.
     *
     * @param line The input line.
     * @return     The field.
     */
    private static String getField(String line) {
        Matcher match = Pattern.compile("^\\s*(\\w{3})\\s*:\\s*.+$").matcher(line);
        match.find();
        return match.group(1);
    }

    /**
     * Retrieves the content from a line.
     *
     * @param line The input line.
     * @return     The content.
     */
    private static String getContent(String line) {
        Matcher match = Pattern.compile("^\\s*\\w{3}\\s*:\\s*(.+)$").matcher(line);
        match.find();
        return match.group(1);
    }

    /**
     * Replaces the content of a line with new content.
     *
     * @param line       The input line.
     * @param newContent The new content to replace.
     * @return           The line with replaced content.
     */
    private static String replaceContent(String line, String newContent) {
        String content = getContent(line);
        return line.replace(content, newContent);
    }

    /**
     * Replaces special cases with expected input in a line with field {@code 030}.
     *
     * @param line The input line.
     * @return     The line with replaced values.
     */
    private static String replaceSpecialCase(String line) {
        line = line.replace(
            "Biographische Beiträge s. Systemstelle: Biographische Darstellungen",
            "m"
        );
        line = line.replace(" oder ", "|").replace(" und ", "|");
        return line;
    }

    /**
     * Extracts pageranges from a line.
     *
     * @param line The input line.
     * @return     The extracted pages information.
     */
    private static String extractPagerange(String line) {
        String regex1 = "(?:\\s*S\\.\\s*(?:\\d|x|\\?|\\*|\\s*)+(?:\\s*(?:\\-|\\+|,)\\s*(?:\\d|x|\\?|\\*|\\s*)+)?)";
        String regex2 = "(?:\\s*(?:\\d|x|\\?|\\*|\\s*)+(?:\\s*(?:\\-|\\+)\\s*(?:\\d|x|\\?|\\*|\\s*)+)\\s*S(?!\\w))";
        Pattern pattern = Pattern.compile(String.format("(%s|%s)", regex1, regex2));
        Matcher match = pattern.matcher(line);
        return match.find() ? match.group(1) : "";
    }
}
