package com.api.restaurant.infrastructure.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import java.math.BigDecimal;

@Entity
@Table(name = "menu_item")
public class MenuItemEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	private String name;

	@Column(nullable = false, length = 2000)
	private String description;

	@Column(nullable = false, precision = 12, scale = 2)
	private BigDecimal price;

	@Column(name = "dine_in_only", nullable = false)
	private boolean dineInOnly;

	@Column(name = "photo_path")
	private String photoPath;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "restaurant_id", nullable = false)
	private RestaurantEntity restaurant;

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

	public RestaurantEntity getRestaurant() {
		return restaurant;
	}

	public void setRestaurant(RestaurantEntity restaurant) {
		this.restaurant = restaurant;
	}
}
