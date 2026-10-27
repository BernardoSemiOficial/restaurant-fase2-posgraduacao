package com.api.restaurant.presentation.dto;

import com.api.restaurant.domain.model.OpeningHour;

import java.time.DayOfWeek;
import java.time.LocalTime;

public record OpeningHourResponse(DayOfWeek dayOfWeek, LocalTime opensAt, LocalTime closesAt) {

	public static OpeningHourResponse from(OpeningHour hour) {
		return new OpeningHourResponse(hour.getDayOfWeek(), hour.getOpensAt(), hour.getClosesAt());
	}
}
