package athena;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.ArrayList;

import org.junit.jupiter.api.Test;

import athena.util.AthenaException;
import athena.util.Parser;
import athena.util.Storage;
import athena.util.TaskList;
import athena.util.Ui;




public class ParserTest {
    @Test
    public void parse_deadlineCommand_success() throws AthenaException {
        TaskList tasks = new TaskList(new ArrayList<>());
        Ui ui = new Ui();
        Storage storage = new Storage("./data/test.txt");

        // Testing non-trivial string splitting and task addition
        Parser.parse("deadline return book /by 2026-12-01", tasks, ui, storage);
        assertEquals(1, tasks.size());
        assertEquals("[D][ ] return book (by: Dec 1 2026)", tasks.getTask(0).toString());
    }

    @Test
    public void parse_deadlineMissingBy_exceptionThrown() {
        TaskList tasks = new TaskList(new ArrayList<>());
        Ui ui = new Ui();
        Storage storage = new Storage("./data/test.txt");

        // Testing error handling for invalid user input
        AthenaException thrown = assertThrows(AthenaException.class, () -> {
            Parser.parse("deadline return book", tasks, ui, storage);
        });
        assertEquals("Invalid deadline format. Please provide a desciption of task and use /by to set deadline",
                thrown.getMessage()
        );
    }
}
