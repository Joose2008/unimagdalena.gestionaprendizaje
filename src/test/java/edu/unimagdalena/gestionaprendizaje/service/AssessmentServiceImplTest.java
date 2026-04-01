package edu.unimagdalena.gestionaprendizaje.service;

import edu.unimagdalena.gestionaprendizaje.api.dto.AssessmentDtos.*;
import edu.unimagdalena.gestionaprendizaje.domine.Entities.Assessment;
import edu.unimagdalena.gestionaprendizaje.domine.Entities.Course;
import edu.unimagdalena.gestionaprendizaje.domine.Entities.Student;
import edu.unimagdalena.gestionaprendizaje.domine.repositories.AssessmentRepository;
import edu.unimagdalena.gestionaprendizaje.domine.repositories.CourseRepository;
import edu.unimagdalena.gestionaprendizaje.domine.repositories.StudentRepository;
import edu.unimagdalena.gestionaprendizaje.service.impl.AssessmentServiceImpl;
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
public class AssessmentServiceImplTest {

    @Mock
    AssessmentRepository assessmentRepository;
    @Mock
    StudentRepository studentRepository;
    @Mock
    CourseRepository courseRepository;
    @InjectMocks
    AssessmentServiceImpl service;

    private Assessment assessment;
    private Course course;
    private Student student;
    private UUID assessmentId;
    private UUID courseId;
    private UUID studentId;

    @BeforeEach
    void setUp() {
        assessmentId = UUID.randomUUID();
        courseId = UUID.randomUUID();
        studentId = UUID.randomUUID();

        course = Course.builder()
                .id(courseId)
                .title("Math")
                .build();

        student = Student.builder()
                .id(studentId)
                .fullName("Samuel Cargas")
                .build();

        assessment = Assessment.builder()
                .id(assessmentId)
                .type("MidTerm")
                .score(10)
                .student(student)
                .course(course)
                .build();
    }

    @Test
    void shouldCreateAndReturnResponseDto() {
        var req = new AssessmentCreateRequest("MidTerm", 10, studentId, courseId);

        when(courseRepository.findById(courseId)).thenReturn(Optional.of(course));
        when(studentRepository.findById(studentId)).thenReturn(Optional.of(student));
        when(assessmentRepository.save(any(Assessment.class))).thenReturn(assessment);

        AssessmentResponse response = service.create(req);

        assertNotNull(response);
        assertEquals("MidTerm", response.type());
        assertEquals(10, response.score());

        verify(courseRepository).findById(courseId);
        verify(studentRepository, times(1)).findById(studentId);
        verify(assessmentRepository, times(1)).save(any(Assessment.class));
    }

    @Test
    void shouldUpdateAndReturnResponseDto() {
        var req = new AssessmentUpdateRequest("Final", 9, null, null);

        Assessment updated = Assessment.builder()
                .id(assessmentId)
                .type("Final")
                .score(9)
                .student(student)
                .course(course)
                .build();

        when(assessmentRepository.findById(assessmentId)).thenReturn(Optional.of(assessment));
        when(assessmentRepository.save(any(Assessment.class))).thenReturn(updated);

        AssessmentResponse response = service.update(assessmentId, req);

        assertNotNull(response);
        assertEquals("Final", response.type());
        assertEquals(9, response.score());

        verify(assessmentRepository, times(1)).findById(assessmentId);
        verify(assessmentRepository, times(1)).save(any(Assessment.class));
    }

    @Test
    void shouldUpdateStudentWhenProvided() {
        UUID newStudentId = UUID.randomUUID();
        Student newStudent = Student.builder().id(newStudentId).fullName("Laura Gil").build();
        var req = new AssessmentUpdateRequest(null, null , newStudentId, null);

        Assessment updated = Assessment.builder()
                .id(assessmentId)
                .type("MidTerm")
                .score(10)
                .student(newStudent)
                .course(course)
                .build();

        when(assessmentRepository.findById(assessmentId)).thenReturn(Optional.of(assessment));
        when(studentRepository.findById(newStudentId)).thenReturn(Optional.of(newStudent));
        when(assessmentRepository.save(any(Assessment.class))).thenReturn(updated);

        AssessmentResponse response = service.update(assessmentId, req);

        assertNotNull(response);
        verify(studentRepository, times(1)).findById(newStudentId);
    }

    @Test
    void shouldUpdateCourseWhenProvided() {
        UUID newCourseId = UUID.randomUUID();
        Course newCourse = Course.builder().id(newCourseId).title("History").build();
        var req = new AssessmentUpdateRequest(null, null, null, newCourseId);

        Assessment updated = Assessment.builder()
                .id(assessmentId)
                .type("MidTerm")
                .score(10)
                .student(student)
                .course(newCourse)
                .build();

        when(assessmentRepository.findById(assessmentId)).thenReturn(Optional.of(assessment));
        when(courseRepository.findById(newCourseId)).thenReturn(Optional.of(newCourse));
        when(assessmentRepository.save(any(Assessment.class))).thenReturn(updated);

        AssessmentResponse response = service.update(assessmentId, req);

        assertNotNull(response);
        verify(courseRepository, times(1)).findById(newCourseId);
    }

    @Test
    void shouldReturnAssessmentById() {
        when(assessmentRepository.findById(assessmentId)).thenReturn(Optional.of(assessment));

        AssessmentResponse response = service.getById(assessmentId);

        assertNotNull(response);
        assertEquals(assessmentId, response.id());

        verify(assessmentRepository, times(1)).findById(assessmentId);
    }

    @Test
    void shouldReturnAllAssessments() {
        when(assessmentRepository.findAll()).thenReturn(List.of(assessment));

        List<AssessmentResponse> responses = service.list();

        assertNotNull(responses);
        assertEquals(1, responses.size());

        verify(assessmentRepository, times(1)).findAll();
    }

    @Test
    void shouldDeleteAssessment() {
        when(assessmentRepository.findById(assessmentId)).thenReturn(Optional.of(assessment));
        doNothing().when(assessmentRepository).delete(assessment);

        service.delete(assessmentId);

        verify(assessmentRepository, times(1)).findById(assessmentId);
        verify(assessmentRepository, times(1)).delete(assessment);
    }

    @Test
    void shouldFindBestAssessmentByStudent() {
        when(assessmentRepository.findBestAssessmentByStudent("Samuel Cargas"))
                .thenReturn(List.of(assessment));

        List<AssessmentResponse> responses = service.findBestAssessmentByStudent("Samuel Cargas");

        assertNotNull(responses);
        assertEquals(1, responses.size());
        assertEquals(10, responses.get(0).score());

        verify(assessmentRepository, times(1)).findBestAssessmentByStudent("Samuel Cargas");
    }

    @Test
    void shouldReturnEmptyListWhenStudentHasNoAssessments() {
        when(assessmentRepository.findBestAssessmentByStudent("Unknown Student"))
                .thenReturn(List.of());

        List<AssessmentResponse> responses = service.findBestAssessmentByStudent("Unknown Student");

        assertNotNull(responses);
        assertTrue(responses.isEmpty());

        verify(assessmentRepository, times(1)).findBestAssessmentByStudent("Unknown Student");
    }

    @Test
    void shouldFindAssessmentsByType() {
        when(assessmentRepository.findAssessmentsByType("MidTerm"))
                .thenReturn(List.of(assessment));

        List<AssessmentResponse> responses = service.findAssessmentsByType("MidTerm");

        assertNotNull(responses);
        assertEquals(1, responses.size());
        assertEquals("MidTerm", responses.get(0).type());

        verify(assessmentRepository, times(1)).findAssessmentsByType("MidTerm");
    }

    @Test
    void shouldReturnEmptyListWhenNoAssessmentsOfType() {
        when(assessmentRepository.findAssessmentsByType("Quiz"))
                .thenReturn(List.of());

        List<AssessmentResponse> responses = service.findAssessmentsByType("Quiz");

        assertNotNull(responses);
        assertTrue(responses.isEmpty());

        verify(assessmentRepository, times(1)).findAssessmentsByType("Quiz");
    }
}