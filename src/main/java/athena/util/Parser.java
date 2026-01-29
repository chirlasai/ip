package athena.util;

import athena.task.Deadline;
import athena.task.Event;
import athena.task.Task;
import athena.task.Todo;

/**
 * Handles the interpretation of user input commands.
 * The Parser class contains logic to split user input strings,
 * identify the intended command, and execute the corresponding actions
 * on the task list.
 */
public class Parser {

    /**
     * Parses the user input and executes the corresponding command.
     *
     * @param fullCommand The raw input string from the user.
     * @param tasks       The TaskList containing current tasks.
     * @param ui          The Ui object for handling user output.
     * @param storage     The Storage object for file operations.
     * @throws AthenaException If the command is invalid or arguments are missing.
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

    /**
     * Processes the 'mark' and 'unmark' commands to change a task's completion status.
     * This method validates that a task number is provided and that it falls
     * within the valid range of the current task list. Upon a successful status
     * change, it triggers a UI confirmation and saves the updated list to storage.
     *
     * @param words   The split user input containing the command and the task index.
     * @param tasks   The list of tasks to be modified.
     * @param ui      The user interface for displaying success or error messages.
     * @param storage The storage handler to persist the status change.
     * @param isMark  True if the task should be marked as done, false if it should be unmarked.
     * @throws AthenaException If the task index is missing, not a number, or out of bounds.
     */
    private static void handleMarkStatus(String[] words, TaskList tasks, Ui ui, Storage storage, boolean isMark)
            throws AthenaException {
        if (words.length < 2) {
            throw new AthenaException("Please specify a task number.");
        }
        try {
            int index = Integer.parseInt(words[1]) - 1;
            if (index < 0 || index >= tasks.size()) {
                throw new AthenaException("Cannot Mark! Task does not exist.");
            }

            if (isMark) {
                tasks.getTask(index).markAsDone();
            } else {
                tasks.getTask(index).unmark();
            }

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

    /**
     * Validates and processes a todo command.
     *
     * @param words   The split input containing the command and description.
     * @param tasks   The list to which the new task will be added.
     * @param ui      The UI to confirm the addition to the user.
     * @param storage The storage to persist the new task.
     * @throws AthenaException If the description is empty.
     */
    private static void handleTodo(String[] words, TaskList tasks, Ui ui, Storage storage) throws AthenaException {
        if (words.length < 2 || words[1].trim().isEmpty()) {
            throw new AthenaException("The description of a todo task cannot be empty.");
        }
        Task t = new Todo(words[1].trim());
        tasks.addTask(t);
        showAddition(t, tasks, ui);
        storage.save(tasks);
    }

    /**
     * Validates the input and adds a new deadline task to the list.
     * This method splits the user input using the "/by" delimiter to separate
     * the task description from the deadline time. It ensures both parts are
     * present before creating the task, adding it to the list, and triggering
     * a save to storage.
     *
     * @param words   The array containing the split user input (command and arguments).
     * @param tasks   The TaskList to which the new deadline will be added.
     * @param ui      The Ui object for providing feedback to the user.
     * @param storage The Storage object to persist the new task.
     * @throws AthenaException If the description is empty or if the "/by" delimiter
     * is missing from the input.
     */
    private static void handleDeadline(String[] words, TaskList tasks, Ui ui, Storage storage) throws AthenaException {
        if (words.length < 2 || !words[1].contains(" /by ")) {
            throw new AthenaException("Invalid deadline format. "
                    + "Please provide a desciption of task and use /by to set deadline");
        }
        String[] parts = words[1].split(" /by ");
        Task t = new Deadline(parts[0].trim(), parts[1].trim());
        tasks.addTask(t);
        showAddition(t, tasks, ui);
        storage.save(tasks);
    }

    /**
     * Validates the input and adds a new event task to the list.
     * This method uses multiple delimiters ("/from" and "/to") to extract the
     * task description, start time, and end time. It ensures all three components
     * are present and non-empty before creating the Event object and
     * updating the storage.
     *
     * @param words   The array containing the split user input (command and arguments).
     * @param tasks   The TaskList to which the new event will be added.
     * @param ui      The Ui object for providing feedback to the user.
     * @param storage The Storage object to persist the new task.
     * @throws AthenaException If any of the required parts (description, /from, or /to)
     * are missing or incorrectly formatted.
     */
    private static void handleEvent(String[] words, TaskList tasks, Ui ui, Storage storage) throws AthenaException {
        if (words.length < 2 || !words[1].contains(" /from ") || !words[1].contains(" /to ")) {
            throw new AthenaException("Events need a description and a timeline. "
                    + " Please provide a description and use /from and /to to set the duration."
            );
        }
        String[] parts = words[1].split(" /from | /to ");
        Task t = new Event(parts[0].trim(), parts[1].trim(), parts[2].trim());
        tasks.addTask(t);
        showAddition(t, tasks, ui);
        storage.save(tasks);
    }

    /**
     * Validates the task index and removes the specified task from the list.
     * This method converts the user-provided index into a zero-based integer.
     * It ensures the index is within the valid range of the current task list
     * before deleting the task and updating the persistent storage.
     *
     * @param words   The array containing the split user input (command and index).
     * @param tasks   The TaskList from which the task will be removed.
     * @param ui      The Ui object for providing feedback to the user.
     * @param storage The Storage object to save changes after deletion.
     * @throws AthenaException If the task index is missing, not a valid number,
     * or points to a non-existent task.
     */
    private static void handleDelete(String[] words, TaskList tasks, Ui ui, Storage storage) throws AthenaException {
        if (words.length < 2) {
            throw new AthenaException("Please specify a task number to delete.");
        }
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

    /**
     * Displays a confirmation message to the user after a task is added.
     * This helper method uses the provided Ui object to print a standardized
     * success message, show the task that was just added, and report the new
     * total number of tasks in the TaskList.
     *
     * @param t     The Task that has been successfully added.
     * @param tasks The TaskList used to retrieve the current total count.
     * @param ui    The Ui object responsible for formatting and printing the output.
     */
    private static void showAddition(Task t, TaskList tasks, Ui ui) {
        ui.showLine();
        ui.showMessage("Got it. I've added this task:\n  " + t);
        ui.showMessage("Now you have " + tasks.size() + " tasks in the list.");
        ui.showLine();
    }
}
