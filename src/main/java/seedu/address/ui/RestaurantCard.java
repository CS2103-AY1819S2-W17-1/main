package seedu.address.ui;

import java.text.DecimalFormat;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import seedu.address.model.restaurant.Restaurant;

/**
 * An UI component that displays information of a {@code Restaurant}.
 */
public class RestaurantCard extends UiPart<Region> {

    private static final String FXML = "RestaurantListCard.fxml";
    private static final DecimalFormat ONE_DP = new DecimalFormat("0.0");
    private static final String FIELD_NOT_ADDED = "N.A.";

    /**
     * Note: Certain keywords such as "location" and "resources" are reserved keywords in JavaFX.
     * As a consequence, UI elements' variable names cannot be set to such keywords
     * or an exception will be thrown by JavaFX during runtime.
     *
     * @see <a href="https://github.com/se-edu/fooddiary-level4/issues/336">The issue on FoodDiary level 4</a>
     */

    public final Restaurant restaurant;

    @FXML
    private HBox cardPane;
    @FXML
    private Label name;
    @FXML
    private Label id;
    @FXML
    private Label phone;
    @FXML
    private Label address;
    @FXML
    private Label postal;
    @FXML
    private Label email;
    @FXML
    private FlowPane tags;
    @FXML
    private HBox categoriesPane;
    @FXML
    private Label avgRating;
    @FXML
    private Label cuisine;
    @FXML
    private Label occasion;
    @FXML
    private Label priceRange;
    @FXML
    private Label weblink;
    @FXML
    private Label openingHours;

    public RestaurantCard(Restaurant restaurant, int displayedIndex) {
        super(FXML);
        this.restaurant = restaurant;
        id.setText(displayedIndex + ". ");
        name.setText(restaurant.getName().fullName);
        address.setText(restaurant.getAddress().value);
        postal.setText("S" + restaurant.getPostal().value);
        restaurant.getTags().forEach(tag -> tags.getChildren().add(new Label(tag.tagName)));

        restaurant.getCategories().setLabels(cuisine, occasion, priceRange);


        // if the following optional fields are default fields, they will not be shown on the UI
        if (restaurant.getEmail().isDefault()) {
            email.setVisible(false);
            email.setManaged(false);
        } else {
            email.setText(restaurant.getEmail().value);
        }

        if (restaurant.getPhone().isDefault()) {
            phone.setVisible(false);
            phone.setManaged(false);
        } else {
            phone.setText(restaurant.getPhone().value);
        }


        if (restaurant.getCategories().isEmpty()) {
            categoriesPane.setVisible(false);
            categoriesPane.setManaged(false);
        }

        // if restaurant weblink is not added, it should not appear on restaurant card
        if (restaurant.getWeblink().isDefault()) {
            weblink.setVisible(false);
            weblink.setManaged(false);
        } else {
            weblink.setText(restaurant.getWeblink().value);
        }

        // if restaurant opening hours is not added, it should not appear on restaurant card
        if (restaurant.getOpeningHours().isDefault()) {
            openingHours.setVisible(false);
            openingHours.setManaged(false);
        } else {
            openingHours.setText(restaurant.getOpeningHours().value);
        }

        // Check if Restaurant has been visited before
        if (restaurant.getSummary().getTotalVisits() > 0) {
            avgRating.setText(ONE_DP.format(restaurant.getSummary().getAvgRating()));
        } else {
            avgRating.setText(FIELD_NOT_ADDED);
        }
    }

    @Override
    public boolean equals(Object other) {
        // short circuit if same object
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof RestaurantCard)) {
            return false;
        }

        // state check
        RestaurantCard card = (RestaurantCard) other;
        return id.getText().equals(card.id.getText())
                && restaurant.equals(card.restaurant);
    }
}
