package seedu.eatme.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.eatme.logic.parser.CliSyntax.PREFIX_NAME;

import seedu.eatme.logic.commands.exceptions.CommandException;
import seedu.eatme.model.Model;
import seedu.eatme.model.feed.Feed;

/**
 * Deletes a feed identified using its name from the feed list.
 */
public class DeleteFeedCommand extends Command {

    public static final String COMMAND_WORD = "deletefeed";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Deletes the feed identified using its name from the feed list.\n"
            + "Parameters: " + PREFIX_NAME + " NAME"
            + "Example: " + COMMAND_WORD + " " + PREFIX_NAME + " Seth Lui";

    public static final String MESSAGE_DELETE_FEED_SUCCESS = "Deleted feed: %1$s";
    public static final String MESSAGE_MISSING_FEED = "Feed does not exist in feed list.";

    private final String name;

    public DeleteFeedCommand(String name) {
        this.name = name;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);

        Feed feedToDelete = null;
        for (Feed f : model.getFeedList().getFeedList()) {
            if (f.getName().equals(name)) {
                feedToDelete = f;
                break;
            }
        }

        if (feedToDelete == null) {
            throw new CommandException(MESSAGE_MISSING_FEED);
        }

        model.deleteFeed(feedToDelete);
        return new CommandResult(String.format(MESSAGE_DELETE_FEED_SUCCESS, feedToDelete));
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof DeleteFeedCommand // instanceof handles nulls
                && name.equals(((DeleteFeedCommand) other).name)); // state check
    }
}
