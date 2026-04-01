package edu.unimagdalena.gestionaprendizaje.api.dto;

import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;

public class AssessmentDtos {

    public record  AssessmentCreateRequest(
            String type,
            int score,
            UUID studentId,
            UUID courseId
    ) implements Serializable{}

    public record AssessmentUpdateRequest(
            String type,
            Integer score,
            UUID studentId,
            UUID courseId
    ) implements Serializable{}

    public record AssessmentResponse(
            UUID id,
            String type,
            int score,
            Instant takenAt,
            UUID studentId,
            UUID courseId
    ) implements Serializable{}
}
