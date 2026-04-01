package edu.unimagdalena.gestionaprendizaje.domine.Entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "instructor_profiles")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class InstructorProfile {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String phone;
    private String bio;

    @OneToOne(optional = false)
    @JoinColumn(name = "instuctor_id", referencedColumnName = "id")
    private Instructor instructor;
}
