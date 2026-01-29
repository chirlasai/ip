package athena.util;

import athena.task.*;

public class Parser {

    /**
     * Parses the user input and executes the corresponding command.
     *
     * @param fullCommand The raw input string from the user.
     * @param tasks       The TaskList containing current tasks.
     * @param ui          The Ui object for handling user output.
     * @param storage     The Storage object for file operations.
     * @throws DukeException If the command is invalid or arguments are missing.
     */
    public static void parse(String fullCommand, TaskList tasks, Ui ui, Storage storage) throws AthenaException {
        String[] words = fullCommand.split(" ", 2);
        String command = words[0].toLowerCase();

        switch (command) {
            case "list":
                ui.showLine();
                ui.showMessage("Here are the tasks in your list:");
                for (int i = 0; i < tasks.size(); i++) {
                    ui.showMessage((i + 1) + "." + tasks.getTask(i));
                }
                ui.showLine();
                break;

            case "mark":
                handleMarkStatus(words, tasks, ui, storage, true);
                break;

            case "unmark":
                handleMarkStatus(words, tasks, ui, storage, false);
                break;

            case "delete":
                handleDelete(words, tasks, ui, storage);
                break;

            case "todo":
                handleTodo(words, tasks, ui, storage);
                break;

            case "deadline":
                handleDeadline(words, tasks, ui, storage);
                break;

            case "event":
                handleEvent(words, tasks, ui, storage);
                break;

            default:
                throw new AthenaException("I'm sorry, but I don't know what that means."
                    + "Try using 'todo', 'event', 'deadline', 'mark', 'unmark', 'delete' or 'list'"
                );
        }
    }

    private static void handleMarkStatus(String[] words, TaskList tasks, Ui ui, Storage storage, boolean isMark)
            throws AthenaException {
        if (words.length < 2)
            throw new AthenaException("Please specify a task number.");
        try {
            int index = Integer.parseInt(words[1]) - 1;
            if (index < 0 || index >= tasks.size())
                throw new AthenaException("Cannot Mark! Task does not exist.");

            if (isMark)
                tasks.getTask(index).markAsDone();
            else
                tasks.getTask(index).unmark();

            ui.showLine();
            ui.showMessage(
                    isMark ? "Nice! I've marked this task as done:" : "OK, I've marked this task as not done yet:");
            ui.showMessage("  " + tasks.getTask(index));
            ui.showLine();
            storage.save(tasks);
        } catch (NumberFormatException e) {
            throw new AthenaException("The task number must be an integer.");
        }

    }

    private static void handleTodo(String[] words, TaskList tasks, Ui ui, Storage storage) throws AthenaException {
        if (words.length < 2 || words[1].trim().isEmpty())
            throw new AthenaException("The description of a todo task cannot be empty.");
        Task t = new Todo(words[1].trim());
        tasks.addTask(t);
        showAddition(t, tasks, ui);
        storage.save(tasks);
    }

    private static void handleDeadline(String[] words, TaskList tasks, Ui ui, Storage storage) throws AthenaException {
        if (words.length < 2 || !words[1].contains(" /by "))
            throw new AthenaException("Invalid deadline format. " 
                + "Please provide a desciption of task and use /by to set deadline");
        String[] parts = words[1].split(" /by ");
        Task t = new Deadline(parts[0].trim(), parts[1].trim());
        tasks.addTask(t);
        showAddition(t, tasks, ui);
        storage.save(tasks);
    }

    private static void handleEvent(String[] words, TaskList tasks, Ui ui, Storage storage) throws AthenaException {
        if (words.length < 2 || !words[1].contains(" /from ") || !words[1].contains(" /to "))
            throw new AthenaException("Events need a description and a timeline. " 
                +" Please provide a description and use /from and /to to set the duration."
            );
        String[] parts = words[1].split(" /from | /to ");
        Task t = new Event(parts[0].trim(), parts[1].trim(), parts[2].trim());
        tasks.addTask(t);
        showAddition(t, tasks, ui);
        storage.save(tasks);
    }

    private static void handleDelete(String[] words, TaskList tasks, Ui ui, Storage storage) throws AthenaException {
        if (words.length < 2)
            throw new AthenaException("Please specify a task number to delete.");
        try {
            int index = Integer.parseInt(words[1]) - 1;
            Task removed = tasks.deleteTask(index);
            ui.showLine();
            ui.showMessage("Noted. I've removed this task:\n  " + removed);
            ui.showMessage("Now you have " + tasks.size() + " tasks in the list.");
            ui.showLine();
            storage.save(tasks);
        } catch (Exception e) {
            throw new AthenaException("Cannot delete! Task does not exist.");
        }
    }

    private static void showAddition(Task t, TaskList tasks, Ui ui) {
        ui.showLine();
        ui.showMessage("Got it. I've added this task:\n  " + t);
        ui.showMessage("Now you have " + tasks.size() + " tasks in the list.");
        ui.showLine();
    }
}
