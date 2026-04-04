package edu.unimagdalena.gestionaprendizaje.service.impl;

import edu.unimagdalena.gestionaprendizaje.api.dto.StudentDtos.*;
import edu.unimagdalena.gestionaprendizaje.domine.Entities.Student;
import edu.unimagdalena.gestionaprendizaje.domine.repositories.StudentRepository;
import edu.unimagdalena.gestionaprendizaje.service.StudentService;
import edu.unimagdalena.gestionaprendizaje.service.mapper.StudentMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class StudentServiceImpl implements StudentService {

    private final StudentRepository studentRepository;

    @Override
    public StudentResponse create(StudentCreateRequest req) {
        Student student = StudentMapper.toEntity(req);
        Student saved = studentRepository.save(student);
        return StudentMapper.toResponse(saved);
    }

    @Override
    public StudentResponse update(UUID id, StudentUpdateRequest req) {
        Student student = studentRepository.findById(id).get();
        StudentMapper.patch(student, req);
        Student updated = studentRepository.save(student);
        return StudentMapper.toResponse(updated);
    }

    @Transactional(readOnly = true)
    @Override
    public StudentResponse getById(UUID id) {
        Student student = studentRepository.findById(id).get();
        return StudentMapper.toResponse(student);
    }

    @Transactional(readOnly = true)
    @Override
    public List<StudentResponse> list() {
        return studentRepository.findAll().stream()
            .map(StudentMapper::toResponse)
            .toList();
    }

    @Override
    public void delete(UUID id) {
        Student student = studentRepository.findById(id).get();
        studentRepository.delete(student);
    }
}
