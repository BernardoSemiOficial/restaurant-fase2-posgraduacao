package com.api.restaurant.domain.port;

import com.api.restaurant.domain.model.UserType;

import java.util.List;
import java.util.Optional;

public interface UserTypeRepository {

	UserType save(UserType userType);

	Optional<UserType> findById(Long id);

	List<UserType> findAll();

	boolean existsByName(String name);

	boolean existsByNameAndIdNot(String name, Long id);

	void deleteById(Long id);
}
