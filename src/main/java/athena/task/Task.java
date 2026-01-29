package athena.task;

/**
 * Represents a generic task in the chatbot.
 * This is an abstract class that serves as the parent for all specific task types.
 * It encapsulates common functionality such as tracking the task description
 * and whether the task has been completed.
 */
public abstract class Task {
    protected String description;
    protected boolean isDone;

    /**
     * Constructs a Task with the specified description
     * When a task is first created, its completion status isDone
     * is initialized to false by default.
     *
     * @param description A brief explanation or name of the task.
     */
    public Task(String description) {
        this.description = description;
        this.isDone = false;
    }

    public String getStatusIcon() {
        return (isDone ? "X" : " "); // mark done task with X
    }

    public void markAsDone() {
        this.isDone = true;
    }

    public void unmark() {
        this.isDone = false;
    }

    public abstract String toFileFormat();

    @Override
    public String toString() {
        return "[" + getStatusIcon() + "] " + description;
    }
}
