package com.api.restaurant.presentation.mapper;

import com.api.restaurant.domain.model.MenuItem;
import com.api.restaurant.domain.model.Restaurant;
import com.api.restaurant.presentation.dto.AddressRequest;
import com.api.restaurant.presentation.dto.MenuItemRequest;
import com.api.restaurant.presentation.dto.OpeningHourRequest;
import com.api.restaurant.presentation.dto.RestaurantRequest;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class RestaurantDtoMapperTest {

	@Test
	void mapsRestaurantAndMenuItem() {
		RestaurantRequest request = new RestaurantRequest(
				"  Nome  ",
				new AddressRequest("01310-100", "Rua", "1", "SP City", "sp"),
				" Japonesa ",
				List.of(new OpeningHourRequest(DayOfWeek.FRIDAY, LocalTime.of(18, 0), LocalTime.of(23, 0))),
				9L);

		Restaurant restaurant = RestaurantDtoMapper.toDomain(request);
		assertEquals("Nome", restaurant.getName());
		assertEquals("SP", restaurant.getAddress().getState());
		assertEquals(DayOfWeek.FRIDAY, restaurant.getOpeningHours().getFirst().getDayOfWeek());

		MenuItem item = RestaurantDtoMapper.toMenuItem(
				new MenuItemRequest("Sushi", "Combo", new BigDecimal("80"), true, "/a.jpg"));
		assertEquals("Sushi", item.getName());
		assertTrue(item.isDineInOnly());
	}
}
