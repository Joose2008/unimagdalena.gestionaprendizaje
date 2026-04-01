package edu.unimagdalena.gestionaprendizaje.service;

import edu.unimagdalena.gestionaprendizaje.api.dto.StudentDtos.*;
import edu.unimagdalena.gestionaprendizaje.domine.Entities.Student;
import edu.unimagdalena.gestionaprendizaje.domine.repositories.StudentRepository;
import edu.unimagdalena.gestionaprendizaje.service.impl.StudentServiceImpl;
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
public class StudentServiceImplTest {

    @Mock
    StudentRepository studentRepository;
    @InjectMocks
    StudentServiceImpl service;

    private Student student;
    private UUID studentId;

    @BeforeEach
    void setUp() {
        studentId = UUID.randomUUID();

        student = Student.builder()
                .id(studentId)
                .fullName("Luis Torres")
                .build();
    }

    @Test
    void shouldCreateAndReturnResponseDto() {
        var req = new StudentCreateRequest("Luis Torres");

        when(studentRepository.save(any(Student.class))).thenReturn(student);

        StudentResponse response = service.create(req);

        assertNotNull(response);
        assertEquals("Luis Torres", response.fullName());
        verify(studentRepository, times(1)).save(any(Student.class));
    }

    @Test
    void shouldUpdateAndReturnResponseDto() {
        var req = new StudentUpdateRequest("Luis Martínez");

        Student updated = Student.builder()
                .id(studentId)
                .fullName("Luis Martínez")
                .build();

        when(studentRepository.findById(studentId)).thenReturn(Optional.of(student));
        when(studentRepository.save(any(Student.class))).thenReturn(updated);

        StudentResponse response = service.update(studentId, req);

        assertNotNull(response);
        assertEquals("Luis Martínez", response.fullName());

        verify(studentRepository, times(1)).findById(studentId);
        verify(studentRepository, times(1)).save(any(Student.class));
    }

    @Test
    void shouldReturnStudentById() {
        when(studentRepository.findById(studentId)).thenReturn(Optional.of(student));

        StudentResponse response = service.getById(studentId);

        assertNotNull(response);
        assertEquals(studentId, response.id());

        verify(studentRepository, times(1)).findById(studentId);
    }

    @Test
    void shouldReturnAllStudents() {
        when(studentRepository.findAll()).thenReturn(List.of(student));

        List<StudentResponse> responses = service.list();

        assertNotNull(responses);
        assertEquals(1, responses.size());

        verify(studentRepository, times(1)).findAll();
    }

    @Test
    void shouldDeleteStudent() {
        when(studentRepository.findById(studentId)).thenReturn(Optional.of(student));
        doNothing().when(studentRepository).delete(student);

        service.delete(studentId);

        verify(studentRepository, times(1)).findById(studentId);
        verify(studentRepository, times(1)).delete(student);
    }
}
