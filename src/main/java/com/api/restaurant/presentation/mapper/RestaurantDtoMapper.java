package com.api.restaurant.presentation.mapper;

import com.api.restaurant.domain.model.Address;
import com.api.restaurant.domain.model.MenuItem;
import com.api.restaurant.domain.model.OpeningHour;
import com.api.restaurant.domain.model.Restaurant;
import com.api.restaurant.presentation.dto.AddressRequest;
import com.api.restaurant.presentation.dto.MenuItemRequest;
import com.api.restaurant.presentation.dto.OpeningHourRequest;
import com.api.restaurant.presentation.dto.RestaurantRequest;

import java.util.List;

public final class RestaurantDtoMapper {

	private RestaurantDtoMapper() {
	}

	public static Restaurant toDomain(RestaurantRequest request) {
		Restaurant restaurant = new Restaurant();
		restaurant.setName(request.name().trim());
		restaurant.setCuisineType(request.cuisineType().trim());
		restaurant.setOwnerId(request.ownerId());
		restaurant.setAddress(toAddress(request.address()));
		restaurant.setOpeningHours(toOpeningHours(request.openingHours()));
		return restaurant;
	}

	public static Address toAddress(AddressRequest request) {
		Address address = new Address();
		address.setZipCode(request.zipCode().trim());
		address.setStreet(request.street().trim());
		address.setNumber(request.number().trim());
		address.setCity(request.city().trim());
		address.setState(request.state().trim().toUpperCase());
		return address;
	}

	public static List<OpeningHour> toOpeningHours(List<OpeningHourRequest> requests) {
		return requests.stream().map(item -> {
			OpeningHour hour = new OpeningHour();
			hour.setDayOfWeek(item.dayOfWeek());
			hour.setOpensAt(item.opensAt());
			hour.setClosesAt(item.closesAt());
			return hour;
		}).toList();
	}

	public static MenuItem toMenuItem(MenuItemRequest request) {
		MenuItem item = new MenuItem();
		item.setName(request.name().trim());
		item.setDescription(request.description().trim());
		item.setPrice(request.price());
		item.setDineInOnly(Boolean.TRUE.equals(request.dineInOnly()));
		item.setPhotoPath(request.photoPath());
		return item;
	}
}
