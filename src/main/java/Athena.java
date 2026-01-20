import java.util.Scanner;

public class Athena {
    public static void main(String[] args) {
        String name = "Athena";
        String line = "________________________________________________________________";
        String[] storage = new String[100];
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
                for (int i = 0; i < itemCount; i++) {
                    System.out.println((i + 1) + ". " + storage[i]);
                }
                System.out.println(line);
            } else {
                storage[itemCount] = input;
                itemCount++;
                System.out.println("added: " + input);
                System.out.println(line);
            }
        }
        
        scanner.close();
    }

       
}
