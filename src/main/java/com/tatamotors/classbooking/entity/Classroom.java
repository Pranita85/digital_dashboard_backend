//////package com.tatamotors.classbooking.entity;
//////
//////import jakarta.persistence.*;
//////import java.time.LocalTime;
//////
//////@Entity
//////@Table(name = "classrooms")
//////public class Classroom {
//////
//////    @Id
//////    @GeneratedValue(strategy = GenerationType.IDENTITY)
//////    private Long id;
//////    
//////    private String name;
//////    private int capacity;
//////    private boolean ac;
//////    
//////    @Column(name = "price_per_hour")
//////    private double pricePerHour; // Standard Price
//////
//////    // --- NEW: Peak Pricing Fields ---
//////    @Column(name = "peak_price_per_hour")
//////    private double peakPricePerHour;
//////    
//////    @Column(name = "peak_start")
//////    private String peakStart; // e.g. "10:00"
//////    
//////    @Column(name = "peak_end")
//////    private String peakEnd;   // e.g. "18:00"
//////    
//////    private ClassroomStatus status;
//////
//////	public Long getId() {
//////		return id;
//////	}
//////
//////	public void setId(Long id) {
//////		this.id = id;
//////	}
//////
//////	public String getName() {
//////		return name;
//////	}
//////
//////	public void setName(String name) {
//////		this.name = name;
//////	}
//////
//////	public int getCapacity() {
//////		return capacity;
//////	}
//////
//////	public void setCapacity(int capacity) {
//////		this.capacity = capacity;
//////	}
//////
//////	public boolean isAc() {
//////		return ac;
//////	}
//////
//////	public void setAc(boolean ac) {
//////		this.ac = ac;
//////	}
//////
//////	public double getPricePerHour() {
//////		return pricePerHour;
//////	}
//////
//////	public void setPricePerHour(double pricePerHour) {
//////		this.pricePerHour = pricePerHour;
//////	}
//////
//////	public double getPeakPricePerHour() {
//////		return peakPricePerHour;
//////	}
//////
//////	public void setPeakPricePerHour(double peakPricePerHour) {
//////		this.peakPricePerHour = peakPricePerHour;
//////	}
//////
//////	public String getPeakStart() {
//////		return peakStart;
//////	}
//////
//////	public void setPeakStart(String peakStart) {
//////		this.peakStart = peakStart;
//////	}
//////
//////	public String getPeakEnd() {
//////		return peakEnd;
//////	}
//////
//////	public void setPeakEnd(String peakEnd) {
//////		this.peakEnd = peakEnd;
//////	}
//////
//////	public ClassroomStatus getStatus() {
//////		return status;
//////	}
//////
//////	public void setStatus(ClassroomStatus status) {
//////		this.status = status;
//////	}
//////    
//////    
//////    
//////
//////    // ... existing getters and setters for all fields
//////}
////
////
////
//////package com.tatamotors.classbooking.entity;
//////
//////import jakarta.persistence.*;
//////
//////@Entity
//////@Table(name = "classrooms")
//////public class Classroom {
//////
//////    @Id
//////    @GeneratedValue(strategy = GenerationType.IDENTITY)
//////    private Long id;
//////
//////    private String name;
//////    private int capacity;
//////    private boolean ac;
//////
//////    @Column(name = "price_per_hour")
//////    private Double pricePerHour;
//////
//////    @Column(name = "peak_price_per_hour")
//////    private Double peakPricePerHour;
//////
//////    @Column(name = "peak_start")
//////    private String peakStart;
//////
//////    @Column(name = "peak_end")
//////    private String peakEnd;
//////
//////    private ClassroomStatus status;
//////
//////    public Long getId() { return id; }
//////    public void setId(Long id) { this.id = id; }
//////
//////    public String getName() { return name; }
//////    public void setName(String name) { this.name = name; }
//////
//////    public int getCapacity() { return capacity; }
//////    public void setCapacity(int capacity) { this.capacity = capacity; }
//////
//////    public boolean isAc() { return ac; }
//////    public void setAc(boolean ac) { this.ac = ac; }
//////
//////    public Double getPricePerHour() { return pricePerHour != null ? pricePerHour : 0.0; }
//////    public void setPricePerHour(Double pricePerHour) { this.pricePerHour = pricePerHour; }
//////
//////    public Double getPeakPricePerHour() { return peakPricePerHour != null ? peakPricePerHour : 0.0; }
//////    public void setPeakPricePerHour(Double peakPricePerHour) { this.peakPricePerHour = peakPricePerHour; }
//////
//////    public String getPeakStart() { return peakStart; }
//////    public void setPeakStart(String peakStart) { this.peakStart = peakStart; }
//////
//////    public String getPeakEnd() { return peakEnd; }
//////    public void setPeakEnd(String peakEnd) { this.peakEnd = peakEnd; }
//////
//////    public ClassroomStatus getStatus() { return status; }
//////    public void setStatus(ClassroomStatus status) { this.status = status; }
//////}
////
////
////
////package com.tatamotors.classbooking.entity;
////
////import jakarta.persistence.*;
////
////@Entity
////@Table(name = "classrooms")
////public class Classroom {
////
////    @Id
////    @GeneratedValue(strategy = GenerationType.IDENTITY)
////    private Long id;
////
////    private String name;
////    private int capacity;
////    private boolean ac;
////
////    @Column(name = "price_per_hour")
////    private Double pricePerHour;
////
////    @Column(name = "peak_price_per_hour")
////    private Double peakPricePerHour;
////
////    @Column(name = "peak_start")
////    private String peakStart;
////
////    @Column(name = "peak_end")
////    private String peakEnd;
////
////    private ClassroomStatus status;
////
////    public Long getId() { return id; }
////    public void setId(Long id) { this.id = id; }
////
////    public String getName() { return name; }
////    public void setName(String name) { this.name = name; }
////
////    public int getCapacity() { return capacity; }
////    public void setCapacity(int capacity) { this.capacity = capacity; }
////
////    public boolean isAc() { return ac; }
////    public void setAc(boolean ac) { this.ac = ac; }
////
////    public Double getPricePerHour() { return pricePerHour != null ? pricePerHour : 0.0; }
////    public void setPricePerHour(Double pricePerHour) { this.pricePerHour = pricePerHour; }
////
////    public Double getPeakPricePerHour() { return peakPricePerHour != null ? peakPricePerHour : 0.0; }
////    public void setPeakPricePerHour(Double peakPricePerHour) { this.peakPricePerHour = peakPricePerHour; }
////
////    public String getPeakStart() { return peakStart; }
////    public void setPeakStart(String peakStart) { this.peakStart = peakStart; }
////
////    public String getPeakEnd() { return peakEnd; }
////    public void setPeakEnd(String peakEnd) { this.peakEnd = peakEnd; }
////
////    public ClassroomStatus getStatus() { return status; }
////    public void setStatus(ClassroomStatus status) { this.status = status; }
////}
//
//
//package com.tatamotors.classbooking.entity;
//
//import jakarta.persistence.*;
//
//@Entity
//@Table(name = "classrooms")
//public class Classroom {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;
//
//    private String name;
//    private int capacity;
//    private boolean ac;
//
//    @Column(name = "price_per_hour")
//    private Double pricePerHour;
//
//    @Column(name = "peak_price_per_hour")
//    private Double peakPricePerHour;
//
//    @Column(name = "peak_start")
//    private String peakStart;
//
//    @Column(name = "peak_end")
//    private String peakEnd;
//
//    private ClassroomStatus status;
//
//    public Long getId() { return id; }
//    public void setId(Long id) { this.id = id; }
//
//    public String getName() { return name; }
//    public void setName(String name) { this.name = name; }
//
//    public int getCapacity() { return capacity; }
//    public void setCapacity(int capacity) { this.capacity = capacity; }
//
//    public boolean isAc() { return ac; }
//    public void setAc(boolean ac) { this.ac = ac; }
//
//    public Double getPricePerHour() { return pricePerHour != null ? pricePerHour : 0.0; }
//    public void setPricePerHour(Double pricePerHour) { this.pricePerHour = pricePerHour; }
//
//    public Double getPeakPricePerHour() { return peakPricePerHour != null ? peakPricePerHour : 0.0; }
//    public void setPeakPricePerHour(Double peakPricePerHour) { this.peakPricePerHour = peakPricePerHour; }
//
//    public String getPeakStart() { return peakStart; }
//    public void setPeakStart(String peakStart) { this.peakStart = peakStart; }
//
//    public String getPeakEnd() { return peakEnd; }
//    public void setPeakEnd(String peakEnd) { this.peakEnd = peakEnd; }
//
//    public ClassroomStatus getStatus() { return status; }
//    public void setStatus(ClassroomStatus status) { this.status = status; }
//}


package com.tatamotors.classbooking.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "classrooms")
public class Classroom {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private int capacity;
    private boolean ac;

    @Column(name = "price_per_hour")
    private Double pricePerHour;

    @Column(name = "peak_price_per_hour")
    private Double peakPricePerHour;

    @Column(name = "peak_start")
    private String peakStart;

    @Column(name = "peak_end")
    private String peakEnd;

    private ClassroomStatus status;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public int getCapacity() { return capacity; }
    public void setCapacity(int capacity) { this.capacity = capacity; }

    public boolean isAc() { return ac; }
    public void setAc(boolean ac) { this.ac = ac; }

    public Double getPricePerHour() { return pricePerHour != null ? pricePerHour : 0.0; }
    public void setPricePerHour(Double pricePerHour) { this.pricePerHour = pricePerHour; }

    public Double getPeakPricePerHour() { return peakPricePerHour != null ? peakPricePerHour : 0.0; }
    public void setPeakPricePerHour(Double peakPricePerHour) { this.peakPricePerHour = peakPricePerHour; }

    public String getPeakStart() { return peakStart; }
    public void setPeakStart(String peakStart) { this.peakStart = peakStart; }

    public String getPeakEnd() { return peakEnd; }
    public void setPeakEnd(String peakEnd) { this.peakEnd = peakEnd; }

    public ClassroomStatus getStatus() { return status; }
    public void setStatus(ClassroomStatus status) { this.status = status; }
}