package com.api.restaurant.presentation.dto;

import com.api.restaurant.domain.model.MenuItem;

import java.math.BigDecimal;

public record MenuItemResponse(
		Long id,
		Long restaurantId,
		String name,
		String description,
		BigDecimal price,
		boolean dineInOnly,
		String photoPath
) {

	public static MenuItemResponse from(MenuItem item) {
		return new MenuItemResponse(
				item.getId(),
				item.getRestaurantId(),
				item.getName(),
				item.getDescription(),
				item.getPrice(),
				item.isDineInOnly(),
				item.getPhotoPath());
	}
}
