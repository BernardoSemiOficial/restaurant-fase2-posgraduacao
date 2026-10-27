package com.api.restaurant.infrastructure.persistence.jpa;

import com.api.restaurant.infrastructure.persistence.entity.RestaurantEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SpringDataRestaurantRepository extends JpaRepository<RestaurantEntity, Long> {

	boolean existsByOwnerId(Long ownerId);
}
