package com.mycompany.finalinshallah;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.RoundRectangle2D;


public class RoleSelector extends JFrame {

    public RoleSelector() {
        setTitle("Parking Guidance System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(520, 520);
        setLocationRelativeTo(null);
        setResizable(false);
        getContentPane().setBackground(Theme.BG_DARK);
        setLayout(new BorderLayout());


        add(Theme.header("Parking Guidance System", "Select your role to continue"), BorderLayout.NORTH);


        JPanel grid = new JPanel(new GridLayout(2, 2, 14, 14));
        grid.setBackground(Theme.BG_DARK);
        grid.setBorder(BorderFactory.createEmptyBorder(16, 28, 28, 28));

        grid.add(makeRoleCard("Customer",
                "Print ticket · Pay at exit · View receipt",
                "🚗", Theme.RAMP_BLUE,
                e -> { dispose(); new CustomerPanel().setVisible(true); }));

        grid.add(makeRoleCard("Entry Operator",
                "Monitor free spots · Assign parking",
                "🟢", Theme.RAMP_GREEN,
                e -> { dispose(); new OperatorEntryPanel().setVisible(true); }));

        grid.add(makeRoleCard("Exit Operator",
                "Process payments · Release spots",
                "🔴", Theme.RAMP_AMBER,
                e -> { dispose(); new ExitStation().setVisible(true); }));

        grid.add(makeRoleCard("Admin",
                "Spots · Users · Reports · Payments",
                "⚙", Theme.RAMP_PURPLE,
                e -> { dispose(); new AdminMenu().setVisible(true); }));

        add(grid, BorderLayout.CENTER);

        JPanel footer = new JPanel(new FlowLayout(FlowLayout.CENTER));
        footer.setBackground(Theme.BG_DARK);
        JLabel ver = Theme.label("Parking System v1.0  •  20 EGP / hour",
                Theme.FONT_SMALL, Theme.TEXT_MUT);
        footer.add(ver);
        add(footer, BorderLayout.SOUTH);
    }
    
    private JPanel makeRoleCard(String role, String desc, String emoji,
                                Color[] ramp, ActionListener action) {
        JPanel card = new JPanel(new BorderLayout(0, 10)) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                boolean hov = Boolean.TRUE.equals(getClientProperty("hov"));
                g2.setColor(hov ? ramp[0].brighter() : ramp[0]);
                g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 14, 14));
                g2.setColor(ramp[1]);
                g2.fillRoundRect(0, 0, getWidth(), 4, 4, 4);
                g2.setColor(new Color(ramp[1].getRed(), ramp[1].getGreen(), ramp[1].getBlue(), 60));
                g2.setStroke(new BasicStroke(1f));
                g2.draw(new RoundRectangle2D.Float(0.5f, 0.5f, getWidth()-1, getHeight()-1, 14, 14));
                g2.dispose();
                super.paintComponent(g);
            }
        };
        card.setOpaque(false);
        card.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        card.setBorder(BorderFactory.createEmptyBorder(18, 18, 18, 18));


        JLabel icon = new JLabel(emoji);
        icon.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 28));


        JLabel roleLbl = Theme.label(role, Theme.FONT_HEAD, ramp[2]);


        JLabel descLbl = new JLabel("<html><div style='width:120px'>" + desc + "</div></html>");
        descLbl.setForeground(ramp[3]);
        descLbl.setFont(Theme.FONT_SMALL);

        JPanel text = new JPanel(new GridLayout(2, 1, 0, 4));
        text.setOpaque(false);
        text.add(roleLbl);
        text.add(descLbl);

        JLabel arrow = Theme.label("›", new Font("Segoe UI", Font.PLAIN, 24), ramp[3]);

        card.add(icon,  BorderLayout.NORTH);
        card.add(text,  BorderLayout.CENTER);
        card.add(arrow, BorderLayout.EAST);

        card.addMouseListener(new MouseAdapter() {
            @Override public void mouseEntered(MouseEvent e) { card.putClientProperty("hov", true);  card.repaint(); }
            @Override public void mouseExited (MouseEvent e) { card.putClientProperty("hov", false); card.repaint(); }
            @Override public void mouseClicked(MouseEvent e) { action.actionPerformed(null); }
        });

        return card;
    }

    public static void main(String[] args) {
        Theme.applyLAF();
        SwingUtilities.invokeLater(() -> new RoleSelector().setVisible(true));
    }
}
