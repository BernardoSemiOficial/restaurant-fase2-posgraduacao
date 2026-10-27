package com.api.restaurant.presentation.dto;

import com.api.restaurant.domain.model.User;

import java.time.Instant;

public record UserResponse(
		Long id,
		String name,
		String email,
		String login,
		Long userTypeId,
		Instant updatedAt
) {

	public static UserResponse from(User user) {
		return new UserResponse(
				user.getId(),
				user.getName(),
				user.getEmail(),
				user.getLogin(),
				user.getUserTypeId(),
				user.getUpdatedAt());
	}
}
