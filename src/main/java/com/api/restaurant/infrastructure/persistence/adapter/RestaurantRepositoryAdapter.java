package com.api.restaurant.infrastructure.persistence.adapter;

import com.api.restaurant.domain.model.Restaurant;
import com.api.restaurant.domain.port.RestaurantRepository;
import com.api.restaurant.infrastructure.persistence.entity.RestaurantEntity;
import com.api.restaurant.infrastructure.persistence.jpa.SpringDataRestaurantRepository;
import com.api.restaurant.infrastructure.persistence.mapper.PersistenceMapper;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Component
public class RestaurantRepositoryAdapter implements RestaurantRepository {

	private final SpringDataRestaurantRepository jpa;

	public RestaurantRepositoryAdapter(SpringDataRestaurantRepository jpa) {
		this.jpa = jpa;
	}

	@Override
	@Transactional
	public Restaurant save(Restaurant restaurant) {
		RestaurantEntity entity;
		if (restaurant.getId() != null) {
			entity = jpa.findById(restaurant.getId()).orElseGet(RestaurantEntity::new);
			PersistenceMapper.updateEntity(entity, restaurant);
		} else {
			entity = PersistenceMapper.toEntity(restaurant);
		}
		return PersistenceMapper.toDomain(jpa.save(entity));
	}

	@Override
	@Transactional(readOnly = true)
	public Optional<Restaurant> findById(Long id) {
		return jpa.findById(id).map(PersistenceMapper::toDomain);
	}

	@Override
	@Transactional(readOnly = true)
	public List<Restaurant> findAll() {
		return jpa.findAll().stream().map(PersistenceMapper::toDomain).toList();
	}

	@Override
	public boolean existsByOwnerId(Long ownerId) {
		return jpa.existsByOwnerId(ownerId);
	}

	@Override
	public void deleteById(Long id) {
		jpa.deleteById(id);
	}
}
