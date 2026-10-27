package com.api.restaurant.infrastructure.persistence.jpa;

import com.api.restaurant.infrastructure.persistence.entity.UserTypeEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SpringDataUserTypeRepository extends JpaRepository<UserTypeEntity, Long> {

	boolean existsByNameIgnoreCase(String name);

	boolean existsByNameIgnoreCaseAndIdNot(String name, Long id);
}
