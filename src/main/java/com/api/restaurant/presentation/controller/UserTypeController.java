package com.api.restaurant.presentation.controller;

import com.api.restaurant.application.service.UserTypeService;
import com.api.restaurant.presentation.dto.UserTypeRequest;
import com.api.restaurant.presentation.dto.UserTypeResponse;
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
@RequestMapping("/api/v1/user-types")
public class UserTypeController {

	private final UserTypeService userTypeService;

	public UserTypeController(UserTypeService userTypeService) {
		this.userTypeService = userTypeService;
	}

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public UserTypeResponse create(@Valid @RequestBody UserTypeRequest request) {
		return UserTypeResponse.from(userTypeService.create(request.name(), request.category()));
	}

	@GetMapping
	public List<UserTypeResponse> list() {
		return userTypeService.findAll().stream().map(UserTypeResponse::from).toList();
	}

	@GetMapping("/{id}")
	public UserTypeResponse get(@PathVariable Long id) {
		return UserTypeResponse.from(userTypeService.findById(id));
	}

	@PutMapping("/{id}")
	public UserTypeResponse update(@PathVariable Long id, @Valid @RequestBody UserTypeRequest request) {
		return UserTypeResponse.from(userTypeService.update(id, request.name(), request.category()));
	}

	@DeleteMapping("/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void delete(@PathVariable Long id) {
		userTypeService.delete(id);
	}
}
