package com.tatamotors.classbooking.dto;

// Simple class to hold data for one day
public class DailyRevenueDTO {
    private String date;
    private double revenue;
    private long bookingsCount;

    // Constructor
    public DailyRevenueDTO(String date, double revenue, long bookingsCount) {
        this.date = date;
        this.revenue = revenue;
        this.bookingsCount = bookingsCount;
    }

    // Getters
    public String getDate() { return date; }
    public double getRevenue() { return revenue; }
    public long getBookingsCount() { return bookingsCount; }
}