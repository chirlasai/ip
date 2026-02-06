package athena.util;

/**
 * Handles the welcome message and error messages
 */
public class Ui {

    public static String getWelcomeMessage() {
        return "Hello! I'm Athena\nWhat can I do for you?";
    }

    public void showError(String message) {
        System.out.println(" ERROR: " + message);
    }
}


