package smartmunimji.backend.daos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import smartmunimji.backend.entities.RegisteredProduct;

import java.util.Optional;

@Repository
public interface RegisteredProductDao extends JpaRepository<RegisteredProduct, Integer> {
    Optional<RegisteredProduct> findByOrderIdAndCustomerId(String orderId, Integer customerId);
}