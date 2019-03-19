package seedu.address.testutil;

import static seedu.address.logic.parser.CliSyntax.PREFIX_REVIEWENTRY;
import static seedu.address.logic.parser.CliSyntax.PREFIX_REVIEWRATING;

import seedu.address.logic.commands.AddReviewCommand;
import seedu.address.logic.commands.EditReviewCommand.EditReviewDescriptor;
import seedu.address.model.review.Review;

/**
 * A utility class for Reviews.
 */
public class ReviewUtil {

    /**
     * Returns an addReview command string for adding the {@code Review} with a default {@code Index} of value 1.
     */
    public static String getAddReviewCommand(Review review) {
        return AddReviewCommand.COMMAND_WORD + " 1 " + getReviewDetails(review);
    }

    /**
     * Returns the part of command string for the given {@code review}'s details.
     */
    public static String getReviewDetails(Review review) {
        StringBuilder sb = new StringBuilder();
        sb.append(PREFIX_REVIEWENTRY + review.getEntry().toString() + " ");
        sb.append(PREFIX_REVIEWRATING + review.getRating().toString() + " ");
        return sb.toString();
    }

    /**
     * Returns the part of command string for the given {@code EditReviewDescriptor}'s details.
     */
    public static String getEditReviewDescriptorDetails(EditReviewDescriptor descriptor) {
        StringBuilder sb = new StringBuilder();
        descriptor.getEntry().ifPresent(entry -> sb.append(PREFIX_REVIEWENTRY).append(entry.toString()).append(" "));
        descriptor.getRating().ifPresent(rating -> sb.append(PREFIX_REVIEWRATING)
                .append(rating.toString()).append(" "));

        return sb.toString();
    }
}
