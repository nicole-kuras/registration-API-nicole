package pl.sda.registrationapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.sda.registrationapi.model.Address;

@Repository
public interface AddressesRepository extends JpaRepository<Address, Long> {
}
