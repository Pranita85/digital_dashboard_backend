//package com.tatamotors.classbooking.entity;
//
//import jakarta.persistence.*;
//
//@Entity
//@Table(name = "system_settings")
//public class SystemSettings {
//
//    @Id
//    private Integer id = 1; // always single row
//
//    @Column(name = "operating_start")
//    private String operatingStart = "08:00";
//
//    @Column(name = "operating_end")
//    private String operatingEnd = "20:00";
//
//    @Column(name = "max_booking_hours")
//    private int maxBookingHours = 3;
//
//    @Column(name = "max_advance_days")
//    private int maxAdvanceDays = 30;
//
//    @Column(name = "auto_confirm")
//    private boolean autoConfirm = true;
//
//    @Column(name = "require_approval")
//    private boolean requireApproval = false;
//
//    @Column(name = "admin_email")
//    private String adminEmail = "admin@tatamotors.com";
//
//    @Column(name = "admin_phone")
//    private String adminPhone = "";
//
//    @Column(name = "email_notifications")
//    private boolean emailNotifications = true;
//
//    @Column(name = "booking_reminders")
//    private boolean bookingReminders = true;
//
//    public Integer getId() { return id; }
//    public void setId(Integer id) { this.id = id; }
//
//    public String getOperatingStart() { return operatingStart; }
//    public void setOperatingStart(String operatingStart) { this.operatingStart = operatingStart; }
//
//    public String getOperatingEnd() { return operatingEnd; }
//    public void setOperatingEnd(String operatingEnd) { this.operatingEnd = operatingEnd; }
//
//    public int getMaxBookingHours() { return maxBookingHours; }
//    public void setMaxBookingHours(int maxBookingHours) { this.maxBookingHours = maxBookingHours; }
//
//    public int getMaxAdvanceDays() { return maxAdvanceDays; }
//    public void setMaxAdvanceDays(int maxAdvanceDays) { this.maxAdvanceDays = maxAdvanceDays; }
//
//    public boolean isAutoConfirm() { return autoConfirm; }
//    public void setAutoConfirm(boolean autoConfirm) { this.autoConfirm = autoConfirm; }
//
//    public boolean isRequireApproval() { return requireApproval; }
//    public void setRequireApproval(boolean requireApproval) { this.requireApproval = requireApproval; }
//
//    public String getAdminEmail() { return adminEmail; }
//    public void setAdminEmail(String adminEmail) { this.adminEmail = adminEmail; }
//
//    public String getAdminPhone() { return adminPhone; }
//    public void setAdminPhone(String adminPhone) { this.adminPhone = adminPhone; }
//
//    public boolean isEmailNotifications() { return emailNotifications; }
//    public void setEmailNotifications(boolean emailNotifications) { this.emailNotifications = emailNotifications; }
//
//    public boolean isBookingReminders() { return bookingReminders; }
//    public void setBookingReminders(boolean bookingReminders) { this.bookingReminders = bookingReminders; }
//}

package com.tatamotors.classbooking.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "system_settings")
public class SystemSettings {

    @Id
    private Integer id = 1; // always single row

    @Column(name = "operating_start")
    private String operatingStart = "08:00";

    @Column(name = "operating_end")
    private String operatingEnd = "20:00";

    @Column(name = "max_booking_hours")
    private int maxBookingHours = 3;

    @Column(name = "max_advance_days")
    private int maxAdvanceDays = 30;

    @Column(name = "auto_confirm")
    private boolean autoConfirm = true;

    @Column(name = "require_approval")
    private boolean requireApproval = false;

    @Column(name = "admin_email")
    private String adminEmail = "admin@tatamotors.com";

    @Column(name = "admin_phone")
    private String adminPhone = "";

    @Column(name = "email_notifications")
    private boolean emailNotifications = true;

    @Column(name = "booking_reminders")
    private boolean bookingReminders = true;

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getOperatingStart() { return operatingStart; }
    public void setOperatingStart(String operatingStart) { this.operatingStart = operatingStart; }

    public String getOperatingEnd() { return operatingEnd; }
    public void setOperatingEnd(String operatingEnd) { this.operatingEnd = operatingEnd; }

    public int getMaxBookingHours() { return maxBookingHours; }
    public void setMaxBookingHours(int maxBookingHours) { this.maxBookingHours = maxBookingHours; }

    public int getMaxAdvanceDays() { return maxAdvanceDays; }
    public void setMaxAdvanceDays(int maxAdvanceDays) { this.maxAdvanceDays = maxAdvanceDays; }

    public boolean isAutoConfirm() { return autoConfirm; }
    public void setAutoConfirm(boolean autoConfirm) { this.autoConfirm = autoConfirm; }

    public boolean isRequireApproval() { return requireApproval; }
    public void setRequireApproval(boolean requireApproval) { this.requireApproval = requireApproval; }

    public String getAdminEmail() { return adminEmail; }
    public void setAdminEmail(String adminEmail) { this.adminEmail = adminEmail; }

    public String getAdminPhone() { return adminPhone; }
    public void setAdminPhone(String adminPhone) { this.adminPhone = adminPhone; }

    public boolean isEmailNotifications() { return emailNotifications; }
    public void setEmailNotifications(boolean emailNotifications) { this.emailNotifications = emailNotifications; }

    public boolean isBookingReminders() { return bookingReminders; }
    public void setBookingReminders(boolean bookingReminders) { this.bookingReminders = bookingReminders; }
}