//////package com.tatamotors.classbooking.service;
//////
//////public interface EmailService {
//////    void sendBookingConfirmation(
//////            String to,
//////            String classroomName,
//////            String bookingDate,
//////            String timeSlot,
//////            double price
//////    );
//////}
////
////
//////package com.tatamotors.classbooking.service;
//////
//////public interface EmailService {
//////
//////    void sendBookingConfirmation(
//////            String to,
//////            String classroomName,
//////            String bookingDate,
//////            String timeSlot,
//////            double price
//////    );
//////
//////    void sendCancellationEmail(
//////            String to,
//////            String classroomName,
//////            String bookingDate,
//////            String timeSlot
//////    );
//////}
////
////
////
////package com.tatamotors.classbooking.service;
////
////public interface EmailService {
////
////    void sendBookingConfirmation(String to, String classroomName,
////            String bookingDate, String timeSlot, double price);
////
////    void sendCancellationEmail(String to, String classroomName,
////            String bookingDate, String timeSlot);
////
////    void sendPendingApprovalEmail(String to, String classroomName,
////            String bookingDate, String timeSlot, double price);
////}
//
//package com.tatamotors.classbooking.service;
//
//public interface EmailService {
//
//    void sendBookingConfirmation(String to, String classroomName,
//            String bookingDate, String timeSlot, double price);
//
//    void sendCancellationEmail(String to, String classroomName,
//            String bookingDate, String timeSlot);
//
//    void sendPendingApprovalEmail(String to, String classroomName,
//            String bookingDate, String timeSlot, double price);
//}

package com.tatamotors.classbooking.service;

public interface EmailService {

    void sendBookingConfirmation(String to, String classroomName,
            String bookingDate, String timeSlot, double price);

    void sendCancellationEmail(String to, String classroomName,
            String bookingDate, String timeSlot);

    void sendPendingApprovalEmail(String to, String classroomName,
            String bookingDate, String timeSlot, double price);
}