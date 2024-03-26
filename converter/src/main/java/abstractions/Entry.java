package abstractions;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * The abstract class {@code Entry} represents an entry with categories and fields.
 * The class provides methods to retrieve categories and fields.
 */
public abstract class Entry {

    /** The list of categories for this entry. */
    protected ArrayList<String> categories = new ArrayList<>();

    /** The mapping of categories to fields. */
    protected HashMap<String, String[]> fields = new HashMap<>();

    /**
     * Returns an array with the categories of this entry.
     *
     * @return A string array containing the categories.
     */
    public String[] getCategories() {
        return categories.toArray(new String[0]);
    }

    /**
     * Checks if a specific field is present in a category.
     *
     * @param category The category to check.
     * @return         {@code true} if the field is present, otherwise {@code false}.
     */
    public boolean hasField(String category) {
        return fields.containsKey(category);
    }

    /**
     * Returns the fields of a specific category.
     *
     * @param category                   The category to retrieve fields for.
     * @return                           A string array with the fields of the specified category.
     * @throws IndexOutOfBoundsException If the specified category is not present.
     */
    public String[] getField(String category) throws IndexOutOfBoundsException {
        if (fields.containsKey(category)) {
            return fields.get(category);
        }
        else {
            throw new IndexOutOfBoundsException();
        }
    }

    /**
     * Appends a fields to a specific category.
     *
     * @param category                   The category to retrieve fields for.
     * @throws IndexOutOfBoundsException If the specified category is not present.
     */
    public void appendField(String category, String field) throws IndexOutOfBoundsException {
        if (fields.containsKey(category)) {
            int arrayLength = this.getField(category).length;
            String[] newArray = new String[arrayLength + 1];
            for (int i = 0; i < arrayLength; i++) {
                newArray[i] = this.getField(category)[i];
            }
            newArray[arrayLength] = field;
            this.fields.replace(category, newArray);
        }
        else {
            throw new IndexOutOfBoundsException();
        }
    }
}
