package athena.task;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

/**
 * Represents an event task.
 * An Event object includes a description and two time strings:
 * one for the start time and one for the end time.
 */
public class Event extends Task {
    // rawFrom/rawTo store the exact string the user typed (e.g., "2026-01-29 1400")
    protected String rawFrom;
    protected String rawTo;
    // displayFrom/displayTo store the "pretty" version (e.g., "Jan 29 2026, 2:00 PM")
    protected String displayFrom;
    protected String displayTo;
    protected LocalDateTime startDateTime;

    private static final DateTimeFormatter INPUT_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HHmm");
    private static final DateTimeFormatter OUTPUT_FORMAT = DateTimeFormatter.ofPattern("MMM d yyyy, h:mm a");

    /**
     * Constructs an Event task with a description, start time, and end time.
     * Both the from and to parameters are processed flexibly:
     * the constructor attempts to parse them as date-times (yyyy-MM-dd HHmm) or
     * dates (yyyy-MM-dd). If parsing fails for either, that specific value is
     * stored and displayed as a raw string.
     *
     * @param description The description of the event.
     * @param from The start date or time of the event, supporting multiple formats.
     * @param to The end date or time of the event, supporting multiple formats.
     */
    public Event(String description, String from, String to) {
        super(description);
        this.rawFrom = from;
        this.rawTo = to;
        // Parse the strings into pretty display formats
        this.displayFrom = parseFlexible(from);
        this.displayTo = parseFlexible(to);
    }

    /**
     * Processes a raw string into a formatted date-time string if it matches a known pattern.
     * This method attempts to parse the input in the following order:
     * Tries to parse as a date-time (yyyy-MM-dd HHmm).
     * If that fails, tries to parse as a date only (yyyy-MM-dd).
     *
     * @param input The raw input string provided by the user (e.g., "2026-01-29 1800" or "Monday").
     * @return A human-readable formatted date/time string (e.g., "Jan 29 2026, 6:00 PM")
     *         if parsing succeeds
     */
    private String parseFlexible(String input) throws DateTimeParseException {
        try {
            // Level 1: Try Date + Time (yyyy-MM-dd HHmm)
            LocalDateTime dt = LocalDateTime.parse(input, INPUT_FORMAT);
            this.startDateTime = dt;
            return dt.format(OUTPUT_FORMAT);
        } catch (DateTimeParseException e1) {
            // Level 2: Try Date only (yyyy-MM-dd)
            LocalDate d = LocalDate.parse(input);
            this.startDateTime = d.atStartOfDay();
            return d.format(DateTimeFormatter.ofPattern("MMM dd yyyy"));
        }
    }



    /**
     * Retrieves the event's start date and time for comparison logic.
     * @return The LocalDateTime representation, or null if parsing failed.
     */
    public LocalDateTime getFrom() {
        return this.startDateTime;
    }

    @Override
    public String toFileFormat() {
        // Saves raw input so the bot can re-run parsing logic on restart
        return "E | " + (isDone ? "X" : "0") + " | " + description + " | " + rawFrom + " | " + rawTo;
    }

    @Override
    public String toString() {
        return "[E]" + super.toString() + " (from: " + displayFrom + " to: " + displayTo + ")";
    }
}
