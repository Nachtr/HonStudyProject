import java.time.*;

public class Task extends Item implements Schedulable {
    // We use extends because task is-a
    // implement because task provides the schedulables behavior

    private Instant dueDate; // Make sure the following are private
    private int priority; // Creating a system from 1 - 5 for prio
    private boolean completed; // boolean instead of int because I was going to use 0 - 1 anyways

    public Task(String title, String notes, Instant dueDate, int priority) {
        super(title, notes);


        this.dueDate = dueDate;
        this.priority = priority;
        this.completed = false;
    }

    // Encap/get/set
    // dueInstant
    public Instant getDueDate() {
        return this.dueDate;
    }

    public void setDueDate(Instant newDueDate) {
        this.dueDate = newDueDate;
    }

    // prio
    public int getPriority() {
        return this.priority;
    }

    public void setPriority(int newPriority) {
        this.priority = newPriority;
    }

    // comple
    public boolean getCompleted() {
        return this.completed;
    }

    public void setCompleted(boolean newCompleted) {
        this.completed = newCompleted;
    }

    // extends item and implementing the int
    @Override
    public long nextOccurrenceEpochSeconds() {
        if (dueDate == null) {
            return Long.MAX_VALUE;
        } else {
            return dueDate.getEpochSecond();
        }
    }

    @Override
    protected long sortKeyEpochSeconds() {
        if (this.dueDate == null) {
            return Long.MAX_VALUE; 
        } else {
            return this.dueDate.getEpochSecond();
        }
    }
    

    // Make sure to implement toString
}