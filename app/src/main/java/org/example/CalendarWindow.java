package org.example;

import javax.swing.*;
import java.awt.*;
import java.time.*;
import java.time.format.DateTimeFormatter;

public class CalendarWindow {

    private static class Event {
        String name;
        String tag;
        Color color;
        LocalDateTime startTime;

        Event(String name, String tag, Color color, LocalDateTime startTime) {
            this.name = name;
            this.tag = tag;
            this.color = color;
            this.startTime = startTime;
        }

        LocalDate getDate() {
            return startTime.toLocalDate();
        }
    }

    // Color-coded Homework, Sports, Health only, with times on different days
    private final Event[] events = {
            // Today
            new Event("Math Homework", "Homework", Color.BLUE,
                    LocalDateTime.now().plusMinutes(5)), // soon, for testing
            new Event("English Essay", "Homework", Color.BLUE,
                    LocalDateTime.now().withHour(16).withMinute(0)), // 4:00 pm today

            // Tomorrow
            new Event("Science Homework", "Homework", Color.BLUE,
                    LocalDateTime.now().plusDays(1).withHour(18).withMinute(30)),
            new Event("Basketball Practice", "Sports", Color.GREEN,
                    LocalDateTime.now().plusDays(1).withHour(15).withMinute(0)),

            // Two days from now
            new Event("Gym Workout", "Health", Color.RED,
                    LocalDateTime.now().plusDays(2).withHour(7).withMinute(0)),
            new Event("Soccer Game", "Sports", Color.GREEN,
                    LocalDateTime.now().plusDays(2).withHour(17).withMinute(30)),

            // Later in the week
            new Event("Doctor Visit", "Health", Color.RED,
                    LocalDateTime.now().plusDays(4).withHour(10).withMinute(0)),
            new Event("Chemistry Lab Report", "Homework", Color.BLUE,
                    LocalDateTime.now().plusDays(5).withHour(20).withMinute(0))
    };

    private LocalDate currentMonth = LocalDate.now().withDayOfMonth(1);
    private final JLabel monthLabel = new JLabel("", SwingConstants.CENTER);
    private final JButton[] dayButtons = new JButton[42];

    private String filterTag = "Homework";

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

        // Only Homework, Sports, Health (+ All)
        String[] tags = { "Homework", "Sports", "Health", "All" };
        JComboBox<String> tagFilter = new JComboBox<>(tags);
        tagFilter.setSelectedItem("Homework");
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
        frame.setSize(800, 600);
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

    // Every colored day shows one event's start time directly on the button
    private void updateGrid() {
        for (JButton b : dayButtons) {
            b.setText("");
            b.setBackground(null);
            b.setToolTipText(null);
        }

        LocalDate firstDay = currentMonth;
        int lengthOfMonth = firstDay.lengthOfMonth();

        DayOfWeek dow = firstDay.getDayOfWeek();
        int startIndex = dow.getValue() % 7; // Sunday = 0

        for (int day = 1; day <= lengthOfMonth; day++) {
            int index = startIndex + day - 1;
            LocalDate date = currentMonth.withDayOfMonth(day);
            JButton btn = dayButtons[index];
            btn.setText(String.valueOf(day));

            boolean timeShown = false;

            for (Event e : events) {
                if (e.getDate().equals(date)
                        && (isAllTagSelected() || e.tag.equalsIgnoreCase(filterTag))) {

                    // color the cell
                    btn.setBackground(e.color);

                    // tooltip with full info
                    String oldTip = btn.getToolTipText();
                    String eventText = e.name + " [" + e.tag + "] at " +
                            e.startTime.format(timeFmt);
                    if (oldTip == null || oldTip.isEmpty()) {
                        btn.setToolTipText(eventText);
                    } else {
                        btn.setToolTipText(oldTip + "; " + eventText);
                    }

                    // show one time on the button
                    if (!timeShown) {
                        String base = String.valueOf(day);
                        String timeText = e.startTime.format(timeFmt);
                        btn.setText(base + "  " + timeText);
                        timeShown = true;
                    }
                }
            }
        }
    }
}
