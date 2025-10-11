import java.io.*;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

/*
 * Note for whoever reviews this. I decided to go with ICS instead of data scraping or web scraping,
 * as one might consider in Python, because it will be more efficient in the long run and accomplishes exactly
 * what we want to achieve with this project. Grabbing calendar information...
 */

 public class IcsParser {
    // We need todefine the required format for ICS timestamps.
    // ICS files as I have found often include Z time or Zulu time which corresponds to ZoneOffset.utc
    // The X format in the pattern handles the 'Z' time I think

    // lets try this... Last time it errored out
    private static final DateTimeFormatter ICS_DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMdd'T'HHmmss[X]").withZone(ZoneOffset.UTC);


     // create a method to parse ICS files and convert the VEVENTs into our simple linked list and previous Item object

    public SimpleLinkedList<Item> parse(File ics) throws IOException {
        SimpleLinkedList<Item> items = new SimpleLinkedList<>();
        
        String uid = null;
        String summary = null;
        String description = null;
        Instant dtStart = null;
        Instant dtEnd = null;
        boolean inVEvent = false;

        // now lets try to open with BufferedReader. I have found that for some people this works... Lets see...
        try (BufferedReader reader = new BufferedReader(new FileReader(ics))) {
            String line;

            while((line = reader.readLine()) != null) {
                line = line.trim();

                if (line.equals("BEGIN:VEVENT")) {
                    inVEvent = true;
                    // Reset fields
                    uid = summary = description = null; // should reset all 3
                    dtStart = dtEnd = null;
                    continue;
                }

                if (line.equals("END:VEVENT")) {
                    inVEvent = false;
                    if (summary != null && dtStart != null) {
                        Item newItem = createItemFromEvent(summary, description, dtStart, dtEnd);
                        items.addLast(newItem);
                    }
                    continue;
                }

                if (inVEvent) {
                    // collect the required fields : UID : SUM : DESC : DTST : DTEN
                    if (line.startsWith("UID:")) {
                        uid = line.substring(4);
                    } else if (line.startsWith("SUMMARY:")) {
                        summary = line.substring(8);
                    } else if (line.startsWith("DESCRIPTION:")) {
                        description = line.substring(12);
                    } else if (line.startsWith("DTSTART:")) {
                        dtStart = parseIcsTime(line.substring(8));
                    } else if (line.startsWith("DTEND:")) {
                        dtEnd = parseIcsTime(line.substring(6));
                    }
                }
            }
        }

        return items;
    }

    private Instant parseIcsTime(String timeString) {
        if (timeString.endsWith("Z")) { // processing the "Z"
            timeString = timeString.substring(0, timeString.length() - 1);
        }

        try { // hopefully this works but here we are parseing as LocalDateTime and converting to a time instant
            LocalDateTime ldt = LocalDateTime.parse(timeString, ICS_DATE_TIME_FORMATTER);
            return ldt.toInstant(ZoneOffset.UTC);
        } catch (Exception e) {
            System.err.println("Failed to parse ICS time string: " + timeString + "- Error: " + e.getMessage());
            return null;
        }
    }

    private Item createItemFromEvent(String title, String notes, Instant start, Instant end) {
        // I think we are going to go with the following logic for this:
        // If the title contains Assignment or Exam, create an assignment or Exam
        // Otherwise (else) create a regular Task

        Instant dueDate = (end != null) ? end : start;
        int defaultPriority = 3;

        return new Task(title, notes != null ? notes : "", dueDate, defaultPriority);
    }
 }
