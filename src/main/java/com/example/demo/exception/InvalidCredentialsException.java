package com.example.demo.exception;

public class InvalidCredentialsException extends RuntimeException{
	public InvalidCredentialsException(String massage) {
		super(massage);
	}
}
