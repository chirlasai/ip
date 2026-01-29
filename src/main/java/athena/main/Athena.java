package athena.main;

import
        athena.util.AthenaException;
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
    public Athena (String filePath) {
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
     * Runs the main program loop of the chatbot.
     */
    public void run() {
        ui.showWelcome();
        boolean isExit = false;
        while (!isExit) {
            try {
                String fullCommand = ui.readCommand();
                if (fullCommand.equals("bye")) {
                    isExit = true;
                } else {
                    Parser.parse(fullCommand, tasks, ui, storage);
                }
            } catch (AthenaException e) {
                ui.showLine();
                ui.showError(e.getMessage());
                ui.showLine();
            }
        }
        ui.showMessage("Bye. Hope to see you again soon!");
    }

    public static void main(String[] args) {
        new Athena("./data/checklist.txt").run();
    }
}
