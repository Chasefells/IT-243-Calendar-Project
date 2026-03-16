package org.example;

import javax.swing.*;
import java.awt.*;
import java.time.*;
import java.time.format.DateTimeFormatter;

public class CalendarWindow {

    private enum Privacy {
        PUBLIC,
        PRIVATE
    }

    private static class Event {
        String name;
        String tag;
        Color color;
        LocalDateTime startTime;
        Privacy privacy;
        String ownerId;

        Event(String name,
              String tag,
              Color color,
              LocalDateTime startTime,
              Privacy privacy,
              String ownerId) {
            this.name = name;
            this.tag = tag;
            this.color = color;
            this.startTime = startTime;
            this.privacy = privacy;
            this.ownerId = ownerId;
        }

        LocalDate getDate() {
            return startTime.toLocalDate();
        }
    }

    private final String currentUserId = "user1";

    // March now has 10 events + 3 privates (2nd private added)
    private final Event[] events = {
        // MARCH 2026 (10 events + 3 privates)
        new Event("Math Homework", "Homework", Color.BLUE,
                LocalDateTime.of(2026, 3, 16, 10, 0), Privacy.PUBLIC, "user1"),
        new Event("Therapy Session", "Health", Color.RED,
                LocalDateTime.of(2026, 3, 16, 14, 0), Privacy.PRIVATE, "user2"), // PRIVATE #1
        new Event("Basketball Practice", "Sports", Color.GREEN,
                LocalDateTime.of(2026, 3, 18, 15, 30), Privacy.PUBLIC, "user3"),
        new Event("Gym Workout", "Health", new Color(255, 100, 100),
                LocalDateTime.of(2026, 3, 20, 7, 0), Privacy.PUBLIC, "user1"),
        new Event("Dentist Appointment", "Health", Color.RED,
                LocalDateTime.of(2026, 3, 22, 9, 30), Privacy.PRIVATE, "user3"), // NEW PRIVATE #2
        new Event("Science Project", "Homework", Color.BLUE,
                LocalDateTime.of(2026, 3, 23, 18, 0), Privacy.PUBLIC, "user1"),
        new Event("Soccer Game", "Sports", Color.GREEN,
                LocalDateTime.of(2026, 3, 25, 17, 0), Privacy.PUBLIC, "user3"),
        new Event("Doctor Checkup", "Health", Color.RED,
                LocalDateTime.of(2026, 3, 27, 11, 0), Privacy.PRIVATE, "user1"), // PRIVATE #3 (owner)
        new Event("History Test Prep", "Homework", new Color(100, 150, 255),
                LocalDateTime.of(2026, 3, 29, 19, 0), Privacy.PUBLIC, "user1"),
        new Event("Team Meeting", "Sports", Color.GREEN,
                LocalDateTime.of(2026, 3, 31, 16, 0), Privacy.PUBLIC, "user3"),

        // APRIL 2026 (8 events + 1 private)
        new Event("English Essay", "Homework", Color.BLUE,
                LocalDateTime.of(2026, 4, 2, 20, 0), Privacy.PUBLIC, "user1"),
        new Event("Counseling", "Health", Color.RED,
                LocalDateTime.of(2026, 4, 5, 9, 0), Privacy.PRIVATE, "user2"),
        new Event("Track Practice", "Sports", Color.GREEN,
                LocalDateTime.of(2026, 4, 7, 16, 0), Privacy.PUBLIC, "user3"),
        new Event("Yoga Class", "Health", new Color(255, 100, 100),
                LocalDateTime.of(2026, 4, 10, 18, 0), Privacy.PUBLIC, "user1"),
        new Event("Physics Lab", "Homework", new Color(100, 150, 255),
                LocalDateTime.of(2026, 4, 13, 17, 0), Privacy.PUBLIC, "user1"),
        new Event("Volleyball", "Sports", Color.GREEN,
                LocalDateTime.of(2026, 4, 15, 19, 0), Privacy.PUBLIC, "user3"),
        new Event("Nutrition Consult", "Health", Color.RED,
                LocalDateTime.of(2026, 4, 18, 14, 0), Privacy.PUBLIC, "user1"),
        new Event("Bio Test Prep", "Homework", Color.BLUE,
                LocalDateTime.of(2026, 4, 22, 19, 30), Privacy.PUBLIC, "user1"),
        new Event("Spring Game", "Sports", Color.GREEN,
                LocalDateTime.of(2026, 4, 25, 14, 0), Privacy.PUBLIC, "user3"),

        // MAY 2026 (8 events + 1 private)
        new Event("Finals Review", "Homework", Color.BLUE,
                LocalDateTime.of(2026, 5, 3, 20, 0), Privacy.PUBLIC, "user1"),
        new Event("Physical Exam", "Health", Color.RED,
                LocalDateTime.of(2026, 5, 6, 10, 0), Privacy.PRIVATE, "user3"),
        new Event("Championship", "Sports", Color.GREEN,
                LocalDateTime.of(2026, 5, 9, 15, 0), Privacy.PUBLIC, "user3"),
        new Event("Cardio Session", "Health", new Color(255, 100, 100),
                LocalDateTime.of(2026, 5, 12, 6, 30), Privacy.PUBLIC, "user1"),
        new Event("Calc Homework", "Homework", new Color(100, 150, 255),
                LocalDateTime.of(2026, 5, 15, 21, 0), Privacy.PUBLIC, "user1"),
        new Event("Tennis Lesson", "Sports", Color.GREEN,
                LocalDateTime.of(2026, 5, 17, 17, 0), Privacy.PUBLIC, "user3"),
        new Event("Blood Test", "Health", Color.RED,
                LocalDateTime.of(2026, 5, 20, 8, 0), Privacy.PUBLIC, "user1"),
        new Event("Stats Project", "Homework", Color.BLUE,
                LocalDateTime.of(2026, 5, 24, 18, 30), Privacy.PUBLIC, "user1"),
        new Event("All-Star Game", "Sports", Color.GREEN,
                LocalDateTime.of(2026, 5, 28, 13, 0), Privacy.PUBLIC, "user3"),

        // JUNE 2026 (8 events + 1 private)
        new Event("Summer Reading", "Homework", Color.BLUE,
                LocalDateTime.of(2026, 6, 4, 14, 0), Privacy.PUBLIC, "user1"),
        new Event("Eye Exam", "Health", Color.RED,
                LocalDateTime.of(2026, 6, 8, 15, 0), Privacy.PRIVATE, "user2"),
        new Event("Baseball Camp", "Sports", Color.GREEN,
                LocalDateTime.of(2026, 6, 11, 9, 0), Privacy.PUBLIC, "user3"),
        new Event("Swim Lessons", "Health", new Color(255, 100, 100),
                LocalDateTime.of(2026, 6, 14, 11, 0), Privacy.PUBLIC, "user1"),
        new Event("Essay Revision", "Homework", new Color(100, 150, 255),
                LocalDateTime.of(2026, 6, 17, 16, 0), Privacy.PUBLIC, "user1"),
        new Event("Softball Game", "Sports", Color.GREEN,
                LocalDateTime.of(2026, 6, 20, 18, 0), Privacy.PUBLIC, "user3"),
        new Event("Vaccination", "Health", Color.RED,
                LocalDateTime.of(2026, 6, 23, 13, 0), Privacy.PUBLIC, "user1"),
        new Event("Research Paper", "Homework", Color.BLUE,
                LocalDateTime.of(2026, 6, 27, 19, 0), Privacy.PUBLIC, "user1"),
        new Event("Pickup Game", "Sports", Color.GREEN,
                LocalDateTime.of(2026, 6, 30, 17, 0), Privacy.PUBLIC, "user3")
    };

    private LocalDate currentMonth = LocalDate.now().withDayOfMonth(1);
    private final JLabel monthLabel = new JLabel("", SwingConstants.CENTER);
    private final JButton[] dayButtons = new JButton[42];

    private String filterTag = "All";
    private final DateTimeFormatter timeFmt = DateTimeFormatter.ofPattern("h:mm a");

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new CalendarWindow().start());
    }

    public void start() {
        JFrame frame = new JFrame("Calendar");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel root = new JPanel(new BorderLayout(10, 10));
        root.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel header = new JPanel(new BorderLayout());

        JButton prevBtn = new JButton("<=");
        JButton nextBtn = new JButton("=>");

        prevBtn.addActionListener(e -> {
            currentMonth = currentMonth.minusMonths(1);
            updateMonthLabel();
            updateGrid();
        });

        nextBtn.addActionListener(e -> {
            currentMonth = currentMonth.plusMonths(1);
            updateMonthLabel();
            updateGrid();
        });

        header.add(prevBtn, BorderLayout.WEST);
        header.add(monthLabel, BorderLayout.CENTER);
        header.add(nextBtn, BorderLayout.EAST);

        String[] tags = { "Homework", "Sports", "Health", "All" };
        JComboBox<String> tagFilter = new JComboBox<>(tags);
        tagFilter.setSelectedItem("All");
        tagFilter.addActionListener(e -> {
            filterTag = (String) tagFilter.getSelectedItem();
            updateGrid();
        });
        header.add(tagFilter, BorderLayout.SOUTH);

        JPanel dow = new JPanel(new GridLayout(1, 7));
        String[] days = { "Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat" };
        for (String d : days) {
            JLabel lbl = new JLabel(d, SwingConstants.CENTER);
            lbl.setFont(lbl.getFont().deriveFont(Font.BOLD));
            dow.add(lbl);
        }

        JPanel grid = new JPanel(new GridLayout(6, 7, 5, 5));
        for (int i = 0; i < 42; i++) {
            JButton cell = new JButton("");
            cell.setFocusable(false);
            cell.setOpaque(true);
            dayButtons[i] = cell;
            grid.add(cell);
        }

        JPanel center = new JPanel(new BorderLayout(0, 5));
        center.add(dow, BorderLayout.NORTH);
        center.add(grid, BorderLayout.CENTER);

        root.add(header, BorderLayout.NORTH);
        root.add(center, BorderLayout.CENTER);

        frame.setContentPane(root);
        frame.pack();
        frame.setSize(900, 700);
        frame.setLocationRelativeTo(null);

        updateMonthLabel();
        updateGrid();

        frame.setVisible(true);
    }

    private void updateMonthLabel() {
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("MMMM yyyy");
        monthLabel.setText(currentMonth.format(fmt));
    }

    private boolean isAllTagSelected() {
        return filterTag != null && filterTag.equalsIgnoreCase("All");
    }

    private String getDisplayTitle(Event e) {
        boolean owner = currentUserId.equals(e.ownerId);
        if (e.privacy == Privacy.PRIVATE && !owner) {
            return "Private";
        }
        return e.name;
    }

    private String getDisplayTag(Event e) {
        boolean owner = currentUserId.equals(e.ownerId);
        if (e.privacy == Privacy.PRIVATE && !owner) {
            return "Private";
        }
        return e.tag;
    }

    private void updateGrid() {
        for (JButton b : dayButtons) {
            b.setText("");
            b.setBackground(null);
            b.setToolTipText(null);
        }

        LocalDate firstDay = currentMonth;
        int lengthOfMonth = firstDay.lengthOfMonth();

        DayOfWeek dow = firstDay.getDayOfWeek();
        int startIndex = dow.getValue() % 7;

        for (int day = 1; day <= lengthOfMonth; day++) {
            int index = startIndex + day - 1;
            LocalDate date = currentMonth.withDayOfMonth(day);
            JButton btn = dayButtons[index];
            btn.setText(String.valueOf(day));

            boolean timeShown = false;

            for (Event e : events) {
                if (!e.getDate().equals(date)) continue;
                if (!isAllTagSelected() && !e.tag.equalsIgnoreCase(filterTag)) continue;

                btn.setBackground(e.color);

                String displayTitle = getDisplayTitle(e);
                String displayTag = getDisplayTag(e);

                String oldTip = btn.getToolTipText();
                String eventText = displayTitle + " [" + displayTag + "] at " +
                        e.startTime.format(timeFmt);
                if (oldTip == null || oldTip.isEmpty()) {
                    btn.setToolTipText(eventText);
                } else {
                    btn.setToolTipText(oldTip + "; " + eventText);
                }

                if (!timeShown) {
                    String base = String.valueOf(day);
                    String timeText = e.startTime.format(timeFmt);
                    
                    boolean owner = currentUserId.equals(e.ownerId);
                    if (e.privacy == Privacy.PRIVATE && !owner) {
                        btn.setText(base + "  Private " + timeText);
                    } else {
                        btn.setText(base + "  " + displayTitle + " " + timeText);
                    }
                    timeShown = true;
                }
            }
        }
    }
}
