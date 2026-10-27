package com.api.restaurant.domain.model;

import java.math.BigDecimal;

public class MenuItem {

	private Long id;
	private Long restaurantId;
	private String name;
	private String description;
	private BigDecimal price;
	private boolean dineInOnly;
	private String photoPath;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getRestaurantId() {
		return restaurantId;
	}

	public void setRestaurantId(Long restaurantId) {
		this.restaurantId = restaurantId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public boolean isDineInOnly() {
		return dineInOnly;
	}

	public void setDineInOnly(boolean dineInOnly) {
		this.dineInOnly = dineInOnly;
	}

	public String getPhotoPath() {
		return photoPath;
	}

	public void setPhotoPath(String photoPath) {
		this.photoPath = photoPath;
	}
}
