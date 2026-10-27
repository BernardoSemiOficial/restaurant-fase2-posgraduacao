package com.api.restaurant.application.service;

import com.api.restaurant.domain.exception.BusinessConflictException;
import com.api.restaurant.domain.exception.ResourceNotFoundException;
import com.api.restaurant.domain.model.UserType;
import com.api.restaurant.domain.model.UserTypeCategory;
import com.api.restaurant.domain.port.UserRepository;
import com.api.restaurant.domain.port.UserTypeRepository;
import com.api.restaurant.support.TestEntities;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserTypeServiceTest {

	@Mock
	private UserTypeRepository userTypeRepository;
	@Mock
	private UserRepository userRepository;

	private UserTypeService service;

	@BeforeEach
	void setUp() {
		service = new UserTypeService(userTypeRepository, userRepository);
	}

	@Test
	void createPersistsTrimmedName() {
		when(userTypeRepository.existsByName("Dono")).thenReturn(false);
		when(userTypeRepository.save(any())).thenAnswer(inv -> {
			UserType saved = inv.getArgument(0);
			saved.setId(1L);
			return saved;
		});

		UserType result = service.create("  Dono  ", UserTypeCategory.RESTAURANT_STAFF);

		ArgumentCaptor<UserType> captor = ArgumentCaptor.forClass(UserType.class);
		verify(userTypeRepository).save(captor.capture());
		assertEquals("Dono", captor.getValue().getName());
		assertEquals(UserTypeCategory.RESTAURANT_STAFF, result.getCategory());
		assertEquals(1L, result.getId());
	}

	@Test
	void createRejectsDuplicateName() {
		when(userTypeRepository.existsByName("Cliente")).thenReturn(true);
		assertThrows(BusinessConflictException.class,
				() -> service.create("Cliente", UserTypeCategory.CUSTOMER));
	}

	@Test
	void findByIdThrowsWhenMissing() {
		when(userTypeRepository.findById(9L)).thenReturn(Optional.empty());
		assertThrows(ResourceNotFoundException.class, () -> service.findById(9L));
	}

	@Test
	void findAllDelegates() {
		when(userTypeRepository.findAll()).thenReturn(List.of(
				TestEntities.userType(1L, "Cliente", UserTypeCategory.CUSTOMER)));
		assertEquals(1, service.findAll().size());
	}

	@Test
	void updateChangesFields() {
		UserType existing = TestEntities.userType(1L, "Old", UserTypeCategory.CUSTOMER);
		when(userTypeRepository.findById(1L)).thenReturn(Optional.of(existing));
		when(userTypeRepository.existsByNameAndIdNot("Novo", 1L)).thenReturn(false);
		when(userTypeRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

		UserType updated = service.update(1L, "Novo", UserTypeCategory.RESTAURANT_STAFF);

		assertEquals("Novo", updated.getName());
		assertEquals(UserTypeCategory.RESTAURANT_STAFF, updated.getCategory());
	}

	@Test
	void deleteBlockedWhenUsersExist() {
		when(userTypeRepository.findById(1L))
				.thenReturn(Optional.of(TestEntities.userType(1L, "Cliente", UserTypeCategory.CUSTOMER)));
		when(userRepository.existsByUserTypeId(1L)).thenReturn(true);
		assertThrows(BusinessConflictException.class, () -> service.delete(1L));
	}

	@Test
	void deleteSucceedsWhenUnused() {
		when(userTypeRepository.findById(1L))
				.thenReturn(Optional.of(TestEntities.userType(1L, "Cliente", UserTypeCategory.CUSTOMER)));
		when(userRepository.existsByUserTypeId(1L)).thenReturn(false);
		service.delete(1L);
		verify(userTypeRepository).deleteById(1L);
	}
}
