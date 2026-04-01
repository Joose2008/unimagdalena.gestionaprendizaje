package edu.unimagdalena.gestionaprendizaje.service;

import edu.unimagdalena.gestionaprendizaje.api.dto.LessonDtos.*;
import java.util.List;
import java.util.UUID;

public interface LessonService {
    LessonResponse create(LessonCreateRequest req);
    LessonResponse update(UUID id, LessonUpdateRequest req);
    LessonResponse getById(UUID id);
    List<LessonResponse> list();
    void delete(UUID id);
}
