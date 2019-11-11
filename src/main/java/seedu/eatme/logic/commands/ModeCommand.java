package seedu.eatme.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.eatme.model.Model.PREDICATE_SHOW_ALL_EATERIES;

import seedu.eatme.model.Model;

/**
 * Lists all persons in the eatery list to the user.
 */
public class ModeCommand extends Command {

    public static final String COMMAND_WORD = "mode";

    public static final String MESSAGE_SUCCESS = "Mode successfully changed: %s";

    @Override
    public CommandResult execute(Model model) {
        requireNonNull(model);
        model.toggle();
        model.updateFilteredEateryList(PREDICATE_SHOW_ALL_EATERIES);

        return new CommandResult(String.format(MESSAGE_SUCCESS, model.isMainMode() ? "main mode" : "todo mode"));
    }
}
