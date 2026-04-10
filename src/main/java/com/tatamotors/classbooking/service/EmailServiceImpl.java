//////package com.tatamotors.classbooking.service;
//////
//////import org.springframework.mail.SimpleMailMessage;
//////import org.springframework.mail.javamail.JavaMailSender;
//////import org.springframework.stereotype.Service;
//////
//////@Service
//////public class EmailServiceImpl implements EmailService {
//////
//////    private final JavaMailSender mailSender;
//////
//////    public EmailServiceImpl(JavaMailSender mailSender) {
//////        this.mailSender = mailSender;
//////    }
//////
//////    @Override
//////    public void sendBookingConfirmation(
//////            String to,
//////            String classroomName,
//////            String bookingDate,
//////            String timeSlot,
//////            double price
//////    ) {
//////        SimpleMailMessage message = new SimpleMailMessage();
//////        message.setTo(to);
//////        message.setSubject("Classroom Booking Confirmation");
//////        message.setText(
//////                "Your classroom booking is confirmed.\n\n" +
//////                "Classroom: " + classroomName + "\n" +
//////                "Date: " + bookingDate + "\n" +
//////                "Time: " + timeSlot + "\n" +
//////                "Total Price: ₹" + price + "\n\n" +
//////                "Thank you,\nTata Motors Training Center"
//////        );
//////
//////        mailSender.send(message);
//////    }
//////}
////
////
//////package com.tatamotors.classbooking.service;
//////
//////import org.springframework.mail.SimpleMailMessage;
//////import org.springframework.mail.javamail.JavaMailSender;
//////import org.springframework.stereotype.Service;
//////
//////@Service
//////public class EmailServiceImpl implements EmailService {
//////
//////    private final JavaMailSender mailSender;
//////
//////    public EmailServiceImpl(JavaMailSender mailSender) {
//////        this.mailSender = mailSender;
//////    }
//////
//////    @Override
//////    public void sendBookingConfirmation(
//////            String to,
//////            String classroomName,
//////            String bookingDate,
//////            String timeSlot,
//////            double price) {
//////
//////        SimpleMailMessage message = new SimpleMailMessage();
//////        message.setTo(to);
//////        message.setSubject("✅ Booking Confirmed - " + classroomName);
//////        message.setText(
//////                "Dear User,\n\n" +
//////                "Your classroom booking has been confirmed!\n\n" +
//////                "━━━━━━━━━━━━━━━━━━━━━━━━━━━━\n" +
//////                "  Classroom : " + classroomName + "\n" +
//////                "  Date      : " + bookingDate + "\n" +
//////                "  Time      : " + timeSlot + "\n" +
//////                "  Total Cost: ₹" + price + "\n" +
//////                "━━━━━━━━━━━━━━━━━━━━━━━━━━━━\n\n" +
//////                "Please arrive on time.\n\n" +
//////                "Thank you,\n" +
//////                "Tata Motors Training Center"
//////        );
//////        mailSender.send(message);
//////    }
//////
//////    @Override
//////    public void sendCancellationEmail(
//////            String to,
//////            String classroomName,
//////            String bookingDate,
//////            String timeSlot) {
//////
//////        SimpleMailMessage message = new SimpleMailMessage();
//////        message.setTo(to);
//////        message.setSubject("❌ Booking Cancelled - " + classroomName);
//////        message.setText(
//////                "Dear User,\n\n" +
//////                "Your classroom booking has been cancelled.\n\n" +
//////                "━━━━━━━━━━━━━━━━━━━━━━━━━━━━\n" +
//////                "  Classroom : " + classroomName + "\n" +
//////                "  Date      : " + bookingDate + "\n" +
//////                "  Time      : " + timeSlot + "\n" +
//////                "━━━━━━━━━━━━━━━━━━━━━━━━━━━━\n\n" +
//////                "If you did not request this cancellation, please contact admin.\n\n" +
//////                "Thank you,\n" +
//////                "Tata Motors Training Center"
//////        );
//////        mailSender.send(message);
//////    }
//////}
////
////
////
////package com.tatamotors.classbooking.service;
////
////import org.springframework.mail.SimpleMailMessage;
////import org.springframework.mail.javamail.JavaMailSender;
////import org.springframework.stereotype.Service;
////
////@Service
////public class EmailServiceImpl implements EmailService {
////
////    private final JavaMailSender mailSender;
////
////    public EmailServiceImpl(JavaMailSender mailSender) {
////        this.mailSender = mailSender;
////    }
////
////    @Override
////    public void sendBookingConfirmation(String to, String classroomName,
////            String bookingDate, String timeSlot, double price) {
////        SimpleMailMessage msg = new SimpleMailMessage();
////        msg.setTo(to);
////        msg.setSubject("✅ Booking Confirmed - " + classroomName);
////        msg.setText(
////            "Dear User,\n\n" +
////            "Your classroom booking has been CONFIRMED!\n\n" +
////            "━━━━━━━━━━━━━━━━━━━━━━━━━━━━\n" +
////            "  Classroom : " + classroomName + "\n" +
////            "  Date      : " + bookingDate + "\n" +
////            "  Time      : " + timeSlot + "\n" +
////            "  Total Cost: ₹" + price + "\n" +
////            "━━━━━━━━━━━━━━━━━━━━━━━━━━━━\n\n" +
////            "Please arrive on time.\n\n" +
////            "Thank you,\nTata Motors Training Center"
////        );
////        mailSender.send(msg);
////    }
////
////    @Override
////    public void sendCancellationEmail(String to, String classroomName,
////            String bookingDate, String timeSlot) {
////        SimpleMailMessage msg = new SimpleMailMessage();
////        msg.setTo(to);
////        msg.setSubject("❌ Booking Cancelled - " + classroomName);
////        msg.setText(
////            "Dear User,\n\n" +
////            "Your classroom booking has been CANCELLED.\n\n" +
////            "━━━━━━━━━━━━━━━━━━━━━━━━━━━━\n" +
////            "  Classroom : " + classroomName + "\n" +
////            "  Date      : " + bookingDate + "\n" +
////            "  Time      : " + timeSlot + "\n" +
////            "━━━━━━━━━━━━━━━━━━━━━━━━━━━━\n\n" +
////            "If you did not request this, please contact admin.\n\n" +
////            "Thank you,\nTata Motors Training Center"
////        );
////        mailSender.send(msg);
////    }
////
////    @Override
////    public void sendPendingApprovalEmail(String to, String classroomName,
////            String bookingDate, String timeSlot, double price) {
////        SimpleMailMessage msg = new SimpleMailMessage();
////        msg.setTo(to);
////        msg.setSubject("⏳ Booking Pending Approval - " + classroomName);
////        msg.setText(
////            "Dear User,\n\n" +
////            "Your booking request has been received and is PENDING admin approval.\n\n" +
////            "━━━━━━━━━━━━━━━━━━━━━━━━━━━━\n" +
////            "  Classroom : " + classroomName + "\n" +
////            "  Date      : " + bookingDate + "\n" +
////            "  Time      : " + timeSlot + "\n" +
////            "  Total Cost: ₹" + price + "\n" +
////            "━━━━━━━━━━━━━━━━━━━━━━━━━━━━\n\n" +
////            "You will receive another email once admin approves your booking.\n\n" +
////            "Thank you,\nTata Motors Training Center"
////        );
////        mailSender.send(msg);
////    }
////}
//
//
//package com.tatamotors.classbooking.service;
//
//import org.springframework.mail.SimpleMailMessage;
//import org.springframework.mail.javamail.JavaMailSender;
//import org.springframework.stereotype.Service;
//
//@Service
//public class EmailServiceImpl implements EmailService {
//
//    private final JavaMailSender mailSender;
//
//    public EmailServiceImpl(JavaMailSender mailSender) {
//        this.mailSender = mailSender;
//    }
//
//    @Override
//    public void sendBookingConfirmation(String to, String classroomName,
//            String bookingDate, String timeSlot, double price) {
//        SimpleMailMessage msg = new SimpleMailMessage();
//        msg.setTo(to);
//        msg.setSubject("✅ Booking Confirmed - " + classroomName);
//        msg.setText(
//            "Dear User,\n\n" +
//            "Your classroom booking has been CONFIRMED!\n\n" +
//            "━━━━━━━━━━━━━━━━━━━━━━━━━━━━\n" +
//            "  Classroom : " + classroomName + "\n" +
//            "  Date      : " + bookingDate + "\n" +
//            "  Time      : " + timeSlot + "\n" +
//            "  Total Cost: ₹" + price + "\n" +
//            "━━━━━━━━━━━━━━━━━━━━━━━━━━━━\n\n" +
//            "Please arrive on time.\n\n" +
//            "Thank you,\nTata Motors Training Center"
//        );
//        mailSender.send(msg);
//    }
//
//    @Override
//    public void sendCancellationEmail(String to, String classroomName,
//            String bookingDate, String timeSlot) {
//        SimpleMailMessage msg = new SimpleMailMessage();
//        msg.setTo(to);
//        msg.setSubject("❌ Booking Cancelled - " + classroomName);
//        msg.setText(
//            "Dear User,\n\n" +
//            "Your classroom booking has been CANCELLED.\n\n" +
//            "━━━━━━━━━━━━━━━━━━━━━━━━━━━━\n" +
//            "  Classroom : " + classroomName + "\n" +
//            "  Date      : " + bookingDate + "\n" +
//            "  Time      : " + timeSlot + "\n" +
//            "━━━━━━━━━━━━━━━━━━━━━━━━━━━━\n\n" +
//            "If you did not request this, please contact admin.\n\n" +
//            "Thank you,\nTata Motors Training Center"
//        );
//        mailSender.send(msg);
//    }
//
//    @Override
//    public void sendPendingApprovalEmail(String to, String classroomName,
//            String bookingDate, String timeSlot, double price) {
//        SimpleMailMessage msg = new SimpleMailMessage();
//        msg.setTo(to);
//        msg.setSubject("⏳ Booking Pending Approval - " + classroomName);
//        msg.setText(
//            "Dear User,\n\n" +
//            "Your booking request has been received and is PENDING admin approval.\n\n" +
//            "━━━━━━━━━━━━━━━━━━━━━━━━━━━━\n" +
//            "  Classroom : " + classroomName + "\n" +
//            "  Date      : " + bookingDate + "\n" +
//            "  Time      : " + timeSlot + "\n" +
//            "  Total Cost: ₹" + price + "\n" +
//            "━━━━━━━━━━━━━━━━━━━━━━━━━━━━\n\n" +
//            "You will receive another email once admin approves your booking.\n\n" +
//            "Thank you,\nTata Motors Training Center"
//        );
//        mailSender.send(msg);
//    }
//}


package com.tatamotors.classbooking.service;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender mailSender;

    public EmailServiceImpl(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @Override
    public void sendBookingConfirmation(String to, String classroomName,
            String bookingDate, String timeSlot, double price) {
        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setTo(to);
        msg.setSubject("✅ Booking Confirmed - " + classroomName);
        msg.setText(
            "Dear User,\n\n" +
            "Your classroom booking has been CONFIRMED!\n\n" +
            "━━━━━━━━━━━━━━━━━━━━━━━━━━━━\n" +
            "  Classroom : " + classroomName + "\n" +
            "  Date      : " + bookingDate + "\n" +
            "  Time      : " + timeSlot + "\n" +
            "  Total Cost: ₹" + price + "\n" +
            "━━━━━━━━━━━━━━━━━━━━━━━━━━━━\n\n" +
            "Please arrive on time.\n\n" +
            "Thank you,\nTata Motors Training Center"
        );
        mailSender.send(msg);
    }

    @Override
    public void sendCancellationEmail(String to, String classroomName,
            String bookingDate, String timeSlot) {
        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setTo(to);
        msg.setSubject("❌ Booking Cancelled - " + classroomName);
        msg.setText(
            "Dear User,\n\n" +
            "Your classroom booking has been CANCELLED.\n\n" +
            "━━━━━━━━━━━━━━━━━━━━━━━━━━━━\n" +
            "  Classroom : " + classroomName + "\n" +
            "  Date      : " + bookingDate + "\n" +
            "  Time      : " + timeSlot + "\n" +
            "━━━━━━━━━━━━━━━━━━━━━━━━━━━━\n\n" +
            "If you did not request this, please contact admin.\n\n" +
            "Thank you,\nTata Motors Training Center"
        );
        mailSender.send(msg);
    }

    @Override
    public void sendPendingApprovalEmail(String to, String classroomName,
            String bookingDate, String timeSlot, double price) {
        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setTo(to);
        msg.setSubject("⏳ Booking Pending Approval - " + classroomName);
        msg.setText(
            "Dear User,\n\n" +
            "Your booking request has been received and is PENDING admin approval.\n\n" +
            "━━━━━━━━━━━━━━━━━━━━━━━━━━━━\n" +
            "  Classroom : " + classroomName + "\n" +
            "  Date      : " + bookingDate + "\n" +
            "  Time      : " + timeSlot + "\n" +
            "  Total Cost: ₹" + price + "\n" +
            "━━━━━━━━━━━━━━━━━━━━━━━━━━━━\n\n" +
            "You will receive another email once admin approves your booking.\n\n" +
            "Thank you,\nTata Motors Training Center"
        );
        mailSender.send(msg);
    }
}