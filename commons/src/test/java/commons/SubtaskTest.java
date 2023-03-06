package commons;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class SubtaskTest {

    @Test
    public void checkConstructor(){
        var subtask = new Subtask("Task1", false,0, null);
        assertEquals(subtask.title, "Task1");
        assertEquals(subtask.done, false);
        assertEquals(subtask.index, 0);
        assertNull(subtask.card);
    }

    @Test
    public void equalsHashCode(){
        var subtask1 = new Subtask("Task1", false,0, null);
        var subtask2 = new Subtask("Task1", false,0, null);

        assertEquals(subtask1, subtask2);
        assertEquals(subtask1.hashCode(), subtask2.hashCode());

    }

    @Test
    public void notEqualsHashCode(){
        var subtask1 = new Subtask("Task1", false,0, null);
        var subtask2 = new Subtask("Task2", false,0, null);

        assertNotEquals(subtask1, subtask2);
        assertNotEquals(subtask1.hashCode(), subtask2.hashCode());

    }

    @Test
    public void hasToString(){
        var subtask = new Subtask("Task", false,0, null);

        assertTrue(subtask.toString().contains("Task"));
        assertTrue(subtask.toString().contains("false"));
        assertTrue(subtask.toString().contains("0"));
    }
}