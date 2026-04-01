package edu.unimagdalena.gestionaprendizaje.service;

import edu.unimagdalena.gestionaprendizaje.api.dto.InstructorDtos.*;
import edu.unimagdalena.gestionaprendizaje.domine.Entities.Instructor;
import edu.unimagdalena.gestionaprendizaje.domine.Entities.InstructorProfile;
import edu.unimagdalena.gestionaprendizaje.domine.repositories.InstructorRepository;
import edu.unimagdalena.gestionaprendizaje.service.impl.InstructorServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class InstructorServiceImplTest {

    @Mock
    InstructorRepository instructorRepository;
    // InstructorProfileRepository NO existe en el servicio,
    // el perfil se persiste en cascada junto al instructor.
    @InjectMocks
    InstructorServiceImpl service;

    private Instructor instructor;
    private InstructorProfile profile;
    private UUID instructorId;
    private UUID instructorProfileId;

    @BeforeEach
    void setUp() {
        instructorId = UUID.randomUUID();
        instructorProfileId = UUID.randomUUID();

        profile = InstructorProfile.builder()
                .id(instructorProfileId)
                .phone("3152165120")
                .bio("Experienced instructor")
                .build();

        instructor = Instructor.builder()
                .id(instructorId)
                .fullName("Ana Pérez")
                .email("ana@example.com")
                .profile(profile)
                .build();

        // Relación bidireccional
        profile.setInstructor(instructor);
    }

    @Test
    void shouldCreateAndReturnResponseDto() {
        // InstructorCreateRequest recibe InstructorProfile (entidad), no un DTO.
        // El mapper internamente extrae phone y bio de la entidad.
        var req = new InstructorCreateRequest("ana@example.com", "Ana Pérez", profile);

        when(instructorRepository.save(any(Instructor.class))).thenReturn(instructor);

        InstructorReponse response = service.create(req);

        assertNotNull(response);
        assertEquals("Ana Pérez", response.fullName());
        assertEquals("ana@example.com", response.email());
        // El perfil viene mapeado dentro del response
        assertNotNull(response.profile());
        assertEquals("3152165120", response.profile().phone());
        assertEquals("Experienced instructor", response.profile().bio());

        // Solo se verifica el repositorio que el servicio realmente usa
        verify(instructorRepository, times(1)).save(any(Instructor.class));
    }

    @Test
    void shouldCreateWithoutProfileAndReturnResponseDto() {
        // Caso en que el instructor se crea sin perfil
        var req = new InstructorCreateRequest("ana@example.com", "Ana Pérez", null);

        Instructor instructorSinPerfil = Instructor.builder()
                .id(instructorId)
                .fullName("Ana Pérez")
                .email("ana@example.com")
                .profile(null)
                .build();

        when(instructorRepository.save(any(Instructor.class))).thenReturn(instructorSinPerfil);

        InstructorReponse response = service.create(req);

        assertNotNull(response);
        // El mapper devuelve profile null cuando no hay perfil
        assertNull(response.profile());

        verify(instructorRepository, times(1)).save(any(Instructor.class));
    }

    @Test
    void shouldUpdateEmailAndReturnResponseDto() {
        var req = new InstructorUpdateRequest("ana.nueva@example.com", null, null);

        Instructor updated = Instructor.builder()
                .id(instructorId)
                .fullName("Ana Pérez")
                .email("ana.nueva@example.com")
                .profile(profile)
                .build();

        when(instructorRepository.findById(instructorId)).thenReturn(Optional.of(instructor));
        when(instructorRepository.save(any(Instructor.class))).thenReturn(updated);

        InstructorReponse response = service.update(instructorId, req);

        assertNotNull(response);
        assertEquals("ana.nueva@example.com", response.email());
        // El nombre no cambió
        assertEquals("Ana Pérez", response.fullName());

        verify(instructorRepository, times(1)).findById(instructorId);
        verify(instructorRepository, times(1)).save(any(Instructor.class));
    }

    @Test
    void shouldUpdateProfilePhoneAndReturnResponseDto() {
        // InstructorUpdateRequest recibe InstructorProfileDto para actualizar el perfil
        var profileDto = new InstructorProfileDto("3009998877", null);
        var req = new InstructorUpdateRequest(null, null, profileDto);

        InstructorProfile updatedProfile = InstructorProfile.builder()
                .id(instructorProfileId)
                .phone("3009998877")
                .bio("Experienced instructor")
                .instructor(instructor)
                .build();

        Instructor updated = Instructor.builder()
                .id(instructorId)
                .fullName("Ana Pérez")
                .email("ana@example.com")
                .profile(updatedProfile)
                .build();

        when(instructorRepository.findById(instructorId)).thenReturn(Optional.of(instructor));
        when(instructorRepository.save(any(Instructor.class))).thenReturn(updated);

        InstructorReponse response = service.update(instructorId, req);

        assertNotNull(response);
        assertNotNull(response.profile());
        assertEquals("3009998877", response.profile().phone());

        verify(instructorRepository, times(1)).findById(instructorId);
        verify(instructorRepository, times(1)).save(any(Instructor.class));
    }

    @Test
    void shouldReturnInstructorById() {
        when(instructorRepository.findById(instructorId)).thenReturn(Optional.of(instructor));

        InstructorReponse response = service.getById(instructorId);

        assertNotNull(response);
        assertEquals(instructorId, response.id());
        assertEquals("Ana Pérez", response.fullName());

        verify(instructorRepository, times(1)).findById(instructorId);
    }

    @Test
    void shouldReturnAllInstructors() {
        when(instructorRepository.findAll()).thenReturn(List.of(instructor));

        List<InstructorReponse> responses = service.list();

        assertNotNull(responses);
        assertEquals(1, responses.size());
        assertEquals("Ana Pérez", responses.get(0).fullName());

        verify(instructorRepository, times(1)).findAll();
    }

    @Test
    void shouldDeleteInstructor() {
        when(instructorRepository.findById(instructorId)).thenReturn(Optional.of(instructor));
        doNothing().when(instructorRepository).delete(instructor);

        service.delete(instructorId);

        verify(instructorRepository, times(1)).findById(instructorId);
        verify(instructorRepository, times(1)).delete(instructor);
    }

    @Test
    void shouldFindInstructorByEmail() {
        when(instructorRepository.findInstructorByFullNameOrEmailOrPhone(
                "ana@example.com", null, null
        )).thenReturn(instructor);

        InstructorReponse response = service.findInstructorByFullNameOrEmailOrPhone(
                "ana@example.com", null, null
        );

        assertNotNull(response);
        assertEquals("ana@example.com", response.email());

        verify(instructorRepository, times(1))
                .findInstructorByFullNameOrEmailOrPhone("ana@example.com", null, null);
    }

    @Test
    void shouldFindInstructorByPhone() {
        // El teléfono correcto es el del perfil seteado en setUp
        when(instructorRepository.findInstructorByFullNameOrEmailOrPhone(
                null, null, "3152165120"
        )).thenReturn(instructor);

        InstructorReponse response = service.findInstructorByFullNameOrEmailOrPhone(
                null, null, "3152165120"
        );

        assertNotNull(response);
        assertEquals("3152165120", response.profile().phone());

        verify(instructorRepository, times(1))
                .findInstructorByFullNameOrEmailOrPhone(null, null, "3152165120");
    }

    @Test
    void shouldFindInstructorByFullName() {
        when(instructorRepository.findInstructorByFullNameOrEmailOrPhone(
                null, "Ana Pérez", null
        )).thenReturn(instructor);

        InstructorReponse response = service.findInstructorByFullNameOrEmailOrPhone(
                null, "Ana Pérez", null
        );

        assertNotNull(response);
        assertEquals("Ana Pérez", response.fullName());

        verify(instructorRepository, times(1))
                .findInstructorByFullNameOrEmailOrPhone(null, "Ana Pérez", null);
    }
}