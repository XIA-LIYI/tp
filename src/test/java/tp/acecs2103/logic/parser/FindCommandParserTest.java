package tp.acecs2103.logic.parser;

import org.junit.jupiter.api.Test;
import tp.acecs2103.logic.commands.DoneCommand;
import tp.acecs2103.logic.commands.FindCommand;
import tp.acecs2103.logic.parser.exceptions.ParseException;
import tp.acecs2103.model.task.Index;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class FindCommandParserTest {
    @Test
    public void parse_validFindCommand_success() {
        FindCommandParser findCommandParser = new FindCommandParser();
        String parametersStub = "testtest";
        FindCommand expected = new FindCommand("testtest");
        try {
            assertEquals(expected, findCommandParser.parse(parametersStub));
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void parse_invalidFindCommand_emptyKeyword_fail() {
        FindCommandParser findCommandParser = new FindCommandParser();
        String parametersStub = "";
        try {
            findCommandParser.parse(parametersStub);
        } catch (ParseException e) {
            assert true;
        }
    }
}
