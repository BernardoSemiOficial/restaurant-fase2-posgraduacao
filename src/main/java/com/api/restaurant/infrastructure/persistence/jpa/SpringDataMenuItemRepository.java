package com.api.restaurant.infrastructure.persistence.jpa;

import com.api.restaurant.infrastructure.persistence.entity.MenuItemEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SpringDataMenuItemRepository extends JpaRepository<MenuItemEntity, Long> {

	List<MenuItemEntity> findByRestaurantId(Long restaurantId);

	Optional<MenuItemEntity> findByIdAndRestaurantId(Long id, Long restaurantId);

	void deleteByRestaurantId(Long restaurantId);
}
