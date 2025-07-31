package smartmunimji.backend.entities;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "sellers")
public class Seller {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "seller_id")
    private Long sellerId;

    @Column(name = "user_id", nullable = false, unique = true)
    private Long userId;

    @Column(name = "shop_name", nullable = false)
    private String shopName;

    @Column(name = "business_name")
    private String businessName;

    @Column(name = "business_email", nullable = false, unique = true)
    private String businessEmail;

    @Column(name = "business_phone_number", nullable = false, unique = true)
    private String businessPhoneNumber;

    @Column(name = "address")
    private String address;

    @Column(name = "contract_status", nullable = false)
    private String contractStatus = "ACTIVE";

    @Column(name = "api_base_url")
    private String apiBaseUrl;

    @Column(name = "api_key")
    private String apiKey;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt = LocalDateTime.now();

    // Getters
    public Long getSellerId() { return sellerId; }
    public Long getUserId() { return userId; }
    public String getShopName() { return shopName; }
    public String getBusinessName() { return businessName; }
    public String getBusinessEmail() { return businessEmail; }
    public String getBusinessPhoneNumber() { return businessPhoneNumber; }
    public String getAddress() { return address; }
    public String getContractStatus() { return contractStatus; }
    public String getApiBaseUrl() { return apiBaseUrl; }
    public String getApiKey() { return apiKey; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }

    // Setters
    public void setSellerId(Long sellerId) { this.sellerId = sellerId; }
    public void setUserId(Long userId) { this.userId = userId; }
    public void setShopName(String shopName) { this.shopName = shopName; }
    public void setBusinessName(String businessName) { this.businessName = businessName; }
    public void setBusinessEmail(String businessEmail) { this.businessEmail = businessEmail; }
    public void setBusinessPhoneNumber(String businessPhoneNumber) { this.businessPhoneNumber = businessPhoneNumber; }
    public void setAddress(String address) { this.address = address; }
    public void setContractStatus(String contractStatus) { this.contractStatus = contractStatus; }
    public void setApiBaseUrl(String apiBaseUrl) { this.apiBaseUrl = apiBaseUrl; }
    public void setApiKey(String apiKey) { this.apiKey = apiKey; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}