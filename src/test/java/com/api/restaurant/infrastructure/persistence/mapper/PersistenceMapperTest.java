package com.api.restaurant.infrastructure.persistence.mapper;

import com.api.restaurant.domain.model.Address;
import com.api.restaurant.domain.model.MenuItem;
import com.api.restaurant.domain.model.OpeningHour;
import com.api.restaurant.domain.model.Restaurant;
import com.api.restaurant.domain.model.User;
import com.api.restaurant.domain.model.UserType;
import com.api.restaurant.domain.model.UserTypeCategory;
import com.api.restaurant.infrastructure.persistence.entity.AddressEntity;
import com.api.restaurant.infrastructure.persistence.entity.MenuItemEntity;
import com.api.restaurant.infrastructure.persistence.entity.RestaurantEntity;
import com.api.restaurant.infrastructure.persistence.entity.UserEntity;
import com.api.restaurant.infrastructure.persistence.entity.UserTypeEntity;
import com.api.restaurant.support.TestEntities;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.Instant;
import java.time.LocalTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PersistenceMapperTest {

	@Test
	void mapsUserTypeRoundTrip() {
		UserType domain = TestEntities.userType(1L, "Dono", UserTypeCategory.RESTAURANT_STAFF);
		UserTypeEntity entity = PersistenceMapper.toEntity(domain);
		PersistenceMapper.updateEntity(entity, domain);
		UserType mapped = PersistenceMapper.toDomain(entity);
		assertEquals("Dono", mapped.getName());
		assertNull(PersistenceMapper.toDomain((UserTypeEntity) null));
	}

	@Test
	void mapsUserRoundTrip() {
		User domain = TestEntities.user(2L, 1L);
		UserEntity entity = PersistenceMapper.toEntity(domain);
		PersistenceMapper.updateEntity(entity, domain);
		assertEquals(domain.getEmail(), PersistenceMapper.toDomain(entity).getEmail());
		assertNull(PersistenceMapper.toDomain((UserEntity) null));
	}

	@Test
	void mapsRestaurantWithHours() {
		Restaurant domain = TestEntities.restaurant(3L, 7L);
		RestaurantEntity entity = PersistenceMapper.toEntity(domain);
		assertEquals(1, entity.getOpeningHours().size());
		assertEquals(DayOfWeek.MONDAY, entity.getOpeningHours().getFirst().getDayOfWeek());

		domain.setName("Atualizado");
		OpeningHour hour = new OpeningHour();
		hour.setDayOfWeek(DayOfWeek.TUESDAY);
		hour.setOpensAt(LocalTime.of(10, 0));
		hour.setClosesAt(LocalTime.of(20, 0));
		domain.setOpeningHours(List.of(hour));
		PersistenceMapper.updateEntity(entity, domain);

		Restaurant mapped = PersistenceMapper.toDomain(entity);
		assertEquals("Atualizado", mapped.getName());
		assertEquals(DayOfWeek.TUESDAY, mapped.getOpeningHours().getFirst().getDayOfWeek());
		assertNull(PersistenceMapper.toDomain((RestaurantEntity) null));
	}

	@Test
	void mapsAddressAndMenuItem() {
		Address address = TestEntities.address();
		AddressEntity addressEntity = PersistenceMapper.toEntity(address);
		PersistenceMapper.updateEntity(addressEntity, address);
		assertEquals("SP", PersistenceMapper.toDomain(addressEntity).getState());

		RestaurantEntity restaurant = new RestaurantEntity();
		restaurant.setId(1L);
		MenuItem item = TestEntities.menuItem(5L, 1L);
		MenuItemEntity menuEntity = PersistenceMapper.toEntity(item, restaurant);
		PersistenceMapper.updateEntity(menuEntity, item);
		MenuItem mapped = PersistenceMapper.toDomain(menuEntity);
		assertEquals(new BigDecimal("45.90"), mapped.getPrice());
		assertTrue(mapped.getName().contains("Pizza"));
	}

	@Test
	void mapsUserWithUpdatedAt() {
		UserEntity entity = new UserEntity();
		entity.setId(1L);
		entity.setName("A");
		entity.setEmail("a@a.com");
		entity.setLogin("a");
		entity.setPassword("x");
		entity.setUserTypeId(1L);
		entity.setUpdatedAt(Instant.parse("2026-01-01T00:00:00Z"));
		assertEquals(Instant.parse("2026-01-01T00:00:00Z"), PersistenceMapper.toDomain(entity).getUpdatedAt());
	}
}
