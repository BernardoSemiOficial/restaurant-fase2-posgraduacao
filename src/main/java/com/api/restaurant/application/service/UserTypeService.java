package com.api.restaurant.application.service;

import com.api.restaurant.domain.exception.BusinessConflictException;
import com.api.restaurant.domain.exception.ResourceNotFoundException;
import com.api.restaurant.domain.model.UserType;
import com.api.restaurant.domain.model.UserTypeCategory;
import com.api.restaurant.domain.port.UserRepository;
import com.api.restaurant.domain.port.UserTypeRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UserTypeService {

	private final UserTypeRepository userTypeRepository;
	private final UserRepository userRepository;

	public UserTypeService(UserTypeRepository userTypeRepository, UserRepository userRepository) {
		this.userTypeRepository = userTypeRepository;
		this.userRepository = userRepository;
	}

	@Transactional
	public UserType create(String name, UserTypeCategory category) {
		validateNameUnique(name, null);
		UserType userType = new UserType();
		userType.setName(name.trim());
		userType.setCategory(category);
		return userTypeRepository.save(userType);
	}

	@Transactional(readOnly = true)
	public UserType findById(Long id) {
		return userTypeRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Tipo de usuário não encontrado: " + id));
	}

	@Transactional(readOnly = true)
	public List<UserType> findAll() {
		return userTypeRepository.findAll();
	}

	@Transactional
	public UserType update(Long id, String name, UserTypeCategory category) {
		UserType existing = findById(id);
		validateNameUnique(name, id);
		existing.setName(name.trim());
		existing.setCategory(category);
		return userTypeRepository.save(existing);
	}

	@Transactional
	public void delete(Long id) {
		findById(id);
		if (userRepository.existsByUserTypeId(id)) {
			throw new BusinessConflictException(
					"Não é possível excluir o tipo de usuário: ainda há usuários associados");
		}
		userTypeRepository.deleteById(id);
	}

	private void validateNameUnique(String name, Long id) {
		boolean exists = id == null
				? userTypeRepository.existsByName(name.trim())
				: userTypeRepository.existsByNameAndIdNot(name.trim(), id);
		if (exists) {
			throw new BusinessConflictException("Já existe um tipo de usuário com o nome: " + name);
		}
	}
}
