package edu.unimagdalena.gestionaprendizaje.service;

import edu.unimagdalena.gestionaprendizaje.api.dto.EnrollmentDtos.*;
import java.util.List;
import java.util.UUID;

public interface EnrollmentService {
    EnrollmentResponse create(EnrollmentCreateRequest req);
    EnrollmentResponse update(UUID id, EnrollmentUpdateRequest req);
    EnrollmentResponse getById(UUID id);
    List<EnrollmentResponse> list();
    void delete(UUID id);
    List<EnrollmentResponse> findEnrollmentByStudentFullName(String studentFullName);
}
