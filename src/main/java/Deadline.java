import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class Deadline extends Task {
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
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    /*protected LocalDate dateBy;
    protected String stringBy;

    public Deadline(String description, String by) {
        super(description);
        try {
            this.dateBy = LocalDate.parse(by);
            this.stringBy = null;
        } catch (DateTimeParseException e) {
            this.dateBy = null;
            this.stringBy = by; // Store as old-style string
        }
    }

    @Override
    public String toFileFormat() {
        String time = (dateBy != null) ? dateBy.toString() : stringBy;
        return "D | " + (isDone ? "X" : "0") + " | " + description + " | " + time;
    }

    @Override
    public String toString() {
        String displayTime = (dateBy != null) 
            ? dateBy.format(DateTimeFormatter.ofPattern("MMM d yyyy")) 
            : stringBy;
        return "[D]" + super.toString() + " (by: " + displayTime + ")";
    }
        */
}