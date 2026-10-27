package com.api.restaurant.support;

import com.api.restaurant.domain.model.Address;
import com.api.restaurant.domain.model.MenuItem;
import com.api.restaurant.domain.model.OpeningHour;
import com.api.restaurant.domain.model.Restaurant;
import com.api.restaurant.domain.model.User;
import com.api.restaurant.domain.model.UserType;
import com.api.restaurant.domain.model.UserTypeCategory;

import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.Instant;
import java.time.LocalTime;
import java.util.List;

public final class TestEntities {

	private TestEntities() {
	}

	public static UserType userType(Long id, String name, UserTypeCategory category) {
		UserType type = new UserType();
		type.setId(id);
		type.setName(name);
		type.setCategory(category);
		return type;
	}

	public static User user(Long id, Long userTypeId) {
		User user = new User();
		user.setId(id);
		user.setName("Nome");
		user.setEmail("user" + id + "@mail.com");
		user.setLogin("login" + id);
		user.setPassword("encoded");
		user.setUserTypeId(userTypeId);
		user.setUpdatedAt(Instant.now());
		return user;
	}

	public static Address address() {
		Address address = new Address();
		address.setZipCode("01310-100");
		address.setStreet("Av Paulista");
		address.setNumber("1000");
		address.setCity("São Paulo");
		address.setState("SP");
		return address;
	}

	public static OpeningHour mondayHours() {
		OpeningHour hour = new OpeningHour();
		hour.setDayOfWeek(DayOfWeek.MONDAY);
		hour.setOpensAt(LocalTime.of(11, 0));
		hour.setClosesAt(LocalTime.of(22, 0));
		return hour;
	}

	public static Restaurant restaurant(Long id, Long ownerId) {
		Restaurant restaurant = new Restaurant();
		restaurant.setId(id);
		restaurant.setName("Cantina");
		restaurant.setAddress(address());
		restaurant.setCuisineType("Italiana");
		restaurant.setOpeningHours(List.of(mondayHours()));
		restaurant.setOwnerId(ownerId);
		return restaurant;
	}

	public static MenuItem menuItem(Long id, Long restaurantId) {
		MenuItem item = new MenuItem();
		item.setId(id);
		item.setRestaurantId(restaurantId);
		item.setName("Pizza");
		item.setDescription("Mussarela");
		item.setPrice(new BigDecimal("45.90"));
		item.setDineInOnly(false);
		item.setPhotoPath("/photos/pizza.jpg");
		return item;
	}
}
