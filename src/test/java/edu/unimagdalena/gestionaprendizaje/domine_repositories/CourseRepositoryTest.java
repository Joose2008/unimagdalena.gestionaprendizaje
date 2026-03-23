package edu.unimagdalena.gestionaprendizaje.domine_repositories;

import edu.unimagdalena.gestionaprendizaje.Entities.Course;
import edu.unimagdalena.gestionaprendizaje.Entities.Instructor;
import edu.unimagdalena.gestionaprendizaje.Entities.Lesson;
import edu.unimagdalena.gestionaprendizaje.repositories.CourseRepository;
import edu.unimagdalena.gestionaprendizaje.repositories.InstructorRepository;
import edu.unimagdalena.gestionaprendizaje.repositories.LessonRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Set;

import static org.assertj.core.api.Assertions.*;
import static  org.assertj.core.api.Assertions.assertThat;
public class CourseRepositoryTest extends AbstractRepositoryIT{

    @Autowired
    CourseRepository courseRep;

    @Autowired
    InstructorRepository instRep;

    @Autowired
    LessonRepository lesRep;

    @Test
    @DisplayName("Course: encontrar cursos por nombre de instructor")
    void shouldFindCourseByInstructorFullName() {

        Instructor instructor = instRep.save(Instructor.builder()
                        .email("@Juan133")
                        .fullName("Juan Rodriguez")
                .build());

        courseRep.save(Course.builder()
                        .title("Math")
                        .instructor(instructor)
                .build());
        courseRep.save(Course.builder()
                        .title("English")
                .build());

        //Pruebas

        assertThat(courseRep.findCourseByInstructorFullName("Juan Rodriguez")
                .getFirst().getInstructor().getFullName()).isEqualTo("Juan Rodriguez");
    }

    @Test
    @DisplayName("Course: Encuentra los cursos por lessons")
    void shouldFindCourseByLesson(){
        Course course = Course.builder()
                .title("Math")
                .active(true)
                .build();

        Lesson lesson = Lesson.builder()
                .title("Algebra")
                .orderIndex(1)
                .course(course)
                .build();

        course.setLessons(Set.of(lesson));

        courseRep.save(course);
        lesRep.save(lesson);

        //Prueba

        assertThat(courseRep.findCourseByLessonTitle("Algebra").getFirst().getTitle()).isEqualTo("Math");
    }

}
