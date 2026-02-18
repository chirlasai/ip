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

    public String getWelcomeMessage() {
        String welcome = "Hello! I'm Athena.\nHow can I help you today?";
        String reminders = Parser.handleReminders(this.tasks); // Static call to your new logic
        return welcome + "\n\n" + reminders;
    }

    /**
     * Processes the user's input and returns a response string from the chatbot.
     * This method acts as the primary interface between the JavaFX GUI and the
     * chatbot logic. It handles the termination command "bye" specifically and
     * delegates all other command processing to the Parser.
     *
     * @param input The raw text input entered by the user in the GUI.
     * @return A string representing the chatbot's response, including error messages
     *         if an AthenaException occurs.
     */
    public String getResponse(String input) {
        try {
            if (input.equalsIgnoreCase("bye") || input.equalsIgnoreCase("Bye")) {
                return "Bye. Hope to see you again soon!";
            }
            return Parser.parse(input, tasks, ui, storage);
        } catch (AthenaException e) {
            return "ERROR: " + e.getMessage();
        }
    }
}
