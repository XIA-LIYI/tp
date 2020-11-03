package tp.acecs2103.logic.commands;

import static java.util.Objects.requireNonNull;

import java.util.logging.Logger;

import tp.acecs2103.commons.core.LogsCenter;
import tp.acecs2103.logic.commands.exceptions.CommandException;
import tp.acecs2103.model.Model;
import tp.acecs2103.model.TaskList;
import tp.acecs2103.model.task.Index;
import tp.acecs2103.model.task.Task;


/**
 * Mark a task as pending as identified by the index number.
 */

public class UndoneCommand extends Command {
    public static final String COMMAND_WORD = "undone";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Mark the task identified by the index number used in the displayed task list as pending.\n"
            + "Parameters: INDEX (in the form of 0 + two-digit week number + two-digit task number e.g. 01205)\n"
            + "Example: " + COMMAND_WORD + " 0101";

    public static final String MESSAGE_DONE_TASK_SUCCESS = "Undone Task: \n%1$s";

    private final Logger logger = LogsCenter.getLogger(getClass());

    private final Index targetIndex;

    public UndoneCommand(Index targetIndex) {
        this.targetIndex = targetIndex;
    }

    public Index getTargetIndex() {
        return this.targetIndex;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        TaskList lastShownList = model.getTaskList();
        Task taskToMarkAsPending = lastShownList.getTask(targetIndex);
        try {
            model.markTaskAsPending(targetIndex);
        } catch (Exception e) {
            throw new CommandException(e.getMessage());
        }
        return new CommandResult(String.format(MESSAGE_DONE_TASK_SUCCESS, taskToMarkAsPending));
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof DeleteCommand // instanceof handles nulls
                && targetIndex.equals(((UndoneCommand) other).getTargetIndex())); // state check
    }
}
