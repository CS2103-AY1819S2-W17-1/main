package seedu.address.logic.parser;

import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.*;

import java.util.stream.Stream;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.AddReviewCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.restaurant.Restaurant;
import seedu.address.model.review.Entry;
import seedu.address.model.review.Rating;
import seedu.address.model.review.Review;

/**
 * Parses input arguments and creates a new AddCommand object
 */
public class AddReviewCommandParser implements Parser<AddReviewCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the AddCommand
     * and returns an AddCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public AddReviewCommand parse(String args) throws ParseException {
        ArgumentMultimap argMultimap =
                ArgumentTokenizer.tokenize(args, PREFIX_INDEX, PREFIX_REVIEWENTRY, PREFIX_REVIEWRATING);

        if (!arePrefixesPresent(argMultimap, PREFIX_INDEX, PREFIX_REVIEWENTRY, PREFIX_REVIEWRATING)
                || !argMultimap.getPreamble().isEmpty()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddReviewCommand.MESSAGE_USAGE));
        }

        Index index = ParserUtil.parseIndex(argMultimap.getValue(PREFIX_INDEX).get());
        Entry entry = ParserUtil.parseEntry(argMultimap.getValue(PREFIX_REVIEWENTRY).get());
        Rating rating = ParserUtil.parseRating(argMultimap.getValue(PREFIX_REVIEWRATING).get());


        Review review = new Review(entry, rating);

        return new AddReviewCommand(index, review);
    }

    /**
     * Returns true if none of the prefixes contains empty {@code Optional} values in the given
     * {@code ArgumentMultimap}.
     */
    private static boolean arePrefixesPresent(ArgumentMultimap argumentMultimap, Prefix... prefixes) {
        return Stream.of(prefixes).allMatch(prefix -> argumentMultimap.getValue(prefix).isPresent());
    }

}
