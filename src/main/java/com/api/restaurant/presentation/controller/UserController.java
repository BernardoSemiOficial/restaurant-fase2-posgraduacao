package com.api.restaurant.presentation.controller;

import com.api.restaurant.application.service.UserService;
import com.api.restaurant.presentation.dto.ChangeUserTypeRequest;
import com.api.restaurant.presentation.dto.CreateUserRequest;
import com.api.restaurant.presentation.dto.UpdateUserRequest;
import com.api.restaurant.presentation.dto.UserResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

	private final UserService userService;

	public UserController(UserService userService) {
		this.userService = userService;
	}

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public UserResponse create(@Valid @RequestBody CreateUserRequest request) {
		return UserResponse.from(userService.create(
				request.name(),
				request.email(),
				request.login(),
				request.password(),
				request.userTypeId()));
	}

	@GetMapping
	public List<UserResponse> list() {
		return userService.findAll().stream().map(UserResponse::from).toList();
	}

	@GetMapping("/{id}")
	public UserResponse get(@PathVariable Long id) {
		return UserResponse.from(userService.findById(id));
	}

	@PutMapping("/{id}")
	public UserResponse update(@PathVariable Long id, @Valid @RequestBody UpdateUserRequest request) {
		return UserResponse.from(userService.update(id, request.name(), request.email(), request.login()));
	}

	@PatchMapping("/{id}/user-type")
	public UserResponse changeUserType(
			@PathVariable Long id,
			@Valid @RequestBody ChangeUserTypeRequest request) {
		return UserResponse.from(userService.changeUserType(id, request.userTypeId()));
	}

	@DeleteMapping("/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void delete(@PathVariable Long id) {
		userService.delete(id);
	}
}
