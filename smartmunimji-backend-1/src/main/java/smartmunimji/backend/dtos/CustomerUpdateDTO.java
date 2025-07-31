package smartmunimji.backend.dtos;

import jakarta.validation.constraints.Size;

public record CustomerUpdateDTO(
        @Size(min = 3, max = 50, message = "Name must be 3-50 characters")
        String name,
        @Size(max = 255, message = "Address must be up to 255 characters")
        String address
) {}