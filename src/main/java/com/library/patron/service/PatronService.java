package com.library.patron.service;

import com.library.patron.dto.PatronDTO;
import com.library.patron.entity.Patron;
import com.library.patron.repository.PatronRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PatronService {

    private final PatronRepository patronRepository;

    @Autowired
    public PatronService(PatronRepository patronRepository) {
        this.patronRepository = patronRepository;
    }

    @Transactional(readOnly = true)
    public List<PatronDTO> getAllPatrons() {
        return patronRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public PatronDTO getPatronById(Long id) {
        Patron patron = patronRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Patron not found with id: " + id));
        return convertToDTO(patron);
    }

    @Transactional
    public PatronDTO createPatron(PatronDTO patronDTO) {
        if (patronRepository.existsByEmail(patronDTO.getEmail())) {
            throw new DataIntegrityViolationException("Patron with email " + patronDTO.getEmail() + " already exists");
        }
        Patron patron = convertToEntity(patronDTO);
        Patron savedPatron = patronRepository.save(patron);
        return convertToDTO(savedPatron);
    }

    @Transactional
    public PatronDTO updatePatron(Long id, PatronDTO patronDTO) {
        Patron existingPatron = patronRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Patron not found with id: " + id));

        if (!existingPatron.getEmail().equals(patronDTO.getEmail()) && patronRepository.existsByEmail(patronDTO.getEmail())) {
            throw new DataIntegrityViolationException("Patron with email " + patronDTO.getEmail() + " already exists");
        }

        updatePatronFields(existingPatron, patronDTO);
        Patron updatedPatron = patronRepository.save(existingPatron);
        return convertToDTO(updatedPatron);
    }

    @Transactional
    public void deletePatron(Long id) {
        if (!patronRepository.existsById(id)) {
            throw new EntityNotFoundException("Patron not found with id: " + id);
        }
        patronRepository.deleteById(id);
    }

    private PatronDTO convertToDTO(Patron patron) {
        PatronDTO dto = new PatronDTO();
        dto.setId(patron.getId());
        dto.setName(patron.getName());
        dto.setEmail(patron.getEmail());
        dto.setPhoneNumber(patron.getPhoneNumber());
        dto.setAddress(patron.getAddress());
        return dto;
    }

    private Patron convertToEntity(PatronDTO dto) {
        Patron patron = new Patron();
        patron.setName(dto.getName());
        patron.setEmail(dto.getEmail());
        patron.setPhoneNumber(dto.getPhoneNumber());
        patron.setAddress(dto.getAddress());
        return patron;
    }

    private void updatePatronFields(Patron patron, PatronDTO dto) {
        patron.setName(dto.getName());
        patron.setEmail(dto.getEmail());
        patron.setPhoneNumber(dto.getPhoneNumber());
        patron.setAddress(dto.getAddress());
    }
}