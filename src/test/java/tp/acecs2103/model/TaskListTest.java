package tp.acecs2103.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static tp.acecs2103.testutil.Assert.assertThrows;
import static tp.acecs2103.testutil.TypicalTasks.AdminOne;
import static tp.acecs2103.testutil.TypicalTasks.getTypicalTaskList;

import java.util.*;

import org.junit.jupiter.api.Test;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import tp.acecs2103.model.task.Admin;
import tp.acecs2103.model.task.Remark;
import tp.acecs2103.model.task.Task;
import tp.acecs2103.testutil.TaskBuilder;

public class TaskListTest {

    private final TaskList taskList = new TaskList();

    @Test
    public void constructor() {
        assertEquals(new ArrayList<>(), taskList.getTaskList());
    }

    @Test
    public void resetData_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> taskList.resetData(null));
    }

    @Test
    public void resetData_withValidReadOnlyTaskList_replacesData() {
        TaskList newData = getTypicalTaskList();
        taskList.resetData(newData);
        assertEquals(newData, taskList);
    }

    @Test
    public void hasTask_taskNotInTaskList_returnsFalse() {
        assertFalse(taskList.hasTask(AdminOne));
    }

    @Test
    public void hasTask_taskInTaskList_returnsTrue() {
        taskList.add(AdminOne);
        assertTrue(taskList.hasTask(AdminOne));
    }

    @Test
    public void hasTask_taskWithSameIdentityFieldsInTaskList_returnsTrue() {
        taskList.add(AdminOne);
        Task editedAdminOne = new TaskBuilder(AdminOne).buildAdmin();
        assertTrue(taskList.hasTask(editedAdminOne));
    }

}
