package athena.task;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class Deadline extends Task {
    // rawBy stores the original input (e.g., "2026-01-29 2359") for file storage
    protected String rawBy;
    
    // displayBy stores the formatted version (e.g., "Jan 29 2026, 11:59 PM") for the user
    protected String displayBy;

    private static final DateTimeFormatter INPUT_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HHmm");
    private static final DateTimeFormatter OUTPUT_FORMAT = DateTimeFormatter.ofPattern("MMM d yyyy, h:mm a");

    public Deadline(String description, String by) {
        super(description);
        this.rawBy = by;
        this.displayBy = parseFlexible(by);
    }

    /**
     * Attempts to parse the string into a pretty date/time format.
     */
    private String parseFlexible(String input) {
        try {
            // Try Date + Time (yyyy-MM-dd HHmm)
            LocalDateTime dt = LocalDateTime.parse(input, INPUT_FORMAT);
            return dt.format(OUTPUT_FORMAT);
        } catch (DateTimeParseException e1) {
            try {
                // Try Date only (yyyy-MM-dd)
                LocalDate d = LocalDate.parse(input);
                return d.format(DateTimeFormatter.ofPattern("MMM d yyyy"));
            } catch (DateTimeParseException e2) {
                // Fallback to plain string (e.g., "tonight")
                return input;
            }
        }
    }

    @Override
    public String toFileFormat() {
        // Saves the raw input so the bot can re-process it when reloading
        return "D | " + (isDone ? "1" : "0") + " | " + description + " | " + rawBy;
    }

    @Override
    public String toString() {
        return "[D]" + super.toString() + " (by: " + displayBy + ")";
    }
}