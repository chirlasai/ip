package athena.util;

import java.util.ArrayList;
import java.util.List;

import athena.task.Task;


/**
 * Manages the collection of tasks in the chatbot.
 * The TaskList class provides methods to add, delete, and retrieve tasks.
 * It acts as a wrapper around an ArrayList, abstracting the underlying
 * list operations from the rest of the application.
 */
public class TaskList {
    private final List<Task> tasks;

    public TaskList(List<Task> tasks) {
        this.tasks = tasks;
    }

    /**
     * Adds a task to the task list.
     * @param task The Task to be added.
     */
    public void addTask(Task task) {
        tasks.add(task);
    }

    /**
     * Deletes the task at the specified index from the list.
     * @param index The zero-based index of the task to be removed.
     * @return The Task that was removed from the list.
     * @throws IndexOutOfBoundsException If the index is out of range.
     */
    public Task deleteTask(int index) {
        assert index >= 0 && index < tasks.size() : "Attempted to delete task at invalid index: " + index;
        return tasks.remove(index);
    }

    /**
     * Retrieves the task at the specified index.
     * @param index The zero-based index of the task to retrieve.
     * @return The Task at the specified index.
     * @throws IndexOutOfBoundsException If the index is out of range.
     */
    public Task getTask(int index) {
        assert index >= 0 && index < tasks.size() : "Attempted to access task at invalid index: " + index;
        return tasks.get(index);
    }

    /**
     * Returns the number of tasks in the list.
     * @return The current size of the task list.
     */
    public int size() {
        return tasks.size();
    }

    /**
     * Returns the underlying list of tasks.
     * @return A List containing all Task objects.
     */
    public List<Task> getTasks() {
        return tasks;
    }

    /**
     * Searches for tasks whose descriptions contain the specified keyword.
     *
     * @param keyword The string to search for.
     * @return A new TaskList containing the matching tasks.
     */
    public TaskList findTasks(String keyword) {
        ArrayList<Task> matchingTasks = new ArrayList<>();
        for (Task task : tasks) {
            if (task.toString().contains(keyword)) {
                matchingTasks.add(task);
            }
        }
        return new TaskList(matchingTasks);
    }
}
