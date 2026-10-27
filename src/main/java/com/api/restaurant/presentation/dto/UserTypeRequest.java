package com.api.restaurant.presentation.dto;

import com.api.restaurant.domain.model.UserTypeCategory;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record UserTypeRequest(
		@NotBlank @Size(max = 100) String name,
		@NotNull UserTypeCategory category
) {
}
