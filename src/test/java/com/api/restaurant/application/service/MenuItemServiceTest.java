package com.api.restaurant.application.service;

import com.api.restaurant.domain.exception.ResourceNotFoundException;
import com.api.restaurant.domain.model.MenuItem;
import com.api.restaurant.domain.port.MenuItemRepository;
import com.api.restaurant.domain.port.RestaurantRepository;
import com.api.restaurant.support.TestEntities;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MenuItemServiceTest {

	@Mock
	private MenuItemRepository menuItemRepository;
	@Mock
	private RestaurantRepository restaurantRepository;

	private MenuItemService service;

	@BeforeEach
	void setUp() {
		service = new MenuItemService(menuItemRepository, restaurantRepository);
	}

	@Test
	void createLinksRestaurant() {
		when(restaurantRepository.findById(1L)).thenReturn(Optional.of(TestEntities.restaurant(1L, 7L)));
		when(menuItemRepository.save(any())).thenAnswer(inv -> {
			MenuItem item = inv.getArgument(0);
			item.setId(10L);
			return item;
		});

		MenuItem created = service.create(1L, TestEntities.menuItem(null, null));
		assertEquals(1L, created.getRestaurantId());
		assertEquals(10L, created.getId());
	}

	@Test
	void createRejectsNonPositivePrice() {
		when(restaurantRepository.findById(1L)).thenReturn(Optional.of(TestEntities.restaurant(1L, 7L)));
		MenuItem item = TestEntities.menuItem(null, 1L);
		item.setPrice(BigDecimal.ZERO);
		assertThrows(IllegalArgumentException.class, () -> service.create(1L, item));
	}

	@Test
	void findUpdateDelete() {
		when(restaurantRepository.findById(1L)).thenReturn(Optional.of(TestEntities.restaurant(1L, 7L)));
		when(menuItemRepository.findByIdAndRestaurantId(10L, 1L))
				.thenReturn(Optional.of(TestEntities.menuItem(10L, 1L)));
		when(menuItemRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

		assertEquals("Pizza", service.findById(1L, 10L).getName());

		MenuItem changes = TestEntities.menuItem(null, 1L);
		changes.setName("Pizza Especial");
		assertEquals("Pizza Especial", service.update(1L, 10L, changes).getName());

		service.delete(1L, 10L);
		verify(menuItemRepository).deleteById(10L);
	}

	@Test
	void listByRestaurant() {
		when(restaurantRepository.findById(1L)).thenReturn(Optional.of(TestEntities.restaurant(1L, 7L)));
		when(menuItemRepository.findByRestaurantId(1L)).thenReturn(List.of(TestEntities.menuItem(10L, 1L)));
		assertEquals(1, service.findByRestaurant(1L).size());
	}

	@Test
	void restaurantMissing() {
		when(restaurantRepository.findById(99L)).thenReturn(Optional.empty());
		assertThrows(ResourceNotFoundException.class, () -> service.findByRestaurant(99L));
	}
}
