package com.mycompany.finalinshallah;

import javax.swing.*;
import java.awt.*;

/**
 * EntryStation — Customer enters plate number and receives a parking ticket.
 */
public class EntryStation extends JFrame {

    private JTextField plateField;

    public EntryStation() {
        setTitle("Entry Station — Print Ticket");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(400, 320);
        setLocationRelativeTo(null);
        setResizable(false);
        getContentPane().setBackground(Theme.BG_DARK);
        setLayout(new BorderLayout());

        add(Theme.header("Entry Station", "Enter your plate number to get a parking ticket"),
                BorderLayout.NORTH);

        // Form
        JPanel form = new JPanel();
        form.setBackground(Theme.BG_DARK);
        form.setLayout(new BoxLayout(form, BoxLayout.Y_AXIS));
        form.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));

        JLabel plateLbl = Theme.label("Plate Number", Theme.FONT_BODY, Theme.TEXT_SEC);
        plateLbl.setAlignmentX(Component.LEFT_ALIGNMENT);

        plateField = Theme.textField("e.g. ABC-1234");
        plateField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        plateField.setAlignmentX(Component.LEFT_ALIGNMENT);

        JPanel info = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        info.setBackground(Theme.BG_DARK);
        info.setAlignmentX(Component.LEFT_ALIGNMENT);
        JLabel infoLbl = Theme.label("Rate: 20 EGP per hour  •  Min. 1 hour", Theme.FONT_SMALL, Theme.TEXT_MUT);
        info.add(infoLbl);

        JButton printBtn = Theme.pillButton("Print Ticket", Theme.ACCENT_BLUE, Color.WHITE);
        printBtn.setAlignmentX(Component.LEFT_ALIGNMENT);
        printBtn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        printBtn.addActionListener(e -> printTicket());

        // Allow Enter key
        plateField.addActionListener(e -> printTicket());

        form.add(plateLbl);
        form.add(Box.createRigidArea(new Dimension(0, 8)));
        form.add(plateField);
        form.add(Box.createRigidArea(new Dimension(0, 6)));
        form.add(info);
        form.add(Box.createRigidArea(new Dimension(0, 16)));
        form.add(printBtn);

        add(form, BorderLayout.CENTER);

        JPanel footer = new JPanel(new FlowLayout(FlowLayout.CENTER));
        footer.setBackground(Theme.BG_DARK);
        JButton back = Theme.ghostButton("← Back");
        back.addActionListener(e -> { dispose(); new CustomerPanel().setVisible(true); });
        footer.add(back);
        add(footer, BorderLayout.SOUTH);
    }

    private void printTicket() {
        String plate = plateField.getText().trim();
        if (plate.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter a plate number first!",
                    "Missing Input", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Ticket t = ParkingSystem.parkCar(plate);
        if (t != null) {
            plateField.setText("");
            JOptionPane.showMessageDialog(this,
                    "╔══════════════════════════╗\n" +
                    "     TICKET PRINTED 🎫       \n" +
                    "╠══════════════════════════╣\n" +
                    "  Ticket ID  : " + t.getTicketId()  + "\n" +
                    "  Plate No.  : " + t.getCarNumber() + "\n" +
                    "  Entry Time : " + t.getEntryTime().toLocalTime().withNano(0) + "\n" +
                    "  Rate       : 20 EGP / hour\n" +
                    "╚══════════════════════════╝\n" +
                    "  Keep this ticket — you need the\n" +
                    "  Ticket ID when you exit.",
                    "Ticket Printed", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this,
                    "Sorry, the parking is FULL!\nNo spots available right now.",
                    "Parking Full", JOptionPane.WARNING_MESSAGE);
        }
    }
}
