import athena.util.*;

public class Athena {
    private Storage storage;
    private TaskList tasks;
    private Ui ui;

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
