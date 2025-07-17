package com.demo.bankapp.controller;

import java.util.List;
import java.util.regex.Pattern;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.demo.bankapp.configuration.Constants;
import com.demo.bankapp.exception.BadCredentialsException;
import com.demo.bankapp.exception.BadRequestException;
import com.demo.bankapp.model.User;
import com.demo.bankapp.request.CreateUserRequest;
import com.demo.bankapp.response.CreateUserResponse;
import com.demo.bankapp.response.FindAllUsersResponse;
import com.demo.bankapp.service.abstractions.UserService;
import com.demo.bankapp.service.abstractions.WealthService;

@RestController
@RequestMapping(value = "/user", produces = { MediaType.APPLICATION_JSON_VALUE })
@RequiredArgsConstructor
public class UserController {

	private final UserService userService;
	private final WealthService wealthService;

//	@Autowired
//	public UserController(UserService userService, WealthService wealthService) {
//		this.userService = userService;
//		this.wealthService = wealthService;
//	}

	@GetMapping("/find/all")
	public FindAllUsersResponse findAll() {
		List<User> userList = userService.findAll();
		
		FindAllUsersResponse response = new FindAllUsersResponse();
		response.setUserList(userList);
		return response;
	}

	@PostMapping("/create")
	public CreateUserResponse createUser(@RequestBody CreateUserRequest request) {

		if (request.getUsername() == null || request.getUsername().equals("")) {
			throw new BadRequestException(Constants.MESSAGE_INVALIDUSERNAME);
		}
		
		if (request.getPassword() == null || request.getPassword().equals("")) {
			throw new BadRequestException(Constants.MESSAGE_INVALIDPASSWORD);
		}

		if (request.getTcno() == null || request.getTcno().length() != 11 || !Pattern.matches("[0-9]+", request.getTcno())) {
			throw new BadRequestException(Constants.MESSAGE_INVALIDTCNO);
		}

		boolean isUsernameExist = userService.isUsernameExist(request.getUsername());
		if (isUsernameExist) {
			throw new BadCredentialsException(Constants.MESSAGE_SAMEUSERNAMEEXIST);
		}

		boolean isTcnoExist = userService.isTcnoExist(request.getTcno());
		if (isTcnoExist) {
			throw new BadCredentialsException(Constants.MESSAGE_SAMETCNOEXIST);
		}

		User user = userService.createNewUser(new User(request.getUsername(), request.getPassword(), request.getTcno()));
		wealthService.newWealthRecord(user.getId());

		CreateUserResponse response = new CreateUserResponse();
		response.setUsername(user.getUsername());
		response.setTcno(user.getTcno());
		return response;
	}

}
