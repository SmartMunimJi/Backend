package smartmunimji.backend.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CustomerRegistrationDTO(
        @NotBlank(message = "Name is required") @Size(min = 3, max = 50, message = "Name must be 3-50 characters")
        String name,
        @NotBlank(message = "Email is required") @Email(message = "Invalid email format")
        String email,
        @NotBlank(message = "Password is required") @Size(min = 8, max = 100, message = "Password must be 8-100 characters")
        String password,
        @NotBlank(message = "Phone number is required") @Size(max = 20, message = "Phone number must be up to 20 characters")
        String phoneNumber,
        @Size(max = 255, message = "Address must be up to 255 characters")
        String address
) {}