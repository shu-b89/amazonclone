package com.example.demo.controller;

public class DuplicateEmailException extends RuntimeException {
	public DuplicateEmailException(String massage) {
		super(massage);
	}
}
