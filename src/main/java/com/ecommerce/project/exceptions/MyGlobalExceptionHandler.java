package com.ecommerce.project.exceptions;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class MyGlobalExceptionHandler {

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<Map<String, String>> myMethodArgumentNotValidException(MethodArgumentNotValidException e) {
		Map<String, String> response = new HashMap<String, String>();
		e.getBindingResult().getAllErrors().forEach(err -> {
			String feildName = ((FieldError) err).getField();
			String message = err.getDefaultMessage();
			response.put(feildName, message);
		});
		return new ResponseEntity<Map<String, String>>(response, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(ResouceNotFoundException.class)
	public ResponseEntity<String> myResourceNotFoundException(ResouceNotFoundException e) {
		String message = e.getMessage();
		return new ResponseEntity<>(message, HttpStatus.NOT_FOUND);
	}
	
	@ExceptionHandler(APIException.class)
	public ResponseEntity<String> myAPIException(APIException e) {
		String message = e.getMessage(); 
		return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);
	}

}
