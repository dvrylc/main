package seedu.eatme.logic.commands;

import static seedu.eatme.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.eatme.testutil.TypicalEateries.getTypicalOpenEateryList;
import static seedu.eatme.testutil.TypicalFeeds.getTypicalFeedList;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import seedu.eatme.model.Model;
import seedu.eatme.model.ModelManager;
import seedu.eatme.model.UserPrefs;

public class ModeCommandTest {
    private Model model;
    private Model expectedModel;

    @BeforeEach
    public void setUp() {
        model = new ModelManager(getTypicalOpenEateryList(), getTypicalFeedList(), new UserPrefs());
        expectedModel = new ModelManager(model.getEateryList(), model.getFeedList(), new UserPrefs());
    }

    @Test
    public void execute_listIsNotFiltered_modeChanged() {
        assertCommandSuccess(new ModeCommand(), model, ModeCommand.MESSAGE_SUCCESS_TODO, expectedModel);
        assertCommandSuccess(new ModeCommand(), model, ModeCommand.MESSAGE_SUCCESS_MAIN, expectedModel);
    }


}
