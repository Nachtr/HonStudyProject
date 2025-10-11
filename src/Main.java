import java.io.File;
import java.io.IOException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

// Last part. Basically like wiring a circuit

public class Main {
    private SimpleLinkedList<Item> allItems;
    private Scanner scanner;
    private FlatFileStore store;

    // Create a con
    public Main() {
        this.allItems = new SimpleLinkedList<>();
        this.scanner = new Scanner(System.in);
        this.store = new FlatFileStore("data/planner_data.txt"); // file name for now
    }

    // Creating the utility methods
    private Instant readInstant(String prompt) {
        System.out.print(prompt + " (yyyy-MM-dd HH:mm): ");
        String dtString = scanner.nextLine();


        try {
        // define local format
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
            LocalDateTime localDateTime = LocalDateTime.parse(dtString, formatter);
            return localDateTime.atZone(ZoneId.systemDefault()).toInstant();
        } catch (Exception e) {
            System.err.println("Invalid data/time format. Attempting with current time.");
            return Instant.now();
        }
    }

    // Helper metho for reading an integer with val
    private int readInt(String prompt) {
        System.out.print(prompt + ": ");
        while (!scanner.hasNextInt()) {
            System.out.println("Invalid input. Please enter a number.");
            scanner.next();
            System.out.print(prompt + ": ");
        }
        int value = scanner.nextInt();
        scanner.nextLine();
        return value;
    }

    // MAIN LOOP YAY!!
    public void run() {
        System.out.println("--- Honors Study Project CLI ---");
        loadData();

        int choice;
        do { 
            printMenu();
            choice = readInt("Enter choice (0-7)");

            try {
                switch (choice) {
                    case 1:
                        addTask();
                        break;
                    case 2:
                        listAgenda();
                        break;
                    case 3:
                        completeTask();
                        break;
                    case 4:
                        buildTodayQueue();
                        break;
                    case 5:
                        importIcs();
                        break;
                    case 6:
                        saveData();
                        break;
                    case 7:
                        loadData();
                        break;
                    case 0:
                        System.out.println("Thank you! Exiting... Data not saved...");
                        break;
                    default:
                        System.out.println("Invalid option. Please try again!");
                }
            } catch (Exception e) {
                System.err.println("An error occurred: " + e.getMessage());
            }
        } while (choice != 0);

        scanner.close();
    }

    private void printMenu() {
        System.out.println("\n-=-* MENU *-=-");
        System.out.println("1. Add Task");
        System.out.println("2. List Agenda (Sorted)");
        System.out.println("3. Complete Task (by index)");
        System.out.println("4. Build 'Today Queue' (Next 24h)");
        System.out.println("5. Import from ICS");
        System.out.println("6. Save Data");
        System.out.println("7. Load Data");
        System.out.println("0. Exit");
    }

    // Fixing out the bugs and errors
    private void addTask() {
        System.out.println("\n-=- ADD TASK -=-");
        System.out.print("title: ");
        String title = scanner.nextLine();
        System.out.print("Notes: ");
        String notes = scanner.nextLine();
        Instant dueDate = readInstant("Due Date");
        int priority = readInt("Priority (1-5)");

        Task newTask = new Task(title, notes, dueDate, priority);
        allItems.addLast(newTask);
        System.out.println("Task added successfully! Task : " + title);
    }

    private void listAgenda() {
        if (allItems.isEmpty()) {
            System.out.println("Agenda is currently empty.");
            return;
        }

        // Convert SimpleLinkedList to array. Why Error?
        Item[] itemArray = allItems.toArray(new Item[allItems.size()]); // fix
        
        // MSort
        MergeSort.sort(itemArray);

        System.out.println("\n-=- AGENDA (Sorted by Due Date) -=-");
        for (int i = 0; i < itemArray.length; i++) {
            Item item = itemArray[i];
            String status = (item instanceof Task && ((Task)item).isCompleted()) ? "[DONE]" : "[  ]"; // fix error

            // Form time
            String time = Instant.ofEpochSecond(item.sortKeyEpochSeconds()).atZone(ZoneId.systemDefault()).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
            System.out.printf("%d. %s %s | Due: %s | %s\n", 1 + 1, status, item.getTitle(), time, item.getClass().getSimpleName());

        }
    }

    private void completeTask() {
        if (allItems.isEmpty()) {
            System.out.println("No items to complete.");
            return;
        }

        listAgenda();
        int index = readInt("enter the number of item to COMPLETE (or 0 to cancel)");
        if (index < 0 || index >= allItems.size()) {
            System.out.println("Cancellation or invalid index.");
            return;
        }


        Node<Item> current = (Node<Item>) allItems.getHead(); // create getHead
        int counter = 0;
        Item target = null;

        while (current != null) {
            if (counter == index) {
                target = current.data;
                break;
            }

            current = current.next;
            counter++;
        }

        if (target instanceof Task) {
            Task t = (Task) target;
            t.setCompleted(true);
            System.out.println("Task '" + t.getTitle() + "' marked as COMPLETED.");

        } else {
            System.out.println("Item is not a Task or could not be found.");

        }
    }


    private void buildTodayQueue() {
        System.out.println("\n-=- TODAY AGENDA (Next 24 hours) -=-");
        Instant now = Instant.now();
        Instant tomorrow = now.plusSeconds(24 * 3600); // hopefully?
        Item[] itemArray = allItems.toArray(new Item[allItems.size()]); // Fix

        System.out.println("Items due soon:");
        int count = 0;

        for (Item item : itemArray) {
            if (item instanceof Schedulable) {
                Schedulable schedulable = (Schedulable) item;
                Instant nextDue = Instant.ofEpochSecond(schedulable.nextOccurrenceEpochSeconds());

                if (nextDue.isAfter(now) && nextDue.isBefore(tomorrow)) {
                    System.out.println("- " + item.getTitle() + " (Due: " + nextDue + ")");
                    count++;
                }
            }
        }

        if (count == 0) {
            System.out.println("Nothing is due within the next 24 hours!");
        }
    }

    private void importIcs() {
        System.out.print("Enter path to .ics file: ");
        String path = scanner.nextLine();
        File icsFile = new File(path);

        if (!icsFile.exists()) {
            System.out.println("Error: File cannot be found at " + path);
            return;
        }

        IcsParser parser = new IcsParser();
        try {
            SimpleLinkedList<Item> importedItems = parser.parse(icsFile);
            // Fix:
            Item[] newItems = new Item[importedItems.size()];
            newItems = importedItems.toArray(newItems);


            for (Object item : newItems) {
                allItems.addLast((Item) item);
            }

            System.out.println("Successfully imported " + importedItems.size() + " events...");
        } catch (IOException e) {
            System.err.println("Error reading or parsing ICS file: " + e.getMessage());
        }
    }

    private void saveData() {
        try {
            store.save(allItems);
            System.out.println("Data saved successfully!");
        } catch (Exception e) {
            System.err.println("Error saving data: " + e.getMessage());
        }
    }

    private void loadData() {
        try {
            allItems = store.load();
            System.out.println("Data loaded successfully!");
        } catch (Exception e) {
            System.err.println("Error loading data: " + e.getMessage());
            allItems = new SimpleLinkedList<>();
        }
    }

    public void cleanup() {
        if (scanner != null) {
            scanner.close();
        }
    }



    public static void main(String[] args) {
        Main app = new Main();
        app.run();
        app.cleanup();
    }
}

