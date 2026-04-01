package edu.unimagdalena.gestionaprendizaje.service.mapper;

import edu.unimagdalena.gestionaprendizaje.api.dto.StudentDtos;
import edu.unimagdalena.gestionaprendizaje.domine.Entities.Student;

public class StudentMapper {

    public static Student toEntity(StudentDtos.StudentCreateRequest req) {
        return Student.builder()
                .fullName(req.fullName())
                .build();
    }

    public static void patch(Student entity, StudentDtos.StudentUpdateRequest req) {
        if(req.fullName() != null)
            entity.setFullName(req.fullName());
    }

    public static StudentDtos.StudentResponse toResponse(Student student) {
        return new StudentDtos.StudentResponse(
                student.getId(),
                student.getFullName(),
                student.getCreatedAt(),
                student.getUpdatedAt()
        );
    }
}