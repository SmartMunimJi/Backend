package smartmunimji.backend.dtos;

public record ResponseUtil<T>(
        String status,
        T data,
        String message
) {}