package edu.unimagdalena.gestionaprendizaje.api.dto;

import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;

public class StudentDtos {

    public record StudentCreateRequest(String fullName) implements Serializable{}
    public record StudentUpdateRequest(String fullName) implements Serializable{}

    public record StudentResponse(
            UUID id,
            String fullName,
            Instant createdAt,
            Instant updatedAt
    ) implements Serializable{}
}
