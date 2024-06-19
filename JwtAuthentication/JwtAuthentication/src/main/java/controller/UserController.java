package controller;


import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import entity.User;
import services.UserService;


@RequestMapping("/user")
@RestController
public class UserController {
	
	private UserService userService;
	
	
	@GetMapping("/me")
	public ResponseEntity<User> authenticatedUser()
	{
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		User currentuser = (User) authentication.getPrincipal();
		return ResponseEntity.ok(currentuser);
				
	}
	
	@GetMapping("/")
	public ResponseEntity<List<User>> allUsers()
	{
		List<User> users = userService.allUsers();
		return ResponseEntity.ok(users);
	}
	
}
