package smartmunimji.backend.daos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import smartmunimji.backend.entities.RegisteredProduct;

import java.util.List;
import java.util.Optional;

@Repository
public interface RegisteredProductDao extends JpaRepository<RegisteredProduct, Long> {
    Optional<RegisteredProduct> findByCustomerUserIdAndSellerIdAndSellerOrderId(Long customerUserId, Long sellerId, String sellerOrderId);
    List<RegisteredProduct> findByCustomerUserId(Long customerUserId);
}