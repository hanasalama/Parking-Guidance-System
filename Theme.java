package com.mycompany.finalinshallah;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;

public class Theme {

    public static final Color BG_DARK      = new Color(13,  17,  23);   
    public static final Color BG_SURFACE   = new Color(22,  30,  44);   
    public static final Color BG_ELEVATED  = new Color(30,  41,  59);   
    public static final Color ACCENT_BLUE  = new Color(56, 139, 253);   
    public static final Color ACCENT_GREEN = new Color(63, 185, 119);   
    public static final Color ACCENT_RED   = new Color(248, 81,  73);  
    public static final Color ACCENT_AMBER = new Color(210,153,  34);   
    public static final Color BORDER       = new Color(48,  54,  61);   
    public static final Color TEXT_PRI     = new Color(230, 237, 243);  
    public static final Color TEXT_SEC     = new Color(125, 140, 160);  
    public static final Color TEXT_MUT     = new Color(72,  84,  96);   


    public static final Color[] RAMP_BLUE   = {new Color(12,28,55),  ACCENT_BLUE,  new Color(147,197,253), new Color(96,160,250)};
    public static final Color[] RAMP_GREEN  = {new Color(10,30,22),  ACCENT_GREEN, new Color(134,239,172), new Color(74,200,120)};
    public static final Color[] RAMP_AMBER  = {new Color(35,25,5),   ACCENT_AMBER, new Color(253,224,71),  new Color(210,153,34)};
    public static final Color[] RAMP_PURPLE = {new Color(25,15,55),  new Color(139,92,246), new Color(196,181,253), new Color(139,92,246)};


    public static final Font FONT_TITLE  = new Font("Segoe UI", Font.BOLD,  20);
    public static final Font FONT_HEAD   = new Font("Segoe UI", Font.BOLD,  15);
    public static final Font FONT_BODY   = new Font("Segoe UI", Font.PLAIN, 13);
    public static final Font FONT_SMALL  = new Font("Segoe UI", Font.PLAIN, 11);
    public static final Font FONT_MONO   = new Font("Consolas",  Font.PLAIN, 13);


    public static JPanel panel(Color bg) {
        JPanel p = new JPanel();
        p.setBackground(bg);
        return p;
    }

    public static JLabel label(String text, Font font, Color fg) {
        JLabel l = new JLabel(text);
        l.setFont(font);
        l.setForeground(fg);
        return l;
    }

    public static JButton pillButton(String text, Color bg, Color fg) {
        JButton btn = new JButton(text) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                Color fill = Boolean.TRUE.equals(getClientProperty("hov"))
                        ? bg.brighter() : bg;
                g2.setColor(fill);
                g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 10, 10));
                g2.dispose();
                super.paintComponent(g);
            }
        };
        btn.setForeground(fg);
        btn.setFont(FONT_BODY);
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setBorder(BorderFactory.createEmptyBorder(9, 20, 9, 20));
        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override public void mouseEntered(java.awt.event.MouseEvent e) { btn.putClientProperty("hov", true);  btn.repaint(); }
            @Override public void mouseExited (java.awt.event.MouseEvent e) { btn.putClientProperty("hov", false); btn.repaint(); }
        });
        return btn;
    }

    public static JButton ghostButton(String text) {
        JButton btn = new JButton(text) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                boolean hov = Boolean.TRUE.equals(getClientProperty("hov"));
                g2.setColor(hov ? BG_ELEVATED : new Color(0,0,0,0));
                g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 10, 10));
                g2.setColor(BORDER);
                g2.setStroke(new BasicStroke(1f));
                g2.draw(new RoundRectangle2D.Float(0.5f, 0.5f, getWidth()-1, getHeight()-1, 10, 10));
                g2.dispose();
                super.paintComponent(g);
            }
        };
        btn.setForeground(TEXT_PRI);
        btn.setFont(FONT_BODY);
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setBorder(BorderFactory.createEmptyBorder(8, 18, 8, 18));
        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override public void mouseEntered(java.awt.event.MouseEvent e) { btn.putClientProperty("hov", true);  btn.repaint(); }
            @Override public void mouseExited (java.awt.event.MouseEvent e) { btn.putClientProperty("hov", false); btn.repaint(); }
        });
        return btn;
    }


    public static JTextField textField(String placeholder) {
        JTextField tf = new JTextField(14);
        tf.setBackground(BG_ELEVATED);
        tf.setForeground(TEXT_PRI);
        tf.setCaretColor(TEXT_PRI);
        tf.setFont(FONT_BODY);
        tf.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER, 1, true),
            BorderFactory.createEmptyBorder(7, 12, 7, 12)
        ));
        return tf;
    }

    public static JPasswordField passwordField() {
        JPasswordField pf = new JPasswordField(14);
        pf.setBackground(BG_ELEVATED);
        pf.setForeground(TEXT_PRI);
        pf.setCaretColor(TEXT_PRI);
        pf.setFont(FONT_BODY);
        pf.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER, 1, true),
            BorderFactory.createEmptyBorder(7, 12, 7, 12)
        ));
        return pf;
    }

    public static void styleTable(JTable table) {
        table.setBackground(BG_SURFACE);
        table.setForeground(TEXT_PRI);
        table.setGridColor(BORDER);
        table.setRowHeight(30);
        table.setFont(FONT_BODY);
        table.setSelectionBackground(new Color(56,139,253,60));
        table.setSelectionForeground(TEXT_PRI);
        table.setShowHorizontalLines(true);
        table.setShowVerticalLines(false);
        table.getTableHeader().setBackground(BG_ELEVATED);
        table.getTableHeader().setForeground(TEXT_SEC);
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        table.getTableHeader().setBorder(BorderFactory.createMatteBorder(0,0,1,0,BORDER));
    }

    public static JScrollPane scrollPane(JTable table) {
        JScrollPane sp = new JScrollPane(table);
        sp.setBackground(BG_SURFACE);
        sp.getViewport().setBackground(BG_SURFACE);
        sp.setBorder(BorderFactory.createLineBorder(BORDER, 1));
        return sp;
    }

    public static JComboBox<String> comboBox(String[] items) {
        JComboBox<String> cb = new JComboBox<>(items);
        cb.setBackground(BG_ELEVATED);
        cb.setForeground(TEXT_PRI);
        cb.setFont(FONT_BODY);
        return cb;
    }

    public static JPanel card(Color bg) {
        JPanel p = new JPanel() {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(bg);
                g2.fill(new RoundRectangle2D.Float(0,0,getWidth(),getHeight(),14,14));
                g2.setColor(BORDER);
                g2.setStroke(new BasicStroke(1f));
                g2.draw(new RoundRectangle2D.Float(0.5f,0.5f,getWidth()-1,getHeight()-1,14,14));
                g2.dispose();
                super.paintComponent(g);
            }
        };
        p.setOpaque(false);
        return p;
    }


    public static JPanel header(String title, String subtitle) {
        JPanel h = new JPanel();
        h.setBackground(BG_DARK);
        h.setLayout(new BoxLayout(h, BoxLayout.Y_AXIS));
        h.setBorder(BorderFactory.createEmptyBorder(24, 28, 18, 28));

        JLabel t = label(title,    FONT_TITLE, TEXT_PRI);
        JLabel s = label(subtitle, FONT_BODY,  TEXT_SEC);
        t.setAlignmentX(Component.LEFT_ALIGNMENT);
        s.setAlignmentX(Component.LEFT_ALIGNMENT);
        h.add(t);
        h.add(Box.createRigidArea(new Dimension(0, 4)));
        h.add(s);


        JPanel sep = new JPanel();
        sep.setBackground(BORDER);
        sep.setPreferredSize(new Dimension(0, 1));
        sep.setMaximumSize(new Dimension(Integer.MAX_VALUE, 1));
        sep.setAlignmentX(Component.LEFT_ALIGNMENT);
        h.add(Box.createRigidArea(new Dimension(0, 14)));
        h.add(sep);
        return h;
    }


    public static void applyLAF() {
        try {
            UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
        } catch (Exception ignored) {}
        UIManager.put("OptionPane.background",        BG_SURFACE);
        UIManager.put("Panel.background",             BG_SURFACE);
        UIManager.put("OptionPane.messageForeground", TEXT_PRI);
        UIManager.put("Button.background",            BG_ELEVATED);
        UIManager.put("Button.foreground",            TEXT_PRI);
        UIManager.put("TextField.background",         BG_ELEVATED);
        UIManager.put("TextField.foreground",         TEXT_PRI);
        UIManager.put("TextField.caretForeground",    TEXT_PRI);
        UIManager.put("PasswordField.background",     BG_ELEVATED);
        UIManager.put("PasswordField.foreground",     TEXT_PRI);
        UIManager.put("ComboBox.background",          BG_ELEVATED);
        UIManager.put("ComboBox.foreground",          TEXT_PRI);
        UIManager.put("Label.foreground",             TEXT_PRI);
    }
}
