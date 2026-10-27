package com.api.restaurant.presentation.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record RestaurantRequest(
		@NotBlank String name,
		@NotNull @Valid AddressRequest address,
		@NotBlank String cuisineType,
		@NotEmpty List<@Valid OpeningHourRequest> openingHours,
		@NotNull Long ownerId
) {
}
