package edu.unimagdalena.gestionaprendizaje.service;

import edu.unimagdalena.gestionaprendizaje.api.dto.CourseDtos.*;
import java.util.List;
import java.util.UUID;

public interface CourseService {
    CourseResponse create(CourseCreateRequest req);
    CourseResponse update(UUID id, CourseUpdateRequest req);
    CourseResponse getById(UUID id);
    List<CourseResponse> list();
    void delete(UUID id);
    List<CourseResponse> findCourseByActive(boolean active);
    List<CourseResponse> findCourseByLessonTitle(String lessonTitle);
    List<CourseResponse> findCourseByInstructorFullName(String instructorFullName);
}
