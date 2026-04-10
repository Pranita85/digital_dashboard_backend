package com.tatamotors.classbooking.dto;

public class BookingReport {

    private long totalBookings;
    private double totalRevenue;

    public BookingReport(long totalBookings, double totalRevenue) {
        this.totalBookings = totalBookings;
        this.totalRevenue = totalRevenue;
    }

    public long getTotalBookings() {
        return totalBookings;
    }

    public double getTotalRevenue() {
        return totalRevenue;
    }
}
