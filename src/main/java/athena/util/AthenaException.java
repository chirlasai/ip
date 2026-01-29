package athena.util;

/**
 * Represents exceptions specific to the chatbot application.
 * This class is used to signal errors that are unique to the application's logic,
 * such as invalid user input, formatting errors, or issues with task management.
 */
public class AthenaException extends Exception {
    public AthenaException(String message) {
        super(message);
    }
}