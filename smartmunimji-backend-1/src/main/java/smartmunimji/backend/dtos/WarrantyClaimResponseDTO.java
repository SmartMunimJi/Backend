package smartmunimji.backend.dtos;

public record WarrantyClaimResponseDTO(
        Long claimId,
        Long registeredProductId,
        String productName,
        String shopName,
        String issueDescription,
        String claimStatus,
        String sellerResponseNotes,
        String claimedAt,
        String lastStatusUpdateAt
) {}