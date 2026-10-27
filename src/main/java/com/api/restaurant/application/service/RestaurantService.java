package com.api.restaurant.application.service;

import com.api.restaurant.domain.exception.BusinessConflictException;
import com.api.restaurant.domain.exception.ResourceNotFoundException;
import com.api.restaurant.domain.model.Address;
import com.api.restaurant.domain.model.OpeningHour;
import com.api.restaurant.domain.model.Restaurant;
import com.api.restaurant.domain.model.User;
import com.api.restaurant.domain.model.UserType;
import com.api.restaurant.domain.model.UserTypeCategory;
import com.api.restaurant.domain.port.RestaurantRepository;
import com.api.restaurant.domain.port.UserRepository;
import com.api.restaurant.domain.port.UserTypeRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class RestaurantService {

	private final RestaurantRepository restaurantRepository;
	private final UserRepository userRepository;
	private final UserTypeRepository userTypeRepository;

	public RestaurantService(
			RestaurantRepository restaurantRepository,
			UserRepository userRepository,
			UserTypeRepository userTypeRepository) {
		this.restaurantRepository = restaurantRepository;
		this.userRepository = userRepository;
		this.userTypeRepository = userTypeRepository;
	}

	@Transactional
	public Restaurant create(Restaurant restaurant) {
		validateOwnerIsStaff(restaurant.getOwnerId());
		validateOpeningHours(restaurant.getOpeningHours());
		return restaurantRepository.save(restaurant);
	}

	@Transactional(readOnly = true)
	public Restaurant findById(Long id) {
		return restaurantRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Restaurante não encontrado: " + id));
	}

	@Transactional(readOnly = true)
	public List<Restaurant> findAll() {
		return restaurantRepository.findAll();
	}

	@Transactional
	public Restaurant update(Long id, Restaurant changes) {
		Restaurant existing = findById(id);
		validateOwnerIsStaff(changes.getOwnerId());
		validateOpeningHours(changes.getOpeningHours());

		existing.setName(changes.getName());
		existing.setCuisineType(changes.getCuisineType());
		existing.setOwnerId(changes.getOwnerId());
		existing.setOpeningHours(changes.getOpeningHours());

		Address address = existing.getAddress();
		if (address == null) {
			address = new Address();
			existing.setAddress(address);
		}
		Address incoming = changes.getAddress();
		address.setZipCode(incoming.getZipCode());
		address.setStreet(incoming.getStreet());
		address.setNumber(incoming.getNumber());
		address.setCity(incoming.getCity());
		address.setState(incoming.getState());

		return restaurantRepository.save(existing);
	}

	@Transactional
	public void delete(Long id) {
		findById(id);
		restaurantRepository.deleteById(id);
	}

	private void validateOwnerIsStaff(Long ownerId) {
		User owner = userRepository.findById(ownerId)
				.orElseThrow(() -> new ResourceNotFoundException("Dono não encontrado: " + ownerId));
		UserType type = userTypeRepository.findById(owner.getUserTypeId())
				.orElseThrow(() -> new ResourceNotFoundException(
						"Tipo de usuário do dono não encontrado: " + owner.getUserTypeId()));
		if (type.getCategory() != UserTypeCategory.RESTAURANT_STAFF) {
			throw new BusinessConflictException(
					"O responsável pelo restaurante deve pertencer à categoria RESTAURANT_STAFF");
		}
	}

	private void validateOpeningHours(List<OpeningHour> hours) {
		if (hours == null || hours.isEmpty()) {
			throw new IllegalArgumentException("Informe ao menos um horário de funcionamento");
		}
		Set<Object> days = new HashSet<>();
		for (OpeningHour hour : hours) {
			if (hour.getDayOfWeek() == null || hour.getOpensAt() == null || hour.getClosesAt() == null) {
				throw new IllegalArgumentException("Horário incompleto: dayOfWeek, opensAt e closesAt são obrigatórios");
			}
			if (!days.add(hour.getDayOfWeek())) {
				throw new IllegalArgumentException("Dia duplicado no horário de funcionamento: " + hour.getDayOfWeek());
			}
			LocalTime opens = hour.getOpensAt();
			LocalTime closes = hour.getClosesAt();
			if (!opens.isBefore(closes)) {
				throw new IllegalArgumentException(
						"opensAt deve ser anterior a closesAt para " + hour.getDayOfWeek());
			}
		}
	}
}
