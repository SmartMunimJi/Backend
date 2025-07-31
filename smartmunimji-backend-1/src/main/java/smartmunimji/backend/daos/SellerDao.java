package smartmunimji.backend.daos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import smartmunimji.backend.entities.Seller;

import java.util.Optional;

@Repository
public interface SellerDao extends JpaRepository<Seller, Long> {
    Optional<Seller> findByUserId(Long userId);
}