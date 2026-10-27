package com.api.restaurant.presentation.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class ApiIntegrationTest {

	@Autowired
	private MockMvc mockMvc;

	private final ObjectMapper objectMapper = new ObjectMapper().findAndRegisterModules();

	@DynamicPropertySource
	static void props(DynamicPropertyRegistry registry) {
		registry.add("spring.datasource.url", () -> "jdbc:h2:mem:api_it;MODE=PostgreSQL;DB_CLOSE_DELAY=-1");
		registry.add("spring.datasource.username", () -> "sa");
		registry.add("spring.datasource.password", () -> "");
		registry.add("spring.datasource.driver-class-name", () -> "org.h2.Driver");
		registry.add("spring.jpa.hibernate.ddl-auto", () -> "create-drop");
		registry.add("spring.docker.compose.enabled", () -> "false");
	}

	@Test
	void fullHappyPathAndBusinessRules() throws Exception {
		MvcResult types = mockMvc.perform(get("/api/v1/user-types"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$", hasSize(org.hamcrest.Matchers.greaterThanOrEqualTo(3))))
				.andReturn();

		JsonNode typeArray = objectMapper.readTree(types.getResponse().getContentAsString());
		long staffTypeId = -1;
		long customerTypeId = -1;
		for (JsonNode node : typeArray) {
			if ("Dono".equals(node.get("name").asText())) {
				staffTypeId = node.get("id").asLong();
			}
			if ("Cliente".equals(node.get("name").asText())) {
				customerTypeId = node.get("id").asLong();
			}
		}

		String ownerBody = """
				{
				  "name": "Maria Dona",
				  "email": "maria@rest.com",
				  "login": "maria",
				  "password": "senha123",
				  "userTypeId": %d
				}
				""".formatted(staffTypeId);

		MvcResult ownerResult = mockMvc.perform(post("/api/v1/users")
						.contentType(MediaType.APPLICATION_JSON)
						.content(ownerBody))
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$.login").value("maria"))
				.andReturn();
		long ownerId = objectMapper.readTree(ownerResult.getResponse().getContentAsString()).get("id").asLong();

		String customerBody = """
				{
				  "name": "Joao Cliente",
				  "email": "joao@mail.com",
				  "login": "joao",
				  "password": "senha123",
				  "userTypeId": %d
				}
				""".formatted(customerTypeId);
		MvcResult customerResult = mockMvc.perform(post("/api/v1/users")
						.contentType(MediaType.APPLICATION_JSON)
						.content(customerBody))
				.andExpect(status().isCreated())
				.andReturn();
		long customerId = objectMapper.readTree(customerResult.getResponse().getContentAsString()).get("id").asLong();

		String restaurantAsCustomer = restaurantJson(customerId);
		mockMvc.perform(post("/api/v1/restaurants")
						.contentType(MediaType.APPLICATION_JSON)
						.content(restaurantAsCustomer))
				.andExpect(status().isConflict());

		MvcResult restaurantResult = mockMvc.perform(post("/api/v1/restaurants")
						.contentType(MediaType.APPLICATION_JSON)
						.content(restaurantJson(ownerId)))
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$.name").value("Cantina da Maria"))
				.andReturn();
		long restaurantId = objectMapper.readTree(restaurantResult.getResponse().getContentAsString()).get("id").asLong();

		mockMvc.perform(patch("/api/v1/users/{id}/user-type", ownerId)
						.contentType(MediaType.APPLICATION_JSON)
						.content("{\"userTypeId\": %d}".formatted(customerTypeId)))
				.andExpect(status().isConflict());

		mockMvc.perform(post("/api/v1/restaurants/{id}/menu-items", restaurantId)
						.contentType(MediaType.APPLICATION_JSON)
						.content("""
								{
								  "name": "Lasanha",
								  "description": "Bolonhesa",
								  "price": 39.90,
								  "dineInOnly": true,
								  "photoPath": "/photos/lasanha.jpg"
								}
								"""))
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$.dineInOnly").value(true));

		mockMvc.perform(get("/api/v1/restaurants/{id}/menu-items", restaurantId))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$", hasSize(1)));

		mockMvc.perform(delete("/api/v1/user-types/{id}", staffTypeId))
				.andExpect(status().isConflict());

		mockMvc.perform(delete("/api/v1/restaurants/{id}", restaurantId))
				.andExpect(status().isNoContent());

		mockMvc.perform(get("/api/v1/restaurants/{id}/menu-items", restaurantId))
				.andExpect(status().isNotFound());

		mockMvc.perform(put("/api/v1/users/{id}", ownerId)
						.contentType(MediaType.APPLICATION_JSON)
						.content("""
								{
								  "name": "Maria Atualizada",
								  "email": "maria2@rest.com",
								  "login": "maria2"
								}
								"""))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.name").value("Maria Atualizada"));

		mockMvc.perform(post("/api/v1/user-types")
						.contentType(MediaType.APPLICATION_JSON)
						.content("""
								{"name":"Gerente","category":"RESTAURANT_STAFF"}
								"""))
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$.name").value("Gerente"));
	}

	private static String restaurantJson(long ownerId) {
		return """
				{
				  "name": "Cantina da Maria",
				  "address": {
				    "zipCode": "01310-100",
				    "street": "Av Paulista",
				    "number": "1000",
				    "city": "São Paulo",
				    "state": "SP"
				  },
				  "cuisineType": "Italiana",
				  "openingHours": [
				    {"dayOfWeek":"MONDAY","opensAt":"11:00:00","closesAt":"22:00:00"}
				  ],
				  "ownerId": %d
				}
				""".formatted(ownerId);
	}
}
