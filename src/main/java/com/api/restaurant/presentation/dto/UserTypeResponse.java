package com.api.restaurant.presentation.dto;

import com.api.restaurant.domain.model.UserType;
import com.api.restaurant.domain.model.UserTypeCategory;

public record UserTypeResponse(Long id, String name, UserTypeCategory category) {

	public static UserTypeResponse from(UserType userType) {
		return new UserTypeResponse(userType.getId(), userType.getName(), userType.getCategory());
	}
}
