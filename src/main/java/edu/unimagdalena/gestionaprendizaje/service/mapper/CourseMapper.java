package edu.unimagdalena.gestionaprendizaje.service.mapper;

import edu.unimagdalena.gestionaprendizaje.api.dto.CourseDtos;
import edu.unimagdalena.gestionaprendizaje.domine.Entities.Course;

public class CourseMapper {

    public static Course toEntity(CourseDtos.CourseCreateRequest req){
        return Course.builder()
                .title(req.title())
                .status(req.status()).build();
    }

    public static void Patch(Course course, CourseDtos.CourseUpdateRequest req){
        if(req.title() != null) course.setTitle(req.title());
        if(req.status() != null) course.setStatus(req.status());
        if(req.active() != null) course.setActive(req.active());
    }

    public static CourseDtos.CourseResponse toResponse(Course course){
        return new CourseDtos.CourseResponse(
                course.getId(),
                course.getTitle(),
                course.getStatus(),
                course.isActive(),
                course.getCreatedAt(),
                course.getUpdatedAt()
        );
    }

}
