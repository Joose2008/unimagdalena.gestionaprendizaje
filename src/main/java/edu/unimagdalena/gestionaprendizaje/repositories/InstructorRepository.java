package edu.unimagdalena.gestionaprendizaje.repositories;

import edu.unimagdalena.gestionaprendizaje.Entities.Course;
import edu.unimagdalena.gestionaprendizaje.Entities.Instructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;
@Repository
public interface InstructorRepository extends JpaRepository<Instructor, UUID> {

    @Query("select  i from Instructor i left join i.profile p where i.email = :instructorEmail or i.fullName =:instructorFullName or p.phone =:instructorPhone")
    Instructor findInstructorByFullNameOrEmailOrPhone(
            @Param("instructorEmail") String email,
            @Param("instructorFullName") String FullName,
            @Param("instructorPhone") String phone
    );
}
