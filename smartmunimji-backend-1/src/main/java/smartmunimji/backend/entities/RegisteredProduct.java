package smartmunimji.backend.entities;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "customer_registered_products")
public class RegisteredProduct {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "registered_product_id")
    private Long registeredProductId;

    @Column(name = "customer_user_id", nullable = false)
    private Long customerUserId;

    @Column(name = "seller_id", nullable = false)
    private Long sellerId;

    @Column(name = "seller_order_id", nullable = false)
    private String sellerOrderId;

    @Column(name = "seller_customer_phone_at_sale", nullable = false)
    private String sellerCustomerPhoneAtSale;

    @Column(name = "product_name", nullable = false)
    private String productName;

    @Column(name = "product_price")
    private BigDecimal productPrice;

    @Column(name = "date_of_purchase", nullable = false)
    private LocalDate dateOfPurchase;

    @Column(name = "warranty_valid_until", nullable = false)
    private LocalDate warrantyValidUntil;

    @Column(name = "is_warranty_eligible", nullable = false)
    private Boolean isWarrantyEligible = true;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt = LocalDateTime.now();

    // Getters
    public Long getRegisteredProductId() { return registeredProductId; }
    public Long getCustomerUserId() { return customerUserId; }
    public Long getSellerId() { return sellerId; }
    public String getSellerOrderId() { return sellerOrderId; }
    public String getSellerCustomerPhoneAtSale() { return sellerCustomerPhoneAtSale; }
    public String getProductName() { return productName; }
    public BigDecimal getProductPrice() { return productPrice; }
    public LocalDate getDateOfPurchase() { return dateOfPurchase; }
    public LocalDate getWarrantyValidUntil() { return warrantyValidUntil; }
    public Boolean getIsWarrantyEligible() { return isWarrantyEligible; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }

    // Setters
    public void setRegisteredProductId(Long registeredProductId) { this.registeredProductId = registeredProductId; }
    public void setCustomerUserId(Long customerUserId) { this.customerUserId = customerUserId; }
    public void setSellerId(Long sellerId) { this.sellerId = sellerId; }
    public void setSellerOrderId(String sellerOrderId) { this.sellerOrderId = sellerOrderId; }
    public void setSellerCustomerPhoneAtSale(String sellerCustomerPhoneAtSale) { this.sellerCustomerPhoneAtSale = sellerCustomerPhoneAtSale; }
    public void setProductName(String productName) { this.productName = productName; }
    public void setProductPrice(BigDecimal productPrice) { this.productPrice = productPrice; }
    public void setDateOfPurchase(LocalDate dateOfPurchase) { this.dateOfPurchase = dateOfPurchase; }
    public void setWarrantyValidUntil(LocalDate warrantyValidUntil) { this.warrantyValidUntil = warrantyValidUntil; }
    public void setIsWarrantyEligible(Boolean isWarrantyEligible) { this.isWarrantyEligible = isWarrantyEligible; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}