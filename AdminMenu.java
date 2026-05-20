package com.mycompany.finalinshallah;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;

/**
 * AdminMenu — full admin dashboard with:
 *   1. Add Spot
 *   2. View Total Spots
 *   3. Manage Users (Add / Update / Delete)
 *   4. Shifts & Payment Report
 *   5. Parked Cars Report
 */
public class AdminMenu extends JFrame {

    private final AdminAction admin = new AdminAction();

    public AdminMenu() {
        setTitle("Admin Dashboard — Parking System");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(520, 480);
        setLocationRelativeTo(null);
        setResizable(false);
        getContentPane().setBackground(Theme.BG_DARK);
        setLayout(new BorderLayout());

        add(Theme.header("Admin Dashboard", "Manage the parking system"), BorderLayout.NORTH);

        // ── 3×2 menu grid ────────────────────────────────────────
        JPanel grid = new JPanel(new GridLayout(3, 2, 14, 14));
        grid.setBackground(Theme.BG_DARK);
        grid.setBorder(BorderFactory.createEmptyBorder(16, 28, 10, 28));

        grid.add(menuCard("➕", "Add Spot",           "Register a new parking spot",         Theme.RAMP_BLUE,   this::doAddSpot));
        grid.add(menuCard("🅿", "View Spots",          "See status of all parking spots",      Theme.RAMP_GREEN,  this::doViewSpots));
        grid.add(menuCard("👥", "Manage Users",        "Add, update or remove system users",   Theme.RAMP_PURPLE, this::doManageUsers));
        grid.add(menuCard("💰", "Shifts & Payments",   "View payment history and revenue",     Theme.RAMP_AMBER,  this::doPayments));
        grid.add(menuCard("🚘", "Parked Cars Report",  "See which spots are occupied now",     Theme.RAMP_BLUE,   this::doParkedCars));
        grid.add(menuCard("🔙", "Back",                "Return to role selection",             new Color[]{
                new Color(30,15,15), Theme.ACCENT_RED,
                new Color(250,120,120), new Color(200,80,80)},               this::doBack));

        add(grid, BorderLayout.CENTER);

        // Stats footer bar
        JPanel stats = new JPanel(new FlowLayout(FlowLayout.CENTER, 30, 8));
        stats.setBackground(Theme.BG_SURFACE);
        stats.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, Theme.BORDER));

        JLabel spotsLbl = Theme.label(
                "Spots: " + ParkingSystem.spots.size() + " total  |  " +
                ParkingSystem.occupiedCount() + " occupied",
                Theme.FONT_SMALL, Theme.TEXT_SEC);
        JLabel usersLbl = Theme.label(
                "Users: " + AdminAction.users.size(),
                Theme.FONT_SMALL, Theme.TEXT_SEC);
        JLabel revLbl = Theme.label(
                "Revenue: " + String.format("%.2f", admin.getTotalRevenue()) + " EGP",
                Theme.FONT_SMALL, Theme.ACCENT_GREEN);

        stats.add(spotsLbl);
        stats.add(usersLbl);
        stats.add(revLbl);
        add(stats, BorderLayout.SOUTH);
    }

    // ── Menu card builder ────────────────────────────────────────
    private JPanel menuCard(String emoji, String title, String sub,
                            Color[] ramp, Runnable action) {
        JPanel card = new JPanel(new BorderLayout(10, 0)) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                boolean hov = Boolean.TRUE.equals(getClientProperty("hov"));
                g2.setColor(hov ? ramp[0].brighter() : ramp[0]);
                g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 12, 12));
                g2.setColor(new Color(ramp[1].getRed(), ramp[1].getGreen(), ramp[1].getBlue(), 70));
                g2.setStroke(new BasicStroke(1f));
                g2.draw(new RoundRectangle2D.Float(0.5f,0.5f,getWidth()-1,getHeight()-1,12,12));
                g2.dispose();
                super.paintComponent(g);
            }
        };
        card.setOpaque(false);
        card.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        card.setBorder(BorderFactory.createEmptyBorder(14, 16, 14, 14));

        JLabel icon  = Theme.label(emoji, new Font("Segoe UI Emoji", Font.PLAIN, 22), ramp[2]);
        JLabel tLbl  = Theme.label(title, Theme.FONT_HEAD, ramp[2]);
        JLabel sLbl  = Theme.label(sub,   Theme.FONT_SMALL, ramp[3]);

        JPanel text = new JPanel(new GridLayout(2,1,0,3));
        text.setOpaque(false);
        text.add(tLbl);
        text.add(sLbl);

        card.add(icon, BorderLayout.WEST);
        card.add(text, BorderLayout.CENTER);

        card.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override public void mouseEntered(java.awt.event.MouseEvent e) { card.putClientProperty("hov", true);  card.repaint(); }
            @Override public void mouseExited (java.awt.event.MouseEvent e) { card.putClientProperty("hov", false); card.repaint(); }
            @Override public void mouseClicked(java.awt.event.MouseEvent e) { action.run(); }
        });
        return card;
    }

    // ── 1. Add Spot ──────────────────────────────────────────────
    private void doAddSpot() {
        String input = JOptionPane.showInputDialog(this,
                "Enter new Spot Number (numeric):", "Add Parking Spot", JOptionPane.PLAIN_MESSAGE);
        if (input == null || input.isBlank()) return;
        try {
            int id = Integer.parseInt(input.trim());
            if (admin.addSpot(id)) {
                JOptionPane.showMessageDialog(this,
                        "✓ Spot " + id + " added successfully!\nTotal spots: " + admin.totalSpots(),
                        "Spot Added", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this,
                        "Spot " + id + " already exists.", "Duplicate", JOptionPane.WARNING_MESSAGE);
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Please enter a valid number.",
                    "Invalid Input", JOptionPane.ERROR_MESSAGE);
        }
    }

    // ── 2. View Spots ────────────────────────────────────────────
    private void doViewSpots() {
        String[] cols = {"Spot #", "Status", "Plate (if occupied)", "Ticket ID"};
        DefaultTableModel model = new DefaultTableModel(cols, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        for (Spot s : ParkingSystem.spots) {
            String status = s.isOccupied() ? "🔴 Occupied" : "🟢 Available";
            String plate  = "—";
            String tid    = "—";
            if (s.isOccupied() && s.getCurrentTicket() != null) {
                plate = s.getCurrentTicket().getCarNumber();
                tid   = String.valueOf(s.getCurrentTicket().getTicketId());
            }
            model.addRow(new Object[]{"Spot " + s.getSpotNumber(), status, plate, tid});
        }

        JTable table = new JTable(model);
        Theme.styleTable(table);
        JScrollPane sp = Theme.scrollPane(table);
        sp.setPreferredSize(new Dimension(460, 200));

        JLabel summary = Theme.label(
                "Total: " + ParkingSystem.spots.size() +
                "   Available: " + ParkingSystem.availableCount() +
                "   Occupied: " + ParkingSystem.occupiedCount(),
                Theme.FONT_BODY, Theme.ACCENT_GREEN);
        summary.setBorder(BorderFactory.createEmptyBorder(8, 4, 2, 4));

        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Theme.BG_SURFACE);
        panel.add(sp, BorderLayout.CENTER);
        panel.add(summary, BorderLayout.SOUTH);

        JOptionPane.showMessageDialog(this, panel, "Parking Spots", JOptionPane.PLAIN_MESSAGE);
    }

    // ── 3. Manage Users ──────────────────────────────────────────
    private void doManageUsers() {
        JDialog dialog = new JDialog(this, "Manage Users", true);
        dialog.setSize(540, 400);
        dialog.setLocationRelativeTo(this);
        dialog.getContentPane().setBackground(Theme.BG_DARK);
        dialog.setLayout(new BorderLayout());

        // Table
        String[] cols = {"Username", "Password", "Role"};
        DefaultTableModel model = new DefaultTableModel(cols, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        refreshUserTable(model);

        JTable table = new JTable(model);
        Theme.styleTable(table);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane sp = Theme.scrollPane(table);

        dialog.add(sp, BorderLayout.CENTER);

        // Button row
        JPanel btnRow = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        btnRow.setBackground(Theme.BG_DARK);
        btnRow.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, Theme.BORDER));

        JButton addBtn  = Theme.pillButton("+ Add User",    Theme.ACCENT_BLUE,  Color.WHITE);
        JButton editBtn = Theme.pillButton("✎ Update",      new Color(40,80,40),Theme.ACCENT_GREEN);
        JButton delBtn  = Theme.pillButton("✕ Delete",      new Color(70,15,15),Theme.ACCENT_RED);
        JButton closeBtn= Theme.ghostButton("Close");

        btnRow.add(addBtn);
        btnRow.add(editBtn);
        btnRow.add(delBtn);
        btnRow.add(closeBtn);
        dialog.add(btnRow, BorderLayout.SOUTH);

        // ── Add ──
        addBtn.addActionListener(e -> {
            JTextField uField = Theme.textField("");
            JTextField pField = Theme.textField("");
            JComboBox<String> roleBox = Theme.comboBox(new String[]{"Customer","Operator","Admin"});
            Object[] fields = {
                Theme.label("Username:", Theme.FONT_BODY, Theme.TEXT_PRI), uField,
                Theme.label("Password:", Theme.FONT_BODY, Theme.TEXT_PRI), pField,
                Theme.label("Role:",     Theme.FONT_BODY, Theme.TEXT_PRI), roleBox
            };
            int res = JOptionPane.showConfirmDialog(dialog, fields, "Add User",
                    JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
            if (res != JOptionPane.OK_OPTION) return;
            String u = uField.getText().trim();
            String p = pField.getText().trim();
            if (u.isEmpty() || p.isEmpty()) {
                JOptionPane.showMessageDialog(dialog, "Username and password cannot be empty.");
                return;
            }
            if (admin.addUser(u, p, (String) roleBox.getSelectedItem())) {
                refreshUserTable(model);
            } else {
                JOptionPane.showMessageDialog(dialog, "Username already exists!", "Duplicate", JOptionPane.WARNING_MESSAGE);
            }
        });

        // ── Update ──
        editBtn.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row < 0) { JOptionPane.showMessageDialog(dialog, "Select a user first."); return; }
            String uname = (String) model.getValueAt(row, 0);
            JTextField pField = Theme.textField((String) model.getValueAt(row, 1));
            JComboBox<String> roleBox = Theme.comboBox(new String[]{"Customer","Operator","Admin"});
            roleBox.setSelectedItem(model.getValueAt(row, 2));
            Object[] fields = {
                Theme.label("New Password:", Theme.FONT_BODY, Theme.TEXT_PRI), pField,
                Theme.label("New Role:",     Theme.FONT_BODY, Theme.TEXT_PRI), roleBox
            };
            int res = JOptionPane.showConfirmDialog(dialog, fields, "Update: " + uname,
                    JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
            if (res == JOptionPane.OK_OPTION) {
                admin.updateUser(uname, pField.getText().trim(), (String) roleBox.getSelectedItem());
                refreshUserTable(model);
            }
        });

        // ── Delete ──
        delBtn.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row < 0) { JOptionPane.showMessageDialog(dialog, "Select a user first."); return; }
            String uname = (String) model.getValueAt(row, 0);
            int confirm = JOptionPane.showConfirmDialog(dialog,
                    "Delete user \"" + uname + "\"? This cannot be undone.",
                    "Confirm Delete", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
            if (confirm == JOptionPane.YES_OPTION) {
                admin.deleteUser(uname);
                refreshUserTable(model);
            }
        });

        closeBtn.addActionListener(e -> dialog.dispose());
        dialog.setVisible(true);
    }

    private void refreshUserTable(DefaultTableModel model) {
        model.setRowCount(0);
        for (User u : AdminAction.users) {
            model.addRow(new Object[]{u.getUsername(), u.getPassword(), u.getRole()});
        }
    }

    // ── 4. Shifts & Payments ─────────────────────────────────────
    private void doPayments() {
        String[] cols = {"Ticket ID", "Plate", "Hours", "Amount (EGP)", "Timestamp"};
        DefaultTableModel model = new DefaultTableModel(cols, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };

        for (String[] rec : AdminAction.paymentRecords) {
            model.addRow(rec);
        }
        if (model.getRowCount() == 0) {
            model.addRow(new Object[]{"—", "No payments recorded yet", "—", "—", "—"});
        }

        JTable table = new JTable(model);
        Theme.styleTable(table);
        JScrollPane sp = Theme.scrollPane(table);
        sp.setPreferredSize(new Dimension(480, 220));

        JLabel totalLbl = Theme.label(
                "  Total Revenue: " + String.format("%.2f", admin.getTotalRevenue()) + " EGP",
                Theme.FONT_HEAD, Theme.ACCENT_GREEN);
        totalLbl.setBorder(BorderFactory.createEmptyBorder(10, 4, 4, 4));

        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Theme.BG_SURFACE);
        panel.add(sp, BorderLayout.CENTER);
        panel.add(totalLbl, BorderLayout.SOUTH);

        JOptionPane.showMessageDialog(this, panel, "Shifts & Payment Report", JOptionPane.PLAIN_MESSAGE);
    }

    // ── 5. Parked Cars Report ─────────────────────────────────────
    private void doParkedCars() {
        String[] cols = {"Spot #", "Ticket ID", "Plate Number", "Entry Time"};
        DefaultTableModel model = new DefaultTableModel(cols, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };

        for (Spot s : ParkingSystem.spots) {
            if (s.isOccupied()) {
                Ticket t = s.getCurrentTicket(); // FIX: use spot's own ticket reference
                if (t != null) {
                    model.addRow(new Object[]{
                        "Spot " + s.getSpotNumber(),
                        t.getTicketId(),
                        t.getCarNumber(),
                        t.getEntryTime().toString().replace("T", " ").substring(0, 19)
                    });
                }
            }
        }

        if (model.getRowCount() == 0) {
            model.addRow(new Object[]{"—", "—", "No cars currently parked", "—"});
        }

        JTable table = new JTable(model);
        Theme.styleTable(table);
        JScrollPane sp = Theme.scrollPane(table);
        sp.setPreferredSize(new Dimension(460, 200));

        JLabel count = Theme.label(
                "  Cars parked: " + ParkingSystem.occupiedCount() +
                " / " + ParkingSystem.spots.size(),
                Theme.FONT_BODY, Theme.TEXT_SEC);
        count.setBorder(BorderFactory.createEmptyBorder(8, 4, 4, 4));

        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Theme.BG_SURFACE);
        panel.add(sp, BorderLayout.CENTER);
        panel.add(count, BorderLayout.SOUTH);

        JOptionPane.showMessageDialog(this, panel, "Parked Cars Report", JOptionPane.PLAIN_MESSAGE);
    }

    // ── Back ──────────────────────────────────────────────────────
    private void doBack() {
        dispose();
        new RoleSelector().setVisible(true);
    }
}
