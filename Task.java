import java.io.Serializable;
/**
 * Task.java
 *
 * Sends tasks to the server
 *
 * @author Timothy Murray, L11
 *
 * @version December 8 2024
 */
public class Task implements Serializable, TaskInterface {
    private String taskName;

    public Task(String taskName) {
        this.taskName = taskName;
    }

    public String getTask() {
        return taskName;
    }
}
