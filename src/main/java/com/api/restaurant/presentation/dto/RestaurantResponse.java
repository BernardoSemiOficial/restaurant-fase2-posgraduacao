package com.api.restaurant.presentation.dto;

import com.api.restaurant.domain.model.Restaurant;

import java.util.List;

public record RestaurantResponse(
		Long id,
		String name,
		AddressResponse address,
		String cuisineType,
		List<OpeningHourResponse> openingHours,
		Long ownerId
) {

	public static RestaurantResponse from(Restaurant restaurant) {
		return new RestaurantResponse(
				restaurant.getId(),
				restaurant.getName(),
				AddressResponse.from(restaurant.getAddress()),
				restaurant.getCuisineType(),
				restaurant.getOpeningHours().stream().map(OpeningHourResponse::from).toList(),
				restaurant.getOwnerId());
	}
}
