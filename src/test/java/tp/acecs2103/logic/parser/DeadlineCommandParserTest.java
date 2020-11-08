package tp.acecs2103.logic.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;

import tp.acecs2103.logic.commands.DeadlineCommand;
import tp.acecs2103.logic.parser.exceptions.ParseException;
import tp.acecs2103.model.task.CustomizedDeadline;
import tp.acecs2103.model.task.Index;

public class DeadlineCommandParserTest {
    @Test
    public void parse_validDeadlineCommand_success() {
        DeadlineCommandParser deadlineCommandParser = new DeadlineCommandParser();
        String parametersStub = " i/0101 c/2020-08-17";
        DeadlineCommand expected = new DeadlineCommand(
                new Index("0101"), new CustomizedDeadline("2020-08-17", LocalDate.parse("2020-08-17")));
        try {
            assertEquals(expected, deadlineCommandParser.parse(parametersStub));
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
}
