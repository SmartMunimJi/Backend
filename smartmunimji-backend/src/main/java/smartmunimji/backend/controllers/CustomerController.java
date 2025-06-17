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
import smartmunimji.backend.daos.CustomerDao;
import smartmunimji.backend.dtos.*;
import smartmunimji.backend.entities.Customer;
import smartmunimji.backend.security.JwtUtil;

import java.util.Optional;

@RestController
@RequestMapping("/sm")
public class CustomerController {

    private static final Logger logger = LoggerFactory.getLogger(CustomerController.class);

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private CustomerDao customerDao;

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
        return ResponseEntity.ok("Customer registered successfully");
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

    @GetMapping("/cust/profile")
    public ResponseEntity<String> getProfile() {
        logger.info("Accessing customer profile");
        return ResponseEntity.ok("Customer profile accessed successfully");
    }

    @PutMapping("/update-profile")
    public ResponseEntity<String> updateProfile(@RequestBody UpdateProfileRequest updateRequest) {
        logger.info("Updating profile for authenticated user");
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            logger.warn("Unauthorized profile update attempt");
            return ResponseEntity.status(401).body("Unauthorized");
        }

        String email = authentication.getName();
        Optional<Customer> customerOptional = customerDao.findByEmail(email);
        if (customerOptional.isEmpty()) {
            logger.warn("Customer not found for email: {}", email);
            return ResponseEntity.badRequest().body("Customer not found");
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
        logger.info("Profile updated successfully for email: {}", email);
        return ResponseEntity.ok("Profile updated successfully");
    }
}