package edu.unimagdalena.gestionaprendizaje.service.impl;

import edu.unimagdalena.gestionaprendizaje.api.dto.CourseDtos.*;
import edu.unimagdalena.gestionaprendizaje.domine.Entities.Course;
import edu.unimagdalena.gestionaprendizaje.domine.Entities.Instructor;
import edu.unimagdalena.gestionaprendizaje.domine.repositories.CourseRepository;
import edu.unimagdalena.gestionaprendizaje.domine.repositories.InstructorRepository;
import edu.unimagdalena.gestionaprendizaje.service.CourseService;
import edu.unimagdalena.gestionaprendizaje.service.mapper.CourseMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class CourseServiceImpl implements CourseService {

    private final CourseRepository courseRepository;
    private final InstructorRepository instructorRepository;

    @Override
    public CourseResponse create(CourseCreateRequest req) {
        Course course = CourseMapper.toEntity(req);

        Instructor instructor = instructorRepository.findById(req.instructorId()).get();
        course.setInstructor(instructor);

        Course saved = courseRepository.save(course);
        return CourseMapper.toResponse(saved);
    }

    @Override
    public CourseResponse update(UUID id, CourseUpdateRequest req) {
        Course course = courseRepository.findById(id).get();

        CourseMapper.Patch(course, req);

        if(req.instructorId() != null) {
            Instructor instructor = instructorRepository.findById(req.instructorId()).get();
            course.setInstructor(instructor);
        }

        Course updated = courseRepository.save(course);
        return CourseMapper.toResponse(updated);
    }

    @Transactional(readOnly = true)
    @Override
    public CourseResponse getById(UUID id) {
        Course course = courseRepository.findById(id).get();
        return CourseMapper.toResponse(course);
    }

    @Transactional(readOnly = true)
    @Override
    public List<CourseResponse> list() {
        return courseRepository.findAll().stream()
            .map(CourseMapper::toResponse)
            .toList();
    }

    @Override
    public void delete(UUID id) {
        Course course = courseRepository.findById(id).get();
        courseRepository.delete(course);
    }

    @Transactional(readOnly = true)
    @Override
    public List<CourseResponse> findCourseByActive(boolean active) {
        return courseRepository.findCourseByActive(active).stream()
            .map(CourseMapper::toResponse)
            .toList();
    }

    @Transactional(readOnly = true)
    @Override
    public List<CourseResponse> findCourseByLessonTitle(String lessonTitle) {
        return courseRepository.findCourseByLessonTitle(lessonTitle).stream()
            .map(CourseMapper::toResponse)
            .toList();
    }

    @Transactional(readOnly = true)
    @Override
    public List<CourseResponse> findCourseByInstructorFullName(String instructorFullName) {
        return courseRepository.findCourseByInstructorFullName(instructorFullName).stream()
            .map(CourseMapper::toResponse)
            .toList();
    }
}
