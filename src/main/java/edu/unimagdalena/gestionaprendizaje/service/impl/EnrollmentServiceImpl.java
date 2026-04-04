package edu.unimagdalena.gestionaprendizaje.service.impl;

import edu.unimagdalena.gestionaprendizaje.api.dto.EnrollmentDtos.*;
import edu.unimagdalena.gestionaprendizaje.domine.Entities.Enrollment;
import edu.unimagdalena.gestionaprendizaje.domine.Entities.Course;
import edu.unimagdalena.gestionaprendizaje.domine.Entities.Student;
import edu.unimagdalena.gestionaprendizaje.domine.repositories.EnrollmentRepository;
import edu.unimagdalena.gestionaprendizaje.domine.repositories.CourseRepository;
import edu.unimagdalena.gestionaprendizaje.domine.repositories.StudentRepository;
import edu.unimagdalena.gestionaprendizaje.service.EnrollmentService;
import edu.unimagdalena.gestionaprendizaje.service.mapper.EnrollmentMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class EnrollmentServiceImpl implements EnrollmentService {

    private final EnrollmentRepository enrollmentRepository;
    private final CourseRepository courseRepository;
    private final StudentRepository studentRepository;

    @Override
    public EnrollmentResponse create(EnrollmentCreateRequest req) {
        Enrollment enrollment = EnrollmentMapper.toEntity(req);

        Course course = courseRepository.findById(req.courseId()).get();
        Student student = studentRepository.findById(req.studentId()).get();

        enrollment.setCourse(course);
        enrollment.setStudent(student);

        Enrollment saved = enrollmentRepository.save(enrollment);
        return EnrollmentMapper.toResponse(saved);
    }

    @Override
    public EnrollmentResponse update(UUID id, EnrollmentUpdateRequest req) {
        Enrollment enrollment = enrollmentRepository.findById(id).get();

        EnrollmentMapper.patch(enrollment, req);

        if(req.studentId() != null) {
            Student student = studentRepository.findById(req.studentId()).get();
            enrollment.setStudent(student);
        }

        if(req.courseId() != null) {
            Course course = courseRepository.findById(req.courseId()).get();
            enrollment.setCourse(course);
        }

        Enrollment updated = enrollmentRepository.save(enrollment);
        return EnrollmentMapper.toResponse(updated);
    }

    @Transactional(readOnly = true)
    @Override
    public EnrollmentResponse getById(UUID id) {
        Enrollment enrollment = enrollmentRepository.findById(id).get();
        return EnrollmentMapper.toResponse(enrollment);
    }

    @Transactional(readOnly = true)
    @Override
    public List<EnrollmentResponse> list() {
        return enrollmentRepository.findAll().stream()
            .map(EnrollmentMapper::toResponse)
            .toList();
    }

    @Override
    public void delete(UUID id) {
        Enrollment enrollment = enrollmentRepository.findById(id).get();
        enrollmentRepository.delete(enrollment);
    }

    @Transactional(readOnly = true)
    @Override
    public List<EnrollmentResponse> findEnrollmentByStudentFullName(String studentFullName) {
        return enrollmentRepository.findEnrollmentByStudentFullName(studentFullName).stream()
            .map(EnrollmentMapper::toResponse)
            .toList();
    }
}
