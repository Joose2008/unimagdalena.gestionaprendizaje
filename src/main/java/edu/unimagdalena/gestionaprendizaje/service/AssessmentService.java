package edu.unimagdalena.gestionaprendizaje.service;

import edu.unimagdalena.gestionaprendizaje.api.dto.AssessmentDtos.*;

import java.util.List;
import java.util.UUID;

public interface AssessmentService {
    AssessmentResponse create(AssessmentCreateRequest req);
    AssessmentResponse update(UUID id, AssessmentUpdateRequest req);
    AssessmentResponse getById(UUID id);
    List<AssessmentResponse> list();
    List<AssessmentResponse> findBestAssessmentByStudent(String fullName);
    List<AssessmentResponse> findAssessmentsByType(String type);
    void delete(UUID id);
}
