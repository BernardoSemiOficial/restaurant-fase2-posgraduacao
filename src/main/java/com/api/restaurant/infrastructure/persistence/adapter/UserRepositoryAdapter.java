package com.api.restaurant.infrastructure.persistence.adapter;

import com.api.restaurant.domain.model.User;
import com.api.restaurant.domain.port.UserRepository;
import com.api.restaurant.infrastructure.persistence.entity.UserEntity;
import com.api.restaurant.infrastructure.persistence.jpa.SpringDataUserRepository;
import com.api.restaurant.infrastructure.persistence.mapper.PersistenceMapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class UserRepositoryAdapter implements UserRepository {

	private final SpringDataUserRepository jpa;

	public UserRepositoryAdapter(SpringDataUserRepository jpa) {
		this.jpa = jpa;
	}

	@Override
	public User save(User user) {
		UserEntity entity;
		if (user.getId() != null) {
			entity = jpa.findById(user.getId()).orElseGet(UserEntity::new);
			PersistenceMapper.updateEntity(entity, user);
		} else {
			entity = PersistenceMapper.toEntity(user);
		}
		return PersistenceMapper.toDomain(jpa.save(entity));
	}

	@Override
	public Optional<User> findById(Long id) {
		return jpa.findById(id).map(PersistenceMapper::toDomain);
	}

	@Override
	public List<User> findAll() {
		return jpa.findAll().stream().map(PersistenceMapper::toDomain).toList();
	}

	@Override
	public boolean existsByUserTypeId(Long userTypeId) {
		return jpa.existsByUserTypeId(userTypeId);
	}

	@Override
	public boolean existsByEmail(String email) {
		return jpa.existsByEmailIgnoreCase(email);
	}

	@Override
	public boolean existsByLogin(String login) {
		return jpa.existsByLoginIgnoreCase(login);
	}

	@Override
	public boolean existsByEmailAndIdNot(String email, Long id) {
		return jpa.existsByEmailIgnoreCaseAndIdNot(email, id);
	}

	@Override
	public boolean existsByLoginAndIdNot(String login, Long id) {
		return jpa.existsByLoginIgnoreCaseAndIdNot(login, id);
	}

	@Override
	public void deleteById(Long id) {
		jpa.deleteById(id);
	}
}
