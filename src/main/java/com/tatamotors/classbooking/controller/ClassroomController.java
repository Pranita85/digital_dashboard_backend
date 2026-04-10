//////package com.tatamotors.classbooking.controller;
//////
//////import org.springframework.web.bind.annotation.*;
//////import org.springframework.web.bind.annotation.CrossOrigin;
//////import com.tatamotors.classbooking.entity.Classroom;
//////import com.tatamotors.classbooking.service.ClassroomService;
//////
//////import java.util.List;
//////
//////@RestController
//////@RequestMapping("/api/classrooms")
//////@CrossOrigin 
//////public class ClassroomController {
//////
//////    private final ClassroomService classroomService;
//////
//////    public ClassroomController(ClassroomService classroomService) {
//////        this.classroomService = classroomService;
//////    }
//////
//////    @PostMapping
//////    public Classroom add(@RequestBody Classroom classroom) {
//////        return classroomService.addClassroom(classroom);
//////    }
//////
//////    @GetMapping
//////    public List<Classroom> getAll() {
//////        return classroomService.getAllClassrooms();
//////    }
//////
//////    @GetMapping("/filter")
//////    public List<Classroom> filter(
//////            @RequestParam boolean ac,
//////            @RequestParam int capacity
//////    ) {
//////        return classroomService.filterClassrooms(ac, capacity);
//////    }
//////    
//////    // Add this method inside ClassroomController.java
//////    @PutMapping("/{id}")
//////    public Classroom updateClassroom(@PathVariable Long id, @RequestBody Classroom classroomDetails) {
//////        return classroomRepository.findById(id).map(classroom -> {
//////            classroom.setPricePerHour(classroomDetails.getPricePerHour());
//////            classroom.setPeakPricePerHour(classroomDetails.getPeakPricePerHour());
//////            // You can update other fields here if needed
//////            return classroomRepository.save(classroom);
//////        }).orElseThrow(() -> new RuntimeException("Classroom not found"));
//////    }
//////}
////
////
////
//////package com.tatamotors.classbooking.controller;
//////
//////import org.springframework.http.ResponseEntity;
//////import org.springframework.web.bind.annotation.*;
//////import org.springframework.web.bind.annotation.CrossOrigin;
//////import com.tatamotors.classbooking.entity.Classroom;
//////import com.tatamotors.classbooking.repository.ClassroomRepository; // 1. IMPORT REPOSITORY
//////import com.tatamotors.classbooking.service.ClassroomService;
//////
//////import java.util.List;
//////import java.util.Optional; // 2. IMPORT OPTIONAL
//////
//////@RestController
//////@RequestMapping("/api/classrooms")
//////@CrossOrigin 
//////public class ClassroomController {
//////
//////    private final ClassroomService classroomService;
//////    private final ClassroomRepository classroomRepository; // 3. INJECT REPOSITORY
//////
//////    // 4. UPDATE CONSTRUCTOR
//////    public ClassroomController(ClassroomService classroomService, ClassroomRepository classroomRepository) {
//////        this.classroomService = classroomService;
//////        this.classroomRepository = classroomRepository;
//////    }
//////
//////    @PostMapping
//////    public Classroom add(@RequestBody Classroom classroom) {
//////        return classroomService.addClassroom(classroom);
//////    }
//////
//////    @GetMapping
//////    public List<Classroom> getAll() {
//////        return classroomService.getAllClassrooms();
//////    }
//////
//////    @GetMapping("/filter")
//////    public List<Classroom> filter(
//////            @RequestParam boolean ac,
//////            @RequestParam int capacity
//////    ) {
//////        return classroomService.filterClassrooms(ac, capacity);
//////    }
//////    
//////    // UPDATE CLASSROOM (Fixed Logic)
//////    @PutMapping("/{id}")
//////    public ResponseEntity<Classroom> updateClassroom(@PathVariable Long id, @RequestBody Classroom classroomDetails) {
//////        Optional<Classroom> optionalClassroom = classroomRepository.findById(id);
//////        
//////        if (optionalClassroom.isPresent()) {
//////            Classroom classroom = optionalClassroom.get();
//////            classroom.setPricePerHour(classroomDetails.getPricePerHour());
//////            classroom.setPeakPricePerHour(classroomDetails.getPeakPricePerHour());
//////            // You can update other fields here if needed
//////            return ResponseEntity.ok(classroomRepository.save(classroom));
//////        } else {
//////            return ResponseEntity.notFound().build();
//////        }
//////    }
//////}
////
////
////package com.tatamotors.classbooking.controller;
////
////import org.springframework.http.ResponseEntity;
////import org.springframework.web.bind.annotation.*;
////import com.tatamotors.classbooking.entity.Classroom;
////import com.tatamotors.classbooking.entity.ClassroomStatus;
////import com.tatamotors.classbooking.repository.ClassroomRepository;
////import com.tatamotors.classbooking.service.ClassroomService;
////
////import java.util.List;
////import java.util.Map;
////import java.util.Optional;
////
////@RestController
////@RequestMapping("/api/classrooms")
////@CrossOrigin
////public class ClassroomController {
////
////    private final ClassroomService classroomService;
////    private final ClassroomRepository classroomRepository;
////
////    public ClassroomController(ClassroomService classroomService, ClassroomRepository classroomRepository) {
////        this.classroomService = classroomService;
////        this.classroomRepository = classroomRepository;
////    }
////
////    // GET all classrooms
////    @GetMapping
////    public List<Classroom> getAll() {
////        return classroomService.getAllClassrooms();
////    }
////
////    // GET filter
////    @GetMapping("/filter")
////    public List<Classroom> filter(@RequestParam boolean ac, @RequestParam int capacity) {
////        return classroomService.filterClassrooms(ac, capacity);
////    }
////
////    // POST add classroom
////    @PostMapping
////    public ResponseEntity<Classroom> add(@RequestBody Map<String, Object> body) {
////        Classroom classroom = new Classroom();
////        classroom.setName((String) body.get("name"));
////        classroom.setCapacity(body.get("capacity") != null ? ((Number) body.get("capacity")).intValue() : 0);
////        classroom.setAc(body.get("ac") != null && (Boolean) body.get("ac"));
////        classroom.setPricePerHour(body.get("pricePerHour") != null ? ((Number) body.get("pricePerHour")).doubleValue() : 0);
////        classroom.setPeakPricePerHour(body.get("peakPricePerHour") != null ? ((Number) body.get("peakPricePerHour")).doubleValue() : 0);
////        classroom.setStatus(ClassroomStatus.ACTIVE);
////        return ResponseEntity.ok(classroomRepository.save(classroom));
////    }
////
////    // PUT update classroom
////    @PutMapping("/{id}")
////    public ResponseEntity<Classroom> update(@PathVariable Long id, @RequestBody Map<String, Object> body) {
////        Optional<Classroom> opt = classroomRepository.findById(id);
////        if (opt.isEmpty()) return ResponseEntity.notFound().build();
////
////        Classroom classroom = opt.get();
////        if (body.get("name") != null) classroom.setName((String) body.get("name"));
////        if (body.get("capacity") != null) classroom.setCapacity(((Number) body.get("capacity")).intValue());
////        if (body.get("ac") != null) classroom.setAc((Boolean) body.get("ac"));
////        if (body.get("pricePerHour") != null) classroom.setPricePerHour(((Number) body.get("pricePerHour")).doubleValue());
////        if (body.get("peakPricePerHour") != null) classroom.setPeakPricePerHour(((Number) body.get("peakPricePerHour")).doubleValue());
////
////        return ResponseEntity.ok(classroomRepository.save(classroom));
////    }
////
////    // DELETE classroom
////    @DeleteMapping("/{id}")
////    public ResponseEntity<Void> delete(@PathVariable Long id) {
////        if (!classroomRepository.existsById(id)) return ResponseEntity.notFound().build();
////        classroomRepository.deleteById(id);
////        return ResponseEntity.noContent().build();
////    }
////}
//
//
//package com.tatamotors.classbooking.controller;
//
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//import com.tatamotors.classbooking.entity.Classroom;
//import com.tatamotors.classbooking.entity.ClassroomStatus;
//import com.tatamotors.classbooking.repository.ClassroomRepository;
//import com.tatamotors.classbooking.service.ClassroomService;
//
//import java.util.List;
//import java.util.Map;
//import java.util.Optional;
//
//@RestController
//@RequestMapping("/api/classrooms")
//@CrossOrigin
//public class ClassroomController {
//
//    private final ClassroomService classroomService;
//    private final ClassroomRepository classroomRepository;
//
//    public ClassroomController(ClassroomService classroomService, ClassroomRepository classroomRepository) {
//        this.classroomService = classroomService;
//        this.classroomRepository = classroomRepository;
//    }
//
//    @GetMapping
//    public List<Classroom> getAll() {
//        return classroomService.getAllClassrooms();
//    }
//
//    @GetMapping("/filter")
//    public List<Classroom> filter(@RequestParam boolean ac, @RequestParam int capacity) {
//        return classroomService.filterClassrooms(ac, capacity);
//    }
//
//    @PostMapping
//    public ResponseEntity<?> add(@RequestBody Map<String, Object> body) {
//        try {
//            Classroom classroom = new Classroom();
//            classroom.setName((String) body.get("name"));
//            classroom.setCapacity(body.get("capacity") != null ? ((Number) body.get("capacity")).intValue() : 0);
//            classroom.setAc(body.get("ac") != null && (Boolean) body.get("ac"));
//            classroom.setPricePerHour(body.get("pricePerHour") != null ? ((Number) body.get("pricePerHour")).doubleValue() : 0);
//            classroom.setPeakPricePerHour(body.get("peakPricePerHour") != null ? ((Number) body.get("peakPricePerHour")).doubleValue() : 0);
//            classroom.setStatus(ClassroomStatus.ACTIVE);
//            return ResponseEntity.ok(classroomRepository.save(classroom));
//        } catch (Exception e) {
//            return ResponseEntity.badRequest().body("Could not add lab: " + e.getMessage());
//        }
//    }
//
//    @PutMapping("/{id}")
//    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody Map<String, Object> body) {
//        Optional<Classroom> opt = classroomRepository.findById(id);
//        if (opt.isEmpty()) return ResponseEntity.notFound().build();
//        try {
//            Classroom classroom = opt.get();
//            if (body.get("name") != null) classroom.setName((String) body.get("name"));
//            if (body.get("capacity") != null) classroom.setCapacity(((Number) body.get("capacity")).intValue());
//            if (body.get("ac") != null) classroom.setAc((Boolean) body.get("ac"));
//            if (body.get("pricePerHour") != null) classroom.setPricePerHour(((Number) body.get("pricePerHour")).doubleValue());
//            if (body.get("peakPricePerHour") != null) classroom.setPeakPricePerHour(((Number) body.get("peakPricePerHour")).doubleValue());
//            return ResponseEntity.ok(classroomRepository.save(classroom));
//        } catch (Exception e) {
//            return ResponseEntity.badRequest().body("Could not update lab: " + e.getMessage());
//        }
//    }
//
//    @DeleteMapping("/{id}")
//    public ResponseEntity<?> delete(@PathVariable Long id) {
//        if (!classroomRepository.existsById(id)) {
//            return ResponseEntity.notFound().build();
//        }
//        try {
//            classroomRepository.deleteById(id);
//            return ResponseEntity.ok("Lab deleted successfully");
//        } catch (Exception e) {
//            // Foreign key constraint — lab has bookings
//            return ResponseEntity.status(409).body(
//                "Cannot delete this lab because it has existing bookings. " +
//                "Cancel all bookings for this lab first, then delete it."
//            );
//        }
//    }
//}


package com.tatamotors.classbooking.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.tatamotors.classbooking.entity.Classroom;
import com.tatamotors.classbooking.entity.ClassroomStatus;
import com.tatamotors.classbooking.repository.BookingRepository;
import com.tatamotors.classbooking.repository.ClassroomRepository;
import com.tatamotors.classbooking.service.ClassroomService;

import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/classrooms")
@CrossOrigin
public class ClassroomController {

    private final ClassroomService classroomService;
    private final ClassroomRepository classroomRepository;
    private final BookingRepository bookingRepository;

    public ClassroomController(ClassroomService classroomService,
                               ClassroomRepository classroomRepository,
                               BookingRepository bookingRepository) {
        this.classroomService = classroomService;
        this.classroomRepository = classroomRepository;
        this.bookingRepository = bookingRepository;
    }

    @GetMapping
    public List<Classroom> getAll() {
        return classroomService.getAllClassrooms();
    }

    @GetMapping("/filter")
    public List<Classroom> filter(@RequestParam boolean ac, @RequestParam int capacity) {
        return classroomService.filterClassrooms(ac, capacity);
    }

    @PostMapping
    public ResponseEntity<?> add(@RequestBody Map<String, Object> body) {
        try {
            Classroom classroom = new Classroom();
            classroom.setName((String) body.get("name"));
            classroom.setCapacity(body.get("capacity") != null ? ((Number) body.get("capacity")).intValue() : 0);
            classroom.setAc(body.get("ac") != null && (Boolean) body.get("ac"));
            classroom.setPricePerHour(body.get("pricePerHour") != null ? ((Number) body.get("pricePerHour")).doubleValue() : 0);
            classroom.setPeakPricePerHour(body.get("peakPricePerHour") != null ? ((Number) body.get("peakPricePerHour")).doubleValue() : 0);
            classroom.setStatus(ClassroomStatus.ACTIVE);
            return ResponseEntity.ok(classroomRepository.save(classroom));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Could not add lab: " + e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody Map<String, Object> body) {
        Optional<Classroom> opt = classroomRepository.findById(id);
        if (opt.isEmpty()) return ResponseEntity.notFound().build();
        try {
            Classroom classroom = opt.get();
            if (body.get("name") != null) classroom.setName((String) body.get("name"));
            if (body.get("capacity") != null) classroom.setCapacity(((Number) body.get("capacity")).intValue());
            if (body.get("ac") != null) classroom.setAc((Boolean) body.get("ac"));
            if (body.get("pricePerHour") != null) classroom.setPricePerHour(((Number) body.get("pricePerHour")).doubleValue());
            if (body.get("peakPricePerHour") != null) classroom.setPeakPricePerHour(((Number) body.get("peakPricePerHour")).doubleValue());
            return ResponseEntity.ok(classroomRepository.save(classroom));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Could not update: " + e.getMessage());
        }
    }

    // DELETE — deletes all bookings for this lab first, then deletes the lab
    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity<?> delete(@PathVariable Long id) {
        if (!classroomRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        try {
            // Delete all bookings for this classroom first to remove FK constraint
            bookingRepository.deleteByClassroomId(id);
            // Now safe to delete the classroom
            classroomRepository.deleteById(id);
            return ResponseEntity.ok("Lab deleted successfully");
        } catch (Exception e) {
            System.err.println("Delete lab error: " + e.getMessage());
            return ResponseEntity.status(500).body("Could not delete lab: " + e.getMessage());
        }
    }
}