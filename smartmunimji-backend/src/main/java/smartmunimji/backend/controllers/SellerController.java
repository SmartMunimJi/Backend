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
import smartmunimji.backend.daos.SellerDao;
import smartmunimji.backend.dtos.*;
import smartmunimji.backend.entities.Seller;
import smartmunimji.backend.security.JwtUtil;

import java.util.Optional;

@RestController
@RequestMapping("/sm/seller")
public class SellerController {

    private static final Logger logger = LoggerFactory.getLogger(SellerController.class);

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private SellerDao sellerDao;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/register")
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

    @PostMapping("/authenticate")
    public ResponseEntity<String> authenticateSeller(@RequestBody SellerAuthRequest sellerRequest) {
        logger.info("Authenticating seller with email: {}", sellerRequest.getSellersEmail());
        try {
            Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                    sellerRequest.getSellersEmail(),
                    sellerRequest.getPassword()
                )
            );
            String token = jwtUtil.createToken(authentication);
            logger.info("Seller authenticated successfully: {}", sellerRequest.getSellersEmail());
            return ResponseEntity.ok(token);
        } catch (BadCredentialsException e) {
            logger.error("Invalid credentials for seller: {}", sellerRequest.getSellersEmail());
            return ResponseEntity.status(401).body("Invalid credentials");
        }
    }
}