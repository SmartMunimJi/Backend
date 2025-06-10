package smartmunimji.backend.daos;

import org.springframework.data.jpa.repository.JpaRepository;

import smartmunimji.backend.entities.Customer;

public interface CustomerDao extends JpaRepository<Customer, Integer> {
	Customer findByEmail(String email);
}
