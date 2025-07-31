package smartmunimji.backend.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import smartmunimji.backend.dtos.ResponseUtil;

@RestController
@RequestMapping("/smart-munimji/seller")
public class SellerController {

    private static final Logger logger = LoggerFactory.getLogger(SellerController.class);

    @GetMapping("/dashboard")
    public ResponseEntity<ResponseUtil<String>> getSellerDashboard() {
        logger.debug("Accessing seller dashboard");
        return ResponseEntity.ok(new ResponseUtil<>("success", "Seller dashboard", null));
    }
}