package com.tatamotors.classbooking.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.tatamotors.classbooking.entity.BookingHistory;

public interface BookingHistoryRepository
        extends JpaRepository<BookingHistory, Long> {
}
