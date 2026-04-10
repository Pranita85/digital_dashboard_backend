package com.tatamotors.classbooking.service;

import org.springframework.stereotype.Service;
import com.tatamotors.classbooking.entity.Classroom;
import com.tatamotors.classbooking.repository.ClassroomRepository;

import java.util.List;

@Service
public class ClassroomServiceImpl implements ClassroomService {

    private final ClassroomRepository classroomRepository;

    public ClassroomServiceImpl(ClassroomRepository classroomRepository) {
        this.classroomRepository = classroomRepository;
    }

    @Override
    public Classroom addClassroom(Classroom classroom) {
        return classroomRepository.save(classroom);
    }

    @Override
    public List<Classroom> getAllClassrooms() {
        return classroomRepository.findAll();
    }

    @Override
    public List<Classroom> filterClassrooms(boolean ac, int capacity) {
        return classroomRepository
                .findByAcAndCapacityGreaterThanEqual(ac, capacity);
    }
}
