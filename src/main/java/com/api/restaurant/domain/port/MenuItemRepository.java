package com.api.restaurant.domain.port;

import com.api.restaurant.domain.model.MenuItem;

import java.util.List;
import java.util.Optional;

public interface MenuItemRepository {

	MenuItem save(MenuItem menuItem);

	Optional<MenuItem> findById(Long id);

	List<MenuItem> findByRestaurantId(Long restaurantId);

	Optional<MenuItem> findByIdAndRestaurantId(Long id, Long restaurantId);

	void deleteById(Long id);

	void deleteByRestaurantId(Long restaurantId);
}
