////////package com.tatamotors.classbooking.config;
////////
////////import org.springframework.context.annotation.Bean;
////////import org.springframework.context.annotation.Configuration;
////////import org.springframework.security.config.annotation.web.builders.HttpSecurity;
////////import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
////////import org.springframework.security.web.SecurityFilterChain;
////////import org.springframework.web.cors.CorsConfiguration;
////////import org.springframework.web.cors.CorsConfigurationSource;
////////import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
////////
////////import java.util.List;
////////
////////@Configuration
////////@EnableWebSecurity
////////public class SecurityConfig {
////////
////////    @Bean
////////    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
////////        http
////////            .cors(cors -> cors.configurationSource(corsConfigurationSource()))
////////            .csrf(csrf -> csrf.disable())
////////            .authorizeHttpRequests(auth -> auth
////////                .anyRequest().permitAll()  // Allow all requests for now
////////            );
////////        return http.build();
////////    }
////////
////////    @Bean
////////    public CorsConfigurationSource corsConfigurationSource() {
////////        CorsConfiguration config = new CorsConfiguration();
////////
////////        // Your React app runs on port 5173 (Vite) or 3000 (Create React App)
////////        config.setAllowedOrigins(List.of(
////////            "http://localhost:5173",
////////            "http://localhost:3000"
////////        ));
////////        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
////////        config.setAllowedHeaders(List.of("*"));
////////        config.setAllowCredentials(true);
////////
////////        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
////////        source.registerCorsConfiguration("/**", config);
////////        return source;
////////    }
////////}
//////
//////
//////
//////
////////package com.tatamotors.classbooking.config;
////////
////////import org.springframework.context.annotation.Bean;
////////import org.springframework.context.annotation.Configuration;
////////import org.springframework.security.config.annotation.web.builders.HttpSecurity;
////////import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
////////import org.springframework.security.web.SecurityFilterChain;
////////import org.springframework.web.cors.CorsConfiguration;
////////import org.springframework.web.cors.CorsConfigurationSource;
////////import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
////////
////////import java.util.Arrays;
////////import java.util.List;
////////
////////@Configuration
////////@EnableWebSecurity
////////public class SecurityConfig {
////////
////////    @Bean
////////    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
////////        http
////////            .cors(cors -> cors.configurationSource(corsConfigurationSource()))
////////            .csrf(csrf -> csrf.disable())
////////            .authorizeHttpRequests(auth -> auth
////////                .anyRequest().permitAll()
////////            );
////////
////////        return http.build();
////////    }
////////
////////    @Bean
////////    public CorsConfigurationSource corsConfigurationSource() {
////////        CorsConfiguration config = new CorsConfiguration();
////////
////////        config.setAllowedOriginPatterns(List.of("*"));
////////        config.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH"));
////////        config.setAllowedHeaders(List.of("*"));
////////        config.setAllowCredentials(true);
////////        config.setMaxAge(3600L);
////////
////////        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
////////        source.registerCorsConfiguration("/**", config);
////////        return source;
////////    }
////////}
//////
//////
//////package com.tatamotors.classbooking.config;
//////
//////import org.springframework.context.annotation.Bean;
//////import org.springframework.context.annotation.Configuration;
//////import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//////import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//////import org.springframework.security.web.SecurityFilterChain;
//////import org.springframework.web.cors.CorsConfiguration;
//////import org.springframework.web.cors.CorsConfigurationSource;
//////import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
//////
//////import java.util.Arrays;
//////import java.util.List;
//////
//////@Configuration
//////@EnableWebSecurity
//////public class SecurityConfig {
//////
//////    @Bean
//////    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
//////        http
//////            .cors(cors -> cors.configurationSource(corsConfigurationSource()))
//////            .csrf(csrf -> csrf.disable())
//////            .authorizeHttpRequests(auth -> auth
//////                .anyRequest().permitAll()
//////            );
//////        return http.build();
//////    }
//////
//////    @Bean
//////    public CorsConfigurationSource corsConfigurationSource() {
//////        CorsConfiguration config = new CorsConfiguration();
//////
//////        // ✅ FIXED: explicit origins required when allowCredentials = true
//////        config.setAllowedOrigins(List.of(
//////            "http://localhost:8080",
//////            "http://localhost:5173"
//////        ));
//////        config.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH"));
//////        config.setAllowedHeaders(List.of("*"));
//////        config.setAllowCredentials(true);
//////        config.setMaxAge(3600L);
//////
//////        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
//////        source.registerCorsConfiguration("/**", config);
//////        return source;
//////    }
//////}
////
////package com.tatamotors.classbooking.config;
////
////import org.springframework.context.annotation.Bean;
////import org.springframework.context.annotation.Configuration;
////import org.springframework.security.config.annotation.web.builders.HttpSecurity;
////import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
////import org.springframework.security.web.SecurityFilterChain;
////import org.springframework.web.cors.CorsConfiguration;
////import org.springframework.web.cors.CorsConfigurationSource;
////import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
////
////import java.util.Arrays;
////import java.util.List;
////
////@Configuration
////@EnableWebSecurity
////public class SecurityConfig {
////
////    @Bean
////    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
////        http
////            .cors(cors -> cors.configurationSource(corsConfigurationSource()))
////            .csrf(csrf -> csrf.disable())
////            .authorizeHttpRequests(auth -> auth
////                .anyRequest().permitAll()
////            );
////
////        return http.build();
////    }
////
////    @Bean
////    public CorsConfigurationSource corsConfigurationSource() {
////        CorsConfiguration config = new CorsConfiguration();
////
////        config.setAllowedOriginPatterns(List.of("*"));
////        config.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH"));
////        config.setAllowedHeaders(List.of("*"));
////        config.setAllowCredentials(true);
////        config.setMaxAge(3600L);
////
////        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
////        source.registerCorsConfiguration("/**", config);
////        return source;
////    }
////}
//
//
//package com.tatamotors.classbooking.config;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.web.SecurityFilterChain;
//import org.springframework.web.cors.CorsConfiguration;
//import org.springframework.web.cors.CorsConfigurationSource;
//import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
//
//import java.util.Arrays;
//import java.util.List;
//
//@Configuration
//@EnableWebSecurity
//public class SecurityConfig {
//
//    @Bean
//    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
//        http
//            .cors(cors -> cors.configurationSource(corsConfigurationSource()))
//            .csrf(csrf -> csrf.disable())
//            .authorizeHttpRequests(auth -> auth
//                .anyRequest().permitAll()
//            );
//        return http.build();
//    }
//
//    @Bean
//    public CorsConfigurationSource corsConfigurationSource() {
//        CorsConfiguration config = new CorsConfiguration();
//
//        // ✅ Explicitly allow your frontend origin
//        config.setAllowedOrigins(List.of(
//            "http://localhost:8080",
//            "http://localhost:5173"
//        ));
//        config.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH"));
//        config.setAllowedHeaders(List.of("*"));
//        config.setAllowCredentials(true);
//        config.setMaxAge(3600L);
//
//        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
//        source.registerCorsConfiguration("/**", config);
//        return source;
//    }
//}


package com.tatamotors.classbooking.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .cors(cors -> cors.configurationSource(corsConfigurationSource()))
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> auth
                .anyRequest().permitAll()
            );
        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOriginPatterns(List.of("*"));
        config.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH"));
        config.setAllowedHeaders(List.of("*"));
        config.setAllowCredentials(true);
        config.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }
}