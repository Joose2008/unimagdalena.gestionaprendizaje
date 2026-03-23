package edu.unimagdalena.gestionaprendizaje.domine_repositories;

import edu.unimagdalena.gestionaprendizaje.Entities.Assessment;

import java.time.Instant;
import java.util.List;

import edu.unimagdalena.gestionaprendizaje.Entities.Student;
import edu.unimagdalena.gestionaprendizaje.repositories.AssessmentRepository;
import edu.unimagdalena.gestionaprendizaje.repositories.StudentRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;

public class AssessmentRepositoryTest extends AbstractRepositoryIT{
    @Autowired
    AssessmentRepository assRep;

    @Autowired
    StudentRepository studRep;

    @Test
    @DisplayName("Assesment: Encuentra las calificaciones por estudiantes, ordenadas de mayor a menor.")
    void shouldFindByType(){

        // Save
        assRep.save(Assessment.builder()
                .type("Quiz")
                .score(15)
                .build());
        assRep.save(Assessment.builder()
                .type("Homework")
                .score(15)
                .build());

        // Pruebas

        assertThat(assRep.findAssessmentsByType("Quiz")).isNotEmpty();
        assertThat(assRep.findAssessmentsByType("Quiz").getFirst().getScore()).isEqualTo(15);
        assertThat(assRep.findAssessmentsByType("Homework")).hasSize(1);
        assertThat(assRep.findAssessmentsByType("Homework").getFirst().getType()).isEqualTo("Homework");
    }

    @Test
    @DisplayName("Assessment: encontrar assessment de students")
    void shouldFindAssessmentByStudent(){

        Student student = Student.builder().fullName("Carlos").build();
        studRep.save(student);

        //Assessments
        assRep.save(Assessment.builder()
                        .type("Quiz")
                        .score(50)
                        .student(student)
                .build());

        assRep.save(Assessment.builder()
                .type("HomeWork")
                .score(20)
                .student(student)
                .build());

        assRep.save(Assessment.builder()
                .type("Exam")
                .score(100)
                .student(student)
                .build());

        //pruebas
        assertThat(assRep.findBestAssessmentByStudent("Carlos").getFirst().getScore()).isEqualTo(100);
        assertThat(assRep.findBestAssessmentByStudent("Carlos").get(1).getScore()).isEqualTo(50);
        assertThat(assRep.findBestAssessmentByStudent("Carlos").get(2).getScore()).isEqualTo(20);

    }
}
