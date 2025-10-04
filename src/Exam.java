import java.time.*;

public class Exam extends Item implements Schedulable {
    private String course;
    private Instant startAt;
    private Instant endAt;

    public Exam(String title, String notes, String course, Instant startAt, Instant endAt) {
        super(title, notes);

        this.course = course;
        this.startAt = startAt;
        this.endAt = endAt;
    }

    @Override
    public long nextOccurrenceEpochSeconds() {
        if (startAt == null) {
            return Long.MAX_VALUE;
        } else {
            return startAt.getEpochSecond();
        }
    }

    @Override
    protected long sortKeyEpochSeconds() {
        if (this.startAt == null) {
            return Long.MAX_VALUE; 
        } else {
            return this.startAt.getEpochSecond();
        }
    }
}
