package modules;

/**
 * The {@link Assembler} class is responsible for assembling information from a Translator entry.
 */
public class Assembler {

    private Assembler() {}

    /**
     * Assembles information from a Translator entry.
     *
     * @param entry The {@link Translator} object containing the information to be assembled.
     * @return      The assembled information as a formatted string.
     */
    public static String assemble(Translator entry) {
        StringBuilder assembledEntry = new StringBuilder();

        if (entry.hasField("TY")) {
            assembledEntry.append(String.format("TY - %s\n", entry.getField("TY")[0]));
        }
        else {
            assembledEntry.append("TY - GEN\n");
        }

        for (String category : entry.getCategories()) {
            if (category.equals("TY")) {
                continue;
            }

            String[] content = entry.getField(category);

            for (String value : content) {
                assembledEntry.append(String.format("%s - %s\n", category, value));
            }
        }

        assembledEntry.append("ER -\n\n");
        return assembledEntry.toString();
    }
}
