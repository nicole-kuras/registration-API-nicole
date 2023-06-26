package pl.sda.registrationapi.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import pl.sda.registrationapi.dto.DoctorDTO;
import pl.sda.registrationapi.exception.ConflictException;
import pl.sda.registrationapi.exception.ResourceNotFoundException;
import pl.sda.registrationapi.mapper.DoctorsMapper;
import pl.sda.registrationapi.model.Doctor;
import pl.sda.registrationapi.model.User;
import pl.sda.registrationapi.repository.DoctorsRepository;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class DoctorsService {

    private final DoctorsMapper doctorsMapper;
    private final DoctorsRepository doctorsRepository;

    public List<DoctorDTO> findAll(PageRequest pageRequest) {
        log.info("Fetch doctors page req: '{}'... ", pageRequest);

        List<DoctorDTO> doctors = doctorsRepository.findAll(pageRequest).stream()
                .map(doctorsMapper::map)
                .toList();

        log.info("Fetching doctors completed.");
        return doctors;
    }

    public DoctorDTO findById(Long id) {
        log.info("Fetching doctor with id: '{}'...", id);
        Doctor doctor = getDoctor(id);
        log.info("Fetching doctor info completed.");
        return doctorsMapper.map(doctor);
    }

    public DoctorDTO create(Long userId, DoctorDTO doctorDTO) {
        log.info("Creating doctor...");
        Doctor doctor = doctorsMapper.map(doctorDTO);
        doctor.setUser(User.builder().id(userId).build());
        Doctor savedDoctor = doctorsRepository.save(doctor);
        log.info("Doctor create successfully.");
        return doctorsMapper.map(savedDoctor);
    }

    public void update(Long id, DoctorDTO doctorDTO) {
        log.info("Updating doctor with id: '{}'...", id);

        if (!id.equals(doctorDTO.getId())) {
            throw new ConflictException("Conflict - given ids does not match!");
        }
        Doctor currentDoctor = getDoctor(id);
        Doctor newDoctor = doctorsMapper.map(doctorDTO);
        newDoctor.setUser(currentDoctor.getUser());

        doctorsRepository.save(newDoctor);

        log.info("Update completed successfully.");
    }

    public void deleteById(Long id) {
        log.info("Removing doctor with id: '{}'...", id);
        throwResourceNotFoundIfDoctorDoesNotExists(id);
        doctorsRepository.deleteById(id);
        log.info("Doctor removal completed.");
    }

    private Doctor getDoctor(Long id) {
        return doctorsRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Doctor with given id does not exists!"));
    }

    private void throwResourceNotFoundIfDoctorDoesNotExists(Long id) {
        boolean exists = doctorsRepository.existsById(id);

        if (!exists) {
            throw new ResourceNotFoundException("Doctor with given id does not exists!");
        }
    }
}
