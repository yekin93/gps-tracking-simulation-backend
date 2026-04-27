package com.gps.gps_tracking_simulation_backend.exception;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(DeviceNotFoundException.class)
	public ResponseEntity<Map<String, Object>> handleDeviceNotFound(DeviceNotFoundException ex){
		return ResponseEntity
				.status(HttpStatus.NOT_FOUND)
				.body(Map.of("message", ex.getMessage(),
							 "status", HttpStatus.NOT_FOUND.value(),
							 "error", "Not Found"));
	}
	
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<Map<String, Object>> handleMethodArgumentNotValid(MethodArgumentNotValidException ex) {
		Map<String, String> errors = new HashMap<>();
		
		for(FieldError err : ex.getBindingResult().getFieldErrors()) {
			errors.put(err.getField(), err.getDefaultMessage());
		}
		
		return ResponseEntity
				.status(HttpStatus.BAD_REQUEST)
				.body(Map.of("status", HttpStatus.BAD_REQUEST.value(),
							 "errors", errors,
							 "error", "Bad Request"));
	}
}
