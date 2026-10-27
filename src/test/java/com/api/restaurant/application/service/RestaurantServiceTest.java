package com.api.restaurant.application.service;

import com.api.restaurant.domain.exception.BusinessConflictException;
import com.api.restaurant.domain.exception.ResourceNotFoundException;
import com.api.restaurant.domain.model.OpeningHour;
import com.api.restaurant.domain.model.Restaurant;
import com.api.restaurant.domain.model.UserTypeCategory;
import com.api.restaurant.domain.port.RestaurantRepository;
import com.api.restaurant.domain.port.UserRepository;
import com.api.restaurant.domain.port.UserTypeRepository;
import com.api.restaurant.support.TestEntities;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RestaurantServiceTest {

	@Mock
	private RestaurantRepository restaurantRepository;
	@Mock
	private UserRepository userRepository;
	@Mock
	private UserTypeRepository userTypeRepository;

	private RestaurantService service;

	@BeforeEach
	void setUp() {
		service = new RestaurantService(restaurantRepository, userRepository, userTypeRepository);
	}

	private void stubStaffOwner(Long ownerId) {
		when(userRepository.findById(ownerId)).thenReturn(Optional.of(TestEntities.user(ownerId, 2L)));
		when(userTypeRepository.findById(2L))
				.thenReturn(Optional.of(TestEntities.userType(2L, "Dono", UserTypeCategory.RESTAURANT_STAFF)));
	}

	@Test
	void createRequiresStaffOwner() {
		stubStaffOwner(7L);
		when(restaurantRepository.save(any())).thenAnswer(inv -> {
			Restaurant r = inv.getArgument(0);
			r.setId(1L);
			return r;
		});

		Restaurant created = service.create(TestEntities.restaurant(null, 7L));
		assertEquals(1L, created.getId());
	}

	@Test
	void createRejectsCustomerOwner() {
		when(userRepository.findById(7L)).thenReturn(Optional.of(TestEntities.user(7L, 1L)));
		when(userTypeRepository.findById(1L))
				.thenReturn(Optional.of(TestEntities.userType(1L, "Cliente", UserTypeCategory.CUSTOMER)));

		assertThrows(BusinessConflictException.class,
				() -> service.create(TestEntities.restaurant(null, 7L)));
	}

	@Test
	void createRejectsInvalidHours() {
		stubStaffOwner(7L);
		Restaurant restaurant = TestEntities.restaurant(null, 7L);
		OpeningHour bad = new OpeningHour();
		bad.setDayOfWeek(DayOfWeek.MONDAY);
		bad.setOpensAt(LocalTime.of(22, 0));
		bad.setClosesAt(LocalTime.of(11, 0));
		restaurant.setOpeningHours(List.of(bad));

		assertThrows(IllegalArgumentException.class, () -> service.create(restaurant));
	}

	@Test
	void updateAndDelete() {
		stubStaffOwner(7L);
		Restaurant existing = TestEntities.restaurant(3L, 7L);
		when(restaurantRepository.findById(3L)).thenReturn(Optional.of(existing));
		when(restaurantRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

		Restaurant changes = TestEntities.restaurant(null, 7L);
		changes.setName("Novo Nome");
		Restaurant updated = service.update(3L, changes);
		assertEquals("Novo Nome", updated.getName());

		service.delete(3L);
		verify(restaurantRepository).deleteById(3L);
	}

	@Test
	void findByIdMissing() {
		when(restaurantRepository.findById(99L)).thenReturn(Optional.empty());
		assertThrows(ResourceNotFoundException.class, () -> service.findById(99L));
	}

	@Test
	void findAll() {
		when(restaurantRepository.findAll()).thenReturn(List.of(TestEntities.restaurant(1L, 7L)));
		assertEquals(1, service.findAll().size());
	}
}
