package smartmunimji.backend.services;

import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import smartmunimji.backend.daos.*;
import smartmunimji.backend.dtos.*;
import smartmunimji.backend.entities.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CustomerService {

    private static final Logger logger = LoggerFactory.getLogger(CustomerService.class);

    private final CustomerDao customerDao;
    private final RoleDao roleDao;
    private final SellerDao sellerDao;
    private final RegisteredProductDao registeredProductDao;
    private final WarrantyClaimDao warrantyClaimDao;
    private final PasswordEncoder passwordEncoder;
    private final SellerService sellerService;

    @Autowired
    public CustomerService(CustomerDao customerDao, RoleDao roleDao, SellerDao sellerDao,
                          RegisteredProductDao registeredProductDao, WarrantyClaimDao warrantyClaimDao,
                          PasswordEncoder passwordEncoder, SellerService sellerService) {
        this.customerDao = customerDao;
        this.roleDao = roleDao;
        this.sellerDao = sellerDao;
        this.registeredProductDao = registeredProductDao;
        this.warrantyClaimDao = warrantyClaimDao;
        this.passwordEncoder = passwordEncoder;
        this.sellerService = sellerService;
    }

    public Long registerCustomer(CustomerRegistrationDTO request) {
        logger.info("Registering customer with email: {}", request.email());
        if (customerDao.existsByEmail(request.email())) {
            logger.error("Email already exists: {}", request.email());
            throw new IllegalArgumentException("Email already exists");
        }
        if (customerDao.existsByPhoneNumber(request.phoneNumber())) {
            logger.error("Phone number already exists: {}", request.phoneNumber());
            throw new IllegalArgumentException("Phone number already exists");
        }

        Role customerRole = roleDao.findByRoleName("CUSTOMER")
                .orElseThrow(() -> {
                    logger.error("Role CUSTOMER not found");
                    return new EntityNotFoundException("Role CUSTOMER not found");
                });

        Customer customer = new Customer();
        customer.setName(request.name());
        customer.setEmail(request.email());
        customer.setPasswordHash(passwordEncoder.encode(request.password()));
        customer.setPhoneNumber(request.phoneNumber());
        customer.setAddress(request.address());
        customer.setRoleId(customerRole.getRoleId());
        customer.setIsActive(true);

        Long userId = customerDao.save(customer).getUserId();
        logger.info("Customer registered with userId: {}", userId);
        return userId;
    }

    public void updateCustomerProfile(Long userId, String authenticatedEmail, CustomerUpdateDTO request) {
        logger.info("Updating profile for userId: {} by email: {}", userId, authenticatedEmail);
        Customer customer = customerDao.findById(userId)
                .orElseThrow(() -> {
                    logger.error("User not found with ID: {}", userId);
                    return new EntityNotFoundException("User not found with ID: " + userId);
                });
        if (!customer.getEmail().equals(authenticatedEmail)) {
            logger.error("Attempt to update another user's profile: userId={}, email={}", userId, authenticatedEmail);
            throw new IllegalArgumentException("Cannot update another user's profile");
        }
        if (request.name() != null && !request.name().isEmpty()) {
            customer.setName(request.name());
        }
        if (request.address() != null) {
            customer.setAddress(request.address());
        }
        customer.setUpdatedAt(LocalDateTime.now());
        customerDao.save(customer);
        logger.info("Profile updated for userId: {}", userId);
    }

    public ProductRegisterResponseDTO registerProduct(String authenticatedEmail, ProductRegistrationDTO request) {
        logger.info("Registering product for email: {} with sellerId: {}", authenticatedEmail, request.sellerId());
        Customer customer = customerDao.findByEmail(authenticatedEmail)
                .orElseThrow(() -> {
                    logger.error("Customer not found with email: {}", authenticatedEmail);
                    return new EntityNotFoundException("Customer not found with email: " + authenticatedEmail);
                });
        Seller seller = sellerDao.findById(request.sellerId())
                .orElseThrow(() -> {
                    logger.error("Seller not found with ID: {}", request.sellerId());
                    return new EntityNotFoundException("Seller not found with ID: " + request.sellerId());
                });
        if (!seller.getContractStatus().equals("ACTIVE") || seller.getApiBaseUrl() == null || seller.getApiKey() == null) {
            logger.error("Seller API details incomplete or seller inactive for sellerId: {}", request.sellerId());
            throw new IllegalArgumentException("Seller API details incomplete or seller inactive");
        }
        if (registeredProductDao.findByCustomerUserIdAndSellerIdAndSellerOrderId(
                customer.getUserId(), request.sellerId(), request.orderId()).isPresent()) {
            logger.error("Product already registered for userId: {}, sellerId: {}, orderId: {}", 
                customer.getUserId(), request.sellerId(), request.orderId());
            throw new IllegalArgumentException("Product already registered");
        }

        SellerService.ProductDetails productDetails = sellerService.validateOrderWithSeller(
                seller, request.orderId(), customer.getPhoneNumber());
        if (!productDetails.isValid()) {
            logger.error("Invalid order or phone number for orderId: {}, phone: {}", 
                request.orderId(), customer.getPhoneNumber());
            throw new IllegalArgumentException("Invalid order or phone number");
        }

        RegisteredProduct product = new RegisteredProduct();
        product.setCustomerUserId(customer.getUserId());
        product.setSellerId(request.sellerId());
        product.setSellerOrderId(request.orderId());
        product.setSellerCustomerPhoneAtSale(customer.getPhoneNumber());
        product.setProductName(productDetails.productName());
        product.setProductPrice(productDetails.productPrice());
        LocalDate purchaseDate = LocalDate.parse(request.purchaseDate());
        product.setDateOfPurchase(purchaseDate);
        product.setWarrantyValidUntil(purchaseDate.plusMonths(productDetails.warrantyPeriodMonths()));
        product.setIsWarrantyEligible(true);

        RegisteredProduct savedProduct = registeredProductDao.save(product);
        logger.info("Product registered with registeredProductId: {} for user: {}", 
            savedProduct.getRegisteredProductId(), authenticatedEmail);
        return new ProductRegisterResponseDTO(
                "Product registered successfully",
                savedProduct.getRegisteredProductId(),
                savedProduct.getProductName(),
                savedProduct.getWarrantyValidUntil().toString()
        );
    }

    public List<RegisteredProductDTO> getRegisteredProducts(String authenticatedEmail) {
        logger.info("Fetching registered products for email: {}", authenticatedEmail);
        Customer customer = customerDao.findByEmail(authenticatedEmail)
                .orElseThrow(() -> {
                    logger.error("Customer not found with email: {}", authenticatedEmail);
                    return new EntityNotFoundException("Customer not found with email: " + authenticatedEmail);
                });
        List<RegisteredProduct> products = registeredProductDao.findByCustomerUserId(customer.getUserId());
        logger.info("Retrieved {} registered products for userId: {}", products.size(), customer.getUserId());
        return products.stream().map(product -> {
            Seller seller = sellerDao.findById(product.getSellerId())
                    .orElseThrow(() -> {
                        logger.error("Seller not found for sellerId: {}", product.getSellerId());
                        return new EntityNotFoundException("Seller not found");
                    });
            long daysRemaining = product.getIsWarrantyEligible() && !product.getWarrantyValidUntil().isBefore(LocalDate.now())
                    ? ChronoUnit.DAYS.between(LocalDate.now(), product.getWarrantyValidUntil())
                    : 0;
            return new RegisteredProductDTO(
                    product.getRegisteredProductId(),
                    product.getProductName(),
                    seller.getShopName(),
                    product.getSellerOrderId(),
                    product.getDateOfPurchase().toString(),
                    product.getWarrantyValidUntil().toString(),
                    product.getIsWarrantyEligible(),
                    daysRemaining
            );
        }).collect(Collectors.toList());
    }

    public void submitWarrantyClaim(String authenticatedEmail, WarrantyClaimDTO request) {
        logger.info("Submitting warranty claim for email: {} with registeredProductId: {}", 
            authenticatedEmail, request.registeredProductId());
        Customer customer = customerDao.findByEmail(authenticatedEmail)
                .orElseThrow(() -> {
                    logger.error("Customer not found with email: {}", authenticatedEmail);
                    return new EntityNotFoundException("Customer not found with email: " + authenticatedEmail);
                });
        RegisteredProduct product = registeredProductDao.findById(request.registeredProductId())
                .orElseThrow(() -> {
                    logger.error("Registered product not found with ID: {}", request.registeredProductId());
                    return new EntityNotFoundException("Registered product not found with ID: " + request.registeredProductId());
                });
        if (!product.getCustomerUserId().equals(customer.getUserId())) {
            logger.error("Product does not belong to customer: userId={}, productId={}", 
                customer.getUserId(), request.registeredProductId());
            throw new IllegalArgumentException("Product does not belong to this customer");
        }
        if (!product.getIsWarrantyEligible() || product.getWarrantyValidUntil().isBefore(LocalDate.now())) {
            logger.error("Product not eligible for warranty: productId={}", request.registeredProductId());
            throw new IllegalArgumentException("Product is not eligible for warranty");
        }
        if (warrantyClaimDao.existsByRegisteredProductIdAndClaimStatusIn(
                request.registeredProductId(), new String[]{"REQUESTED", "IN_PROGRESS"})) {
            logger.error("Active claim already exists for productId: {}", request.registeredProductId());
            throw new IllegalArgumentException("An active claim already exists for this product");
        }

        WarrantyClaim claim = new WarrantyClaim();
        claim.setRegisteredProductId(request.registeredProductId());
        claim.setCustomerUserId(customer.getUserId());
        claim.setIssueDescription(request.issueDescription());
        claim.setClaimStatus("REQUESTED");
        warrantyClaimDao.save(claim);
        logger.info("Warranty claim submitted for registeredProductId: {}", request.registeredProductId());
    }

    public WarrantyClaimResponseDTO getClaimStatus(String authenticatedEmail, Long claimId) {
        logger.info("Fetching claim status for email: {} with claimId: {}", authenticatedEmail, claimId);
        Customer customer = customerDao.findByEmail(authenticatedEmail)
                .orElseThrow(() -> {
                    logger.error("Customer not found with email: {}", authenticatedEmail);
                    return new EntityNotFoundException("Customer not found with email: " + authenticatedEmail);
                });
        WarrantyClaim claim = warrantyClaimDao.findByClaimIdAndCustomerUserId(claimId, customer.getUserId())
                .orElseThrow(() -> {
                    logger.error("Claim not found with ID: {} for userId: {}", claimId, customer.getUserId());
                    return new EntityNotFoundException("Claim not found with ID: " + claimId);
                });
        RegisteredProduct product = registeredProductDao.findById(claim.getRegisteredProductId())
                .orElseThrow(() -> {
                    logger.error("Registered product not found for claimId: {}", claimId);
                    return new EntityNotFoundException("Registered product not found");
                });
        Seller seller = sellerDao.findById(product.getSellerId())
                .orElseThrow(() -> {
                    logger.error("Seller not found for claimId: {}", claimId);
                    return new EntityNotFoundException("Seller not found");
                });
        logger.info("Retrieved claim status for claimId: {}", claimId);
        return new WarrantyClaimResponseDTO(
                claim.getClaimId(),
                claim.getRegisteredProductId(),
                product.getProductName(),
                seller.getShopName(),
                claim.getIssueDescription(),
                claim.getClaimStatus(),
                claim.getSellerResponseNotes(),
                claim.getClaimedAt().toString(),
                claim.getLastStatusUpdateAt() != null ? claim.getLastStatusUpdateAt().toString() : null
        );
    }
}