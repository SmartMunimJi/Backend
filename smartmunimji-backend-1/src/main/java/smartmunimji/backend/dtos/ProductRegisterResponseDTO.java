package smartmunimji.backend.dtos;

public record ProductRegisterResponseDTO(
        String message,
        Long registeredProductId,
        String productName,
        String warrantyValidUntil
) {}