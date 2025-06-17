package smartmunimji.backend.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import smartmunimji.backend.daos.*;
import smartmunimji.backend.dtos.ProductRegistrationRequest;
import smartmunimji.backend.entities.*;

import java.time.LocalDate;
import java.util.Optional;

@RestController
@RequestMapping("/sm/customer")
public class ProductController {

    private static final Logger logger = LoggerFactory.getLogger(ProductController.class);

    @Autowired
    private CustomerDao customerDao;

    @Autowired
    private SellerDao sellerDao;

    @Autowired
    private ProductDao productDao;

    @Autowired
    private OrderDao orderDao;

    @Autowired
    private RegisteredProductDao registeredProductDao;

    @PostMapping("/register-product")
    public ResponseEntity<String> registerProduct(@RequestBody ProductRegistrationRequest request) {
        logger.info("Attempting to register product for customer email: {}", SecurityContextHolder.getContext().getAuthentication().getName());
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            logger.warn("Unauthorized product registration attempt");
            return ResponseEntity.status(401).body("Unauthorized");
        }

        String email = authentication.getName();
        Optional<Customer> customerOptional = customerDao.findByEmail(email);
        if (customerOptional.isEmpty()) {
            logger.warn("Customer not found for email: {}", email);
            return ResponseEntity.badRequest().body("Customer not found");
        }
        Integer customerId = customerOptional.get().getId();

        // Validate input
        if (request.getOrderId() == null || request.getDateOfPurchase() == null || request.getShopName() == null) {
            logger.warn("Missing required fields in request");
            return ResponseEntity.badRequest().body("Order ID, date of purchase, and shop name are required");
        }

        // Find seller by shop name
        Optional<Seller> sellerOptional = sellerDao.findByShopName(request.getShopName());
        if (sellerOptional.isEmpty()) {
            logger.warn("Seller not found for shop name: {}", request.getShopName());
            return ResponseEntity.badRequest().body("Invalid shop name");
        }
        Seller seller = sellerOptional.get();

        // Verify order in radhe_shyam_db.orders
        Optional<Order> orderOptional = orderDao.findByOrderIdAndDateOfPurchase(request.getOrderId(), request.getDateOfPurchase());
        if (orderOptional.isEmpty()) {
            logger.warn("Order not found for order ID: {} and date: {}", request.getOrderId(), request.getDateOfPurchase());
            return ResponseEntity.badRequest().body("Invalid order ID or date of purchase");
        }
        Order order = orderOptional.get();

        // Get product details
        Optional<Product> productOptional = productDao.findById(order.getProductId());
        if (productOptional.isEmpty()) {
            logger.warn("Product not found for ID: {}", order.getProductId());
            return ResponseEntity.status(500).body("Product not found");
        }
        Product product = productOptional.get();

        // Calculate warranty expiry date
        LocalDate warrantyExpiryDate = request.getDateOfPurchase();
        if (product.getWarrantyMonths() != null && product.getWarrantyMonths() > 0) {
            warrantyExpiryDate = warrantyExpiryDate.plusMonths(product.getWarrantyMonths());
        }

        // Check for duplicate registration
        Optional<RegisteredProduct> existingRegistration = registeredProductDao.findByOrderIdAndCustomerId(request.getOrderId(), customerId);
        if (existingRegistration.isPresent()) {
            logger.warn("Product already registered for order ID: {} and customer ID: {}", request.getOrderId(), customerId);
            return ResponseEntity.badRequest().body("Product already registered");
        }

        // Register product
        RegisteredProduct registeredProduct = new RegisteredProduct();
        registeredProduct.setCustomerId(customerId);
        registeredProduct.setSellerId(seller.getId());
        registeredProduct.setProductId(order.getProductId());
        registeredProduct.setOrderId(request.getOrderId());
        registeredProduct.setPurchaseDate(request.getDateOfPurchase());
        registeredProduct.setWarrantyExpiryDate(warrantyExpiryDate);
        registeredProduct.setStatus(RegisteredProduct.Status.ACTIVE);
        registeredProduct.setProductImage(null); // Pending implementation

        registeredProductDao.save(registeredProduct);
        logger.info("Product registered successfully for customer ID: {}, order ID: {}", customerId, request.getOrderId());
        return ResponseEntity.ok("Product registered successfully");
    }
}