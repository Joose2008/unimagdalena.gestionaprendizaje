package edu.unimagdalena.gestionaprendizaje.service.impl;

import edu.unimagdalena.gestionaprendizaje.api.dto.AssessmentDtos.*;
import edu.unimagdalena.gestionaprendizaje.domine.Entities.Assessment;
import edu.unimagdalena.gestionaprendizaje.domine.Entities.Course;
import edu.unimagdalena.gestionaprendizaje.domine.Entities.Student;
import edu.unimagdalena.gestionaprendizaje.domine.repositories.AssessmentRepository;
import edu.unimagdalena.gestionaprendizaje.domine.repositories.CourseRepository;
import edu.unimagdalena.gestionaprendizaje.domine.repositories.StudentRepository;
import edu.unimagdalena.gestionaprendizaje.service.AssessmentService;
import edu.unimagdalena.gestionaprendizaje.service.mapper.AssessmentMapper;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.Optional;
import java.util.UUID;
@Service
@RequiredArgsConstructor
@Transactional
public class AssessmentServiceImpl implements AssessmentService {

    private final AssessmentRepository assessmentRepository;
    private final CourseRepository courseRepository;
    private final StudentRepository studentRepository;

    @Override
    public AssessmentResponse create(AssessmentCreateRequest req) {
        Assessment assessment = AssessmentMapper.toEntity(req);

        Course course = courseRepository.findById(req.courseId()).get();
        Student student = studentRepository.findById(req.studentId()).get();

        assessment.setCourse(course);
        assessment.setStudent(student);

        var entitySaved = assessmentRepository.save(assessment);
        return AssessmentMapper.toResponse(entitySaved);
    }

    @Override
    public AssessmentResponse update(UUID id, AssessmentUpdateRequest req) {
        Assessment assessment = assessmentRepository.findById(id).get();

        AssessmentMapper.Patch(assessment, req);

        if(req.studentId() != null){
            Student student = studentRepository.findById(req.studentId()).get();
            assessment.setStudent(student);
        }

        if(req.courseId() != null) {
            Course course = courseRepository.findById(req.courseId()).get();
            assessment.setCourse(course);
        }

        Assessment assessmentUpdated = assessmentRepository.save(assessment);
        return AssessmentMapper.toResponse(assessmentUpdated);
    }

    @Transactional(readOnly = true)
    @Override
    public AssessmentResponse getById(UUID id) {
        Assessment assessment = assessmentRepository.findById(id).get();
        return AssessmentMapper.toResponse(assessment);
    }

    @Transactional(readOnly = true)
    @Override
    public List<AssessmentResponse> list() {
        return assessmentRepository.findAll().stream()
                .map(AssessmentMapper::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    @Override
    public List<AssessmentResponse> findBestAssessmentByStudent(String fullName) {
        return assessmentRepository.findBestAssessmentByStudent(fullName).stream()
                .map(AssessmentMapper::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    @Override
    public List<AssessmentResponse> findAssessmentsByType(String type) {
        return assessmentRepository.findAssessmentsByType(type).stream()
                .map(AssessmentMapper::toResponse)
                .toList();
    }

    @Override
    public void delete(UUID id) {
        Assessment assessment = assessmentRepository.findById(id).get();
        assessmentRepository.delete(assessment);
    }
}