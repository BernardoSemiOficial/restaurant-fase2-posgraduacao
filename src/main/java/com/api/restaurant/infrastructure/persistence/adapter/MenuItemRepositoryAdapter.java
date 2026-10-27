package com.api.restaurant.infrastructure.persistence.adapter;

import com.api.restaurant.domain.exception.ResourceNotFoundException;
import com.api.restaurant.domain.model.MenuItem;
import com.api.restaurant.domain.port.MenuItemRepository;
import com.api.restaurant.infrastructure.persistence.entity.MenuItemEntity;
import com.api.restaurant.infrastructure.persistence.entity.RestaurantEntity;
import com.api.restaurant.infrastructure.persistence.jpa.SpringDataMenuItemRepository;
import com.api.restaurant.infrastructure.persistence.jpa.SpringDataRestaurantRepository;
import com.api.restaurant.infrastructure.persistence.mapper.PersistenceMapper;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Component
public class MenuItemRepositoryAdapter implements MenuItemRepository {

	private final SpringDataMenuItemRepository jpa;
	private final SpringDataRestaurantRepository restaurantJpa;

	public MenuItemRepositoryAdapter(
			SpringDataMenuItemRepository jpa,
			SpringDataRestaurantRepository restaurantJpa) {
		this.jpa = jpa;
		this.restaurantJpa = restaurantJpa;
	}

	@Override
	@Transactional
	public MenuItem save(MenuItem menuItem) {
		RestaurantEntity restaurant = restaurantJpa.findById(menuItem.getRestaurantId())
				.orElseThrow(() -> new ResourceNotFoundException(
						"Restaurante não encontrado: " + menuItem.getRestaurantId()));

		MenuItemEntity entity;
		if (menuItem.getId() != null) {
			entity = jpa.findById(menuItem.getId()).orElseGet(MenuItemEntity::new);
			PersistenceMapper.updateEntity(entity, menuItem);
			entity.setRestaurant(restaurant);
		} else {
			entity = PersistenceMapper.toEntity(menuItem, restaurant);
		}
		return PersistenceMapper.toDomain(jpa.save(entity));
	}

	@Override
	@Transactional(readOnly = true)
	public Optional<MenuItem> findById(Long id) {
		return jpa.findById(id).map(PersistenceMapper::toDomain);
	}

	@Override
	@Transactional(readOnly = true)
	public List<MenuItem> findByRestaurantId(Long restaurantId) {
		return jpa.findByRestaurantId(restaurantId).stream().map(PersistenceMapper::toDomain).toList();
	}

	@Override
	@Transactional(readOnly = true)
	public Optional<MenuItem> findByIdAndRestaurantId(Long id, Long restaurantId) {
		return jpa.findByIdAndRestaurantId(id, restaurantId).map(PersistenceMapper::toDomain);
	}

	@Override
	public void deleteById(Long id) {
		jpa.deleteById(id);
	}

	@Override
	public void deleteByRestaurantId(Long restaurantId) {
		jpa.deleteByRestaurantId(restaurantId);
	}
}
