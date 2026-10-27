package com.api.restaurant.application.service;

import com.api.restaurant.domain.exception.ResourceNotFoundException;
import com.api.restaurant.domain.model.MenuItem;
import com.api.restaurant.domain.port.MenuItemRepository;
import com.api.restaurant.domain.port.RestaurantRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
public class MenuItemService {

	private final MenuItemRepository menuItemRepository;
	private final RestaurantRepository restaurantRepository;

	public MenuItemService(MenuItemRepository menuItemRepository, RestaurantRepository restaurantRepository) {
		this.menuItemRepository = menuItemRepository;
		this.restaurantRepository = restaurantRepository;
	}

	@Transactional
	public MenuItem create(Long restaurantId, MenuItem menuItem) {
		ensureRestaurantExists(restaurantId);
		validatePrice(menuItem.getPrice());
		menuItem.setRestaurantId(restaurantId);
		menuItem.setId(null);
		return menuItemRepository.save(menuItem);
	}

	@Transactional(readOnly = true)
	public MenuItem findById(Long restaurantId, Long id) {
		ensureRestaurantExists(restaurantId);
		return menuItemRepository.findByIdAndRestaurantId(id, restaurantId)
				.orElseThrow(() -> new ResourceNotFoundException(
						"Item do cardápio não encontrado: " + id + " no restaurante " + restaurantId));
	}

	@Transactional(readOnly = true)
	public List<MenuItem> findByRestaurant(Long restaurantId) {
		ensureRestaurantExists(restaurantId);
		return menuItemRepository.findByRestaurantId(restaurantId);
	}

	@Transactional
	public MenuItem update(Long restaurantId, Long id, MenuItem changes) {
		MenuItem existing = findById(restaurantId, id);
		validatePrice(changes.getPrice());
		existing.setName(changes.getName());
		existing.setDescription(changes.getDescription());
		existing.setPrice(changes.getPrice());
		existing.setDineInOnly(changes.isDineInOnly());
		existing.setPhotoPath(changes.getPhotoPath());
		return menuItemRepository.save(existing);
	}

	@Transactional
	public void delete(Long restaurantId, Long id) {
		findById(restaurantId, id);
		menuItemRepository.deleteById(id);
	}

	private void ensureRestaurantExists(Long restaurantId) {
		restaurantRepository.findById(restaurantId)
				.orElseThrow(() -> new ResourceNotFoundException("Restaurante não encontrado: " + restaurantId));
	}

	private void validatePrice(BigDecimal price) {
		if (price == null || price.signum() <= 0) {
			throw new IllegalArgumentException("Preço deve ser maior que zero");
		}
	}
}
