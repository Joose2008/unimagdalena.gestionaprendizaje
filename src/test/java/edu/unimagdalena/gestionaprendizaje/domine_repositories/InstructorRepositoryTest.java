package edu.unimagdalena.gestionaprendizaje.domine_repositories;


import edu.unimagdalena.gestionaprendizaje.domine.Entities.Instructor;
import edu.unimagdalena.gestionaprendizaje.domine.Entities.InstructorProfile;
import edu.unimagdalena.gestionaprendizaje.domine.repositories.InstructorProfileRepository;
import edu.unimagdalena.gestionaprendizaje.domine.repositories.InstructorRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import static org.assertj.core.api.Assertions.assertThat;

public class InstructorRepositoryTest extends AbstractRepositoryIT{

    @Autowired
    InstructorRepository instRep;

    @Autowired
    InstructorProfileRepository instProRep;
    @Test
    @DisplayName("Instructor: encontrar instructor por nombre, email o telefono")
    void shouldFindInstructorByFullNameOrEmailOrPhone(){

        Instructor instructor = Instructor.builder()
                .email("@juan123")
                .fullName("Juan Rodriguez")
                .build();

        instRep.save(instructor);

        InstructorProfile instructorProfile = InstructorProfile.builder()
                .phone("123456")
                .instructor(instructor)
                .build();

        instProRep.save(instructorProfile);

        //pruebas

        assertThat(instRep.findInstructorByFullNameOrEmailOrPhone("@juan123", null, null).getFullName()).isEqualTo("Juan Rodriguez");
        assertThat(instRep.findInstructorByFullNameOrEmailOrPhone(null, null, "123456").getFullName()).isEqualTo("Juan Rodriguez");
        assertThat(instRep.findInstructorByFullNameOrEmailOrPhone(null, "Juan Rodriguez", null).getEmail()).isEqualTo("@juan123");



    }
}
