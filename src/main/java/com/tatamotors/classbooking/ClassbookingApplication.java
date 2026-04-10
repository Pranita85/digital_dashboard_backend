////package com.tatamotors.classbooking;
////
////import org.springframework.boot.SpringApplication;
////import org.springframework.boot.autoconfigure.SpringBootApplication;
////
////@SpringBootApplication
////public class ClassbookingApplication {
////
////	public static void main(String[] args) {
////		SpringApplication.run(ClassbookingApplication.class, args);
////	}
////
////}
//package com.tatamotors.classbooking;
//
//import org.springframework.boot.SpringApplication;
//import org.springframework.boot.autoconfigure.SpringBootApplication;
//import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//
//@SpringBootApplication(exclude = SecurityAutoConfiguration.class)
//public class ClassbookingApplication {
//
//    public static void main(String[] args) {
//        SpringApplication.run(ClassbookingApplication.class, args);
//        
////        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
////        System.out.println(encoder.encode("5757"));
//    }
//}


package com.tatamotors.classbooking;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ClassbookingApplication {

    public static void main(String[] args) {
        SpringApplication.run(ClassbookingApplication.class, args);
    }
}