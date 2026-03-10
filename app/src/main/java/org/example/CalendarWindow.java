package org.example;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class CalendarWindow {

    /* 
    // keep a reference so nothing is weirdly lost
    private final JFrame frame;
     
     public CalendarWindow() {
     frame = new JFrame("Calendar TEST");
     frame.setSize(600, 400);
     frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
     
     // Force it to appear in front + centered
     frame.setLocationRelativeTo(null);
     frame.setAlwaysOnTop(true); // TEMP: remove after it works
     frame.setVisible(true);
     
     // TEMP: bring to front (sometimes Windows puts it behind)
     frame.toFront();
     frame.requestFocus();
     }
    */ 

    private LocalDate currentMonth = LocalDate.now().withDayOfMonth(1);
    private final JLabel monthLabel = new JLabel("", SwingConstants.CENTER);

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new CalendarWindow().start());
    }

    public void start() {
        JFrame frame = new JFrame("Calendar");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Main container
        JPanel root = new JPanel(new BorderLayout(10, 10));
        root.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Header (Prev | Month | Next)
        JPanel header = new JPanel(new BorderLayout());
        JButton prevBtn = new JButton("<=");
        JButton nextBtn = new JButton("=>");

        prevBtn.addActionListener(e -> {
            currentMonth = currentMonth.minusMonths(1);
            updateMonthLabel();
        });

        nextBtn.addActionListener(e -> {
            currentMonth = currentMonth.plusMonths(1);
            updateMonthLabel();
        });

        header.add(prevBtn, BorderLayout.WEST);
        header.add(monthLabel, BorderLayout.CENTER);
        header.add(nextBtn, BorderLayout.EAST);

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
            grid.add(cell);
        }

        JPanel center = new JPanel(new BorderLayout(0, 5));
        center.add(dow, BorderLayout.NORTH);
        center.add(grid, BorderLayout.CENTER);

        root.add(header, BorderLayout.NORTH);
        root.add(center, BorderLayout.CENTER);

        // Put UI in the frame BEFORE showing it
        frame.setContentPane(root);

        // Either pack() (preferred) OR setSize(...)
        frame.pack();
        frame.setSize(800, 600); // optional; remove if you prefer pack only
        frame.setLocationRelativeTo(null);

        updateMonthLabel();

        frame.setVisible(true); // show ONCE, at the end
    }

    private void updateMonthLabel() {
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("MMMM yyyy");
        monthLabel.setText(currentMonth.format(fmt));
    }

}
