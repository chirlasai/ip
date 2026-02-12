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

    public void addTask(Task task) {
        tasks.add(task);
    }

    public Task deleteTask(int index) {
        assert index >= 0 && index < tasks.size() : "Attempted to delete task at invalid index: " + index;
        return tasks.remove(index);
    }

    public Task getTask(int index) {
        assert index >= 0 && index < tasks.size() : "Attempted to access task at invalid index: " + index;
        return tasks.get(index);
    }

    public int size() {
        return tasks.size();
    }

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
