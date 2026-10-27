package com.api.restaurant.presentation.exception;

import com.api.restaurant.domain.exception.BusinessConflictException;
import com.api.restaurant.domain.exception.DomainException;
import com.api.restaurant.domain.exception.ResourceNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import static org.junit.jupiter.api.Assertions.assertEquals;

class GlobalExceptionHandlerTest {

	private final GlobalExceptionHandler handler = new GlobalExceptionHandler();

	@Test
	void mapsDomainExceptions() {
		ProblemDetail notFound = handler.handleNotFound(new ResourceNotFoundException("x"));
		assertEquals(HttpStatus.NOT_FOUND.value(), notFound.getStatus());

		ProblemDetail conflict = handler.handleConflict(new BusinessConflictException("y"));
		assertEquals(HttpStatus.CONFLICT.value(), conflict.getStatus());

		ProblemDetail bad = handler.handleBadRequest(new DomainException("z"));
		assertEquals(HttpStatus.BAD_REQUEST.value(), bad.getStatus());

		ProblemDetail integrity = handler.handleDataIntegrity(new DataIntegrityViolationException("dup"));
		assertEquals(HttpStatus.CONFLICT.value(), integrity.getStatus());
	}

	@Test
	void mapsValidationErrors() {
		BeanPropertyBindingResult binding = new BeanPropertyBindingResult(new Object(), "req");
		binding.addError(new FieldError("req", "name", "não deve estar em branco"));
		MethodArgumentNotValidException ex = new MethodArgumentNotValidException(null, binding);
		ProblemDetail problem = handler.handleValidation(ex);
		assertEquals(HttpStatus.BAD_REQUEST.value(), problem.getStatus());
		assertEquals(true, problem.getDetail().contains("name"));
	}
}
