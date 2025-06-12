package smartmunimji.backend.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import smartmunimji.backend.entities.Customer;
import smartmunimji.backend.daos.CustomerDao;
import smartmunimji.backend.security.JwtUtil;

import java.util.Optional;

@RestController
@RequestMapping("/api")
public class CustomerController {

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
        // Check if email already exists
        Optional<Customer> existingCustomer = customerDao.findByEmail(registerRequest.getEmail());
        if (existingCustomer.isPresent()) {
            return ResponseEntity.badRequest().body("Email already exists");
        }

        // Create new customer
        Customer customer = new Customer();
        customer.setName(registerRequest.getName());
        customer.setEmail(registerRequest.getEmail());
        customer.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        customer.setPhone(registerRequest.getPhone());
        customer.setAddress(registerRequest.getAddress());

        // Save customer to database
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

    @GetMapping("/cust/profile")
    public ResponseEntity<String> getProfile() {
        return ResponseEntity.ok("Customer profile accessed successfully");
    }

    @GetMapping("/admin/dashboard")
    public ResponseEntity<String> getAdminDashboard() {
        return ResponseEntity.ok("Admin dashboard accessed successfully");
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