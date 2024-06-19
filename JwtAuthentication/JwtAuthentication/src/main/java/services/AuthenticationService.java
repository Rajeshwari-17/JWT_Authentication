package services;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import dto.LoginUserdto;
import dto.RegisterUSerDto;
import entity.User;
import repositories.UserRepositories;


@Service
public class AuthenticationService {
	
	private UserRepositories userrepository;
	private PasswordEncoder passwordEncoder;
	private AuthenticationManager authenticationManager;
	
	
	public AuthenticationService(UserRepositories userrepository, PasswordEncoder passwordEncoder,
			AuthenticationManager authenticationManager) {
		super();
		this.userrepository = userrepository;
		this.passwordEncoder = passwordEncoder;
		this.authenticationManager = authenticationManager;
	}
	
	public User signup(RegisterUSerDto input)
	{
        User user = new User()
                .setFullname(input.getFullname())
                .setemail(input.getemail())
                .setPassword(passwordEncoder.encode(input.getPassword()));

		
		return userrepository.save(user);
	}
    public User authenticate(LoginUserdto input) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        input.getEmail(),
                        input.getPassword()
                )
        );

        return userrepository.findByEmail(input.getEmail())
                .orElseThrow();
    }

	
	
	

}
