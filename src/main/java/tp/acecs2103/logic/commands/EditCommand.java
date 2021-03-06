package tp.acecs2103.logic.commands;

import static java.util.Objects.requireNonNull;

import java.util.Optional;

import tp.acecs2103.commons.core.Messages;
import tp.acecs2103.commons.util.CollectionUtil;
import tp.acecs2103.logic.commands.exceptions.CommandException;
import tp.acecs2103.model.Model;
import tp.acecs2103.model.TaskList;
import tp.acecs2103.model.task.Admin;
import tp.acecs2103.model.task.CustomizedDeadline;
import tp.acecs2103.model.task.Description;
import tp.acecs2103.model.task.IP;
import tp.acecs2103.model.task.Index;
import tp.acecs2103.model.task.Remark;
import tp.acecs2103.model.task.TP;
import tp.acecs2103.model.task.Task;
import tp.acecs2103.model.task.Topic;
import tp.acecs2103.model.task.WeekNumber;


/**
 * Edits the details of an existing person in the address book.
 */
public class EditCommand extends Command {

    public static final String COMMAND_WORD = "edit";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Edit the task identified by the index number with provided information.\n"
            + "Parameters: (only INDEX is compulsory)\n"
            + "i/INDEX (in the form of 0 + two-digit week number + two-digit task number e.g. 01205)\n"
            + "w/WEEK_NUMBER (an integer in range [1,13], only for edition of customized task)\n"
            + "d/DESCRIPTION (only for edition of customized task)\n"
            + "c/CUSTOMISED_DEADLINE (in the form of YYYY-MM-DD)\n"
            + "r/REMARK\n"
            + "Example: \n" + COMMAND_WORD + " i/0101 c/2020-10-12 r/new remark (for default task)\n "
            + "edit i/0109 d/new description (for customized task)";

    public static final String MESSAGE_EDIT_TASK_SUCCESS = "Edited Task: \n%1$s";
    public static final String MESSAGE_NOT_EDITED = "At least one field to edit must be provided.";
    public static final String MESSAGE_DUPLICATE_TASK = "This task already exists in the task list.";
    public static final String MESSAGE_INVALID_EDITION =
        "Only customised deadline and remark can be changed for a default task.";
    public static final String MESSAGE_INVALID_INDEX = "\nTask with the input index does not exist in the task list."
            + " Please try again.";

    private final Index index;
    private final EditTaskDescriptor editTaskDescriptor;

    /**
     * @param index of the person in the filtered person list to edit
     * @param editTaskDescriptor details to edit the person with
     */
    public EditCommand(Index index, EditTaskDescriptor editTaskDescriptor) {
        requireNonNull(index);
        requireNonNull(editTaskDescriptor);

        this.index = index;
        this.editTaskDescriptor = new EditTaskDescriptor(editTaskDescriptor);
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        TaskList lastShownList = model.getTaskList();

        if (lastShownList.getTask(index) == null) {
            throw new CommandException(Messages.MESSAGE_INVALID_TASK_DISPLAYED_INDEX
                    + MESSAGE_INVALID_INDEX);
        }

        Task taskToEdit = lastShownList.getTask(index);
        Task editedTask = createEditedTask(taskToEdit, editTaskDescriptor);

        if (!taskToEdit.isSameTask(editedTask) && model.hasTask(editedTask)) {
            throw new CommandException(MESSAGE_DUPLICATE_TASK);
        }

        model.setTask(taskToEdit, editedTask);
        return new CommandResult(String.format(MESSAGE_EDIT_TASK_SUCCESS, editedTask));
    }

    /**
     * edited with {@code editPersonDescriptor}.
     */
    private static Task createEditedTask(Task taskToEdit, EditTaskDescriptor editTaskDescriptor)
        throws CommandException {
        assert taskToEdit != null;
        if (!taskToEdit.isCustomized()) {
            if (!(editTaskDescriptor.weekNumber == null && editTaskDescriptor.description == null)) {
                throw new CommandException(MESSAGE_INVALID_EDITION);
            }
        }

        WeekNumber updatedWeekNumber = editTaskDescriptor
                .getWeekNumber().orElse(taskToEdit.getWeekNumber());
        Description updatedDescription = editTaskDescriptor
                .getDescription().orElse(taskToEdit.getDescription());

        CustomizedDeadline updatedCustomizedDeadline = editTaskDescriptor
                .getCustomizedDeadline().orElse(taskToEdit.getCustomizedDeadline());
        Remark remark = editTaskDescriptor.getRemark().orElse(taskToEdit.getRemark());

        if (taskToEdit instanceof Topic) {
            return new Topic(taskToEdit.getIndex(), updatedWeekNumber, updatedDescription,
                    taskToEdit.getOfficialDeadline(), updatedCustomizedDeadline, remark,
                    taskToEdit.isCustomized(), taskToEdit.isDone());
        } else if (taskToEdit instanceof Admin) {
            return new Admin(taskToEdit.getIndex(), updatedWeekNumber, updatedDescription,
                    taskToEdit.getOfficialDeadline(), updatedCustomizedDeadline, remark,
                    taskToEdit.isCustomized(), taskToEdit.isDone());
        } else if (taskToEdit instanceof TP) {
            return new TP(taskToEdit.getIndex(), updatedWeekNumber, updatedDescription,
                    taskToEdit.getOfficialDeadline(), updatedCustomizedDeadline, remark,
                    taskToEdit.isCustomized(), taskToEdit.isDone());
        } else {
            return new IP(taskToEdit.getIndex(), updatedWeekNumber, updatedDescription,
                    taskToEdit.getOfficialDeadline(), updatedCustomizedDeadline, remark,
                    taskToEdit.isCustomized(), taskToEdit.isDone());
        }
    }


    @Override
    public boolean equals(Object other) {
        // short circuit if same object
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof EditCommand)) {
            return false;
        }

        // state check
        EditCommand e = (EditCommand) other;
        return index.equals(e.index)
                && editTaskDescriptor.equals(e.editTaskDescriptor);
    }

    /**
     * Stores the details to edit the person with. Each non-empty field value will replace the
     * corresponding field value of the person.
     */
    public static class EditTaskDescriptor {
        private WeekNumber weekNumber;
        private Description description;
        private CustomizedDeadline customizedDeadline;
        private Remark remark;

        public EditTaskDescriptor() {}

        /**
         * Copy constructor.
         * A defensive copy of {@code tags} is used internally.
         */
        public EditTaskDescriptor(EditTaskDescriptor toCopy) {
            setWeekNumber(toCopy.weekNumber);
            setDescription(toCopy.description);
            setCustomizedDeadline(toCopy.customizedDeadline);
            setRemark(toCopy.remark);
        }

        /**
         * Returns true if at least one field is edited.
         */
        public boolean isAnyFieldEdited() {
            return CollectionUtil.isAnyNonNull(//index,
                    weekNumber, description, customizedDeadline, remark);
        }



        public void setWeekNumber(WeekNumber weekNumber) {
            this.weekNumber = weekNumber;
        }

        public Optional<WeekNumber> getWeekNumber() {
            return Optional.ofNullable(weekNumber);
        }

        public void setDescription(Description description) {
            this.description = description;
        }

        public Optional<Description> getDescription() {
            return Optional.ofNullable(description);
        }


        public void setCustomizedDeadline(CustomizedDeadline customizedDeadline) {
            this.customizedDeadline = customizedDeadline;
        }

        public Optional<CustomizedDeadline> getCustomizedDeadline() {

            return Optional.ofNullable(customizedDeadline);
        }

        /**
         * Sets {@code remark} to this object's {@code remark}.
         * A defensive copy of {@code remark} is used internally.
         */
        public void setRemark(Remark remark) {
            this.remark = remark;
        }

        /**
         * Returns an unmodifiable remark, which throws {@code UnsupportedOperationException}
         * if modification is attempted.
         * Returns {@code Optional#empty()} if {@code remark} is null.
         */
        public Optional<Remark> getRemark() {
            return Optional.ofNullable(remark);
        }

        @Override
        public boolean equals(Object other) {
            // short circuit if same object
            if (other == this) {
                return true;
            }

            // instanceof handles nulls
            if (!(other instanceof EditTaskDescriptor)) {
                return false;
            }

            // state check
            EditTaskDescriptor e = (EditTaskDescriptor) other;

            return //getIndex().equals(e.getIndex())
                    getWeekNumber().equals(e.getWeekNumber())
                    && getDescription().equals(e.getDescription())
                    && getCustomizedDeadline().equals(e.getCustomizedDeadline())
                    && getRemark().equals(e.getRemark());
        }

        @Override
        public String toString() {
            return "Week Number :" + weekNumber.value
                    + "\nDescription :" + description.value
                    + "\nCustomized deadline :" + customizedDeadline.value
                    + "\nRemark :" + remark.value;
        }
    }
}
