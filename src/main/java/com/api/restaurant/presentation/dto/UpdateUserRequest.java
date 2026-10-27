package com.api.restaurant.presentation.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UpdateUserRequest(
		@NotBlank @Size(max = 255) String name,
		@NotBlank @Email String email,
		@NotBlank @Size(max = 100) String login
) {
}
