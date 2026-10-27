package com.api.restaurant.presentation.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record AddressRequest(
		@NotBlank @Size(max = 9) String zipCode,
		@NotBlank String street,
		@NotBlank String number,
		@NotBlank String city,
		@NotBlank @Size(min = 2, max = 2) String state
) {
}
