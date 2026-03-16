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

    // 8 events per month for March, April, May, June
    private final Event[] events = {
            // MARCH 2026 (8 events)
            new Event("Math Homework", "Homework", Color.BLUE,
                    LocalDateTime.of(2026, 3, 2, 17, 0), Privacy.PUBLIC, "user1"),
            new Event("Therapy Session", "Health", Color.RED,
                    LocalDateTime.of(2026, 3, 4, 14, 0), Privacy.PRIVATE, "user2"),
            new Event("Basketball Practice", "Sports", Color.GREEN,
                    LocalDateTime.of(2026, 3, 6, 15, 30), Privacy.PUBLIC, "user3"),
            new Event("Gym Workout", "Health", new Color(255, 100, 100),
                    LocalDateTime.of(2026, 3, 8, 7, 0), Privacy.PUBLIC, "user1"),
            new Event("Dentist Appointment", "Health", Color.RED,
                    LocalDateTime.of(2026, 3, 10, 9, 30), Privacy.PRIVATE, "user3"),
            new Event("Science Project", "Homework", Color.BLUE,
                    LocalDateTime.of(2026, 3, 12, 18, 0), Privacy.PUBLIC, "user1"),
            new Event("Soccer Game", "Sports", Color.GREEN,
                    LocalDateTime.of(2026, 3, 14, 17, 0), Privacy.PUBLIC, "user3"),
            new Event("History Test Prep", "Homework", new Color(100, 150, 255),
                    LocalDateTime.of(2026, 3, 16, 19, 0), Privacy.PUBLIC, "user1"),

            // APRIL 2026 (8 events)
            new Event("English Essay", "Homework", Color.BLUE,
                    LocalDateTime.of(2026, 4, 2, 20, 0), Privacy.PUBLIC, "user1"),
            new Event("Counseling", "Health", Color.RED,
                    LocalDateTime.of(2026, 4, 3, 9, 0), Privacy.PRIVATE, "user2"),
            new Event("Track Meet", "Sports", Color.GREEN,
                    LocalDateTime.of(2026, 4, 5, 16, 0), Privacy.PUBLIC, "user3"),
            new Event("Doctor Follow-up", "Health", Color.RED,
                    LocalDateTime.of(2026, 4, 7, 11, 0), Privacy.PRIVATE, "user1"),
            new Event("Group Project", "Homework", Color.BLUE,
                    LocalDateTime.of(2026, 4, 9, 18, 30), Privacy.PUBLIC, "user1"),
            new Event("Swim Practice", "Sports", Color.GREEN,
                    LocalDateTime.of(2026, 4, 11, 15, 0), Privacy.PUBLIC, "user3"),
            new Event("Lab Report", "Homework", Color.BLUE,
                    LocalDateTime.of(2026, 4, 13, 19, 0), Privacy.PUBLIC, "user1"),
            new Event("Eye Check", "Health", Color.RED,
                    LocalDateTime.of(2026, 4, 15, 10, 0), Privacy.PRIVATE, "user2"),

            // MAY 2026 (8 events)
            new Event("Finals Review", "Homework", Color.BLUE,
                    LocalDateTime.of(2026, 5, 2, 20, 0), Privacy.PUBLIC, "user1"),
            new Event("Physical Exam", "Health", Color.RED,
                    LocalDateTime.of(2026, 5, 3, 10, 0), Privacy.PRIVATE, "user3"),
            new Event("Baseball Game", "Sports", Color.GREEN,
                    LocalDateTime.of(2026, 5, 5, 17, 0), Privacy.PUBLIC, "user3"),
            new Event("Nutrition Check", "Health", Color.RED,
                    LocalDateTime.of(2026, 5, 7, 9, 0), Privacy.PRIVATE, "user1"),
            new Event("Essay Draft", "Homework", Color.BLUE,
                    LocalDateTime.of(2026, 5, 9, 18, 0), Privacy.PUBLIC, "user1"),
            new Event("Tennis Practice", "Sports", Color.GREEN,
                    LocalDateTime.of(2026, 5, 11, 16, 0), Privacy.PUBLIC, "user3"),
            new Event("Mock Exam", "Homework", Color.BLUE,
                    LocalDateTime.of(2026, 5, 13, 19, 0), Privacy.PUBLIC, "user1"),
            new Event("Therapy Follow-up", "Health", Color.RED,
                    LocalDateTime.of(2026, 5, 15, 14, 0), Privacy.PRIVATE, "user2"),

            // JUNE 2026 (8 events)
            new Event("Summer Reading", "Homework", Color.BLUE,
                    LocalDateTime.of(2026, 6, 2, 14, 0), Privacy.PUBLIC, "user1"),
            new Event("Eye Exam", "Health", Color.RED,
                    LocalDateTime.of(2026, 6, 3, 15, 0), Privacy.PRIVATE, "user2"),
            new Event("Camp Tryouts", "Sports", Color.GREEN,
                    LocalDateTime.of(2026, 6, 5, 13, 0), Privacy.PUBLIC, "user3"),
            new Event("General Checkup", "Health", Color.RED,
                    LocalDateTime.of(2026, 6, 7, 10, 0), Privacy.PRIVATE, "user1"),
            new Event("Essay Outline", "Homework", Color.BLUE,
                    LocalDateTime.of(2026, 6, 9, 17, 30), Privacy.PUBLIC, "user1"),
            new Event("Running Club", "Sports", Color.GREEN,
                    LocalDateTime.of(2026, 6, 11, 7, 0), Privacy.PUBLIC, "user3"),
            new Event("Reading Quiz Prep", "Homework", Color.BLUE,
                    LocalDateTime.of(2026, 6, 13, 19, 0), Privacy.PUBLIC, "user1"),
            new Event("Consultation", "Health", Color.RED,
                    LocalDateTime.of(2026, 6, 15, 9, 0), Privacy.PRIVATE, "user2")
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
            updateGrid();           // Rebuilds ALL base text (no countdown)
            updateCountdownOverlays(); // Only adds countdown to visible 24h events
        });

        nextBtn.addActionListener(e -> {
            currentMonth = currentMonth.plusMonths(1);
            updateMonthLabel();
            updateGrid();           // Rebuilds ALL base text (no countdown)
            updateCountdownOverlays(); // Only adds countdown to visible 24h events
        });

        header.add(prevBtn, BorderLayout.WEST);
        header.add(monthLabel, BorderLayout.CENTER);
        header.add(nextBtn, BorderLayout.EAST);

        String[] tags = { "Homework", "Sports", "Health", "All" };
        JComboBox<String> tagFilter = new JComboBox<>(tags);
        tagFilter.setSelectedItem("All");
        tagFilter.addActionListener(e -> {
            filterTag = (String) tagFilter.getSelectedItem();
            updateGrid();           // Rebuilds ALL base text (no countdown)
            updateCountdownOverlays(); // Only adds countdown to visible 24h events
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
            countdownTimer = new Timer(60000, e -> updateCountdownOverlays());
            countdownTimer.start();
        } else if (!countdownTimer.isRunning()) {
            countdownTimer.start();
        }
    }

    private void updateCountdownOverlays() {
        LocalDateTime now = LocalDateTime.now();

        // CRITICAL: First reset ALL buttons to base state (no countdown)
        LocalDate firstDay = currentMonth;
        DayOfWeek dow = firstDay.getDayOfWeek();
        int startIndex = dow.getValue() % 7;
        int lengthOfMonth = firstDay.lengthOfMonth();

        for (int day = 1; day <= lengthOfMonth; day++) {
            int index = startIndex + day - 1;
            if (index >= 0 && index < dayButtons.length) {
                LocalDate date = currentMonth.withDayOfMonth(day);
                JButton btn = dayButtons[index];
                updateButtonBaseText(btn, day, date);  // Pure base text, no countdown
            }
        }

        // Then ONLY add countdown for events that are:
        // 1. Within 24 hours AND
        // 2. In current month AND  
        // 3. Match current tag filter (so they are visible)
        for (Event event : events) {
            Duration timeUntil = Duration.between(now, event.startTime);
            if (timeUntil.isNegative() || timeUntil.toHours() > 24) continue;

            LocalDate eventDate = event.getDate();
            if (!eventDate.getMonth().equals(currentMonth.getMonth()) ||
                eventDate.getYear() != currentMonth.getYear()) continue;

            // CRITICAL: Only countdown for events that match CURRENT filter
            if (!isAllTagSelected() && !event.tag.equalsIgnoreCase(filterTag)) {
                continue;
            }

            int dayOfMonth = eventDate.getDayOfMonth();
            int index = startIndex + dayOfMonth - 1;
            if (index < 0 || index >= dayButtons.length) continue;

            JButton btn = dayButtons[index];
            String countdown = formatCountdown(timeUntil);
            String currentText = btn.getText();
            btn.setText(wrapWithCountdown(currentText, countdown));
        }
    }

    private String wrapWithCountdown(String baseText, String countdown) {
        String plain = baseText;
        if (plain.startsWith("<html>") && plain.endsWith("</html>")) {
            plain = plain.substring(6, plain.length() - 7);
        }
        return "<html>" + plain + 
               "<br><div style='font-size:10px; color:#666; margin-top:2px;'>⏳ " + countdown + "</div></html>";
    }

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
        btn.setBackground(null);
        btn.setToolTipText(null);

        StringBuilder html = new StringBuilder("<html>" + day);
        boolean hasEvent = false;

        for (Event e : events) {
            if (!e.getDate().equals(date)) continue;
            if (!isAllTagSelected() && !e.tag.equalsIgnoreCase(filterTag)) continue;

            hasEvent = true;
            btn.setBackground(e.color);

            String displayTitle = getDisplayTitle(e);
            String displayTag = getDisplayTag(e);
            String timeText = e.startTime.format(timeFmt);

            String oldTip = btn.getToolTipText();
            String eventText = displayTitle + " [" + displayTag + "] at " + timeText;
            if (oldTip == null || oldTip.isEmpty()) {
                btn.setToolTipText(eventText);
            } else {
                btn.setToolTipText(oldTip + "; " + eventText);
            }

            html.append("<br>").append(displayTitle).append(" ").append(timeText);
        }

        html.append("</html>");
        btn.setText(hasEvent ? html.toString() : String.valueOf(day));
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
            updateButtonBaseText(btn, day, date);  // Pure base, no countdown
        }
    }
}
