import java.util.Scanner;

public class Athena {
    public static void main(String[] args) {
        String name = "Athena";
        String line = "________________________________________________________________";
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
            } else {
                System.out.println(input);
                System.out.println(line);
            }
        }
        
        scanner.close();
    }

       
}
