package services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import entity.User;
import repositories.UserRepositories;

@Service
public class UserService {
	
	private UserRepositories userRepository;
	
	public List<User> allUsers()
	{
		List<User> users = new ArrayList<>();
		userRepository.findAll().forEach(users :: add);
		return users;
	}

}
