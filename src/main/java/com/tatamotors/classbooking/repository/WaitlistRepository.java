package com.tatamotors.classbooking.repository;

import com.tatamotors.classbooking.entity.Waitlist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WaitlistRepository extends JpaRepository<Waitlist, Long> {
}