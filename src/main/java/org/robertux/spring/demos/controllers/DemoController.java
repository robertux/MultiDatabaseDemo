package org.robertux.spring.demos.controllers;

import java.util.List;

import org.robertux.spring.demos.data.roles.Rol;
import org.robertux.spring.demos.data.roles.RolRepository;
import org.robertux.spring.demos.data.users.User;
import org.robertux.spring.demos.data.users.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController()
public class DemoController {

	@Autowired
	private RolRepository rolesRepo;
	
	@Autowired
	private UserRepository usersRepo;
	
	@GetMapping("/roles")
	public ResponseEntity<List<Rol>> getRoles() {
		return ResponseEntity.ok(rolesRepo.findAll());
	}
	
	@GetMapping("/users")
	public ResponseEntity<List<User>> getUsers() {
		return ResponseEntity.ok(usersRepo.findAll());
	}
}
