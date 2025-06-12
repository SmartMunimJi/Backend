package smartmunimji.backend.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import smartmunimji.backend.entities.Admin;
import smartmunimji.backend.entities.Customer;
import smartmunimji.backend.entities.Seller;
import smartmunimji.backend.daos.AdminDao;
import smartmunimji.backend.daos.CustomerDao;
import smartmunimji.backend.daos.SellerDao;
import smartmunimji.backend.security.JwtUtil;
import java.util.Optional;

@RestController
@RequestMapping("/sm")
public class AuthController {

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private CustomerDao customerDao;

    @Autowired
    private AdminDao adminDao;

    @Autowired
    private SellerDao sellerDao;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody RegisterRequest registerRequest) {
        logger.info("Registering customer with email: {}", registerRequest.getEmail());
        Optional<Customer> existingCustomer = customerDao.findByEmail(registerRequest.getEmail());
        if (existingCustomer.isPresent()) {
            logger.warn("Email already exists: {}", registerRequest.getEmail());
            return ResponseEntity.badRequest().body("Email already exists");
        }

        Customer customer = new Customer();
        customer.setName(registerRequest.getName());
        customer.setEmail(registerRequest.getEmail());
        customer.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        customer.setPhone(registerRequest.getPhone());
        customer.setAddress(registerRequest.getAddress());

        customerDao.save(customer);
        logger.info("Customer registered successfully: {}", registerRequest.getEmail());
        return ResponseEntity.ok("User registered successfully");
    }

    @PostMapping("/authenticate")
    public ResponseEntity<String> authenticate(@RequestBody AuthRequest authRequest) {
        logger.info("Authenticating customer with email: {}", authRequest.getEmail());
        try {
            Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                    authRequest.getEmail(),
                    authRequest.getPassword()
                )
            );
            String token = jwtUtil.createToken(authentication);
            logger.info("Customer authenticated successfully: {}", authRequest.getEmail());
            return ResponseEntity.ok(token);
        } catch (BadCredentialsException e) {
            logger.error("Invalid credentials for customer: {}", authRequest.getEmail());
            return ResponseEntity.status(401).body("Invalid credentials");
        }
    }

    @PostMapping("/admin/register")
    public ResponseEntity<String> registerAdmin(@RequestBody AdminRegisterRequest adminRequest) {
        logger.info("Registering admin with email: {}", adminRequest.getEmail());
        Optional<Admin> existingAdmin = adminDao.findByEmail(adminRequest.getEmail());
        if (existingAdmin.isPresent()) {
            logger.warn("Admin email already exists: {}", adminRequest.getEmail());
            return ResponseEntity.badRequest().body("Admin email already exists");
        }

        Admin admin = new Admin();
        admin.setEmail(adminRequest.getEmail());
        admin.setPassword(passwordEncoder.encode(adminRequest.getPassword()));

        adminDao.save(admin);
        logger.info("Admin registered successfully: {}", adminRequest.getEmail());
        return ResponseEntity.ok("Admin registered successfully");
    }

    @PostMapping("/admin/authenticate")
    public ResponseEntity<String> authenticateAdmin(@RequestBody AdminAuthRequest adminRequest) {
        logger.info("Authenticating admin with email: {}", adminRequest.getEmail());
        try {
            Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                    adminRequest.getEmail(),
                    adminRequest.getPassword()
                )
            );
            String token = jwtUtil.createToken(authentication);
            logger.info("Admin authenticated successfully: {}", adminRequest.getEmail());
            return ResponseEntity.ok(token);
        } catch (BadCredentialsException e) {
            logger.error("Invalid credentials for admin: {}", adminRequest.getEmail());
            return ResponseEntity.status(401).body("Invalid credentials");
        }
    }

    @PostMapping("/seller/register")
    public ResponseEntity<String> registerSeller(@RequestBody SellerRegisterRequest sellerRequest) {
        logger.info("Registering seller with email: {}", sellerRequest.getSellersEmail());
        Optional<Seller> existingSeller = sellerDao.findBySellersEmail(sellerRequest.getSellersEmail());
        if (existingSeller.isPresent()) {
            logger.warn("Seller email already exists: {}", sellerRequest.getSellersEmail());
            return ResponseEntity.badRequest().body("Seller email already exists");
        }

        Seller seller = new Seller();
        seller.setSellerName(sellerRequest.getSellerName());
        seller.setSellersEmail(sellerRequest.getSellersEmail());
        seller.setPassword(passwordEncoder.encode(sellerRequest.getPassword()));
        seller.setSellerContact(sellerRequest.getSellerContact());
        seller.setShopName(sellerRequest.getShopName());
        seller.setShopAddress(sellerRequest.getShopAddress());
        seller.setCity(sellerRequest.getCity());
        seller.setPincode(sellerRequest.getPincode());
        seller.setCategory(sellerRequest.getCategory());

        sellerDao.save(seller);
        logger.info("Seller registered successfully: {}", sellerRequest.getSellersEmail());
        return ResponseEntity.ok("Seller registered successfully");
    }

    @PostMapping("/seller/authenticate")
    public ResponseEntity<String> authenticateSeller(@RequestBody SellerAuthRequest sellerRequest) {
        logger.info("Attempting to authenticate seller with email: {}", sellerRequest.getSellersEmail());
        try {
            Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                    sellerRequest.getSellersEmail(),
                    sellerRequest.getPassword() // Fixed: Changed getSellerPassword() to getPassword()
                )
            );
            String token = jwtUtil.createToken(authentication);
            logger.info("Successfully authenticated seller with email: {}", sellerRequest.getSellersEmail());
            return ResponseEntity.ok(token);
        } catch (BadCredentialsException ex) {
            logger.warn("Failed to authenticate seller with email: {}", sellerRequest.getSellersEmail());
            return ResponseEntity.status(401).body("Invalid credentials");
        } catch (Exception ex) {
            logger.error("Unexpected error occurred while authenticating seller with email: {}", sellerRequest.getSellersEmail(), ex);
            return ResponseEntity.status(500).body("Internal server error");
        }
    }

    @GetMapping("/cust/profile")
    public ResponseEntity<String> getProfile() {
        return ResponseEntity.ok("Customer profile accessed successfully");
    }

    @GetMapping("/admin/dashboard")
    public ResponseEntity<String> getAdminDashboard() {
        return ResponseEntity.ok("Admin dashboard accessed successfully");
    }

    @PutMapping("/update-profile")
    public ResponseEntity<String> updateProfile(@RequestBody UpdateProfileRequest updateRequest) {
        logger.info("Updating profile for authenticated user");
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            logger.warn("Unauthorized profile update attempt");
            return ResponseEntity.status(401).body("Unauthorized");
        }

        String customerId = authentication.getName();
        Optional<Customer> customerOptional = customerDao.findById(Integer.parseInt(customerId));
        if (customerOptional.isEmpty()) {
            logger.warn("User not found for ID: {}", customerId);
            return ResponseEntity.badRequest().body("User not found");
        }

        Customer customer = customerOptional.get();

        if (updateRequest.getName() != null && !updateRequest.getName().isBlank()) {
            customer.setName(updateRequest.getName());
        }
        if (updateRequest.getPhone() != null) {
            customer.setPhone(updateRequest.getPhone());
        }
        if (updateRequest.getAddress() != null) {
            customer.setAddress(updateRequest.getAddress());
        }

        customerDao.save(customer);
        logger.info("Profile updated successfully for user ID: {}", customerId);
        return ResponseEntity.ok("Profile updated successfully");
    }
}

class AuthRequest {
    private String email;
    private String password;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}

class RegisterRequest {
    private String name;
    private String email;
    private String password;
    private String phone;
    private String address;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}

class UpdateProfileRequest {
    private String name;
    private String phone;
    private String address;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}

class AdminRegisterRequest {
    private String email;
    private String password;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}

class AdminAuthRequest {
    private String email;
    private String password;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}

class SellerRegisterRequest {
    private String sellerName;
    private String sellersEmail;
    private String password;
    private String sellerContact;
    private String shopName;
    private String shopAddress;
    private String city;
    private String pincode;
    private String category;

    public String getSellerName() {
        return sellerName;
    }

    public void setSellerName(String sellerName) {
        this.sellerName = sellerName;
    }

    public String getSellersEmail() {
        return sellersEmail;
    }

    public void setSellersEmail(String sellersEmail) {
        this.sellersEmail = sellersEmail;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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
}

class SellerAuthRequest {
    private String sellersEmail;
    private String password;

    public String getSellersEmail() {
        return sellersEmail;
    }

    public void setSellersEmail(String sellersEmail) {
        this.sellersEmail = sellersEmail;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}