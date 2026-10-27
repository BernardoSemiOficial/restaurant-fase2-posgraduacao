package com.api.restaurant.presentation.exception;

import com.api.restaurant.domain.exception.BusinessConflictException;
import com.api.restaurant.domain.exception.DomainException;
import com.api.restaurant.domain.exception.ResourceNotFoundException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.net.URI;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

	private static final String URI_BASE = "https://api.restaurant.com/errors/";

	@ExceptionHandler(ResourceNotFoundException.class)
	public ProblemDetail handleNotFound(ResourceNotFoundException ex) {
		ProblemDetail problem = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, ex.getMessage());
		problem.setTitle("Recurso não encontrado");
		problem.setType(URI.create(URI_BASE + "not-found"));
		return problem;
	}

	@ExceptionHandler(BusinessConflictException.class)
	public ProblemDetail handleConflict(BusinessConflictException ex) {
		ProblemDetail problem = ProblemDetail.forStatusAndDetail(HttpStatus.CONFLICT, ex.getMessage());
		problem.setTitle("Conflito de negócio");
		problem.setType(URI.create(URI_BASE + "conflict"));
		return problem;
	}

	@ExceptionHandler({IllegalArgumentException.class, DomainException.class})
	public ProblemDetail handleBadRequest(RuntimeException ex) {
		ProblemDetail problem = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, ex.getMessage());
		problem.setTitle("Requisição inválida");
		problem.setType(URI.create(URI_BASE + "bad-request"));
		return problem;
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ProblemDetail handleValidation(MethodArgumentNotValidException ex) {
		String detail = ex.getBindingResult().getFieldErrors().stream()
				.map(this::formatFieldError)
				.collect(Collectors.joining("; "));
		ProblemDetail problem = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, detail);
		problem.setTitle("Validação falhou");
		problem.setType(URI.create(URI_BASE + "validation"));
		return problem;
	}

	@ExceptionHandler(DataIntegrityViolationException.class)
	public ProblemDetail handleDataIntegrity(DataIntegrityViolationException ex) {
		ProblemDetail problem = ProblemDetail.forStatusAndDetail(
				HttpStatus.CONFLICT,
				"Violação de integridade de dados (valor duplicado ou referência inválida)");
		problem.setTitle("Conflito de dados");
		problem.setType(URI.create(URI_BASE + "data-integrity"));
		return problem;
	}

	private String formatFieldError(FieldError error) {
		return error.getField() + ": " + error.getDefaultMessage();
	}
}
