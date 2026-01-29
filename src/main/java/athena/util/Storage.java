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

    private Task parseFileLine(String[] parts) {
        switch (parts[0]) {
        case "T": return new Todo(parts[2]);
        case "D": return new Deadline(parts[2], parts[3]);
        case "E": return new Event(parts[2], parts[3], parts[4]);
        default: return null;
        }
    }

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
