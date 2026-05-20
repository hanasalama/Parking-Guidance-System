package com.mycompany.finalinshallah;

import java.time.LocalDateTime;

public class Ticket {
    private final int ticketId;
    private final String carNumber;
    private final LocalDateTime entryTime;

    public Ticket(int ticketId, String carNumber) {
        this.ticketId = ticketId;
        this.carNumber = carNumber;
        this.entryTime = LocalDateTime.now();
    }

    public int getTicketId()          { return ticketId; }
    public String getCarNumber()      { return carNumber; }
    public LocalDateTime getEntryTime() { return entryTime; }
}
