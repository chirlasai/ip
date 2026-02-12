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
     * Parses the user input and returns the chatbot's response.
     * This method acts as the primary controller for all commands, routing the input
     * to specific handlers and returning their output for display in the GUI.
     *
     * @param fullCommand The raw input string from the user.
     * @param tasks       The TaskList containing current tasks.
     * @param ui          The Ui object for utility operations.
     * @param storage     The Storage object for file operations.
     * @return The result message of the executed command.
     * @throws AthenaException If the command is invalid or arguments are malformed.
     */
    public static String parse(String fullCommand, TaskList tasks, Ui ui, Storage storage) throws AthenaException {
        String[] words = fullCommand.split(" ", 2);
        String command = words[0].toLowerCase();

        switch (command) {
        case "list":
            StringBuilder sb = new StringBuilder("Here are the tasks in your list:\n");
            for (int i = 0; i < tasks.size(); i++) {
                sb.append((i + 1)).append(".").append(tasks.getTask(i)).append("\n");
            }
            return sb.toString();

        case "todo":
            return handleTodo(words, tasks, ui, storage);

        case "deadline":
            return handleDeadline(words, tasks, ui, storage);

        case "mark":
            return handleMarkStatus(words, tasks, ui, storage, true);

        case "unmark":
            return handleMarkStatus(words, tasks, ui, storage, false);

        case "delete":
            return handleDelete(words, tasks, ui, storage);

        case "event":
            return handleEvent(words, tasks, ui, storage);

        case "find":
            return handleFind(words, tasks, ui);

        default:
            throw new AthenaException("I'm sorry, but I don't know what that means. Use 'list', 'todo', 'deadline', "
                    + "'mark', 'unmark', 'delete', 'event' or 'find' keywords");
        }
    }

    /**
     * Processes the 'mark' and 'unmark' commands to change a task's completion status.
     * This method validates the task index, updates the task's status, saves
     * the change to storage, and returns a confirmation message for the GUI.
     *
     * @param words   The split user input containing the command and the task index.
     * @param tasks   The list of tasks to be modified.
     * @param ui      The user interface handler.
     * @param storage The storage handler to persist the status change.
     * @param isMark  True if the task should be marked as done, false otherwise.
     * @return A success message to be displayed in the GUI.
     * @throws AthenaException If the task index is missing, invalid, or out of bounds.
     */
    private static String handleMarkStatus(String[] words, TaskList tasks, Ui ui, Storage storage, boolean isMark)
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

            storage.save(tasks);

            String statusMessage = isMark
                    ? "Nice! I've marked this task as done:"
                    : "OK, I've marked this task as not done yet:";

            return statusMessage + "\n  " + tasks.getTask(index);
        } catch (NumberFormatException e) {
            throw new AthenaException("The task number must be an integer.");
        }
    }

    /**
     * Validates and processes a 'todo' command.
     *
     * @param words   The split input containing the command and description.
     * @param tasks   The list to which the new task will be added.
     * @param ui      The UI utility object.
     * @param storage The storage to persist the new task.
     * @return A confirmation message indicating the task was added.
     * @throws AthenaException If the description is empty.
     */
    private static String handleTodo(String[] words, TaskList tasks, Ui ui, Storage storage) throws AthenaException {
        if (words.length < 2 || words[1].trim().isEmpty()) {
            throw new AthenaException("The description of a todo task cannot be empty.");
        }
        Task t = new Todo(words[1].trim());
        tasks.addTask(t);
        storage.save(tasks);
        return "Got it. I've added this task:\n  " + t + "\nNow you have " + tasks.size() + " tasks in the list.";
    }

    /**
     * Validates input and creates a new deadline task, returning a success message.
     * This method parses the user input for the "/by" delimiter to separate the
     * task description from the deadline. If valid, it adds the task to the list,
     * persists the change to storage, and returns a formatted confirmation
     * string for the GUI to display.
     *
     * @param words   The array containing the split user input.
     * @param tasks   The TaskList to which the new deadline will be added.
     * @param ui      The Ui utility object.
     * @param storage The Storage object to persist the new task.
     * @return A confirmation message including the added task and the updated list size.
     * @throws AthenaException If the description is empty or the "/by" delimiter is missing.
     */
    private static String handleDeadline(String[] words, TaskList tasks, Ui ui, Storage storage)
            throws AthenaException {
        if (words.length < 2 || !words[1].contains(" /by ")) {
            throw new AthenaException("Invalid deadline format. Please use: deadline [desc] /by [time]");
        }
        String[] parts = words[1].split(" /by ");
        Task t = new Deadline(parts[0].trim(), parts[1].trim());
        tasks.addTask(t);
        storage.save(tasks);
        return "Got it. I've added this task:\n  " + t + "\nNow you have " + tasks.size() + " tasks in the list.";
    }

    /**
     * Validates input and creates a new event task, returning a success message.
     * This method parses the user input using "/from" and "/to" delimiters to
     * extract the description, start time, and end time. If all parts are valid,
     * it adds the event to the list, saves it to storage, and returns a
     * formatted confirmation string for the GUI.
     *
     * @param words   The array containing the split user input.
     * @param tasks   The TaskList to which the new event will be added.
     * @param ui      The Ui utility object.
     * @param storage The Storage object to persist the new task.
     * @return A confirmation message including the added event and the updated list size.
     * @throws AthenaException If any required components (/from, /to, or description)
     *                         are missing or malformed.
     */
    private static String handleEvent(String[] words, TaskList tasks, Ui ui, Storage storage) throws AthenaException {
        if (words.length < 2 || !words[1].contains(" /from ") || !words[1].contains(" /to ")) {
            throw new AthenaException("Invalid event format. Please use: event [desc] /from [start] /to [end]");
        }
        String[] parts = words[1].split(" /from | /to ");
        Task t = new Event(parts[0].trim(), parts[1].trim(), parts[2].trim());
        tasks.addTask(t);
        storage.save(tasks);
        return "Got it. I've added this task:\n  " + t + "\nNow you have " + tasks.size() + " tasks in the list.";
    }

    /**
     * Validates the task index and removes the specified task, returning a confirmation message.
     * This method converts the user-provided index into a zero-based integer and ensures
     * it falls within the valid range of the current task list. If valid, the task
     * is removed, the changes are saved to storage, and a success message is
     * returned for display in the GUI.
     *
     * @param words   The array containing the split user input (command and index).
     * @param tasks   The TaskList from which the task will be removed.
     * @param ui      The Ui utility object.
     * @param storage The Storage object to save changes after deletion.
     * @return A message confirming the deleted task and the new total task count.
     * @throws AthenaException If the task index is missing, not a valid number,
     *                         or refers to a non-existent task.
     */
    private static String handleDelete(String[] words, TaskList tasks, Ui ui, Storage storage) throws AthenaException {
        if (words.length < 2) {
            throw new AthenaException("Please specify a task number to delete.");
        }
        try {
            int index = Integer.parseInt(words[1]) - 1;
            Task removed = tasks.deleteTask(index);
            storage.save(tasks);
            return "Noted. I've removed this task:\n  " + removed
                    + "\nNow you have " + tasks.size() + " tasks in the list.";
        } catch (Exception e) {
            throw new AthenaException("Cannot delete! Task does not exist.");
        }
    }

    /**
     * Processes the 'find' command to search for tasks by keyword and returns the results.
     * This method extracts the search keyword, queries the TaskList for matching
     * entries, and returns a formatted list of results. If no matches are found,
     * a specific message is returned to inform the user.
     *
     * @param words   The split user input containing the command and keyword.
     * @param tasks   The full TaskList to search within.
     * @param ui      The Ui utility object.
     * @return A formatted string of matching tasks or a "no results found" message.
     * @throws AthenaException If no keyword is provided for the search.
     */
    private static String handleFind(String[] words, TaskList tasks, Ui ui) throws AthenaException {
        if (words.length < 2 || words[1].trim().isEmpty()) {
            throw new AthenaException("The search keyword cannot be empty.");
        }
        String keyword = words[1].trim();
        TaskList results = tasks.findTasks(keyword);

        if (results.size() == 0) {
            return "No matching tasks found in your list.";
        }

        StringBuilder sb = new StringBuilder("Here are the matching tasks in your list:\n");
        for (int i = 0; i < results.size(); i++) {
            sb.append((i + 1)).append(".").append(results.getTask(i)).append("\n");
        }
        return sb.toString();
    }
}
