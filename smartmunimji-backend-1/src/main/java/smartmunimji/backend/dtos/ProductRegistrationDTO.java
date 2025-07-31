package smartmunimji.backend.dtos;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record ProductRegistrationDTO(
        @Min(value = 1, message = "Seller ID must be positive")
        Long sellerId,
        @NotBlank(message = "Order ID is required")
        String orderId,
        @NotBlank(message = "Purchase date is required") @Pattern(regexp = "\\d{4}-\\d{2}-\\d{2}", message = "Purchase date must be in YYYY-MM-DD format")
        String purchaseDate
) {}