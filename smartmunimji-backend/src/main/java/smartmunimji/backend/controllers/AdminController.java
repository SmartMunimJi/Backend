package smartmunimji.backend.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import smartmunimji.backend.daos.AdminDao;
import smartmunimji.backend.dtos.*;
import smartmunimji.backend.entities.Admin;
import smartmunimji.backend.security.JwtUtil;

import java.util.Optional;

@RestController
@RequestMapping("/sm/admin")
public class AdminController {

    private static final Logger logger = LoggerFactory.getLogger(AdminController.class);

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private AdminDao adminDao;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/register")
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

    @PostMapping("/authenticate")
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

    @GetMapping("/dashboard")
    public ResponseEntity<String> getAdminDashboard() {
        logger.info("Accessing admin dashboard");
        return ResponseEntity.ok("Admin dashboard accessed successfully");
    }
}