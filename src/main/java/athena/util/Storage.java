package athena.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import athena.task.Deadline;
import athena.task.Event;
import athena.task.Task;
import athena.task.Todo;

/**
 * Handles the loading and saving of task data to a local file.
 * The Storage class manages all interactions with the physical database file,
 * including creating directories/files if they do not exist and serializing
 * Task objects into a machine-readable format.
 */
public class Storage {
    private final String filePath;

    public Storage(String filePath) {
        this.filePath = filePath;
    }

    /**
     * Loads tasks from the hard drive and returns them as an ArrayList.
     * This method reads the file line by line, identifies the task type (Todo, Deadline, or Event),
     * and reconstructs the task objects. If the file or directory is missing, it
     * handles the initialization gracefully by returning an empty list.
     *
     * @return An ArrayList of tasks loaded from the file.
     * @throws AthenaException If there is an error reading the file or parsing the task data.
     */
    public List<Task> load() throws AthenaException {
        List<Task> loadedTasks = new ArrayList<>();
        File f = new File(filePath);
        if (!f.exists()) {
            return loadedTasks;
        }

        try (Scanner s = new Scanner(f)) {
            while (s.hasNextLine()) {
                String[] parts = s.nextLine().split(" \\| ");
                Task task = parseFileLine(parts);
                if (task != null) {
                    if (parts[1].equals("1")) {
                        task.markAsDone();
                    }
                    loadedTasks.add(task);
                }
            }
        } catch (FileNotFoundException e) {
            throw new AthenaException("File not found.");
        }
        return loadedTasks;
    }

    /**
     * Parses an array of strings representing a task's data into a specific Task object.
     * This method uses a switch statement on the first element of the array to determine
     * the task type:
     * "T" - Creates a Todo using the description at index 2.
     * "D" - Creates a Deadline using the description at index 2 and date at index 3.
     * "E" - Creates an {@link Event} using the description at index 2,
     * start time at index 3, and end time at index 5
     *
     * @param parts An array of strings containing the split data from a storage file line.
     * @return The reconstructed Task object, or null if the task type is unrecognized.
     */
    private Task parseFileLine(String[] parts) {
        assert parts != null : "The parts array from the storage file should not be null";
        assert parts.length >= 3 : "A valid storage line must have at least 3 parts (type, status, description)";
        switch (parts[0]) {
        case "T": return new Todo(parts[2]);
        case "D":
            assert parts.length >= 4 : "Deadline task must have a date/time part at index 3";
            return new Deadline(parts[2], parts[3]);
        case "E":
            assert parts.length >= 5 : "Event task must have from/to parts at indices 3 and 4";
            return new Event(parts[2], parts[3], parts[4]);
        default: return null;
        }
    }

    /**
     * Saves the current task list to the hard drive.
     * This method overwrites the existing file with the current state of the
     * TaskList. Each task is converted into its storage-friendly string
     * format via the toFileFormat() method.
     *
     * @param tasks The list of tasks to be persisted to disk.
     */
    public void save(TaskList tasks) {
        try {
            File f = new File(filePath);
            if (!f.getParentFile().exists()) {
                f.getParentFile().mkdirs();
            }
            FileWriter fw = new FileWriter(f);
            for (Task t : tasks.getTasks()) {
                fw.write(t.toFileFormat() + System.lineSeparator());
            }
            fw.close();
        } catch (IOException e) {
            System.out.println("Error saving file.");
        }
    }
}
