//////package com.tatamotors.classbooking.service;
//////
//////import com.tatamotors.classbooking.dto.AvailabilitySlot;
//////import com.tatamotors.classbooking.dto.BookingReport;
//////import com.tatamotors.classbooking.dto.BookingRequestDto;
//////import com.tatamotors.classbooking.dto.DailyRevenueDTO;
//////import com.tatamotors.classbooking.entity.Booking;
//////
//////import java.time.LocalDate;
//////import java.util.List;
//////
//////public interface BookingService {
//////
//////    // Create booking using DTO
//////    Booking createBooking(BookingRequestDto request);
//////
//////    // Availability
//////    List<AvailabilitySlot> getAvailability(Long classroomId, LocalDate date);
//////
//////    // Dashboard
//////    BookingReport getDashboardReport();
//////
//////    // My bookings
//////    List<Booking> findByUserId(Long userId);
//////
//////    // Cancel booking
//////    Booking cancelBooking(Long id);
//////
//////    // Analytics
//////    List<DailyRevenueDTO> getAnalytics();
//////}
////
////
////package com.tatamotors.classbooking.service;
////
////import com.tatamotors.classbooking.dto.AvailabilitySlot;
////import com.tatamotors.classbooking.dto.BookingReport;
////import com.tatamotors.classbooking.dto.BookingRequestDto;
////import com.tatamotors.classbooking.dto.DailyRevenueDTO;
////import com.tatamotors.classbooking.entity.Booking;
////
////import java.time.LocalDate;
////import java.util.List;
////
////public interface BookingService {
////
////    Booking createBooking(BookingRequestDto request);
////
////    List<AvailabilitySlot> getAvailability(Long classroomId, LocalDate date);
////
////    BookingReport getDashboardReport();
////
////    List<Booking> findByUserId(Long userId);
////
////    Booking cancelBooking(Long id);
////
////    Booking approveBooking(Long id); // ✅ NEW: admin approves pending booking
////
////    List<DailyRevenueDTO> getAnalytics();
////}
//
//
////package com.tatamotors.classbooking.service;
////
////import com.tatamotors.classbooking.dto.AvailabilitySlot;
////import com.tatamotors.classbooking.dto.BookingReport;
////import com.tatamotors.classbooking.dto.BookingRequestDto;
////import com.tatamotors.classbooking.dto.DailyRevenueDTO;
////import com.tatamotors.classbooking.entity.Booking;
////
////import java.time.LocalDate;
////import java.util.List;
////
////public interface BookingService {
////
////    // Create booking using DTO
////    Booking createBooking(BookingRequestDto request);
////
////    // Availability
////    List<AvailabilitySlot> getAvailability(Long classroomId, LocalDate date);
////
////    // Dashboard
////    BookingReport getDashboardReport();
////
////    // My bookings
////    List<Booking> findByUserId(Long userId);
////
////    // Cancel booking
////    Booking cancelBooking(Long id);
////
////    // Analytics
////    List<DailyRevenueDTO> getAnalytics();
////}
//
//
//package com.tatamotors.classbooking.service;
//
//import com.tatamotors.classbooking.dto.AvailabilitySlot;
//import com.tatamotors.classbooking.dto.BookingReport;
//import com.tatamotors.classbooking.dto.BookingRequestDto;
//import com.tatamotors.classbooking.dto.DailyRevenueDTO;
//import com.tatamotors.classbooking.entity.Booking;
//
//import java.time.LocalDate;
//import java.util.List;
//
//public interface BookingService {
//
//    Booking createBooking(BookingRequestDto request);
//
//    List<AvailabilitySlot> getAvailability(Long classroomId, LocalDate date);
//
//    BookingReport getDashboardReport();
//
//    List<Booking> findByUserId(Long userId);
//
//    Booking cancelBooking(Long id);
//
//    Booking approveBooking(Long id); // ✅ NEW: admin approves pending booking
//
//    List<DailyRevenueDTO> getAnalytics();
//    
//    Long extractUserIdFromToken(String token);
//}

package com.tatamotors.classbooking.service;

import com.tatamotors.classbooking.dto.AvailabilitySlot;
import com.tatamotors.classbooking.dto.BookingReport;
import com.tatamotors.classbooking.dto.BookingRequestDto;
import com.tatamotors.classbooking.dto.DailyRevenueDTO;
import com.tatamotors.classbooking.entity.Booking;

import java.time.LocalDate;
import java.util.List;

public interface BookingService {

    Booking createBooking(BookingRequestDto request);

    List<AvailabilitySlot> getAvailability(Long classroomId, LocalDate date);

    BookingReport getDashboardReport();

    List<Booking> findByUserId(Long userId);

    Booking cancelBooking(Long id);

    Booking approveBooking(Long id); // ✅ NEW: admin approves pending booking

    List<DailyRevenueDTO> getAnalytics();
}