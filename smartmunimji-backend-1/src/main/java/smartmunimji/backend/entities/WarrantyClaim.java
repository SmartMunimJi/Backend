package smartmunimji.backend.entities;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "warranty_claims")
public class WarrantyClaim {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "claim_id")
    private Long claimId;

    @Column(name = "registered_product_id", nullable = false)
    private Long registeredProductId;

    @Column(name = "customer_user_id", nullable = false)
    private Long customerUserId;

    @Column(name = "issue_description", nullable = false)
    private String issueDescription;

    @Column(name = "claim_status", nullable = false)
    private String claimStatus = "REQUESTED";

    @Column(name = "seller_response_notes")
    private String sellerResponseNotes;

    @Column(name = "claimed_at", nullable = false)
    private LocalDateTime claimedAt = LocalDateTime.now();

    @Column(name = "last_status_update_at")
    private LocalDateTime lastStatusUpdateAt;

    // Getters
    public Long getClaimId() { return claimId; }
    public Long getRegisteredProductId() { return registeredProductId; }
    public Long getCustomerUserId() { return customerUserId; }
    public String getIssueDescription() { return issueDescription; }
    public String getClaimStatus() { return claimStatus; }
    public String getSellerResponseNotes() { return sellerResponseNotes; }
    public LocalDateTime getClaimedAt() { return claimedAt; }
    public LocalDateTime getLastStatusUpdateAt() { return lastStatusUpdateAt; }

    // Setters
    public void setClaimId(Long claimId) { this.claimId = claimId; }
    public void setRegisteredProductId(Long registeredProductId) { this.registeredProductId = registeredProductId; }
    public void setCustomerUserId(Long customerUserId) { this.customerUserId = customerUserId; }
    public void setIssueDescription(String issueDescription) { this.issueDescription = issueDescription; }
    public void setClaimStatus(String claimStatus) { this.claimStatus = claimStatus; }
    public void setSellerResponseNotes(String sellerResponseNotes) { this.sellerResponseNotes = sellerResponseNotes; }
    public void setClaimedAt(LocalDateTime claimedAt) { this.claimedAt = claimedAt; }
    public void setLastStatusUpdateAt(LocalDateTime lastStatusUpdateAt) { this.lastStatusUpdateAt = lastStatusUpdateAt; }
}