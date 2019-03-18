package seedu.address.logic.commands;

import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.logic.commands.CommandTestUtil.showRestaurantAtIndex;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_RESTAURANT;
import static seedu.address.testutil.TypicalRestaurants.getTypicalFoodDiary;

import org.junit.Before;
import org.junit.Test;

import seedu.address.logic.CommandHistory;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;

/**
 * Contains integration tests (interaction with the Model) and unit tests for ListCommand.
 */
public class ListUnvisitedCommandTest {

    private Model model;
    private Model expectedModel;
    private CommandHistory commandHistory = new CommandHistory();

    @Before
    public void setUp() {
        model = new ModelManager(getTypicalFoodDiary(), new UserPrefs());
        expectedModel = new ModelManager(model.getFoodDiary(), new UserPrefs());
        expectedModel.updateFilteredRestaurantList(Model.PREDICATE_SHOW_UNVISITED_RESTAURANTS);

    }

    @Test
    public void execute_listIsFiltered_showsEverything() {
        showRestaurantAtIndex(model, INDEX_FIRST_RESTAURANT);
        assertCommandSuccess(new ListUnvisitedCommand(), model, commandHistory, ListUnvisitedCommand.MESSAGE_SUCCESS, expectedModel);
    }

}
