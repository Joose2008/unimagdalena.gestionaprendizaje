package edu.unimagdalena.gestionaprendizaje.service.impl;

import edu.unimagdalena.gestionaprendizaje.api.dto.LessonDtos.*;
import edu.unimagdalena.gestionaprendizaje.domine.Entities.Lesson;
import edu.unimagdalena.gestionaprendizaje.domine.Entities.Course;
import edu.unimagdalena.gestionaprendizaje.domine.repositories.LessonRepository;
import edu.unimagdalena.gestionaprendizaje.domine.repositories.CourseRepository;
import edu.unimagdalena.gestionaprendizaje.service.LessonService;
import edu.unimagdalena.gestionaprendizaje.service.mapper.LessonMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class LessonServiceImpl implements LessonService {

    private final LessonRepository lessonRepository;
    private final CourseRepository courseRepository;

    @Override
    public LessonResponse create(LessonCreateRequest req) {
        Lesson lesson = LessonMapper.toEntity(req);

        Course course = courseRepository.findById(req.courseId()).get();
        lesson.setCourse(course);

        Lesson saved = lessonRepository.save(lesson);
        return LessonMapper.toResponse(saved);
    }

    @Override
    public LessonResponse update(UUID id, LessonUpdateRequest req) {
        Lesson lesson = lessonRepository.findById(id).get();

        LessonMapper.patch(lesson, req);

        if(req.courseId() != null) {
            Course course = courseRepository.findById(req.courseId()).get();
            lesson.setCourse(course);
        }

        Lesson updated = lessonRepository.save(lesson);
        return LessonMapper.toResponse(updated);
    }

    @Transactional(readOnly = true)
    @Override
    public LessonResponse getById(UUID id) {
        Lesson lesson = lessonRepository.findById(id).get();
        return LessonMapper.toResponse(lesson);
    }

    @Transactional(readOnly = true)
    @Override
    public List<LessonResponse> list() {
        return lessonRepository.findAll().stream()
            .map(LessonMapper::toResponse)
            .toList();
    }

    @Override
    public void delete(UUID id) {
        Lesson lesson = lessonRepository.findById(id).get();
        lessonRepository.delete(lesson);
    }
}
