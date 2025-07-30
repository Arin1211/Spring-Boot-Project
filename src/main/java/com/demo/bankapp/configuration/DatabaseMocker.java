package com.demo.bankapp.configuration;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;

import com.demo.bankapp.controller.UserController;
import com.demo.bankapp.repository.UserRepository;
import com.demo.bankapp.request.CreateUserRequest;

@Configuration
class DatabaseMocker {

	@Bean
	@Order(Ordered.HIGHEST_PRECEDENCE)
	CommandLineRunner initDatabase(UserRepository repository, UserController userController) {
		return args -> {
			createUserIfNotExists(repository, userController, "Mert", "Mert12345", "11223344550");
			createUserIfNotExists(repository, userController, "Mert2", "Mert12345", "10000000078");
			createUserIfNotExists(repository, userController, "Mert3", "Mert12345", "10000000146");
		};
	}

	private void createUserIfNotExists(UserRepository repository, UserController controller, String username, String password, String tcno) {
		if (!repository.existsByUsername(username)) {
			CreateUserRequest request = new CreateUserRequest();
			request.setUsername(username);
			request.setPassword(password);
			request.setTcno(tcno);

			try {
				controller.createUser(request);
			} catch (Exception e) {
				System.out.println("Warning: Failed to create user " + username + " – " + e.getMessage());
			}
		} else {
			System.out.println("User already exists: " + username);
		}
	}
}
