package edu.unimagdalena.gestionaprendizaje.service;

import edu.unimagdalena.gestionaprendizaje.api.dto.EnrollmentDtos.*;
import edu.unimagdalena.gestionaprendizaje.domine.Entities.Course;
import edu.unimagdalena.gestionaprendizaje.domine.Entities.Enrollment;
import edu.unimagdalena.gestionaprendizaje.domine.Entities.Student;
import edu.unimagdalena.gestionaprendizaje.domine.repositories.CourseRepository;
import edu.unimagdalena.gestionaprendizaje.domine.repositories.EnrollmentRepository;
import edu.unimagdalena.gestionaprendizaje.domine.repositories.StudentRepository;
import edu.unimagdalena.gestionaprendizaje.service.impl.EnrollmentServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class EnrollmentServiceImplTest {

    @Mock
    EnrollmentRepository enrollmentRepository;
    @Mock
    CourseRepository courseRepository;
    @Mock
    StudentRepository studentRepository;
    @InjectMocks
    EnrollmentServiceImpl service;

    private Enrollment enrollment;
    private Course course;
    private Student student;
    private UUID enrollmentId;
    private UUID courseId;
    private UUID studentId;

    @BeforeEach
    void setUp() {
        enrollmentId = UUID.randomUUID();
        courseId = UUID.randomUUID();
        studentId = UUID.randomUUID();

        course = Course.builder()
                .id(courseId)
                .title("Fisica")
                .build();

        student = Student.builder()
                .id(studentId)
                .fullName("Maria Lopez")
                .build();

        enrollment = Enrollment.builder()
                .id(enrollmentId)
                .status("ACTIVE")
                .course(course)
                .student(student)
                .build();
    }

    @Test
    void shouldCreateAndReturnResponseDto() {
        var req = new EnrollmentCreateRequest("ACTIVE", studentId, courseId);

        when(courseRepository.findById(courseId)).thenReturn(Optional.of(course));
        when(studentRepository.findById(studentId)).thenReturn(Optional.of(student));
        when(enrollmentRepository.save(any(Enrollment.class))).thenReturn(enrollment);

        EnrollmentResponse response = service.create(req);

        assertNotNull(response);
        assertEquals("ACTIVE", response.status());

        verify(courseRepository, times(1)).findById(courseId);
        verify(studentRepository, times(1)).findById(studentId);
        verify(enrollmentRepository, times(1)).save(any(Enrollment.class));
    }

    @Test
    void shouldUpdateAndReturnResponseDto() {
        var req = new EnrollmentUpdateRequest("INACTIVE", studentId, courseId);

        Enrollment updated = Enrollment.builder()
                .id(enrollmentId)
                .status("INACTIVE")
                .course(course)
                .student(student)
                .build();

        when(enrollmentRepository.findById(enrollmentId)).thenReturn(Optional.of(enrollment));
        when(enrollmentRepository.save(any(Enrollment.class))).thenReturn(updated);
        when(studentRepository.findById(studentId)).thenReturn(Optional.of(student));
        when(courseRepository.findById(courseId)).thenReturn(Optional.of(course));

        EnrollmentResponse response = service.update(enrollmentId, req);

        assertNotNull(response);
        assertEquals("INACTIVE", response.status());

        verify(enrollmentRepository, times(1)).findById(enrollmentId);
        verify(enrollmentRepository, times(1)).save(any(Enrollment.class));
    }

    @Test
    void shouldUpdateStudentAndCourseWhenProvided() {
        UUID newStudentId = UUID.randomUUID();
        UUID newCourseId = UUID.randomUUID();
        Student newStudent = Student.builder().id(newStudentId).fullName("Carlos Ruiz").build();
        Course newCourse = Course.builder().id(newCourseId).title("Chemistry").build();

        var req = new EnrollmentUpdateRequest(null, newStudentId, newCourseId);

        Enrollment updated = Enrollment.builder()
                .id(enrollmentId)
                .status("ACTIVE")
                .course(newCourse)
                .student(newStudent)
                .build();

        when(enrollmentRepository.findById(enrollmentId)).thenReturn(Optional.of(enrollment));
        when(studentRepository.findById(newStudentId)).thenReturn(Optional.of(newStudent));
        when(courseRepository.findById(newCourseId)).thenReturn(Optional.of(newCourse));
        when(enrollmentRepository.save(any(Enrollment.class))).thenReturn(updated);

        EnrollmentResponse response = service.update(enrollmentId, req);

        assertNotNull(response);
        verify(studentRepository, times(1)).findById(newStudentId);
        verify(courseRepository, times(1)).findById(newCourseId);
    }

    @Test
    void shouldReturnEnrollmentById() {
        when(enrollmentRepository.findById(enrollmentId)).thenReturn(Optional.of(enrollment));

        EnrollmentResponse response = service.getById(enrollmentId);

        assertNotNull(response);
        assertEquals(enrollmentId, response.id());

        verify(enrollmentRepository, times(1)).findById(enrollmentId);
    }

    @Test
    void shouldReturnAllEnrollments() {
        when(enrollmentRepository.findAll()).thenReturn(List.of(enrollment));

        List<EnrollmentResponse> responses = service.list();

        assertNotNull(responses);
        assertEquals(1, responses.size());

        verify(enrollmentRepository, times(1)).findAll();
    }

    @Test
    void shouldDeleteEnrollment() {
        when(enrollmentRepository.findById(enrollmentId)).thenReturn(Optional.of(enrollment));
        doNothing().when(enrollmentRepository).delete(enrollment);

        service.delete(enrollmentId);

        verify(enrollmentRepository, times(1)).findById(enrollmentId);
        verify(enrollmentRepository, times(1)).delete(enrollment);
    }

    @Test
    void shouldFindEnrollmentsByStudentFullName() {
        when(enrollmentRepository.findEnrollmentByStudentFullName("Maria Lopez")).thenReturn(List.of(enrollment));

        List<EnrollmentResponse> responses = service.findEnrollmentByStudentFullName("Maria Lopez");

        assertNotNull(responses);
        assertEquals(1, responses.size());

        verify(enrollmentRepository, times(1)).findEnrollmentByStudentFullName("Maria Lopez");
    }
}
