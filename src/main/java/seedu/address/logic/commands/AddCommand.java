package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ADDRESS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_EMAIL;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_OPENING_HOURS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PHONE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_POSTAL;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TAG;
import static seedu.address.logic.parser.CliSyntax.PREFIX_WEBLINK;

import seedu.address.logic.CommandHistory;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.restaurant.Restaurant;

/**
 * Adds a restaurant to the food diary.
 */
public class AddCommand extends Command {

    public static final String COMMAND_WORD = "add";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Adds a restaurant to the food diary. "
            + "Parameters: "
            + PREFIX_NAME + "NAME "
            + PREFIX_ADDRESS + "ADDRESS "
            + PREFIX_POSTAL + "POSTAL CODE "
            + "[" + PREFIX_PHONE + "PHONE] "
            + "[" + PREFIX_EMAIL + "EMAIL] "
            + "[" + PREFIX_TAG + "TAG]... "
            + "[" + PREFIX_WEBLINK + "WEBLINK] "
            + "[" + PREFIX_OPENING_HOURS + "OPENING_HOURS]\n"
            + "Example: " + COMMAND_WORD + " "
            + PREFIX_NAME + "KFC "
            + PREFIX_ADDRESS + "311, Clementi Ave 2, #02-25 "
            + PREFIX_POSTAL + "123456 "
            + PREFIX_PHONE + "68765432 "
            + PREFIX_EMAIL + "kfc@example.com "
            + PREFIX_TAG + "indulgences "
            + PREFIX_WEBLINK + "www.kfc.com.sg "
            + PREFIX_OPENING_HOURS + "1000 to 2130";

    public static final String MESSAGE_SUCCESS = "New restaurant added: %1$s";
    public static final String MESSAGE_DUPLICATE_RESTAURANT = "This restaurant already exists in the food diary";
    private String commandMessage;

    private final Restaurant toAdd;

    /**
     * Creates an AddCommand to add the specified {@code Restaurant}
     */
    public AddCommand(Restaurant restaurant) {
        this(restaurant, MESSAGE_SUCCESS);
    }

    /**
     * Creates an AddCommand to add the specified {@code Restaurant}
     */
    public AddCommand(Restaurant restaurant, String commandMessage) {
        requireNonNull(restaurant);
        this.toAdd = restaurant;
        this.commandMessage = commandMessage;
        if (!commandMessage.equals(MESSAGE_SUCCESS)) {
            this.commandMessage = commandMessage.concat(MESSAGE_SUCCESS);
        }
    }

    @Override
    public CommandResult execute(Model model, CommandHistory history) throws CommandException {
        requireNonNull(model);

        if (model.hasRestaurant(toAdd)) {
            throw new CommandException(MESSAGE_DUPLICATE_RESTAURANT);
        }
        model.addRestaurant(toAdd);
        model.commitFoodDiary();
        return new CommandResult(String.format(commandMessage, toAdd));
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof AddCommand // instanceof handles nulls
                && toAdd.equals(((AddCommand) other).toAdd));
    }
}
