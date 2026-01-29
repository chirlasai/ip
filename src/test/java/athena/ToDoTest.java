package athena;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import athena.task.Todo;

public class ToDoTest {
    @Test
    public void testToString() {
        // Verify that a new Todo produces the correct string representation
        Todo todo = new Todo("read book");
        assertEquals("[T][ ] read book", todo.toString());
    }

    @Test
    public void testToFileFormat() {
        // Verify that the file storage format is correct
        Todo todo = new Todo("read book");
        assertEquals("T | 0 | read book", todo.toFileFormat());
    }
}
