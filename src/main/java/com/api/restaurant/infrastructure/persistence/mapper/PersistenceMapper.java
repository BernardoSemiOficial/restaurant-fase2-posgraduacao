package com.api.restaurant.infrastructure.persistence.mapper;

import com.api.restaurant.domain.model.Address;
import com.api.restaurant.domain.model.MenuItem;
import com.api.restaurant.domain.model.OpeningHour;
import com.api.restaurant.domain.model.Restaurant;
import com.api.restaurant.domain.model.User;
import com.api.restaurant.domain.model.UserType;
import com.api.restaurant.infrastructure.persistence.entity.AddressEntity;
import com.api.restaurant.infrastructure.persistence.entity.MenuItemEntity;
import com.api.restaurant.infrastructure.persistence.entity.OpeningHourEntity;
import com.api.restaurant.infrastructure.persistence.entity.RestaurantEntity;
import com.api.restaurant.infrastructure.persistence.entity.UserEntity;
import com.api.restaurant.infrastructure.persistence.entity.UserTypeEntity;

import java.util.List;

public final class PersistenceMapper {

	private PersistenceMapper() {
	}

	public static UserType toDomain(UserTypeEntity entity) {
		if (entity == null) {
			return null;
		}
		UserType domain = new UserType();
		domain.setId(entity.getId());
		domain.setName(entity.getName());
		domain.setCategory(entity.getCategory());
		return domain;
	}

	public static UserTypeEntity toEntity(UserType domain) {
		UserTypeEntity entity = new UserTypeEntity();
		entity.setId(domain.getId());
		entity.setName(domain.getName());
		entity.setCategory(domain.getCategory());
		return entity;
	}

	public static void updateEntity(UserTypeEntity entity, UserType domain) {
		entity.setName(domain.getName());
		entity.setCategory(domain.getCategory());
	}

	public static User toDomain(UserEntity entity) {
		if (entity == null) {
			return null;
		}
		User domain = new User();
		domain.setId(entity.getId());
		domain.setName(entity.getName());
		domain.setEmail(entity.getEmail());
		domain.setLogin(entity.getLogin());
		domain.setPassword(entity.getPassword());
		domain.setUserTypeId(entity.getUserTypeId());
		domain.setUpdatedAt(entity.getUpdatedAt());
		return domain;
	}

	public static UserEntity toEntity(User domain) {
		UserEntity entity = new UserEntity();
		entity.setId(domain.getId());
		entity.setName(domain.getName());
		entity.setEmail(domain.getEmail());
		entity.setLogin(domain.getLogin());
		entity.setPassword(domain.getPassword());
		entity.setUserTypeId(domain.getUserTypeId());
		entity.setUpdatedAt(domain.getUpdatedAt());
		return entity;
	}

	public static void updateEntity(UserEntity entity, User domain) {
		entity.setName(domain.getName());
		entity.setEmail(domain.getEmail());
		entity.setLogin(domain.getLogin());
		entity.setPassword(domain.getPassword());
		entity.setUserTypeId(domain.getUserTypeId());
		entity.setUpdatedAt(domain.getUpdatedAt());
	}

	public static Address toDomain(AddressEntity entity) {
		if (entity == null) {
			return null;
		}
		Address domain = new Address();
		domain.setId(entity.getId());
		domain.setZipCode(entity.getZipCode());
		domain.setStreet(entity.getStreet());
		domain.setNumber(entity.getNumber());
		domain.setCity(entity.getCity());
		domain.setState(entity.getState());
		return domain;
	}

	public static AddressEntity toEntity(Address domain) {
		AddressEntity entity = new AddressEntity();
		entity.setId(domain.getId());
		entity.setZipCode(domain.getZipCode());
		entity.setStreet(domain.getStreet());
		entity.setNumber(domain.getNumber());
		entity.setCity(domain.getCity());
		entity.setState(domain.getState());
		return entity;
	}

	public static void updateEntity(AddressEntity entity, Address domain) {
		entity.setZipCode(domain.getZipCode());
		entity.setStreet(domain.getStreet());
		entity.setNumber(domain.getNumber());
		entity.setCity(domain.getCity());
		entity.setState(domain.getState());
	}

	public static OpeningHour toDomain(OpeningHourEntity entity) {
		OpeningHour domain = new OpeningHour();
		domain.setDayOfWeek(entity.getDayOfWeek());
		domain.setOpensAt(entity.getOpensAt());
		domain.setClosesAt(entity.getClosesAt());
		return domain;
	}

	public static Restaurant toDomain(RestaurantEntity entity) {
		if (entity == null) {
			return null;
		}
		Restaurant domain = new Restaurant();
		domain.setId(entity.getId());
		domain.setName(entity.getName());
		domain.setAddress(toDomain(entity.getAddress()));
		domain.setCuisineType(entity.getCuisineType());
		domain.setOwnerId(entity.getOwnerId());
		domain.setOpeningHours(entity.getOpeningHours().stream().map(PersistenceMapper::toDomain).toList());
		return domain;
	}

	public static RestaurantEntity toEntity(Restaurant domain) {
		RestaurantEntity entity = new RestaurantEntity();
		entity.setId(domain.getId());
		entity.setName(domain.getName());
		entity.setAddress(toEntity(domain.getAddress()));
		entity.setCuisineType(domain.getCuisineType());
		entity.setOwnerId(domain.getOwnerId());
		replaceOpeningHours(entity, domain.getOpeningHours());
		return entity;
	}

	public static void updateEntity(RestaurantEntity entity, Restaurant domain) {
		entity.setName(domain.getName());
		if (entity.getAddress() == null) {
			entity.setAddress(toEntity(domain.getAddress()));
		} else {
			updateEntity(entity.getAddress(), domain.getAddress());
		}
		entity.setCuisineType(domain.getCuisineType());
		entity.setOwnerId(domain.getOwnerId());
		replaceOpeningHours(entity, domain.getOpeningHours());
	}

	private static void replaceOpeningHours(RestaurantEntity entity, List<OpeningHour> hours) {
		entity.getOpeningHours().clear();
		if (hours == null) {
			return;
		}
		for (OpeningHour hour : hours) {
			OpeningHourEntity hourEntity = new OpeningHourEntity();
			hourEntity.setDayOfWeek(hour.getDayOfWeek());
			hourEntity.setOpensAt(hour.getOpensAt());
			hourEntity.setClosesAt(hour.getClosesAt());
			hourEntity.setRestaurant(entity);
			entity.getOpeningHours().add(hourEntity);
		}
	}

	public static MenuItem toDomain(MenuItemEntity entity) {
		if (entity == null) {
			return null;
		}
		MenuItem domain = new MenuItem();
		domain.setId(entity.getId());
		domain.setRestaurantId(entity.getRestaurant().getId());
		domain.setName(entity.getName());
		domain.setDescription(entity.getDescription());
		domain.setPrice(entity.getPrice());
		domain.setDineInOnly(entity.isDineInOnly());
		domain.setPhotoPath(entity.getPhotoPath());
		return domain;
	}

	public static MenuItemEntity toEntity(MenuItem domain, RestaurantEntity restaurant) {
		MenuItemEntity entity = new MenuItemEntity();
		entity.setId(domain.getId());
		entity.setName(domain.getName());
		entity.setDescription(domain.getDescription());
		entity.setPrice(domain.getPrice());
		entity.setDineInOnly(domain.isDineInOnly());
		entity.setPhotoPath(domain.getPhotoPath());
		entity.setRestaurant(restaurant);
		return entity;
	}

	public static void updateEntity(MenuItemEntity entity, MenuItem domain) {
		entity.setName(domain.getName());
		entity.setDescription(domain.getDescription());
		entity.setPrice(domain.getPrice());
		entity.setDineInOnly(domain.isDineInOnly());
		entity.setPhotoPath(domain.getPhotoPath());
	}
}
