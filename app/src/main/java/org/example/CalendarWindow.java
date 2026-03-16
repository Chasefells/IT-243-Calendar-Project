package org.example;

import javax.swing.*;
import java.awt.*;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.Duration;

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

    private final Event[] events = {
            // MARCH 2026 (10 events + 3 privates)
            new Event("Math Homework", "Homework", Color.BLUE,
                    LocalDateTime.of(2026, 3, 16, 17, 0), Privacy.PUBLIC, "user1"),
            new Event("Therapy Session", "Health", Color.RED,
                    LocalDateTime.of(2026, 3, 16, 14, 0), Privacy.PRIVATE, "user2"),
            new Event("Basketball Practice", "Sports", Color.GREEN,
                    LocalDateTime.of(2026, 3, 18, 15, 30), Privacy.PUBLIC, "user3"),
            new Event("Gym Workout", "Health", new Color(255, 100, 100),
                    LocalDateTime.of(2026, 3, 20, 7, 0), Privacy.PUBLIC, "user1"),
            new Event("Dentist Appointment", "Health", Color.RED,
                    LocalDateTime.of(2026, 3, 22, 9, 30), Privacy.PRIVATE, "user3"),
            new Event("Science Project", "Homework", Color.BLUE,
                    LocalDateTime.of(2026, 3, 23, 18, 0), Privacy.PUBLIC, "user1"),
            new Event("Soccer Game", "Sports", Color.GREEN,
                    LocalDateTime.of(2026, 3, 25, 17, 0), Privacy.PUBLIC, "user3"),
            new Event("Doctor Checkup", "Health", Color.RED,
                    LocalDateTime.of(2026, 3, 27, 11, 0), Privacy.PRIVATE, "user1"),
            new Event("History Test Prep", "Homework", new Color(100, 150, 255),
                    LocalDateTime.of(2026, 3, 29, 19, 0), Privacy.PUBLIC, "user1"),
            new Event("Team Meeting", "Sports", Color.GREEN,
                    LocalDateTime.of(2026, 3, 31, 16, 0), Privacy.PUBLIC, "user3"),

            // APRIL, MAY, JUNE events...
            new Event("English Essay", "Homework", Color.BLUE,
                    LocalDateTime.of(2026, 4, 2, 20, 0), Privacy.PUBLIC, "user1"),
            new Event("Counseling", "Health", Color.RED,
                    LocalDateTime.of(2026, 4, 5, 9, 0), Privacy.PRIVATE, "user2"),
            new Event("Finals Review", "Homework", Color.BLUE,
                    LocalDateTime.of(2026, 5, 3, 20, 0), Privacy.PUBLIC, "user1"),
            new Event("Physical Exam", "Health", Color.RED,
                    LocalDateTime.of(2026, 5, 6, 10, 0), Privacy.PRIVATE, "user3"),
            new Event("Summer Reading", "Homework", Color.BLUE,
                    LocalDateTime.of(2026, 6, 4, 14, 0), Privacy.PUBLIC, "user1"),
            new Event("Eye Exam", "Health", Color.RED,
                    LocalDateTime.of(2026, 6, 8, 15, 0), Privacy.PRIVATE, "user2"),

            // Test event a few minutes from now so you can see the timer
            new Event("Test Event", "Homework", Color.MAGENTA,
                    LocalDateTime.now().plusMinutes(5), Privacy.PUBLIC, "user1")
    };

    private LocalDate currentMonth = LocalDate.now().withDayOfMonth(1);
    private final JLabel monthLabel = new JLabel("", SwingConstants.CENTER);
    private final JButton[] dayButtons = new JButton[42];
    private String filterTag = "All";
    private final DateTimeFormatter timeFmt = DateTimeFormatter.ofPattern("h:mm a");

    private Timer countdownTimer;

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
            updateCountdownOverlays();
        });

        nextBtn.addActionListener(e -> {
            currentMonth = currentMonth.plusMonths(1);
            updateMonthLabel();
            updateGrid();
            updateCountdownOverlays();
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
            updateCountdownOverlays();
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
            cell.setHorizontalTextPosition(SwingConstants.CENTER);
            cell.setVerticalTextPosition(SwingConstants.CENTER);
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
        startCountdownTimer();
        updateCountdownOverlays();

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

    private void startCountdownTimer() {
        if (countdownTimer == null) {
            // Update every minute (60000); change to 1000 for every second.
            countdownTimer = new Timer(60000, e -> updateCountdownOverlays());
            countdownTimer.start();
        } else if (!countdownTimer.isRunning()) {
            countdownTimer.start();
        }
    }

    private void updateCountdownOverlays() {
        LocalDateTime now = LocalDateTime.now();

        LocalDate firstDay = currentMonth;
        DayOfWeek dow = firstDay.getDayOfWeek();
        int startIndex = dow.getValue() % 7;
        int lengthOfMonth = firstDay.lengthOfMonth();

        // Reset buttons to base state (no countdown)
        for (int day = 1; day <= lengthOfMonth; day++) {
            int index = startIndex + day - 1;
            if (index >= 0 && index < dayButtons.length) {
                LocalDate date = currentMonth.withDayOfMonth(day);
                JButton btn = dayButtons[index];
                updateButtonBaseText(btn, day, date);
            }
        }

        // Add countdowns for events within 24h
        for (Event event : events) {
            Duration timeUntil = Duration.between(now, event.startTime);
            if (timeUntil.isNegative() || timeUntil.toHours() > 24) continue;

            LocalDate eventDate = event.getDate();
            if (!eventDate.getMonth().equals(currentMonth.getMonth()) ||
                    eventDate.getYear() != currentMonth.getYear()) continue;

            int dayOfMonth = eventDate.getDayOfMonth();
            int index = startIndex + dayOfMonth - 1;
            if (index < 0 || index >= dayButtons.length) continue;

            JButton btn = dayButtons[index];
            String countdown = formatCountdown(timeUntil);

            String currentText = btn.getText();
            boolean owner = currentUserId.equals(event.ownerId);
            String displayTitle = getDisplayTitle(event);

            if (event.privacy == Privacy.PRIVATE && !owner) {
                if (currentText.contains("Private")) {
                    btn.setText(wrapWithCountdown(currentText, countdown));
                }
            } else {
                if (currentText.contains(displayTitle)) {
                    btn.setText(wrapWithCountdown(currentText, countdown));
                }
            }
        }
    }

    // Cleaner 2nd line, subtle gray text: "⏳ in 2h 5m"
    private String wrapWithCountdown(String baseText, String countdown) {
        String plain = baseText;
        if (plain.startsWith("<html>") && plain.endsWith("</html>")) {
            plain = plain.substring(6, plain.length() - 7);
        }

        return "<html>" +
                "<div style='text-align:left;'>" + plain + "</div>" +
                "<div style='font-size:10px; color:#666; margin-top:2px;'>⏳ " + countdown + "</div>" +
               "</html>";
    }

    // Clean format: "in 2h 5m" or "in 42 min"
    private String formatCountdown(Duration duration) {
        long totalMinutes = duration.toMinutes();
        if (totalMinutes >= 60) {
            long hours = totalMinutes / 60;
            long minutes = totalMinutes % 60;
            return String.format("in %dh %dm", hours, minutes);
        } else {
            return String.format("in %d min", totalMinutes);
        }
    }

    private void updateButtonBaseText(JButton btn, int day, LocalDate date) {
        boolean found = false;
        for (Event e : events) {
            if (e.getDate().equals(date) &&
                    (isAllTagSelected() || e.tag.equalsIgnoreCase(filterTag))) {

                String displayTitle = getDisplayTitle(e);
                String timeText = e.startTime.format(timeFmt);
                boolean owner = currentUserId.equals(e.ownerId);

                if (e.privacy == Privacy.PRIVATE && !owner) {
                    btn.setText(day + "  Private " + timeText);
                } else {
                    btn.setText(day + "  " + displayTitle + " " + timeText);
                }
                found = true;
                break;
            }
        }
        if (!found) {
            btn.setText(String.valueOf(day));
        }
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

            if (!timeShown) {
                btn.setText(String.valueOf(day));
            }
        }
    }
}
