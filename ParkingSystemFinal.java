package com.mycompany.finalinshallah;

import javax.swing.SwingUtilities;

public class ParkingSystemFinal {
    public static void main(String[] args) {
        Theme.applyLAF();
        SwingUtilities.invokeLater(() -> new RoleSelector().setVisible(true));
    }
}
