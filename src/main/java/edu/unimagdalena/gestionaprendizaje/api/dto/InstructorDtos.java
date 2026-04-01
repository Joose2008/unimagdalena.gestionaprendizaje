package edu.unimagdalena.gestionaprendizaje.api.dto;

import edu.unimagdalena.gestionaprendizaje.domine.Entities.InstructorProfile;

import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;

public class InstructorDtos {

    public record InstructorCreateRequest(
            String email,
            String fullName,
            InstructorProfile profile
    ) implements Serializable{}

    public record InstructorProfileDto(
            String phone,
            String bio

    ) implements Serializable{}

    public record InstructorUpdateRequest(
            String email,
            String fullName,
            InstructorProfileDto profile
    ) implements Serializable{}

    public record InstructorReponse(
            UUID id,
            String email,
            String fullName,
            Instant createdAt,
            Instant UpdatedAt,
            InstructorProfileDto profile
    ) implements Serializable{}
}
