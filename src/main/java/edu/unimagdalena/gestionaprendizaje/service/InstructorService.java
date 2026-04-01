package edu.unimagdalena.gestionaprendizaje.service;

import edu.unimagdalena.gestionaprendizaje.api.dto.InstructorDtos.*;
import java.util.List;
import java.util.UUID;

public interface InstructorService {
    InstructorReponse create(InstructorCreateRequest req);
    InstructorReponse update(UUID id, InstructorUpdateRequest req);
    InstructorReponse getById(UUID id);
    List<InstructorReponse> list();
    void delete(UUID id);
    InstructorReponse findInstructorByFullNameOrEmailOrPhone(String email, String fullName, String phone);
}
