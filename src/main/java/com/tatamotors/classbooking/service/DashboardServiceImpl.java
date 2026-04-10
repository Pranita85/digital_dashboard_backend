package com.tatamotors.classbooking.service;

import com.tatamotors.classbooking.dto.BookingReport;
import com.tatamotors.classbooking.entity.BookingStatus;
import com.tatamotors.classbooking.repository.BookingRepository;
import com.tatamotors.classbooking.repository.ClassroomRepository;
import org.springframework.stereotype.Service;

@Service
public class DashboardServiceImpl implements DashboardService {

    private final BookingRepository bookingRepository;
    private final ClassroomRepository classroomRepository;

    public DashboardServiceImpl(BookingRepository bookingRepository, ClassroomRepository classroomRepository) {
        this.bookingRepository = bookingRepository;
        this.classroomRepository = classroomRepository;
    }

    @Override
    public BookingReport getBookingReport() {
        long totalBookings = bookingRepository.countByStatus(BookingStatus.BOOKED);
        double totalRevenue = bookingRepository.sumRevenueByStatus(BookingStatus.BOOKED);
        return new BookingReport(totalBookings, totalRevenue);
    }

    @Override
    public double getBookedHours() {
        // ✅ FIX: Pass the status to the repository method
        Long hours = bookingRepository.sumBookedHoursByStatus(BookingStatus.BOOKED);
        return hours != null ? hours : 0.0;
    }
}