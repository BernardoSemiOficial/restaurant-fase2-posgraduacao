package com.api.restaurant.presentation.controller;

import com.api.restaurant.application.service.MenuItemService;
import com.api.restaurant.presentation.dto.MenuItemRequest;
import com.api.restaurant.presentation.dto.MenuItemResponse;
import com.api.restaurant.presentation.mapper.RestaurantDtoMapper;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/restaurants/{restaurantId}/menu-items")
public class MenuItemController {

	private final MenuItemService menuItemService;

	public MenuItemController(MenuItemService menuItemService) {
		this.menuItemService = menuItemService;
	}

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public MenuItemResponse create(
			@PathVariable Long restaurantId,
			@Valid @RequestBody MenuItemRequest request) {
		return MenuItemResponse.from(
				menuItemService.create(restaurantId, RestaurantDtoMapper.toMenuItem(request)));
	}

	@GetMapping
	public List<MenuItemResponse> list(@PathVariable Long restaurantId) {
		return menuItemService.findByRestaurant(restaurantId).stream().map(MenuItemResponse::from).toList();
	}

	@GetMapping("/{id}")
	public MenuItemResponse get(@PathVariable Long restaurantId, @PathVariable Long id) {
		return MenuItemResponse.from(menuItemService.findById(restaurantId, id));
	}

	@PutMapping("/{id}")
	public MenuItemResponse update(
			@PathVariable Long restaurantId,
			@PathVariable Long id,
			@Valid @RequestBody MenuItemRequest request) {
		return MenuItemResponse.from(
				menuItemService.update(restaurantId, id, RestaurantDtoMapper.toMenuItem(request)));
	}

	@DeleteMapping("/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void delete(@PathVariable Long restaurantId, @PathVariable Long id) {
		menuItemService.delete(restaurantId, id);
	}
}
