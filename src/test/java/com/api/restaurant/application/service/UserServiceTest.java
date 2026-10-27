package com.api.restaurant.application.service;

import com.api.restaurant.domain.exception.BusinessConflictException;
import com.api.restaurant.domain.exception.ResourceNotFoundException;
import com.api.restaurant.domain.model.User;
import com.api.restaurant.domain.model.UserTypeCategory;
import com.api.restaurant.domain.port.RestaurantRepository;
import com.api.restaurant.domain.port.UserRepository;
import com.api.restaurant.domain.port.UserTypeRepository;
import com.api.restaurant.support.TestEntities;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

	@Mock
	private UserRepository userRepository;
	@Mock
	private UserTypeRepository userTypeRepository;
	@Mock
	private RestaurantRepository restaurantRepository;
	@Mock
	private PasswordEncoder passwordEncoder;

	private UserService service;

	@BeforeEach
	void setUp() {
		service = new UserService(userRepository, userTypeRepository, restaurantRepository, passwordEncoder);
	}

	@Test
	void createEncodesPasswordAndLinksType() {
		when(userTypeRepository.findById(2L))
				.thenReturn(Optional.of(TestEntities.userType(2L, "Dono", UserTypeCategory.RESTAURANT_STAFF)));
		when(userRepository.existsByEmail("a@mail.com")).thenReturn(false);
		when(userRepository.existsByLogin("dono")).thenReturn(false);
		when(passwordEncoder.encode("secret1")).thenReturn("hash");
		when(userRepository.save(any())).thenAnswer(inv -> {
			User u = inv.getArgument(0);
			u.setId(10L);
			return u;
		});

		User created = service.create("Ana", "a@mail.com", "dono", "secret1", 2L);

		assertEquals(10L, created.getId());
		assertEquals("hash", created.getPassword());
		assertEquals(2L, created.getUserTypeId());
	}

	@Test
	void createFailsWhenTypeMissing() {
		when(userTypeRepository.findById(99L)).thenReturn(Optional.empty());
		assertThrows(ResourceNotFoundException.class,
				() -> service.create("Ana", "a@mail.com", "dono", "secret1", 99L));
	}

	@Test
	void createFailsWhenEmailDuplicated() {
		when(userTypeRepository.findById(1L))
				.thenReturn(Optional.of(TestEntities.userType(1L, "Cliente", UserTypeCategory.CUSTOMER)));
		when(userRepository.existsByEmail("a@mail.com")).thenReturn(true);
		assertThrows(BusinessConflictException.class,
				() -> service.create("Ana", "a@mail.com", "dono", "secret1", 1L));
	}

	@Test
	void changeUserTypeBlockedWhenOwnsRestaurant() {
		User user = TestEntities.user(5L, 2L);
		when(userRepository.findById(5L)).thenReturn(Optional.of(user));
		when(userTypeRepository.findById(1L))
				.thenReturn(Optional.of(TestEntities.userType(1L, "Cliente", UserTypeCategory.CUSTOMER)));
		when(restaurantRepository.existsByOwnerId(5L)).thenReturn(true);

		assertThrows(BusinessConflictException.class, () -> service.changeUserType(5L, 1L));
	}

	@Test
	void changeUserTypeAllowedWhenSameType() {
		User user = TestEntities.user(5L, 2L);
		when(userRepository.findById(5L)).thenReturn(Optional.of(user));
		when(userTypeRepository.findById(2L))
				.thenReturn(Optional.of(TestEntities.userType(2L, "Dono", UserTypeCategory.RESTAURANT_STAFF)));
		when(userRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

		User result = service.changeUserType(5L, 2L);
		assertEquals(2L, result.getUserTypeId());
	}

	@Test
	void updateChangesProfile() {
		User user = TestEntities.user(5L, 1L);
		when(userRepository.findById(5L)).thenReturn(Optional.of(user));
		when(userRepository.existsByEmailAndIdNot(anyString(), any())).thenReturn(false);
		when(userRepository.existsByLoginAndIdNot(anyString(), any())).thenReturn(false);
		when(userRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

		User updated = service.update(5L, "Nova", "nova@mail.com", "novologin");
		assertEquals("Nova", updated.getName());
		assertEquals("nova@mail.com", updated.getEmail());
	}

	@Test
	void deleteBlockedWhenOwnsRestaurant() {
		when(userRepository.findById(5L)).thenReturn(Optional.of(TestEntities.user(5L, 2L)));
		when(restaurantRepository.existsByOwnerId(5L)).thenReturn(true);
		assertThrows(BusinessConflictException.class, () -> service.delete(5L));
	}

	@Test
	void deleteSucceeds() {
		when(userRepository.findById(5L)).thenReturn(Optional.of(TestEntities.user(5L, 2L)));
		when(restaurantRepository.existsByOwnerId(5L)).thenReturn(false);
		service.delete(5L);
		verify(userRepository).deleteById(5L);
	}

	@Test
	void findAllAndFindById() {
		when(userRepository.findAll()).thenReturn(List.of(TestEntities.user(1L, 1L)));
		when(userRepository.findById(1L)).thenReturn(Optional.of(TestEntities.user(1L, 1L)));
		assertEquals(1, service.findAll().size());
		assertEquals(1L, service.findById(1L).getId());
	}
}
