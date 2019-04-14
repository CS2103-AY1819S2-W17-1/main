package seedu.address.model.restaurant;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.AppUtil.checkArgument;

/**
 * Represents a Restaurant's phone number in the food diary.
 * Guarantees: immutable; is valid as declared in {@link #isValidPhone(String)}
 */
public class Phone {


    public static final String MESSAGE_CONSTRAINTS =
            "Phone numbers should only contain numbers, and it should be at least 3 digits long";
    public static final String VALIDATION_REGEX = "\\d{3,}";
    private static final String PHONE_PLACEHOLDER = "No phone added";
    private static final String EMPTY_STRING = "";
    public final String value;

    /**
     * Constructs a {@code Phone}.
     * @param phone A valid phone number.
     */
    public Phone(String phone) {
        requireNonNull(phone);
        checkArgument(isValidPhone(phone), MESSAGE_CONSTRAINTS);
        value = phone;
    }

    /**
     * Returns true if a given string is a valid phone number.
     */
    public static boolean isValidPhone(String test) {
        return test.matches(VALIDATION_REGEX) || test.equals(PHONE_PLACEHOLDER);
    }

    /**
     * @return a default Phone object
     */
    public static Phone makeDefaultPhone() {
        return new Phone(PHONE_PLACEHOLDER);
    }

    /**
     * Returns if phone is a dummy phone
     */
    public boolean isDefault() {
        return value.equals(PHONE_PLACEHOLDER);
    }

    @Override
    public String toString() {
        if (isDefault()) {
            return EMPTY_STRING;
        } else {
            return value;
        }
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof Phone // instanceof handles nulls
                && value.equals(((Phone) other).value)); // state check
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

}
