package edu.unimagdalena.gestionaprendizaje.domine.repositories;

import edu.unimagdalena.gestionaprendizaje.domine.Entities.Assessment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;
@Repository
public interface AssessmentRepository extends JpaRepository<Assessment, UUID> {
    @Query("""
                SELECT a
                from Assessment a
                join a.student s 
                    where s.fullName = :fullNameStud
                    order by a.score desc 
    
""")
    List<Assessment> findBestAssessmentByStudent(@Param("fullNameStud") String fullName);

    List<Assessment> findAssessmentsByType(String type);

}
