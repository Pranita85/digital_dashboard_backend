//package com.tatamotors.classbooking.dto;
//
//public class LoginResponse {
//
//    private String token;
//    private String email;
//    private String role;
//    private Long userId;
//
//    // ✅ FIXED: Added Long userId to the constructor arguments
//    public LoginResponse(String token, String email, String role, Long userId) {
//        this.token = token;
//        this.email = email;
//        this.role = role;
//        this.userId = userId; // ✅ FIXED: Initialize the userId field
//    }
//
//    public String getToken() {
//        return token;
//    }
//
//    public String getEmail() {
//        return email;
//    }
//
//    public String getRole() {
//        return role;
//    }
//
//    public Long getUserId() {
//        return userId;
//    }
//
//    public void setUserId(Long userId) {
//        this.userId = userId;
//    }
//}


package com.tatamotors.classbooking.dto;

public class LoginResponse {

    private String token;
    private String name;
    private String email;
    private String role;
    private Long userId;

    public LoginResponse(String token, String name, String email, String role, Long userId) {
        this.token = token;
        this.name = name;
        this.email = email;
        this.role = role;
        this.userId = userId;
    }

    public String getToken() { return token; }
    public String getName() { return name; }
    public String getEmail() { return email; }
    public String getRole() { return role; }
    public Long getUserId() { return userId; }

    public void setToken(String token) { this.token = token; }
    public void setName(String name) { this.name = name; }
    public void setEmail(String email) { this.email = email; }
    public void setRole(String role) { this.role = role; }
    public void setUserId(Long userId) { this.userId = userId; }
}