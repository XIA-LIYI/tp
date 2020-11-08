package tp.acecs2103.model.task;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TopicTest {
    private Topic task1 = new Topic(new Index("0101"), new WeekNumber("1"), new Description("Test Task One"),
            new OfficialDeadline("2020-09-10", LocalDate.of(2020, 9, 10)),
            null, new Remark("no remark"), false, false);

    @Test
    void getCategory_null_correctTaskCategory() {
        assertEquals(TaskCategory.TOPIC, task1.getCategory());
    }

}
