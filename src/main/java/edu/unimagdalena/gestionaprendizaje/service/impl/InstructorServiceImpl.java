package edu.unimagdalena.gestionaprendizaje.service.impl;

import edu.unimagdalena.gestionaprendizaje.api.dto.InstructorDtos.*;
import edu.unimagdalena.gestionaprendizaje.domine.Entities.Instructor;
import edu.unimagdalena.gestionaprendizaje.domine.Entities.InstructorProfile;
import edu.unimagdalena.gestionaprendizaje.domine.repositories.InstructorRepository;
import edu.unimagdalena.gestionaprendizaje.service.InstructorService;
import edu.unimagdalena.gestionaprendizaje.service.mapper.InstructorMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class InstructorServiceImpl implements InstructorService {

    private final InstructorRepository instructorRepository;

    @Override
    public InstructorReponse create(InstructorCreateRequest req) {
        Instructor instructor = InstructorMapper.toEntity(req);
        Instructor saved = instructorRepository.save(instructor);
        return InstructorMapper.toResponse(saved);
    }

    @Override
    public InstructorReponse update(UUID id, InstructorUpdateRequest req) {
        Instructor instructor = instructorRepository.findById(id).get();

        InstructorMapper.patch(instructor, req);

        Instructor updated = instructorRepository.save(instructor);
        return InstructorMapper.toResponse(updated);
    }

    @Transactional(readOnly = true)
    @Override
    public InstructorReponse getById(UUID id) {
        Instructor instructor = instructorRepository.findById(id).get();
        return InstructorMapper.toResponse(instructor);
    }

    @Transactional(readOnly = true)
    @Override
    public List<InstructorReponse> list() {
        return instructorRepository.findAll().stream()
            .map(InstructorMapper::toResponse)
            .toList();
    }

    @Override
    public void delete(UUID id) {
        Instructor instructor = instructorRepository.findById(id).get();
        instructorRepository.delete(instructor);
    }

    @Transactional(readOnly = true)
    @Override
    public InstructorReponse findInstructorByFullNameOrEmailOrPhone(String email, String fullName, String phone) {
        Instructor instructor = instructorRepository.findInstructorByFullNameOrEmailOrPhone(email, fullName, phone);
        return InstructorMapper.toResponse(instructor);
    }
}
