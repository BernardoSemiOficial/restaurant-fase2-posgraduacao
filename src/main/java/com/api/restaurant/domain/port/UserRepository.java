package com.api.restaurant.domain.port;

import com.api.restaurant.domain.model.User;

import java.util.List;
import java.util.Optional;

public interface UserRepository {

	User save(User user);

	Optional<User> findById(Long id);

	List<User> findAll();

	boolean existsByUserTypeId(Long userTypeId);

	boolean existsByEmail(String email);

	boolean existsByLogin(String login);

	boolean existsByEmailAndIdNot(String email, Long id);

	boolean existsByLoginAndIdNot(String login, Long id);

	void deleteById(Long id);
}
