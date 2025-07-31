package smartmunimji.backend.dtos;

public record RegisteredProductDTO(
        Long registeredProductId,
        String productName,
        String shopName,
        String sellerOrderId,
        String dateOfPurchase,
        String warrantyValidUntil,
        Boolean isWarrantyEligible,
        Long daysRemaining
) {}