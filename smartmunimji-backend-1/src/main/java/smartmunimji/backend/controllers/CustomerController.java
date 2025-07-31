package smartmunimji.backend.controllers;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import smartmunimji.backend.dtos.*;
import smartmunimji.backend.security.JwtUtil;
import smartmunimji.backend.services.CustomerService;

import java.util.List;

@RestController
@RequestMapping("/smart-munimji")
public class CustomerController {

    private static final Logger logger = LoggerFactory.getLogger(CustomerController.class);

    private final CustomerService customerService;
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;

    @Autowired
    public CustomerController(CustomerService customerService, JwtUtil jwtUtil, AuthenticationManager authenticationManager) {
        this.customerService = customerService;
        this.jwtUtil = jwtUtil;
        this.authenticationManager = authenticationManager;
    }

    @PostMapping("/auth/register/customer")
    public ResponseEntity<ResponseUtil<Long>> registerCustomer(@Valid @RequestBody CustomerRegistrationDTO request) {
        logger.debug("Registering customer with email: {}", request.email());
        Long userId = customerService.registerCustomer(request);
        return ResponseEntity.ok(new ResponseUtil<>("success", userId, "Registration successful"));
    }

    @PostMapping("/auth/authenticate")
    public ResponseEntity<ResponseUtil<String>> authenticate(@Valid @RequestBody LoginRequestDTO request) {
        logger.info("Authenticating customer with email: {}", request.email());
        try {
            Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                    request.email(),
                    request.password()
                )
            );
            SecurityContextHolder.getContext().setAuthentication(authentication);
            String token = jwtUtil.createToken(authentication);
            logger.info("Customer authenticated successfully: {}", request.email());
            return ResponseEntity.ok()
                    .header("Authorization", "Bearer " + token)
                    .body(new ResponseUtil<>("success", token, "Login successful"));
        } catch (BadCredentialsException e) {
            logger.error("Invalid credentials for customer: {}", request.email());
            return ResponseEntity.status(401)
                    .body(new ResponseUtil<>("error", null, "Invalid credentials"));
        }
    }

    @PutMapping("/users/{userId}")
    public ResponseEntity<ResponseUtil<String>> updateProfile(
            @PathVariable @Min(1) Long userId,
            @Valid @RequestBody CustomerUpdateDTO request) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        logger.debug("Updating profile for userId: {}", userId);
        customerService.updateCustomerProfile(userId, auth.getName(), request);
        return ResponseEntity.ok(new ResponseUtil<>("success", null, "Profile updated successfully"));
    }

    @PostMapping("/products/register")
    public ResponseEntity<ResponseUtil<ProductRegisterResponseDTO>> registerProduct(
            @Valid @RequestBody ProductRegistrationDTO request) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        logger.debug("Registering product for user: {}", auth.getName());
        ProductRegisterResponseDTO response = customerService.registerProduct(auth.getName(), request);
        return ResponseEntity.ok(new ResponseUtil<>("success", response, null));
    }

    @GetMapping("/products/registered")
    public ResponseEntity<ResponseUtil<List<RegisteredProductDTO>>> getRegisteredProducts() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        logger.debug("Fetching registered products for user: {}", auth.getName());
        List<RegisteredProductDTO> products = customerService.getRegisteredProducts(auth.getName());
        return ResponseEntity.ok(new ResponseUtil<>("success", products, null));
    }

    @PostMapping("/claims")
    public ResponseEntity<ResponseUtil<String>> submitWarrantyClaim(@Valid @RequestBody WarrantyClaimDTO request) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        logger.debug("Submitting warranty claim for user: {}", auth.getName());
        customerService.submitWarrantyClaim(auth.getName(), request);
        return ResponseEntity.ok(new ResponseUtil<>("success", null, "Warranty claim submitted successfully"));
    }

    @GetMapping("/claims/{claimId}")
    public ResponseEntity<ResponseUtil<WarrantyClaimResponseDTO>> getClaimStatus(@PathVariable @Min(1) Long claimId) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        logger.debug("Fetching claim status for claimId: {} by user: {}", claimId, auth.getName());
        WarrantyClaimResponseDTO claim = customerService.getClaimStatus(auth.getName(), claimId);
        return ResponseEntity.ok(new ResponseUtil<>("success", claim, null));
    }
}