package com.tatamotors.classbooking.service;

import com.tatamotors.classbooking.entity.Classroom;
import java.util.List;

public interface ClassroomService {

    Classroom addClassroom(Classroom classroom);

    List<Classroom> getAllClassrooms();

    List<Classroom> filterClassrooms(boolean acAvailable, int capacity);
}
