////////////package com.tatamotors.classbooking.repository;
////////////
////////////import com.tatamotors.classbooking.entity.Booking;
////////////import com.tatamotors.classbooking.entity.BookingStatus;
////////////import org.springframework.data.jpa.repository.JpaRepository;
////////////import org.springframework.data.jpa.repository.Query;
////////////import org.springframework.data.repository.query.Param;
////////////
////////////import java.time.LocalDate;
////////////import java.time.LocalTime;
////////////import java.util.List;
////////////
////////////public interface BookingRepository extends JpaRepository<Booking, Long> {
////////////
////////////    // --- 1. Conflict Check ---
////////////    List<Booking> findByClassroomIdAndBookingDateAndStartTimeLessThanAndEndTimeGreaterThan(
////////////            Long classroomId,
////////////            LocalDate bookingDate,
////////////            LocalTime endTime,
////////////            LocalTime startTime
////////////    );
////////////
////////////    List<Booking> findByClassroomIdAndBookingDate(Long classroomId, LocalDate date);
////////////
////////////    // --- 2. Dashboard Count ---
////////////    long countByStatus(BookingStatus status);
////////////
////////////    // --- 3. Dashboard Revenue ---
////////////    @Query("SELECT COALESCE(SUM(b.totalPrice),0) FROM Booking b WHERE b.status = :status")
////////////    double sumRevenueByStatus(@Param("status") BookingStatus status);
////////////
////////////    // --- 4. Daily Analytics ---
////////////    @Query("SELECT DATE(b.bookingDate) as date, SUM(b.totalPrice) as revenue, COUNT(b.id) as count " +
////////////           "FROM Booking b " +
////////////           "WHERE b.bookingDate >= :startDate " +
////////////           "GROUP BY DATE(b.bookingDate)")
////////////    List<Object[]> findDailyRevenue(@Param("startDate") LocalDate startDate);
////////////
////////////    // --- 5. Get My Bookings ---
////////////    List<Booking> findByUserId(Long userId);
////////////
////////////    // --- 6. NEW: Sum Booked Hours (Fixed Version) ---
////////////    @Query("SELECT COALESCE(SUM(b.totalHours), 0) FROM Booking b WHERE b.status = :status")
////////////    Long sumBookedHoursByStatus(@Param("status") BookingStatus status);
////////////}
//////////
//////////
//////////
////////////package com.tatamotors.classbooking.repository;
////////////
////////////import com.tatamotors.classbooking.entity.Booking;
////////////import com.tatamotors.classbooking.entity.BookingStatus;
////////////import org.springframework.data.jpa.repository.JpaRepository;
////////////import org.springframework.data.jpa.repository.Query;
////////////import org.springframework.data.repository.query.Param;
////////////
////////////import java.time.LocalDate;
////////////import java.time.LocalTime;
////////////import java.util.List;
////////////
////////////public interface BookingRepository extends JpaRepository<Booking, Long> {
////////////
////////////    // Conflict check
////////////    List<Booking> findByClassroomIdAndBookingDateAndStartTimeLessThanAndEndTimeGreaterThan(
////////////            Long classroomId, LocalDate bookingDate, LocalTime endTime, LocalTime startTime);
////////////
////////////    List<Booking> findByClassroomIdAndBookingDate(Long classroomId, LocalDate date);
////////////
////////////    // Admin: total bookings by status
////////////    long countByStatus(BookingStatus status);
////////////
////////////    // Admin: total revenue
////////////    @Query("SELECT COALESCE(SUM(b.totalPrice),0) FROM Booking b WHERE b.status = :status")
////////////    double sumRevenueByStatus(@Param("status") BookingStatus status);
////////////
////////////    // Admin: daily analytics
////////////    @Query("SELECT DATE(b.bookingDate) as date, SUM(b.totalPrice) as revenue, COUNT(b.id) as count " +
////////////           "FROM Booking b WHERE b.bookingDate >= :startDate GROUP BY DATE(b.bookingDate)")
////////////    List<Object[]> findDailyRevenue(@Param("startDate") LocalDate startDate);
////////////
////////////    // My bookings
////////////    List<Booking> findByUserId(Long userId);
////////////
////////////    // Admin: total booked hours
////////////    @Query("SELECT COALESCE(SUM(b.totalHours), 0) FROM Booking b WHERE b.status = :status")
////////////    Long sumBookedHoursByStatus(@Param("status") BookingStatus status);
////////////
////////////    // ✅ NEW: User's booking count by status
////////////    @Query("SELECT COUNT(b) FROM Booking b WHERE b.user.id = :userId AND b.status = :status")
////////////    long countByUserIdAndStatus(@Param("userId") Long userId, @Param("status") BookingStatus status);
////////////
////////////    // ✅ NEW: User's total booked hours
////////////    @Query("SELECT COALESCE(SUM(b.totalHours), 0) FROM Booking b WHERE b.user.id = :userId")
////////////    Long sumHoursByUserId(@Param("userId") Long userId);
////////////}
//////////
//////////
////////////package com.tatamotors.classbooking.repository;
////////////
////////////import com.tatamotors.classbooking.entity.Booking;
////////////import com.tatamotors.classbooking.entity.BookingStatus;
////////////import org.springframework.data.jpa.repository.JpaRepository;
////////////import org.springframework.data.jpa.repository.Query;
////////////import org.springframework.data.repository.query.Param;
////////////
////////////import java.time.LocalDate;
////////////import java.time.LocalTime;
////////////import java.util.List;
////////////
////////////public interface BookingRepository extends JpaRepository<Booking, Long> {
////////////
////////////    // Conflict check
////////////    List<Booking> findByClassroomIdAndBookingDateAndStartTimeLessThanAndEndTimeGreaterThan(
////////////            Long classroomId, LocalDate bookingDate, LocalTime endTime, LocalTime startTime);
////////////
////////////    List<Booking> findByClassroomIdAndBookingDate(Long classroomId, LocalDate date);
////////////
////////////    // Admin: total bookings by status
////////////    long countByStatus(BookingStatus status);
////////////
////////////    // Admin: total revenue
////////////    @Query("SELECT COALESCE(SUM(b.totalPrice),0) FROM Booking b WHERE b.status = :status")
////////////    double sumRevenueByStatus(@Param("status") BookingStatus status);
////////////
////////////    // Admin: daily analytics
////////////    @Query("SELECT DATE(b.bookingDate) as date, SUM(b.totalPrice) as revenue, COUNT(b.id) as count " +
////////////           "FROM Booking b WHERE b.bookingDate >= :startDate GROUP BY DATE(b.bookingDate)")
////////////    List<Object[]> findDailyRevenue(@Param("startDate") LocalDate startDate);
////////////
////////////    // My bookings (by userId)
////////////    List<Booking> findByUserId(Long userId);
////////////
////////////    // Admin: total booked hours
////////////    @Query("SELECT COALESCE(SUM(b.totalHours), 0) FROM Booking b WHERE b.status = :status")
////////////    Long sumBookedHoursByStatus(@Param("status") BookingStatus status);
////////////
////////////    // User: count active bookings (BOOKED + PENDING)
////////////    @Query("SELECT COUNT(b) FROM Booking b WHERE b.user.id = :userId AND b.status IN ('BOOKED', 'PENDING')")
////////////    long countActiveByUserId(@Param("userId") Long userId);
////////////
////////////    // User: count by specific status
////////////    @Query("SELECT COUNT(b) FROM Booking b WHERE b.user.id = :userId AND b.status = :status")
////////////    long countByUserIdAndStatus(@Param("userId") Long userId, @Param("status") BookingStatus status);
////////////
////////////    // User: total booked hours
////////////    @Query("SELECT COALESCE(SUM(b.totalHours), 0) FROM Booking b WHERE b.user.id = :userId AND b.status IN ('BOOKED', 'PENDING')")
////////////    Long sumHoursByUserId(@Param("userId") Long userId);
////////////}
//////////
//////////
////////////package com.tatamotors.classbooking.repository;
////////////
////////////import com.tatamotors.classbooking.entity.Booking;
////////////import com.tatamotors.classbooking.entity.BookingStatus;
////////////import org.springframework.data.jpa.repository.JpaRepository;
////////////import org.springframework.data.jpa.repository.Query;
////////////import org.springframework.data.repository.query.Param;
////////////
////////////import java.time.LocalDate;
////////////import java.time.LocalTime;
////////////import java.util.List;
////////////
////////////public interface BookingRepository extends JpaRepository<Booking, Long> {
////////////
////////////    // Conflict check
////////////    List<Booking> findByClassroomIdAndBookingDateAndStartTimeLessThanAndEndTimeGreaterThan(
////////////            Long classroomId, LocalDate bookingDate, LocalTime endTime, LocalTime startTime);
////////////
////////////    List<Booking> findByClassroomIdAndBookingDate(Long classroomId, LocalDate date);
////////////
////////////    // Admin: count by status
////////////    long countByStatus(BookingStatus status);
////////////
////////////    // Admin: total revenue
////////////    @Query("SELECT COALESCE(SUM(b.totalPrice),0) FROM Booking b WHERE b.status = :status")
////////////    double sumRevenueByStatus(@Param("status") BookingStatus status);
////////////
////////////    // Admin: daily analytics
////////////    @Query("SELECT DATE(b.bookingDate) as date, SUM(b.totalPrice) as revenue, COUNT(b.id) as count " +
////////////           "FROM Booking b WHERE b.bookingDate >= :startDate GROUP BY DATE(b.bookingDate)")
////////////    List<Object[]> findDailyRevenue(@Param("startDate") LocalDate startDate);
////////////
////////////    // My bookings by userId
////////////    List<Booking> findByUserId(Long userId);
////////////
////////////    // Admin: total booked hours
////////////    @Query("SELECT COALESCE(SUM(b.totalHours), 0) FROM Booking b WHERE b.status = :status")
////////////    Long sumBookedHoursByStatus(@Param("status") BookingStatus status);
////////////
////////////    // User: count active (BOOKED + PENDING) using enum params
////////////    @Query("SELECT COUNT(b) FROM Booking b WHERE b.user.id = :userId AND (b.status = :booked OR b.status = :pending)")
////////////    long countActiveByUserId(
////////////            @Param("userId") Long userId,
////////////            @Param("booked") BookingStatus booked,
////////////            @Param("pending") BookingStatus pending);
////////////
////////////    // User: count by specific status
////////////    @Query("SELECT COUNT(b) FROM Booking b WHERE b.user.id = :userId AND b.status = :status")
////////////    long countByUserIdAndStatus(@Param("userId") Long userId, @Param("status") BookingStatus status);
////////////
////////////    // User: total hours (BOOKED + PENDING)
////////////    @Query("SELECT COALESCE(SUM(b.totalHours), 0) FROM Booking b WHERE b.user.id = :userId AND (b.status = :booked OR b.status = :pending)")
////////////    Long sumHoursByUserId(
////////////            @Param("userId") Long userId,
////////////            @Param("booked") BookingStatus booked,
////////////            @Param("pending") BookingStatus pending);
////////////}
//////////
//////////
//////////
//////////package com.tatamotors.classbooking.repository;
//////////
//////////import com.tatamotors.classbooking.entity.Booking;
//////////import com.tatamotors.classbooking.entity.BookingStatus;
//////////import org.springframework.data.jpa.repository.JpaRepository;
//////////import org.springframework.data.jpa.repository.Query;
//////////import org.springframework.data.repository.query.Param;
//////////
//////////import java.time.LocalDate;
//////////import java.time.LocalTime;
//////////import java.util.List;
//////////
//////////public interface BookingRepository extends JpaRepository<Booking, Long> {
//////////
//////////    // Conflict check
//////////    List<Booking> findByClassroomIdAndBookingDateAndStartTimeLessThanAndEndTimeGreaterThan(
//////////            Long classroomId, LocalDate bookingDate, LocalTime endTime, LocalTime startTime);
//////////
//////////    List<Booking> findByClassroomIdAndBookingDate(Long classroomId, LocalDate date);
//////////
//////////    // Admin: count by status
//////////    long countByStatus(BookingStatus status);
//////////
//////////    // Admin: total revenue
//////////    @Query("SELECT COALESCE(SUM(b.totalPrice),0) FROM Booking b WHERE b.status = :status")
//////////    double sumRevenueByStatus(@Param("status") BookingStatus status);
//////////
//////////    // Admin: daily analytics
//////////    @Query("SELECT DATE(b.bookingDate) as date, SUM(b.totalPrice) as revenue, COUNT(b.id) as count " +
//////////           "FROM Booking b WHERE b.bookingDate >= :startDate GROUP BY DATE(b.bookingDate)")
//////////    List<Object[]> findDailyRevenue(@Param("startDate") LocalDate startDate);
//////////
//////////    // ✅ FIXED: custom @Query because Booking has a `user` relationship, not a plain `userId` field
//////////    @Query("SELECT b FROM Booking b WHERE b.user.id = :userId")
//////////    List<Booking> findByUserId(@Param("userId") Long userId);
//////////
//////////    // Admin: total booked hours
//////////    @Query("SELECT COALESCE(SUM(b.totalHours), 0) FROM Booking b WHERE b.status = :status")
//////////    Long sumBookedHoursByStatus(@Param("status") BookingStatus status);
//////////
//////////    // User: count active (BOOKED + PENDING)
//////////    @Query("SELECT COUNT(b) FROM Booking b WHERE b.user.id = :userId AND (b.status = :booked OR b.status = :pending)")
//////////    long countActiveByUserId(
//////////            @Param("userId") Long userId,
//////////            @Param("booked") BookingStatus booked,
//////////            @Param("pending") BookingStatus pending);
//////////
//////////    // User: count by specific status
//////////    @Query("SELECT COUNT(b) FROM Booking b WHERE b.user.id = :userId AND b.status = :status")
//////////    long countByUserIdAndStatus(@Param("userId") Long userId, @Param("status") BookingStatus status);
//////////
//////////    // User: total hours (BOOKED + PENDING)
//////////    @Query("SELECT COALESCE(SUM(b.totalHours), 0) FROM Booking b WHERE b.user.id = :userId AND (b.status = :booked OR b.status = :pending)")
//////////    Long sumHoursByUserId(
//////////            @Param("userId") Long userId,
//////////            @Param("booked") BookingStatus booked,
//////////            @Param("pending") BookingStatus pending);
//////////}
////////
////////package com.tatamotors.classbooking.repository;
////////
////////import com.tatamotors.classbooking.entity.Booking;
////////import com.tatamotors.classbooking.entity.BookingStatus;
////////import org.springframework.data.jpa.repository.JpaRepository;
////////import org.springframework.data.jpa.repository.Query;
////////import org.springframework.data.repository.query.Param;
////////
////////import java.time.LocalDate;
////////import java.time.LocalTime;
////////import java.util.List;
////////
////////public interface BookingRepository extends JpaRepository<Booking, Long> {
////////
////////    // Conflict check
////////    List<Booking> findByClassroomIdAndBookingDateAndStartTimeLessThanAndEndTimeGreaterThan(
////////            Long classroomId, LocalDate bookingDate, LocalTime endTime, LocalTime startTime);
////////
////////    List<Booking> findByClassroomIdAndBookingDate(Long classroomId, LocalDate date);
////////
////////    // Admin: count by status
////////    long countByStatus(BookingStatus status);
////////
////////    // Admin: total revenue
////////    @Query("SELECT COALESCE(SUM(b.totalPrice),0) FROM Booking b WHERE b.status = :status")
////////    double sumRevenueByStatus(@Param("status") BookingStatus status);
////////
////////    // Admin: daily analytics
////////    @Query("SELECT DATE(b.bookingDate) as date, SUM(b.totalPrice) as revenue, COUNT(b.id) as count " +
////////           "FROM Booking b WHERE b.bookingDate >= :startDate GROUP BY DATE(b.bookingDate)")
////////    List<Object[]> findDailyRevenue(@Param("startDate") LocalDate startDate);
////////
////////    // My bookings by userId
////////    List<Booking> findByUserId(Long userId);
////////
////////    // Admin: total booked hours
////////    @Query("SELECT COALESCE(SUM(b.totalHours), 0) FROM Booking b WHERE b.status = :status")
////////    Long sumBookedHoursByStatus(@Param("status") BookingStatus status);
////////
////////    // User: count active (BOOKED + PENDING) using enum params
////////    @Query("SELECT COUNT(b) FROM Booking b WHERE b.user.id = :userId AND (b.status = :booked OR b.status = :pending)")
////////    long countActiveByUserId(
////////            @Param("userId") Long userId,
////////            @Param("booked") BookingStatus booked,
////////            @Param("pending") BookingStatus pending);
////////
////////    // User: count by specific status
////////    @Query("SELECT COUNT(b) FROM Booking b WHERE b.user.id = :userId AND b.status = :status")
////////    long countByUserIdAndStatus(@Param("userId") Long userId, @Param("status") BookingStatus status);
////////
////////    // User: total hours (BOOKED + PENDING)
////////    @Query("SELECT COALESCE(SUM(b.totalHours), 0) FROM Booking b WHERE b.user.id = :userId AND (b.status = :booked OR b.status = :pending)")
////////    Long sumHoursByUserId(
////////            @Param("userId") Long userId,
////////            @Param("booked") BookingStatus booked,
////////            @Param("pending") BookingStatus pending);
////////}
//////
//////package com.tatamotors.classbooking.repository;
//////
//////import com.tatamotors.classbooking.entity.Booking;
//////import com.tatamotors.classbooking.entity.BookingStatus;
//////import org.springframework.data.jpa.repository.JpaRepository;
//////import org.springframework.data.jpa.repository.Query;
//////import org.springframework.data.repository.query.Param;
//////
//////import java.time.LocalDate;
//////import java.time.LocalTime;
//////import java.util.List;
//////
//////public interface BookingRepository extends JpaRepository<Booking, Long> {
//////
//////    // Conflict check
//////    List<Booking> findByClassroomIdAndBookingDateAndStartTimeLessThanAndEndTimeGreaterThan(
//////            Long classroomId, LocalDate bookingDate, LocalTime endTime, LocalTime startTime);
//////
//////    List<Booking> findByClassroomIdAndBookingDate(Long classroomId, LocalDate date);
//////
//////    // Admin: count by status
//////    long countByStatus(BookingStatus status);
//////
//////    // Admin: total revenue
//////    @Query("SELECT COALESCE(SUM(b.totalPrice),0) FROM Booking b WHERE b.status = :status")
//////    double sumRevenueByStatus(@Param("status") BookingStatus status);
//////
//////    // Admin: daily analytics
//////    @Query("SELECT DATE(b.bookingDate) as date, SUM(b.totalPrice) as revenue, COUNT(b.id) as count " +
//////           "FROM Booking b WHERE b.bookingDate >= :startDate GROUP BY DATE(b.bookingDate)")
//////    List<Object[]> findDailyRevenue(@Param("startDate") LocalDate startDate);
//////
//////    // My bookings by userId
//////    List<Booking> findByUserId(Long userId);
//////
//////    // Admin: total booked hours
//////    @Query("SELECT COALESCE(SUM(b.totalHours), 0) FROM Booking b WHERE b.status = :status")
//////    Long sumBookedHoursByStatus(@Param("status") BookingStatus status);
//////
//////    // User: count active (BOOKED + PENDING) using enum params
//////    @Query("SELECT COUNT(b) FROM Booking b WHERE b.user.id = :userId AND (b.status = :booked OR b.status = :pending)")
//////    long countActiveByUserId(
//////            @Param("userId") Long userId,
//////            @Param("booked") BookingStatus booked,
//////            @Param("pending") BookingStatus pending);
//////
//////    // User: count by specific status
//////    @Query("SELECT COUNT(b) FROM Booking b WHERE b.user.id = :userId AND b.status = :status")
//////    long countByUserIdAndStatus(@Param("userId") Long userId, @Param("status") BookingStatus status);
//////
//////    // User: total hours (BOOKED + PENDING)
//////    @Query("SELECT COALESCE(SUM(b.totalHours), 0) FROM Booking b WHERE b.user.id = :userId AND (b.status = :booked OR b.status = :pending)")
//////    Long sumHoursByUserId(
//////            @Param("userId") Long userId,
//////            @Param("booked") BookingStatus booked,
//////            @Param("pending") BookingStatus pending);
//////}
////
////
////package com.tatamotors.classbooking.repository;
////
////import com.tatamotors.classbooking.entity.Booking;
////import com.tatamotors.classbooking.entity.BookingStatus;
////import org.springframework.data.jpa.repository.JpaRepository;
////import org.springframework.data.jpa.repository.Query;
////import org.springframework.data.repository.query.Param;
////
////import java.time.LocalDate;
////import java.time.LocalTime;
////import java.util.List;
////
////public interface BookingRepository extends JpaRepository<Booking, Long> {
////
////    // Conflict check
////    List<Booking> findByClassroomIdAndBookingDateAndStartTimeLessThanAndEndTimeGreaterThan(
////            Long classroomId, LocalDate bookingDate, LocalTime endTime, LocalTime startTime);
////
////    List<Booking> findByClassroomIdAndBookingDate(Long classroomId, LocalDate date);
////
////    // Admin: count by status
////    long countByStatus(BookingStatus status);
////
////    // Admin: total revenue
////    @Query("SELECT COALESCE(SUM(b.totalPrice),0) FROM Booking b WHERE b.status = :status")
////    double sumRevenueByStatus(@Param("status") BookingStatus status);
////
////    // Admin: daily analytics
////    @Query("SELECT DATE(b.bookingDate) as date, SUM(b.totalPrice) as revenue, COUNT(b.id) as count " +
////           "FROM Booking b WHERE b.bookingDate >= :startDate GROUP BY DATE(b.bookingDate)")
////    List<Object[]> findDailyRevenue(@Param("startDate") LocalDate startDate);
////
//////    // My bookings by userId
//////    List<Booking> findByUserId(Long userId);
////    
////    @Query("SELECT b FROM Booking b WHERE b.user.id = :userId")
////    List<Booking> findByUserId(@Param("userId") Long userId);
////
////    // Admin: total booked hours
////    @Query("SELECT COALESCE(SUM(b.totalHours), 0) FROM Booking b WHERE b.status = :status")
////    Long sumBookedHoursByStatus(@Param("status") BookingStatus status);
////
////    // User: count active (BOOKED + PENDING) using enum params
////    @Query("SELECT COUNT(b) FROM Booking b WHERE b.user.id = :userId AND (b.status = :booked OR b.status = :pending)")
////    long countActiveByUserId(
////            @Param("userId") Long userId,
////            @Param("booked") BookingStatus booked,
////            @Param("pending") BookingStatus pending);
////
////    // User: count by specific status
////    @Query("SELECT COUNT(b) FROM Booking b WHERE b.user.id = :userId AND b.status = :status")
////    long countByUserIdAndStatus(@Param("userId") Long userId, @Param("status") BookingStatus status);
////
////    // User: total hours (BOOKED + PENDING)
////    @Query("SELECT COALESCE(SUM(b.totalHours), 0) FROM Booking b WHERE b.user.id = :userId AND (b.status = :booked OR b.status = :pending)")
////    Long sumHoursByUserId(
////            @Param("userId") Long userId,
////            @Param("booked") BookingStatus booked,
////            @Param("pending") BookingStatus pending);
////}
//
//package com.tatamotors.classbooking.repository;
//
//import com.tatamotors.classbooking.entity.Booking;
//import com.tatamotors.classbooking.entity.BookingStatus;
//import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.data.jpa.repository.Query;
//import org.springframework.data.repository.query.Param;
//
//import java.time.LocalDate;
//import java.time.LocalTime;
//import java.util.List;
//
//public interface BookingRepository extends JpaRepository<Booking, Long> {
//
//    List<Booking> findByClassroomIdAndBookingDateAndStartTimeLessThanAndEndTimeGreaterThan(
//            Long classroomId, LocalDate bookingDate, LocalTime endTime, LocalTime startTime);
//
//    List<Booking> findByClassroomIdAndBookingDate(Long classroomId, LocalDate date);
//
//    long countByStatus(BookingStatus status);
//
//    @Query("SELECT COALESCE(SUM(b.totalPrice),0) FROM Booking b WHERE b.status = :status")
//    double sumRevenueByStatus(@Param("status") BookingStatus status);
//
//    @Query("SELECT DATE(b.bookingDate) as date, SUM(b.totalPrice) as revenue, COUNT(b.id) as count " +
//           "FROM Booking b WHERE b.bookingDate >= :startDate GROUP BY DATE(b.bookingDate)")
//    List<Object[]> findDailyRevenue(@Param("startDate") LocalDate startDate);
//
//    // ✅ FIXED: b.user.id instead of derived findByUserId
// // ✅ Fixed with explicit JPQL query
//    @Query("SELECT b FROM Booking b WHERE b.user.id = :userId")
//    List<Booking> findByUserId(@Param("userId") Long userId);
//
//    @Query("SELECT COALESCE(SUM(b.totalHours), 0) FROM Booking b WHERE b.status = :status")
//    Long sumBookedHoursByStatus(@Param("status") BookingStatus status);
//
//    @Query("SELECT COUNT(b) FROM Booking b WHERE b.user.id = :userId AND (b.status = :booked OR b.status = :pending)")
//    long countActiveByUserId(
//            @Param("userId") Long userId,
//            @Param("booked") BookingStatus booked,
//            @Param("pending") BookingStatus pending);
//
//    @Query("SELECT COUNT(b) FROM Booking b WHERE b.user.id = :userId AND b.status = :status")
//    long countByUserIdAndStatus(@Param("userId") Long userId, @Param("status") BookingStatus status);
//
//    @Query("SELECT COALESCE(SUM(b.totalHours), 0) FROM Booking b WHERE b.user.id = :userId AND (b.status = :booked OR b.status = :pending)")
//    Long sumHoursByUserId(
//            @Param("userId") Long userId,
//            @Param("booked") BookingStatus booked,
//            @Param("pending") BookingStatus pending);
//}


package com.tatamotors.classbooking.repository;

import com.tatamotors.classbooking.entity.Booking;
import com.tatamotors.classbooking.entity.BookingStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    List<Booking> findByClassroomIdAndBookingDateAndStartTimeLessThanAndEndTimeGreaterThan(
            Long classroomId, LocalDate bookingDate, LocalTime endTime, LocalTime startTime);

    List<Booking> findByClassroomIdAndBookingDate(Long classroomId, LocalDate date);

    long countByStatus(BookingStatus status);

    @Query("SELECT COALESCE(SUM(b.totalPrice),0) FROM Booking b WHERE b.status = :status")
    double sumRevenueByStatus(@Param("status") BookingStatus status);

    @Query("SELECT DATE(b.bookingDate) as date, SUM(b.totalPrice) as revenue, COUNT(b.id) as count " +
           "FROM Booking b WHERE b.bookingDate >= :startDate GROUP BY DATE(b.bookingDate)")
    List<Object[]> findDailyRevenue(@Param("startDate") LocalDate startDate);

    @Query("SELECT b FROM Booking b WHERE b.user.id = :userId")
    List<Booking> findByUserId(@Param("userId") Long userId);

    @Query("SELECT COALESCE(SUM(b.totalHours), 0) FROM Booking b WHERE b.status = :status")
    Long sumBookedHoursByStatus(@Param("status") BookingStatus status);

    @Query("SELECT COUNT(b) FROM Booking b WHERE b.user.id = :userId AND (b.status = :booked OR b.status = :pending)")
    long countActiveByUserId(
            @Param("userId") Long userId,
            @Param("booked") BookingStatus booked,
            @Param("pending") BookingStatus pending);

    @Query("SELECT COUNT(b) FROM Booking b WHERE b.user.id = :userId AND b.status = :status")
    long countByUserIdAndStatus(@Param("userId") Long userId, @Param("status") BookingStatus status);

    @Query("SELECT COALESCE(SUM(b.totalHours), 0) FROM Booking b WHERE b.user.id = :userId AND (b.status = :booked OR b.status = :pending)")
    Long sumHoursByUserId(
            @Param("userId") Long userId,
            @Param("booked") BookingStatus booked,
            @Param("pending") BookingStatus pending);

    // ✅ Delete all bookings for a classroom (used before deleting classroom)
    @Modifying
    @Query("DELETE FROM Booking b WHERE b.classroom.id = :classroomId")
    void deleteByClassroomId(@Param("classroomId") Long classroomId);
}