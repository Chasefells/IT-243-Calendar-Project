package org.example;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class AsimaFeatures {

    // Simple Event model (so you don’t touch teammates' code)
    public static class Event {
        public final String title;
        public final LocalDate date;

        public Event(String title, LocalDate date) {
            this.title = title;
            this.date = date;
        }

        @Override
        public String toString() {
            return date + " - " + title;
        }
    }

    // SEARCH: returns events whose title contains the keyword (case-insensitive)
    public static List<Event> searchEvents(List<Event> events, String keyword) {
        String k = keyword.toLowerCase().trim();
        List<Event> results = new ArrayList<>();
        for (Event e : events) {
            if (e.title.toLowerCase().contains(k)) {
                results.add(e);
            }
        }
        return results;
    }

    // RECURRING: generates weekly occurrences (easy demo version)
    public static List<Event> createWeeklyRecurringEvent(String title, LocalDate startDate, int weeks) {
        List<Event> events = new ArrayList<>();
        events.add(new Event(title, startDate));
        for (int i = 1; i <= weeks; i++) {
            events.add(new Event(title, startDate.plusWeeks(i)));
        }
        return events;
    }

    // DEMO runner so you can show your feature working now
    public static void runAsimaDemo() {
        System.out.println("\n=== ASIMA FEATURE DEMO ===");

        // 1) Recurring weekly events demo
        List<Event> all = createWeeklyRecurringEvent("Exam", LocalDate.now(), 4);
        all.add(new Event("Gym", LocalDate.now().plusDays(1)));
        all.add(new Event("Project meeting", LocalDate.now().plusDays(2)));

        System.out.println("All events:");
        for (Event e : all) System.out.println("  " + e);

        // 2) Search demo
        String keyword = "exam";
        List<Event> matches = searchEvents(all, keyword);

        System.out.println("\nSearch keyword: \"" + keyword + "\"");
        System.out.println("Matches:");
        for (Event e : matches) System.out.println("  " + e);

        System.out.println("=== END ASIMA DEMO ===\n");
    }
}
