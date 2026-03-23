package edu.unimagdalena.gestionaprendizaje.repositories;

import edu.unimagdalena.gestionaprendizaje.Entities.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;
@Repository
public interface CourseRepository extends JpaRepository<Course, UUID> {
    List<Course> findCourseByActive(boolean active);

    @Query("select c from Course c join c.lessons l where l.title = :lessonTitle")
    List<Course> findCourseByLessonTitle(@Param("lessonTitle") String lessonTitle);

    List<Course> findCourseByInstructorFullName(String instructorFullName);
}
