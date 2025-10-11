import java.io.*;
import java.time.Instant;

public class FlatFileStore {
    private File filePath;

    public FlatFileStore(String path) {
        this.filePath = new File(path);
    }

    // Write the save method. Should write each item to the file...
    public void save(SimpleLinkedList<Item> items) {
        try(PrintWriter writer = new PrintWriter(new FileWriter(filePath))) {
            Item[] itemArray = new Item[items.size()];
            itemArray = items.toArray(itemArray);

            for (Item item : itemArray) {
                writer.println(item.toFlatFileString());
            }
        } catch (IOException e) {
            System.err.println("Error saving data: " + e.getMessage()); // Less comments through the above part because I had to work to fix the bugs through here.
        }
    }

    // Write the load method. This should rebuild the items objects one by one
    public SimpleLinkedList<Item> load() {
        SimpleLinkedList<Item> loadedItems = new SimpleLinkedList<>();

        if (!filePath.exists()) {
            return loadedItems;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) { 
            String line;

            // Read line by line
            while ((line = reader.readLine()) != null) {
                if (line.isBlank()) continue;

                String[] parts = line.split("\\|"); // simple regex like what we would use in python
                long dueEpoch, startEpoch, endEpoch;
                Instant dueAt, startAt, endAt;
                int priority, points;
                boolean completed;

                String itemType = parts[0].trim();

                switch(itemType) {
                    case "TASK":
                        // We are checking lengths and format
                        if (parts.length != 6) {
                            System.err.println("Warning: Skipping malformed? TASK line... : " + line);
                            continue;
                        }

                        dueEpoch = Long.parseLong(parts[3]);
                        dueAt = (dueEpoch == Long.MAX_VALUE) ? null : Instant.ofEpochSecond(dueEpoch);
                        priority = Integer.parseInt(parts[4]);
                        completed = Boolean.parseBoolean(parts[5]);

                        // Bugfix: Match the constructor from before

                        Task task = new Task(
                            parts[1].trim(), // title : Why still error?
                            parts[2].trim(), // notes
                            dueAt,   // dueDate (Instant)
                            priority // priority (int)
                        );

                        task.setCompleted(completed);
                        loadedItems.addLast(task);
                        break;
                    case "ASSIGNMENT":
                        // Basically a lot of the same stuff
                        if (parts.length != 6) {
                            System.err.println("Warning: Skipping malformed? ASSIGNMENT line... : " + line);
                            continue;
                        }

                        dueEpoch = Long.parseLong(parts[4]);
                        dueAt = (dueEpoch == Long.MAX_VALUE) ? null : Instant.ofEpochSecond(dueEpoch);
                        points = Integer.parseInt(parts[5]);

                        loadedItems.addLast(new Assignment (
                            parts[1].trim(), // Title
                            parts[2].trim(), // Notes
                            parts[3].trim(), // Course
                            dueAt,
                            points
                        ));
                        break;
                    case "EXAM":
                        if (parts.length != 6) {
                            System.err.println("Warning: Skipping malformed? EXAM line... : " + line);
                            continue;
                        }

                        startEpoch = Long.parseLong(parts[4]);
                        endEpoch = Long.parseLong(parts[5]);

                        startAt = (startEpoch == Long.MAX_VALUE) ? null : Instant.ofEpochSecond(startEpoch);
                        endAt = (endEpoch == Long.MAX_VALUE) ? null : Instant.ofEpochSecond(endEpoch);

                        loadedItems.addLast(new Exam(
                            parts[1].trim(),
                            parts[2].trim(),
                            parts[3].trim(),
                            startAt,
                            endAt
                        ));
                        break;
                }
            }
        } catch (IOException | NumberFormatException e) {
            System.err.println("Error loading selected data: " + e.getMessage());
        }

        return loadedItems;
    }

    public String getFilePath() {
        return filePath.getAbsolutePath();
    }
}