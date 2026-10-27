package com.api.restaurant.infrastructure.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import java.time.DayOfWeek;
import java.time.LocalTime;

@Entity
@Table(name = "opening_hour")
public class OpeningHourEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Enumerated(EnumType.STRING)
	@Column(name = "day_of_week", nullable = false, length = 16)
	private DayOfWeek dayOfWeek;

	@Column(name = "opens_at", nullable = false)
	private LocalTime opensAt;

	@Column(name = "closes_at", nullable = false)
	private LocalTime closesAt;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "restaurant_id", nullable = false)
	private RestaurantEntity restaurant;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

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

	public RestaurantEntity getRestaurant() {
		return restaurant;
	}

	public void setRestaurant(RestaurantEntity restaurant) {
		this.restaurant = restaurant;
	}
}
