package org.example;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

public class AppTest {

    private List<App.Event> sampleEvents;

    @BeforeEach
    void setup() {
        sampleEvents = List.of(
            new App.Event("Math Homework", "Homework", "\u001B[34m"),
            new App.Event("Science Homework", "Homework", "\u001B[34m"),
            new App.Event("Basketball Practice", "Sports", "\u001B[32m"),
            new App.Event("Dentist Appointment", "Health", "\u001B[31m")
        );
    }

    @Test
    void testFilterHomework_returnsTwoEvents() {
        assertEquals(2, App.filterEventsByTag(sampleEvents, "Homework").size());
    }

    @Test
    void testFilterTagCaseInsensitive() {
        assertEquals(2, App.filterEventsByTag(sampleEvents, "homeWORK").size());
    }

    @Test
    void testFilterSports_returnsOneEvent() {
        List<App.Event> filtered = App.filterEventsByTag(sampleEvents, "Sports");
        assertEquals(1, filtered.size());
        assertEquals("Basketball Practice", filtered.get(0).name);
    }

    @Test
    void testFilterNonexistentTag_returnsEmptyList() {
        assertTrue(App.filterEventsByTag(sampleEvents, "Music").isEmpty());
    }

    @Test
    void testFilterWithNullTag_returnsEmptyList() {
        assertTrue(App.filterEventsByTag(sampleEvents, null).isEmpty());
    }

    @Test
    void testFilterWithEmptyTag_returnsEmptyList() {
        assertTrue(App.filterEventsByTag(sampleEvents, "").isEmpty());
    }

    @Test
    void testEventColorRetainedAfterFilter() {
        List<App.Event> filtered = App.filterEventsByTag(sampleEvents, "Homework");
        assertTrue(filtered.stream().allMatch(e -> e.color.equals("\u001B[34m")));
    }

    @Test
    void testEventTagsAreCaseInsensitive() {
        App.Event event = new App.Event("Test", "HOMEWORK", "\u001B[34m");
        List<App.Event> filtered = App.filterEventsByTag(List.of(event), "homework");
        assertEquals(1, filtered.size());
    }

    @Test
    void testOriginalListUnchanged() {
        App.filterEventsByTag(sampleEvents, "Homework");
        assertEquals(4, sampleEvents.size());
    }

    @Test
    void testFilterReturnsNewListInstance() {
        List<App.Event> filtered = App.filterEventsByTag(sampleEvents, "Homework");
        assertNotSame(filtered, sampleEvents);
    }
}
