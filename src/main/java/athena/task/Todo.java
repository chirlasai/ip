package athena.task;

/**
 * Represents a basic task without any specific date or time attached to it.
 * A Todo object is the simplest task type, consisting only of
 * a description and a completion status.
 */
public class Todo extends Task {
    public Todo(String description) {
        super(description);
    }

    @Override
    public String toFileFormat() {
        return "T | " + (isDone ? "X" : "0") + " | " + description;
    }

    @Override
    public String toString() {
        return "[T]" + super.toString();
    }
}
