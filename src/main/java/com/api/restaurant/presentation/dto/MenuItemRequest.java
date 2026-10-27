package com.api.restaurant.presentation.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public record MenuItemRequest(
		@NotBlank String name,
		@NotBlank @Size(max = 2000) String description,
		@NotNull @Positive BigDecimal price,
		@NotNull Boolean dineInOnly,
		String photoPath
) {
}
