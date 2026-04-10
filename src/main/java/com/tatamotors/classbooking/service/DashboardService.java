package com.tatamotors.classbooking.service;

import com.tatamotors.classbooking.dto.BookingReport;

public interface DashboardService {

    BookingReport getBookingReport();
    
    // Add this new method signature
    double getBookedHours();
}