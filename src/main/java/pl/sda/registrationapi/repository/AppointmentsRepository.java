package pl.sda.registrationapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.sda.registrationapi.model.Appointment;

@Repository
public interface AppointmentsRepository extends JpaRepository<Appointment, Long> {
}
