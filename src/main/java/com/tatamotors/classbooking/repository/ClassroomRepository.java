package com.tatamotors.classbooking.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.tatamotors.classbooking.entity.Classroom;

import java.util.List;

public interface ClassroomRepository extends JpaRepository<Classroom, Long> {

    List<Classroom> findByAcAndCapacityGreaterThanEqual(
            boolean ac,
            int capacity
    );
}
