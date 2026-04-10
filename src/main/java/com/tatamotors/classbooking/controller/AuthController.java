//package com.tatamotors.classbooking.controller;
//
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//import com.tatamotors.classbooking.dto.LoginRequest;
//import com.tatamotors.classbooking.dto.LoginResponse;
//import com.tatamotors.classbooking.dto.SignupRequest;
//import com.tatamotors.classbooking.entity.User;
//import com.tatamotors.classbooking.service.UserService;
//
//@RestController
//@RequestMapping("/auth")
//@CrossOrigin
//public class AuthController {
//
//    private final UserService userService;
//
//    public AuthController(UserService userService) {
//        this.userService = userService;
//    }
//
//    @PostMapping("/signup")
//    public ResponseEntity<User> signup(@RequestBody SignupRequest request) {
//        return ResponseEntity.ok(userService.signup(request));
//    }
//
//    @PostMapping("/login")
//    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {
//        return ResponseEntity.ok(userService.login(request));
//    }
//}






//package com.tatamotors.classbooking.controller;
//
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//import com.tatamotors.classbooking.dto.LoginRequest;
//import com.tatamotors.classbooking.dto.LoginResponse;
//import com.tatamotors.classbooking.dto.SignupRequest;
//import com.tatamotors.classbooking.entity.User;
//import com.tatamotors.classbooking.service.UserService;
//
//@RestController
//@RequestMapping("/auth")
//@CrossOrigin
//public class AuthController {
//
//    private final UserService userService;
//
//    public AuthController(UserService userService) {
//        this.userService = userService;
//    }
//
//    @PostMapping("/signup")
//    public ResponseEntity<User> signup(@RequestBody SignupRequest request) {
//        return ResponseEntity.ok(userService.signup(request));
//    }
////
////    @PostMapping("/login")
////    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {
////        return ResponseEntity.ok(userService.login(request));
////    }
//    
//    @PostMapping("/login")
//    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {
//
//        System.out.println("EmployeeId: " + request.getEmployeeId());
//        System.out.println("Password: " + request.getPassword());
//
//        return ResponseEntity.ok(userService.login(request));
//    }
//
//}



package com.tatamotors.classbooking.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.tatamotors.classbooking.dto.LoginRequest;
import com.tatamotors.classbooking.dto.LoginResponse;
import com.tatamotors.classbooking.dto.SignupRequest;
import com.tatamotors.classbooking.entity.User;
import com.tatamotors.classbooking.service.UserService;

@RestController
@RequestMapping("/auth")
@CrossOrigin
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/signup")
    public ResponseEntity<User> signup(@RequestBody SignupRequest request) {
        return ResponseEntity.ok(userService.signup(request));
    }
    
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {

        // FIX: Use getEmail() instead of getEmployeeId() to match your DTO
        System.out.println("Email: " + request.getEmail()); 
        System.out.println("Password: " + request.getPassword());

        return ResponseEntity.ok(userService.login(request));
    }
}















