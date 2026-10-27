package com.api.restaurant.domain.model;

import java.time.DayOfWeek;
import java.time.LocalTime;

public class OpeningHour {

	private DayOfWeek dayOfWeek;
	private LocalTime opensAt;
	private LocalTime closesAt;

	public DayOfWeek getDayOfWeek() {
		return dayOfWeek;
	}

	public void setDayOfWeek(DayOfWeek dayOfWeek) {
		this.dayOfWeek = dayOfWeek;
	}

	public LocalTime getOpensAt() {
		return opensAt;
	}

	public void setOpensAt(LocalTime opensAt) {
		this.opensAt = opensAt;
	}

	public LocalTime getClosesAt() {
		return closesAt;
	}

	public void setClosesAt(LocalTime closesAt) {
		this.closesAt = closesAt;
	}
}
