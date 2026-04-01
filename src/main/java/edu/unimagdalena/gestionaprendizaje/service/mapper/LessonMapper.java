package edu.unimagdalena.gestionaprendizaje.service.mapper;

import edu.unimagdalena.gestionaprendizaje.api.dto.LessonDtos;
import edu.unimagdalena.gestionaprendizaje.domine.Entities.Lesson;

public class LessonMapper {

    public static Lesson toEntity(LessonDtos.LessonCreateRequest req) {
        return Lesson.builder()
                .title(req.title())
                .orderIndex(req.orderIndex())
                .build();
    }

    public static void patch(Lesson entity, LessonDtos.LessonUpdateRequest req) {
        if(req.title() != null)
            entity.setTitle(req.title());
        if(req.orderIndex() != null && req.orderIndex() > 0)
            entity.setOrderIndex(req.orderIndex());
    }

    public static LessonDtos.LessonResponse toResponse(Lesson lesson) {
        return new LessonDtos.LessonResponse(
                lesson.getId(),
                lesson.getTitle(),
                lesson.getOrderIndex(),
                lesson.getCourse().getId()
        );
    }
}