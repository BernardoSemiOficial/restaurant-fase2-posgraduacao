package com.api.restaurant.domain.model;

public class UserType {

	private Long id;
	private String name;
	private UserTypeCategory category;

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

	public UserTypeCategory getCategory() {
		return category;
	}

	public void setCategory(UserTypeCategory category) {
		this.category = category;
	}
}
