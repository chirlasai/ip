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
            } else if (input.equals("list")) {
                System.out.println("Here are the tasks in your list:");
                for (int i = 0; i < itemCount; i++) {
                    System.out.println((i + 1) + ". " + tasks[i].toString());
                }
            } else if (input.startsWith("mark ")) {
                int index = Integer.parseInt(input.substring(5)) - 1;
                tasks[index].markAsDone();
                System.out.println("Nice! I've marked this task as done:\n  " + tasks[index]);
            } else if (input.startsWith("unmark ")) {
                int index = Integer.parseInt(input.substring(7)) - 1;
                tasks[index].unmark();
                System.out.println("OK, I've marked this task as not done yet:\n  " + tasks[index]);
            } else {
                tasks[itemCount] = new Task(input);
                itemCount++;
                System.out.println("added: " + input);
            }
                System.out.println(line);
            }
        
        scanner.close();
    }
}
