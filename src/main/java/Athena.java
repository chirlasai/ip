import java.util.Scanner;

public class Athena {
    public static void main(String[] args) {
        String name = "Athena";
        String line = "________________________________________________________________";
        Task[] tasks = new Task[100];
        int itemCount = 0;
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
                processCommand(input, tasks, itemCount, line);
                // Note: We need to track itemCount outside this method or make it a class
                // variable
                if (!input.equals("list") && !input.startsWith("mark") && !input.startsWith("unmark")) {
                    itemCount++;
                }
            } catch (AthenaException e) {
                System.out.println("OOPS!!! " + e.getMessage() + "\n" + line);
            }
        }
        scanner.close();
    }

    public static void processCommand(String input, Task[] tasks, int itemCount, String line) throws AthenaException {
        System.out.println(line);
        if (input.equals("list")) {
            System.out.println("Here are the tasks in your list:");
            for (int i = 0; i < itemCount; i++) {
                System.out.println((i + 1) + "." + tasks[i]);
            }
        } else if (input.startsWith("todo")) {
            if (input.length() <= 5) {
                throw new AthenaException("A 'todo' needs a description!");
            }
            tasks[itemCount] = new Todo(input.substring(5));
            System.out.println("Got it. I've added this task:\n  " + tasks[itemCount]);
        } else if (input.startsWith("deadline")) {
            if (input.length() <= 9) {
                throw new AthenaException("Deadlines need details!");
            }
            String[] parts = input.substring(9).split(" /by ");
            if (parts.length < 2) {
                throw new AthenaException("Missing the deadline! Use /by to specify when it's due.");
            }
            tasks[itemCount] = new Deadline(parts[0], parts[1]);
            System.out.println("Got it. I've added this task:\n  " + tasks[itemCount]);
        } else if (input.startsWith("event")) {
            if (input.length() <= 6) {
                throw new AthenaException("Events can't be empty!");
            }
            // Basic check for /from and /to
            if (!input.contains("/from") || !input.contains("/to")) {
                throw new AthenaException("Events need a timeline. Use /from and /to to set the duration.");
            }
            String[] parts = input.substring(6).split(" /from | /to ");
            tasks[itemCount] = new Event(parts[0], parts[1], parts[2]);
            System.out.println("Got it. I've added this task:\n  " + tasks[itemCount]);
        } else if (input.startsWith("mark") || input.startsWith("unmark")) {
            handleMarkStatus(input, tasks, itemCount, true);
        } else if (input.startsWith("unmark")) {
            handleMarkStatus(input, tasks, itemCount, false);
        } else {
            // If the command is not recognized
            throw new AthenaException("I don't recognize that command. Try 'todo', 'deadline', 'event', or 'list'.");
        }
        System.out.println(line);
    }

    private static void handleMarkStatus(String input, Task[] tasks, int itemCount, boolean isMark) throws AthenaException {
        // Error: User just typed "mark" or "unmark" without a number
        String[] parts = input.split(" ");
        if (parts.length < 2) {
            throw new AthenaException("Please specify a task number to " + (isMark ? "mark." : "unmark."));
        }

        try {
            int index = Integer.parseInt(parts[1]) - 1;

            // Error: Task number is out of bounds (e.g., mark 100 when only 3 tasks exist)
            if (index < 0 || index >= itemCount) {
                throw new AthenaException("Task number " + (index + 1) + " does not exist in your list.");
            }

            if (isMark) {
                tasks[index].markAsDone();
                System.out.println("Nice! I've marked this task as done:\n  " + tasks[index]);
            } else {
                tasks[index].unmark();
                System.out.println("OK, I've marked this task as not done yet:\n  " + tasks[index]);
            }
        } catch (NumberFormatException e) {
            // Error: User typed "mark abc" instead of "mark 1"
            throw new AthenaException("The task number must be a valid integer.");
        }
    }

}
