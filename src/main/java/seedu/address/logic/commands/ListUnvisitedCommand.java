package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_POSTAL;
import static seedu.address.model.Model.PREDICATE_SHOW_UNVISITED_RESTAURANTS;

import java.util.Optional;

import seedu.address.logic.CommandHistory;
import seedu.address.logic.commands.comparators.SortDistanceComparator;
import seedu.address.model.Model;
import seedu.address.model.PostalData;
import seedu.address.model.PostalDataSet;
import seedu.address.model.restaurant.Postal;

/**
 * Lists all restaurants that have yet to be visited in the food diary to the user.
 */
public class ListUnvisitedCommand extends Command {

    public static final String COMMAND_WORD = "listUnvisited";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": List unvisited "
            + "Parameters: "
            + PREFIX_POSTAL + "POSTAL CODE ";

    public static final String MESSAGE_SUCCESS = "Listed all unvisited restaurants based on "
            + "proximity to provided Postal Code";

    public static final String MESSAGE_INVALID_POSTAL_DATA = "Postal Data file is invalid";

    public static final String MESSAGE_INVALID_POSTAL_CODE = "Postal Code provided cannot be found";

    private String postal;

    public ListUnvisitedCommand(Postal postal) {
        this.postal = postal.value;
    }

    @Override
    public CommandResult execute(Model model, CommandHistory history) {

        requireNonNull(model);
        Optional<PostalDataSet> postalDataSetOptional = model.getPostalDataSet();
        Optional<PostalData> postalDataOfUser;
        if (postalDataSetOptional.isPresent()) {
            postalDataOfUser = postalDataSetOptional.get().getPostalData(postal);
        } else {
            model.updateFilteredRestaurantList(PREDICATE_SHOW_UNVISITED_RESTAURANTS);
            return new CommandResult(MESSAGE_INVALID_POSTAL_DATA);
        }
        if (postalDataOfUser.isPresent()) {
            model.updateFilteredRestaurantListAndSort(PREDICATE_SHOW_UNVISITED_RESTAURANTS,
                    new SortDistanceComparator(postalDataSetOptional.get(), postalDataOfUser.get()));
            return new CommandResult(MESSAGE_SUCCESS);
        } else {
            model.updateFilteredRestaurantList(PREDICATE_SHOW_UNVISITED_RESTAURANTS);
            return new CommandResult(MESSAGE_INVALID_POSTAL_CODE);
        }
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof ListUnvisitedCommand // instanceof handles nulls
                && postal.equals(((ListUnvisitedCommand) other).postal));
    }


}
