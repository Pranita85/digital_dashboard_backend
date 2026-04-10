//package com.tatamotors.classbooking.security;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.provisioning.InMemoryUserDetailsManager;
//
//@Configuration
//public class CustomUserDetailsService {
//
//    @Bean
//    public UserDetailsService userDetailsService() {
//        return new InMemoryUserDetailsManager(); // empty for now
//    }
//}


package com.tatamotors.classbooking.security;

import org.springframework.stereotype.Component;

// This class is intentionally left minimal.
// Authentication is handled directly in UserServiceImpl via email/password lookup.
@Component
public class CustomUserDetailsService {
    // No beans here — removing the conflicting InMemoryUserDetailsManager
    // that was overriding Spring Security and causing 400 errors on login.
}