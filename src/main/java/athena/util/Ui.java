package athena.util;

import java.util.Scanner;

/**
 * Handles all interactions with the user.
 * The Ui class is responsible for displaying messages to the user
 * and reading their input. It manages the formatting of the chatbot's
 * responses, including the display of welcome and farewell messages.
 */
public class Ui {
    private final String line = "________________________________________________________________";
    private final Scanner scanner = new Scanner(System.in);

    public void showWelcome() {
        System.out.println(line + "\nHello! I'm Athena\nWhat can I do for you?\n" + line);
    }

    public String readCommand() {
        return scanner.nextLine().trim();
    }

    public void showLine() {
        System.out.println(line);
    }

    public void showError(String message) {
        System.out.println(" ERROR: " + message);
    }

    public void showMessage(String message) {
        System.out.println(message);
    }
}
