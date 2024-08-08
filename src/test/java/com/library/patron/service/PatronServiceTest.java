package com.library.patron.service;

import com.library.patron.dto.PatronDTO;
import com.library.patron.entity.Patron;
import com.library.patron.repository.PatronRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PatronServiceTest {

    @Mock
    private PatronRepository patronRepository;

    @InjectMocks
    private PatronService patronService;

    private Patron patron1;
    private Patron patron2;
    private PatronDTO patronDTO1;
    private PatronDTO patronDTO2;

    @BeforeEach
    void setUp() {
        patron1 = new Patron();
        patron1.setId(1L);
        patron1.setName("Patron 1");
        patron1.setEmail("patron1@example.com");
        patron1.setPhoneNumber("1234567890");
        patron1.setAddress("Address 1");

        patron2 = new Patron();
        patron2.setId(2L);
        patron2.setName("Patron 2");
        patron2.setEmail("patron2@example.com");
        patron2.setPhoneNumber("0987654321");
        patron2.setAddress("Address 2");

        patronDTO1 = new PatronDTO();
        patronDTO1.setId(1L);
        patronDTO1.setName("Patron 1");
        patronDTO1.setEmail("patron1@example.com");
        patronDTO1.setPhoneNumber("1234567890");
        patronDTO1.setAddress("Address 1");

        patronDTO2 = new PatronDTO();
        patronDTO2.setId(2L);
        patronDTO2.setName("Patron 2");
        patronDTO2.setEmail("patron2@example.com");
        patronDTO2.setPhoneNumber("0987654321");
        patronDTO2.setAddress("Address 2");
    }

    @Test
    void getAllPatrons_ShouldReturnListOfPatronDTOs() {
        when(patronRepository.findAll()).thenReturn(Arrays.asList(patron1, patron2));

        List<PatronDTO> result = patronService.getAllPatrons();

        assertEquals(2, result.size());
        assertEquals(patronDTO1.getName(), result.get(0).getName());
        assertEquals(patronDTO2.getName(), result.get(1).getName());
    }

    @Test
    void getPatronById_WhenPatronExists_ShouldReturnPatronDTO() {
        when(patronRepository.findById(1L)).thenReturn(Optional.of(patron1));

        PatronDTO result = patronService.getPatronById(1L);

        assertEquals(patronDTO1.getId(), result.getId());
        assertEquals(patronDTO1.getName(), result.getName());
    }

    @Test
    void getPatronById_WhenPatronNotFound_ShouldThrowEntityNotFoundException() {
        when(patronRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> {
            patronService.getPatronById(1L);
        });
    }

    @Test
    void createPatron_WhenEmailIsUnique_ShouldCreateAndReturnPatronDTO() {
        when(patronRepository.existsByEmail("patron1@example.com")).thenReturn(false);
        when(patronRepository.save(any(Patron.class))).thenReturn(patron1);

        PatronDTO result = patronService.createPatron(patronDTO1);

        assertEquals(patronDTO1.getName(), result.getName());
        assertEquals(patronDTO1.getEmail(), result.getEmail());
    }

    @Test
    void createPatron_WhenEmailIsNotUnique_ShouldThrowDataIntegrityViolationException() {
        when(patronRepository.existsByEmail("patron1@example.com")).thenReturn(true);

        assertThrows(DataIntegrityViolationException.class, () -> {
            patronService.createPatron(patronDTO1);
        });
    }

    @Test
    void updatePatron_WhenPatronExists_ShouldUpdateAndReturnPatronDTO() {
        when(patronRepository.findById(1L)).thenReturn(Optional.of(patron1));
        when(patronRepository.save(any(Patron.class))).thenReturn(patron1);

        PatronDTO result = patronService.updatePatron(1L, patronDTO1);

        assertEquals(patronDTO1.getName(), result.getName());
        assertEquals(patronDTO1.getEmail(), result.getEmail());
    }
    @Test
    void updatePatron_WhenPatronNotFound_ShouldThrowEntityNotFoundException() {
        when(patronRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> {
            patronService.updatePatron(1L, patronDTO1);
        });
    }

    @Test
    void updatePatron_WhenEmailNotUnique_ShouldThrowDataIntegrityViolationException() {
        // Arrange
        Long patronId = 1L;

        Patron existingPatron = new Patron();
        existingPatron.setId(patronId);
        existingPatron.setEmail("existingEmail@example.com");

        PatronDTO updatedPatronDTO = new PatronDTO();
        updatedPatronDTO.setId(patronId);
        updatedPatronDTO.setEmail("newEmail@example.com");

        when(patronRepository.findById(patronId)).thenReturn(Optional.of(existingPatron));
        when(patronRepository.existsByEmail(updatedPatronDTO.getEmail())).thenReturn(true);

        // Act & Assert
        assertThrows(DataIntegrityViolationException.class, () -> {
            patronService.updatePatron(patronId, updatedPatronDTO);
        });
    }


    @Test
    void deletePatron_WhenPatronExists_ShouldDeleteThePatron() {
        when(patronRepository.existsById(1L)).thenReturn(true);

        assertDoesNotThrow(() -> {
            patronService.deletePatron(1L);
        });

        verify(patronRepository, times(1)).deleteById(1L);
    }

    @Test
    void deletePatron_WhenPatronNotFound_ShouldThrowEntityNotFoundException() {
        when(patronRepository.existsById(1L)).thenReturn(false);

        assertThrows(EntityNotFoundException.class, () -> {
            patronService.deletePatron(1L);
        });
    }
}