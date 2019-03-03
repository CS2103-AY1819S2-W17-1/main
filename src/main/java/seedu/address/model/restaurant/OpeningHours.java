package seedu.address.model.restaurant;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.AppUtil.checkArgument;

/**
 * Represents a Restaurant's opening hours in the food diary.
 * Guarantees: immutable; is valid as declared in {@link #isValidOpeningHour(String)}
 */
public class OpeningHours {
    public static final String MESSAGE_CONSTRAINTS = "Opening hours should be of the format 'HHMM to HHMM' "
            + "for example, 1000 to 2200";
    // alphanumeric and special characters
    private static final String HOURS = "(0[0-9]|1[0-9]|2[0-3])[0-5][0-9]";
    public static final String VALIDATION_REGEX = HOURS + " to " + HOURS;

    public final String value;

    /**
     * Constructs an {@code OpeningHours}.
     *
     * @param openingHours A opening hour.
     */
    public OpeningHours(String openingHours) {
        requireNonNull(openingHours);
        checkArgument(isValidOpeningHour(openingHours), MESSAGE_CONSTRAINTS);
        value = openingHours;
    }

    /**
     * Returns if a given string is a valid email.
     */
    public static boolean isValidOpeningHour(String test) {
        return test.matches(VALIDATION_REGEX);
    }

    public static OpeningHours makeDefaultOpening() {
        return new OpeningHours("No opening hours added");
    }

    @Override
    public String toString() {
        return value;
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof OpeningHours // instanceof handles nulls
                && value.equals(((OpeningHours) other).value)); // state check
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

}
