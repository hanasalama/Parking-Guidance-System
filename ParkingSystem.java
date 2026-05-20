package com.mycompany.finalinshallah;

import java.util.ArrayList;

public class ParkingSystem {

    public static final ArrayList<Ticket> tickets = new ArrayList<>();
    public static final ArrayList<Spot>   spots   = new ArrayList<>();

    static {
        for (int i = 1; i <= 6; i++) {
            spots.add(new Spot(i));
        }
    }

    /** Parks a car in the first available spot. Returns the ticket, or null if full. */
    public static Ticket parkCar(String carNumber) {
        for (Spot s : spots) {
            if (!s.isOccupied()) {
                Ticket t = new Ticket(tickets.size() + 1, carNumber);
                tickets.add(t);
                s.occupy(t);   // FIX: link ticket to spot
                return t;
            }
        }
        return null; // parking full
    }

    /**
     * Processes exit for a given ticket ID.
     * FIX: finds the exact spot that holds this ticket, not just "any occupied spot".
     * Returns the ticket if found, null otherwise.
     */
    public static Ticket exitCar(int ticketId) {
        for (Ticket t : tickets) {
            if (t.getTicketId() == ticketId) {
                // Find the specific spot holding this ticket
                for (Spot s : spots) {
                    if (s.isOccupied() && s.getCurrentTicket() == t) {
                        s.free();
                        break;
                    }
                }
                return t;
            }
        }
        return null;
    }

    public static int availableCount() {
        int count = 0;
        for (Spot s : spots) if (!s.isOccupied()) count++;
        return count;
    }

    public static int occupiedCount() {
        return spots.size() - availableCount();
    }
}
