package com.api.restaurant.infrastructure.persistence.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "restaurant")
public class RestaurantEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	private String name;

	@OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
	@JoinColumn(name = "address_id", nullable = false)
	private AddressEntity address;

	@Column(name = "cuisine_type", nullable = false)
	private String cuisineType;

	@Column(name = "owner_id", nullable = false)
	private Long ownerId;

	@OneToMany(mappedBy = "restaurant", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<OpeningHourEntity> openingHours = new ArrayList<>();

	@OneToMany(mappedBy = "restaurant", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<MenuItemEntity> menuItems = new ArrayList<>();

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

	public AddressEntity getAddress() {
		return address;
	}

	public void setAddress(AddressEntity address) {
		this.address = address;
	}

	public String getCuisineType() {
		return cuisineType;
	}

	public void setCuisineType(String cuisineType) {
		this.cuisineType = cuisineType;
	}

	public Long getOwnerId() {
		return ownerId;
	}

	public void setOwnerId(Long ownerId) {
		this.ownerId = ownerId;
	}

	public List<OpeningHourEntity> getOpeningHours() {
		return openingHours;
	}

	public void setOpeningHours(List<OpeningHourEntity> openingHours) {
		this.openingHours = openingHours;
	}

	public List<MenuItemEntity> getMenuItems() {
		return menuItems;
	}

	public void setMenuItems(List<MenuItemEntity> menuItems) {
		this.menuItems = menuItems;
	}
}
