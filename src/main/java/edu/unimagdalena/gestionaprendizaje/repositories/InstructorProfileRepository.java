package edu.unimagdalena.gestionaprendizaje.repositories;

import edu.unimagdalena.gestionaprendizaje.Entities.InstructorProfile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;
@Repository
public interface InstructorProfileRepository extends JpaRepository<InstructorProfile, UUID> {

}
