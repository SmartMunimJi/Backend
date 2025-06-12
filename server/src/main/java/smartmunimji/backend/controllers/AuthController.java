package smartmunimji.backend.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import smartmunimji.backend.entities.Admin;
import smartmunimji.backend.entities.Customer;
import smartmunimji.backend.daos.AdminDao;
import smartmunimji.backend.daos.CustomerDao;
import smartmunimji.backend.security.JwtUtil;
import java.util.Optional;

@RestController
@RequestMapping("/sm")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private CustomerDao customerDao;

    @Autowired
    private AdminDao adminDao;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody RegisterRequest registerRequest) {
        Optional<Customer> existingCustomer = customerDao.findByEmail(registerRequest.getEmail());
        if (existingCustomer.isPresent()) {
            return ResponseEntity.badRequest().body("Email already exists");
        }

        Customer customer = new Customer();
        customer.setName(registerRequest.getName());
        customer.setEmail(registerRequest.getEmail());
        customer.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        customer.setPhone(registerRequest.getPhone());
        customer.setAddress(registerRequest.getAddress());

        customerDao.save(customer);
        return ResponseEntity.ok("User registered successfully");
    }

    @PostMapping("/authenticate")
    public ResponseEntity<String> authenticate(@RequestBody AuthRequest authRequest) {
        Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(
                authRequest.getEmail(),
                authRequest.getPassword()
            )
        );

        String token = jwtUtil.createToken(authentication);
        return ResponseEntity.ok(token);
    }

    @PostMapping("/admin/register")
    public ResponseEntity<String> registerAdmin(@RequestBody AdminRegisterRequest adminRequest) {
        Optional<Admin> existingAdmin = adminDao.findByEmail(adminRequest.getEmail());
        if (existingAdmin.isPresent()) {
            return ResponseEntity.badRequest().body("Admin email already exists");
        }

        Admin admin = new Admin();
        admin.setEmail(adminRequest.getEmail());
        admin.setPassword(passwordEncoder.encode(adminRequest.getPassword()));

        adminDao.save(admin);
        return ResponseEntity.ok("Admin registered successfully");
    }

    @PostMapping("/admin/authenticate")
    public ResponseEntity<String> authenticateAdmin(@RequestBody AdminAuthRequest adminRequest) {
        Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(
                adminRequest.getEmail(),
                adminRequest.getPassword()
            )
        );

        String token = jwtUtil.createToken(authentication);
        return ResponseEntity.ok(token);
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
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(401).body("Unauthorized");
        }

        String customerId = authentication.getName();
        Optional<Customer> customerOptional = customerDao.findById(Integer.parseInt(customerId));
        if (customerOptional.isEmpty()) {
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