package edu.unimagdalena.gestionaprendizaje.service;

import edu.unimagdalena.gestionaprendizaje.api.dto.StudentDtos.*;
import java.util.List;
import java.util.UUID;

public interface StudentService {
    StudentResponse create(StudentCreateRequest req);
    StudentResponse update(UUID id, StudentUpdateRequest req);
    StudentResponse getById(UUID id);
    List<StudentResponse> list();
    void delete(UUID id);
}
