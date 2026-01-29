package athena.task;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class Event extends Task {
    // rawFrom/rawTo store the exact string the user typed (e.g., "2026-01-29 1400")
    protected String rawFrom;
    protected String rawTo;
    
    // displayFrom/displayTo store the "pretty" version (e.g., "Jan 29 2026, 2:00 PM")
    protected String displayFrom;
    protected String displayTo;

    private static final DateTimeFormatter INPUT_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HHmm");
    private static final DateTimeFormatter OUTPUT_FORMAT = DateTimeFormatter.ofPattern("MMM d yyyy, h:mm a");

    public Event(String description, String from, String to) {
        super(description);
        this.rawFrom = from;
        this.rawTo = to;
        
        // Parse the strings into pretty display formats
        this.displayFrom = parseFlexible(from);
        this.displayTo = parseFlexible(to);
    }

    /**
     * Attempts to parse the input as Date-Time, then Date, then falls back to String.
     */
    private String parseFlexible(String input) {
        try {
            // Level 1: Try Date + Time (yyyy-MM-dd HHmm)
            LocalDateTime dt = LocalDateTime.parse(input, INPUT_FORMAT);
            return dt.format(OUTPUT_FORMAT);
        } catch (DateTimeParseException e1) {
            try {
                // Level 2: Try Date only (yyyy-MM-dd)
                LocalDate d = LocalDate.parse(input);
                return d.format(DateTimeFormatter.ofPattern("MMM d yyyy"));
            } catch (DateTimeParseException e2) {
                // Level 3: Fallback to plain string
                return input;
            }
        }
    }

    @Override
    public String toFileFormat() {
        // Saves raw input so the bot can re-run parsing logic on restart
        return "E | " + (isDone ? "1" : "0") + " | " + description + " | " + rawFrom + " | " + rawTo;
    }

    @Override
    public String toString() {
        return "[E]" + super.toString() + " (from: " + displayFrom + " to: " + displayTo + ")";
    }
}
