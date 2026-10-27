package com.api.restaurant.domain.model;

import java.util.ArrayList;
import java.util.List;

public class Restaurant {

	private Long id;
	private String name;
	private Address address;
	private String cuisineType;
	private List<OpeningHour> openingHours = new ArrayList<>();
	private Long ownerId;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Address getAddress() {
		return address;
	}

	public void setAddress(Address address) {
		this.address = address;
	}

	public String getCuisineType() {
		return cuisineType;
	}

	public void setCuisineType(String cuisineType) {
		this.cuisineType = cuisineType;
	}

	public List<OpeningHour> getOpeningHours() {
		return openingHours;
	}

	public void setOpeningHours(List<OpeningHour> openingHours) {
		this.openingHours = openingHours != null ? openingHours : new ArrayList<>();
	}

	public Long getOwnerId() {
		return ownerId;
	}

	public void setOwnerId(Long ownerId) {
		this.ownerId = ownerId;
	}
}
