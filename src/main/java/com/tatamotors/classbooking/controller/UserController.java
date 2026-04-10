////package com.tatamotors.classbooking.controller;
////
////import com.tatamotors.classbooking.entity.User;
////import com.tatamotors.classbooking.repository.UserRepository;
////import org.springframework.http.ResponseEntity;
////import org.springframework.web.bind.annotation.*;
////
////import java.util.List;
////
////@RestController
////@RequestMapping("/api/users")
////@CrossOrigin
////public class UserController {
////
////    private final UserRepository userRepository;
////
////    public UserController(UserRepository userRepository) {
////        this.userRepository = userRepository;
////    }
////
////    // GET all users (for Admin User Management page)
////    @GetMapping
////    public List<User> getAllUsers() {
////        return userRepository.findAll();
////    }
////
////    // POST add user
////    @PostMapping
////    public User addUser(@RequestBody User user) {
////        return userRepository.save(user);
////    }
////
////    // DELETE user
////    @DeleteMapping("/{id}")
////    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
////        userRepository.deleteById(id);
////        return ResponseEntity.noContent().build();
////    }
////}
//
//
//package com.tatamotors.classbooking.controller;
//
//import com.tatamotors.classbooking.entity.User;
//import com.tatamotors.classbooking.repository.UserRepository;
//import org.springframework.http.ResponseEntity;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//import java.util.Map;
//
//@RestController
//@RequestMapping("/api/users")
//@CrossOrigin
//public class UserController {
//
//    private final UserRepository userRepository;
//    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
//
//    public UserController(UserRepository userRepository) {
//        this.userRepository = userRepository;
//    }
//
//    // GET all users (admin)
//    @GetMapping
//    public List<User> getAllUsers() {
//        return userRepository.findAll();
//    }
//
//    // POST add user (admin)
//    @PostMapping
//    public User addUser(@RequestBody User user) {
//        return userRepository.save(user);
//    }
//
//    // DELETE user (admin)
//    @DeleteMapping("/{id}")
//    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
//        userRepository.deleteById(id);
//        return ResponseEntity.noContent().build();
//    }
//
//    // PUT update profile (name + email)
//    @PutMapping("/{id}/profile")
//    public ResponseEntity<?> updateProfile(
//            @PathVariable Long id,
//            @RequestBody Map<String, String> body) {
//
//        return userRepository.findById(id).map(user -> {
//            String name = body.get("name");
//            String email = body.get("email");
//
//            if (name != null && !name.isBlank()) user.setName(name);
//
//            if (email != null && !email.isBlank()) {
//                // Check if email is taken by another user
//                userRepository.findByEmail(email.trim().toLowerCase()).ifPresent(existing -> {
//                    if (!existing.getId().equals(id)) {
//                        throw new RuntimeException("Email already in use");
//                    }
//                });
//                user.setEmail(email.trim().toLowerCase());
//            }
//
//            return ResponseEntity.ok(userRepository.save(user));
//        }).orElse(ResponseEntity.notFound().build());
//    }
//
//    // PUT change password
//    @PutMapping("/{id}/password")
//    public ResponseEntity<?> changePassword(
//            @PathVariable Long id,
//            @RequestBody Map<String, String> body) {
//
//        String currentPassword = body.get("currentPassword");
//        String newPassword = body.get("newPassword");
//
//        if (currentPassword == null || newPassword == null) {
//            return ResponseEntity.badRequest().body("Both currentPassword and newPassword are required");
//        }
//
//        return userRepository.findById(id).map(user -> {
//            String dbPassword = user.getPassword();
//
//            // Check current password (handles both plain text and BCrypt)
//            boolean matches;
//            if (dbPassword.startsWith("$2a$") || dbPassword.startsWith("$2b$")) {
//                matches = encoder.matches(currentPassword, dbPassword);
//            } else {
//                matches = dbPassword.equals(currentPassword);
//            }
//
//            if (!matches) {
//                return ResponseEntity.badRequest().body("Current password is incorrect");
//            }
//
//            // Save new password as BCrypt
//            user.setPassword(encoder.encode(newPassword));
//            userRepository.save(user);
//            return ResponseEntity.ok("Password changed successfully");
//        }).orElse(ResponseEntity.notFound().build());
//    }
//}

package com.tatamotors.classbooking.controller;

import com.tatamotors.classbooking.entity.User;
import com.tatamotors.classbooking.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
@CrossOrigin
public class UserController {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // GET all users (admin)
    @GetMapping
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    // POST add user (admin)
    @PostMapping
    public User addUser(@RequestBody User user) {
        return userRepository.save(user);
    }

    // DELETE user (admin)
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    // PUT update profile (name + email)
    @PutMapping("/{id}/profile")
    public ResponseEntity<?> updateProfile(
            @PathVariable Long id,
            @RequestBody Map<String, String> body) {

        return userRepository.findById(id).map(user -> {
            String name = body.get("name");
            String email = body.get("email");

            if (name != null && !name.isBlank()) user.setName(name);

            if (email != null && !email.isBlank()) {
                // Check if email is taken by another user
                userRepository.findByEmail(email.trim().toLowerCase()).ifPresent(existing -> {
                    if (!existing.getId().equals(id)) {
                        throw new RuntimeException("Email already in use");
                    }
                });
                user.setEmail(email.trim().toLowerCase());
            }

            return ResponseEntity.ok(userRepository.save(user));
        }).orElse(ResponseEntity.notFound().build());
    }

    // PUT change password
    @PutMapping("/{id}/password")
    public ResponseEntity<?> changePassword(
            @PathVariable Long id,
            @RequestBody Map<String, String> body) {

        String currentPassword = body.get("currentPassword");
        String newPassword = body.get("newPassword");

        if (currentPassword == null || newPassword == null) {
            return ResponseEntity.badRequest().body("Both currentPassword and newPassword are required");
        }

        return userRepository.findById(id).map(user -> {
            String dbPassword = user.getPassword();

            // Check current password (handles both plain text and BCrypt)
            boolean matches;
            if (dbPassword.startsWith("$2a$") || dbPassword.startsWith("$2b$")) {
                matches = encoder.matches(currentPassword, dbPassword);
            } else {
                matches = dbPassword.equals(currentPassword);
            }

            if (!matches) {
                return ResponseEntity.badRequest().body("Current password is incorrect");
            }

            // Save new password as BCrypt
            user.setPassword(encoder.encode(newPassword));
            userRepository.save(user);
            return ResponseEntity.ok("Password changed successfully");
        }).orElse(ResponseEntity.notFound().build());
    }
}