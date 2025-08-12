package com.ase.userservice.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RootController {

	@GetMapping("/")
	public ResponseEntity<String> root() {
		return ResponseEntity.ok("API Root: /api/v1/");
	}

}
