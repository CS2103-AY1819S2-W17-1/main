package systemtests;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.ADDRESS_DESC_AMY;
import static seedu.address.logic.commands.CommandTestUtil.ADDRESS_DESC_BOB;
import static seedu.address.logic.commands.CommandTestUtil.EMAIL_DESC_AMY;
import static seedu.address.logic.commands.CommandTestUtil.EMAIL_DESC_BOB;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_ADDRESS_DESC;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_EMAIL_DESC;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_NAME_DESC;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_PHONE_DESC;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_POSTAL_DESC;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_TAG_DESC;
import static seedu.address.logic.commands.CommandTestUtil.NAME_DESC_AMY;
import static seedu.address.logic.commands.CommandTestUtil.NAME_DESC_BOB;
import static seedu.address.logic.commands.CommandTestUtil.OPENING_HOURS_DESC;
import static seedu.address.logic.commands.CommandTestUtil.PHONE_DESC_AMY;
import static seedu.address.logic.commands.CommandTestUtil.PHONE_DESC_BOB;
import static seedu.address.logic.commands.CommandTestUtil.POSTAL_DESC_AMY;
import static seedu.address.logic.commands.CommandTestUtil.POSTAL_DESC_BOB;
import static seedu.address.logic.commands.CommandTestUtil.TAG_DESC_FRIEND;
import static seedu.address.logic.commands.CommandTestUtil.TAG_DESC_HUSBAND;
import static seedu.address.logic.commands.CommandTestUtil.VALID_EMAIL_AMY;
import static seedu.address.logic.commands.CommandTestUtil.VALID_NAME_AMY;
import static seedu.address.logic.commands.CommandTestUtil.VALID_NAME_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_PHONE_AMY;
import static seedu.address.logic.commands.CommandTestUtil.VALID_TAG_HUSBAND;
import static seedu.address.logic.commands.CommandTestUtil.WEBLINK_DESC_AMY;
import static seedu.address.logic.commands.CommandTestUtil.WEBLINK_DESC_BOB;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TAG;
import static seedu.address.model.Model.PREDICATE_SHOW_ALL_RESTAURANTS;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_RESTAURANT;
import static seedu.address.testutil.TypicalIndexes.INDEX_SECOND_RESTAURANT;
import static seedu.address.testutil.TypicalRestaurants.AMY;
import static seedu.address.testutil.TypicalRestaurants.BOB;
import static seedu.address.testutil.TypicalRestaurants.KEYWORD_MATCHING_MEIER;

import org.junit.Test;

import seedu.address.commons.core.Messages;
import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.EditCommand;
import seedu.address.logic.commands.RedoCommand;
import seedu.address.logic.commands.UndoCommand;
import seedu.address.model.Model;
import seedu.address.model.restaurant.Address;
import seedu.address.model.restaurant.Email;
import seedu.address.model.restaurant.Name;
import seedu.address.model.restaurant.Phone;
import seedu.address.model.restaurant.Postal;
import seedu.address.model.restaurant.Restaurant;
import seedu.address.model.tag.Tag;
import seedu.address.testutil.RestaurantBuilder;
import seedu.address.testutil.RestaurantUtil;

public class EditCommandSystemTest extends FoodDiarySystemTest {

    @Test
    public void edit() {
        Model model = getModel();

        /* ----------------- Performing edit operation while an unfiltered list is being shown ---------------------- */

        /* Case: edit all fields, command with leading spaces, trailing spaces and multiple spaces between each field
         * -> edited
         */
        Index index = INDEX_FIRST_RESTAURANT;
        String command = " " + EditCommand.COMMAND_WORD + "  " + index.getOneBased() + "  " + NAME_DESC_BOB + "  "
                + PHONE_DESC_BOB + " " + EMAIL_DESC_BOB + "  " + ADDRESS_DESC_BOB + " " + POSTAL_DESC_BOB + " "
                + TAG_DESC_HUSBAND + " " + WEBLINK_DESC_BOB + OPENING_HOURS_DESC;
        Restaurant editedRestaurant = new RestaurantBuilder(BOB).withTags(VALID_TAG_HUSBAND).build();
        assertCommandSuccess(command, index, editedRestaurant);

        /* Case: undo editing the last restaurant in the list -> last restaurant restored */
        command = UndoCommand.COMMAND_WORD;
        String expectedResultMessage = UndoCommand.MESSAGE_SUCCESS;
        assertCommandSuccess(command, model, expectedResultMessage);

        /* Case: redo editing the last restaurant in the list -> last restaurant edited again */
        command = RedoCommand.COMMAND_WORD;
        expectedResultMessage = RedoCommand.MESSAGE_SUCCESS;
        model.setRestaurant(getModel().getFilteredRestaurantList().get(INDEX_FIRST_RESTAURANT.getZeroBased()),
                editedRestaurant);
        assertCommandSuccess(command, model, expectedResultMessage);

        /* Case: edit a restaurant with new values same as existing values -> edited */
        command = EditCommand.COMMAND_WORD + " " + index.getOneBased() + NAME_DESC_BOB + PHONE_DESC_BOB + EMAIL_DESC_BOB
                + ADDRESS_DESC_BOB + TAG_DESC_FRIEND + TAG_DESC_HUSBAND + WEBLINK_DESC_BOB + OPENING_HOURS_DESC;
        assertCommandSuccess(command, index, BOB);

        /* Case: edit a restaurant with new values same as another restaurant's values
         * but with different name -> edited
         */
        assertTrue(getModel().getFoodDiary().getRestaurantList().contains(BOB));
        index = INDEX_SECOND_RESTAURANT;
        assertNotEquals(getModel().getFilteredRestaurantList().get(index.getZeroBased()), BOB);
        command = EditCommand.COMMAND_WORD + " " + index.getOneBased() + NAME_DESC_AMY + PHONE_DESC_BOB + EMAIL_DESC_BOB
                + ADDRESS_DESC_BOB + POSTAL_DESC_BOB + TAG_DESC_FRIEND + TAG_DESC_HUSBAND
                + WEBLINK_DESC_BOB + OPENING_HOURS_DESC;
        editedRestaurant = new RestaurantBuilder(BOB).withName(VALID_NAME_AMY).build();
        assertCommandSuccess(command, index, editedRestaurant);

        /* Case: edit a restaurant with new values same as another restaurant's values but with different phone and
         * email -> edited
         */
        index = INDEX_SECOND_RESTAURANT;
        command = EditCommand.COMMAND_WORD + " " + index.getOneBased() + NAME_DESC_BOB + PHONE_DESC_AMY + EMAIL_DESC_AMY
                + ADDRESS_DESC_BOB + TAG_DESC_FRIEND + TAG_DESC_HUSBAND + WEBLINK_DESC_BOB + OPENING_HOURS_DESC;
        editedRestaurant = new RestaurantBuilder(BOB).withPhone(VALID_PHONE_AMY).withEmail(VALID_EMAIL_AMY).build();
        assertCommandSuccess(command, index, editedRestaurant);

        /* Case: clear tags -> cleared */
        index = INDEX_FIRST_RESTAURANT;
        command = EditCommand.COMMAND_WORD + " " + index.getOneBased() + " " + PREFIX_TAG.getPrefix();
        Restaurant restaurantToEdit = getModel().getFilteredRestaurantList().get(index.getZeroBased());
        editedRestaurant = new RestaurantBuilder(restaurantToEdit).withTags().build();
        assertCommandSuccess(command, index, editedRestaurant);

        /* ------------------ Performing edit operation while a filtered list is being shown ------------------------ */

        /* Case: filtered restaurant list, edit index within bounds of food diary and restaurant list -> edited */
        showRestaurantsWithName(KEYWORD_MATCHING_MEIER);
        index = INDEX_FIRST_RESTAURANT;
        assertTrue(index.getZeroBased() < getModel().getFilteredRestaurantList().size());
        command = EditCommand.COMMAND_WORD + " " + index.getOneBased() + " " + NAME_DESC_BOB;
        restaurantToEdit = getModel().getFilteredRestaurantList().get(index.getZeroBased());
        editedRestaurant = new RestaurantBuilder(restaurantToEdit).withName(VALID_NAME_BOB).build();
        assertCommandSuccess(command, index, editedRestaurant);

        /* Case: filtered restaurant list, edit index within bounds of food diary but out of bounds of restaurant list
         * -> rejected
         */
        showRestaurantsWithName(KEYWORD_MATCHING_MEIER);
        int invalidIndex = getModel().getFoodDiary().getRestaurantList().size();
        assertCommandFailure(EditCommand.COMMAND_WORD + " " + invalidIndex + NAME_DESC_BOB,
                Messages.MESSAGE_INVALID_RESTAURANT_DISPLAYED_INDEX);

        /* ------------------- Performing edit operation while a restaurant card is selected ------------------------ */

        /* Case: selects first card in the restaurant list, edit a restaurant -> edited, card selection remains
         * unchanged but browser url changes
         */
        showAllRestaurants();
        index = INDEX_FIRST_RESTAURANT;
        selectRestaurant(index);
        command = EditCommand.COMMAND_WORD + " " + index.getOneBased() + NAME_DESC_AMY + PHONE_DESC_AMY + EMAIL_DESC_AMY
                + ADDRESS_DESC_AMY + POSTAL_DESC_AMY + TAG_DESC_FRIEND + WEBLINK_DESC_AMY + OPENING_HOURS_DESC;
        // this can be misleading: card selection actually remains unchanged but the
        // browser's url is updated to reflect the new restaurant's name
        assertCommandSuccess(command, index, AMY, index);

        /* --------------------------------- Performing invalid edit operation -------------------------------------- */

        /* Case: invalid index (0) -> rejected */
        assertCommandFailure(EditCommand.COMMAND_WORD + " 0" + NAME_DESC_BOB,
                String.format(Messages.MESSAGE_INVALID_COMMAND_FORMAT, EditCommand.MESSAGE_USAGE));

        /* Case: invalid index (-1) -> rejected */
        assertCommandFailure(EditCommand.COMMAND_WORD + " -1" + NAME_DESC_BOB,
                String.format(Messages.MESSAGE_INVALID_COMMAND_FORMAT, EditCommand.MESSAGE_USAGE));

        /* Case: invalid index (size + 1) -> rejected */
        invalidIndex = getModel().getFilteredRestaurantList().size() + 1;
        assertCommandFailure(EditCommand.COMMAND_WORD + " " + invalidIndex + NAME_DESC_BOB,
                Messages.MESSAGE_INVALID_RESTAURANT_DISPLAYED_INDEX);

        /* Case: missing index -> rejected */
        assertCommandFailure(EditCommand.COMMAND_WORD + NAME_DESC_BOB,
                String.format(Messages.MESSAGE_INVALID_COMMAND_FORMAT, EditCommand.MESSAGE_USAGE));

        /* Case: missing all fields -> rejected */
        assertCommandFailure(EditCommand.COMMAND_WORD + " " + INDEX_FIRST_RESTAURANT.getOneBased(),
                EditCommand.MESSAGE_NOT_EDITED);

        /* Case: invalid name -> rejected */
        assertCommandFailure(EditCommand.COMMAND_WORD + " " + INDEX_FIRST_RESTAURANT.getOneBased()
                        + INVALID_NAME_DESC,
                Name.MESSAGE_CONSTRAINTS);

        /* Case: invalid phone -> rejected */
        assertCommandFailure(EditCommand.COMMAND_WORD + " " + INDEX_FIRST_RESTAURANT.getOneBased()
                        + INVALID_PHONE_DESC,
                Phone.MESSAGE_CONSTRAINTS);

        /* Case: invalid email -> rejected */
        assertCommandFailure(EditCommand.COMMAND_WORD + " " + INDEX_FIRST_RESTAURANT.getOneBased()
                        + INVALID_EMAIL_DESC,
                Email.MESSAGE_CONSTRAINTS);

        /* Case: invalid address -> rejected */
        assertCommandFailure(EditCommand.COMMAND_WORD + " " + INDEX_FIRST_RESTAURANT.getOneBased()
                        + INVALID_ADDRESS_DESC,
                Address.MESSAGE_CONSTRAINTS);

        /* Case: invalid postal -> rejected */
        assertCommandFailure(EditCommand.COMMAND_WORD + " " + INDEX_FIRST_RESTAURANT.getOneBased()
                        + INVALID_POSTAL_DESC,
                Postal.MESSAGE_CONSTRAINTS);

        /* Case: invalid tag -> rejected */
        assertCommandFailure(EditCommand.COMMAND_WORD + " " + INDEX_FIRST_RESTAURANT.getOneBased()
                        + INVALID_TAG_DESC,
                Tag.MESSAGE_CONSTRAINTS);

        /* Case: edit a restaurant with new values same as another restaurant's values -> rejected */
        executeCommand(RestaurantUtil.getAddCommand(BOB));
        assertTrue(getModel().getFoodDiary().getRestaurantList().contains(BOB));
        index = INDEX_FIRST_RESTAURANT;
        assertFalse(getModel().getFilteredRestaurantList().get(index.getZeroBased()).equals(BOB));
        command = EditCommand.COMMAND_WORD + " " + index.getOneBased() + NAME_DESC_BOB + PHONE_DESC_BOB + EMAIL_DESC_BOB
                + ADDRESS_DESC_BOB + TAG_DESC_FRIEND + TAG_DESC_HUSBAND + WEBLINK_DESC_BOB + OPENING_HOURS_DESC;
        assertCommandFailure(command, EditCommand.MESSAGE_DUPLICATE_RESTAURANT);

        /* Case: edit a restaurant with new values same as another restaurant's values but with different tags
         * -> rejected
         */
        command = EditCommand.COMMAND_WORD + " " + index.getOneBased() + NAME_DESC_BOB + PHONE_DESC_BOB + EMAIL_DESC_BOB
                + ADDRESS_DESC_BOB + TAG_DESC_HUSBAND + WEBLINK_DESC_BOB + OPENING_HOURS_DESC;
        assertCommandFailure(command, EditCommand.MESSAGE_DUPLICATE_RESTAURANT);

        /* Case: edit a restaurant with new values same as another restaurant's values but with different address
         * -> rejected
         */
        command = EditCommand.COMMAND_WORD + " " + index.getOneBased() + NAME_DESC_BOB + PHONE_DESC_BOB + EMAIL_DESC_BOB
                + ADDRESS_DESC_AMY + TAG_DESC_FRIEND + TAG_DESC_HUSBAND + WEBLINK_DESC_BOB + OPENING_HOURS_DESC;
        assertCommandFailure(command, EditCommand.MESSAGE_DUPLICATE_RESTAURANT);

        /* Case: edit a restaurant with new values same as another restaurant's values but with different phone
         * -> rejected
         */
        command = EditCommand.COMMAND_WORD + " " + index.getOneBased() + NAME_DESC_BOB + PHONE_DESC_AMY + EMAIL_DESC_BOB
                + ADDRESS_DESC_BOB + TAG_DESC_FRIEND + TAG_DESC_HUSBAND + WEBLINK_DESC_BOB + OPENING_HOURS_DESC;
        assertCommandFailure(command, EditCommand.MESSAGE_DUPLICATE_RESTAURANT);

        /* Case: edit a restaurant with new values same as another restaurant's values but with different email
         * -> rejected
         */
        command = EditCommand.COMMAND_WORD + " " + index.getOneBased() + NAME_DESC_BOB + PHONE_DESC_BOB + EMAIL_DESC_AMY
                + ADDRESS_DESC_BOB + TAG_DESC_FRIEND + TAG_DESC_HUSBAND + WEBLINK_DESC_BOB + OPENING_HOURS_DESC;
        assertCommandFailure(command, EditCommand.MESSAGE_DUPLICATE_RESTAURANT);
    }

    /**
     * Performs the same verification as {@code assertCommandSuccess(String, Index, Restaurant, Index)} except that
     * the browser url and selected card remain unchanged.
     * @param toEdit the index of the current model's filtered list
     * @see EditCommandSystemTest#assertCommandSuccess(String, Index, Restaurant, Index)
     */
    private void assertCommandSuccess(String command, Index toEdit, Restaurant editedRestaurant) {
        assertCommandSuccess(command, toEdit, editedRestaurant, null);
    }

    /**
     * Performs the same verification as {@code assertCommandSuccess(String, Model, String, Index)} and in addition,<br>
     * 1. Asserts that result display box displays the success message of executing {@code EditCommand}.<br>
     * 2. Asserts that the model related components are updated to reflect the restaurant at index {@code toEdit} being
     * updated to values specified {@code editedRestaurant}.<br>
     * @param toEdit the index of the current model's filtered list.
     * @see EditCommandSystemTest#assertCommandSuccess(String, Model, String, Index)
     */
    private void assertCommandSuccess(String command, Index toEdit, Restaurant editedRestaurant,
            Index expectedSelectedCardIndex) {
        Model expectedModel = getModel();
        expectedModel.setRestaurant(expectedModel.getFilteredRestaurantList().get(toEdit.getZeroBased()),
                editedRestaurant);
        expectedModel.updateFilteredRestaurantList(PREDICATE_SHOW_ALL_RESTAURANTS);

        assertCommandSuccess(command, expectedModel,
                String.format(EditCommand.MESSAGE_EDIT_RESTAURANT_SUCCESS, editedRestaurant),
                expectedSelectedCardIndex);
    }

    /**
     * Performs the same verification as {@code assertCommandSuccess(String, Model, String, Index)} except that the
     * browser url and selected card remain unchanged.
     * @see EditCommandSystemTest#assertCommandSuccess(String, Model, String, Index)
     */
    private void assertCommandSuccess(String command, Model expectedModel, String expectedResultMessage) {
        assertCommandSuccess(command, expectedModel, expectedResultMessage, null);
    }

    /**
     * Executes {@code command} and in addition,<br>
     * 1. Asserts that the command box displays an empty string.<br>
     * 2. Asserts that the result display box displays {@code expectedResultMessage}.<br>
     * 3. Asserts that the browser url and selected card update accordingly depending on the card at
     * {@code expectedSelectedCardIndex}.<br>
     * 4. Asserts that the status bar's sync status changes.<br>
     * 5. Asserts that the command box has the default style class.<br>
     * Verifications 1 and 2 are performed by
     * {@code FoodDiarySystemTest#assertApplicationDisplaysExpected(String, String, Model)}.<br>
     * @see FoodDiarySystemTest#assertApplicationDisplaysExpected(String, String, Model)
     * @see FoodDiarySystemTest#assertSelectedCardChanged(Index)
     */
    private void assertCommandSuccess(String command, Model expectedModel, String expectedResultMessage,
            Index expectedSelectedCardIndex) {
        executeCommand(command);
        expectedModel.updateFilteredRestaurantList(PREDICATE_SHOW_ALL_RESTAURANTS);
        assertApplicationDisplaysExpected("", expectedResultMessage, expectedModel);
        assertCommandBoxShowsDefaultStyle();
        if (expectedSelectedCardIndex != null) {
            assertSelectedCardChanged(expectedSelectedCardIndex);
        } else {
            assertSelectedCardUnchanged();
        }
        assertStatusBarUnchangedExceptSyncStatus();
    }

    /**
     * Executes {@code command} and in addition,<br>
     * 1. Asserts that the command box displays {@code command}.<br>
     * 2. Asserts that result display box displays {@code expectedResultMessage}.<br>
     * 3. Asserts that the browser url, selected card and status bar remain unchanged.<br>
     * 4. Asserts that the command box has the error style.<br>
     * Verifications 1 and 2 are performed by
     * {@code FoodDiarySystemTest#assertApplicationDisplaysExpected(String, String, Model)}.<br>
     * @see FoodDiarySystemTest#assertApplicationDisplaysExpected(String, String, Model)
     */
    private void assertCommandFailure(String command, String expectedResultMessage) {
        Model expectedModel = getModel();

        executeCommand(command);
        assertApplicationDisplaysExpected(command, expectedResultMessage, expectedModel);
        assertSelectedCardUnchanged();
        assertCommandBoxShowsErrorStyle();
        assertStatusBarUnchanged();
    }
}
