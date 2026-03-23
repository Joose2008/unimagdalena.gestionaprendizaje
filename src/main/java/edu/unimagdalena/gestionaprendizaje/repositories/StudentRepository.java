package edu.unimagdalena.gestionaprendizaje.repositories;

import edu.unimagdalena.gestionaprendizaje.Entities.Enrollment;
import edu.unimagdalena.gestionaprendizaje.Entities.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.UUID;
@Repository
public interface StudentRepository extends JpaRepository<Student, UUID> {
}
