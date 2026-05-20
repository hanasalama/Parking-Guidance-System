package com.mycompany.finalinshallah;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.util.ArrayList;
import java.util.List;

/**
 * OperatorEntryPanel — Entry Operator screen.
 * Shows a live map of all parking spots.
 * FIX: spot buttons now call ParkingSystem.parkCar() / exitCar() so the
 * backend state is always in sync with what the operator sees.
 */
public class OperatorEntryPanel extends JFrame {

    private JPanel spotGrid;
    private JLabel statusLbl;
    private final List<JButton> spotButtons = new ArrayList<>();

    public OperatorEntryPanel() {
        setTitle("Entry Operator — Spot Monitor");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(520, 420);
        setLocationRelativeTo(null);
        setResizable(false);
        getContentPane().setBackground(Theme.BG_DARK);
        setLayout(new BorderLayout());

        add(Theme.header("Spot Monitor", "Live view of parking availability"), BorderLayout.NORTH);

        // Status bar
        JPanel statusBar = new JPanel(new FlowLayout(FlowLayout.LEFT, 16, 8));
        statusBar.setBackground(Theme.BG_SURFACE);
        statusBar.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Theme.BORDER));
        statusLbl = Theme.label("", Theme.FONT_BODY, Theme.TEXT_SEC);
        JLabel legendFree = Theme.label("● Available", Theme.FONT_SMALL, Theme.ACCENT_GREEN);
        JLabel legendOcc  = Theme.label("● Occupied",  Theme.FONT_SMALL, Theme.ACCENT_RED);
        statusBar.add(statusLbl);
        statusBar.add(Box.createRigidArea(new Dimension(20, 0)));
        statusBar.add(legendFree);
        statusBar.add(legendOcc);

        // Spot grid
        spotGrid = new JPanel();
        spotGrid.setBackground(Theme.BG_DARK);
        spotGrid.setBorder(BorderFactory.createEmptyBorder(20, 30, 10, 30));

        JPanel center = new JPanel(new BorderLayout());
        center.setBackground(Theme.BG_DARK);
        center.add(statusBar, BorderLayout.NORTH);
        center.add(spotGrid, BorderLayout.CENTER);
        add(center, BorderLayout.CENTER);

        // Footer
        JPanel footer = new JPanel(new FlowLayout(FlowLayout.CENTER, 12, 8));
        footer.setBackground(Theme.BG_DARK);

        JButton refreshBtn = Theme.pillButton("⟳  Refresh", Theme.BG_ELEVATED, Theme.TEXT_PRI);
        refreshBtn.addActionListener(e -> refreshSpots());

        JButton back = Theme.ghostButton("← Back");
        back.addActionListener(e -> { dispose(); new RoleSelector().setVisible(true); });

        footer.add(refreshBtn);
        footer.add(back);
        add(footer, BorderLayout.SOUTH);

        refreshSpots();
    }

    private void refreshSpots() {
        spotGrid.removeAll();
        spotButtons.clear();

        int cols = Math.min(3, ParkingSystem.spots.size());
        spotGrid.setLayout(new GridLayout(0, cols, 14, 14));

        for (Spot s : ParkingSystem.spots) {
            JButton btn = makeSpotButton(s);
            spotButtons.add(btn);
            spotGrid.add(btn);
        }

        int avail = ParkingSystem.availableCount();
        int total = ParkingSystem.spots.size();
        statusLbl.setText(avail + " of " + total + " spots available");

        spotGrid.revalidate();
        spotGrid.repaint();
    }

    private JButton makeSpotButton(Spot s) {
        boolean occupied = s.isOccupied();
        Color bg     = occupied ? new Color(60, 15, 15) : new Color(10, 35, 20);
        Color accent = occupied ? Theme.ACCENT_RED      : Theme.ACCENT_GREEN;
        String label = "<html><center><b>SPOT " + s.getSpotNumber() + "</b><br><small>"
                     + (occupied ? "Occupied" : "Free") + "</small></center></html>";

        JButton btn = new JButton(label) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                boolean hov = Boolean.TRUE.equals(getClientProperty("hov"));
                g2.setColor(hov ? bg.brighter() : bg);
                g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 12, 12));
                g2.setColor(accent);
                g2.setStroke(new BasicStroke(1.5f));
                g2.draw(new RoundRectangle2D.Float(0.75f, 0.75f, getWidth()-1.5f, getHeight()-1.5f, 12, 12));
                g2.dispose();
                super.paintComponent(g);
            }
        };
        btn.setForeground(occupied ? Theme.ACCENT_RED : Theme.ACCENT_GREEN);
        btn.setFont(Theme.FONT_BODY);
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setPreferredSize(new Dimension(130, 70));
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override public void mouseEntered(java.awt.event.MouseEvent e) { btn.putClientProperty("hov", true);  btn.repaint(); }
            @Override public void mouseExited (java.awt.event.MouseEvent e) { btn.putClientProperty("hov", false); btn.repaint(); }
        });

        btn.addActionListener(e -> handleSpotClick(s));
        return btn;
    }

    private void handleSpotClick(Spot s) {
        if (!s.isOccupied()) {
            // Advise customer of available spot
            int choice = JOptionPane.showConfirmDialog(this,
                    "Spot " + s.getSpotNumber() + " is FREE.\n" +
                    "Direct a customer here?\n\n" +
                    "(Click Yes to mark it as occupied manually)",
                    "Spot " + s.getSpotNumber(), JOptionPane.YES_NO_OPTION,
                    JOptionPane.INFORMATION_MESSAGE);
            if (choice == JOptionPane.YES_OPTION) {
                String plate = JOptionPane.showInputDialog(this,
                        "Enter customer plate number:", "Assign Spot", JOptionPane.PLAIN_MESSAGE);
                if (plate != null && !plate.isBlank()) {
                    Ticket t = ParkingSystem.parkCar(plate.trim());
                    if (t != null) {
                        JOptionPane.showMessageDialog(this,
                                "Ticket #" + t.getTicketId() + " issued for Spot " + s.getSpotNumber()
                                + "\nPlate: " + plate.trim(),
                                "Spot Assigned", JOptionPane.INFORMATION_MESSAGE);
                    }
                }
            }
        } else {
            Ticket t = s.getCurrentTicket();
            String info = t != null
                    ? "Plate: " + t.getCarNumber() + "\nTicket ID: " + t.getTicketId()
                      + "\nEntry: " + t.getEntryTime().toLocalTime().withNano(0)
                    : "Unknown occupant";
            JOptionPane.showMessageDialog(this,
                    "Spot " + s.getSpotNumber() + " is OCCUPIED.\n" + info,
                    "Spot Info", JOptionPane.INFORMATION_MESSAGE);
        }
        refreshSpots();
    }
}
