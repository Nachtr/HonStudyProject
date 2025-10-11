import java.time.*;

public class Assignment extends Item implements Schedulable {
    private String course;
    private Instant dueDate; // I cannot write instant for the life of me ;(
    private int points;

    public Assignment(String title, String notes, String course, Instant dueDate, int points) {
        super(title, notes);

        this.course = course;
        this.dueDate = dueDate;
        this.points = points;
    }

    // Getters and setters for course; duedate; points
    // course
    public String getCourse() {
        return this.course;
    }

    public void setCourse(String newCourse) {
        this.course = newCourse;
    }

    // dueDate
    public Instant getDueDate() {
        return this.dueDate;
    }

    public void setDueDate(Instant newdueDate) {
        this.dueDate = newdueDate;
    }

    // points
    public int getPoints() {
        return this.points;
    }

    public void setPoints(int newPoints) {
        this.points = newPoints;
    }

    // Implementing and extending
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

    protected String toFlatFileString() {
    long dueEpoch = (getDueDate() != null) ? getDueDate().getEpochSecond() : 0;
    
    return "ASSIGNMENT|" + getTitle() + 
           "|" + getNotes() + 
           "|" + getCourse() + 
           "|" + dueEpoch + 
           "|" + getPoints();
}

    

}
