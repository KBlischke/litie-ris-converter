package modules;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * The {@code Parser} class extends the {@link abstractions.Entry} class
 * and represents a parser for processing entry data.
 */
public class Parser extends abstractions.Entry {

    /**
     * Constructor for the {@link Parser} class.
     *
     * @param entry The entry data to be parsed.
     */
    public Parser(String entry) {
        String[] lines = entry.split("\n");

        String field;
        for (int i = 0; i < lines.length; i++) {
            if (Pattern.compile("^\\w{3}: .+$").matcher(lines[i]).find()) {
                field = lines[i].substring(0, 3);
            } else {
                continue;
            }

            switch (field) {
                case "000", "040":
                    fields.put(field, parseField(lines[i], ""));
                    break;
                case "030", "050", "08I", "200", "270", "510", "520", "530",
                "540", "550", "590", "890", "900":
                    fields.put(field, parseField(lines[i], "\\|"));
                    break;
                case "100":
                    if (Pattern.compile("https?://").matcher(lines[i]).find()) {
                        fields.put(field, parseField(lines[i], ""));
                    }
                    else {
                        fields.put(field, parseField(lines[i], ":"));
                    }
                    break;
                case "21E", "855":
                    fields.put(field, parseField(lines[i], ";"));
                    break;
                case "700":
                    fields.put(field, parseField700(lines[i]));
                    break;
                case "860":
                    fields.put(field, parseField860(lines[i]));
                    break;
                default:
                    fields.put(field, parseField(lines[i], ""));
            }
            this.categories.add(field);
        }
    }

    /**
     * Parses a field from a line with a specified delimiter.
     *
     * @param line                   The line containing the field.
     * @param delimiter              The delimiter used to split the field.
     * @return                       An array of parsed field values.
     * @throws IllegalStateException If the parsing encounters an issue.
     */
    private String[] parseField(String line, String delimiter) throws IllegalStateException {
        Pattern pattern = Pattern.compile("^\\w{3}: (.+)$");
        Matcher match = pattern.matcher(line);
        match.find();
        String content = match.group(1);

        String[] parts = new String[]{content};
        if (!delimiter.isEmpty() && Pattern.compile(delimiter).matcher(content).find()) {
            parts = content.split(delimiter);
            for (int i = 0; i < parts.length; i++) {
                parts[i] = parts[i].trim();
            }
        }

        return parts;
    }

    /**
     * Parses a line with field {@code 700}.
     *
     * @param line                   The line containing the field.
     * @return                       An array of parsed field values.
     * @throws IllegalStateException If the parsing encounters an issue.
     */
    private String[] parseField700(String line) throws IllegalStateException {
        Pattern pattern = Pattern.compile("^\\w{3}: (.+)$");
        Matcher match = pattern.matcher(line);
        match.find();
        String content = match.group(1);

        String title = "([\\wÄäÖöÜüÆæØøÅåÁáÀàÂâÉéÈèÊêÍíÌìÎîÏïÓóÒòÔôŒœÚúÙùÛûÃãŸÿÇçÑñßÞþ,:;¨!\\?\\-_\\+#@&%»«=\\$/¬\\'\\\"\\(\\)\\[\\]<>\\s*]+)\\.";
        String empty = "(?:\\s*,)?";
        String volume = "(?:\\s*(?:Vol\\.|Nr\\.|Bd\\.)?([\\d\\.\\(\\)/\\s*]+),?\\s*)?";
        String date = "(?:\\s*(?:\\w+)\\s*((?:\\d|\\?|x|\\-)+\\.(?:\\d|\\?|x|\\-)+\\.(?:\\d|\\?|x|\\-)+)\\s*)?,?";
        String issue = "(?:\\s*((?:H|nos?)\\.[x\\?\\d/]+),\\s*)?";
        String url = "(?:(?:\\w+)?\\s*\\[?(https?://[\\w\\.,:;!\\?\\-_\\+#@&~=\\$/\\(\\)<>\\s*]+)\\]?)?";
        String singleTitle = "([\\wÄäÖöÜüÆæØøÅåÁáÀàÂâÉéÈèÊêÍíÌìÎîÏïÓóÒòÔôŒœÚúÙùÛûÃãŸÿÇçÑñßÞþ\\.,:;¨~!\\?\\-_\\+#@&%»«=\\$/¬\\'\\\"\\(\\)\\[\\]<>\\s*]+)";
        pattern = Pattern.compile(String.format(
                "^(?:(?:%s%s%s%s%s%s)|%s)$",
                title, empty, volume, date, issue, url, singleTitle
        ));

        match = pattern.matcher(content);
        match.find();
        String[] matches = new String[]{
            match.group(1),
            match.group(2),
            match.group(3),
            match.group(4),
            match.group(5),
            match.group(6)
        };

        ArrayList<String> foundMatches = new ArrayList<>();
        for (int i = 0; i < matches.length; i++) {
            if (matches[i] != null && !Pattern.compile("^\\s*$").matcher(matches[i]).find()) {
                foundMatches.add(matches[i].trim());
            }
        }

        String[] parts = new String[foundMatches.size()];
        parts = foundMatches.toArray(parts);

        return parts;
    }

    /**
     * Parses a line with field {@code 860}.
     *
     * @param line                   The line containing the field.
     * @return                       An array of parsed field values.
     * @throws IllegalStateException If the parsing encounters an issue.
     */
    private String[] parseField860(String line) throws IllegalStateException {
        line = line.replaceAll("\\s*;\\s*", ";");

        Pattern pattern = Pattern.compile("^\\w{3}: (.+)$");
        Matcher match = pattern.matcher(line);
        match.find();
        String content = match.group(1);

        String place = "(?:([\\wÄäÖöÜüÆæØøÅåÁáÀàÂâÉéÈèÊêÍíÌìÎîÏïÓóÒòÔôŒœÚúÙùÛûÃãŸÿÇçÑñßÞþ\\.,;¨\\?/\\(\\)\\[\\]\\- ]+)\\s*)";
        String empty = "(?:\\s*[,: ]+\\s*)?";
        String publisher = "(?:\\s*([\\wÄäÖöÜüÆæØøÅåÁáÀàÂâÉéÈèÊêÍíÌìÎîÏïÓóÒòÔôŒœÚúÙùÛûÃãŸÿÇçÑñßÞþ\\.,:;¨\\?!/\\-&%\\+\\'\\\"\\(\\)\\[\\] ]+)\\s*)?";
        pattern = Pattern.compile(String.format(
            "^%s%s%s$", place, empty, publisher
        ));

        match = pattern.matcher(content);
        match.find();
        String[] matches = new String[]{
            match.group(1),
            match.group(2)
        };

        ArrayList<String> foundMatches = new ArrayList<>();
        for (int i = 0; i < matches.length; i++) {
            if (matches[i] != null && !Pattern.compile("^\\s*$").matcher(matches[i]).find()) {
                foundMatches.add(matches[i].trim());
            }
        }

        String[] parts = new String[foundMatches.size()];
        parts = foundMatches.toArray(parts);

        return parts;
    }
}
