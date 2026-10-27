package com.api.restaurant.infrastructure.persistence.adapter;

import com.api.restaurant.domain.model.UserType;
import com.api.restaurant.domain.port.UserTypeRepository;
import com.api.restaurant.infrastructure.persistence.entity.UserTypeEntity;
import com.api.restaurant.infrastructure.persistence.jpa.SpringDataUserTypeRepository;
import com.api.restaurant.infrastructure.persistence.mapper.PersistenceMapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class UserTypeRepositoryAdapter implements UserTypeRepository {

	private final SpringDataUserTypeRepository jpa;

	public UserTypeRepositoryAdapter(SpringDataUserTypeRepository jpa) {
		this.jpa = jpa;
	}

	@Override
	public UserType save(UserType userType) {
		UserTypeEntity entity;
		if (userType.getId() != null) {
			entity = jpa.findById(userType.getId()).orElseGet(UserTypeEntity::new);
			PersistenceMapper.updateEntity(entity, userType);
		} else {
			entity = PersistenceMapper.toEntity(userType);
		}
		return PersistenceMapper.toDomain(jpa.save(entity));
	}

	@Override
	public Optional<UserType> findById(Long id) {
		return jpa.findById(id).map(PersistenceMapper::toDomain);
	}

	@Override
	public List<UserType> findAll() {
		return jpa.findAll().stream().map(PersistenceMapper::toDomain).toList();
	}

	@Override
	public boolean existsByName(String name) {
		return jpa.existsByNameIgnoreCase(name);
	}

	@Override
	public boolean existsByNameAndIdNot(String name, Long id) {
		return jpa.existsByNameIgnoreCaseAndIdNot(name, id);
	}

	@Override
	public void deleteById(Long id) {
		jpa.deleteById(id);
	}
}
