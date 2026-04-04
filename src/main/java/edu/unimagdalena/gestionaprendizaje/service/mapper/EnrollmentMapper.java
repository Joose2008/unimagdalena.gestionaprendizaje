package edu.unimagdalena.gestionaprendizaje.service.mapper;

import edu.unimagdalena.gestionaprendizaje.api.dto.EnrollmentDtos;
import edu.unimagdalena.gestionaprendizaje.domine.Entities.Enrollment;

public class EnrollmentMapper {

    public static Enrollment toEntity(EnrollmentDtos.EnrollmentCreateRequest req) {
        return Enrollment.builder()
                .status(req.status())
                .build();
    }

    public static void patch(Enrollment entity, EnrollmentDtos.EnrollmentUpdateRequest req) {
        if(req.status() != null)
            entity.setStatus(req.status());
    }

    public static EnrollmentDtos.EnrollmentResponse toResponse(Enrollment enrollment) {
        return new EnrollmentDtos.EnrollmentResponse(
                enrollment.getId(),
                enrollment.getStatus(),
                enrollment.getEnrolledAt(),
                enrollment.getStudent().getId(),
                enrollment.getCourse().getId()
        );
    }
}