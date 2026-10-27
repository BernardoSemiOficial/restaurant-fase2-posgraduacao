package com.api.restaurant.presentation.dto;

import jakarta.validation.constraints.NotNull;

import java.time.DayOfWeek;
import java.time.LocalTime;

public record OpeningHourRequest(
		@NotNull DayOfWeek dayOfWeek,
		@NotNull LocalTime opensAt,
		@NotNull LocalTime closesAt
) {
}
