import java.util.Scanner;
import java.util.List;
import java.util.ArrayList;

public class Athena {
    public static void main(String[] args) {
        String name = "Athena";
        String line = "________________________________________________________________";
        List<Task> tasks = new ArrayList<>();
        // greetings
        System.out.println(line);
        System.out.println("Hello! I'm " + name);
        System.out.println("What can I do for you?");
        System.out.println(line);
        // input reading and response
        Scanner scanner = new Scanner(System.in);

        while (true) {
            String input = scanner.nextLine(); // Read user input

            System.out.println(line);

            if (input.equals("bye")) {
                System.out.println("Bye. Hope to see you again soon!");
                System.out.println(line);
                break; // Exit the loop
            }
            try {
                processCommand(input, tasks, line);
            } catch (AthenaException e) {
                System.out.println(" ERROR: " + e.getMessage() + "\n" + line);
            }
        }
        scanner.close();
    }

    public static void processCommand(String input, List<Task> tasks, String line) throws AthenaException {
        System.out.println(line);
        if (input.equals("list")) {
            System.out.println("Here are the tasks in your list:");
            for (int i = 0; i < tasks.size(); i++) {
                System.out.println((i + 1) + "." + tasks.get(i));
            }
        } else if (input.startsWith("todo")) {
            if (input.trim().equals("todo")) {
                throw new AthenaException("A 'todo' needs a description!");
            }
            Task newTask = new Todo(input.substring(5).trim());
            tasks.add(newTask);
            printAddition(newTask, tasks.size(), line);
        } else if (input.startsWith("deadline")) {
            if (input.trim().equals("deadline")) {
                throw new AthenaException("Deadlines need details!");
            }
            if (!input.contains(" /by ")) {
                throw new AthenaException("Missing the deadline! Use /by to specify when it's due.");
            }
            String[] parts = input.substring(9).split(" /by ");
            Task newTask = new Deadline(parts[0].trim(), parts[1].trim());
            tasks.add(newTask);
            printAddition(newTask, tasks.size(), line);
        } else if (input.startsWith("event")) {
            if (input.trim().equals("event")) {
                throw new AthenaException("Events can't be empty!");
            }
            // Basic check for /from and /to
            if (!input.contains("/from") || !input.contains("/to")) {
                throw new AthenaException("Events need a timeline. Use /from and /to to set the duration.");
            }
            String[] parts = input.substring(6).split(" /from | /to ");
            Task newTask = new Event(parts[0].trim(), parts[1].trim(), parts[2].trim());
            tasks.add(newTask);
            printAddition(newTask, tasks.size(), line);
        } else if (input.startsWith("mark")) {
            handleMarkStatus(input, tasks, line, true);
        } else if (input.startsWith("unmark")) {
            handleMarkStatus(input, tasks, line, false);
        } else if (input.startsWith("delete")) {
            handleDelete(input, tasks, line);
        } else {
            // If the command is not recognized
            throw new AthenaException("I don't recognize that command. Try 'todo', 'deadline', 'event', 'list', 'mark', 'unmark' or 'delete'");
        }
        System.out.println(line);
    }
    
    // helper functions for marking, deleting and printing
    public static void handleMarkStatus(String input, List<Task> tasks, String line, boolean isMark)
            throws AthenaException {
        String[] parts = input.split(" ");
        if (parts.length < 2) {
            throw new AthenaException("Please specify a task number to " + (isMark ? "mark" : "unmark"));
        }
        try {
            int index = Integer.parseInt(parts[1]) - 1;
            if (index < 0 || index >= tasks.size()) {
                throw new AthenaException("Index " + (index + 1) + " is out of range.");
            }
            if (isMark) {
                tasks.get(index).markAsDone();
                System.out.println("Nice! I've marked this task as done:\n  " + tasks.get(index));
            } else {
                tasks.get(index).unmark();
                System.out.println("OK, I've marked this task as not done yet:\n  " + tasks.get(index));
            }
            System.out.println(line);
        } catch (NumberFormatException e) {
            throw new AthenaException("Invalid index format. Please provide a numeric value.");
        }
    }

    public static void handleDelete(String input, List<Task> tasks, String line) throws AthenaException {
        String[] parts = input.split(" ");
        if (parts.length < 2) {
            throw new AthenaException("The delete command requires a task index.");
        }
        try {
            int index = Integer.parseInt(parts[1]) - 1;
            if (index < 0 || index >= tasks.size()) {
                throw new AthenaException("Cannot delete! Index " + (index + 1) + " does not exist.");
            }
            Task removedTask = tasks.remove(index);
            System.out.println("Noted. I've removed this task:\n  " + removedTask);
            System.out.println("Now you have " + tasks.size() + " tasks in the list.");
            System.out.println(line);
        } catch (NumberFormatException e) {
            throw new AthenaException("The delete index must be a valid integer.");
        }
    }

    public static void printAddition(Task task, int size, String line) {
        System.out.println("Got it. I've added this task:\n  " + task);
        System.out.println("Now you have " + size + " tasks in the list.");
        System.out.println(line);
    }
}
