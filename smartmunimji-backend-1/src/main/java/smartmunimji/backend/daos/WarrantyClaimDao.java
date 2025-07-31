package smartmunimji.backend.daos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import smartmunimji.backend.entities.WarrantyClaim;

import java.util.Optional;

@Repository
public interface WarrantyClaimDao extends JpaRepository<WarrantyClaim, Long> {
    Optional<WarrantyClaim> findByClaimIdAndCustomerUserId(Long claimId, Long customerUserId);
    boolean existsByRegisteredProductIdAndClaimStatusIn(Long registeredProductId, String[] claimStatuses);
}