package smartmunimji.backend.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import smartmunimji.backend.entities.Seller;

import java.math.BigDecimal;

@Service
public class SellerService {

    private static final Logger logger = LoggerFactory.getLogger(SellerService.class);

    public ProductDetails validateOrderWithSeller(Seller seller, String orderId, String phoneNumber) {
        logger.debug("Validating order {} for seller {} with phone {}", orderId, seller.getSellerId(), phoneNumber);
        // Mock implementation; replace with actual HTTP call in production
        if (orderId.equals("ORDER123") && phoneNumber.equals("+919876543210")) {
            return new ProductDetails(true, "Sample Product", new BigDecimal("999.99"), 12);
        }
        return new ProductDetails(false, null, null, 0);
    }

    public record ProductDetails(
            boolean isValid,
            String productName,
            BigDecimal productPrice,
            int warrantyPeriodMonths
    ) {}
}