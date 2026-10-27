package com.api.restaurant.presentation.controller;

import com.api.restaurant.application.service.RestaurantService;
import com.api.restaurant.presentation.dto.RestaurantRequest;
import com.api.restaurant.presentation.dto.RestaurantResponse;
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
@RequestMapping("/api/v1/restaurants")
public class RestaurantController {

	private final RestaurantService restaurantService;

	public RestaurantController(RestaurantService restaurantService) {
		this.restaurantService = restaurantService;
	}

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public RestaurantResponse create(@Valid @RequestBody RestaurantRequest request) {
		return RestaurantResponse.from(restaurantService.create(RestaurantDtoMapper.toDomain(request)));
	}

	@GetMapping
	public List<RestaurantResponse> list() {
		return restaurantService.findAll().stream().map(RestaurantResponse::from).toList();
	}

	@GetMapping("/{id}")
	public RestaurantResponse get(@PathVariable Long id) {
		return RestaurantResponse.from(restaurantService.findById(id));
	}

	@PutMapping("/{id}")
	public RestaurantResponse update(@PathVariable Long id, @Valid @RequestBody RestaurantRequest request) {
		return RestaurantResponse.from(restaurantService.update(id, RestaurantDtoMapper.toDomain(request)));
	}

	@DeleteMapping("/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void delete(@PathVariable Long id) {
		restaurantService.delete(id);
	}
}
