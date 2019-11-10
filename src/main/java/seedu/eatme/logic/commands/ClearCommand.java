package seedu.eatme.logic.commands;

import static java.util.Objects.requireNonNull;

import seedu.eatme.model.EateryList;
import seedu.eatme.model.Model;

/**
 * Clears the address book.
 */
public class ClearCommand extends Command {

    public static final String COMMAND_WORD = "clear";
    public static final String MESSAGE_SUCCESS = "Address book has been cleared!";


    @Override
    public CommandResult execute(Model model) {
        requireNonNull(model);
        model.setEateryList(new EateryList());
        return new CommandResult(MESSAGE_SUCCESS);
    }
}
