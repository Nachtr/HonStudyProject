import java.time.*;

public abstract class Item implements Comparable<Item> {
    private String title;
    private String notes;
    private final Instant createdAt;

    public Item(String title, String notes) {
        this.title = title;
        this.notes = notes;
        this.createdAt = Instant.now();
    }


    // Encap for title
    public String getTitle() {
        return this.title;
    }

    public void setTitle(String newTitle) {
        this.title = newTitle;
    }

    // Encap for notes
    public String getNotes() {
        return this.notes;
    }

    public void setNotes(String newNotes) {
        this.notes = newNotes;
    }

    // Encap for createdAt
    public Instant getCreatedAt() {
        return this.createdAt;
    }

    // Create a abstract to force a sorting key?? Idk if this is the right path or not
    protected abstract long sortKeyEpochSeconds();

    protected abstract String toFlatFileString();


    // Implementing the comp interface
    @Override
    public int compareTo(Item other) {
        return Long.compare(this.sortKeyEpochSeconds(), other.sortKeyEpochSeconds());
    }   

}