package com.example.demo.controller;

public class InvalidCredentialsException extends RuntimeException{
	public InvalidCredentialsException(String massage) {
		super(massage);
	}
}
