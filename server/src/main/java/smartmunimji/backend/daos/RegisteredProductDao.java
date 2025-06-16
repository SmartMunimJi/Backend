package smartmunimji.backend.daos;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import smartmunimji.backend.entities.RegisteredProduct;

@Repository
public interface RegisteredProductDao extends JpaRepository<RegisteredProduct, Integer> {

	Optional<RegisteredProduct> findByOrderIdAndCustomerId(String orderId, Integer customerId);
}