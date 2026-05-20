package com.mycompany.finalinshallah;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.RoundRectangle2D;

/**
 * CustomerPanel — lets a customer:
 *   1. Print a ticket (go to EntryStation)
 *   2. Pay and exit (go to ExitStation)
 *   3. View a receipt by ticket ID (inline)
 */
public class CustomerPanel extends JFrame {

    public CustomerPanel() {
        setTitle("Customer — Parking System");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(420, 460);
        setLocationRelativeTo(null);
        setResizable(false);
        getContentPane().setBackground(Theme.BG_DARK);
        setLayout(new BorderLayout());

        add(Theme.header("Customer", "What would you like to do?"), BorderLayout.NORTH);

        // Cards
        JPanel cards = new JPanel();
        cards.setLayout(new BoxLayout(cards, BoxLayout.Y_AXIS));
        cards.setBackground(Theme.BG_DARK);
        cards.setBorder(BorderFactory.createEmptyBorder(12, 24, 12, 24));

        cards.add(makeCard("🎫", "Print Parking Ticket",
                "Enter your plate number and get a ticket",
                Theme.RAMP_BLUE,  e -> { dispose(); new EntryStation().setVisible(true); }));
        cards.add(Box.createRigidArea(new Dimension(0, 12)));
        cards.add(makeCard("💳", "Pay & Exit",
                "Enter your ticket ID to pay and leave",
                Theme.RAMP_GREEN, e -> { dispose(); new ExitStation().setVisible(true); }));
        cards.add(Box.createRigidArea(new Dimension(0, 12)));
        cards.add(makeCard("🧾", "View Receipt",
                "Check your bill for any ticket ID",
                Theme.RAMP_AMBER, e -> viewReceipt()));

        add(cards, BorderLayout.CENTER);

        // Back button
        JPanel footer = new JPanel(new FlowLayout(FlowLayout.CENTER));
        footer.setBackground(Theme.BG_DARK);
        JButton back = Theme.ghostButton("← Back to role selection");
        back.addActionListener(e -> { dispose(); new RoleSelector().setVisible(true); });
        footer.add(back);
        add(footer, BorderLayout.SOUTH);
    }

    private JPanel makeCard(String emoji, String title, String sub,
                            Color[] ramp, ActionListener action) {
        JPanel card = new JPanel(new BorderLayout(14, 0)) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                boolean hov = Boolean.TRUE.equals(getClientProperty("hov"));
                g2.setColor(hov ? brighter(ramp[0]) : ramp[0]);
                g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 12, 12));
                g2.setColor(new Color(ramp[1].getRed(), ramp[1].getGreen(), ramp[1].getBlue(), 80));
                g2.setStroke(new BasicStroke(1f));
                g2.draw(new RoundRectangle2D.Float(0.5f,0.5f,getWidth()-1,getHeight()-1,12,12));
                g2.dispose();
                super.paintComponent(g);
            }
        };
        card.setOpaque(false);
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 80));
        card.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        card.setBorder(BorderFactory.createEmptyBorder(14, 16, 14, 16));
        card.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Icon circle
        JPanel circle = new JPanel(new GridBagLayout());
        circle.setOpaque(false);
        circle.setPreferredSize(new Dimension(46, 46));
        JLabel icon = new JLabel(emoji);
        icon.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 22));
        circle.add(icon);

        JLabel titleLbl = Theme.label(title, Theme.FONT_HEAD, ramp[2]);
        JLabel subLbl   = Theme.label(sub,   Theme.FONT_SMALL, ramp[3]);
        JPanel text = new JPanel(new GridLayout(2,1,0,3));
        text.setOpaque(false);
        text.add(titleLbl);
        text.add(subLbl);

        JLabel chev = Theme.label("›", new Font("Segoe UI", Font.PLAIN, 22), ramp[3]);

        card.add(circle, BorderLayout.WEST);
        card.add(text,   BorderLayout.CENTER);
        card.add(chev,   BorderLayout.EAST);

        card.addMouseListener(new MouseAdapter() {
            @Override public void mouseEntered(MouseEvent e) { card.putClientProperty("hov", true);  card.repaint(); }
            @Override public void mouseExited (MouseEvent e) { card.putClientProperty("hov", false); card.repaint(); }
            @Override public void mouseClicked(MouseEvent e) { action.actionPerformed(null); }
        });
        return card;
    }

    private void viewReceipt() {
        String input = JOptionPane.showInputDialog(this,
                "Enter your Ticket ID:", "View Receipt", JOptionPane.PLAIN_MESSAGE);
        if (input == null || input.isBlank()) return;
        try {
            int id = Integer.parseInt(input.trim());
            Ticket found = null;
            for (Ticket t : ParkingSystem.tickets) {
                if (t.getTicketId() == id) { found = t; break; }
            }
            if (found != null) {
                long hours = java.time.Duration.between(found.getEntryTime(),
                        java.time.LocalDateTime.now()).toHours();
                if (hours == 0) hours = 1;
                double amount = hours * 20.0;
                JOptionPane.showMessageDialog(this,
                        "╔══════════════════════════╗\n" +
                        "       PARKING RECEIPT       \n" +
                        "╠══════════════════════════╣\n" +
                        "  Ticket ID  : " + found.getTicketId()   + "\n" +
                        "  Plate No.  : " + found.getCarNumber()  + "\n" +
                        "  Entry Time : " + found.getEntryTime().toLocalTime().withNano(0) + "\n" +
                        "  Duration   : " + hours + " hr(s)\n" +
                        "  Amount Due : " + amount + " EGP\n" +
                        "╚══════════════════════════╝",
                        "Receipt — Ticket #" + id, JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this,
                        "Ticket #" + id + " not found.\nPlease check the ID.",
                        "Not Found", JOptionPane.WARNING_MESSAGE);
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this,
                    "Please enter a valid numeric Ticket ID.",
                    "Invalid Input", JOptionPane.ERROR_MESSAGE);
        }
    }

    private Color brighter(Color c) {
        return new Color(Math.min(255, c.getRed()+18),
                         Math.min(255, c.getGreen()+18),
                         Math.min(255, c.getBlue()+18));
    }
}
