package com.mycompany.finalinshallah;

public class Spot {
    private final int spotNumber;
    private boolean occupied;
    private Ticket currentTicket; 

    public Spot(int spotNumber) {
        this.spotNumber = spotNumber;
        this.occupied = false;
        this.currentTicket = null;
    }

    public int getSpotNumber() { return spotNumber; }
    public boolean isOccupied() { return occupied; }
    public Ticket getCurrentTicket() { return currentTicket; }

    public void occupy(Ticket t) {
        this.occupied = true;
        this.currentTicket = t;
    }

    public void free() {
        this.occupied = false;
        this.currentTicket = null;
    }
}
