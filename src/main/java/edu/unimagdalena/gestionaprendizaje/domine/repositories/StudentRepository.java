package edu.unimagdalena.gestionaprendizaje.domine.repositories;

import edu.unimagdalena.gestionaprendizaje.domine.Entities.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;
@Repository
public interface StudentRepository extends JpaRepository<Student, UUID> {
}
