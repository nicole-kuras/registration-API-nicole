package pl.sda.registrationapi.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import pl.sda.registrationapi.dto.PatientDTO;
import pl.sda.registrationapi.exception.ConflictException;
import pl.sda.registrationapi.exception.ResourceNotFoundException;
import pl.sda.registrationapi.mapper.PatientsMapper;
import pl.sda.registrationapi.model.Patient;
import pl.sda.registrationapi.model.User;
import pl.sda.registrationapi.repository.PatientsRepository;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class PatientsService {

    private final PatientsMapper patientsMapper;
    private final PatientsRepository patientsRepository;

    public List<PatientDTO> findAll(PageRequest pageRequest) {
        log.info("Fetch patients page req: '{}'... ", pageRequest);

        List<PatientDTO> patients = patientsRepository.findAll(pageRequest).stream()
                .map(patientsMapper::map)
                .toList();

        log.info("Fetching patients completed.");
        return patients;
    }

    public PatientDTO findById(Long id) {
        log.info("Fetching patient with id: '{}'...", id);
        Patient patient = getPatient(id);
        log.info("Fetching patient info completed.");
        return patientsMapper.map(patient);
    }

    public PatientDTO create(Long userId, PatientDTO patientDTO) {
        log.info("Creating patient...");
        Patient patient = patientsMapper.map(patientDTO);
        patient.setUser(User.builder().id(userId).build());
        Patient savedPatient = patientsRepository.save(patient);
        log.info("Patient create successfully.");
        return patientsMapper.map(savedPatient);
    }

    public void update(Long id, PatientDTO patientDTO) {
        log.info("Updating patient with id: '{}'...", id);

        if (!id.equals(patientDTO.getId())) {
            throw new ConflictException("Conflict - given ids does not match!");
        }
        Patient currentPatient = getPatient(id);
        Patient newPatient = patientsMapper.map(patientDTO);
        newPatient.setUser(currentPatient.getUser());

        patientsRepository.save(newPatient);

        log.info("Update completed successfully.");
    }

    public void deleteById(Long id) {
        log.info("Removing patient with id: '{}'...", id);
        throwResourceNotFoundIfPatientDoesNotExists(id);
        patientsRepository.deleteById(id);
        log.info("Patient removal completed.");
    }

    private Patient getPatient(Long id) {
        return patientsRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Patient with given id does not exists!"));
    }

    private void throwResourceNotFoundIfPatientDoesNotExists(Long id) {
        boolean exists = patientsRepository.existsById(id);

        if (!exists) {
            throw new ResourceNotFoundException("Patient with given id does not exists!");
        }
    }
}
