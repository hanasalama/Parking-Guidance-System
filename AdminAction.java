package com.mycompany.finalinshallah;

import java.util.ArrayList;

public class AdminAction {

    // Payment records: {ticketId, plate, hours, amount, timestamp}
    public static final ArrayList<String[]> paymentRecords = new ArrayList<>();
    public static final ArrayList<User>     users          = new ArrayList<>();

    static {
        users.add(new User("admin",    "1234", "Admin"));
        users.add(new User("operator", "pass", "Operator"));
    }

    // ── Spots ────────────────────────────────────────────────────

    /**
     * Adds a spot only if the ID doesn't already exist.
     * FIX: uses ParkingSystem.spots as the single source of truth.
     */
    public boolean addSpot(int id) {
        for (Spot s : ParkingSystem.spots) {
            if (s.getSpotNumber() == id) return false; // already exists
        }
        ParkingSystem.spots.add(new Spot(id));
        return true;
    }

    public int totalSpots()    { return ParkingSystem.spots.size(); }
    public int occupiedSpots() { return ParkingSystem.occupiedCount(); }

    // ── Users ─────────────────────────────────────────────────────

    public boolean addUser(String username, String password, String role) {
        for (User u : users) {
            if (u.getUsername().equalsIgnoreCase(username)) return false; // duplicate
        }
        users.add(new User(username, password, role));
        return true;
    }

    public boolean deleteUser(String username) {
        return users.removeIf(u -> u.getUsername().equals(username));
    }

    public boolean updateUser(String username, String newPassword, String newRole) {
        for (User u : users) {
            if (u.getUsername().equals(username)) {
                u.setPassword(newPassword);
                u.setRole(newRole);
                return true;
            }
        }
        return false;
    }

    // ── Revenue ───────────────────────────────────────────────────

    public double getTotalRevenue() {
        double total = 0;
        for (String[] rec : paymentRecords) {
            try { total += Double.parseDouble(rec[3]); } catch (Exception ignored) {}
        }
        return total;
    }
}
