package seedu.address.model.restaurant;

import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import seedu.address.model.restaurant.categories.Categories;
import seedu.address.model.review.Review;
import seedu.address.model.tag.Tag;

/**
 * Represents a Restaurant in the food diary.
 * Guarantees: details are present and not null, field values are validated, immutable.
 */
public class Restaurant {

    // Identity fields
    private final Name name;
    private final Phone phone;
    private final Email email;

    // Data fields
    private final Address address;
    private final Set<Tag> tags = new HashSet<>();
    private final Weblink weblink;
    private final List<Review> reviews = new ArrayList<>();
    private final OpeningHours openingHours;
    private final Postal postal;
    private final Summary summary;

    // Categories fields
    private final Categories categories;

    /**
     * Constructor for Restaurant class without Reviews and Categories
     * Every field must be present and not null.
     */
    public Restaurant(Name name, Phone phone, Email email, Address address, Postal postal,
                      Set<Tag> tags, Weblink weblink, OpeningHours openingHours) {
        requireAllNonNull(name, phone, email, address, tags, weblink, openingHours, postal);
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.address = address;
        this.tags.addAll(tags);
        this.weblink = weblink;
        this.openingHours = openingHours;
        this.categories = Categories.empty();
        this.postal = postal;
        this.summary = new Summary(reviews);
    }


    /**
     * Create new restaurant with Categories and Reviews.
     */
    public Restaurant(Name name, Phone phone, Email email, Address address, Postal postal, Set<Tag> tags,
                      Weblink weblink, OpeningHours openingHours, Categories categories, List<Review> reviews) {
        requireAllNonNull(name, phone, email, address, tags, postal);
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.address = address;
        this.tags.addAll(tags);
        this.weblink = weblink;
        this.openingHours = openingHours;
        this.reviews.addAll(reviews);
        if (categories == null) {
            this.categories = Categories.empty();
        } else {
            this.categories = categories;
        }
        this.postal = postal;
        this.summary = new Summary(reviews);
    }

    /**
     * Creates a new restaurant from an existing restaurant with categories set.
     * @param restaurant the restaurant to set cuisine to
     * @param categories the cuisine to be set
     */
    public Restaurant(Restaurant restaurant, Categories categories) {
        requireAllNonNull(restaurant, categories);
        this.name = restaurant.name;
        this.phone = restaurant.phone;
        this.email = restaurant.email;
        this.address = restaurant.address;
        this.tags.addAll(restaurant.tags);
        this.categories = categories;
        this.weblink = restaurant.weblink;
        this.openingHours = restaurant.openingHours;
        this.postal = restaurant.postal;
        this.reviews.addAll(restaurant.reviews);
        this.summary = new Summary(restaurant.getReviews());
    }

    public Name getName() {
        return name;
    }

    public Phone getPhone() {
        return phone;
    }

    public Email getEmail() {
        return email;
    }

    public Address getAddress() {
        return address;
    }

    public Weblink getWeblink() {
        return weblink;
    }

    public OpeningHours getOpeningHours() {
        return openingHours;
    }

    public Categories getCategories() {
        return categories;
    }

    public Postal getPostal() {
        return postal;
    }

    public Summary getSummary() {
        return summary;
    }

    /**
     * Returns an immutable tag set, which throws {@code UnsupportedOperationException}
     * if modification is attempted.
     */
    public Set<Tag> getTags() {
        return Collections.unmodifiableSet(tags);
    }

    /**
     * Returns an immutable review set, which throws {@code UnsupportedOperationException}
     * if modification is attempted.
     */
    public List<Review> getReviews() {
        return Collections.unmodifiableList(reviews);
    }

    /**
     * Returns true if both restaurants of the same name have at least one other identity field that is the same.
     * This defines a weaker notion of equality between two restaurants.
     */
    public boolean isSameRestaurant(Restaurant otherRestaurant) {
        if (otherRestaurant == this) {
            return true;
        }

        return otherRestaurant != null
                && otherRestaurant.getName().equals(getName())
                && (otherRestaurant.getPhone().equals(getPhone()) || otherRestaurant.getEmail().equals(getEmail()));
    }

    /**
     * Returns true if both restaurants have the same identity and data fields.
     * This defines a stronger notion of equality between two restaurants.
     */
    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof Restaurant)) {
            return false;
        }

        Restaurant otherRestaurant = (Restaurant) other;
        return otherRestaurant.getName().equals(getName())
                && otherRestaurant.getPhone().equals(getPhone())
                && otherRestaurant.getEmail().equals(getEmail())
                && otherRestaurant.getAddress().equals(getAddress())
                && otherRestaurant.getTags().equals(getTags())
                && otherRestaurant.getWeblink().equals(getWeblink())
                && otherRestaurant.getOpeningHours().equals(getOpeningHours())
                && otherRestaurant.getReviews().equals(getReviews());
    }

    @Override
    public int hashCode() {
        // use this method for custom fields hashing instead of implementing your own
        return Objects.hash(name, phone, email, address, tags, weblink, openingHours, reviews);
    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append(getName())
                .append(" Address: ")
                .append(getAddress())
                .append(" Postal: ")
                .append(getPostal());

        // Optional fields

        if (!getEmail().isDefault()) {
            builder.append(" Email: ")
                    .append(getEmail());
        }

        if (!getPhone().isDefault()) {
            builder.append(" Phone: ")
                    .append(getPhone());
        }

        if (!getOpeningHours().isDefault()) {
            builder.append(" Opening Hours: ")
                    .append(getOpeningHours());
        }

        if (!getWeblink().isDefault()) {
            builder.append(" Weblink: ")
                    .append(getWeblink());
        }

        if (!getCategories().isEmpty()) {
            builder.append(" Categories: ")
                    .append(categories.toString());
        }

        getTags().forEach(builder::append);

        return builder.toString();
    }

}
