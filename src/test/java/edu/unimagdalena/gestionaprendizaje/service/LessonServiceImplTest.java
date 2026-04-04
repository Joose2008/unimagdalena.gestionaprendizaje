package edu.unimagdalena.gestionaprendizaje.service;

import edu.unimagdalena.gestionaprendizaje.api.dto.LessonDtos.*;
import edu.unimagdalena.gestionaprendizaje.domine.Entities.Course;
import edu.unimagdalena.gestionaprendizaje.domine.Entities.Lesson;
import edu.unimagdalena.gestionaprendizaje.domine.repositories.CourseRepository;
import edu.unimagdalena.gestionaprendizaje.domine.repositories.LessonRepository;
import edu.unimagdalena.gestionaprendizaje.service.impl.LessonServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class LessonServiceImplTest {

    @Mock
    LessonRepository lessonRepository;
    @Mock
    CourseRepository courseRepository;
    @InjectMocks
    LessonServiceImpl service;

    private Lesson lesson;
    private Course course;
    private UUID lessonId;
    private UUID courseId;

    @BeforeEach
    void setUp() {
        lessonId = UUID.randomUUID();
        courseId = UUID.randomUUID();

        course = Course.builder()
                .id(courseId)
                .title("Biologia")
                .build();

        lesson = Lesson.builder()
                .id(lessonId)
                .title("Celulas")
                .orderIndex(1)
                .course(course)
                .build();
    }

    @Test
    void shouldCreateAndReturnResponseDto() {
        var req = new LessonCreateRequest("Celulas", 1, courseId);

        when(courseRepository.findById(courseId)).thenReturn(Optional.of(course));
        when(lessonRepository.save(any(Lesson.class))).thenReturn(lesson);

        LessonResponse response = service.create(req);

        assertNotNull(response);
        assertEquals("Celulas", response.title());
        assertEquals(1, response.orderIndex());

        verify(courseRepository, times(1)).findById(courseId);
        verify(lessonRepository, times(1)).save(any(Lesson.class));
    }

    @Test
    void shouldUpdateAndReturnResponseDto() {
        var req = new LessonUpdateRequest("ADN y ARN", null, null);

        Lesson updated = Lesson.builder()
                .id(lessonId)
                .title("ADN y ARN")
                .orderIndex(1)
                .course(course)
                .build();

        when(lessonRepository.findById(lessonId)).thenReturn(Optional.of(lesson));
        when(lessonRepository.save(any(Lesson.class))).thenReturn(updated);

        LessonResponse response = service.update(lessonId, req);

        assertNotNull(response);
        assertEquals("ADN y ARN", response.title());

        verify(lessonRepository, times(1)).findById(lessonId);
        verify(lessonRepository, times(1)).save(any(Lesson.class));
    }

    @Test
    void shouldUpdateCourseWhenProvided() {
        UUID newCourseId = UUID.randomUUID();
        Course newCourse = Course.builder().id(newCourseId).title("Quimica").build();
        var req = new LessonUpdateRequest(null, null, newCourseId);

        Lesson updated = Lesson.builder()
                .id(lessonId)
                .title("Celulas")
                .orderIndex(1)
                .course(newCourse)
                .build();

        when(lessonRepository.findById(lessonId)).thenReturn(Optional.of(lesson));
        when(courseRepository.findById(newCourseId)).thenReturn(Optional.of(newCourse));
        when(lessonRepository.save(any(Lesson.class))).thenReturn(updated);

        LessonResponse response = service.update(lessonId, req);

        assertNotNull(response);
        verify(courseRepository, times(1)).findById(newCourseId);
    }

    @Test
    void shouldReturnLessonById() {
        when(lessonRepository.findById(lessonId)).thenReturn(Optional.of(lesson));

        LessonResponse response = service.getById(lessonId);

        assertNotNull(response);
        assertEquals(lessonId, response.id());

        verify(lessonRepository, times(1)).findById(lessonId);
    }

    @Test
    void shouldReturnAllLessons() {
        when(lessonRepository.findAll()).thenReturn(List.of(lesson));

        List<LessonResponse> responses = service.list();

        assertNotNull(responses);
        assertEquals(1, responses.size());

        verify(lessonRepository, times(1)).findAll();
    }

    @Test
    void shouldDeleteLesson() {
        when(lessonRepository.findById(lessonId)).thenReturn(Optional.of(lesson));
        doNothing().when(lessonRepository).delete(lesson);

        service.delete(lessonId);

        verify(lessonRepository, times(1)).findById(lessonId);
        verify(lessonRepository, times(1)).delete(lesson);
    }
}
