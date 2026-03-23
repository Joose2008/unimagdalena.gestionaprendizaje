package edu.unimagdalena.gestionaprendizaje.domine_repositories;

import edu.unimagdalena.gestionaprendizaje.Entities.Enrollment;
import edu.unimagdalena.gestionaprendizaje.Entities.Student;
import edu.unimagdalena.gestionaprendizaje.repositories.EnrollmentRepository;
import edu.unimagdalena.gestionaprendizaje.repositories.StudentRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import static org.assertj.core.api.Assertions.assertThat;

public class EnrollmentRepositoryTest extends AbstractRepositoryIT {

    @Autowired
    EnrollmentRepository enrollRep;

    @Autowired
    StudentRepository studRep;
    @Test
    @DisplayName("Enrollment: Encontrar enrollmente por nombre de estudiante")
    void shouldFindEnrollmentByStudentFullName() {

        Student student = Student.builder()
                .fullName("Samuel").build();

        studRep.save(student);

        enrollRep.save(Enrollment.builder()
                        .status("Completed")
                        .student(student)
                .build());

        //Pruebas

        assertThat(enrollRep.findEnrollmentByStudentFullName("Samuel").getFirst().getStatus()).isEqualTo("Completed");
    }





}
