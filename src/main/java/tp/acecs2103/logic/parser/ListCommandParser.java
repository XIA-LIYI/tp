package tp.acecs2103.logic.parser;

import tp.acecs2103.commons.core.Messages;
import tp.acecs2103.logic.commands.ListCommand;
import tp.acecs2103.logic.parser.exceptions.ParseException;
import tp.acecs2103.model.task.WeekNumber;

public class ListCommandParser implements Parser<ListCommand> {
    @Override
    public ListCommand parse(String args) throws ParseException {
        String trimmedArgs = args.trim();
        if (trimmedArgs.equals("")) {
            throw new ParseException(String.format(Messages.MESSAGE_INVALID_COMMAND_FORMAT, ListCommand.MESSAGE_USAGE));
        }
        int weekNumber = ParserUtil.parseWeekNumber(trimmedArgs);
        if (!WeekNumber.isValidWeekNumber(Integer.toString(weekNumber))) {
            throw new ParseException(ParserUtil.MESSAGE_INVALID_WEEKNUMBER);
        }
        return new ListCommand(new WeekNumber(Integer.toString(weekNumber)));
    }
}
