package athena.main;

import athena.util.AthenaException;
import athena.util.Parser;
import athena.util.Storage;
import athena.util.TaskList;
import athena.util.Ui;

/**
 * Main class of chatbot.
 */
public class Athena {
    private Storage storage;
    private TaskList tasks;
    private Ui ui;

    /**
     * constructor for Athena class.
     * @param filePath address of where data is stored
     */
    public Athena(String filePath) {
        ui = new Ui();
        storage = new Storage(filePath);
        try {
            tasks = new TaskList(storage.load());
        } catch (AthenaException e) {
            ui.showError(e.getMessage());
            tasks = new TaskList(new java.util.ArrayList<>());
        }
    }

    /**
     * New method for JavaFX to call.
     */
    public String getResponse(String input) {
        try {
            if (input.equalsIgnoreCase("bye")) {
                return "Bye. Hope to see you again soon!";
            }
            return Parser.parse(input, tasks, ui, storage);
        } catch (AthenaException e) {
            return "ERROR: " + e.getMessage();
        }
    }
}
