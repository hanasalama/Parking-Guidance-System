package com.mycompany.finalinshallah;

import javax.swing.*;
import java.awt.*;
import java.time.Duration;
import java.time.LocalDateTime;

/**
 * ExitStation — Customer enters Ticket ID to pay and release their spot.
 * Records the payment so the Admin shift report is populated.
 */
public class ExitStation extends JFrame {

    private JTextField ticketField;

    public ExitStation() {
        setTitle("Exit Station — Pay & Exit");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(400, 320);
        setLocationRelativeTo(null);
        setResizable(false);
        getContentPane().setBackground(Theme.BG_DARK);
        setLayout(new BorderLayout());

        add(Theme.header("Exit Station", "Enter your Ticket ID to pay and release your spot"),
                BorderLayout.NORTH);

        // Form
        JPanel form = new JPanel();
        form.setBackground(Theme.BG_DARK);
        form.setLayout(new BoxLayout(form, BoxLayout.Y_AXIS));
        form.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));

        JLabel lbl = Theme.label("Ticket ID", Theme.FONT_BODY, Theme.TEXT_SEC);
        lbl.setAlignmentX(Component.LEFT_ALIGNMENT);

        ticketField = Theme.textField("Enter numeric ticket ID");
        ticketField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        ticketField.setAlignmentX(Component.LEFT_ALIGNMENT);

        JButton payBtn = Theme.pillButton("Pay & Exit  →", Theme.ACCENT_GREEN, Color.WHITE);
        payBtn.setAlignmentX(Component.LEFT_ALIGNMENT);
        payBtn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        payBtn.addActionListener(e -> processPayment());
        ticketField.addActionListener(e -> processPayment());

        form.add(lbl);
        form.add(Box.createRigidArea(new Dimension(0, 8)));
        form.add(ticketField);
        form.add(Box.createRigidArea(new Dimension(0, 16)));
        form.add(payBtn);

        add(form, BorderLayout.CENTER);

        JPanel footer = new JPanel(new FlowLayout(FlowLayout.CENTER));
        footer.setBackground(Theme.BG_DARK);
        JButton back = Theme.ghostButton("← Back");
        back.addActionListener(e -> { dispose(); new RoleSelector().setVisible(true); });
        footer.add(back);
        add(footer, BorderLayout.SOUTH);
    }

    private void processPayment() {
        String idText = ticketField.getText().trim();
        if (idText.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter a Ticket ID!",
                    "Missing Input", JOptionPane.WARNING_MESSAGE);
            return;
        }
        try {
            int ticketId = Integer.parseInt(idText);
            Ticket found = ParkingSystem.exitCar(ticketId);

            if (found != null) {
                LocalDateTime exitTime = LocalDateTime.now();
                long hours = Duration.between(found.getEntryTime(), exitTime).toHours();
                if (hours == 0) hours = 1; // minimum 1 hour

                double totalAmount = hours * 20.0;

                // Record payment for admin report
                AdminAction.paymentRecords.add(new String[]{
                    String.valueOf(found.getTicketId()),
                    found.getCarNumber(),
                    String.valueOf(hours),
                    String.format("%.2f", totalAmount),
                    exitTime.toString().replace("T", " ").substring(0, 19)
                });

                ticketField.setText("");
                JOptionPane.showMessageDialog(this,
                        "╔══════════════════════════╗\n" +
                        "     PAYMENT SUCCESSFUL ✓   \n" +
                        "╠══════════════════════════╣\n" +
                        "  Ticket ID   : " + found.getTicketId()  + "\n" +
                        "  Plate No.   : " + found.getCarNumber() + "\n" +
                        "  Hours Parked: " + hours + " hr(s)\n" +
                        "  Amount Paid : " + String.format("%.2f", totalAmount) + " EGP\n" +
                        "╚══════════════════════════╝\n" +
                        "  Thank you for visiting!",
                        "Payment Successful", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this,
                        "Ticket #" + ticketId + " not found.\nPlease check the ID.",
                        "Not Found", JOptionPane.WARNING_MESSAGE);
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this,
                    "Please enter a valid numeric Ticket ID.",
                    "Invalid Input", JOptionPane.ERROR_MESSAGE);
        }
    }
}
