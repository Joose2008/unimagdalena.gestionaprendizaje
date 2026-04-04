package edu.unimagdalena.gestionaprendizaje.service.mapper;

import edu.unimagdalena.gestionaprendizaje.api.dto.InstructorDtos;
import edu.unimagdalena.gestionaprendizaje.domine.Entities.Instructor;
import edu.unimagdalena.gestionaprendizaje.domine.Entities.InstructorProfile;

public class InstructorMapper {

    public static Instructor toEntity(InstructorDtos.InstructorCreateRequest req) {
        var profile = req.profile() == null ? null :
                InstructorProfile.builder()
                        .phone(req.profile().getPhone())
                        .bio(req.profile().getBio())
                        .build();

        return Instructor.builder()
                .email(req.email())
                .fullName(req.fullName())
                .profile(profile)
                .build();
    }

    public static void patch(Instructor entity, InstructorDtos.InstructorUpdateRequest req) {
        if(req.email() != null)
            entity.setEmail(req.email());
        if(req.fullName() != null)
            entity.setFullName(req.fullName());

        if(req.profile() != null) {
            var p = entity.getProfile();
            if(p == null) {
                p = new InstructorProfile();
                entity.setProfile(p);
            }
            if(req.profile().phone() != null)
                p.setPhone(req.profile().phone());
            if(req.profile().bio() != null)
                p.setBio(req.profile().bio());
        }
    }

    public static InstructorDtos.InstructorReponse toResponse(Instructor instructor) {
        var p = instructor.getProfile();
        var dtoProfile = p == null ? null :
                new InstructorDtos.InstructorProfileDto(p.getPhone(), p.getBio());

        return new InstructorDtos.InstructorReponse(
                instructor.getId(),
                instructor.getEmail(),
                instructor.getFullName(),
                instructor.getCreatedAt(),
                instructor.getUpdatedAt(),
                dtoProfile
        );
    }
}