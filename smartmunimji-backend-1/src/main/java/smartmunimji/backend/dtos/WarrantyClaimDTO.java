package smartmunimji.backend.dtos;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record WarrantyClaimDTO(
        @Min(value = 1, message = "Registered product ID must be positive")
        Long registeredProductId,
        @NotBlank(message = "Issue description is required") @Size(min = 10, max = 1000, message = "Issue description must be 10-1000 characters")
        String issueDescription
) {}