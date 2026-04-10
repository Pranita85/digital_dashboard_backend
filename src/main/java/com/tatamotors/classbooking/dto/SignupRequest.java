////package com.tatamotors.classbooking.dto;
////
////public class SignupRequest {
////
////    private String name;
////    private String email;
////    private String password;
////
////    public SignupRequest() {}
////
////    public String getName() {
////        return name;
////    }
////
////    public void setName(String name) {
////        this.name = name;
////    }
////
////    public String getEmail() {
////        return email;
////    }
////    
////    public void setEmail(String email) {
////        this.email = email;
////    }
////    
////    public String getPassword() {
////        return password;
////    }
////    
////    public void setPassword(String password) {
////        this.password = password;
////    }
////}
//
//
//
//
//package com.tatamotors.classbooking.dto;
//
//public class SignupRequest {
//
//    private String name;
//    private String email;
//    private String password;
//    private String role; // "USER" or "ADMIN"
//
//    public SignupRequest() {}
//
//    public String getName() { return name; }
//    public void setName(String name) { this.name = name; }
//
//    public String getEmail() { return email; }
//    public void setEmail(String email) { this.email = email; }
//
//    public String getPassword() { return password; }
//    public void setPassword(String password) { this.password = password; }
//
//    public String getRole() { return role; }
//    public void setRole(String role) { this.role = role; }
//}
//
//


package com.tatamotors.classbooking.dto;

public class SignupRequest {

    private String name;
    private String email;
    private String password;
    private String role; // "USER" or "ADMIN"

    public SignupRequest() {}

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }
}