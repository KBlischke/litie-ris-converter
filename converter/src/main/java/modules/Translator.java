package modules;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * The {@code Translator} class extends the {@link abstractions.Entry} class
 * and represents a translator for data using a parser object.
 */
public class Translator extends abstractions.Entry {

    /**
     * Constructor for the {@link Translator} class.
     *
     * @param parser The {@link Parser} object used to extract and process data.
     */
    public Translator(Parser parser) {
        for (String category : parser.getCategories()) {
            String newCategory = getCategory(category);

            switch (category) {
                case "030":
                    handleCategory030(parser);
                    break;
                case "100":
                    handleCategory100(parser);
                    break;
                case "700":
                    handleCategory700(parser);
                    break;
                case "860":
                    handleCategory860(parser);
                    break;
                case "875":
                    handleCategory875(parser);
                    break;
                case "880":
                    handleCategory880(parser);
                    break;
                case "50C", "50L", "50R", "53A",
                "60B", "60A", "60D", "60E", "60F", "60G", "60K", "60L", "60S", "60R":
                    handleNotes(newCategory, category, parser);
                    break;
                case "9ZE":
                    break;
                default:
                    handleDefaultCategory(newCategory, category, parser);
            }
        }
    }

    /**
     * Handles the processing of data with the field {@code 030}.
     *
     * @param parser The {@link Parser} object containing the data to be processed.
     */
    private void handleCategory030(Parser parser) {
        String referenceType = "";
        String field030Value = parser.getField("030")[0];

        switch (field030Value) {
            case "a":
                if (parser.hasField("700") && parser.hasField("860")) {
                    referenceType = "CHAP";
                }
                else {
                    referenceType = "JOUR";
                }
                break;
            case "ag":
                referenceType = "JOUR";
                break;
            case "au":
                referenceType = "CONF";
                break;
            case "d", "x":
                referenceType = "THES";
                break;
            case "el":
                referenceType = "EBOOK";
                break;
            case "fi":
                referenceType = "ADVS";
                break;
            case "b", "h", "i", "l", "m", "ms", "s":
                referenceType = "BOOK";
                break;
            case "n":
                referenceType = "STAT";
                break;
            case "p":
                referenceType = "JOUR";
                break;
            case "pat":
                referenceType = "PAT";
                break;
            case "r":
                referenceType = "RPRT";
                break;
            case "u":
                referenceType = "SLIDE";
                break;
            case "v", "vi":
                referenceType = "VIDEO";
                break;
            case "z":
                referenceType = "JFULL";
                break;
            case "?":
                referenceType = "GEN";
        }

        this.fields.put("TY", new String[]{referenceType});
        this.categories.add("TY");
    }

    /**
     * Handles the processing of data with the field {@code 100}.
     *
     * @param parser The {@link Parser} object containing the data to be processed.
     */
    private void handleCategory100(Parser parser) {
        String[] field100 = parser.getField("100");

        if (
            parser.hasField("030") && parser.getField("030")[0].equals("?") &&
            field100.length == 2
        ) {
            this.fields.put("TI", new String[]{field100[0]});
            this.categories.add("TI");
            this.fields.put("T2", new String[]{field100[1]});
            this.categories.add("T2");
        }
        else if (field100.length == 2) {
            this.fields.put("TI", new String[]{
                String.format("%s: %s", field100[0], field100[1])
            });
            this.categories.add("TI");
        }
        else if (field100.length == 3) {
            this.fields.put("TI", new String[]{
                String.format("%s: %s: %s", field100[0], field100[1], field100[2])
            });
            this.categories.add("TI");
        }
        else {
            this.fields.put("TI", new String[]{field100[0]});
            this.categories.add("TI");
        }
    }

    /**
     * Handles the processing of data with the field {@code 700}.
     *
     * @param parser The {@link Parser} object containing the data to be processed.
     */
    private void handleCategory700(Parser parser) {
        if (!Pattern.compile("https?://").matcher(parser.getField("700")[0]).find()) {
            this.fields.put("T2", new String[]{parser.getField("700")[0]});
            this.categories.add("T2");
        }
        for (int i = 0; i < parser.getField("700").length; i++) {
            String content = parser.getField("700")[i];
            if (Pattern.compile("^(?:Vol\\.)?\\d+(?:\\([\\d/]+\\))?$").matcher(content).find()) {
                this.fields.put("VL", new String[]{content.replace("Vol.", "")});
                this.categories.add("VL");
            }
            else if (Pattern.compile("^(?:H|nos?)\\.[x\\?\\d/]+$").matcher(content).find()) {
                content = content.replace("H.", "").replace("no.", "").replace("nos.", "");
                this.fields.put("IS", new String[]{content});
                this.categories.add("IS");
            }
            else if (
                Pattern.compile(
                    "(?:\\s*((?:\\d|\\?|x|\\-)+\\.(?:\\d|\\?|x|\\-)+\\.(?:\\d|\\?|x|\\-)+)\\s*)$"
                    ).matcher(content).find()
            ) {
                this.fields.put("DA", new String[]{content});
                this.categories.add("DA");
            }
            else if (Pattern.compile("^\\[?https?://").matcher(content).find()) {
                if (content.startsWith("[")) {
                    content = content.replace("[", "").replace("]", "");
                }
                if (this.hasField("UR")) {
                    this.appendField("UR", content);
                }
                else {
                    this.fields.put("UR", new String[]{content});
                    this.categories.add("UR");
                }
            }
        }
    }

    /**
     * Handles the processing of data with the field {@code 860}.
     *
     * @param parser The {@link Parser} object containing the data to be processed.
     */
    private void handleCategory860(Parser parser) {
        if (Pattern.compile("\\d+\\.\\d+\\.\\d+").matcher(parser.getField("860")[0]).find()) {
            this.fields.put("DA", new String[]{parser.getField("860")[0]});
            this.categories.add("DA");
        }
        else if (parser.getField("860").length == 2) {
            this.fields.put("CY", parser.getField("860")[0].split(";"));
            this.categories.add("CY");
            this.fields.put("PB", new String[]{parser.getField("860")[1]});
            this.categories.add("PB");
        }
        else {
            this.fields.put("CY", parser.getField("860")[0].split(";"));
            this.categories.add("CY");
        }
    }

    /**
     * Handles the processing of data with the field {@code 875}.
     *
     * @param parser The {@link Parser} object containing the data to be processed.
     */
    private void handleCategory875(Parser parser) {
        if (Pattern.compile("https?://").matcher(parser.getField("875")[0]).find()) {
            Pattern pattern = Pattern.compile("^.*(https?://.+)$");
            Matcher match = pattern.matcher(parser.getField("875")[0]);
            match.find();

            String url = match.group(1);
            if (url.endsWith(".")) {
                url = url.substring(0, url.length() - 1);
            }

            if (this.hasField("UR")) {
                this.appendField("UR", url);
            }
            else {
                this.fields.put("UR", new String[]{url});
                this.categories.add("UR");
            }
        }
        else {
            handleNotes("N1", "875", parser);
        }
    }

    /**
     * Handles the processing of data with the field {@code 880}.
     *
     * @param parser The {@link Parser} object containing the data to be processed.
     */
    private void handleCategory880(Parser parser) {
        if (Pattern.compile("https?://").matcher(parser.getField("880")[0]).find()) {
            Pattern pattern = Pattern.compile("^.*(https?://.+)$");
            Matcher match = pattern.matcher(parser.getField("880")[0]);
            match.find();

            String url = match.group(1);
            if (url.endsWith(".")) {
                url = url.substring(0, url.length() - 1);
            }

            if (this.hasField("UR")) {
                this.appendField("UR", url);
            }
            else {
                this.fields.put("UR", new String[]{url});
                this.categories.add("UR");
            }
        }
        else {
            handleNotes("N1", "880", parser);
        }
    }

    /**
     * Handles the processing of data for special categories.
     *
     * @param newCategory The new category to be assigned.
     * @param category    The original category being processed.
     * @param parser      The {@link Parser} object containing the data to be processed.
     */
    private void handleNotes(String newCategory, String category, Parser parser) {
        if (this.hasField(newCategory)) {
            this.fields.put(
                newCategory,
                mergeArray(
                    this.getField(newCategory),
                    new String[]{addPrefix(category, parser.getField(category)[0])}
                )
            );
        }
        else {
            this.fields.put(
                newCategory,
                new String[]{addPrefix(category, parser.getField(category)[0])}
            );
            this.categories.add(newCategory);
        }
    }

    /**
     * Handles the processing of data for default categories.
     *
     * @param newCategory The new category to be assigned.
     * @param category    The original category being processed.
     * @param parser      The {@link Parser} object containing the data to be processed.
     */
    private void handleDefaultCategory(String newCategory, String category, Parser parser) {
        if (category == "9ZE") {
            return;
        }
        else if (this.hasField(newCategory)) {
            this.fields.put(
                newCategory,
                mergeArray(this.getField(newCategory), parser.getField(category))
            );
        }
        else {
            this.fields.put(newCategory, parser.getField(category));
            this.categories.add(newCategory);
        }
    }

    /**
     * Retrieves the appropriate new category based on the original category.
     *
     * @param category The original category for which a new category is needed.
     * @return         The new category corresponding to the original category.
     */
    private String getCategory(String category) {
        switch (category) {
            case "000":
                return "CN";
            case "040":
                return "PY";
            case "050":
                return "LA";
            case "08I":
                return "SN";
            case "200":
                return "AU";
            case "21E":
                return "A2";
            case "411":
                return "T3";
            case "810":
                return "SP";
            case "855":
                return "ET";
            case "870":
                return "AB";
            case "890":
                return "L1";
            case "892":
                return "L4";
            case "900":
                return "AN";
            case "270", "510", "520", "530", "540", "550", "590":
                return "KW";
            case "875", "880", "50C", "50L", "50R", "53A",
            "60B", "60A", "60D", "60E", "60F", "60G", "60K", "60L", "60S", "60R":
                return "N1";
            case "9ZC":
                return "Y2";
            default:
                return "N1";
        }
    }

    /**
     * Adds a prefix to the content based on the category.
     *
     * @param category The category for which the prefix is added.
     * @param content  The content to which the prefix is added.
     * @return         The content with the added prefix.
     */
    private String addPrefix(String category, String content) {
        String prefix = "";
        switch (category) {
            case "875":
                prefix = "Inhalt";
                break;
            case "880":
                prefix = "Fußnote";
                break;
            case "50C":
                prefix = "Compass";
                break;
            case "50L":
                prefix = "LCSH";
                break;
            case "50P":
                prefix = "Precis";
                break;
            case "50R":
                prefix = "RSWK";
                break;
            case "53A":
                prefix = "Hilfsmittel";
                break;
            case "60B":
                prefix = "BK";
                break;
            case "60A":
                prefix = "ASB";
                break;
            case "60D":
                prefix = "DDC";
                break;
            case "60E":
                prefix = "Eppelsheimer";
                break;
            case "60F":
                prefix = "SFB";
                break;
            case "60G":
                prefix = "GHBS";
                break;
            case "60K":
                prefix = "KAB";
                break;
            case "60L":
                prefix = "LCC";
                break;
            case "60R":
                prefix = "RVK";
                break;
        }
        return String.format("%s: %s", prefix, content);
    }

    /**
     * Merges two arrays into a single array.
     *
     * @param array1 The first array to be merged.
     * @param array2 The second array to be merged.
     * @return       The merged array containing elements from both input arrays.
     */
    private String[] mergeArray(String[] array1, String[] array2) {
        String[] newArray = new String[array1.length + array2.length];

        for (int i = 0; i < array1.length; i++) {
            newArray[i] = array1[i];
        }

        for (int j = 0; j < array2.length; j++) {
            newArray[array1.length + j] = array2[j];
        }

        return newArray;
    }
}
