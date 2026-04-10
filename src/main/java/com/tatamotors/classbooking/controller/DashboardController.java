////////////package com.tatamotors.classbooking.controller;
////////////
////////////import com.tatamotors.classbooking.dto.BookingReport;
////////////import com.tatamotors.classbooking.dto.DailyRevenueDTO;
////////////import com.tatamotors.classbooking.repository.BookingRepository;
////////////import com.tatamotors.classbooking.repository.ClassroomRepository;
////////////import com.tatamotors.classbooking.service.DashboardService;
////////////
////////////import java.time.LocalDate;
////////////import java.util.ArrayList;
////////////import java.util.HashMap;
////////////import java.util.List;
////////////import java.util.Map;
////////////
////////////import org.springframework.web.bind.annotation.CrossOrigin;
////////////import org.springframework.web.bind.annotation.GetMapping;
////////////import org.springframework.web.bind.annotation.RequestMapping;
////////////import org.springframework.web.bind.annotation.RestController;
////////////
////////////@RestController
////////////@RequestMapping("/api/dashboard")
////////////@CrossOrigin
////////////public class DashboardController {
////////////
////////////    // Inject Services and Repositories
////////////    private final DashboardService dashboardService;
////////////    private final BookingRepository bookingRepository;
////////////    private final ClassroomRepository classroomRepository;
////////////
////////////    public DashboardController(DashboardService dashboardService, 
////////////                            BookingRepository bookingRepository,
////////////                            ClassroomRepository classroomRepository) {
////////////        this.dashboardService = dashboardService;
////////////        this.bookingRepository = bookingRepository;
////////////        this.classroomRepository = classroomRepository;
////////////    }
////////////
////////////    // 1. Standard Report
////////////    @GetMapping("/report")
////////////    public BookingReport getDashboardReport() {
////////////        return dashboardService.getBookingReport();
////////////    }
////////////
////////////    // 2. Daily Analytics (Already had this, just moved here)
////////////    @GetMapping("/analytics")
////////////    public List<DailyRevenueDTO> getAnalytics() {
////////////        LocalDate startDate = LocalDate.now().minusDays(7);
////////////        List<Object[]> rawData = bookingRepository.findDailyRevenue(startDate);
////////////        
////////////        List<DailyRevenueDTO> result = new ArrayList<>();
////////////        for (Object[] row : rawData) {
////////////            String dateStr = row[0].toString(); 
////////////            double rev = row[1] != null ? ((Number)row[1]).doubleValue() : 0.0;
////////////            long count = row[2] != null ? ((Number)row[2]).longValue() : 0L;
////////////            result.add(new DailyRevenueDTO(dateStr, rev, count));
////////////        }
////////////        return result;
////////////    }
////////////
////////////    // 3. New Utilization Endpoint
////////////    @GetMapping("/utilization")
////////////    public Map<String, Object> getUtilization() {
////////////        // A. Get Total Booked Hours
////////////        double bookedHours = dashboardService.getBookedHours();
////////////        
////////////        // B. Calculate Total Available Hours (Example)
////////////        // Logic: (6 hours/day * 30 days * Number of Classrooms)
////////////        long totalRooms = classroomRepository.count();
////////////        double totalAvailableHours = 6.0 * 30.0 * totalRooms; 
////////////        
////////////        // C. Calculate Percentage
////////////        double utilizationPercent = 0.0;
////////////        if (totalAvailableHours > 0) {
////////////            utilizationPercent = (bookedHours / totalAvailableHours) * 100;
////////////        }
////////////        
////////////        Map<String, Object> data = new HashMap<>();
////////////        data.put("bookedHours", bookedHours);
////////////        data.put("totalAvailableHours", totalAvailableHours);
////////////        data.put("utilization", String.format("%.2f", utilizationPercent) + "%");
////////////        
////////////        return data;
////////////    }
////////////}
//////////
//////////
//////////
////////////package com.tatamotors.classbooking.controller;
////////////
////////////import com.tatamotors.classbooking.dto.BookingReport;
////////////import com.tatamotors.classbooking.dto.DailyRevenueDTO;
////////////import com.tatamotors.classbooking.entity.BookingStatus;
////////////import com.tatamotors.classbooking.repository.BookingRepository;
////////////import com.tatamotors.classbooking.repository.ClassroomRepository;
////////////import com.tatamotors.classbooking.service.DashboardService;
////////////
////////////import java.time.LocalDate;
////////////import java.util.ArrayList;
////////////import java.util.HashMap;
////////////import java.util.List;
////////////import java.util.Map;
////////////
////////////import org.springframework.web.bind.annotation.*;
////////////
////////////@RestController
////////////@RequestMapping("/api/dashboard")
////////////@CrossOrigin
////////////public class DashboardController {
////////////
////////////    private final DashboardService dashboardService;
////////////    private final BookingRepository bookingRepository;
////////////    private final ClassroomRepository classroomRepository;
////////////
////////////    public DashboardController(DashboardService dashboardService,
////////////                               BookingRepository bookingRepository,
////////////                               ClassroomRepository classroomRepository) {
////////////        this.dashboardService = dashboardService;
////////////        this.bookingRepository = bookingRepository;
////////////        this.classroomRepository = classroomRepository;
////////////    }
////////////
////////////    // Admin: overall report
////////////    @GetMapping("/report")
////////////    public BookingReport getDashboardReport() {
////////////        return dashboardService.getBookingReport();
////////////    }
////////////
////////////    // Admin: daily analytics (last 7 days)
////////////    @GetMapping("/analytics")
////////////    public List<DailyRevenueDTO> getAnalytics() {
////////////        LocalDate startDate = LocalDate.now().minusDays(7);
////////////        List<Object[]> rawData = bookingRepository.findDailyRevenue(startDate);
////////////        List<DailyRevenueDTO> result = new ArrayList<>();
////////////        for (Object[] row : rawData) {
////////////            String dateStr = row[0].toString();
////////////            double rev = row[1] != null ? ((Number) row[1]).doubleValue() : 0.0;
////////////            long count = row[2] != null ? ((Number) row[2]).longValue() : 0L;
////////////            result.add(new DailyRevenueDTO(dateStr, rev, count));
////////////        }
////////////        return result;
////////////    }
////////////
////////////    // Admin: utilization
////////////    @GetMapping("/utilization")
////////////    public Map<String, Object> getUtilization() {
////////////        double bookedHours = dashboardService.getBookedHours();
////////////        long totalRooms = classroomRepository.count();
////////////        double totalAvailableHours = 6.0 * 30.0 * totalRooms;
////////////        double utilizationPercent = totalAvailableHours > 0
////////////                ? (bookedHours / totalAvailableHours) * 100 : 0.0;
////////////
////////////        Map<String, Object> data = new HashMap<>();
////////////        data.put("bookedHours", bookedHours);
////////////        data.put("totalAvailableHours", totalAvailableHours);
////////////        data.put("utilization", String.format("%.2f", utilizationPercent) + "%");
////////////        return data;
////////////    }
////////////
////////////    // ✅ NEW: User-specific stats for staff dashboard
////////////    @GetMapping("/user-stats")
////////////    public Map<String, Object> getUserStats(@RequestParam Long userId) {
////////////        // Count this user's BOOKED bookings
////////////        long myBookings = bookingRepository.countByUserIdAndStatus(userId, BookingStatus.BOOKED);
////////////
////////////        // Count total hours booked by this user
////////////        Long hoursObj = bookingRepository.sumHoursByUserId(userId);
////////////        long myHours = hoursObj != null ? hoursObj : 0L;
////////////
////////////        // Total available classrooms
////////////        long totalLabs = classroomRepository.count();
////////////
////////////        Map<String, Object> stats = new HashMap<>();
////////////        stats.put("myBookings", myBookings);
////////////        stats.put("myHours", myHours);
////////////        stats.put("totalLabs", totalLabs);
////////////        return stats;
////////////    }
////////////}
//////////
//////////
////////////package com.tatamotors.classbooking.controller;
////////////
////////////import com.tatamotors.classbooking.dto.BookingReport;
////////////import com.tatamotors.classbooking.dto.DailyRevenueDTO;
////////////import com.tatamotors.classbooking.repository.BookingRepository;
////////////import com.tatamotors.classbooking.repository.ClassroomRepository;
////////////import com.tatamotors.classbooking.service.DashboardService;
////////////import com.tatamotors.classbooking.entity.BookingStatus;
////////////
////////////import java.time.LocalDate;
////////////import java.util.ArrayList;
////////////import java.util.HashMap;
////////////import java.util.List;
////////////import java.util.Map;
////////////
////////////import org.springframework.web.bind.annotation.*;
////////////
////////////@RestController
////////////@RequestMapping("/api/dashboard")
////////////@CrossOrigin
////////////public class DashboardController {
////////////
////////////    private final DashboardService dashboardService;
////////////    private final BookingRepository bookingRepository;
////////////    private final ClassroomRepository classroomRepository;
////////////
////////////    public DashboardController(DashboardService dashboardService,
////////////                               BookingRepository bookingRepository,
////////////                               ClassroomRepository classroomRepository) {
////////////        this.dashboardService = dashboardService;
////////////        this.bookingRepository = bookingRepository;
////////////        this.classroomRepository = classroomRepository;
////////////    }
////////////
////////////    // Admin: overall report
////////////    @GetMapping("/report")
////////////    public BookingReport getDashboardReport() {
////////////        return dashboardService.getBookingReport();
////////////    }
////////////
////////////    // Admin: daily analytics (last 7 days)
////////////    @GetMapping("/analytics")
////////////    public List<DailyRevenueDTO> getAnalytics() {
////////////        LocalDate startDate = LocalDate.now().minusDays(7);
////////////        List<Object[]> rawData = bookingRepository.findDailyRevenue(startDate);
////////////        List<DailyRevenueDTO> result = new ArrayList<>();
////////////        for (Object[] row : rawData) {
////////////            String dateStr = row[0].toString();
////////////            double rev = row[1] != null ? ((Number) row[1]).doubleValue() : 0.0;
////////////            long count = row[2] != null ? ((Number) row[2]).longValue() : 0L;
////////////            result.add(new DailyRevenueDTO(dateStr, rev, count));
////////////        }
////////////        return result;
////////////    }
////////////
////////////    // Admin: utilization
////////////    @GetMapping("/utilization")
////////////    public Map<String, Object> getUtilization() {
////////////        double bookedHours = dashboardService.getBookedHours();
////////////        long totalRooms = classroomRepository.count();
////////////        double totalAvailableHours = 6.0 * 30.0 * totalRooms;
////////////        double utilizationPercent = totalAvailableHours > 0
////////////                ? (bookedHours / totalAvailableHours) * 100 : 0.0;
////////////
////////////        Map<String, Object> data = new HashMap<>();
////////////        data.put("bookedHours", bookedHours);
////////////        data.put("totalAvailableHours", totalAvailableHours);
////////////        data.put("utilization", String.format("%.2f", utilizationPercent) + "%");
////////////        return data;
////////////    }
////////////
////////////    // User-specific stats (counts BOOKED + PENDING as active)
////////////    @GetMapping("/user-stats")
////////////    public Map<String, Object> getUserStats(@RequestParam Long userId) {
////////////        long myBookings = bookingRepository.countActiveByUserId(userId);
////////////        Long hoursObj = bookingRepository.sumHoursByUserId(userId);
////////////        long myHours = hoursObj != null ? hoursObj : 0L;
////////////        long totalLabs = classroomRepository.count();
////////////
////////////        Map<String, Object> stats = new HashMap<>();
////////////        stats.put("myBookings", myBookings);
////////////        stats.put("myHours", myHours);
////////////        stats.put("totalLabs", totalLabs);
////////////        return stats;
////////////    }
////////////}
//////////
//////////
////////////package com.tatamotors.classbooking.controller;
////////////
////////////import com.tatamotors.classbooking.dto.BookingReport;
////////////import com.tatamotors.classbooking.dto.DailyRevenueDTO;
////////////import com.tatamotors.classbooking.entity.BookingStatus;
////////////import com.tatamotors.classbooking.repository.BookingRepository;
////////////import com.tatamotors.classbooking.repository.ClassroomRepository;
////////////import com.tatamotors.classbooking.service.DashboardService;
////////////
////////////import java.time.LocalDate;
////////////import java.util.ArrayList;
////////////import java.util.HashMap;
////////////import java.util.List;
////////////import java.util.Map;
////////////
////////////import org.springframework.web.bind.annotation.*;
////////////
////////////@RestController
////////////@RequestMapping("/api/dashboard")
////////////@CrossOrigin
////////////public class DashboardController {
////////////
////////////    private final DashboardService dashboardService;
////////////    private final BookingRepository bookingRepository;
////////////    private final ClassroomRepository classroomRepository;
////////////
////////////    public DashboardController(DashboardService dashboardService,
////////////                               BookingRepository bookingRepository,
////////////                               ClassroomRepository classroomRepository) {
////////////        this.dashboardService = dashboardService;
////////////        this.bookingRepository = bookingRepository;
////////////        this.classroomRepository = classroomRepository;
////////////    }
////////////
////////////    @GetMapping("/report")
////////////    public BookingReport getDashboardReport() {
////////////        return dashboardService.getBookingReport();
////////////    }
////////////
////////////    @GetMapping("/analytics")
////////////    public List<DailyRevenueDTO> getAnalytics() {
////////////        LocalDate startDate = LocalDate.now().minusDays(7);
////////////        List<Object[]> rawData = bookingRepository.findDailyRevenue(startDate);
////////////        List<DailyRevenueDTO> result = new ArrayList<>();
////////////        for (Object[] row : rawData) {
////////////            String dateStr = row[0].toString();
////////////            double rev = row[1] != null ? ((Number) row[1]).doubleValue() : 0.0;
////////////            long count = row[2] != null ? ((Number) row[2]).longValue() : 0L;
////////////            result.add(new DailyRevenueDTO(dateStr, rev, count));
////////////        }
////////////        return result;
////////////    }
////////////
////////////    @GetMapping("/utilization")
////////////    public Map<String, Object> getUtilization() {
////////////        double bookedHours = dashboardService.getBookedHours();
////////////        long totalRooms = classroomRepository.count();
////////////        double totalAvailableHours = 6.0 * 30.0 * totalRooms;
////////////        double utilizationPercent = totalAvailableHours > 0
////////////                ? (bookedHours / totalAvailableHours) * 100 : 0.0;
////////////
////////////        Map<String, Object> data = new HashMap<>();
////////////        data.put("bookedHours", bookedHours);
////////////        data.put("totalAvailableHours", totalAvailableHours);
////////////        data.put("utilization", String.format("%.2f", utilizationPercent) + "%");
////////////        return data;
////////////    }
////////////
////////////    // User-specific stats — passes enum values explicitly
////////////    @GetMapping("/user-stats")
////////////    public Map<String, Object> getUserStats(@RequestParam Long userId) {
////////////        long myBookings = bookingRepository.countActiveByUserId(
////////////                userId, BookingStatus.BOOKED, BookingStatus.PENDING);
////////////
////////////        Long hoursObj = bookingRepository.sumHoursByUserId(
////////////                userId, BookingStatus.BOOKED, BookingStatus.PENDING);
////////////        long myHours = hoursObj != null ? hoursObj : 0L;
////////////
////////////        long totalLabs = classroomRepository.count();
////////////
////////////        Map<String, Object> stats = new HashMap<>();
////////////        stats.put("myBookings", myBookings);
////////////        stats.put("myHours", myHours);
////////////        stats.put("totalLabs", totalLabs);
////////////        return stats;
////////////    }
////////////}
//////////
//////////
//////////
//////////package com.tatamotors.classbooking.controller;
//////////
//////////import com.tatamotors.classbooking.dto.BookingReport;
//////////import com.tatamotors.classbooking.dto.DailyRevenueDTO;
//////////import com.tatamotors.classbooking.entity.BookingStatus;
//////////import com.tatamotors.classbooking.repository.BookingRepository;
//////////import com.tatamotors.classbooking.repository.ClassroomRepository;
//////////import com.tatamotors.classbooking.service.DashboardService;
//////////
//////////import java.time.LocalDate;
//////////import java.util.ArrayList;
//////////import java.util.HashMap;
//////////import java.util.List;
//////////import java.util.Map;
//////////
//////////import org.springframework.web.bind.annotation.*;
//////////
//////////@RestController
//////////@RequestMapping("/api/dashboard")
//////////// ✅ FIXED: removed @CrossOrigin — handled globally by SecurityConfig to avoid conflicts
//////////public class DashboardController {
//////////
//////////    private final DashboardService dashboardService;
//////////    private final BookingRepository bookingRepository;
//////////    private final ClassroomRepository classroomRepository;
//////////
//////////    public DashboardController(DashboardService dashboardService,
//////////                               BookingRepository bookingRepository,
//////////                               ClassroomRepository classroomRepository) {
//////////        this.dashboardService = dashboardService;
//////////        this.bookingRepository = bookingRepository;
//////////        this.classroomRepository = classroomRepository;
//////////    }
//////////
//////////    @GetMapping("/report")
//////////    public BookingReport getDashboardReport() {
//////////        return dashboardService.getBookingReport();
//////////    }
//////////
//////////    @GetMapping("/analytics")
//////////    public List<DailyRevenueDTO> getAnalytics() {
//////////        LocalDate startDate = LocalDate.now().minusDays(7);
//////////        List<Object[]> rawData = bookingRepository.findDailyRevenue(startDate);
//////////        List<DailyRevenueDTO> result = new ArrayList<>();
//////////        for (Object[] row : rawData) {
//////////            String dateStr = row[0].toString();
//////////            double rev = row[1] != null ? ((Number) row[1]).doubleValue() : 0.0;
//////////            long count = row[2] != null ? ((Number) row[2]).longValue() : 0L;
//////////            result.add(new DailyRevenueDTO(dateStr, rev, count));
//////////        }
//////////        return result;
//////////    }
//////////
//////////    @GetMapping("/utilization")
//////////    public Map<String, Object> getUtilization() {
//////////        double bookedHours = dashboardService.getBookedHours();
//////////        long totalRooms = classroomRepository.count();
//////////        double totalAvailableHours = 6.0 * 30.0 * totalRooms;
//////////        double utilizationPercent = totalAvailableHours > 0
//////////                ? (bookedHours / totalAvailableHours) * 100 : 0.0;
//////////
//////////        Map<String, Object> data = new HashMap<>();
//////////        data.put("bookedHours", bookedHours);
//////////        data.put("totalAvailableHours", totalAvailableHours);
//////////        data.put("utilization", String.format("%.2f", utilizationPercent) + "%");
//////////        return data;
//////////    }
//////////
//////////    @GetMapping("/user-stats")
//////////    public Map<String, Object> getUserStats(@RequestParam Long userId) {
//////////        long myBookings = bookingRepository.countActiveByUserId(
//////////                userId, BookingStatus.BOOKED, BookingStatus.PENDING);
//////////
//////////        Long hoursObj = bookingRepository.sumHoursByUserId(
//////////                userId, BookingStatus.BOOKED, BookingStatus.PENDING);
//////////        long myHours = hoursObj != null ? hoursObj : 0L;
//////////
//////////        long totalLabs = classroomRepository.count();
//////////
//////////        Map<String, Object> stats = new HashMap<>();
//////////        stats.put("myBookings", myBookings);
//////////        stats.put("myHours", myHours);
//////////        stats.put("totalLabs", totalLabs);
//////////        return stats;
//////////    }
//////////}
////////
////////
////////package com.tatamotors.classbooking.controller;
////////
////////import com.tatamotors.classbooking.dto.BookingReport;
////////import com.tatamotors.classbooking.dto.DailyRevenueDTO;
////////import com.tatamotors.classbooking.entity.BookingStatus;
////////import com.tatamotors.classbooking.repository.BookingRepository;
////////import com.tatamotors.classbooking.repository.ClassroomRepository;
////////import com.tatamotors.classbooking.service.DashboardService;
////////
////////import java.time.LocalDate;
////////import java.util.ArrayList;
////////import java.util.HashMap;
////////import java.util.List;
////////import java.util.Map;
////////
////////import org.springframework.web.bind.annotation.*;
////////
////////@RestController
////////@RequestMapping("/api/dashboard")
////////@CrossOrigin
////////public class DashboardController {
////////
////////    private final DashboardService dashboardService;
////////    private final BookingRepository bookingRepository;
////////    private final ClassroomRepository classroomRepository;
////////
////////    public DashboardController(DashboardService dashboardService,
////////                               BookingRepository bookingRepository,
////////                               ClassroomRepository classroomRepository) {
////////        this.dashboardService = dashboardService;
////////        this.bookingRepository = bookingRepository;
////////        this.classroomRepository = classroomRepository;
////////    }
////////
////////    @GetMapping("/report")
////////    public BookingReport getDashboardReport() {
////////        return dashboardService.getBookingReport();
////////    }
////////
////////    @GetMapping("/analytics")
////////    public List<DailyRevenueDTO> getAnalytics() {
////////        LocalDate startDate = LocalDate.now().minusDays(7);
////////        List<Object[]> rawData = bookingRepository.findDailyRevenue(startDate);
////////        List<DailyRevenueDTO> result = new ArrayList<>();
////////        for (Object[] row : rawData) {
////////            String dateStr = row[0].toString();
////////            double rev = row[1] != null ? ((Number) row[1]).doubleValue() : 0.0;
////////            long count = row[2] != null ? ((Number) row[2]).longValue() : 0L;
////////            result.add(new DailyRevenueDTO(dateStr, rev, count));
////////        }
////////        return result;
////////    }
////////
////////    @GetMapping("/utilization")
////////    public Map<String, Object> getUtilization() {
////////        double bookedHours = dashboardService.getBookedHours();
////////        long totalRooms = classroomRepository.count();
////////        double totalAvailableHours = 6.0 * 30.0 * totalRooms;
////////        double utilizationPercent = totalAvailableHours > 0
////////                ? (bookedHours / totalAvailableHours) * 100 : 0.0;
////////
////////        Map<String, Object> data = new HashMap<>();
////////        data.put("bookedHours", bookedHours);
////////        data.put("totalAvailableHours", totalAvailableHours);
////////        data.put("utilization", String.format("%.2f", utilizationPercent) + "%");
////////        return data;
////////    }
////////
////////    // User-specific stats — passes enum values explicitly
////////    @GetMapping("/user-stats")
////////    public Map<String, Object> getUserStats(@RequestParam Long userId) {
////////        long myBookings = bookingRepository.countActiveByUserId(
////////                userId, BookingStatus.BOOKED, BookingStatus.PENDING);
////////
////////        Long hoursObj = bookingRepository.sumHoursByUserId(
////////                userId, BookingStatus.BOOKED, BookingStatus.PENDING);
////////        long myHours = hoursObj != null ? hoursObj : 0L;
////////
////////        long totalLabs = classroomRepository.count();
////////
////////        Map<String, Object> stats = new HashMap<>();
////////        stats.put("myBookings", myBookings);
////////        stats.put("myHours", myHours);
////////        stats.put("totalLabs", totalLabs);
////////        return stats;
////////    }
////////}
//////
//////
//////package com.tatamotors.classbooking.controller;
//////
//////import com.tatamotors.classbooking.dto.BookingReport;
//////import com.tatamotors.classbooking.dto.DailyRevenueDTO;
//////import com.tatamotors.classbooking.entity.BookingStatus;
//////import com.tatamotors.classbooking.repository.BookingRepository;
//////import com.tatamotors.classbooking.repository.ClassroomRepository;
//////import com.tatamotors.classbooking.service.DashboardService;
//////
//////import java.time.LocalDate;
//////import java.util.ArrayList;
//////import java.util.HashMap;
//////import java.util.List;
//////import java.util.Map;
//////
//////import org.springframework.web.bind.annotation.*;
//////
//////@RestController
//////@RequestMapping("/api/dashboard")
//////@CrossOrigin
//////public class DashboardController {
//////
//////    private final DashboardService dashboardService;
//////    private final BookingRepository bookingRepository;
//////    private final ClassroomRepository classroomRepository;
//////
//////    public DashboardController(DashboardService dashboardService,
//////                               BookingRepository bookingRepository,
//////                               ClassroomRepository classroomRepository) {
//////        this.dashboardService = dashboardService;
//////        this.bookingRepository = bookingRepository;
//////        this.classroomRepository = classroomRepository;
//////    }
//////
//////    @GetMapping("/report")
//////    public BookingReport getDashboardReport() {
//////        return dashboardService.getBookingReport();
//////    }
//////
//////    @GetMapping("/analytics")
//////    public List<DailyRevenueDTO> getAnalytics() {
//////        LocalDate startDate = LocalDate.now().minusDays(7);
//////        List<Object[]> rawData = bookingRepository.findDailyRevenue(startDate);
//////        List<DailyRevenueDTO> result = new ArrayList<>();
//////        for (Object[] row : rawData) {
//////            String dateStr = row[0].toString();
//////            double rev = row[1] != null ? ((Number) row[1]).doubleValue() : 0.0;
//////            long count = row[2] != null ? ((Number) row[2]).longValue() : 0L;
//////            result.add(new DailyRevenueDTO(dateStr, rev, count));
//////        }
//////        return result;
//////    }
//////
//////    @GetMapping("/utilization")
//////    public Map<String, Object> getUtilization() {
//////        double bookedHours = dashboardService.getBookedHours();
//////        long totalRooms = classroomRepository.count();
//////        double totalAvailableHours = 6.0 * 30.0 * totalRooms;
//////        double utilizationPercent = totalAvailableHours > 0
//////                ? (bookedHours / totalAvailableHours) * 100 : 0.0;
//////
//////        Map<String, Object> data = new HashMap<>();
//////        data.put("bookedHours", bookedHours);
//////        data.put("totalAvailableHours", totalAvailableHours);
//////        data.put("utilization", String.format("%.2f", utilizationPercent) + "%");
//////        return data;
//////    }
//////
//////    // User-specific stats — passes enum values explicitly
//////    @GetMapping("/user-stats")
//////    public Map<String, Object> getUserStats(@RequestParam Long userId) {
//////        long myBookings = bookingRepository.countActiveByUserId(
//////                userId, BookingStatus.BOOKED, BookingStatus.PENDING);
//////
//////        Long hoursObj = bookingRepository.sumHoursByUserId(
//////                userId, BookingStatus.BOOKED, BookingStatus.PENDING);
//////        long myHours = hoursObj != null ? hoursObj : 0L;
//////
//////        long totalLabs = classroomRepository.count();
//////
//////        Map<String, Object> stats = new HashMap<>();
//////        stats.put("myBookings", myBookings);
//////        stats.put("myHours", myHours);
//////        stats.put("totalLabs", totalLabs);
//////        return stats;
//////    }
//////}
////
////package com.tatamotors.classbooking.controller;
////
////import com.tatamotors.classbooking.dto.BookingReport;
////import com.tatamotors.classbooking.dto.DailyRevenueDTO;
////import com.tatamotors.classbooking.entity.BookingStatus;
////import com.tatamotors.classbooking.repository.BookingRepository;
////import com.tatamotors.classbooking.repository.ClassroomRepository;
////import com.tatamotors.classbooking.service.DashboardService;
////
////import java.time.LocalDate;
////import java.util.ArrayList;
////import java.util.HashMap;
////import java.util.List;
////import java.util.Map;
////
////import org.springframework.web.bind.annotation.*;
////
////@RestController
////@RequestMapping("/api/dashboard")
////@CrossOrigin
////public class DashboardController {
////
////    private final DashboardService dashboardService;
////    private final BookingRepository bookingRepository;
////    private final ClassroomRepository classroomRepository;
////
////    public DashboardController(DashboardService dashboardService,
////                               BookingRepository bookingRepository,
////                               ClassroomRepository classroomRepository) {
////        this.dashboardService = dashboardService;
////        this.bookingRepository = bookingRepository;
////        this.classroomRepository = classroomRepository;
////    }
////
////    @GetMapping("/report")
////    public BookingReport getDashboardReport() {
////        return dashboardService.getBookingReport();
////    }
////
////    @GetMapping("/analytics")
////    public List<DailyRevenueDTO> getAnalytics() {
////        LocalDate startDate = LocalDate.now().minusDays(7);
////        List<Object[]> rawData = bookingRepository.findDailyRevenue(startDate);
////        List<DailyRevenueDTO> result = new ArrayList<>();
////        for (Object[] row : rawData) {
////            String dateStr = row[0].toString();
////            double rev = row[1] != null ? ((Number) row[1]).doubleValue() : 0.0;
////            long count = row[2] != null ? ((Number) row[2]).longValue() : 0L;
////            result.add(new DailyRevenueDTO(dateStr, rev, count));
////        }
////        return result;
////    }
////
////    @GetMapping("/utilization")
////    public Map<String, Object> getUtilization() {
////        double bookedHours = dashboardService.getBookedHours();
////        long totalRooms = classroomRepository.count();
////        double totalAvailableHours = 6.0 * 30.0 * totalRooms;
////        double utilizationPercent = totalAvailableHours > 0
////                ? (bookedHours / totalAvailableHours) * 100 : 0.0;
////
////        Map<String, Object> data = new HashMap<>();
////        data.put("bookedHours", bookedHours);
////        data.put("totalAvailableHours", totalAvailableHours);
////        data.put("utilization", String.format("%.2f", utilizationPercent) + "%");
////        return data;
////    }
////
////    // User-specific stats — passes enum values explicitly
////    @GetMapping("/user-stats")
////    public Map<String, Object> getUserStats(@RequestParam Long userId) {
////        long myBookings = bookingRepository.countActiveByUserId(
////                userId, BookingStatus.BOOKED, BookingStatus.PENDING);
////
////        Long hoursObj = bookingRepository.sumHoursByUserId(
////                userId, BookingStatus.BOOKED, BookingStatus.PENDING);
////        long myHours = hoursObj != null ? hoursObj : 0L;
////
////        long totalLabs = classroomRepository.count();
////
////        Map<String, Object> stats = new HashMap<>();
////        stats.put("myBookings", myBookings);
////        stats.put("myHours", myHours);
////        stats.put("totalLabs", totalLabs);
////        return stats;
////    }
////}
//
//
//package com.tatamotors.classbooking.controller;
//
//import com.tatamotors.classbooking.dto.BookingReport;
//import com.tatamotors.classbooking.dto.DailyRevenueDTO;
//import com.tatamotors.classbooking.entity.BookingStatus;
//import com.tatamotors.classbooking.repository.BookingRepository;
//import com.tatamotors.classbooking.repository.ClassroomRepository;
//import com.tatamotors.classbooking.service.DashboardService;
//
//import java.time.LocalDate;
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//import org.springframework.web.bind.annotation.*;
//
//@RestController
//@RequestMapping("/api/dashboard")
//@CrossOrigin
//public class DashboardController {
//
//    private final DashboardService dashboardService;
//    private final BookingRepository bookingRepository;
//    private final ClassroomRepository classroomRepository;
//
//    public DashboardController(DashboardService dashboardService,
//                               BookingRepository bookingRepository,
//                               ClassroomRepository classroomRepository) {
//        this.dashboardService = dashboardService;
//        this.bookingRepository = bookingRepository;
//        this.classroomRepository = classroomRepository;
//    }
//
//    // Admin: overall report (only BOOKED status)
//    @GetMapping("/report")
//    public BookingReport getDashboardReport() {
//        return dashboardService.getBookingReport();
//    }
//
//    // Admin: daily analytics — last 30 days so older bookings show up
//    @GetMapping("/analytics")
//    public List<DailyRevenueDTO> getAnalytics() {
//        LocalDate startDate = LocalDate.now().minusDays(30);
//        List<Object[]> rawData = bookingRepository.findDailyRevenue(startDate);
//        List<DailyRevenueDTO> result = new ArrayList<>();
//        for (Object[] row : rawData) {
//            String dateStr = row[0].toString();
//            double rev = row[1] != null ? ((Number) row[1]).doubleValue() : 0.0;
//            long count = row[2] != null ? ((Number) row[2]).longValue() : 0L;
//            result.add(new DailyRevenueDTO(dateStr, rev, count));
//        }
//        return result;
//    }
//
//    @GetMapping("/utilization")
//    public Map<String, Object> getUtilization() {
//        double bookedHours = dashboardService.getBookedHours();
//        long totalRooms = classroomRepository.count();
//        double totalAvailableHours = 6.0 * 30.0 * totalRooms;
//        double utilizationPercent = totalAvailableHours > 0
//                ? (bookedHours / totalAvailableHours) * 100 : 0.0;
//
//        Map<String, Object> data = new HashMap<>();
//        data.put("bookedHours", bookedHours);
//        data.put("totalAvailableHours", totalAvailableHours);
//        data.put("utilization", String.format("%.2f", utilizationPercent) + "%");
//        return data;
//    }
//
//    // User-specific stats
//    @GetMapping("/user-stats")
//    public Map<String, Object> getUserStats(@RequestParam Long userId) {
//        long myBookings = bookingRepository.countActiveByUserId(
//                userId, BookingStatus.BOOKED, BookingStatus.PENDING);
//        Long hoursObj = bookingRepository.sumHoursByUserId(
//                userId, BookingStatus.BOOKED, BookingStatus.PENDING);
//        long myHours = hoursObj != null ? hoursObj : 0L;
//        long totalLabs = classroomRepository.count();
//
//        Map<String, Object> stats = new HashMap<>();
//        stats.put("myBookings", myBookings);
//        stats.put("myHours", myHours);
//        stats.put("totalLabs", totalLabs);
//        return stats;
//    }
//}



package com.tatamotors.classbooking.controller;

import com.tatamotors.classbooking.dto.BookingReport;
import com.tatamotors.classbooking.dto.DailyRevenueDTO;
import com.tatamotors.classbooking.entity.BookingStatus;
import com.tatamotors.classbooking.repository.BookingRepository;
import com.tatamotors.classbooking.repository.ClassroomRepository;
import com.tatamotors.classbooking.service.DashboardService;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/dashboard")
@CrossOrigin
public class DashboardController {

    private final DashboardService dashboardService;
    private final BookingRepository bookingRepository;
    private final ClassroomRepository classroomRepository;

    public DashboardController(DashboardService dashboardService,
                               BookingRepository bookingRepository,
                               ClassroomRepository classroomRepository) {
        this.dashboardService = dashboardService;
        this.bookingRepository = bookingRepository;
        this.classroomRepository = classroomRepository;
    }

    @GetMapping("/report")
    public BookingReport getDashboardReport() {
        return dashboardService.getBookingReport();
    }

    // Last 7 days analytics
    @GetMapping("/analytics")
    public List<DailyRevenueDTO> getAnalytics() {
        LocalDate startDate = LocalDate.now().minusDays(7);
        List<Object[]> rawData = bookingRepository.findDailyRevenue(startDate);
        List<DailyRevenueDTO> result = new ArrayList<>();
        for (Object[] row : rawData) {
            String dateStr = row[0].toString();
            double rev = row[1] != null ? ((Number) row[1]).doubleValue() : 0.0;
            long count = row[2] != null ? ((Number) row[2]).longValue() : 0L;
            result.add(new DailyRevenueDTO(dateStr, rev, count));
        }
        return result;
    }

    @GetMapping("/utilization")
    public Map<String, Object> getUtilization() {
        double bookedHours = dashboardService.getBookedHours();
        long totalRooms = classroomRepository.count();
        double totalAvailableHours = 6.0 * 30.0 * totalRooms;
        double utilizationPercent = totalAvailableHours > 0
                ? (bookedHours / totalAvailableHours) * 100 : 0.0;
        Map<String, Object> data = new HashMap<>();
        data.put("bookedHours", bookedHours);
        data.put("totalAvailableHours", totalAvailableHours);
        data.put("utilization", String.format("%.2f", utilizationPercent) + "%");
        return data;
    }

    @GetMapping("/user-stats")
    public Map<String, Object> getUserStats(@RequestParam Long userId) {
        long myBookings = bookingRepository.countActiveByUserId(
                userId, BookingStatus.BOOKED, BookingStatus.PENDING);
        Long hoursObj = bookingRepository.sumHoursByUserId(
                userId, BookingStatus.BOOKED, BookingStatus.PENDING);
        long myHours = hoursObj != null ? hoursObj : 0L;
        long totalLabs = classroomRepository.count();
        Map<String, Object> stats = new HashMap<>();
        stats.put("myBookings", myBookings);
        stats.put("myHours", myHours);
        stats.put("totalLabs", totalLabs);
        return stats;
    }
}