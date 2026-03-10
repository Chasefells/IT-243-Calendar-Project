package org.example;

import org.example.App.Event;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class AppTest {

    // =========================
    // EVENT CONSTRUCTOR TESTS
    // =========================

    @Test
    void constructor_ShouldSetAllFieldsCorrectly() {
        Event event = new Event("Math Homework", "Homework", "Blue");

        assertEquals("Math Homework", event.name);
        assertEquals("Homework", event.tag);
        assertEquals("Blue", event.color);
    }

    @Test
    void constructor_ShouldAllowNullValues() {
        Event event = new Event(null, null, null);

        assertNull(event.name);
        assertNull(event.tag);
        assertNull(event.color);
    }

    @Test
    void constructor_ShouldAllowEmptyStrings() {
        Event event = new Event("", "", "");

        assertEquals("", event.name);
        assertEquals("", event.tag);
        assertEquals("", event.color);
    }

    @Test
    void constructor_ShouldStoreSpecialCharacters() {
        Event event = new Event("Dentist #1", "Health!", "\u001B[31m");

        assertEquals("Dentist #1", event.name);
        assertEquals("Health!", event.tag);
        assertEquals("\u001B[31m", event.color);
    }

    // =========================
    // filterEventsByTag TESTS
    // =========================

    @Test
    void filterEventsByTag_ShouldReturnMatchingEvents() {
        List<Event> events = List.of(
                new Event("Math Homework", "Homework", "Blue"),
                new Event("Basketball", "Sports", "Green")
        );

        List<Event> result = App.filterEventsByTag(events, "Homework");

        assertEquals(1, result.size());
        assertEquals("Math Homework", result.get(0).name);
    }

    @Test
    void filterEventsByTag_ShouldBeCaseInsensitive() {
        List<Event> events = List.of(
                new Event("Math Homework", "Homework", "Blue")
        );

        List<Event> result = App.filterEventsByTag(events, "homework");

        assertEquals(1, result.size());
    }

    @Test
    void filterEventsByTag_ShouldReturnEmptyList_WhenNoMatch() {
        List<Event> events = List.of(
                new Event("Math Homework", "Homework", "Blue")
        );

        List<Event> result = App.filterEventsByTag(events, "Sports");

        assertTrue(result.isEmpty());
    }

    @Test
    void filterEventsByTag_ShouldReturnEmptyList_WhenFilterTagIsNull() {
        List<Event> events = List.of(
                new Event("Math Homework", "Homework", "Blue")
        );

        List<Event> result = App.filterEventsByTag(events, null);

        assertTrue(result.isEmpty());
    }

    @Test
    void filterEventsByTag_ShouldReturnEmptyList_WhenFilterTagIsEmpty() {
        List<Event> events = List.of(
                new Event("Math Homework", "Homework", "Blue")
        );

        List<Event> result = App.filterEventsByTag(events, "");

        assertTrue(result.isEmpty());
    }

    @Test
    void filterEventsByTag_ShouldReturnMultipleMatches() {
        List<Event> events = List.of(
                new Event("Math Homework", "Homework", "Blue"),
                new Event("Science Homework", "Homework", "Blue"),
                new Event("Basketball", "Sports", "Green")
        );

        List<Event> result = App.filterEventsByTag(events, "Homework");

        assertEquals(2, result.size());
    }

    @Test
    void filterEventsByTag_ShouldWorkWithSingleEventList() {
        List<Event> events = List.of(
                new Event("Basketball", "Sports", "Green")
        );

        List<Event> result = App.filterEventsByTag(events, "Sports");

        assertEquals(1, result.size());
        assertEquals("Sports", result.get(0).tag);
    }

    @Test
    void filterEventsByTag_ShouldReturnEmptyList_WhenEventsListIsEmpty() {
        List<Event> events = List.of();

        List<Event> result = App.filterEventsByTag(events, "Homework");

        assertTrue(result.isEmpty());
    }
}