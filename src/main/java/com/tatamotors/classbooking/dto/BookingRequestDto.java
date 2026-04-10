package com.tatamotors.classbooking.dto;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public class BookingRequestDto {
    // These fields MUST match the controller arguments
    private Long userId;
    private Long classroomId;
    private LocalDate date;
    private LocalTime startTime;
    private LocalTime endTime;
    private String departmentCode;
    private String purpose;
    
    // ... Getters and Setters for all these fields
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public Long getClassroomId() { return classroomId; }
    public void setClassroomId(Long classroomId) { this.classroomId = classroomId; }

    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }

    public LocalTime getStartTime() { return startTime; }
    public void setStartTime(LocalTime startTime) { this.startTime = startTime; }

    public LocalTime getEndTime() { return endTime; }
    public void setEndTime(LocalTime endTime) { this.endTime = endTime; }

    public String getDepartmentCode() { return departmentCode; }
    public void setDepartmentCode(String departmentCode) { this.departmentCode = departmentCode; }

    public String getPurpose() { return purpose; }
    public void setPurpose(String purpose) { this.purpose = purpose; }
}