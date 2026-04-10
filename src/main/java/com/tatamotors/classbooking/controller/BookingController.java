////////package com.tatamotors.classbooking.controller;
////////
////////import com.tatamotors.classbooking.dto.AvailabilitySlot;
////////import com.tatamotors.classbooking.dto.BookingRequestDto;
////////import com.tatamotors.classbooking.entity.Booking;
////////import com.tatamotors.classbooking.service.BookingService;
////////
////////import org.springframework.http.ResponseEntity;
////////import org.springframework.web.bind.annotation.*;
////////
////////import java.time.LocalDate;
////////import java.util.List;
////////
////////@RestController
////////@RequestMapping("/api/bookings")
////////@CrossOrigin
////////public class BookingController {
////////
////////    private final BookingService bookingService;
////////
////////    public BookingController(BookingService bookingService) {
////////        this.bookingService = bookingService;
////////    }
////////
////////    // CREATE BOOKING
////////    @PostMapping
////////    public ResponseEntity<?> createBooking(@RequestBody BookingRequestDto request) {
////////        try {
////////            return ResponseEntity.ok(bookingService.createBooking(request));
////////        } catch (RuntimeException e) {
////////            return ResponseEntity.status(409).body(e.getMessage());
////////        }
////////    }
////////
////////    // AVAILABILITY
////////    @GetMapping("/availability")
////////    public List<AvailabilitySlot> availability(
////////            @RequestParam Long classroomId,
////////            @RequestParam LocalDate date
////////    ) {
////////        return bookingService.getAvailability(classroomId, date);
////////    }
////////
////////    // MY BOOKINGS
////////    @GetMapping("/my")
////////    public List<Booking> myBookings(@RequestParam Long userId) {
////////        return bookingService.findByUserId(userId);
////////    }
////////
////////    // CANCEL
////////    @PutMapping("/{id}/cancel")
////////    public Booking cancel(@PathVariable Long id) {
////////        return bookingService.cancelBooking(id);
////////    }
////////    
////////    // Add this method inside BookingController.java
////////    // GET all bookings (for Admin Dashboard & All Bookings page)
////////    @GetMapping("/all")
////////    public List<Booking> getAllBookings() {
////////        return bookingRepository.findAll();
////////    }
////////}
//////
//////
////////package com.tatamotors.classbooking.controller;
////////
////////import com.tatamotors.classbooking.dto.AvailabilitySlot;
////////import com.tatamotors.classbooking.dto.BookingRequestDto;
////////import com.tatamotors.classbooking.entity.Booking;
////////import com.tatamotors.classbooking.repository.BookingRepository; // 1. IMPORT REPOSITORY
////////import com.tatamotors.classbooking.service.BookingService;
////////
////////import org.springframework.http.ResponseEntity;
////////import org.springframework.web.bind.annotation.*;
////////
////////import java.time.LocalDate;
////////import java.util.List;
////////
////////@RestController
////////@RequestMapping("/api/bookings")
////////@CrossOrigin
////////public class BookingController {
////////
////////    private final BookingService bookingService;
////////    private final BookingRepository bookingRepository; // 2. INJECT REPOSITORY
////////
////////    // 3. UPDATE CONSTRUCTOR
////////    public BookingController(BookingService bookingService, BookingRepository bookingRepository) {
////////        this.bookingService = bookingService;
////////        this.bookingRepository = bookingRepository;
////////    }
////////
////////    // CREATE BOOKING
////////    @PostMapping
////////    public ResponseEntity<?> createBooking(@RequestBody BookingRequestDto request) {
////////        try {
////////            return ResponseEntity.ok(bookingService.createBooking(request));
////////        } catch (RuntimeException e) {
////////            return ResponseEntity.status(409).body(e.getMessage());
////////        }
////////    }
////////
////////    // AVAILABILITY
////////    @GetMapping("/availability")
////////    public List<AvailabilitySlot> availability(
////////            @RequestParam Long classroomId,
////////            @RequestParam LocalDate date
////////    ) {
////////        return bookingService.getAvailability(classroomId, date);
////////    }
////////
////////    // MY BOOKINGS
////////    @GetMapping("/my")
////////    public List<Booking> myBookings(@RequestParam Long userId) {
////////        return bookingService.findByUserId(userId);
////////    }
////////
////////    // CANCEL
////////    @PutMapping("/{id}/cancel")
////////    public Booking cancel(@PathVariable Long id) {
////////        return bookingService.cancelBooking(id);
////////    }
////////    
////////    // GET ALL BOOKINGS (Fixed)
////////    @GetMapping("/all")
////////    public List<Booking> getAllBookings() {
////////        return bookingRepository.findAll();
////////    }
////////}
//////
//////
////////package com.tatamotors.classbooking.controller;
////////
////////import com.tatamotors.classbooking.dto.AvailabilitySlot;
////////import com.tatamotors.classbooking.dto.BookingRequestDto;
////////import com.tatamotors.classbooking.entity.Booking;
////////import com.tatamotors.classbooking.repository.BookingRepository;
////////import com.tatamotors.classbooking.service.BookingService;
////////
////////import org.springframework.http.ResponseEntity;
////////import org.springframework.web.bind.annotation.*;
////////
////////import java.time.LocalDate;
////////import java.util.*;
////////
////////@RestController
////////@RequestMapping("/api/bookings")
////////@CrossOrigin
////////public class BookingController {
////////
////////    private final BookingService bookingService;
////////    private final BookingRepository bookingRepository;
////////
////////    public BookingController(BookingService bookingService, BookingRepository bookingRepository) {
////////        this.bookingService = bookingService;
////////        this.bookingRepository = bookingRepository;
////////    }
////////
////////    // Helper: convert Booking entity to flat Map so no serialization issues
////////    private Map<String, Object> toDto(Booking b) {
////////        Map<String, Object> dto = new HashMap<>();
////////        dto.put("id", b.getId());
////////        dto.put("date", b.getBookingDate() != null ? b.getBookingDate().toString() : null);
////////        dto.put("startTime", b.getStartTime() != null ? b.getStartTime().toString() : null);
////////        dto.put("endTime", b.getEndTime() != null ? b.getEndTime().toString() : null);
////////        dto.put("status", b.getStatus() != null ? b.getStatus().name() : null);
////////        dto.put("totalCost", b.getTotalPrice());
////////        dto.put("totalHours", b.getTotalHours());
////////
////////        // Flatten classroom
////////        if (b.getClassroom() != null) {
////////            dto.put("classroomId", b.getClassroom().getId());
////////            dto.put("classroomName", b.getClassroom().getName());
////////        } else {
////////            dto.put("classroomId", null);
////////            dto.put("classroomName", "Unknown");
////////        }
////////
////////        // Flatten user
////////        if (b.getUser() != null) {
////////            dto.put("userId", b.getUser().getId());
////////            dto.put("userName", b.getUser().getName());
////////            dto.put("userEmail", b.getUser().getEmail());
////////        } else {
////////            dto.put("userId", null);
////////            dto.put("userName", "Unknown");
////////        }
////////
////////        return dto;
////////    }
////////
////////    // CREATE BOOKING
////////    @PostMapping
////////    public ResponseEntity<?> createBooking(@RequestBody BookingRequestDto request) {
////////        try {
////////            Booking booking = bookingService.createBooking(request);
////////            return ResponseEntity.ok(toDto(booking));
////////        } catch (RuntimeException e) {
////////            return ResponseEntity.status(409).body(e.getMessage());
////////        }
////////    }
////////
////////    // AVAILABILITY
////////    @GetMapping("/availability")
////////    public List<AvailabilitySlot> availability(
////////            @RequestParam Long classroomId,
////////            @RequestParam LocalDate date) {
////////        return bookingService.getAvailability(classroomId, date);
////////    }
////////
////////    // MY BOOKINGS - returns flat DTOs for logged-in user
////////    @GetMapping("/my")
////////    public List<Map<String, Object>> myBookings(@RequestParam Long userId) {
////////        List<Booking> bookings = bookingService.findByUserId(userId);
////////        List<Map<String, Object>> result = new ArrayList<>();
////////        for (Booking b : bookings) {
////////            result.add(toDto(b));
////////        }
////////        return result;
////////    }
////////
////////    // CANCEL
////////    @PutMapping("/{id}/cancel")
////////    public ResponseEntity<?> cancel(@PathVariable Long id) {
////////        try {
////////            Booking booking = bookingService.cancelBooking(id);
////////            return ResponseEntity.ok(toDto(booking));
////////        } catch (RuntimeException e) {
////////            return ResponseEntity.status(404).body(e.getMessage());
////////        }
////////    }
////////
////////    // ALL BOOKINGS (Admin) - returns flat DTOs
////////    @GetMapping("/all")
////////    public List<Map<String, Object>> getAllBookings() {
////////        List<Booking> bookings = bookingRepository.findAll();
////////        List<Map<String, Object>> result = new ArrayList<>();
////////        for (Booking b : bookings) {
////////            result.add(toDto(b));
////////        }
////////        return result;
////////    }
////////}
//////
//////
////////package com.tatamotors.classbooking.controller;
////////
////////import com.tatamotors.classbooking.dto.AvailabilitySlot;
////////import com.tatamotors.classbooking.dto.BookingRequestDto;
////////import com.tatamotors.classbooking.entity.Booking;
////////import com.tatamotors.classbooking.repository.BookingRepository;
////////import com.tatamotors.classbooking.service.BookingService;
////////
////////import org.springframework.http.ResponseEntity;
////////import org.springframework.web.bind.annotation.*;
////////
////////import java.time.LocalDate;
////////import java.util.*;
////////
////////@RestController
////////@RequestMapping("/api/bookings")
////////@CrossOrigin
////////public class BookingController {
////////
////////    private final BookingService bookingService;
////////    private final BookingRepository bookingRepository;
////////
////////    public BookingController(BookingService bookingService, BookingRepository bookingRepository) {
////////        this.bookingService = bookingService;
////////        this.bookingRepository = bookingRepository;
////////    }
////////
////////    // Flat DTO helper
////////    private Map<String, Object> toDto(Booking b) {
////////        Map<String, Object> dto = new HashMap<>();
////////        dto.put("id", b.getId());
////////        dto.put("date", b.getBookingDate() != null ? b.getBookingDate().toString() : null);
////////        dto.put("startTime", b.getStartTime() != null ? b.getStartTime().toString() : null);
////////        dto.put("endTime", b.getEndTime() != null ? b.getEndTime().toString() : null);
////////        dto.put("status", b.getStatus() != null ? b.getStatus().name() : null);
////////        dto.put("totalCost", b.getTotalPrice());
////////        dto.put("totalHours", b.getTotalHours());
////////        if (b.getClassroom() != null) {
////////            dto.put("classroomId", b.getClassroom().getId());
////////            dto.put("classroomName", b.getClassroom().getName());
////////        } else {
////////            dto.put("classroomId", null);
////////            dto.put("classroomName", "Unknown");
////////        }
////////        if (b.getUser() != null) {
////////            dto.put("userId", b.getUser().getId());
////////            dto.put("userName", b.getUser().getName());
////////            dto.put("userEmail", b.getUser().getEmail());
////////        } else {
////////            dto.put("userId", null);
////////            dto.put("userName", "Unknown");
////////        }
////////        return dto;
////////    }
////////
////////    // CREATE
////////    @PostMapping
////////    public ResponseEntity<?> createBooking(@RequestBody BookingRequestDto request) {
////////        try {
////////            return ResponseEntity.ok(toDto(bookingService.createBooking(request)));
////////        } catch (RuntimeException e) {
////////            return ResponseEntity.status(409).body(e.getMessage());
////////        }
////////    }
////////
////////    // AVAILABILITY
////////    @GetMapping("/availability")
////////    public List<AvailabilitySlot> availability(
////////            @RequestParam Long classroomId,
////////            @RequestParam LocalDate date) {
////////        return bookingService.getAvailability(classroomId, date);
////////    }
////////
////////    // MY BOOKINGS
////////    @GetMapping("/my")
////////    public List<Map<String, Object>> myBookings(@RequestParam Long userId) {
////////        List<Map<String, Object>> result = new ArrayList<>();
////////        for (Booking b : bookingService.findByUserId(userId)) result.add(toDto(b));
////////        return result;
////////    }
////////
////////    // ALL BOOKINGS (admin)
////////    @GetMapping("/all")
////////    public List<Map<String, Object>> getAllBookings() {
////////        List<Map<String, Object>> result = new ArrayList<>();
////////        for (Booking b : bookingRepository.findAll()) result.add(toDto(b));
////////        return result;
////////    }
////////
////////    // CANCEL
////////    @PutMapping("/{id}/cancel")
////////    public ResponseEntity<?> cancel(@PathVariable Long id) {
////////        try {
////////            return ResponseEntity.ok(toDto(bookingService.cancelBooking(id)));
////////        } catch (RuntimeException e) {
////////            return ResponseEntity.status(404).body(e.getMessage());
////////        }
////////    }
////////
////////    // ✅ APPROVE (admin approves a PENDING booking)
////////    @PutMapping("/{id}/approve")
////////    public ResponseEntity<?> approve(@PathVariable Long id) {
////////        try {
////////            return ResponseEntity.ok(toDto(bookingService.approveBooking(id)));
////////        } catch (RuntimeException e) {
////////            return ResponseEntity.status(400).body(e.getMessage());
////////        }
////////    }
////////}
//////
//////
////////package com.tatamotors.classbooking.controller;
////////
////////import com.tatamotors.classbooking.dto.AvailabilitySlot;
////////import com.tatamotors.classbooking.dto.BookingRequestDto;
////////import com.tatamotors.classbooking.entity.Booking;
////////import com.tatamotors.classbooking.repository.BookingRepository;
////////import com.tatamotors.classbooking.service.BookingService;
////////
////////import org.springframework.http.ResponseEntity;
////////import org.springframework.web.bind.annotation.*;
////////
////////import java.time.LocalDate;
////////import java.util.*;
////////
////////@RestController
////////@RequestMapping("/api/bookings")
////////@CrossOrigin
////////public class BookingController {
////////
////////    private final BookingService bookingService;
////////    private final BookingRepository bookingRepository;
////////
////////    public BookingController(BookingService bookingService, BookingRepository bookingRepository) {
////////        this.bookingService = bookingService;
////////        this.bookingRepository = bookingRepository;
////////    }
////////
////////    // Flat DTO helper
////////    private Map<String, Object> toDto(Booking b) {
////////        Map<String, Object> dto = new HashMap<>();
////////        dto.put("id", b.getId());
////////        dto.put("date", b.getBookingDate() != null ? b.getBookingDate().toString() : null);
////////        dto.put("startTime", b.getStartTime() != null ? b.getStartTime().toString() : null);
////////        dto.put("endTime", b.getEndTime() != null ? b.getEndTime().toString() : null);
////////        dto.put("status", b.getStatus() != null ? b.getStatus().name() : null);
////////        dto.put("totalCost", b.getTotalPrice());
////////        dto.put("totalHours", b.getTotalHours());
////////        if (b.getClassroom() != null) {
////////            dto.put("classroomId", b.getClassroom().getId());
////////            dto.put("classroomName", b.getClassroom().getName());
////////        } else {
////////            dto.put("classroomId", null);
////////            dto.put("classroomName", "Unknown");
////////        }
////////        if (b.getUser() != null) {
////////            dto.put("userId", b.getUser().getId());
////////            dto.put("userName", b.getUser().getName());
////////            dto.put("userEmail", b.getUser().getEmail());
////////        } else {
////////            dto.put("userId", null);
////////            dto.put("userName", "Unknown");
////////        }
////////        return dto;
////////    }
////////
////////    // CREATE
////////    @PostMapping
////////    public ResponseEntity<?> createBooking(@RequestBody BookingRequestDto request) {
////////        try {
////////            return ResponseEntity.ok(toDto(bookingService.createBooking(request)));
////////        } catch (RuntimeException e) {
////////            return ResponseEntity.status(409).body(e.getMessage());
////////        }
////////    }
////////
////////    // AVAILABILITY
////////    @GetMapping("/availability")
////////    public List<AvailabilitySlot> availability(
////////            @RequestParam Long classroomId,
////////            @RequestParam LocalDate date) {
////////        return bookingService.getAvailability(classroomId, date);
////////    }
////////
////////    // MY BOOKINGS
////////    @GetMapping("/my")
////////    public List<Map<String, Object>> myBookings(@RequestParam Long userId) {
////////        List<Map<String, Object>> result = new ArrayList<>();
////////        for (Booking b : bookingService.findByUserId(userId)) result.add(toDto(b));
////////        return result;
////////    }
////////
////////    // ALL BOOKINGS (admin)
////////    @GetMapping("/all")
////////    public List<Map<String, Object>> getAllBookings() {
////////        List<Map<String, Object>> result = new ArrayList<>();
////////        for (Booking b : bookingRepository.findAll()) result.add(toDto(b));
////////        return result;
////////    }
////////
////////    // CANCEL
////////    @PutMapping("/{id}/cancel")
////////    public ResponseEntity<?> cancel(@PathVariable Long id) {
////////        try {
////////            return ResponseEntity.ok(toDto(bookingService.cancelBooking(id)));
////////        } catch (RuntimeException e) {
////////            return ResponseEntity.status(404).body(e.getMessage());
////////        }
////////    }
////////
////////    // ✅ APPROVE (admin approves a PENDING booking)
////////    @PutMapping("/{id}/approve")
////////    public ResponseEntity<?> approve(@PathVariable Long id) {
////////        try {
////////            return ResponseEntity.ok(toDto(bookingService.approveBooking(id)));
////////        } catch (RuntimeException e) {
////////            return ResponseEntity.status(400).body(e.getMessage());
////////        }
////////    }
////////}
//////
//////
////////package com.tatamotors.classbooking.controller;
////////
////////import com.tatamotors.classbooking.dto.AvailabilitySlot;
////////import com.tatamotors.classbooking.dto.BookingRequestDto;
////////import com.tatamotors.classbooking.entity.Booking;
////////import com.tatamotors.classbooking.repository.BookingRepository;
////////import com.tatamotors.classbooking.service.BookingService;
////////
////////import org.springframework.http.ResponseEntity;
////////import org.springframework.web.bind.annotation.*;
////////
////////import java.time.LocalDate;
////////import java.util.*;
////////
////////@RestController
////////@RequestMapping("/api/bookings")
////////@CrossOrigin
////////public class BookingController {
////////
////////    private final BookingService bookingService;
////////    private final BookingRepository bookingRepository;
////////
////////    public BookingController(BookingService bookingService, BookingRepository bookingRepository) {
////////        this.bookingService = bookingService;
////////        this.bookingRepository = bookingRepository;
////////    }
////////
////////    // Flat DTO helper
////////    private Map<String, Object> toDto(Booking b) {
////////        Map<String, Object> dto = new HashMap<>();
////////        dto.put("id", b.getId());
////////        dto.put("date", b.getBookingDate() != null ? b.getBookingDate().toString() : null);
////////        dto.put("startTime", b.getStartTime() != null ? b.getStartTime().toString() : null);
////////        dto.put("endTime", b.getEndTime() != null ? b.getEndTime().toString() : null);
////////        dto.put("status", b.getStatus() != null ? b.getStatus().name() : null);
////////        dto.put("totalCost", b.getTotalPrice());
////////        dto.put("totalHours", b.getTotalHours());
////////        if (b.getClassroom() != null) {
////////            dto.put("classroomId", b.getClassroom().getId());
////////            dto.put("classroomName", b.getClassroom().getName());
////////        } else {
////////            dto.put("classroomId", null);
////////            dto.put("classroomName", "Unknown");
////////        }
////////        if (b.getUser() != null) {
////////            dto.put("userId", b.getUser().getId());
////////            dto.put("userName", b.getUser().getName());
////////            dto.put("userEmail", b.getUser().getEmail());
////////        } else {
////////            dto.put("userId", null);
////////            dto.put("userName", "Unknown");
////////        }
////////        return dto;
////////    }
////////
////////    // CREATE
////////    @PostMapping
////////    public ResponseEntity<?> createBooking(@RequestBody BookingRequestDto request) {
////////        try {
////////            return ResponseEntity.ok(toDto(bookingService.createBooking(request)));
////////        } catch (RuntimeException e) {
////////            return ResponseEntity.status(409).body(e.getMessage());
////////        }
////////    }
////////
////////    // AVAILABILITY
////////    @GetMapping("/availability")
////////    public List<AvailabilitySlot> availability(
////////            @RequestParam Long classroomId,
////////            @RequestParam LocalDate date) {
////////        return bookingService.getAvailability(classroomId, date);
////////    }
////////
////////    // MY BOOKINGS
////////    @GetMapping("/my")
////////public List<Map<String, Object>> myBookings(@RequestHeader("Authorization") String authHeader) {
////////
////////    // Extract token (remove "Bearer ")
////////    String token = authHeader.substring(7);
////////
////////    // TODO: replace this with your JWT util
////////    Long userId = bookingService.extractUserIdFromToken(token);
////////
////////    List<Map<String, Object>> result = new ArrayList<>();
////////    for (Booking b : bookingService.findByUserId(userId)) {
////////        result.add(toDto(b));
////////    }
////////
////////    return result;
////////}
////////
////////    // ALL BOOKINGS (admin)
////////    @GetMapping("/all")
////////    public List<Map<String, Object>> getAllBookings() {
////////        List<Map<String, Object>> result = new ArrayList<>();
////////        for (Booking b : bookingRepository.findAll()) result.add(toDto(b));
////////        return result;
////////    }
////////
////////    // CANCEL
////////    @PutMapping("/{id}/cancel")
////////    public ResponseEntity<?> cancel(@PathVariable Long id) {
////////        try {
////////            return ResponseEntity.ok(toDto(bookingService.cancelBooking(id)));
////////        } catch (RuntimeException e) {
////////            return ResponseEntity.status(404).body(e.getMessage());
////////        }
////////    }
////////
////////    // ✅ APPROVE (admin approves a PENDING booking)
////////    @PutMapping("/{id}/approve")
////////    public ResponseEntity<?> approve(@PathVariable Long id) {
////////        try {
////////            return ResponseEntity.ok(toDto(bookingService.approveBooking(id)));
////////        } catch (RuntimeException e) {
////////            return ResponseEntity.status(400).body(e.getMessage());
////////        }
////////    }
////////}
//////
//////
//////
//////package com.tatamotors.classbooking.controller;
//////
//////import com.tatamotors.classbooking.dto.AvailabilitySlot;
//////import com.tatamotors.classbooking.dto.BookingRequestDto;
//////import com.tatamotors.classbooking.entity.Booking;
//////import com.tatamotors.classbooking.repository.BookingRepository;
//////import com.tatamotors.classbooking.service.BookingService;
//////
//////import org.springframework.http.ResponseEntity;
//////import org.springframework.web.bind.annotation.*;
//////
//////import java.time.LocalDate;
//////import java.util.*;
//////
//////@RestController
//////@RequestMapping("/api/bookings")
//////@CrossOrigin
//////public class BookingController {
//////
//////    private final BookingService bookingService;
//////    private final BookingRepository bookingRepository;
//////
//////    public BookingController(BookingService bookingService, BookingRepository bookingRepository) {
//////        this.bookingService = bookingService;
//////        this.bookingRepository = bookingRepository;
//////    }
//////
//////    // Flat DTO helper
//////    private Map<String, Object> toDto(Booking b) {
//////        Map<String, Object> dto = new HashMap<>();
//////        dto.put("id", b.getId());
//////        dto.put("date", b.getBookingDate() != null ? b.getBookingDate().toString() : null);
//////        dto.put("startTime", b.getStartTime() != null ? b.getStartTime().toString() : null);
//////        dto.put("endTime", b.getEndTime() != null ? b.getEndTime().toString() : null);
//////        dto.put("status", b.getStatus() != null ? b.getStatus().name() : null);
//////        dto.put("totalCost", b.getTotalPrice());
//////        dto.put("totalHours", b.getTotalHours());
//////        if (b.getClassroom() != null) {
//////            dto.put("classroomId", b.getClassroom().getId());
//////            dto.put("classroomName", b.getClassroom().getName());
//////        } else {
//////            dto.put("classroomId", null);
//////            dto.put("classroomName", "Unknown");
//////        }
//////        if (b.getUser() != null) {
//////            dto.put("userId", b.getUser().getId());
//////            dto.put("userName", b.getUser().getName());
//////            dto.put("userEmail", b.getUser().getEmail());
//////        } else {
//////            dto.put("userId", null);
//////            dto.put("userName", "Unknown");
//////        }
//////        return dto;
//////    }
//////
//////    // CREATE
//////    @PostMapping
//////    public ResponseEntity<?> createBooking(@RequestBody BookingRequestDto request) {
//////        try {
//////            return ResponseEntity.ok(toDto(bookingService.createBooking(request)));
//////        } catch (RuntimeException e) {
//////            return ResponseEntity.status(409).body(e.getMessage());
//////        }
//////    }
//////
//////    // AVAILABILITY
//////    @GetMapping("/availability")
//////    public List<AvailabilitySlot> availability(
//////            @RequestParam Long classroomId,
//////            @RequestParam LocalDate date) {
//////        return bookingService.getAvailability(classroomId, date);
//////    }
//////
//////    // MY BOOKINGS
//////    @GetMapping("/my")
//////    public List<Map<String, Object>> myBookings(@RequestParam Long userId) {
//////        List<Map<String, Object>> result = new ArrayList<>();
//////        for (Booking b : bookingService.findByUserId(userId)) result.add(toDto(b));
//////        return result;
//////    }
//////
//////    // ALL BOOKINGS (admin)
//////    @GetMapping("/all")
//////    public List<Map<String, Object>> getAllBookings() {
//////        List<Map<String, Object>> result = new ArrayList<>();
//////        for (Booking b : bookingRepository.findAll()) result.add(toDto(b));
//////        return result;
//////    }
//////
//////    // CANCEL
//////    @PutMapping("/{id}/cancel")
//////    public ResponseEntity<?> cancel(@PathVariable Long id) {
//////        try {
//////            return ResponseEntity.ok(toDto(bookingService.cancelBooking(id)));
//////        } catch (RuntimeException e) {
//////            return ResponseEntity.status(404).body(e.getMessage());
//////        }
//////    }
//////
//////    // ✅ APPROVE (admin approves a PENDING booking)
//////    @PutMapping("/{id}/approve")
//////    public ResponseEntity<?> approve(@PathVariable Long id) {
//////        try {
//////            return ResponseEntity.ok(toDto(bookingService.approveBooking(id)));
//////        } catch (RuntimeException e) {
//////            return ResponseEntity.status(400).body(e.getMessage());
//////        }
//////    }
//////}
////
////package com.tatamotors.classbooking.controller;
////
////import com.tatamotors.classbooking.dto.AvailabilitySlot;
////import com.tatamotors.classbooking.dto.BookingRequestDto;
////import com.tatamotors.classbooking.entity.Booking;
////import com.tatamotors.classbooking.repository.BookingRepository;
////import com.tatamotors.classbooking.service.BookingService;
////
////import org.springframework.http.ResponseEntity;
////import org.springframework.web.bind.annotation.*;
////
////import java.time.LocalDate;
////import java.util.*;
////
////@RestController
////@RequestMapping("/api/bookings")
////@CrossOrigin
////public class BookingController {
////
////    private final BookingService bookingService;
////    private final BookingRepository bookingRepository;
////
////    public BookingController(BookingService bookingService, BookingRepository bookingRepository) {
////        this.bookingService = bookingService;
////        this.bookingRepository = bookingRepository;
////    }
////
////    // Flat DTO helper
////    private Map<String, Object> toDto(Booking b) {
////        Map<String, Object> dto = new HashMap<>();
////        dto.put("id", b.getId());
////        dto.put("date", b.getBookingDate() != null ? b.getBookingDate().toString() : null);
////        dto.put("startTime", b.getStartTime() != null ? b.getStartTime().toString() : null);
////        dto.put("endTime", b.getEndTime() != null ? b.getEndTime().toString() : null);
////        dto.put("status", b.getStatus() != null ? b.getStatus().name() : null);
////        dto.put("totalCost", b.getTotalPrice());
////        dto.put("totalHours", b.getTotalHours());
////        if (b.getClassroom() != null) {
////            dto.put("classroomId", b.getClassroom().getId());
////            dto.put("classroomName", b.getClassroom().getName());
////        } else {
////            dto.put("classroomId", null);
////            dto.put("classroomName", "Unknown");
////        }
////        if (b.getUser() != null) {
////            dto.put("userId", b.getUser().getId());
////            dto.put("userName", b.getUser().getName());
////            dto.put("userEmail", b.getUser().getEmail());
////        } else {
////            dto.put("userId", null);
////            dto.put("userName", "Unknown");
////        }
////        return dto;
////    }
////
////    // CREATE
////    @PostMapping
////    public ResponseEntity<?> createBooking(@RequestBody BookingRequestDto request) {
////        try {
////            return ResponseEntity.ok(toDto(bookingService.createBooking(request)));
////        } catch (RuntimeException e) {
////            return ResponseEntity.status(409).body(e.getMessage());
////        }
////    }
////
////    // AVAILABILITY
////    @GetMapping("/availability")
////    public List<AvailabilitySlot> availability(
////            @RequestParam Long classroomId,
////            @RequestParam LocalDate date) {
////        return bookingService.getAvailability(classroomId, date);
////    }
////
////    // MY BOOKINGS
////    @GetMapping("/my")
////    public List<Map<String, Object>> myBookings(@RequestParam Long userId) {
////        List<Map<String, Object>> result = new ArrayList<>();
////        for (Booking b : bookingService.findByUserId(userId)) result.add(toDto(b));
////        return result;
////    }
////
////    // ALL BOOKINGS (admin)
////    @GetMapping("/all")
////    public List<Map<String, Object>> getAllBookings() {
////        List<Map<String, Object>> result = new ArrayList<>();
////        for (Booking b : bookingRepository.findAll()) result.add(toDto(b));
////        return result;
////    }
////
////    // CANCEL
////    @PutMapping("/{id}/cancel")
////    public ResponseEntity<?> cancel(@PathVariable Long id) {
////        try {
////            return ResponseEntity.ok(toDto(bookingService.cancelBooking(id)));
////        } catch (RuntimeException e) {
////            return ResponseEntity.status(404).body(e.getMessage());
////        }
////    }
////
////    // ✅ APPROVE (admin approves a PENDING booking)
////    @PutMapping("/{id}/approve")
////    public ResponseEntity<?> approve(@PathVariable Long id) {
////        try {
////            return ResponseEntity.ok(toDto(bookingService.approveBooking(id)));
////        } catch (RuntimeException e) {
////            return ResponseEntity.status(400).body(e.getMessage());
////        }
////    }
////}
//
//
//package com.tatamotors.classbooking.controller;
//
//import com.tatamotors.classbooking.dto.AvailabilitySlot;
//import com.tatamotors.classbooking.dto.BookingRequestDto;
//import com.tatamotors.classbooking.entity.Booking;
//import com.tatamotors.classbooking.repository.BookingRepository;
//import com.tatamotors.classbooking.service.BookingService;
//
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//import java.time.LocalDate;
//import java.util.*;
//
//@RestController
//@RequestMapping("/api/bookings")
//@CrossOrigin
//public class BookingController {
//
//    private final BookingService bookingService;
//    private final BookingRepository bookingRepository;
//
//    public BookingController(BookingService bookingService, BookingRepository bookingRepository) {
//        this.bookingService = bookingService;
//        this.bookingRepository = bookingRepository;
//    }
//
//    // Flat DTO helper
//    private Map<String, Object> toDto(Booking b) {
//        Map<String, Object> dto = new HashMap<>();
//        dto.put("id", b.getId());
//        dto.put("date", b.getBookingDate() != null ? b.getBookingDate().toString() : null);
//        dto.put("startTime", b.getStartTime() != null ? b.getStartTime().toString() : null);
//        dto.put("endTime", b.getEndTime() != null ? b.getEndTime().toString() : null);
//        dto.put("status", b.getStatus() != null ? b.getStatus().name() : null);
//        dto.put("totalCost", b.getTotalPrice());
//        dto.put("totalHours", b.getTotalHours());
//        if (b.getClassroom() != null) {
//            dto.put("classroomId", b.getClassroom().getId());
//            dto.put("classroomName", b.getClassroom().getName());
//        } else {
//            dto.put("classroomId", null);
//            dto.put("classroomName", "Unknown");
//        }
//        if (b.getUser() != null) {
//            dto.put("userId", b.getUser().getId());
//            dto.put("userName", b.getUser().getName());
//            dto.put("userEmail", b.getUser().getEmail());
//        } else {
//            dto.put("userId", null);
//            dto.put("userName", "Unknown");
//        }
//        return dto;
//    }
//
//    // CREATE
//    @PostMapping
//    public ResponseEntity<?> createBooking(@RequestBody BookingRequestDto request) {
//        try {
//            return ResponseEntity.ok(toDto(bookingService.createBooking(request)));
//        } catch (RuntimeException e) {
//            return ResponseEntity.status(409).body(e.getMessage());
//        }
//    }
//
//    // AVAILABILITY
//    @GetMapping("/availability")
//    public List<AvailabilitySlot> availability(
//            @RequestParam Long classroomId,
//            @RequestParam LocalDate date) {
//        return bookingService.getAvailability(classroomId, date);
//    }
//
//    // MY BOOKINGS
//    @GetMapping("/my")
//    public List<Map<String, Object>> myBookings(@RequestParam Long userId) {
//        List<Map<String, Object>> result = new ArrayList<>();
//        for (Booking b : bookingService.findByUserId(userId)) result.add(toDto(b));
//        return result;
//    }
//
//    // ALL BOOKINGS (admin)
//    @GetMapping("/all")
//    public List<Map<String, Object>> getAllBookings() {
//        List<Map<String, Object>> result = new ArrayList<>();
//        for (Booking b : bookingRepository.findAll()) result.add(toDto(b));
//        return result;
//    }
//
//    // CANCEL
//    @PutMapping("/{id}/cancel")
//    public ResponseEntity<?> cancel(@PathVariable Long id) {
//        try {
//            return ResponseEntity.ok(toDto(bookingService.cancelBooking(id)));
//        } catch (RuntimeException e) {
//            return ResponseEntity.status(404).body(e.getMessage());
//        }
//    }
//
//    // ✅ APPROVE (admin approves a PENDING booking)
//    @PutMapping("/{id}/approve")
//    public ResponseEntity<?> approve(@PathVariable Long id) {
//        try {
//            return ResponseEntity.ok(toDto(bookingService.approveBooking(id)));
//        } catch (RuntimeException e) {
//            return ResponseEntity.status(400).body(e.getMessage());
//        }
//    }
//}


package com.tatamotors.classbooking.controller;

import com.tatamotors.classbooking.dto.AvailabilitySlot;
import com.tatamotors.classbooking.dto.BookingRequestDto;
import com.tatamotors.classbooking.entity.Booking;
import com.tatamotors.classbooking.entity.BookingStatus;
import com.tatamotors.classbooking.repository.BookingRepository;
import com.tatamotors.classbooking.service.BookingService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.*;

@RestController
@RequestMapping("/api/bookings")
@CrossOrigin
public class BookingController {

    private final BookingService bookingService;
    private final BookingRepository bookingRepository;

    public BookingController(BookingService bookingService, BookingRepository bookingRepository) {
        this.bookingService = bookingService;
        this.bookingRepository = bookingRepository;
    }

    // Flat DTO — safe null checks everywhere
    private Map<String, Object> toDto(Booking b) {
        Map<String, Object> dto = new HashMap<>();
        try {
            dto.put("id", b.getId());
            dto.put("date", b.getBookingDate() != null ? b.getBookingDate().toString() : null);
            dto.put("startTime", b.getStartTime() != null ? b.getStartTime().toString() : null);
            dto.put("endTime", b.getEndTime() != null ? b.getEndTime().toString() : null);
            dto.put("status", b.getStatus() != null ? b.getStatus().name() : null);
            dto.put("totalCost", b.getTotalPrice());
            dto.put("totalHours", b.getTotalHours());

            if (b.getClassroom() != null) {
                dto.put("classroomId", b.getClassroom().getId());
                dto.put("classroomName", b.getClassroom().getName());
            } else {
                dto.put("classroomId", null);
                dto.put("classroomName", "Unknown");
            }

            if (b.getUser() != null) {
                dto.put("userId", b.getUser().getId());
                dto.put("userName", b.getUser().getName());
                dto.put("userEmail", b.getUser().getEmail());
            } else {
                dto.put("userId", null);
                dto.put("userName", "Unknown");
                dto.put("userEmail", "");
            }
        } catch (Exception e) {
            System.err.println("toDto error for booking " + b.getId() + ": " + e.getMessage());
        }
        return dto;
    }

    // CREATE
    @PostMapping
    public ResponseEntity<?> createBooking(@RequestBody BookingRequestDto request) {
        try {
            return ResponseEntity.ok(toDto(bookingService.createBooking(request)));
        } catch (RuntimeException e) {
            System.err.println("createBooking error: " + e.getMessage());
            return ResponseEntity.status(409).body(e.getMessage());
        }
    }

    // AVAILABILITY
    @GetMapping("/availability")
    public ResponseEntity<?> availability(
            @RequestParam Long classroomId,
            @RequestParam LocalDate date) {
        try {
            List<AvailabilitySlot> slots = bookingService.getAvailability(classroomId, date);
            return ResponseEntity.ok(slots);
        } catch (Exception e) {
            System.err.println("availability error: " + e.getMessage());
            return ResponseEntity.ok(new ArrayList<>());
        }
    }

    // MY BOOKINGS — always returns an array, never throws to client
    @GetMapping("/my")
    public ResponseEntity<?> myBookings(@RequestParam Long userId) {
        try {
            List<Booking> bookings = bookingService.findByUserId(userId);
            List<Map<String, Object>> result = new ArrayList<>();
            for (Booking b : bookings) {
                result.add(toDto(b));
            }
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            System.err.println("myBookings error for userId " + userId + ": " + e.getMessage());
            e.printStackTrace();
            // Return empty array instead of 400 so frontend doesn't crash
            return ResponseEntity.ok(new ArrayList<>());
        }
    }

    // ALL BOOKINGS (admin) — always returns an array
    @GetMapping("/all")
    public ResponseEntity<?> getAllBookings() {
        try {
            List<Booking> bookings = bookingRepository.findAll();
            List<Map<String, Object>> result = new ArrayList<>();
            for (Booking b : bookings) {
                result.add(toDto(b));
            }
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            System.err.println("getAllBookings error: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.ok(new ArrayList<>());
        }
    }

    // CANCEL
    @PutMapping("/{id}/cancel")
    public ResponseEntity<?> cancel(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(toDto(bookingService.cancelBooking(id)));
        } catch (RuntimeException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        }
    }

    // APPROVE (admin)
    @PutMapping("/{id}/approve")
    public ResponseEntity<?> approve(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(toDto(bookingService.approveBooking(id)));
        } catch (RuntimeException e) {
            return ResponseEntity.status(400).body(e.getMessage());
        }
    }
}