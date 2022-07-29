package com.example.cursospringboot.controller;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.cursospringboot.entity.User;
import com.example.cursospringboot.service.UserService;

@RestController
@RequestMapping("/api/users")
public class UserController {

	@Autowired
	private UserService userService;

	// Create a new user
	@PostMapping
	public ResponseEntity<?> create(@RequestBody User user) {

		return ResponseEntity.status(HttpStatus.CREATED).body(userService.save(user));

	}
	
	// Read an user
	@GetMapping("/{id}")
	public ResponseEntity<?> read(@PathVariable(value = "id") Long userId){
		
		Optional<User> opUser = userService.findById(userId);
		
		if (opUser.isPresent()) {
			
			return ResponseEntity.ok(opUser);
		}
		
		return ResponseEntity.notFound().build();
	}
	
	
	//Update an user
	@PutMapping("/{id}")
	public ResponseEntity<?> update(@RequestBody User userDetails, @PathVariable(value = "id") Long userId){
		
		Optional<User> opUser = userService.findById(userId);
		if (!opUser.isPresent()) {
			
			return ResponseEntity.notFound().build();	
			
		}
		
		//Propiedad de spring para copiar objetos de una entidad a otra
		//BeanUtils.copyProperties(userDetails, opUser.get());
		
		opUser.get().setName(userDetails.getName());
		opUser.get().setSurname(userDetails.getSurname());
		opUser.get().setEmail(userDetails.getEmail());
		opUser.get().setEnabled(userDetails.getEnabled());
		
		return ResponseEntity.status(HttpStatus.CREATED).body(userService.save(opUser.get()));
		
	}
	
	//Delete an user
	@DeleteMapping("/{id}")
	public ResponseEntity<?> delete (@PathVariable Long id){
		
		if (!userService.findById(id).isPresent()) {
			
			return ResponseEntity.notFound().build();
			
		}
		
		userService.deleteById(id);
		
		return ResponseEntity.ok().build();
	}
	
	//Read all users
	@GetMapping
	public List<User> readAll(){
		
		List<User> users = StreamSupport.stream(userService.findAll().spliterator(), false)
				.collect(Collectors.toList());
		
		return users;
	}

}
