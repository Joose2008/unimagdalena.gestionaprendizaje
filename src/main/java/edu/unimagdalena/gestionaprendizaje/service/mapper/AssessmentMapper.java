package edu.unimagdalena.gestionaprendizaje.service.mapper;

import edu.unimagdalena.gestionaprendizaje.api.dto.AssessmentDtos;
import edu.unimagdalena.gestionaprendizaje.domine.Entities.Assessment;

import javax.sound.midi.Patch;
import java.nio.file.Path;

public class AssessmentMapper {

    public static Assessment toEntity(AssessmentDtos.AssessmentCreateRequest req) {
        return Assessment.builder().type(req.type()).score(req.score()).build();
    }

    public static void Patch(Assessment assessment, AssessmentDtos.AssessmentUpdateRequest req){
        if(req.type() != null){
            assessment.setType(req.type());
        }
        if(req.score() != null) {
            assessment.setScore(req.score());
        }
    }

    public static AssessmentDtos.AssessmentResponse toResponse(Assessment assessment) {
        return new AssessmentDtos.AssessmentResponse(
                assessment.getId(),
                assessment.getType(),
                assessment.getScore(),
                assessment.getTakenAt(),
                assessment.getStudent().getId(),
                assessment.getCourse().getId()
        );
    }

}
