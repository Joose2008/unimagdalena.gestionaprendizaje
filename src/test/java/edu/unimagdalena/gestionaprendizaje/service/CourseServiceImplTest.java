package edu.unimagdalena.gestionaprendizaje.service;

import edu.unimagdalena.gestionaprendizaje.api.dto.CourseDtos.*;
import edu.unimagdalena.gestionaprendizaje.domine.Entities.Course;
import edu.unimagdalena.gestionaprendizaje.domine.Entities.Instructor;
import edu.unimagdalena.gestionaprendizaje.domine.repositories.CourseRepository;
import edu.unimagdalena.gestionaprendizaje.domine.repositories.InstructorRepository;
import edu.unimagdalena.gestionaprendizaje.service.impl.CourseServiceImpl;
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
public class CourseServiceImplTest {

    @Mock
    CourseRepository courseRepository;
    @Mock
    InstructorRepository instructorRepository;
    @InjectMocks
    CourseServiceImpl service;

    private Course course;
    private Instructor instructor;
    private UUID courseId;
    private UUID instructorId;

    @BeforeEach
    void setUp() {
        courseId = UUID.randomUUID();
        instructorId = UUID.randomUUID();

        instructor = Instructor.builder()
                .id(instructorId)
                .fullName("Daniel Verdugo")
                .build();

        course = Course.builder()
                .id(courseId)
                .title("Algebra")
                .active(true)
                .instructor(instructor)
                .build();
    }

    @Test
    void shouldCreateAndReturnResponseDto() {
        var req = new CourseCreateRequest("Algebra", "Active", instructorId);

        when(instructorRepository.findById(instructorId)).thenReturn(Optional.of(instructor));
        when(courseRepository.save(any(Course.class))).thenReturn(course);

        CourseResponse response = service.create(req);

        assertNotNull(response);
        assertEquals("Algebra", response.title());
        assertTrue(response.active());

        verify(instructorRepository, times(1)).findById(instructorId);
        verify(courseRepository, times(1)).save(any(Course.class));
    }

    @Test
    void shouldUpdateAndReturnResponseDto() {
        var req = new CourseUpdateRequest("Calculo Diferencial", "Pendiente", null, instructorId);

        Course updated = Course.builder()
                .id(courseId)
                .title("Calculo Diferencial")
                .active(false)
                .instructor(instructor)
                .build();

        when(courseRepository.findById(courseId)).thenReturn(Optional.of(course));
        when(courseRepository.save(any(Course.class))).thenReturn(updated);
        when(instructorRepository.findById(instructorId)).thenReturn(Optional.of(instructor));

        CourseResponse response = service.update(courseId, req);

        assertNotNull(response);
        assertEquals("Calculo Diferencial", response.title());
        assertFalse(response.active());

        verify(courseRepository, times(1)).findById(courseId);
        verify(courseRepository, times(1)).save(any(Course.class));
    }

    @Test
    void shouldUpdateInstructorWhenProvided() {
        UUID newInstructorId = UUID.randomUUID();
        Instructor newInstructor = Instructor.builder().id(newInstructorId).fullName("Jane Smith").build();
        var req = new CourseUpdateRequest(null, null, null, newInstructorId);

        Course updated = Course.builder()
                .id(courseId)
                .title("Algebra")
                .active(true)
                .instructor(newInstructor)
                .build();

        when(courseRepository.findById(courseId)).thenReturn(Optional.of(course));
        when(instructorRepository.findById(newInstructorId)).thenReturn(Optional.of(newInstructor));
        when(courseRepository.save(any(Course.class))).thenReturn(updated);

        CourseResponse response = service.update(courseId, req);

        assertNotNull(response);
        verify(instructorRepository, times(1)).findById(newInstructorId);
    }

    @Test
    void shouldReturnCourseById() {
        when(courseRepository.findById(courseId)).thenReturn(Optional.of(course));

        CourseResponse response = service.getById(courseId);

        assertNotNull(response);
        assertEquals(courseId, response.id());

        verify(courseRepository, times(1)).findById(courseId);
    }

    @Test
    void shouldReturnAllCourses() {
        when(courseRepository.findAll()).thenReturn(List.of(course));

        List<CourseResponse> responses = service.list();

        assertNotNull(responses);
        assertEquals(1, responses.size());

        verify(courseRepository, times(1)).findAll();
    }

    @Test
    void shouldDeleteCourse() {
        when(courseRepository.findById(courseId)).thenReturn(Optional.of(course));
        doNothing().when(courseRepository).delete(course);

        service.delete(courseId);

        verify(courseRepository, times(1)).findById(courseId);
        verify(courseRepository, times(1)).delete(course);
    }

    @Test
    void shouldFindCoursesByActive() {
        when(courseRepository.findCourseByActive(true)).thenReturn(List.of(course));

        List<CourseResponse> responses = service.findCourseByActive(true);

        assertNotNull(responses);
        assertEquals(1, responses.size());
        assertTrue(responses.get(0).active());

        verify(courseRepository, times(1)).findCourseByActive(true);
    }

    @Test
    void shouldFindCoursesByLessonTitle() {
        when(courseRepository.findCourseByLessonTitle("Intro")).thenReturn(List.of(course));

        List<CourseResponse> responses = service.findCourseByLessonTitle("Intro");

        assertNotNull(responses);
        assertEquals(1, responses.size());

        verify(courseRepository, times(1)).findCourseByLessonTitle("Intro");
    }

    @Test
    void shouldFindCoursesByInstructorFullName() {
        when(courseRepository.findCourseByInstructorFullName("John Doe")).thenReturn(List.of(course));

        List<CourseResponse> responses = service.findCourseByInstructorFullName("John Doe");

        assertNotNull(responses);
        assertEquals(1, responses.size());

        verify(courseRepository, times(1)).findCourseByInstructorFullName("John Doe");
    }
}
