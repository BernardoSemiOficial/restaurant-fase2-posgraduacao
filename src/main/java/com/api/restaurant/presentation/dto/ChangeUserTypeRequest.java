package com.api.restaurant.presentation.dto;

import jakarta.validation.constraints.NotNull;

public record ChangeUserTypeRequest(@NotNull Long userTypeId) {
}
