package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.commons.core.Messages.MESSAGE_EATERIES_LISTED_OVERVIEW;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.testutil.TypicalEateries.KFC;
import static seedu.address.testutil.TypicalEateries.MAC;
import static seedu.address.testutil.TypicalEateries.TEXAS;
import static seedu.address.testutil.TypicalEateries.getTypicalOpenAddressBook;
import static seedu.address.testutil.TypicalFeeds.getTypicalFeedList;

import java.util.Arrays;
import java.util.Collections;

import org.junit.jupiter.api.Test;

import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.eatery.EateryAttributesContainsKeywordsPredicate;

/**
 * Contains integration tests (interaction with the Model) for {@code FindCommand}.
 */
public class FindCommandTest {
    private Model model = new ModelManager(getTypicalOpenAddressBook(), getTypicalFeedList(), new UserPrefs());
    private Model expectedModel = new ModelManager(getTypicalOpenAddressBook(), getTypicalFeedList(), new UserPrefs());

    @Test
    public void equals() {
        EateryAttributesContainsKeywordsPredicate firstPredicate =
                new EateryAttributesContainsKeywordsPredicate(Collections.singletonList("first"));
        EateryAttributesContainsKeywordsPredicate secondPredicate =
                new EateryAttributesContainsKeywordsPredicate(Collections.singletonList("second"));

        FindCommand findFirstCommand = new FindCommand(firstPredicate);
        FindCommand findSecondCommand = new FindCommand(secondPredicate);

        // same object -> returns true
        assertTrue(findFirstCommand.equals(findFirstCommand));

        // same values -> returns true
        FindCommand findFirstCommandCopy = new FindCommand(firstPredicate);
        assertTrue(findFirstCommand.equals(findFirstCommandCopy));

        // different types -> returns false
        assertFalse(findFirstCommand.equals(1));

        // null -> returns false
        assertFalse(findFirstCommand.equals(null));

        // different eatery -> returns false
        assertFalse(findFirstCommand.equals(findSecondCommand));
    }

    @Test
    public void execute_zeroKeywords_allEateriesFound() {
        String expectedMessage = String.format(MESSAGE_EATERIES_LISTED_OVERVIEW, 7);
        EateryAttributesContainsKeywordsPredicate predicate = preparePredicate(" ");
        FindCommand command = new FindCommand(predicate);
        expectedModel.updateFilteredEateryList(predicate);
        assertCommandSuccess(command, model, expectedMessage, expectedModel);
        assertEquals(expectedModel.getFilteredEateryList(), model.getFilteredEateryList());
    }

    @Test
    public void execute_multipleKeywords_multipleEateriesFound() {
        String expectedMessage = String.format(MESSAGE_EATERIES_LISTED_OVERVIEW, 3);
        EateryAttributesContainsKeywordsPredicate predicate = preparePredicate("McDonald Kentucky Texas");
        FindCommand command = new FindCommand(predicate);
        expectedModel.updateFilteredEateryList(predicate);
        assertCommandSuccess(command, model, expectedMessage, expectedModel);
        assertEquals(Arrays.asList(MAC, KFC, TEXAS), model.getFilteredEateryList());
    }

    /**
     * Parses {@code userInput} into a {@code EateryAttributesContainsKeywordsPredicate}.
     */
    private EateryAttributesContainsKeywordsPredicate preparePredicate(String userInput) {
        return new EateryAttributesContainsKeywordsPredicate(Arrays.asList(userInput.split("\\s+")));
    }
}
