package com.api.restaurant.presentation.dto;

import com.api.restaurant.domain.model.Address;

public record AddressResponse(
		Long id,
		String zipCode,
		String street,
		String number,
		String city,
		String state
) {

	public static AddressResponse from(Address address) {
		return new AddressResponse(
				address.getId(),
				address.getZipCode(),
				address.getStreet(),
				address.getNumber(),
				address.getCity(),
				address.getState());
	}
}
