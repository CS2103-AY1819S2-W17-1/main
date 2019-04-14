package seedu.address.logic.parser;

import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ADDRESS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_EMAIL;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_OPENING_HOURS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PHONE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_POSTAL;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TAG;
import static seedu.address.logic.parser.CliSyntax.PREFIX_WEBLINK;

import java.util.Set;
import java.util.stream.Stream;

import seedu.address.commons.core.Messages;
import seedu.address.commons.exceptions.NoInternetException;
import seedu.address.logic.commands.AddCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.restaurant.Address;
import seedu.address.model.restaurant.Email;
import seedu.address.model.restaurant.Name;
import seedu.address.model.restaurant.OpeningHours;
import seedu.address.model.restaurant.Phone;
import seedu.address.model.restaurant.Postal;
import seedu.address.model.restaurant.Restaurant;
import seedu.address.model.restaurant.Weblink;
import seedu.address.model.tag.Tag;

/**
 * Parses input arguments and creates a new AddCommand object
 */
public class AddCommandParser implements Parser<AddCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the AddCommand
     * and returns an AddCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public AddCommand parse(String args) throws ParseException {

        ArgumentMultimap argMultimap =
                ArgumentTokenizer.tokenize(args, PREFIX_NAME, PREFIX_PHONE, PREFIX_EMAIL, PREFIX_ADDRESS, PREFIX_POSTAL,
                        PREFIX_TAG, PREFIX_WEBLINK, PREFIX_OPENING_HOURS);

        if (!arePrefixesPresent(argMultimap, PREFIX_NAME, PREFIX_ADDRESS, PREFIX_POSTAL)
                || !argMultimap.getPreamble().isEmpty()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddCommand.MESSAGE_USAGE));
        }

        Name name = ParserUtil.parseName(argMultimap.getValue(PREFIX_NAME).get());
        Address address = ParserUtil.parseAddress(argMultimap.getValue(PREFIX_ADDRESS).get());
        Postal postal = ParserUtil.parsePostal(argMultimap.getValue(PREFIX_POSTAL).get());
        Set<Tag> tagList = ParserUtil.parseTags(argMultimap.getAllValues(PREFIX_TAG));

        // Optional fields
        Email email;
        Phone phone;
        OpeningHours openingHours;
        Weblink weblink;
        Restaurant restaurant;

        if (!arePrefixesPresent(argMultimap, PREFIX_EMAIL)) {
            email = Email.makeDefaultEmail();
        } else {
            email = ParserUtil.parseEmail(argMultimap.getValue(PREFIX_EMAIL).get());
        }

        if (!arePrefixesPresent(argMultimap, PREFIX_PHONE)) {
            phone = Phone.makeDefaultPhone();
        } else {
            phone = ParserUtil.parsePhone(argMultimap.getValue(PREFIX_PHONE).get());
        }

        if (!arePrefixesPresent(argMultimap, PREFIX_OPENING_HOURS)) {
            openingHours = OpeningHours.makeDefaultOpening();
        } else {
            openingHours = ParserUtil.parseOpeningHours(argMultimap.getValue(PREFIX_OPENING_HOURS).get());
        }

        if (!arePrefixesPresent(argMultimap, PREFIX_WEBLINK)) {
            weblink = Weblink.makeDefaultWeblink();
        } else {
            try {
                weblink = ParserUtil.parseWeblink(argMultimap.getValue(PREFIX_WEBLINK).get());
            } catch (NoInternetException e) {
                weblink = Weblink.makeDefaultWeblink();
                restaurant = new Restaurant(name, phone, email, address, postal, tagList,
                        weblink, openingHours);
                return new AddCommand(restaurant, Messages.MESSAGE_ADD_NO_INTERNET);
            }
        }

        restaurant = new Restaurant(name, phone, email, address, postal, tagList, weblink, openingHours);
        return new AddCommand(restaurant);
    }

    /**
     * Returns true if none of the prefixes contains empty {@code Optional} values in the given
     * {@code ArgumentMultimap}.
     */
    private static boolean arePrefixesPresent(ArgumentMultimap argumentMultimap, Prefix... prefixes) {
        return Stream.of(prefixes).allMatch(prefix -> argumentMultimap.getValue(prefix).isPresent());
    }

}
