package com.api.restaurant.infrastructure.persistence.jpa;

import com.api.restaurant.infrastructure.persistence.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SpringDataUserRepository extends JpaRepository<UserEntity, Long> {

	boolean existsByUserTypeId(Long userTypeId);

	boolean existsByEmailIgnoreCase(String email);

	boolean existsByLoginIgnoreCase(String login);

	boolean existsByEmailIgnoreCaseAndIdNot(String email, Long id);

	boolean existsByLoginIgnoreCaseAndIdNot(String login, Long id);
}
