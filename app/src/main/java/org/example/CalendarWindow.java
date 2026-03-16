package org.example;

import javax.swing.*;
import java.awt.*;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class CalendarWindow {

    // --- Event class (adapted from chasesMethod, but for Swing) ---
    private static class Event {
        String name;
        String tag;
        Color color;     // Swing Color
        LocalDate date;  // which day the event is on

        Event(String name, String tag, Color color, LocalDate date) {
            this.name = name;
            this.tag = tag;
            this.color = color;
            this.date = date;
        }
    }

    // sample events
    private final Event[] events = {
            new Event("Math Homework", "Homework", Color.BLUE, LocalDate.now()),
            new Event("Science Homework", "Homework", Color.BLUE, LocalDate.now().plusDays(1)),
            new Event("Basketball Practice", "Sports", Color.GREEN, LocalDate.now().plusDays(2)),
            new Event("Dentist Appointment", "Health", Color.RED, LocalDate.now().plusDays(3))
    };

    private LocalDate currentMonth = LocalDate.now().withDayOfMonth(1);
    private final JLabel monthLabel = new JLabel("", SwingConstants.CENTER);

    // store all day buttons (7 x 6 = 42 cells)
    private final JButton[] dayButtons = new JButton[42];

    // which tag to filter (like chasesMethod’s filterTag)
    private String filterTag = "Homework";

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new CalendarWindow().start());
    }

    public void start() {
        JFrame frame = new JFrame("Calendar");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Main container
        JPanel root = new JPanel(new BorderLayout(10, 10));
        root.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Header (Prev | Month | Next + Tag filter)
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

        // Tag filter dropdown (bottom of header)
        String[] tags = { "Homework", "Sports", "Health", "All" };
        JComboBox<String> tagFilter = new JComboBox<>(tags);
        tagFilter.setSelectedItem("Homework");
        tagFilter.addActionListener(e -> {
            filterTag = (String) tagFilter.getSelectedItem();
            updateGrid();
        });
        header.add(tagFilter, BorderLayout.SOUTH);

        // Days of week row
        JPanel dow = new JPanel(new GridLayout(1, 7));
        String[] days = { "Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat" };
        for (String d : days) {
            JLabel lbl = new JLabel(d, SwingConstants.CENTER);
            lbl.setFont(lbl.getFont().deriveFont(Font.BOLD));
            dow.add(lbl);
        }

        // Calendar grid (7 columns x 6 rows)
        JPanel grid = new JPanel(new GridLayout(6, 7, 5, 5));
        for (int i = 0; i < 42; i++) {
            JButton cell = new JButton("");
            cell.setFocusable(false);
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

    // Fill the grid with days and apply events + filter
    private void updateGrid() {
        // clear all buttons
        for (JButton b : dayButtons) {
            b.setText("");
            b.setBackground(null);
            b.setToolTipText(null);
            b.setOpaque(true);
        }

        LocalDate firstDay = currentMonth;
        int lengthOfMonth = firstDay.lengthOfMonth();

        // Find index of first day in grid (0 = Sunday)
        DayOfWeek dow = firstDay.getDayOfWeek();          // MON..SUN (1..7)
        int startIndex = dow.getValue() % 7;              // make Sunday = 0

        // Fill numbers and attach events
        for (int day = 1; day <= lengthOfMonth; day++) {
            int index = startIndex + day - 1;
            LocalDate date = currentMonth.withDayOfMonth(day);
            JButton btn = dayButtons[index];
            btn.setText(String.valueOf(day));

            // For each event: if it occurs on this date and matches filter, color it
            for (Event e : events) {
                if (e.date.equals(date) || isAllTagSelected()) {
                    if (isAllTagSelected() || e.tag.equalsIgnoreCase(filterTag)) {
                        // simple behavior: color the cell and show name + tag in tooltip
                        btn.setBackground(e.color);
                        String oldTip = btn.getToolTipText();
                        String eventText = e.name + " [" + e.tag + "]";
                        if (oldTip == null || oldTip.isEmpty()) {
                            btn.setToolTipText(eventText);
                        } else {
                            btn.setToolTipText(oldTip + "; " + eventText);
                        }
                    }
                }
            }
        }
    }

    private boolean isAllTagSelected() {
        return filterTag != null && filterTag.equalsIgnoreCase("All");
    }
}
