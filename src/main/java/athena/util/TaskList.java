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
    public List findTasks(String keyword) {
        List<Integer> matchingIndices = new ArrayList<>();
        for (int i = 0; i < tasks.size(); i++) {
            if (tasks.get(i).toString().contains(keyword)) {
                matchingIndices.add(i);
            }
        }
        return matchingIndices;
    }

    /**
     * Filters the list for tasks occurring within a specified number of days from now.
     * This method checks Deadlines by their 'by' date and Events by their 'from' date.
     * It uses assertions to ensure the time window is valid for internal logic.
     * @param days The number of days ahead to look for reminders.
     * @return A new TaskList containing tasks occurring soon.
     */
    public TaskList getReminders(int days) {
        assert days >= 0 : "Reminder window cannot be negative"; // A-Assertions

        java.time.LocalDateTime now = java.time.LocalDateTime.now();
        java.time.LocalDateTime limit = now.plusDays(days);
        java.util.ArrayList<Task> upcoming = new java.util.ArrayList<>();

        for (athena.task.Task task : tasks) {
            if (task instanceof athena.task.Deadline) {
                athena.task.Deadline d = (athena.task.Deadline) task;
                // Check if the deadline falls between now and the limit
                if (d.getBy().isBefore(limit) && d.getBy().isAfter(now)) {
                    upcoming.add(task);
                }
            } else if (task instanceof athena.task.Event) {
                athena.task.Event e = (athena.task.Event) task;
                // Events are relevant if their start time ('from') is approaching
                if (e.getFrom().isBefore(limit) && e.getFrom().isAfter(now)) {
                    upcoming.add(task);
                }
            }
        }
        return new TaskList(upcoming);
    }
}
