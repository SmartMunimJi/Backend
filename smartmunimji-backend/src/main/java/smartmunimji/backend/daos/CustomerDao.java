package smartmunimji.backend.daos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import smartmunimji.backend.entities.Customer;

import java.util.Optional;

@Repository
public interface CustomerDao extends JpaRepository<Customer, Integer> {
    Optional<Customer> findByEmail(String email);
}