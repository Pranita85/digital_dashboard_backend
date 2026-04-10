////////package com.tatamotors.classbooking.service;
////////
////////import com.tatamotors.classbooking.dto.AvailabilitySlot;
////////import com.tatamotors.classbooking.dto.BookingReport;
////////import com.tatamotors.classbooking.dto.BookingRequestDto;
////////import com.tatamotors.classbooking.dto.DailyRevenueDTO;
//////////import com.tatamotors.classbooking.entity.*;
////////
////////import com.tatamotors.classbooking.entity.Booking;
////////import com.tatamotors.classbooking.entity.BookingStatus;
////////import com.tatamotors.classbooking.entity.Classroom;
////////import com.tatamotors.classbooking.entity.User;
////////import com.tatamotors.classbooking.entity.Waitlist;
////////
////////
////////import com.tatamotors.classbooking.repository.*;
////////
////////import org.springframework.stereotype.Service;
////////
////////import java.time.Duration;
////////import java.time.LocalDate;
////////import java.time.LocalDateTime;
////////import java.time.LocalTime;
////////import java.util.ArrayList;
////////import java.util.List;
////////
////////@Service
////////public class BookingServiceImpl implements BookingService {
////////
////////    private final BookingRepository bookingRepository;
////////    private final ClassroomRepository classroomRepository;
////////    private final UserRepository userRepository;
////////    private final WaitlistRepository waitlistRepository;
////////    private final EmailService emailService;
////////
////////    public BookingServiceImpl(
////////            BookingRepository bookingRepository,
////////            ClassroomRepository classroomRepository,
////////            UserRepository userRepository,
////////            WaitlistRepository waitlistRepository,
////////            EmailService emailService
////////    ) {
////////        this.bookingRepository = bookingRepository;
////////        this.classroomRepository = classroomRepository;
////////        this.userRepository = userRepository;
////////        this.waitlistRepository = waitlistRepository;
////////        this.emailService = emailService;
////////    }
////////
////////    // 1️⃣ CREATE BOOKING
////////    @Override
////////    public Booking createBooking(BookingRequestDto request) {
////////
////////        Classroom classroom = classroomRepository.findById(request.getClassroomId())
////////                .orElseThrow(() -> new RuntimeException("Classroom not found"));
////////
////////        User user = userRepository.findById(request.getUserId())
////////                .orElseThrow(() -> new RuntimeException("User not found"));
////////
////////        List<Booking> conflicts =
////////                bookingRepository.findByClassroomIdAndBookingDateAndStartTimeLessThanAndEndTimeGreaterThan(
////////                        request.getClassroomId(),
////////                        request.getDate(),
////////                        request.getEndTime(),
////////                        request.getStartTime()
////////                );
////////
////////        if (!conflicts.isEmpty()) {
////////            Waitlist w = new Waitlist();
////////            w.setUserId(user.getId());
////////            w.setClassroomId(classroom.getId());
////////
////////            w.setDate(request.getDate());
////////            w.setStartTime(request.getStartTime());
////////            w.setEndTime(request.getEndTime());
////////            waitlistRepository.save(w);
////////
////////            throw new RuntimeException("Slot already booked. Added to waitlist.");
////////        }
////////
////////        int hours = (int) Duration.between(
////////                request.getStartTime(),
////////                request.getEndTime()
////////        ).toHours();
////////
////////        double pricePerHour = calculatePrice(classroom, request.getStartTime());
////////        double totalPrice = hours * pricePerHour;
////////
////////        Booking booking = new Booking();
////////        booking.setUser(user);            // ✅ correct (User entity)
////////        booking.setClassroom(classroom);  // ✅ correct (Classroom entity)
////////        booking.setBookingDate(request.getDate());
////////        booking.setStartTime(request.getStartTime());
////////        booking.setEndTime(request.getEndTime());
////////        booking.setTotalHours(hours);
////////        booking.setTotalPrice(totalPrice);
////////        booking.setStatus(BookingStatus.BOOKED);
////////        booking.setCreatedAt(LocalDateTime.now());
////////
////////
////////
////////        Booking saved = bookingRepository.save(booking);
////////
////////        emailService.sendBookingConfirmation(
////////                user.getEmail(),
////////                classroom.getName(),
////////                request.getDate().toString(),
////////                request.getStartTime() + " - " + request.getEndTime(),
////////                totalPrice
////////        );
////////
////////        return saved;
////////    }
////////
////////    // 🔹 Dynamic pricing
////////    private double calculatePrice(Classroom classroom, LocalTime startTime) {
////////        if (classroom.getPeakStart() == null || classroom.getPeakEnd() == null) {
////////            return classroom.getPricePerHour();
////////        }
////////
////////        LocalTime peakStart = LocalTime.parse(classroom.getPeakStart());
////////        LocalTime peakEnd = LocalTime.parse(classroom.getPeakEnd());
////////
////////        if (!startTime.isBefore(peakStart) && !startTime.isAfter(peakEnd)) {
////////            return classroom.getPeakPricePerHour();
////////        }
////////        return classroom.getPricePerHour();
////////    }
////////
////////    // 2️⃣ AVAILABILITY
////////    @Override
////////    public List<AvailabilitySlot> getAvailability(Long classroomId, LocalDate date) {
////////        List<Booking> bookings =
////////                bookingRepository.findByClassroomIdAndBookingDate(classroomId, date);
////////
////////        List<AvailabilitySlot> slots = new ArrayList<>();
////////
////////        LocalTime start = LocalTime.of(8, 0);
////////        LocalTime end = LocalTime.of(20, 0);
////////
////////        while (start.isBefore(end)) {
////////            LocalTime slotEnd = start.plusHours(1);
////////            boolean available = true;
////////
////////            for (Booking booking : bookings) {
////////                if (booking.getStartTime().isBefore(slotEnd)
////////                        && booking.getEndTime().isAfter(start)) {
////////                    available = false;
////////                    break;
////////                }
////////            }
////////
////////            slots.add(new AvailabilitySlot(start, available));
////////            start = slotEnd;
////////        }
////////        return slots;
////////    }
////////
////////    // 3️⃣ DASHBOARD
////////    @Override
////////    public BookingReport getDashboardReport() {
////////        long totalBookings = bookingRepository.countByStatus(BookingStatus.BOOKED);
////////        double revenue = bookingRepository.sumRevenueByStatus(BookingStatus.BOOKED);
////////        return new BookingReport(totalBookings, revenue);
////////    }
////////
////////    // 4️⃣ MY BOOKINGS
////////    @Override
////////    public List<Booking> findByUserId(Long userId) {
////////        return bookingRepository.findByUserId(userId);
////////    }
////////
////////    // 5️⃣ CANCEL BOOKING
////////    @Override
////////    public Booking cancelBooking(Long id) {
////////        Booking booking = bookingRepository.findById(id)
////////                .orElseThrow(() -> new RuntimeException("Booking not found"));
////////
////////        if (booking.getStatus() == BookingStatus.CANCELLED) {
////////            throw new RuntimeException("Already cancelled");
////////        }
////////
////////        booking.setStatus(BookingStatus.CANCELLED);
////////        return bookingRepository.save(booking);
////////    }
////////
////////    // 6️⃣ ANALYTICS
////////    @Override
////////    public List<DailyRevenueDTO> getAnalytics() {
////////        LocalDate startDate = LocalDate.now().minusDays(7);
////////        List<Object[]> raw = bookingRepository.findDailyRevenue(startDate);
////////
////////        List<DailyRevenueDTO> result = new ArrayList<>();
////////        for (Object[] row : raw) {
////////            result.add(new DailyRevenueDTO(
////////                    row[0].toString(),
////////                    ((Number) row[1]).doubleValue(),
////////                    ((Number) row[2]).longValue()
////////            ));
////////        }
////////        return result;
////////    }
////////}
//////
//////
//////
////////package com.tatamotors.classbooking.service;
////////
////////import com.tatamotors.classbooking.dto.AvailabilitySlot;
////////import com.tatamotors.classbooking.dto.BookingReport;
////////import com.tatamotors.classbooking.dto.BookingRequestDto;
////////import com.tatamotors.classbooking.dto.DailyRevenueDTO;
////////import com.tatamotors.classbooking.entity.Booking;
////////import com.tatamotors.classbooking.entity.BookingStatus;
////////import com.tatamotors.classbooking.entity.Classroom;
////////import com.tatamotors.classbooking.entity.User;
////////import com.tatamotors.classbooking.entity.Waitlist;
////////import com.tatamotors.classbooking.repository.*;
////////import org.springframework.stereotype.Service;
////////
////////import java.time.Duration;
////////import java.time.LocalDate;
////////import java.time.LocalDateTime;
////////import java.time.LocalTime;
////////import java.util.ArrayList;
////////import java.util.List;
////////
////////@Service
////////public class BookingServiceImpl implements BookingService {
////////
////////    private final BookingRepository bookingRepository;
////////    private final ClassroomRepository classroomRepository;
////////    private final UserRepository userRepository;
////////    private final WaitlistRepository waitlistRepository;
////////    private final EmailService emailService;
////////
////////    public BookingServiceImpl(
////////            BookingRepository bookingRepository,
////////            ClassroomRepository classroomRepository,
////////            UserRepository userRepository,
////////            WaitlistRepository waitlistRepository,
////////            EmailService emailService) {
////////        this.bookingRepository = bookingRepository;
////////        this.classroomRepository = classroomRepository;
////////        this.userRepository = userRepository;
////////        this.waitlistRepository = waitlistRepository;
////////        this.emailService = emailService;
////////    }
////////
////////    @Override
////////    public Booking createBooking(BookingRequestDto request) {
////////
////////        Classroom classroom = classroomRepository.findById(request.getClassroomId())
////////                .orElseThrow(() -> new RuntimeException("Classroom not found"));
////////
////////        User user = userRepository.findById(request.getUserId())
////////                .orElseThrow(() -> new RuntimeException("User not found"));
////////
////////        // Check for conflicts
////////        List<Booking> conflicts =
////////                bookingRepository.findByClassroomIdAndBookingDateAndStartTimeLessThanAndEndTimeGreaterThan(
////////                        request.getClassroomId(),
////////                        request.getDate(),
////////                        request.getEndTime(),
////////                        request.getStartTime());
////////
////////        if (!conflicts.isEmpty()) {
////////            Waitlist w = new Waitlist();
////////            w.setUserId(user.getId());
////////            w.setClassroomId(classroom.getId());
////////            w.setDate(request.getDate());
////////            w.setStartTime(request.getStartTime());
////////            w.setEndTime(request.getEndTime());
////////            waitlistRepository.save(w);
////////            throw new RuntimeException("Slot already booked. Added to waitlist.");
////////        }
////////
////////        int hours = (int) Duration.between(request.getStartTime(), request.getEndTime()).toHours();
////////        double pricePerHour = calculatePrice(classroom, request.getStartTime());
////////        double totalPrice = hours * pricePerHour;
////////
////////        Booking booking = new Booking();
////////        booking.setUser(user);
////////        booking.setClassroom(classroom);
////////        booking.setBookingDate(request.getDate());
////////        booking.setStartTime(request.getStartTime());
////////        booking.setEndTime(request.getEndTime());
////////        booking.setTotalHours(hours);
////////        booking.setTotalPrice(totalPrice);
////////        booking.setStatus(BookingStatus.BOOKED);
////////        booking.setCreatedAt(LocalDateTime.now());
////////
////////        Booking saved = bookingRepository.save(booking);
////////
////////        // Send confirmation email - wrapped in try/catch so email failure
////////        // does NOT cancel the booking
////////        try {
////////            emailService.sendBookingConfirmation(
////////                    user.getEmail(),
////////                    classroom.getName(),
////////                    request.getDate().toString(),
////////                    request.getStartTime() + " - " + request.getEndTime(),
////////                    totalPrice
////////            );
////////            System.out.println("Confirmation email sent to: " + user.getEmail());
////////        } catch (Exception e) {
////////            // Log but don't fail the booking
////////            System.err.println("Email failed (booking still saved): " + e.getMessage());
////////        }
////////
////////        return saved;
////////    }
////////
////////    private double calculatePrice(Classroom classroom, LocalTime startTime) {
////////        if (classroom.getPeakStart() == null || classroom.getPeakEnd() == null) {
////////            return classroom.getPricePerHour();
////////        }
////////        LocalTime peakStart = LocalTime.parse(classroom.getPeakStart());
////////        LocalTime peakEnd = LocalTime.parse(classroom.getPeakEnd());
////////        if (!startTime.isBefore(peakStart) && !startTime.isAfter(peakEnd)) {
////////            return classroom.getPeakPricePerHour();
////////        }
////////        return classroom.getPricePerHour();
////////    }
////////
////////    @Override
////////    public List<AvailabilitySlot> getAvailability(Long classroomId, LocalDate date) {
////////        List<Booking> bookings = bookingRepository.findByClassroomIdAndBookingDate(classroomId, date);
////////        List<AvailabilitySlot> slots = new ArrayList<>();
////////        LocalTime start = LocalTime.of(8, 0);
////////        LocalTime end = LocalTime.of(20, 0);
////////        while (start.isBefore(end)) {
////////            LocalTime slotEnd = start.plusHours(1);
////////            boolean available = true;
////////            for (Booking booking : bookings) {
////////                if (booking.getStatus() == BookingStatus.BOOKED &&
////////                    booking.getStartTime().isBefore(slotEnd) &&
////////                    booking.getEndTime().isAfter(start)) {
////////                    available = false;
////////                    break;
////////                }
////////            }
////////            slots.add(new AvailabilitySlot(start, available));
////////            start = slotEnd;
////////        }
////////        return slots;
////////    }
////////
////////    @Override
////////    public BookingReport getDashboardReport() {
////////        long totalBookings = bookingRepository.countByStatus(BookingStatus.BOOKED);
////////        double revenue = bookingRepository.sumRevenueByStatus(BookingStatus.BOOKED);
////////        return new BookingReport(totalBookings, revenue);
////////    }
////////
////////    @Override
////////    public List<Booking> findByUserId(Long userId) {
////////        return bookingRepository.findByUserId(userId);
////////    }
////////
////////    @Override
////////    public Booking cancelBooking(Long id) {
////////        Booking booking = bookingRepository.findById(id)
////////                .orElseThrow(() -> new RuntimeException("Booking not found"));
////////
////////        if (booking.getStatus() == BookingStatus.CANCELLED) {
////////            throw new RuntimeException("Already cancelled");
////////        }
////////
////////        booking.setStatus(BookingStatus.CANCELLED);
////////        Booking saved = bookingRepository.save(booking);
////////
////////        // Send cancellation email - also wrapped so it doesn't break cancel
////////        try {
////////            String userEmail = booking.getUser() != null ? booking.getUser().getEmail() : null;
////////            if (userEmail != null) {
////////                emailService.sendCancellationEmail(
////////                        userEmail,
////////                        booking.getClassroom() != null ? booking.getClassroom().getName() : "Classroom",
////////                        booking.getBookingDate().toString(),
////////                        booking.getStartTime() + " - " + booking.getEndTime()
////////                );
////////                System.out.println("Cancellation email sent to: " + userEmail);
////////            }
////////        } catch (Exception e) {
////////            System.err.println("Cancellation email failed: " + e.getMessage());
////////        }
////////
////////        return saved;
////////    }
////////
////////    @Override
////////    public List<DailyRevenueDTO> getAnalytics() {
////////        LocalDate startDate = LocalDate.now().minusDays(7);
////////        List<Object[]> raw = bookingRepository.findDailyRevenue(startDate);
////////        List<DailyRevenueDTO> result = new ArrayList<>();
////////        for (Object[] row : raw) {
////////            result.add(new DailyRevenueDTO(
////////                    row[0].toString(),
////////                    ((Number) row[1]).doubleValue(),
////////                    ((Number) row[2]).longValue()
////////            ));
////////        }
////////        return result;
////////    }
////////}
//////
//////
//////
//////
//////package com.tatamotors.classbooking.service;
//////
//////import com.tatamotors.classbooking.dto.AvailabilitySlot;
//////import com.tatamotors.classbooking.dto.BookingReport;
//////import com.tatamotors.classbooking.dto.BookingRequestDto;
//////import com.tatamotors.classbooking.dto.DailyRevenueDTO;
//////import com.tatamotors.classbooking.entity.*;
//////import com.tatamotors.classbooking.repository.*;
//////import org.springframework.stereotype.Service;
//////
//////import java.time.Duration;
//////import java.time.LocalDate;
//////import java.time.LocalDateTime;
//////import java.time.LocalTime;
//////import java.util.ArrayList;
//////import java.util.List;
//////
//////@Service
//////public class BookingServiceImpl implements BookingService {
//////
//////    private final BookingRepository bookingRepository;
//////    private final ClassroomRepository classroomRepository;
//////    private final UserRepository userRepository;
//////    private final WaitlistRepository waitlistRepository;
//////    private final EmailService emailService;
//////    private final SystemSettingsRepository settingsRepository;
//////
//////    public BookingServiceImpl(
//////            BookingRepository bookingRepository,
//////            ClassroomRepository classroomRepository,
//////            UserRepository userRepository,
//////            WaitlistRepository waitlistRepository,
//////            EmailService emailService,
//////            SystemSettingsRepository settingsRepository) {
//////        this.bookingRepository = bookingRepository;
//////        this.classroomRepository = classroomRepository;
//////        this.userRepository = userRepository;
//////        this.waitlistRepository = waitlistRepository;
//////        this.emailService = emailService;
//////        this.settingsRepository = settingsRepository;
//////    }
//////
//////    @Override
//////    public Booking createBooking(BookingRequestDto request) {
//////
//////        Classroom classroom = classroomRepository.findById(request.getClassroomId())
//////                .orElseThrow(() -> new RuntimeException("Classroom not found"));
//////
//////        User user = userRepository.findById(request.getUserId())
//////                .orElseThrow(() -> new RuntimeException("User not found"));
//////
//////        // Check conflicts
//////        List<Booking> conflicts =
//////                bookingRepository.findByClassroomIdAndBookingDateAndStartTimeLessThanAndEndTimeGreaterThan(
//////                        request.getClassroomId(),
//////                        request.getDate(),
//////                        request.getEndTime(),
//////                        request.getStartTime());
//////
//////        if (!conflicts.isEmpty()) {
//////            Waitlist w = new Waitlist();
//////            w.setUserId(user.getId());
//////            w.setClassroomId(classroom.getId());
//////            w.setDate(request.getDate());
//////            w.setStartTime(request.getStartTime());
//////            w.setEndTime(request.getEndTime());
//////            waitlistRepository.save(w);
//////            throw new RuntimeException("Slot already booked. Added to waitlist.");
//////        }
//////
//////        int hours = (int) Duration.between(request.getStartTime(), request.getEndTime()).toHours();
//////        double pricePerHour = calculatePrice(classroom, request.getStartTime());
//////        double totalPrice = hours * pricePerHour;
//////
//////        // READ SYSTEM SETTINGS to decide status
//////        SystemSettings settings = settingsRepository.findById(1)
//////                .orElse(new SystemSettings());
//////
//////        // If require approval is ON → PENDING, else → BOOKED
//////        BookingStatus status = settings.isRequireApproval()
//////                ? BookingStatus.PENDING
//////                : BookingStatus.BOOKED;
//////
//////        Booking booking = new Booking();
//////        booking.setUser(user);
//////        booking.setClassroom(classroom);
//////        booking.setBookingDate(request.getDate());
//////        booking.setStartTime(request.getStartTime());
//////        booking.setEndTime(request.getEndTime());
//////        booking.setTotalHours(hours);
//////        booking.setTotalPrice(totalPrice);
//////        booking.setStatus(status);
//////        booking.setCreatedAt(LocalDateTime.now());
//////
//////        Booking saved = bookingRepository.save(booking);
//////
//////        // Send email based on status
//////        try {
//////            if (status == BookingStatus.PENDING) {
//////                emailService.sendPendingApprovalEmail(
//////                        user.getEmail(),
//////                        classroom.getName(),
//////                        request.getDate().toString(),
//////                        request.getStartTime() + " - " + request.getEndTime(),
//////                        totalPrice
//////                );
//////            } else {
//////                emailService.sendBookingConfirmation(
//////                        user.getEmail(),
//////                        classroom.getName(),
//////                        request.getDate().toString(),
//////                        request.getStartTime() + " - " + request.getEndTime(),
//////                        totalPrice
//////                );
//////            }
//////        } catch (Exception e) {
//////            System.err.println("Email failed (booking still saved): " + e.getMessage());
//////        }
//////
//////        return saved;
//////    }
//////
//////    private double calculatePrice(Classroom classroom, LocalTime startTime) {
//////        if (classroom.getPeakStart() == null || classroom.getPeakEnd() == null) {
//////            return classroom.getPricePerHour();
//////        }
//////        LocalTime peakStart = LocalTime.parse(classroom.getPeakStart());
//////        LocalTime peakEnd = LocalTime.parse(classroom.getPeakEnd());
//////        if (!startTime.isBefore(peakStart) && !startTime.isAfter(peakEnd)) {
//////            return classroom.getPeakPricePerHour();
//////        }
//////        return classroom.getPricePerHour();
//////    }
//////
//////    @Override
//////    public List<AvailabilitySlot> getAvailability(Long classroomId, LocalDate date) {
//////        List<Booking> bookings = bookingRepository.findByClassroomIdAndBookingDate(classroomId, date);
//////        List<AvailabilitySlot> slots = new ArrayList<>();
//////
//////        // Read operating hours from settings
//////        LocalTime start = LocalTime.of(8, 0);
//////        LocalTime end = LocalTime.of(20, 0);
//////        try {
//////            SystemSettings settings = settingsRepository.findById(1).orElse(new SystemSettings());
//////            start = LocalTime.parse(settings.getOperatingStart());
//////            end = LocalTime.parse(settings.getOperatingEnd());
//////        } catch (Exception ignored) {}
//////
//////        while (start.isBefore(end)) {
//////            LocalTime slotEnd = start.plusHours(1);
//////            boolean available = true;
//////            for (Booking booking : bookings) {
//////                // PENDING bookings also block the slot
//////                if ((booking.getStatus() == BookingStatus.BOOKED ||
//////                     booking.getStatus() == BookingStatus.PENDING) &&
//////                    booking.getStartTime().isBefore(slotEnd) &&
//////                    booking.getEndTime().isAfter(start)) {
//////                    available = false;
//////                    break;
//////                }
//////            }
//////            slots.add(new AvailabilitySlot(start, available));
//////            start = slotEnd;
//////        }
//////        return slots;
//////    }
//////
//////    @Override
//////    public BookingReport getDashboardReport() {
//////        long totalBookings = bookingRepository.countByStatus(BookingStatus.BOOKED);
//////        double revenue = bookingRepository.sumRevenueByStatus(BookingStatus.BOOKED);
//////        return new BookingReport(totalBookings, revenue);
//////    }
//////
//////    @Override
//////    public List<Booking> findByUserId(Long userId) {
//////        return bookingRepository.findByUserId(userId);
//////    }
//////
//////    @Override
//////    public Booking cancelBooking(Long id) {
//////        Booking booking = bookingRepository.findById(id)
//////                .orElseThrow(() -> new RuntimeException("Booking not found"));
//////        if (booking.getStatus() == BookingStatus.CANCELLED) {
//////            throw new RuntimeException("Already cancelled");
//////        }
//////        booking.setStatus(BookingStatus.CANCELLED);
//////        Booking saved = bookingRepository.save(booking);
//////        try {
//////            if (booking.getUser() != null) {
//////                emailService.sendCancellationEmail(
//////                        booking.getUser().getEmail(),
//////                        booking.getClassroom() != null ? booking.getClassroom().getName() : "Classroom",
//////                        booking.getBookingDate().toString(),
//////                        booking.getStartTime() + " - " + booking.getEndTime()
//////                );
//////            }
//////        } catch (Exception e) {
//////            System.err.println("Cancellation email failed: " + e.getMessage());
//////        }
//////        return saved;
//////    }
//////
//////    @Override
//////    public Booking approveBooking(Long id) {
//////        Booking booking = bookingRepository.findById(id)
//////                .orElseThrow(() -> new RuntimeException("Booking not found"));
//////        if (booking.getStatus() != BookingStatus.PENDING) {
//////            throw new RuntimeException("Booking is not pending approval");
//////        }
//////        booking.setStatus(BookingStatus.BOOKED);
//////        Booking saved = bookingRepository.save(booking);
//////        try {
//////            emailService.sendBookingConfirmation(
//////                    booking.getUser().getEmail(),
//////                    booking.getClassroom().getName(),
//////                    booking.getBookingDate().toString(),
//////                    booking.getStartTime() + " - " + booking.getEndTime(),
//////                    booking.getTotalPrice()
//////            );
//////        } catch (Exception e) {
//////            System.err.println("Approval email failed: " + e.getMessage());
//////        }
//////        return saved;
//////    }
//////
//////    @Override
//////    public List<DailyRevenueDTO> getAnalytics() {
//////        LocalDate startDate = LocalDate.now().minusDays(7);
//////        List<Object[]> raw = bookingRepository.findDailyRevenue(startDate);
//////        List<DailyRevenueDTO> result = new ArrayList<>();
//////        for (Object[] row : raw) {
//////            result.add(new DailyRevenueDTO(
//////                    row[0].toString(),
//////                    ((Number) row[1]).doubleValue(),
//////                    ((Number) row[2]).longValue()
//////            ));
//////        }
//////        return result;
//////    }
//////}
////
////
//////package com.tatamotors.classbooking.service;
//////
//////import com.tatamotors.classbooking.dto.AvailabilitySlot;
//////import com.tatamotors.classbooking.dto.BookingReport;
//////import com.tatamotors.classbooking.dto.BookingRequestDto;
//////import com.tatamotors.classbooking.dto.DailyRevenueDTO;
////////import com.tatamotors.classbooking.entity.*;
//////
//////import com.tatamotors.classbooking.entity.Booking;
//////import com.tatamotors.classbooking.entity.BookingStatus;
//////import com.tatamotors.classbooking.entity.Classroom;
//////import com.tatamotors.classbooking.entity.User;
//////import com.tatamotors.classbooking.entity.Waitlist;
//////
//////
//////import com.tatamotors.classbooking.repository.*;
//////
//////import org.springframework.stereotype.Service;
//////
//////import java.time.Duration;
//////import java.time.LocalDate;
//////import java.time.LocalDateTime;
//////import java.time.LocalTime;
//////import java.util.ArrayList;
//////import java.util.List;
//////
//////@Service
//////public class BookingServiceImpl implements BookingService {
//////
//////    private final BookingRepository bookingRepository;
//////    private final ClassroomRepository classroomRepository;
//////    private final UserRepository userRepository;
//////    private final WaitlistRepository waitlistRepository;
//////    private final EmailService emailService;
//////
//////    public BookingServiceImpl(
//////            BookingRepository bookingRepository,
//////            ClassroomRepository classroomRepository,
//////            UserRepository userRepository,
//////            WaitlistRepository waitlistRepository,
//////            EmailService emailService
//////    ) {
//////        this.bookingRepository = bookingRepository;
//////        this.classroomRepository = classroomRepository;
//////        this.userRepository = userRepository;
//////        this.waitlistRepository = waitlistRepository;
//////        this.emailService = emailService;
//////    }
//////
//////    // 1️⃣ CREATE BOOKING
//////    @Override
//////    public Booking createBooking(BookingRequestDto request) {
//////
//////        Classroom classroom = classroomRepository.findById(request.getClassroomId())
//////                .orElseThrow(() -> new RuntimeException("Classroom not found"));
//////
//////        User user = userRepository.findById(request.getUserId())
//////                .orElseThrow(() -> new RuntimeException("User not found"));
//////
//////        List<Booking> conflicts =
//////                bookingRepository.findByClassroomIdAndBookingDateAndStartTimeLessThanAndEndTimeGreaterThan(
//////                        request.getClassroomId(),
//////                        request.getDate(),
//////                        request.getEndTime(),
//////                        request.getStartTime()
//////                );
//////
//////        if (!conflicts.isEmpty()) {
//////            Waitlist w = new Waitlist();
//////            w.setUserId(user.getId());
//////            w.setClassroomId(classroom.getId());
//////
//////            w.setDate(request.getDate());
//////            w.setStartTime(request.getStartTime());
//////            w.setEndTime(request.getEndTime());
//////            waitlistRepository.save(w);
//////
//////            throw new RuntimeException("Slot already booked. Added to waitlist.");
//////        }
//////
//////        int hours = (int) Duration.between(
//////                request.getStartTime(),
//////                request.getEndTime()
//////        ).toHours();
//////
//////        double pricePerHour = calculatePrice(classroom, request.getStartTime());
//////        double totalPrice = hours * pricePerHour;
//////
//////        Booking booking = new Booking();
//////        booking.setUser(user);            // ✅ correct (User entity)
//////        booking.setClassroom(classroom);  // ✅ correct (Classroom entity)
//////        booking.setBookingDate(request.getDate());
//////        booking.setStartTime(request.getStartTime());
//////        booking.setEndTime(request.getEndTime());
//////        booking.setTotalHours(hours);
//////        booking.setTotalPrice(totalPrice);
//////        booking.setStatus(BookingStatus.BOOKED);
//////        booking.setCreatedAt(LocalDateTime.now());
//////
//////
//////
//////        Booking saved = bookingRepository.save(booking);
//////
//////        emailService.sendBookingConfirmation(
//////                user.getEmail(),
//////                classroom.getName(),
//////                request.getDate().toString(),
//////                request.getStartTime() + " - " + request.getEndTime(),
//////                totalPrice
//////        );
//////
//////        return saved;
//////    }
//////
//////    // 🔹 Dynamic pricing
//////    private double calculatePrice(Classroom classroom, LocalTime startTime) {
//////        if (classroom.getPeakStart() == null || classroom.getPeakEnd() == null) {
//////            return classroom.getPricePerHour();
//////        }
//////
//////        LocalTime peakStart = LocalTime.parse(classroom.getPeakStart());
//////        LocalTime peakEnd = LocalTime.parse(classroom.getPeakEnd());
//////
//////        if (!startTime.isBefore(peakStart) && !startTime.isAfter(peakEnd)) {
//////            return classroom.getPeakPricePerHour();
//////        }
//////        return classroom.getPricePerHour();
//////    }
//////
//////    // 2️⃣ AVAILABILITY
//////    @Override
//////    public List<AvailabilitySlot> getAvailability(Long classroomId, LocalDate date) {
//////        List<Booking> bookings =
//////                bookingRepository.findByClassroomIdAndBookingDate(classroomId, date);
//////
//////        List<AvailabilitySlot> slots = new ArrayList<>();
//////
//////        LocalTime start = LocalTime.of(8, 0);
//////        LocalTime end = LocalTime.of(20, 0);
//////
//////        while (start.isBefore(end)) {
//////            LocalTime slotEnd = start.plusHours(1);
//////            boolean available = true;
//////
//////            for (Booking booking : bookings) {
//////                if (booking.getStartTime().isBefore(slotEnd)
//////                        && booking.getEndTime().isAfter(start)) {
//////                    available = false;
//////                    break;
//////                }
//////            }
//////
//////            slots.add(new AvailabilitySlot(start, available));
//////            start = slotEnd;
//////        }
//////        return slots;
//////    }
//////
//////    // 3️⃣ DASHBOARD
//////    @Override
//////    public BookingReport getDashboardReport() {
//////        long totalBookings = bookingRepository.countByStatus(BookingStatus.BOOKED);
//////        double revenue = bookingRepository.sumRevenueByStatus(BookingStatus.BOOKED);
//////        return new BookingReport(totalBookings, revenue);
//////    }
//////
//////    // 4️⃣ MY BOOKINGS
//////    @Override
//////    public List<Booking> findByUserId(Long userId) {
//////        return bookingRepository.findByUserId(userId);
//////    }
//////
//////    // 5️⃣ CANCEL BOOKING
//////    @Override
//////    public Booking cancelBooking(Long id) {
//////        Booking booking = bookingRepository.findById(id)
//////                .orElseThrow(() -> new RuntimeException("Booking not found"));
//////
//////        if (booking.getStatus() == BookingStatus.CANCELLED) {
//////            throw new RuntimeException("Already cancelled");
//////        }
//////
//////        booking.setStatus(BookingStatus.CANCELLED);
//////        return bookingRepository.save(booking);
//////    }
//////
//////    // 6️⃣ ANALYTICS
//////    @Override
//////    public List<DailyRevenueDTO> getAnalytics() {
//////        LocalDate startDate = LocalDate.now().minusDays(7);
//////        List<Object[]> raw = bookingRepository.findDailyRevenue(startDate);
//////
//////        List<DailyRevenueDTO> result = new ArrayList<>();
//////        for (Object[] row : raw) {
//////            result.add(new DailyRevenueDTO(
//////                    row[0].toString(),
//////                    ((Number) row[1]).doubleValue(),
//////                    ((Number) row[2]).longValue()
//////            ));
//////        }
//////        return result;
//////    }
//////}
////
////
////
//////package com.tatamotors.classbooking.service;
//////
//////import com.tatamotors.classbooking.dto.AvailabilitySlot;
//////import com.tatamotors.classbooking.dto.BookingReport;
//////import com.tatamotors.classbooking.dto.BookingRequestDto;
//////import com.tatamotors.classbooking.dto.DailyRevenueDTO;
//////import com.tatamotors.classbooking.entity.Booking;
//////import com.tatamotors.classbooking.entity.BookingStatus;
//////import com.tatamotors.classbooking.entity.Classroom;
//////import com.tatamotors.classbooking.entity.User;
//////import com.tatamotors.classbooking.entity.Waitlist;
//////import com.tatamotors.classbooking.repository.*;
//////import org.springframework.stereotype.Service;
//////
//////import java.time.Duration;
//////import java.time.LocalDate;
//////import java.time.LocalDateTime;
//////import java.time.LocalTime;
//////import java.util.ArrayList;
//////import java.util.List;
//////
//////@Service
//////public class BookingServiceImpl implements BookingService {
//////
//////    private final BookingRepository bookingRepository;
//////    private final ClassroomRepository classroomRepository;
//////    private final UserRepository userRepository;
//////    private final WaitlistRepository waitlistRepository;
//////    private final EmailService emailService;
//////
//////    public BookingServiceImpl(
//////            BookingRepository bookingRepository,
//////            ClassroomRepository classroomRepository,
//////            UserRepository userRepository,
//////            WaitlistRepository waitlistRepository,
//////            EmailService emailService) {
//////        this.bookingRepository = bookingRepository;
//////        this.classroomRepository = classroomRepository;
//////        this.userRepository = userRepository;
//////        this.waitlistRepository = waitlistRepository;
//////        this.emailService = emailService;
//////    }
//////
//////    @Override
//////    public Booking createBooking(BookingRequestDto request) {
//////
//////        Classroom classroom = classroomRepository.findById(request.getClassroomId())
//////                .orElseThrow(() -> new RuntimeException("Classroom not found"));
//////
//////        User user = userRepository.findById(request.getUserId())
//////                .orElseThrow(() -> new RuntimeException("User not found"));
//////
//////        // Check for conflicts
//////        List<Booking> conflicts =
//////                bookingRepository.findByClassroomIdAndBookingDateAndStartTimeLessThanAndEndTimeGreaterThan(
//////                        request.getClassroomId(),
//////                        request.getDate(),
//////                        request.getEndTime(),
//////                        request.getStartTime());
//////
//////        if (!conflicts.isEmpty()) {
//////            Waitlist w = new Waitlist();
//////            w.setUserId(user.getId());
//////            w.setClassroomId(classroom.getId());
//////            w.setDate(request.getDate());
//////            w.setStartTime(request.getStartTime());
//////            w.setEndTime(request.getEndTime());
//////            waitlistRepository.save(w);
//////            throw new RuntimeException("Slot already booked. Added to waitlist.");
//////        }
//////
//////        int hours = (int) Duration.between(request.getStartTime(), request.getEndTime()).toHours();
//////        double pricePerHour = calculatePrice(classroom, request.getStartTime());
//////        double totalPrice = hours * pricePerHour;
//////
//////        Booking booking = new Booking();
//////        booking.setUser(user);
//////        booking.setClassroom(classroom);
//////        booking.setBookingDate(request.getDate());
//////        booking.setStartTime(request.getStartTime());
//////        booking.setEndTime(request.getEndTime());
//////        booking.setTotalHours(hours);
//////        booking.setTotalPrice(totalPrice);
//////        booking.setStatus(BookingStatus.BOOKED);
//////        booking.setCreatedAt(LocalDateTime.now());
//////
//////        Booking saved = bookingRepository.save(booking);
//////
//////        // Send confirmation email - wrapped in try/catch so email failure
//////        // does NOT cancel the booking
//////        try {
//////            emailService.sendBookingConfirmation(
//////                    user.getEmail(),
//////                    classroom.getName(),
//////                    request.getDate().toString(),
//////                    request.getStartTime() + " - " + request.getEndTime(),
//////                    totalPrice
//////            );
//////            System.out.println("Confirmation email sent to: " + user.getEmail());
//////        } catch (Exception e) {
//////            // Log but don't fail the booking
//////            System.err.println("Email failed (booking still saved): " + e.getMessage());
//////        }
//////
//////        return saved;
//////    }
//////
//////    private double calculatePrice(Classroom classroom, LocalTime startTime) {
//////        if (classroom.getPeakStart() == null || classroom.getPeakEnd() == null) {
//////            return classroom.getPricePerHour();
//////        }
//////        LocalTime peakStart = LocalTime.parse(classroom.getPeakStart());
//////        LocalTime peakEnd = LocalTime.parse(classroom.getPeakEnd());
//////        if (!startTime.isBefore(peakStart) && !startTime.isAfter(peakEnd)) {
//////            return classroom.getPeakPricePerHour();
//////        }
//////        return classroom.getPricePerHour();
//////    }
//////
//////    @Override
//////    public List<AvailabilitySlot> getAvailability(Long classroomId, LocalDate date) {
//////        List<Booking> bookings = bookingRepository.findByClassroomIdAndBookingDate(classroomId, date);
//////        List<AvailabilitySlot> slots = new ArrayList<>();
//////        LocalTime start = LocalTime.of(8, 0);
//////        LocalTime end = LocalTime.of(20, 0);
//////        while (start.isBefore(end)) {
//////            LocalTime slotEnd = start.plusHours(1);
//////            boolean available = true;
//////            for (Booking booking : bookings) {
//////                if (booking.getStatus() == BookingStatus.BOOKED &&
//////                    booking.getStartTime().isBefore(slotEnd) &&
//////                    booking.getEndTime().isAfter(start)) {
//////                    available = false;
//////                    break;
//////                }
//////            }
//////            slots.add(new AvailabilitySlot(start, available));
//////            start = slotEnd;
//////        }
//////        return slots;
//////    }
//////
//////    @Override
//////    public BookingReport getDashboardReport() {
//////        long totalBookings = bookingRepository.countByStatus(BookingStatus.BOOKED);
//////        double revenue = bookingRepository.sumRevenueByStatus(BookingStatus.BOOKED);
//////        return new BookingReport(totalBookings, revenue);
//////    }
//////
//////    @Override
//////    public List<Booking> findByUserId(Long userId) {
//////        return bookingRepository.findByUserId(userId);
//////    }
//////
//////    @Override
//////    public Booking cancelBooking(Long id) {
//////        Booking booking = bookingRepository.findById(id)
//////                .orElseThrow(() -> new RuntimeException("Booking not found"));
//////
//////        if (booking.getStatus() == BookingStatus.CANCELLED) {
//////            throw new RuntimeException("Already cancelled");
//////        }
//////
//////        booking.setStatus(BookingStatus.CANCELLED);
//////        Booking saved = bookingRepository.save(booking);
//////
//////        // Send cancellation email - also wrapped so it doesn't break cancel
//////        try {
//////            String userEmail = booking.getUser() != null ? booking.getUser().getEmail() : null;
//////            if (userEmail != null) {
//////                emailService.sendCancellationEmail(
//////                        userEmail,
//////                        booking.getClassroom() != null ? booking.getClassroom().getName() : "Classroom",
//////                        booking.getBookingDate().toString(),
//////                        booking.getStartTime() + " - " + booking.getEndTime()
//////                );
//////                System.out.println("Cancellation email sent to: " + userEmail);
//////            }
//////        } catch (Exception e) {
//////            System.err.println("Cancellation email failed: " + e.getMessage());
//////        }
//////
//////        return saved;
//////    }
//////
//////    @Override
//////    public List<DailyRevenueDTO> getAnalytics() {
//////        LocalDate startDate = LocalDate.now().minusDays(7);
//////        List<Object[]> raw = bookingRepository.findDailyRevenue(startDate);
//////        List<DailyRevenueDTO> result = new ArrayList<>();
//////        for (Object[] row : raw) {
//////            result.add(new DailyRevenueDTO(
//////                    row[0].toString(),
//////                    ((Number) row[1]).doubleValue(),
//////                    ((Number) row[2]).longValue()
//////            ));
//////        }
//////        return result;
//////    }
//////}
////
////
////
////
////package com.tatamotors.classbooking.service;
////
////import com.tatamotors.classbooking.dto.AvailabilitySlot;
////import com.tatamotors.classbooking.dto.BookingReport;
////import com.tatamotors.classbooking.dto.BookingRequestDto;
////import com.tatamotors.classbooking.dto.DailyRevenueDTO;
////import com.tatamotors.classbooking.entity.*;
////import com.tatamotors.classbooking.repository.*;
////import org.springframework.stereotype.Service;
////
////import java.time.Duration;
////import java.time.LocalDate;
////import java.time.LocalDateTime;
////import java.time.LocalTime;
////import java.util.ArrayList;
////import java.util.List;
////
////import java.util.Base64;
////
////
////@Service
////public class BookingServiceImpl implements BookingService {
////
////    private final BookingRepository bookingRepository;
////    private final ClassroomRepository classroomRepository;
////    private final UserRepository userRepository;
////    private final WaitlistRepository waitlistRepository;
////    private final EmailService emailService;
////    private final SystemSettingsRepository settingsRepository;
////
////    public BookingServiceImpl(
////            BookingRepository bookingRepository,
////            ClassroomRepository classroomRepository,
////            UserRepository userRepository,
////            WaitlistRepository waitlistRepository,
////            EmailService emailService,
////            SystemSettingsRepository settingsRepository) {
////        this.bookingRepository = bookingRepository;
////        this.classroomRepository = classroomRepository;
////        this.userRepository = userRepository;
////        this.waitlistRepository = waitlistRepository;
////        this.emailService = emailService;
////        this.settingsRepository = settingsRepository;
////    }
////
////    @Override
////    public Booking createBooking(BookingRequestDto request) {
////
////        Classroom classroom = classroomRepository.findById(request.getClassroomId())
////                .orElseThrow(() -> new RuntimeException("Classroom not found"));
////
////        User user = userRepository.findById(request.getUserId())
////                .orElseThrow(() -> new RuntimeException("User not found"));
////
////        // Check conflicts
////        List<Booking> conflicts =
////                bookingRepository.findByClassroomIdAndBookingDateAndStartTimeLessThanAndEndTimeGreaterThan(
////                        request.getClassroomId(),
////                        request.getDate(),
////                        request.getEndTime(),
////                        request.getStartTime());
////
////        if (!conflicts.isEmpty()) {
////            Waitlist w = new Waitlist();
////            w.setUserId(user.getId());
////            w.setClassroomId(classroom.getId());
////            w.setDate(request.getDate());
////            w.setStartTime(request.getStartTime());
////            w.setEndTime(request.getEndTime());
////            waitlistRepository.save(w);
////            throw new RuntimeException("Slot already booked. Added to waitlist.");
////        }
////
////        int hours = (int) Duration.between(request.getStartTime(), request.getEndTime()).toHours();
////        double pricePerHour = calculatePrice(classroom, request.getStartTime());
////        double totalPrice = hours * pricePerHour;
////
////        // READ SYSTEM SETTINGS to decide status
////        SystemSettings settings = settingsRepository.findById(1)
////                .orElse(new SystemSettings());
////
////        // If require approval is ON → PENDING, else → BOOKED
////        BookingStatus status = settings.isRequireApproval()
////                ? BookingStatus.PENDING
////                : BookingStatus.BOOKED;
////
////        Booking booking = new Booking();
////        booking.setUser(user);
////        booking.setClassroom(classroom);
////        booking.setBookingDate(request.getDate());
////        booking.setStartTime(request.getStartTime());
////        booking.setEndTime(request.getEndTime());
////        booking.setTotalHours(hours);
////        booking.setTotalPrice(totalPrice);
////        booking.setStatus(status);
////        booking.setCreatedAt(LocalDateTime.now());
////
////        Booking saved = bookingRepository.save(booking);
////
////        // Send email based on status
////        try {
////            if (status == BookingStatus.PENDING) {
////                emailService.sendPendingApprovalEmail(
////                        user.getEmail(),
////                        classroom.getName(),
////                        request.getDate().toString(),
////                        request.getStartTime() + " - " + request.getEndTime(),
////                        totalPrice
////                );
////            } else {
////                emailService.sendBookingConfirmation(
////                        user.getEmail(),
////                        classroom.getName(),
////                        request.getDate().toString(),
////                        request.getStartTime() + " - " + request.getEndTime(),
////                        totalPrice
////                );
////            }
////        } catch (Exception e) {
////            System.err.println("Email failed (booking still saved): " + e.getMessage());
////        }
////
////        return saved;
////    }
////    
//// // 👇 ADD THIS METHOD HERE
////    public Long extractUserIdFromToken(String token) {
////        try {
////            String[] parts = token.split("\\.");
////            String payload = new String(Base64.getDecoder().decode(parts[1]));
////
////            if (payload.contains("userId")) {
////                String id = payload.split("\"userId\":")[1]
////                        .split(",")[0]
////                        .replaceAll("[^0-9]", "");
////                return Long.parseLong(id);
////            }
////        } catch (Exception e) {
////            throw new RuntimeException("Invalid token");
////        }
////
////        throw new RuntimeException("User not found in token");
////    }
////
////    private double calculatePrice(Classroom classroom, LocalTime startTime) {
////        if (classroom.getPeakStart() == null || classroom.getPeakEnd() == null) {
////            return classroom.getPricePerHour();
////        }
////        LocalTime peakStart = LocalTime.parse(classroom.getPeakStart());
////        LocalTime peakEnd = LocalTime.parse(classroom.getPeakEnd());
////        if (!startTime.isBefore(peakStart) && !startTime.isAfter(peakEnd)) {
////            return classroom.getPeakPricePerHour();
////        }
////        return classroom.getPricePerHour();
////    }
////
////    @Override
////    public List<AvailabilitySlot> getAvailability(Long classroomId, LocalDate date) {
////        List<Booking> bookings = bookingRepository.findByClassroomIdAndBookingDate(classroomId, date);
////        List<AvailabilitySlot> slots = new ArrayList<>();
////
////        // Read operating hours from settings
////        LocalTime start = LocalTime.of(8, 0);
////        LocalTime end = LocalTime.of(20, 0);
////        try {
////            SystemSettings settings = settingsRepository.findById(1).orElse(new SystemSettings());
////            start = LocalTime.parse(settings.getOperatingStart());
////            end = LocalTime.parse(settings.getOperatingEnd());
////        } catch (Exception ignored) {}
////
////        while (start.isBefore(end)) {
////            LocalTime slotEnd = start.plusHours(1);
////            boolean available = true;
////            for (Booking booking : bookings) {
////                // PENDING bookings also block the slot
////                if ((booking.getStatus() == BookingStatus.BOOKED ||
////                     booking.getStatus() == BookingStatus.PENDING) &&
////                    booking.getStartTime().isBefore(slotEnd) &&
////                    booking.getEndTime().isAfter(start)) {
////                    available = false;
////                    break;
////                }
////            }
////            slots.add(new AvailabilitySlot(start, available));
////            start = slotEnd;
////        }
////        return slots;
////    }
////
////    @Override
////    public BookingReport getDashboardReport() {
////        long totalBookings = bookingRepository.countByStatus(BookingStatus.BOOKED);
////        double revenue = bookingRepository.sumRevenueByStatus(BookingStatus.BOOKED);
////        return new BookingReport(totalBookings, revenue);
////    }
////
////    @Override
////    public List<Booking> findByUserId(Long userId) {
////        return bookingRepository.findByUserId(userId);
////    }
////
////    @Override
////    public Booking cancelBooking(Long id) {
////        Booking booking = bookingRepository.findById(id)
////                .orElseThrow(() -> new RuntimeException("Booking not found"));
////        if (booking.getStatus() == BookingStatus.CANCELLED) {
////            throw new RuntimeException("Already cancelled");
////        }
////        booking.setStatus(BookingStatus.CANCELLED);
////        Booking saved = bookingRepository.save(booking);
////        try {
////            if (booking.getUser() != null) {
////                emailService.sendCancellationEmail(
////                        booking.getUser().getEmail(),
////                        booking.getClassroom() != null ? booking.getClassroom().getName() : "Classroom",
////                        booking.getBookingDate().toString(),
////                        booking.getStartTime() + " - " + booking.getEndTime()
////                );
////            }
////        } catch (Exception e) {
////            System.err.println("Cancellation email failed: " + e.getMessage());
////        }
////        return saved;
////    }
////
////    @Override
////    public Booking approveBooking(Long id) {
////        Booking booking = bookingRepository.findById(id)
////                .orElseThrow(() -> new RuntimeException("Booking not found"));
////        if (booking.getStatus() != BookingStatus.PENDING) {
////            throw new RuntimeException("Booking is not pending approval");
////        }
////        booking.setStatus(BookingStatus.BOOKED);
////        Booking saved = bookingRepository.save(booking);
////        try {
////            emailService.sendBookingConfirmation(
////                    booking.getUser().getEmail(),
////                    booking.getClassroom().getName(),
////                    booking.getBookingDate().toString(),
////                    booking.getStartTime() + " - " + booking.getEndTime(),
////                    booking.getTotalPrice()
////            );
////        } catch (Exception e) {
////            System.err.println("Approval email failed: " + e.getMessage());
////        }
////        return saved;
////    }
////
////    @Override
////    public List<DailyRevenueDTO> getAnalytics() {
////        LocalDate startDate = LocalDate.now().minusDays(7);
////        List<Object[]> raw = bookingRepository.findDailyRevenue(startDate);
////        List<DailyRevenueDTO> result = new ArrayList<>();
////        for (Object[] row : raw) {
////            result.add(new DailyRevenueDTO(
////                    row[0].toString(),
////                    ((Number) row[1]).doubleValue(),
////                    ((Number) row[2]).longValue()
////            ));
////        }
////        return result;
////    }
////}
//
//package com.tatamotors.classbooking.service;
//
//import com.tatamotors.classbooking.dto.AvailabilitySlot;
//import com.tatamotors.classbooking.dto.BookingReport;
//import com.tatamotors.classbooking.dto.BookingRequestDto;
//import com.tatamotors.classbooking.dto.DailyRevenueDTO;
//import com.tatamotors.classbooking.entity.*;
//import com.tatamotors.classbooking.repository.*;
//import org.springframework.stereotype.Service;
//
//import java.time.Duration;
//import java.time.LocalDate;
//import java.time.LocalDateTime;
//import java.time.LocalTime;
//import java.util.ArrayList;
//import java.util.List;
//
//@Service
//public class BookingServiceImpl implements BookingService {
//
//    private final BookingRepository bookingRepository;
//    private final ClassroomRepository classroomRepository;
//    private final UserRepository userRepository;
//    private final WaitlistRepository waitlistRepository;
//    private final EmailService emailService;
//    private final SystemSettingsRepository settingsRepository;
//
//    public BookingServiceImpl(
//            BookingRepository bookingRepository,
//            ClassroomRepository classroomRepository,
//            UserRepository userRepository,
//            WaitlistRepository waitlistRepository,
//            EmailService emailService,
//            SystemSettingsRepository settingsRepository) {
//        this.bookingRepository = bookingRepository;
//        this.classroomRepository = classroomRepository;
//        this.userRepository = userRepository;
//        this.waitlistRepository = waitlistRepository;
//        this.emailService = emailService;
//        this.settingsRepository = settingsRepository;
//    }
//    
//    @Override
//    public Long extractUserIdFromToken(String token) {
//        // temporary dummy logic
//        return 1L;
//    }
//
//    @Override
//    public Booking createBooking(BookingRequestDto request) {
//
//        Classroom classroom = classroomRepository.findById(request.getClassroomId())
//                .orElseThrow(() -> new RuntimeException("Classroom not found"));
//
//        User user = userRepository.findById(request.getUserId())
//                .orElseThrow(() -> new RuntimeException("User not found"));
//
//        // Check conflicts
//        List<Booking> conflicts =
//                bookingRepository.findByClassroomIdAndBookingDateAndStartTimeLessThanAndEndTimeGreaterThan(
//                        request.getClassroomId(),
//                        request.getDate(),
//                        request.getEndTime(),
//                        request.getStartTime());
//
//        if (!conflicts.isEmpty()) {
//            Waitlist w = new Waitlist();
//            w.setUserId(user.getId());
//            w.setClassroomId(classroom.getId());
//            w.setDate(request.getDate());
//            w.setStartTime(request.getStartTime());
//            w.setEndTime(request.getEndTime());
//            waitlistRepository.save(w);
//            throw new RuntimeException("Slot already booked. Added to waitlist.");
//        }
//
//        int hours = (int) Duration.between(request.getStartTime(), request.getEndTime()).toHours();
//        double pricePerHour = calculatePrice(classroom, request.getStartTime());
//        double totalPrice = hours * pricePerHour;
//
//        // READ SYSTEM SETTINGS to decide status
//        SystemSettings settings = settingsRepository.findById(1)
//                .orElse(new SystemSettings());
//
//        // If require approval is ON → PENDING, else → BOOKED
//        BookingStatus status = settings.isRequireApproval()
//                ? BookingStatus.PENDING
//                : BookingStatus.BOOKED;
//
//        Booking booking = new Booking();
//        booking.setUser(user);
//        booking.setClassroom(classroom);
//        booking.setBookingDate(request.getDate());
//        booking.setStartTime(request.getStartTime());
//        booking.setEndTime(request.getEndTime());
//        booking.setTotalHours(hours);
//        booking.setTotalPrice(totalPrice);
//        booking.setStatus(status);
//        booking.setCreatedAt(LocalDateTime.now());
//
//        Booking saved = bookingRepository.save(booking);
//
//        // Send email based on status
//        try {
//            if (status == BookingStatus.PENDING) {
//                emailService.sendPendingApprovalEmail(
//                        user.getEmail(),
//                        classroom.getName(),
//                        request.getDate().toString(),
//                        request.getStartTime() + " - " + request.getEndTime(),
//                        totalPrice
//                );
//            } else {
//                emailService.sendBookingConfirmation(
//                        user.getEmail(),
//                        classroom.getName(),
//                        request.getDate().toString(),
//                        request.getStartTime() + " - " + request.getEndTime(),
//                        totalPrice
//                );
//            }
//        } catch (Exception e) {
//            System.err.println("Email failed (booking still saved): " + e.getMessage());
//        }
//
//        return saved;
//    }
//
//    private double calculatePrice(Classroom classroom, LocalTime startTime) {
//        if (classroom.getPeakStart() == null || classroom.getPeakEnd() == null) {
//            return classroom.getPricePerHour();
//        }
//        LocalTime peakStart = LocalTime.parse(classroom.getPeakStart());
//        LocalTime peakEnd = LocalTime.parse(classroom.getPeakEnd());
//        if (!startTime.isBefore(peakStart) && !startTime.isAfter(peakEnd)) {
//            return classroom.getPeakPricePerHour();
//        }
//        return classroom.getPricePerHour();
//    }
//
//    @Override
//    public List<AvailabilitySlot> getAvailability(Long classroomId, LocalDate date) {
//        List<Booking> bookings = bookingRepository.findByClassroomIdAndBookingDate(classroomId, date);
//        List<AvailabilitySlot> slots = new ArrayList<>();
//
//        // Read operating hours from settings
//        LocalTime start = LocalTime.of(8, 0);
//        LocalTime end = LocalTime.of(20, 0);
//        try {
//            SystemSettings settings = settingsRepository.findById(1).orElse(new SystemSettings());
//            start = LocalTime.parse(settings.getOperatingStart());
//            end = LocalTime.parse(settings.getOperatingEnd());
//        } catch (Exception ignored) {}
//
//        while (start.isBefore(end)) {
//            LocalTime slotEnd = start.plusHours(1);
//            boolean available = true;
//            for (Booking booking : bookings) {
//                // PENDING bookings also block the slot
//                if ((booking.getStatus() == BookingStatus.BOOKED ||
//                     booking.getStatus() == BookingStatus.PENDING) &&
//                    booking.getStartTime().isBefore(slotEnd) &&
//                    booking.getEndTime().isAfter(start)) {
//                    available = false;
//                    break;
//                }
//            }
//            slots.add(new AvailabilitySlot(start, available));
//            start = slotEnd;
//        }
//        return slots;
//    }
//
//    @Override
//    public BookingReport getDashboardReport() {
//        long totalBookings = bookingRepository.countByStatus(BookingStatus.BOOKED);
//        double revenue = bookingRepository.sumRevenueByStatus(BookingStatus.BOOKED);
//        return new BookingReport(totalBookings, revenue);
//    }
//
//    @Override
//    public List<Booking> findByUserId(Long userId) {
//        return bookingRepository.findByUserId(userId);
//    }
//
//    @Override
//    public Booking cancelBooking(Long id) {
//        Booking booking = bookingRepository.findById(id)
//                .orElseThrow(() -> new RuntimeException("Booking not found"));
//        if (booking.getStatus() == BookingStatus.CANCELLED) {
//            throw new RuntimeException("Already cancelled");
//        }
//        booking.setStatus(BookingStatus.CANCELLED);
//        Booking saved = bookingRepository.save(booking);
//        try {
//            if (booking.getUser() != null) {
//                emailService.sendCancellationEmail(
//                        booking.getUser().getEmail(),
//                        booking.getClassroom() != null ? booking.getClassroom().getName() : "Classroom",
//                        booking.getBookingDate().toString(),
//                        booking.getStartTime() + " - " + booking.getEndTime()
//                );
//            }
//        } catch (Exception e) {
//            System.err.println("Cancellation email failed: " + e.getMessage());
//        }
//        return saved;
//    }
//
//    @Override
//    public Booking approveBooking(Long id) {
//        Booking booking = bookingRepository.findById(id)
//                .orElseThrow(() -> new RuntimeException("Booking not found"));
//        if (booking.getStatus() != BookingStatus.PENDING) {
//            throw new RuntimeException("Booking is not pending approval");
//        }
//        booking.setStatus(BookingStatus.BOOKED);
//        Booking saved = bookingRepository.save(booking);
//        try {
//            emailService.sendBookingConfirmation(
//                    booking.getUser().getEmail(),
//                    booking.getClassroom().getName(),
//                    booking.getBookingDate().toString(),
//                    booking.getStartTime() + " - " + booking.getEndTime(),
//                    booking.getTotalPrice()
//            );
//        } catch (Exception e) {
//            System.err.println("Approval email failed: " + e.getMessage());
//        }
//        return saved;
//    }
//
//    @Override
//    public List<DailyRevenueDTO> getAnalytics() {
//        LocalDate startDate = LocalDate.now().minusDays(7);
//        List<Object[]> raw = bookingRepository.findDailyRevenue(startDate);
//        List<DailyRevenueDTO> result = new ArrayList<>();
//        for (Object[] row : raw) {
//            result.add(new DailyRevenueDTO(
//                    row[0].toString(),
//                    ((Number) row[1]).doubleValue(),
//                    ((Number) row[2]).longValue()
//            ));
//        }
//        return result;
//    }
//}

package com.tatamotors.classbooking.service;

import com.tatamotors.classbooking.dto.AvailabilitySlot;
import com.tatamotors.classbooking.dto.BookingReport;
import com.tatamotors.classbooking.dto.BookingRequestDto;
import com.tatamotors.classbooking.dto.DailyRevenueDTO;
import com.tatamotors.classbooking.entity.*;
import com.tatamotors.classbooking.repository.*;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;
    private final ClassroomRepository classroomRepository;
    private final UserRepository userRepository;
    private final WaitlistRepository waitlistRepository;
    private final EmailService emailService;
    private final SystemSettingsRepository settingsRepository;

    public BookingServiceImpl(
            BookingRepository bookingRepository,
            ClassroomRepository classroomRepository,
            UserRepository userRepository,
            WaitlistRepository waitlistRepository,
            EmailService emailService,
            SystemSettingsRepository settingsRepository) {
        this.bookingRepository = bookingRepository;
        this.classroomRepository = classroomRepository;
        this.userRepository = userRepository;
        this.waitlistRepository = waitlistRepository;
        this.emailService = emailService;
        this.settingsRepository = settingsRepository;
    }

    @Override
    public Booking createBooking(BookingRequestDto request) {

        Classroom classroom = classroomRepository.findById(request.getClassroomId())
                .orElseThrow(() -> new RuntimeException("Classroom not found"));

        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Check conflicts
        List<Booking> conflicts =
                bookingRepository.findByClassroomIdAndBookingDateAndStartTimeLessThanAndEndTimeGreaterThan(
                        request.getClassroomId(),
                        request.getDate(),
                        request.getEndTime(),
                        request.getStartTime());

        if (!conflicts.isEmpty()) {
            Waitlist w = new Waitlist();
            w.setUserId(user.getId());
            w.setClassroomId(classroom.getId());
            w.setDate(request.getDate());
            w.setStartTime(request.getStartTime());
            w.setEndTime(request.getEndTime());
            waitlistRepository.save(w);
            throw new RuntimeException("Slot already booked. Added to waitlist.");
        }

        int hours = (int) Duration.between(request.getStartTime(), request.getEndTime()).toHours();
        double pricePerHour = calculatePrice(classroom, request.getStartTime());
        double totalPrice = hours * pricePerHour;

        // READ SYSTEM SETTINGS to decide status
        SystemSettings settings = settingsRepository.findById(1)
                .orElse(new SystemSettings());

        // If require approval is ON → PENDING, else → BOOKED
        BookingStatus status = settings.isRequireApproval()
                ? BookingStatus.PENDING
                : BookingStatus.BOOKED;

        Booking booking = new Booking();
        booking.setUser(user);
        booking.setClassroom(classroom);
        booking.setBookingDate(request.getDate());
        booking.setStartTime(request.getStartTime());
        booking.setEndTime(request.getEndTime());
        booking.setTotalHours(hours);
        booking.setTotalPrice(totalPrice);
        booking.setStatus(status);
        booking.setCreatedAt(LocalDateTime.now());

        Booking saved = bookingRepository.save(booking);

        // Send email based on status
        try {
            if (status == BookingStatus.PENDING) {
                emailService.sendPendingApprovalEmail(
                        user.getEmail(),
                        classroom.getName(),
                        request.getDate().toString(),
                        request.getStartTime() + " - " + request.getEndTime(),
                        totalPrice
                );
            } else {
                emailService.sendBookingConfirmation(
                        user.getEmail(),
                        classroom.getName(),
                        request.getDate().toString(),
                        request.getStartTime() + " - " + request.getEndTime(),
                        totalPrice
                );
            }
        } catch (Exception e) {
            System.err.println("Email failed (booking still saved): " + e.getMessage());
        }

        return saved;
    }

    private double calculatePrice(Classroom classroom, LocalTime startTime) {
        if (classroom.getPeakStart() == null || classroom.getPeakEnd() == null) {
            return classroom.getPricePerHour();
        }
        LocalTime peakStart = LocalTime.parse(classroom.getPeakStart());
        LocalTime peakEnd = LocalTime.parse(classroom.getPeakEnd());
        if (!startTime.isBefore(peakStart) && !startTime.isAfter(peakEnd)) {
            return classroom.getPeakPricePerHour();
        }
        return classroom.getPricePerHour();
    }

    @Override
    public List<AvailabilitySlot> getAvailability(Long classroomId, LocalDate date) {
        List<Booking> bookings = bookingRepository.findByClassroomIdAndBookingDate(classroomId, date);
        List<AvailabilitySlot> slots = new ArrayList<>();

        // Read operating hours from settings
        LocalTime start = LocalTime.of(8, 0);
        LocalTime end = LocalTime.of(20, 0);
        try {
            SystemSettings settings = settingsRepository.findById(1).orElse(new SystemSettings());
            start = LocalTime.parse(settings.getOperatingStart());
            end = LocalTime.parse(settings.getOperatingEnd());
        } catch (Exception ignored) {}

        while (start.isBefore(end)) {
            LocalTime slotEnd = start.plusHours(1);
            boolean available = true;
            for (Booking booking : bookings) {
                // PENDING bookings also block the slot
                if ((booking.getStatus() == BookingStatus.BOOKED ||
                     booking.getStatus() == BookingStatus.PENDING) &&
                    booking.getStartTime().isBefore(slotEnd) &&
                    booking.getEndTime().isAfter(start)) {
                    available = false;
                    break;
                }
            }
            slots.add(new AvailabilitySlot(start, available));
            start = slotEnd;
        }
        return slots;
    }

    @Override
    public BookingReport getDashboardReport() {
        long totalBookings = bookingRepository.countByStatus(BookingStatus.BOOKED);
        double revenue = bookingRepository.sumRevenueByStatus(BookingStatus.BOOKED);
        return new BookingReport(totalBookings, revenue);
    }

    @Override
    public List<Booking> findByUserId(Long userId) {
        return bookingRepository.findByUserId(userId);
    }

    @Override
    public Booking cancelBooking(Long id) {
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Booking not found"));
        if (booking.getStatus() == BookingStatus.CANCELLED) {
            throw new RuntimeException("Already cancelled");
        }
        booking.setStatus(BookingStatus.CANCELLED);
        Booking saved = bookingRepository.save(booking);
        try {
            if (booking.getUser() != null) {
                emailService.sendCancellationEmail(
                        booking.getUser().getEmail(),
                        booking.getClassroom() != null ? booking.getClassroom().getName() : "Classroom",
                        booking.getBookingDate().toString(),
                        booking.getStartTime() + " - " + booking.getEndTime()
                );
            }
        } catch (Exception e) {
            System.err.println("Cancellation email failed: " + e.getMessage());
        }
        return saved;
    }

    @Override
    public Booking approveBooking(Long id) {
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Booking not found"));
        if (booking.getStatus() != BookingStatus.PENDING) {
            throw new RuntimeException("Booking is not pending approval");
        }
        booking.setStatus(BookingStatus.BOOKED);
        Booking saved = bookingRepository.save(booking);
        try {
            emailService.sendBookingConfirmation(
                    booking.getUser().getEmail(),
                    booking.getClassroom().getName(),
                    booking.getBookingDate().toString(),
                    booking.getStartTime() + " - " + booking.getEndTime(),
                    booking.getTotalPrice()
            );
        } catch (Exception e) {
            System.err.println("Approval email failed: " + e.getMessage());
        }
        return saved;
    }

    @Override
    public List<DailyRevenueDTO> getAnalytics() {
        LocalDate startDate = LocalDate.now().minusDays(7);
        List<Object[]> raw = bookingRepository.findDailyRevenue(startDate);
        List<DailyRevenueDTO> result = new ArrayList<>();
        for (Object[] row : raw) {
            result.add(new DailyRevenueDTO(
                    row[0].toString(),
                    ((Number) row[1]).doubleValue(),
                    ((Number) row[2]).longValue()
            ));
        }
        return result;
    }
}