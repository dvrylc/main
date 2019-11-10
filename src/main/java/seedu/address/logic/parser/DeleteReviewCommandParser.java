package seedu.address.logic.parser;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.DeleteReviewCommand;
import seedu.address.logic.parser.exceptions.ParseException;

/**
 * Parses delete review commands input by user.
 */
public class DeleteReviewCommandParser implements Parser<DeleteReviewCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the DeleteReviewCommand
     * and returns a DeleteReviewCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public DeleteReviewCommand parse(String args) throws ParseException {
        try {
            requireNonNull(args);
            Index index = ParserUtil.parseIndex(args);
            return new DeleteReviewCommand(index);
        } catch (ParseException pe) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, DeleteReviewCommand.MESSAGE_USAGE), pe);
        }
    }
}