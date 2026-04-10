package com.tatamotors.classbooking.dto;

import java.time.LocalTime;

public class AvailabilitySlot {

    private LocalTime hour;
    private boolean available;

    public AvailabilitySlot(LocalTime hour, boolean available) {
        this.hour = hour;
        this.available = available;
    }

    public LocalTime getHour() {
        return hour;
    }

    public boolean isAvailable() {
        return available;
    }
}
