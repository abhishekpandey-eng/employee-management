package com.employeemanagement.controllers;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import com.employeemanagement.dto.ApiError;
import com.employeemanagement.exceptions.InvalidInputException;
import com.employeemanagement.exceptions.NotFoundException;


@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

	@ExceptionHandler(InvalidInputException.class)
	protected ResponseEntity<Object> handle400(InvalidInputException ex) {
		ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST, ex.getMessage(), ex);
		return buildResponseEntity(apiError);
	}
	
	@ExceptionHandler(NotFoundException.class)
	protected ResponseEntity<Object> handle404(NotFoundException ex) {
		ApiError apiError = new ApiError(HttpStatus.NOT_FOUND, ex.getMessage(), ex);
		return buildResponseEntity(apiError);
	}
	
	@ExceptionHandler(Exception.class)
	protected ResponseEntity<Object> handle500(Exception ex) {
		ApiError apiError = new ApiError(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage(), ex);
		return buildResponseEntity(apiError);
	}

	private ResponseEntity<Object> buildResponseEntity(ApiError apiError) {
		return new ResponseEntity<>(apiError, apiError.getStatus());
	}
}