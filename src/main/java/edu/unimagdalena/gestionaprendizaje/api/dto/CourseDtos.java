package edu.unimagdalena.gestionaprendizaje.api.dto;

import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;

public class CourseDtos {

    public record CourseCreateRequest(
        String title,
        String status,
        UUID instructorId
    ) implements Serializable{}

    public record CourseUpdateRequest(
            String title,
            String status,
            Boolean active,
            UUID instructorId
    ) implements Serializable{}

    public record CourseResponse(
            UUID id,
            String title,
            String status,
            boolean active,
            Instant createdAt,
            Instant updatedAt
    ) implements Serializable{}
}
