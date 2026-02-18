package athena.task;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

/**
 * Represents a task with a deadline.
 * A DeadLine object contains a description and a date/time string
 * representing when the task must be completed.
 */
public class Deadline extends Task {
    // rawBy stores the original input (e.g., "2026-01-29 2359") for file storage
    protected String rawBy;

    // displayBy stores the formatted version (e.g., "Jan 29 2026, 11:59 PM") for the user
    protected String displayBy;

    // Added field to store the parsed LocalDateTime object
    protected LocalDateTime dateTimeBy;

    private static final DateTimeFormatter INPUT_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HHmm");
    private static final DateTimeFormatter OUTPUT_FORMAT = DateTimeFormatter.ofPattern("MMM d yyyy, h:mm a");

    /**
     * Constructs a DeadLine task with a description and deadline time.
     * @param description The description of the task.
     * @param by The due date or time for the task, supporting multiple formats.
     */
    public Deadline(String description, String by) {
        super(description);
        this.displayBy = parseFlexible(by);
    }

    /**
     * Parses the input string into a human-readable date-time format.
     * This method attempts to process the input through a three-level fallback system:
     * 1. Attempt to parse as a full date and time (yyyy-MM-dd HHmm).
     * 2. Attempt to parse as a date only (yyyy-MM-dd).
     *
     * @param input The date string provided by the user.
     * @return A formatted string (e.g., "Jan 29 2026, 7:00 PM" or "Jan 29 2026")
     *         if parsing succeeds
     */
    private String parseFlexible(String input) throws DateTimeParseException {
        try {
            // Try Date + Time (yyyy-MM-dd HHmm)
            LocalDateTime dt = LocalDateTime.parse(input, INPUT_FORMAT);
            this.dateTimeBy = dt;
            return dt.format(OUTPUT_FORMAT);
        } catch (DateTimeParseException e1) {
            // Try Date only (yyyy-MM-dd)
            LocalDate d = LocalDate.parse(input);
            this.dateTimeBy = d.atStartOfDay();
            return d.format(DateTimeFormatter.ofPattern("MMM d yyyy"));
        }
    }

    /**
     * Retrieves the deadline's date and time for comparison logic.
     * @return The LocalDateTime representation, or null if it's a raw string.
     */
    public LocalDateTime getBy() {
        return this.dateTimeBy;
    }

    @Override
    public String toFileFormat() {
        // Saves the raw input so the bot can re-process it when reloading
        return "D | " + (isDone ? "X" : "0") + " | " + description + " | " + rawBy;
    }

    @Override
    public String toString() {
        return "[D]" + super.toString() + " (by: " + displayBy + ")";
    }
}
