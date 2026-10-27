package com.api.restaurant.application.service;

import com.api.restaurant.domain.exception.BusinessConflictException;
import com.api.restaurant.domain.exception.ResourceNotFoundException;
import com.api.restaurant.domain.model.User;
import com.api.restaurant.domain.port.RestaurantRepository;
import com.api.restaurant.domain.port.UserRepository;
import com.api.restaurant.domain.port.UserTypeRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;

@Service
public class UserService {

	private final UserRepository userRepository;
	private final UserTypeRepository userTypeRepository;
	private final RestaurantRepository restaurantRepository;
	private final PasswordEncoder passwordEncoder;

	public UserService(
			UserRepository userRepository,
			UserTypeRepository userTypeRepository,
			RestaurantRepository restaurantRepository,
			PasswordEncoder passwordEncoder) {
		this.userRepository = userRepository;
		this.userTypeRepository = userTypeRepository;
		this.restaurantRepository = restaurantRepository;
		this.passwordEncoder = passwordEncoder;
	}

	@Transactional
	public User create(String name, String email, String login, String rawPassword, Long userTypeId) {
		ensureUserTypeExists(userTypeId);
		validateEmailUnique(email, null);
		validateLoginUnique(login, null);

		User user = new User();
		user.setName(name.trim());
		user.setEmail(email.trim());
		user.setLogin(login.trim());
		user.setPassword(passwordEncoder.encode(rawPassword));
		user.setUserTypeId(userTypeId);
		user.setUpdatedAt(Instant.now());
		return userRepository.save(user);
	}

	@Transactional(readOnly = true)
	public User findById(Long id) {
		return userRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado: " + id));
	}

	@Transactional(readOnly = true)
	public List<User> findAll() {
		return userRepository.findAll();
	}

	@Transactional
	public User update(Long id, String name, String email, String login) {
		User user = findById(id);
		validateEmailUnique(email, id);
		validateLoginUnique(login, id);
		user.setName(name.trim());
		user.setEmail(email.trim());
		user.setLogin(login.trim());
		user.setUpdatedAt(Instant.now());
		return userRepository.save(user);
	}

	@Transactional
	public User changeUserType(Long id, Long userTypeId) {
		User user = findById(id);
		ensureUserTypeExists(userTypeId);
		if (!user.getUserTypeId().equals(userTypeId) && restaurantRepository.existsByOwnerId(id)) {
			throw new BusinessConflictException(
					"Não é possível alterar o tipo: o usuário já está associado a restaurantes");
		}
		user.setUserTypeId(userTypeId);
		user.setUpdatedAt(Instant.now());
		return userRepository.save(user);
	}

	@Transactional
	public void delete(Long id) {
		findById(id);
		if (restaurantRepository.existsByOwnerId(id)) {
			throw new BusinessConflictException(
					"Não é possível excluir o usuário: ainda é dono de restaurante(s)");
		}
		userRepository.deleteById(id);
	}

	private void ensureUserTypeExists(Long userTypeId) {
		userTypeRepository.findById(userTypeId)
				.orElseThrow(() -> new ResourceNotFoundException("Tipo de usuário não encontrado: " + userTypeId));
	}

	private void validateEmailUnique(String email, Long id) {
		boolean exists = id == null
				? userRepository.existsByEmail(email.trim())
				: userRepository.existsByEmailAndIdNot(email.trim(), id);
		if (exists) {
			throw new BusinessConflictException("E-mail já cadastrado: " + email);
		}
	}

	private void validateLoginUnique(String login, Long id) {
		boolean exists = id == null
				? userRepository.existsByLogin(login.trim())
				: userRepository.existsByLoginAndIdNot(login.trim(), id);
		if (exists) {
			throw new BusinessConflictException("Login já cadastrado: " + login);
		}
	}
}
