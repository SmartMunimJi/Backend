package smartmunimji.backend.entities;

import java.util.Collection;
import java.time.LocalDateTime;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Convert;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "sellers")
public class Seller implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "sellername", nullable = false, length = 100)
    private String sellerName;

    @Column(name = "sellercontact", length = 20)
    private String sellerContact;

    @Column(name = "shopname", length = 150)
    private String shopName;

    @Column(name = "sellersemail", nullable = false, length = 100, unique = true)
    private String sellersEmail;

    @Column(name = "shopaddress", columnDefinition = "TEXT")
    private String shopAddress;

    @Column(name = "city", length = 50)
    private String city;

    @Column(name = "pincode", length = 20)
    private String pincode;

    @Column(name = "category", length = 50)
    private String category;

    @Enumerated(EnumType.STRING)
    @Column(name = "contract_status", columnDefinition = "ENUM('active','pending','terminated') DEFAULT 'pending'")
    @Convert(converter = ContractStatusConverter.class)
    private ContractStatus contractStatus = ContractStatus.PENDING;

    @JsonIgnore
    @Column(name = "password", nullable = false, length = 255)
    private String password;

    @Column(name = "shop_image", length = 255)
    private String shopImage;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "extra1", length = 255)
    private String extra1;

    @Column(name = "extra2", length = 255)
    private String extra2;

    @Column(name = "extra3", length = 255)
    private String extra3;

    @Column(name = "extra4", length = 255)
    private String extra4;

    @Column(name = "extra5", length = 255)
    private String extra5;

    public enum ContractStatus {
        ACTIVE, PENDING, TERMINATED
    }

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return AuthorityUtils.createAuthorityList("ROLE_SELLER");
    }

    @Override
    public String getUsername() {
        return sellersEmail;
    }

    @Override
    @JsonIgnore
    public String getPassword() {
        return password;
    }

    @Override
    @JsonIgnore
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    @JsonIgnore
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    @JsonIgnore
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    @JsonIgnore
    public boolean isEnabled() {
        return true;
    }

    public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getSellerName() {
		return sellerName;
	}

	public void setSellerName(String sellerName) {
		this.sellerName = sellerName;
	}

	public String getSellerContact() {
		return sellerContact;
	}

	public void setSellerContact(String sellerContact) {
		this.sellerContact = sellerContact;
	}

	public String getShopName() {
		return shopName;
	}

	public void setShopName(String shopName) {
		this.shopName = shopName;
	}

	public String getSellersEmail() {
		return sellersEmail;
	}

	public void setSellersEmail(String sellersEmail) {
		this.sellersEmail = sellersEmail;
	}

	public String getShopAddress() {
		return shopAddress;
	}

	public void setShopAddress(String shopAddress) {
		this.shopAddress = shopAddress;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getPincode() {
		return pincode;
	}

	public void setPincode(String pincode) {
		this.pincode = pincode;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public ContractStatus getContractStatus() {
		return contractStatus;
	}

	public void setContractStatus(ContractStatus contractStatus) {
		this.contractStatus = contractStatus;
	}

	public String getShopImage() {
		return shopImage;
	}

	public void setShopImage(String shopImage) {
		this.shopImage = shopImage;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
	@Converter(autoApply = false)
    public static class ContractStatusConverter implements AttributeConverter<ContractStatus, String> {
        @Override
        public String convertToDatabaseColumn(ContractStatus status) {
            if (status == null) {
                return null;
            }
            return status.name().toLowerCase();
        }

        @Override
        public ContractStatus convertToEntityAttribute(String dbData) {
            if (dbData == null) {
                return null;
            }
            try {
                return ContractStatus.valueOf(dbData.toUpperCase());
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException("Unknown contract status: " + dbData, e);
            }
        }
    }
}