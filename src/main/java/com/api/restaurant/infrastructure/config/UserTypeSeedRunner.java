package com.api.restaurant.infrastructure.config;

import com.api.restaurant.domain.model.UserType;
import com.api.restaurant.domain.model.UserTypeCategory;
import com.api.restaurant.domain.port.UserTypeRepository;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
public class UserTypeSeedRunner implements ApplicationRunner {

	private final UserTypeRepository userTypeRepository;

	public UserTypeSeedRunner(UserTypeRepository userTypeRepository) {
		this.userTypeRepository = userTypeRepository;
	}

	@Override
	public void run(ApplicationArguments args) {
		seedIfAbsent("Cliente", UserTypeCategory.CUSTOMER);
		seedIfAbsent("Dono", UserTypeCategory.RESTAURANT_STAFF);
		seedIfAbsent("Equipe", UserTypeCategory.RESTAURANT_STAFF);
	}

	private void seedIfAbsent(String name, UserTypeCategory category) {
		if (!userTypeRepository.existsByName(name)) {
			UserType type = new UserType();
			type.setName(name);
			type.setCategory(category);
			userTypeRepository.save(type);
		}
	}
}
