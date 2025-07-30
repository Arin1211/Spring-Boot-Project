package com.demo.bankapp.exception.configuration;

import java.util.ArrayList;
import java.util.List;
import jakarta.validation.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.demo.bankapp.exception.BadCredentialsException;
import com.demo.bankapp.exception.BadRequestException;
import com.demo.bankapp.exception.DailyOperationLimitReachedException;
import com.demo.bankapp.exception.InsufficientFundsException;
import com.demo.bankapp.exception.TransactionLimitException;
import com.demo.bankapp.exception.UserNotFoundException;

@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

	@Override
	protected ResponseEntity<Object> handleNoHandlerFoundException(
			NoHandlerFoundException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
		final String error = "No handler found for " + ex.getHttpMethod() + " " + ex.getRequestURL();

		final ApiError apiError = new ApiError(HttpStatus.NOT_FOUND, ex.getLocalizedMessage(), error);
		return buildResponseEntity(ex, apiError);
	}

	@Override
	protected ResponseEntity<Object> handleHttpRequestMethodNotSupported(
			HttpRequestMethodNotSupportedException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
		final StringBuilder builder = new StringBuilder();
		builder.append(ex.getMethod());
		builder.append(" method is not supported for this request. Supported methods are ");
		ex.getSupportedHttpMethods().forEach(t -> builder.append(t + " "));

		final ApiError apiError = new ApiError(HttpStatus.METHOD_NOT_ALLOWED, ex.getLocalizedMessage(), builder.toString());
		return buildResponseEntity(ex, apiError);
	}

	@ExceptionHandler({ MethodArgumentTypeMismatchException.class })
	public ResponseEntity<Object> handleMethodArgumentTypeMismatch(
			final MethodArgumentTypeMismatchException ex, final WebRequest request) {
		final String error = ex.getName() + " should be of type " + ex.getRequiredType().getName();
		final ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST, ex.getLocalizedMessage(), error);
		return buildResponseEntity(ex, apiError);
	}

	@ExceptionHandler({ ConstraintViolationException.class })
	public ResponseEntity<Object> handleConstraintViolation(
			final ConstraintViolationException ex, final WebRequest request) {
		final List<String> errors = new ArrayList<>();
		ex.getConstraintViolations().forEach(violation -> {
			errors.add(violation.getRootBeanClass().getName() + " " +
					violation.getPropertyPath() + ": " + violation.getMessage());
		});

		final ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST, ex.getLocalizedMessage(), errors);
		return buildResponseEntity(ex, apiError);
	}

	// Handle Hibernate constraint violations (database level)
	@ExceptionHandler({ DataIntegrityViolationException.class })
	public ResponseEntity<Object> handleDataIntegrityViolation(
			final DataIntegrityViolationException ex, final WebRequest request) {
		String error = "Database constraint violation";
		if (ex.getCause() instanceof org.hibernate.exception.ConstraintViolationException) {
			org.hibernate.exception.ConstraintViolationException constraintEx =
					(org.hibernate.exception.ConstraintViolationException) ex.getCause();
			if (constraintEx.getConstraintName() != null) {
				error = "Constraint violation: " + constraintEx.getConstraintName();
			}
		}

		final ApiError apiError = new ApiError(HttpStatus.CONFLICT, ex.getLocalizedMessage(), error);
		return buildResponseEntity(ex, apiError);
	}

	// Custom Exception Handlers

	@ExceptionHandler({ BadCredentialsException.class })
	public ResponseEntity<Object> handleBadCredentials(
			final BadCredentialsException ex, final WebRequest request) {
		final ApiError apiError = new ApiError(HttpStatus.CONFLICT, ex.getLocalizedMessage(), "error occurred");
		return buildResponseEntity(ex, apiError);
	}

	@ExceptionHandler({ BadRequestException.class })
	public ResponseEntity<Object> handleBadRequest(
			final BadRequestException ex, final WebRequest request) {
		final ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST, ex.getLocalizedMessage(), "error occurred");
		return buildResponseEntity(ex, apiError);
	}

	@ExceptionHandler({ UserNotFoundException.class })
	public ResponseEntity<Object> handleUserNotFound(
			final UserNotFoundException ex, final WebRequest request) {
		final ApiError apiError = new ApiError(HttpStatus.NOT_FOUND, ex.getLocalizedMessage(), "error occurred");
		return buildResponseEntity(ex, apiError);
	}

	@ExceptionHandler({ InsufficientFundsException.class })
	public ResponseEntity<Object> handleInsufficientFunds(
			final InsufficientFundsException ex, final WebRequest request) {
		final ApiError apiError = new ApiError(HttpStatus.UNAUTHORIZED, ex.getLocalizedMessage(), "error occurred");
		return buildResponseEntity(ex, apiError);
	}

	@ExceptionHandler({ TransactionLimitException.class })
	public ResponseEntity<Object> handleTransactionLimit(
			final TransactionLimitException ex, final WebRequest request) {
		final ApiError apiError = new ApiError(HttpStatus.UNAUTHORIZED, ex.getLocalizedMessage(), "error occurred");
		return buildResponseEntity(ex, apiError);
	}

	@ExceptionHandler({ DailyOperationLimitReachedException.class })
	public ResponseEntity<Object> handleDailyOperationLimit(
			final DailyOperationLimitReachedException ex, final WebRequest request) {
		final ApiError apiError = new ApiError(HttpStatus.UNAUTHORIZED, ex.getLocalizedMessage(), "error occurred");
		return buildResponseEntity(ex, apiError);
	}

	private ResponseEntity<Object> buildResponseEntity(Exception ex, ApiError apiError) {
		logger.info(ex.getClass().getName());
		logger.error("error", ex);

		return new ResponseEntity<>(apiError, new HttpHeaders(), apiError.getStatus());
	}
}